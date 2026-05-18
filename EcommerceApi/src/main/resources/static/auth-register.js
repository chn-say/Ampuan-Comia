document.addEventListener('DOMContentLoaded', () => {
    const registerForm = document.getElementById('register-form');
    const messageDiv = document.getElementById('reg-message');

    if (registerForm) {
        registerForm.addEventListener('submit', async (e) => {
            e.preventDefault();

            const username = document.getElementById('reg-username').value;
            const password = document.getElementById('reg-password').value;
            const role = document.getElementById('reg-role').value;

            messageDiv.className = "message";
            messageDiv.textContent = "Processing registration...";

            try {

                const response = await fetch("http://localhost:8080/api/v1/auth/register", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify({ username, password, role })
                });

                if (response.ok) {
                    messageDiv.className = "message success";
                    messageDiv.textContent = "Registration successful! Redirecting to login...";

                    setTimeout(() => {
                        window.location.href = "login.html";
                    }, 2000);
                } else {
                    const errorText = await response.text();
                    messageDiv.className = "message error";
                    messageDiv.textContent = `Registration failed: ${errorText || 'Username might already be taken.'}`;
                }

            } catch (error) {
                console.error("Network connection issue:", error);
                messageDiv.className = "message error";
                messageDiv.textContent = "Cannot connect to server. Make sure Spring Boot app is running!";
            }
        });
    }
});