import requests
import time
from typing import List, Dict, Any, Optional
from datetime import datetime
import os
from dotenv import load_dotenv
import logging
from dataclasses import dataclass
from abc import ABC, abstractmethod

@dataclass
class Album:
    name: str
    artist: str
    cover_url: str
    release_date: str
    genres: List[str]
    spotify_id: Optional[str] = None
    musicbrainz_id: Optional[str] = None

class MusicAPIClient(ABC):
    @abstractmethod
    def get_albums(self, limit: int, offset: int) -> List[Album]:
        pass

class SpotifyClient(MusicAPIClient):
    def __init__(self):
        load_dotenv()
        self.client_id = os.getenv('SPOTIFY_CLIENT_ID')
        self.client_secret = os.getenv('SPOTIFY_CLIENT_SECRET')
        self.token = self._get_access_token()
        
    def _get_access_token(self) -> str:
        auth_url = 'https://accounts.spotify.com/api/token'
        auth_response = requests.post(auth_url, {
            'grant_type': 'client_credentials',
            'client_id': self.client_id,
            'client_secret': self.client_secret,
        })
        
        if auth_response.status_code != 200:
            raise Exception('Failed to get Spotify access token')
            
        return auth_response.json()['access_token']
    
    def _make_request(self, url: str) -> Dict:
        headers = {'Authorization': f'Bearer {self.token}'}
        response = requests.get(url, headers=headers)
        
        if response.status_code == 429:
            retry_after = int(response.headers.get('Retry-After', 1))
            time.sleep(retry_after)
            return self._make_request(url)
            
        if response.status_code != 200:
            raise Exception(f'Spotify API request failed: {response.status_code}')
            
        return response.json()
    
    def get_albums(self, limit: int = 50, offset: int = 0) -> List[Album]:
        """
        Get albums using Spotify's browse/new-releases and recommendations endpoints
        This provides a good mix of popular and diverse albums
        """
        albums = []
        
        url = f'https://api.spotify.com/v1/browse/new-releases?limit={min(limit, 50)}&offset={offset}'
        response = self._make_request(url)
        
        for item in response['albums']['items']:
            albums.append(Album(
                name=item['name'],
                artist=', '.join(artist['name'] for artist in item['artists']),
                cover_url=item['images'][0]['url'] if item['images'] else '',
                release_date=item['release_date'],
                genres=[],
                spotify_id=item['id']
            ))
            
        if len(albums) < limit:
            seed_genres = ['pop', 'rock', 'hip-hop', 'classical', 'jazz']
            for genre in seed_genres:
                url = f'https://api.spotify.com/v1/recommendations?seed_genres={genre}&limit={20}'
                response = self._make_request(url)
                
                for track in response['tracks']:
                    if len(albums) >= limit:
                        break
                        
                    album = track['album']
                    if not any(a.spotify_id == album['id'] for a in albums):  # Isso evita albuns duplicados!!!!!!
                        albums.append(Album(
                            name=album['name'],
                            artist=', '.join(artist['name'] for artist in album['artists']),
                            cover_url=album['images'][0]['url'] if album['images'] else '',
                            release_date=album['release_date'],
                            genres=[],
                            spotify_id=album['id']
                        ))
        
        for album in albums:
            if album.spotify_id:
                try:
                    url = f'https://api.spotify.com/v1/albums/{album.spotify_id}'
                    response = self._make_request(url)
                    album.genres = response.get('genres', [])
                    time.sleep(0.1)
                except Exception as e:
                    logging.error(f"Error fetching genres for album {album.name}: {str(e)}")
        
        return albums[:limit]


def get_diverse_album_collection(total_albums: int = 1000) -> List[Album]:
    """
    Get a diverse collection of albums using multiple sources
    """
    spotify = SpotifyClient()
    
    albums = []
    batch_size = 50
    
    sources = [
        (spotify, 1),
    ]
    
    for client, proportion in sources:
        target_count = int(total_albums * proportion)
        offset = 0
        
        while len(albums) < target_count:
            try:
                batch = client.get_albums(limit=min(batch_size, target_count - len(albums)), offset=offset)
                if not batch:
                    break
                    
                albums.extend(batch)
                offset += batch_size
                print(f"Fetched {len(albums)} albums so far...")
                
            except Exception as e:
                logging.error(f"Error fetching batch from {client.__class__.__name__}: {str(e)}")
                break
    
    return albums[:total_albums]

def save_albums_to_json(albums: List[Album], filename: str = 'albums.json'):
    """Save album data to JSON file"""
    import json
    from dataclasses import asdict
    
    with open(filename, 'w', encoding='utf-8') as f:
        json.dump([asdict(album) for album in albums], f, indent=2, ensure_ascii=False)

def main():
    logging.basicConfig(level=logging.INFO)
    
    try:
        albums = get_diverse_album_collection(1000)
        save_albums_to_json(albums)
        print(f"Successfully saved {len(albums)} albums to albums.json")
        
    except Exception as e:
        logging.error(f"An error occurred: {str(e)}")

if __name__ == "__main__":
    main()