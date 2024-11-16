const API_URL = window.location.hostname === 'localhost' 
    ? 'http://localhost:8080'
    : 'https://goodlisteners.onrender.com';

async function loadAlbumDetails(albumId) {
    try {
        console.log('Fetching album:', `${API_URL}/api/albums/${albumId}`);
        const response = await fetch(`${API_URL}/api/albums/${albumId}`);
        
        if (!response.ok) {
            console.error('Response not ok:', response.status);
            throw new Error('Failed to fetch album details');
        }
        
        const albumData = await response.json();
        console.log('Album data received:', albumData);

        document.title = albumData.name;
        document.getElementById('albumTitle').textContent = `${albumData.name}\n${albumData.artist}`;
        document.getElementById('albumCover').src = albumData.coverUrl;
        document.getElementById('albumName').textContent = albumData.name;
        document.getElementById('releaseYear').textContent = albumData.year;
        document.getElementById('albumGenre').textContent = albumData.genre;
        document.getElementById('albumLength').textContent = albumData.length;
        
        await loadAlbumAverage(albumId);
    } catch (error) {
        console.error('Error loading album details:', error);
        throw error;
    }
}

async function loadAlbumAverage(albumId) {
    try {
        const response = await fetch(`${API_URL}/api/albums/${albumId}/average`);
        if (!response.ok) throw new Error('Failed to fetch average');
        
        const data = await response.json();
        if (data.average > 0) {
            document.getElementById('averageScore').textContent = data.average.toFixed(1);
            document.getElementById('scoreBar').style.width = `${data.average}%`;
        } else {
            document.getElementById('averageScore').textContent = 'Not Enough Reviews';
            document.getElementById('scoreBar').style.width = '0%';
        }
    } catch (error) {
        console.error('Error loading average:', error);
        document.getElementById('averageScore').textContent = 'Error loading average';
    }
}

async function loadAlbumReviews(albumId) {
    try {
        const response = await fetch(`${API_URL}/api/albums/${albumId}/reviews`);
        if (!response.ok) throw new Error('Failed to fetch reviews');
        
        const reviews = await response.json();
        const reviewsContainer = document.getElementById('reviewsContainer');
        reviewsContainer.innerHTML = ''; // Clear existing reviews
        
        reviews.forEach(review => {
            const reviewElement = document.createElement('div');
            reviewElement.className = 'review';
            reviewElement.innerHTML = `
                <div class="review-content">
                    <div class="review-header">
                        <span class="review-user">${review.userName}</span>
                        <span class="review-rating">Rating: ${review.rating}</span>
                    </div>
                </div>
            `;
            reviewsContainer.appendChild(reviewElement);
        });

        if (reviews.length === 0) {
            reviewsContainer.innerHTML = '<p class="no-reviews">No reviews yet</p>';
        }
    } catch (error) {
        console.error('Error loading reviews:', error);
        document.getElementById('reviewsContainer').innerHTML = 
            '<p class="error">Error loading reviews</p>';
    }
}

async function submitReview(albumId, rating) {
    try {
        const userId = 1; // TODO: Get actual user ID from authentication
        const response = await fetch(`${API_URL}/api/review`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                userId,
                albumId,
                rating
            })
        });

        if (!response.ok) {
            const error = await response.text();
            throw new Error(error || 'Failed to submit review');
        }

        // Reload reviews and average after successful submission
        await Promise.all([
            loadAlbumReviews(albumId),
            loadAlbumAverage(albumId)
        ]);

        return true;
    } catch (error) {
        console.error('Error submitting review:', error);
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
            await submitReview(albumId, rating);
            alert('Avaliação salva com sucesso!');
            albumRatingInput.value = ''; // Clear input after successful submission
        } catch (error) {
            alert(`Erro ao salvar a avaliação: ${error.message}`);
        }
    });
}

document.addEventListener('DOMContentLoaded', initializeAlbumPage);
