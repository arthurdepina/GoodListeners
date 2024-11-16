document.getElementById('loginForm').addEventListener('submit', async function(event) {
    event.preventDefault();


    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;


    document.getElementById('message').innerHTML = 'Verificando as credenciais...';

    try {

        const response = await fetch("http://localhost:8080/api/auth/login", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify({
                email: email,
                password: password
            })
        });

        if (!response.ok) {
            throw new Error('Credenciais inválidas');
        }

        const data = await response.json();

        localStorage.setItem('userName', data.user.name);
        localStorage.setItem('userId', data.user.userId);
        localStorage.setItem('email', data.user.email);
        localStorage.setItem('token', data.token)

        document.getElementById('message').innerHTML = 'Login realizado com sucesso!';

        window.location.href = "perfil.html";

    } catch (error) {
        document.getElementById('message').innerHTML = 'Erro: ' + error.message;
    }
});

document.getElementById('forgotPassword').addEventListener('click', async function () {

    window.location.href = '/resetar_senha.html';
});
