// Helper function para makuha ang token (Magagamit mo ito sa iba pang fetch requests mo)
function getAuthHeader() {
    const token = localStorage.getItem('jwt_token');
    return token ? { 'Authorization': `Bearer ${token}` } : {};
}

// Event Listener para sa iyong login form submission flow
document.getElementById('loginForm').addEventListener('submit', async function(e) {
    e.preventDefault();

    const messageElement = document.getElementById('message');
    messageElement.textContent = "";

    // Kuhanin ang data mula sa form inputs
    const usernameInput = document.getElementById('username').value;
    const passwordInput = document.getElementById('password').value;

    // 1. I-POST ang login credentials bilang JSON Payload sa bagong authentication endpoint ng backend
    try {
        const response = await fetch('/api/v1/auth/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json' // Binago mula sa urlencoded patungong JSON framework
            },
            body: JSON.stringify({
                username: usernameInput,
                password: passwordInput
            })
        });

        // 2. I-verify kung matagumpay ang pagkakaproseso sa server credentials
        if (response.ok) {
            const data = await response.json();

            // 3. I-save ang ibinalik na JSON Web Token (JWT) sa Local Storage ng browser
            if (data.token) {
                localStorage.setItem('jwt_token', data.token); // Dito naiipon ang stateless user access token

                // Mabilisang alert at ituloy ang user sa pangunahing landing page
                alert("Login successful! Redirecting...");
                window.location.href = '/index.html';
            } else {
                messageElement.textContent = "Authentication failed: Token payload missing from server.";
            }
        } else if (response.status === 401) {
            messageElement.textContent = "Invalid username or password.";
        } else {
            messageElement.textContent = "Server configuration error encountered.";
        }

    } catch (error) {
        messageElement.textContent = "An error occurred during login. Please try again.";
        console.error("JWT Authentication flow runtime tracking error:", error);
    }
});