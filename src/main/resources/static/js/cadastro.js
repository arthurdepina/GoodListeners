document.getElementById('registerForm').addEventListener('submit', async function (e) {
    e.preventDefault();

    const username = document.getElementById('user').value.trim();
    const email = document.getElementById('email').value.trim();
    const password = document.getElementById('password').value;
    const confirmPassword = document.getElementById('confirmPassword').value;
    const messageDiv = document.getElementById('message');

    messageDiv.innerHTML = '';

    if (password !== confirmPassword) {
        messageDiv.innerHTML = "<p class='error-message'>As senhas não coincidem!</p>";
        return;
    }

    try {
        const response = await fetch('http://localhost:8080/api/users/register', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                name: username,
                password: password,
                email: email,
            }),
        });

        if (response.ok) {
            messageDiv.innerHTML = "<p class='success-message'>Cadastro realizado com sucesso!</p>";
            setTimeout(() => {
                window.location.href = 'login.html';
            }, 1000);
        } else if (response.status === 409) {
            // Tratamento para 409 Conflict
            const errorData = await response.json();
//            const errorMessage = errorData.errors?.[0]?.message || "Erro de conflito!";
            messageDiv.innerHTML = `<p class='error-message'>${errorData.errors?.[0]?.message}</p>`;
        } else {
            // Tratamento genérico
            const error = await response.text();
            messageDiv.innerHTML = `<p class='error-message'>Erro: ${error}</p>`;
        }
    } catch (error) {
        // Outros erros
        console.error("Erro ao realizar o cadastro:", error);
        messageDiv.innerHTML = "<p class='error-message'>Erro ao realizar cadastro. Tente novamente mais tarde.</p>";
    }
});
