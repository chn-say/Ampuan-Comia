const API_URL = "http://localhost:8080/api/v1/products";

async function fetchProducts() {
    try {
        const response = await fetch(API_URL);
        const products = await response.json();
        console.log("Data fetched from MySQL:", products);
        displayProducts(products);
    } catch (error) {
        console.error("Hindi makakonekta sa API:", error);
        document.getElementById('product-container').innerHTML =
            "<p>Error loading products. Make sure your Spring Boot app is running!</p>";
    }
}

function displayProducts(products) {
    const container = document.getElementById('product-container');
    container.innerHTML = "";

    products.forEach(product => {
        const card = `
            <div class="product-card">
                <img src="${product.imageUrl || 'https://via.placeholder.com/200'}" alt="${product.name}">
                <p class="category">${product.category}</p>
                <h3>${product.name}</h3>
                <p>${product.description}</p>
                <p class="price">₱${product.price.toLocaleString()}</p>
                <p>Stock: ${product.stock}</p>
                <button onclick="alert('Added ${product.name} to cart!')">Add to Cart</button>
            </div>
        `;
        container.innerHTML += card;
    });
}

document.addEventListener('DOMContentLoaded', fetchProducts);