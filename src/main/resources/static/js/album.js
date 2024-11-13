// Determine API URL based on environment
const API_URL = window.location.hostname === 'localhost' 
    ? 'http://localhost:8080'
    : 'https://goodlisteners.onrender.com';

async function loadAlbumDetails(albumId) {
    try {
        const response = await fetch(`${API_URL}/api/albums/${albumId}`);
        if (!response.ok) throw new Error('Failed to fetch album details');
        const albumData = await response.json();

        // Update page with album details
        document.title = albumData.name;
        document.getElementById('albumTitle').textContent = `${albumData.name}\n${albumData.artist}`;
        document.getElementById('albumCover').src = albumData.coverUrl;
        document.getElementById('albumName').textContent = albumData.name;
        document.getElementById('releaseYear').textContent = albumData.releaseYear;
        document.getElementById('albumGenre').textContent = albumData.genre;
        document.getElementById('albumLength').textContent = albumData.length;
    } catch (error) {
        console.error('Error loading album details:', error);
        throw error;
    }
}

async function loadAlbumReviews(albumId) {
    try {
        const reviewsResponse = await fetch(`${API_URL}/api/albums/${albumId}/reviews`);
        if (!reviewsResponse.ok) throw new Error('Failed to fetch reviews');
        
        const reviewsData = await reviewsResponse.json();
        const reviewsContainer = document.getElementById('reviewsContainer');
        
        reviewsContainer.innerHTML = ''; // Clear existing reviews
        
        reviewsData.forEach(review => {
            const reviewElement = document.createElement('div');
            reviewElement.className = 'review';
            reviewElement.innerHTML = `
                <div class="album-cover">
                    <img src="${review.userProfilePic}" alt="Profile picture">
                </div>
                <div class="album-info">
                    <a class="album-title" href="profile.html?id=${review.userId}">${review.userName}</a>
                    <p class="album-rating">Nota: ${review.rating}</p>
                </div>
            `;
            reviewsContainer.appendChild(reviewElement);
        });

        // Update average score if available
        if (reviewsData.length > 0) {
            const avgScore = reviewsData.reduce((acc, rev) => acc + rev.rating, 0) / reviewsData.length;
            document.getElementById('averageScore').textContent = avgScore.toFixed(1);
            document.getElementById('scoreBar').style.width = `${avgScore}%`;
        }
    } catch (error) {
        console.error('Error loading reviews:', error);
        throw error;
    }
}

async function submitRating(albumId, rating) {
    const userId = 1; // Replace with actual user ID from authentication
    
    try {
        const response = await fetch(`${API_URL}/api/review`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            },
            body: JSON.stringify({
                userId,
                albumId,
                rating
            }),
            signal: AbortSignal.timeout(5000)
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        document.getElementById('savedRating').textContent = `Rating: ${rating}`;
        alert('Avaliação salva com sucesso!');
        // Reload reviews to show the new rating
        await loadAlbumReviews(albumId);
    } catch (error) {
        console.error('Error submitting rating:', error);
        throw error;
    }
}

function initializeAlbumPage() {
    const urlParams = new URLSearchParams(window.location.search);
    const albumId = urlParams.get('id');

    if (!albumId) {
        alert('No album ID provided');
        window.location.href = 'home.html';
        return;
    }

    // Load initial data
    Promise.all([
        loadAlbumDetails(albumId),
        loadAlbumReviews(albumId)
    ]).catch(error => {
        console.error('Error initializing page:', error);
        alert('Error loading album information');
        window.location.href = 'home.html';
    });

    // Set up rating submission handler
    const saveRatingButton = document.getElementById('saveRatingButton');
    const albumRatingInput = document.getElementById('albumRating');

    saveRatingButton.addEventListener('click', async () => {
        const rating = parseInt(albumRatingInput.value);
        
        if (isNaN(rating) || rating < 0 || rating > 100) {
            alert('Por favor, insira uma nota válida entre 0 e 100.');
            return;
        }

        try {
            await submitRating(albumId, rating);
        } catch (error) {
            alert(`Erro ao salvar a avaliação: ${error.message}`);
        }
    });
}

// Initialize when DOM is loaded
document.addEventListener('DOMContentLoaded', initializeAlbumPage);