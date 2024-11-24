// Função para buscar os álbuns da API e renderizá-los na página
async function fetchAndRenderAlbums() {
    const API_URL = window.location.hostname === 'localhost'
        ? 'http://localhost:8080'
        : 'https://goodlisteners.onrender.com';

    try {
        const response = await fetch(`${API_URL}/api/albums`);
        if (!response.ok) {
            throw new Error(`Error: ${response.status}`);
        }

        const albums = await response.json();
        const container = document.getElementById('albums-container');
        container.innerHTML = '';

        albums.forEach(album => {
            const albumHtml = `
                <div class="album">
                    <img src="${album.coverUrl}" alt="${album.name}" />
                    <div class="album-info">
                        <h3 class="album-title">
                            <a class="album-link" href="${API_URL}/album.html?id=${album.albumId}">${album.name}</a>
                        </h3>
                        <p class="album-artist">${album.artist}</p>
                        <div class="album-score user-score">
                            <div class="score-label">
                                <span>Average Score</span>
                                <span>${album.averageScore || 'N/A'}</span>
                            </div>
                            <div class="score-bar">
                                <div class="score-fill" style="width: ${album.averageScore * 10 || 0}%;"></div>
                            </div>
                        </div>
                    </div>
                </div>
            `;
            container.insertAdjacentHTML('beforeend', albumHtml);
        });
    } catch (error) {
        console.error('Failed to load albums:', error);
    }
}

// Chama a função fetchAndRenderAlbums ao carregar a página
document.addEventListener('DOMContentLoaded', fetchAndRenderAlbums);
