document.addEventListener("DOMContentLoaded", () => {
    const canteenSelect = document.getElementById("canteenSelect");
    const defaultCanteenId = localStorage.getItem("defaultCanteenId");

    // Automatically select the first canteen if no selection is made
    if (defaultCanteenId) {
        canteenSelect.value = defaultCanteenId;
    } else if (canteenSelect.options.length > 1) {
        canteenSelect.selectedIndex = 1; // Set the first canteen as default
        localStorage.setItem("defaultCanteenId", canteenSelect.options[1].value);
    }

    fetchShopsAndItems(); // Load shops and items for the selected or default canteen
});

// Fetch shops and items based on selected canteen
function fetchShopsAndItems() {
    const canteenSelect = document.getElementById("canteenSelect");
    const canteenId = canteenSelect.value;
    const selectedCanteenName = canteenSelect.options[canteenSelect.selectedIndex].text;
    
    document.getElementById("selectedCanteenName").innerText = " " + selectedCanteenName;

    if (canteenId) {
        localStorage.setItem("defaultCanteenId", canteenId); // Save as default
        fetchShops(canteenId);
        fetchRecommendedDishes(canteenId);
    } else {
        clearContainers(); // Clear displays if no canteen is selected
    }
}

// Fetch and display shops for a given canteen
function fetchShops(canteenId) {
    fetch(`/user/canteen/shops/${canteenId}`)
        .then(response => response.json())
        .then(displayShops)
        .catch(error => console.error('Error fetching shops:', error));
}

// Fetch and display available items for a given canteen
function fetchRecommendedDishes(canteenId) {
    fetch(`/user/canteen/shops/${canteenId}/items`)
        .then(response => response.json())
        .then(data => {
            allItems = data; // Store fetched available items for filtering
            displayRecommendedDishes(allItems); // Display available items initially
        })
        .catch(error => console.error('Error fetching recommended dishes:', error));
}

// Clear the shop and recommended dish containers
function clearContainers() {
    document.getElementById("shopContainer").innerHTML = "";
    document.getElementById("recommendedFoodContainer1").innerHTML = "";
    document.getElementById("recommendedFoodContainer2").innerHTML = "";
}

// Display shops in the shop container
function displayShops(shops) {
    const shopContainer = document.getElementById("shopContainer");
    shopContainer.innerHTML = ""; // Clear previous shops

    shops.forEach(shop => {
        shopContainer.innerHTML += `
            <div class="recommended-food-box">
                <div class="item-pic"><img src="${shop.picture}" alt="${shop.name}"></div>
                <div class="item-title">
                    <div class="item-name">
                        <div class="dish-name">${shop.name}</div>
                        <div class="div-rating-star"><img src="/images/5stars.png" alt="Rating Star"></div>
                    </div>
                </div>
            </div>`;
    });
}

let allItems = []; // Store all items fetched for the current canteen

// Filter and display items by selected category
function filterByCategory(category) {
    const recommendedText = document.getElementById("recommendedText");
    const categoryButtons = document.querySelectorAll(".category-button");

    // Set active category button and update display
    categoryButtons.forEach(button => button.classList.toggle("active", button.textContent === category || (!category && button.textContent === "Recommended Dishes")));
    recommendedText.textContent = category || "Recommended Dishes";
    displayRecommendedDishes(allItems.filter(item => !category || item.category === category));
}

// Search and display items by text input
function searchItems() {
    const searchInput = document.getElementById("searchInput").value.toLowerCase();
    const filteredItems = allItems.filter(item => item.name.toLowerCase().includes(searchInput) || item.category.toLowerCase().includes(searchInput));
    displayRecommendedDishes(filteredItems);
}

// Display recommended dishes in alternating containers for layout balance
function displayRecommendedDishes(items) {
    const [container1, container2] = [
        document.getElementById("recommendedFoodContainer1"),
        document.getElementById("recommendedFoodContainer2"),
    ];
    container1.innerHTML = container2.innerHTML = ""; // Clear previous items

    if (items.length === 0) {
        container1.innerHTML = container2.innerHTML = "<div>No items found.</div>";
        return;
    }

    items.forEach((item, index) => {
        const itemBox = `
            <div class="recommended-food-box" onclick= viewItem(${item.itemId})>
                <div class="add-to-favorite">
                    <div class="heart-shape">
                        <img src="/icons/icons8-heart-50.png" alt="Add to Favorite">
                        <div class="color-heart">
                            <img src="/icons/icons8-heart-50 (1).png" alt="Favorite Icon">
                        </div>
                        <span class="favorite-tooltip">Add to Favorite</span>
                    </div>
                </div>
                <div class="add-button" data-item-id="${item.itemId}" onclick="addToCart(this, event)">
                    +
                    <span class="cart-tooltip">Add to Cart</span>
                </div>
                <div class="item-pic"><img src="${item.imageUrl}" alt="${item.name}"></div>
                <div class="item-title">
                    <div class="item-name">
                        <div class="dish-name">${item.name}</div>
                        <div class="div-rating-star"><img src="/images/5stars.png" alt="Rating Star"></div>
                    </div>
                    <div class="item-price">
                        <img class="thai-baht-icon" src="/icons/icons8-thai-baht-24.png" alt="Baht Icon">
                        ${item.price.toFixed(2)}
                    </div>
                </div>
            </div>`;

        if (index % 2 === 0) {
            container1.innerHTML += itemBox;
        } else {
            container2.innerHTML += itemBox;
        }
    });
}


// Navigate to item details page for the selected item
function viewItem(itemId) {
    if (userId) {
        window.location.href = `/user/${userId}/item/${itemId}`;
    } else {
        console.error("User ID is not available");
    }
}

document.getElementById("canteenSelect").addEventListener("change", function() {
    const canteenName = this.options[this.selectedIndex].text;
    localStorage.setItem("selectedCanteenName", canteenName);
});

function addToCart(button, event) {
    if (event) event.preventDefault(); // Prevent default action

    const itemId = button.getAttribute('data-item-id');
    const userId = document.getElementById('userIdField').value;

    fetch(`/user/${userId}/cart/ajax-add?itemId=${itemId}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
    })
        .then((response) => {
            if (!response.ok) {
                throw new Error('Failed to add item to cart.');
            }
            return response.json();
        })
        .then((data) => {
            const cartQtyElement = document.querySelector('.cart-total-qty');
            if (cartQtyElement) {
                cartQtyElement.textContent = data.totalQuantity;
            }
            showNotification('Item added to cart successfully!');
        })
        .catch((error) => {
            console.error('Error:', error);
            showNotification('Failed to add item to cart.', 'error');
        });
}

// Show notification
function showNotification(message, type = 'success') {
    const notification = document.createElement('div');
    notification.className = `notification ${type}`;
    notification.textContent = message;
    document.body.appendChild(notification);

    setTimeout(() => {
        notification.remove();
    }, 3000);
}




