const API_URL = "http://localhost:8080/api/v1/products";

async function fetchProducts() {
    const container = document.getElementById('product-container');

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

document.addEventListener('DOMContentLoaded', fetchProducts);