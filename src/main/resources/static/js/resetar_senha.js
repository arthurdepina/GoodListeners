 document.getElementById('resetarSenha').addEventListener('submit', async function (e) {
    e.preventDefault();

    const email = document.getElementById('email').value.trim();
    const messageDiv = document.getElementById('message');
    messageDiv.innerHTML = '';

    if (!email) {
        messageDiv.innerHTML = "<p class='error-message'>Por favor, insira um e-mail válido.</p>";
        return;
    }

    try {
        const response = await fetch(`http://localhost:8080/api/auth/reset-password/${email}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        });

        if (response.ok) {
            const result = await response.json();
            messageDiv.innerHTML = `<p class='success-message'>${result.message}</p>`;
        } else {
            const error = await response.json();
            messageDiv.innerHTML = `<p class='error-message'>${error.errors?.[0]?.message}</p>`;
        }
    } catch (error) {
        messageDiv.innerHTML = "<p class='error-message'>Erro ao tentar redefinir a senha. Tente novamente mais tarde.</p>";
    }
});
