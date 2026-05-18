document.addEventListener('DOMContentLoaded', () => {
    const registerForm = document.getElementById('register-form');
    const messageDiv = document.getElementById('reg-message');

    // TAMA: Helper function para basahin ang XSRF-TOKEN cookie para sa Task 6 requirement
    function getCookie(name) {
        let cookieValue = null;
        if (document.cookie && document.cookie !== '') {
            const cookies = document.cookie.split(';');
            for (let i = 0; i < cookies.length; i++) {
                const cookie = cookies[i].trim();
                if (cookie.substring(0, name.length + 1) === (name + '=')) {
                    cookieValue = decodeURIComponent(cookie.substring(name.length + 1));
                    break;
                }
            }
        }
        return cookieValue;
    }

    if (registerForm) {
        registerForm.addEventListener('submit', async (e) => {
            e.preventDefault();

            const username = document.getElementById('reg-username').value;
            const password = document.getElementById('reg-password').value;
            const role = document.getElementById('reg-role').value;

            messageDiv.className = "message";
            messageDiv.textContent = "Processing registration...";

            try {
                // 1. Gumawa muna ng mabilisang fetch to root/login para masigurong nag-drop ang Spring Security ng CSRF session cookie
                try {
                    await fetch("http://localhost:8080/login", { method: "GET" });
                } catch (err) {
                    console.log("Initial token fetch check:", err);
                }

                const csrfToken = getCookie('XSRF-TOKEN'); // Kunin ang security string key mula sa browser database

                // 2. I-POST ang credentials kasama ang token protection headers
                const response = await fetch("http://localhost:8080/api/v1/auth/register", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json",
                        "X-XSRF-TOKEN": csrfToken // Idinagdag para sa advanced interaction safety checkpoint
                    },
                    body: JSON.stringify({ username, password, role })
                });

                if (response.ok) {
                    messageDiv.className = "message success";
                    messageDiv.style.color = "green"; // Siguraduhing madaling basahin
                    messageDiv.textContent = "Registration successful! Redirecting to login...";

                    setTimeout(() => {
                        window.location.href = "login.html";
                    }, 2000);
                } else {
                    // Kung may advanced bean validation errors (mula sa Task 5 Global Handler)
                    // Mas magandang basahin bilang JSON para makuha ang errors object map
                    try {
                        const errorData = await response.json();
                        if (errorData.errors) {
                            // Ipunin ang mga mensahe mula sa validation array list
                            const validationMessages = Object.values(errorData.errors).join(", ");
                            messageDiv.className = "message error";
                            messageDiv.style.color = "red";
                            messageDiv.textContent = `Registration failed: ${validationMessages}`;
                        } else {
                            messageDiv.className = "message error";
                            messageDiv.style.color = "red";
                            messageDiv.textContent = `Registration failed: ${errorData.message || 'Check input rules.'}`;
                        }
                    } catch (jsonErr) {
                        const errorText = await response.text();
                        messageDiv.className = "message error";
                        messageDiv.style.color = "red";
                        messageDiv.textContent = `Registration failed: ${errorText || 'Username might already be taken.'}`;
                    }
                }

            } catch (error) {
                console.error("Network connection issue:", error);
                messageDiv.className = "message error";
                messageDiv.style.color = "red";
                messageDiv.textContent = "Cannot connect to server. Make sure Spring Boot app is running!";
            }
        });
    }
});