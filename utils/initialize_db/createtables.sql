CREATE TABLE IF NOT EXISTS User (
    user_id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    password TEXT NOT NULL);

CREATE TABLE IF NOT EXISTS Albums ( 
    album_id INTEGER PRIMARY KEY AUTOINCREMENT, 
    name TEXT NOT NULL, 
    artist TEXT NOT NULL, 
    genre TEXT,
    year INTEGER, 
    length INTEGER, 
    cover_url TEXT, 
    average_score REAL DEFAULT 0.0, 
    number_favorites INTEGER DEFAULT 0,
    spotify_id TEXT,);

CREATE TABLE IF NOT EXISTS Reviews ( 
    reviews_id INTEGER PRIMARY KEY AUTOINCREMENT, 
    user_id INTEGER, 
    album_id INTEGER, 
    rating INTEGER CHECK (rating >= 0 AND rating <= 100), 
    FOREIGN KEY (user_id) REFERENCES User(user_id), 
    FOREIGN KEY (album_id) REFERENCES Albums(album_id));;