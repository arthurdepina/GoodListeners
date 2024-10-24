package main

import (
	"bytes"
	"encoding/base64"
	"encoding/json"
	"flag"
	"fmt"
	"io"
	"log"
	"net/http"
	"os"
	"path/filepath"
	"time"

	"github.com/joho/godotenv"
)

type TokenResponse struct {
	AccessToken string `json:"access_token"`
	TokenType   string `json:"token_type"`
	ExpiresIn   int    `json:"expires_in"`
}

type Artist struct {
	Name       string `json:"name"`
	ID         string `json:"id"`
	Popularity int    `json:"popularity"`
	Followers  struct {
		Total int `json:"total"`
	} `json:"followers"`
}

type SpotifyClient struct {
	ClientID     string
	ClientSecret string
	AccessToken  string
	HTTPClient   *http.Client
}

func NewSpotifyClient(clientID, clientSecret string) *SpotifyClient {
	return &SpotifyClient{
		ClientID:     clientID,
		ClientSecret: clientSecret,
		HTTPClient: &http.Client{
			Timeout: 10 * time.Second,
		},
	}
}

func (c *SpotifyClient) GetAccessToken() error {
	auth := base64.StdEncoding.EncodeToString([]byte(c.ClientID + ":" + c.ClientSecret))
	data := bytes.NewBuffer([]byte("grant_type=client_credentials"))

	req, err := http.NewRequest("POST", "https://accounts.spotify.com/api/token", data)
	if err != nil {
		return fmt.Errorf("error creating token request: %w", err)
	}

	req.Header.Set("Authorization", "Basic "+auth)
	req.Header.Set("Content-Type", "application/x-www-form-urlencoded")

	resp, err := c.HTTPClient.Do(req)
	if err != nil {
		return fmt.Errorf("error making token request: %w", err)
	}
	defer resp.Body.Close()

	body, err := io.ReadAll(resp.Body)
	if err != nil {
		return fmt.Errorf("error reading token response: %w", err)
	}

	if resp.StatusCode != http.StatusOK {
		return fmt.Errorf("error getting access token: %s", string(body))
	}

	var tokenResp TokenResponse
	if err := json.Unmarshal(body, &tokenResp); err != nil {
		return fmt.Errorf("error parsing token response: %w", err)
	}

	c.AccessToken = tokenResp.AccessToken
	return nil
}

func (c *SpotifyClient) TestAPIAccess() (*Artist, error) {
	req, err := http.NewRequest("GET", "https://api.spotify.com/v1/artists/7sOR7gk6XUlGnxj3p9F54k", nil)
	if err != nil {
		return nil, fmt.Errorf("error creating API request: %w", err)
	}

	req.Header.Set("Authorization", "Bearer "+c.AccessToken)

	resp, err := c.HTTPClient.Do(req)
	if err != nil {
		return nil, fmt.Errorf("error making API request: %w", err)
	}
	defer resp.Body.Close()

	body, err := io.ReadAll(resp.Body)
	if err != nil {
		return nil, fmt.Errorf("error reading API response: %w", err)
	}

	if resp.StatusCode != http.StatusOK {
		return nil, fmt.Errorf("API request failed: %s", string(body))
	}

	var artist Artist
	if err := json.Unmarshal(body, &artist); err != nil {
		return nil, fmt.Errorf("error parsing API response: %w", err)
	}

	return &artist, nil
}

func loadEnvFile(envPath string) error {
	if envPath != "" {
		absPath, err := filepath.Abs(envPath)
		if err != nil {
			return fmt.Errorf("error resolving env file path: %w", err)
		}

		if _, err := os.Stat(absPath); os.IsNotExist(err) {
			return fmt.Errorf("env file not found at: %s", absPath)
		}

		return godotenv.Load(absPath)
	}

	locations := []string{
		".env",
		"../../.env",
		"../../../.env",
		filepath.Join("..", ".env"),
	}

	for _, loc := range locations {
		if _, err := os.Stat(loc); err == nil {
			absPath, _ := filepath.Abs(loc)
			if err := godotenv.Load(loc); err == nil {
				fmt.Printf("Loaded env file from: %s\n", absPath)
				return nil
			}
		}
	}

	return fmt.Errorf("no .env file found in default locations")
}

func main() {
	envPath := flag.String("env", "", "Path to .env file (relative or absolute)")
	flag.Parse()

	if err := loadEnvFile(*envPath); err != nil {
		log.Fatal("Error loading .env file:", err)
	}

	clientID := os.Getenv("SPOTIFY_CLIENT_ID")
	clientSecret := os.Getenv("SPOTIFY_CLIENT_SECRET")

	if clientID == "" || clientSecret == "" {
		log.Fatal("Missing Spotify credentials in .env file")
	}

	client := NewSpotifyClient(clientID, clientSecret)

	fmt.Println("Getting access token...")
	if err := client.GetAccessToken(); err != nil {
		log.Fatal("Error getting access token:", err)
	}
	fmt.Println("Successfully obtained access token!")

	fmt.Println("\nTesting API access...")
	artist, err := client.TestAPIAccess()
	if err != nil {
		log.Fatal("Error testing API access:", err)
	}

	fmt.Printf("\nAPI Test Successful!\n")
	fmt.Printf("Retrieved artist: %s\n", artist.Name)
	fmt.Printf("Popularity: %d\n", artist.Popularity)
	fmt.Printf("Followers: %d\n", artist.Followers.Total)
}
