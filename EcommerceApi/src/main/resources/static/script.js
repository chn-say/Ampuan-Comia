const API_URL = "http://localhost:8080/api/v1/products";

// Helper function para makuha ang tokens mula sa cookies
function getCookie(name) {
    const value = `; ${document.cookie}`;
    const parts = value.split(`; ${name}=`);
    if (parts.length === 2) {
        return parts.pop().split(';').shift();
    }
    return null;
}

// Function para sa authenticated application logout
async function handleLogout() {
    try {
        const response = await fetch("http://localhost:8080/logout", {
            method: "POST",
            headers: {
                "X-XSRF-TOKEN": getCookie("XSRF-TOKEN") || ""
            }
        });
        alert("Logged out successfully!");
        window.location.href = "login.html";
    } catch (error) {
        console.error("Logout connection failed:", error);
        alert("Error connecting to server during logout.");
    }
}

// Ang iyong orihinal na functional product logic na kumukuha ng data mula sa MySQL (Task 8)
async function fetchProducts() {
    const container = document.getElementById("product-container");
    if (!container) {
        return;
    }

    try {
        const response = await fetch(API_URL);

        if (!response.ok) {
            throw new Error(`Server responded with status: ${response.status} (${response.statusText})`);
        }

        const products = await response.json();
        console.log("Data fetched from MySQL:", products);

        if (!products || products.length === 0) {
            displayEmptyState(container);
            return;
        }

        displayProducts(products, container);

    } catch (error) {
        console.error("Debugging Fetch Error / Hindi makakonekta sa API:", error.message);

        container.innerHTML = `
            <div class="error-state" style="text-align: center; padding: 20px; color: red;">
                <p><strong>Error loading products.</strong> Make sure your Spring Boot app is running!</p>
                <small style="color: gray;">Details: ${error.message}</small>
            </div>
        `;
    }
}

function displayProducts(products, container) {
    container.innerHTML = "";

    products.forEach(product => {
        const card = `
            <div class="product-card">
                <img src="${product.imageUrl || 'https://via.placeholder.com/200'}" alt="${product.name}">
                <p class="category">${product.category ? product.category.name : 'Uncategorized'}</p>
                <h3>${product.name}</h3>
                <p>${product.description}</p>
                <p class="price">₱${product.price.toLocaleString()}</p>
                <p>Stock: ${product.stock}</p>
                <button onclick="alert('Added ${product.name} to cart!')" ${product.stock === 0 ? 'disabled' : ''}>
                    ${product.stock > 0 ? 'Add to Cart' : 'Out of Stock'}
                </button>
            </div>
        `;
        container.innerHTML += card;
    });
}

function displayEmptyState(container) {
    container.innerHTML = `
        <div class="empty-state" style="text-align: center; padding: 40px; color: gray;">
            <p>No products available at the moment. Check back later!</p>
        </div>
    `;
}

// Dynamics interface modifications base sa Session data status
function initializeAuthenticationUI() {
    const activeSessionToken = getCookie("XSRF-TOKEN");
    const mainHeader = document.querySelector("header") || document.body;
    const authStatusBar = document.createElement("div");

    authStatusBar.className = "auth-status-bar";
    authStatusBar.style.cssText = "background: #f4f4f4; padding: 10px 20px; text-align: right; font-family: sans-serif; font-size: 14px; border-bottom: 1px solid #ddd;";

    if (activeSessionToken) {
        authStatusBar.innerHTML = `
            <span style="color: green; font-weight: bold; margin-right: 15px;">🔑 Active Session Verified</span>
            <button onclick="handleLogout()" style="padding: 6px 12px; background: #d9534f; color: white; border: none; border-radius: 4px; cursor: pointer; font-weight: bold;">Logout</button>
        `;
    } else {
        authStatusBar.innerHTML = `
            <span style="color: #777; margin-right: 15px;">👤 Guest Mode (Browse Only)</span>
            <a href="login.html" style="padding: 6px 12px; background: #333; color: white; text-decoration: none; border-radius: 4px; font-weight: bold;">Login</a>
            <a href="register.html" style="padding: 6px 12px; background: #5cb85c; color: white; text-decoration: none; border-radius: 4px; font-weight: bold; margin-left: 5px;">Register</a>
        `;
    }

    mainHeader.prepend(authStatusBar);
}

// DOM trigger attachments
document.addEventListener("DOMContentLoaded", () => {
    initializeAuthenticationUI();
    fetchProducts();
});