package com.goodlisteners.model;

public class Album {
    private int albumId;
    private String name;
    private String artist;
    private String genre;
    private int year;
    private int length;
    private String coverUrl;
    private double averageScore;
    private int numberFavorites;
    private String spotifyId;

    public int getAlbumId() { return albumId; }
    public void setAlbumId(int albumId) { this.albumId = albumId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getArtist() { return artist; }
    public void setArtist(String artist) { this.artist = artist; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public int getLength() { return length; }
    public void setLength(int length) { this.length = length; }

    public String getCoverUrl() { return coverUrl; }
    public void setCoverUrl(String coverUrl) { this.coverUrl = coverUrl; }

    public double getAverageScore() { return averageScore; }
    public void setAverageScore(double averageScore) { this.averageScore = averageScore; }

    public int getNumberFavorites() { return numberFavorites; }
    public void setNumberFavorites(int numberFavorites) { this.numberFavorites = numberFavorites; }

    public String getSpotifyId() { return spotifyId; }
    public void setSpotifyId(String spotifyId) { this.spotifyId = spotifyId; }
}