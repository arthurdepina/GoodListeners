document.getElementById("linkPerfil").addEventListener("click", async function (e) {
    e.preventDefault(); 

    const userId = localStorage.getItem("userId");
    const messageDiv = document.getElementById("message");

    if (!userId) {
        alert("Usuário não autenticado. Por favor, faça login.");
        window.location.href = "login.html";
        return;
    }

    try {

        let token = localStorage.getItem("token");
        const response = await fetch(`http://localhost:8080/api/users/profile/${userId}`, {
            method: "GET",
            headers: {
                "Authorization": `Bearer ${token}`,
                "Content-Type": "application/json"
            }
        });

        if (response.ok) {
            const userProfile = await response.json();

            window.location.href = `perfil.html?id=${userId}`;
        } else if (response.status === 401) {

            alert("Usuário não autenticado. Por favor, faça login.");
            window.location.href = "login.html";
        } else {
            const data = await response.json();
            alert(data.errors?.[0]?.message);
        }
    } catch (error) {
        console.error("Erro na requisição:", error);
        alert("Erro ao acessar o servidor. Verifique sua conexão.");
    }
});
