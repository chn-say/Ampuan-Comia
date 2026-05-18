/**
 * Task 7: Global Reusable Fetch Wrapper & Route Guard
 * Pinoprotektahan nito ang mga sensitibong HTML routes laban sa 401/403 security states.
 */

// 1. Intercept at i-check kung valid ang state ng session bago mag-render ng UI
async function checkAuthAndProtectPage() {
    try {
        // Gumawa ng mabilisang tawag sa iyong check status endpoint
        // Kung walang session status endpoint, pwede ring gamitin ang checkout initialization api fetch
        const response = await fetch("http://localhost:8080/api/v1/orders", { method: "GET" });

        if (response.status === 401) {
            // Requirement 2: If 401, redirect to the login page
            alert("Session expired or unauthorized! Redirecting to login...");
            window.location.href = "login.html";
        } else if (response.status === 403) {
            // Requirement 2: If 403, show an "Access Denied" message
            document.body.innerHTML = `
                <div style="max-width: 500px; margin: 100px auto; text-align: center; font-family: sans-serif; padding: 20px; box-shadow: 0 4px 12px rgba(0,0,0,0.1); border-radius: 8px;">
                    <h1 style="color: red; font-size: 48px; margin-bottom: 10px;">⚠️ 403</h1>
                    <h2 style="color: #333;">Access Denied</h2>
                    <p style="color: #666; margin-top: 10px;">You do not have administrative privileges to access this secure zone.</p>
                    <a href="index.html" style="display: inline-block; margin-top: 20px; padding: 10px 20px; background: #222; color: #fff; text-decoration: none; border-radius: 4px;">Back to Home Store</a>
                </div>
            `;
        }
    } catch (error) {
        console.error("Auth security initialization failure:", error);
    }
}

// 2. Custom Wrapper para sa ordinaryong functional actions (para sa mga forms at table tables)
async function secureFetch(url, options = {}) {
    try {
        const response = await fetch(url, options);

        if (response.status === 401) {
            alert("You must log in first!");
            window.location.href = "login.html";
            return null;
        }

        if (response.status === 403) {
            alert("Access Denied: You are not allowed to perform this operation.");
            return null;
        }

        return response;
    } catch (err) {
        console.error("Network integration security fault:", err);
        throw err;
    }
}

// Patakbuhin agad ang authentication validation guard sa pag-load ng page
document.addEventListener("DOMContentLoaded", checkAuthAndProtectPage);