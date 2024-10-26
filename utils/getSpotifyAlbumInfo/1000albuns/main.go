package main

import (
	"database/sql"
	"encoding/json"
	"log"
	"os"
	"strings"
	"time"

	_ "github.com/mattn/go-sqlite3"
)

type Album struct {
	Name        string   `json:"name"`
	Artist      string   `json:"artist"`
	CoverURL    string   `json:"cover_url"`
	ReleaseDate string   `json:"release_date"`
	Genres      []string `json:"genres"`
	SpotifyID   string   `json:"spotify_id"`
}

func main() {
	jsonData, err := os.ReadFile("albums.json")
	if err != nil {
		log.Fatalf("Error reading JSON file: %v", err)
	}

	var albums []Album
	if err := json.Unmarshal(jsonData, &albums); err != nil {
		log.Fatalf("Error parsing JSON: %v", err)
	}

	db, err := sql.Open("sqlite3", "../../../goodlisteners.db")
	if err != nil {
		log.Fatalf("Error opening database: %v", err)
	}
	defer db.Close()

	stmt, err := db.Prepare(`
		INSERT INTO Albums (name, artist, genre, year, cover_url, spotify_id)
		VALUES (?, ?, ?, ?, ?, ?)
	`)
	if err != nil {
		log.Fatalf("Error preparing statement: %v", err)
	}
	defer stmt.Close()

	for _, album := range albums {
		var year int
		if t, err := time.Parse("2006-01-02", album.ReleaseDate); err == nil {
			year = t.Year()
		}

		genre := strings.Join(album.Genres, ", ")

		_, err = stmt.Exec(
			album.Name,
			album.Artist,
			genre,
			year,
			album.CoverURL,
			album.SpotifyID,
		)
		if err != nil {
			log.Printf("Error inserting album %s: %v", album.Name, err)
			continue
		}
	}

	log.Println("Import completed successfully")
}
