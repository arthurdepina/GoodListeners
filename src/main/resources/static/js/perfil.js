document.getElementById('logoutButton').addEventListener('click', async function () {
    try {
        const response = await fetch('/api/auth/logout', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
        });

        if (response.ok) {
            const data = await response.json();

            localStorage.removeItem('token');
            localStorage.removeItem('userId');
            localStorage.removeItem('userName');

            alert(data.message);
            window.location.href = '/home.html';
        } else {
            alert('Erro de logout.');
        }
    } catch (error) {
        console.error('Erro ao tentar fazer logout:', error);
    }
});

async function getProfile(userId) {
    try {
        const response = await fetch(`/api/users/profile/${userId}`, {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`,
                'Content-Type': 'application/json'
            }
        });

        if (response.ok) {
            const data = await response.json();
            console.log("Dados do perfil:", data);
        } else {
            console.error("Erro ao buscar perfil:", await response.text());
        }
    } catch (error) {
        console.error("Erro na requisição:", error);
    }
}

document.getElementById('toHomeButton').addEventListener('click', async function () {

    window.location.href = '/home.html';
});
