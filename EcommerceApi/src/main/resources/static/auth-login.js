// Helper function para makuha ang CSRF token mula sa document cookies
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

document.getElementById('loginForm').addEventListener('submit', async function(e) {
    e.preventDefault();

    const messageElement = document.getElementById('message');
    messageElement.textContent = "";

    // 1. Gumawa muna ng mabilisang GET request sa /login para ma-trigger at makuha ang CSRF cookie mula sa Spring Security
    try {
        await fetch('/login', { method: 'GET' });
    } catch (err) {
        console.log("Initial CSRF trigger fetch note:", err);
    }

    const csrfToken = getCookie('XSRF-TOKEN'); // Kunin ang token mula sa cookie

    const formData = new URLSearchParams();
    formData.append('username', document.getElementById('username').value);
    formData.append('password', document.getElementById('password').value);

    // 2. I-POST ang login data kasama ang CSRF token sa X-XSRF-TOKEN header
    try {
        const response = await fetch('/login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                'X-XSRF-TOKEN': csrfToken // Napakahalaga para hindi ma-403 Forbidden!
            },
            body: formData.toString()
        });

        if (response.redirected || response.ok) {
            // Kung matagumpay, itutuloy tayo sa main landing page
            window.location.href = '/index.html';
        } else {
            messageElement.textContent = "Invalid username or password.";
        }
    } catch (error) {
        messageElement.textContent = "An error occurred during login. Please try again.";
        console.error("Error:", error);
    }
});