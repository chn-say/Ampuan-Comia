/**
 * Task 7: Global Reusable Fetch Wrapper & Route Guard (JWT Stateless Version)
 * Pinoprotektahan nito ang mga sensitibong HTML routes laban sa 401/403 security states gamit ang Bearer Token.
 */

// 1. Intercept at i-check kung valid ang state ng session bago mag-render ng UI
async function checkAuthAndProtectPage() {
    // Kunin ang token mula sa Local Storage
    const token = localStorage.getItem("jwt_token");

    // Kung wala man lang token sa local engine, huwag nang patakbuhin ang fetch, i-redirect na agad (Mabilis na Client-Side Guard)
    if (!token) {
        alert("Session missing or expired! Redirecting to login...");
        window.location.href = "login.html";
        return;
    }

    try {
        // Gumawa ng mabilisang tawag sa iyong check status endpoint kasama ang Authorization Header
        const response = await fetch("http://localhost:8080/api/v1/orders", {
            method: "GET",
            headers: {
                "Authorization": `Bearer ${token}` // NAPAKAHALAGA: Dito bineverify ng filter ang identity mo
            }
        });

        if (response.status === 401) {
            // Requirement 2: If 401, redirect to the login page
            localStorage.removeItem("jwt_token"); // Linisin ang sirang token
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

// 2. Custom Wrapper para sa ordinaryong functional actions (para sa mga forms, product tables, at pag-add ng data)
async function secureFetch(url, options = {}) {
    const token = localStorage.getItem("jwt_token");

    // Tiyakin na may 'headers' object ang options parameters
    options.headers = options.headers || {};

    // Kung may token na naka-save, awtomatikong isaksak ang Bearer footprint sa headers metadata
    if (token) {
        options.headers["Authorization"] = `Bearer ${token}`; // dynamic payload formatting inject
    }

    try {
        const response = await fetch(url, options);

        if (response.status === 401) {
            localStorage.removeItem("jwt_token"); // Linisin ang invalid token profile
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