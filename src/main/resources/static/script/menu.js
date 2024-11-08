
// Function to load items when a shop is selected
function loadItemsForShop(shopId) {
    const createItemLink = document.getElementById("createItemLink");
    const ownerId = document.getElementById("ownerIdField").value;
    createItemLink.href = `/owner/shop/${shopId}/create-item?ownerId=${ownerId}`;

    // Fetch items for the selected shop
    fetch(`/owner/shop/${shopId}/items`)
        .then(response => response.json())
        .then(items => {
            const menuBox = document.getElementById("menuBox");
            menuBox.innerHTML = ""; // Clear previous items
            items.forEach(item => {
                const itemHtml = `
                    <div class="menu-card">
                        <img src="${item.imageUrl}" alt="${item.name}" class="menu-image">
                        <div class="toggle">
                            <label class="switch">
                                <input type="checkbox" ${item.available ? 'checked' : ''}>
                                <span class="slider"></span>
                                <span class="status">${item.available ? 'ON' : 'OFF'}</span>
                            </label>
                        </div>
                        <div class="menu-info">
                            <p>${item.name}</p>
                            <div class="info-bottom">
                                <div class="rating">
                                    <img src="/images/rating-star.png">
                                </div>
                                <div class="price-update">
                                    <img class="thai-baht" src="/icons/icons8-thai-baht-24.png">
                                    <span class="price">${item.price.toFixed(2)}</span>
                                </div>
                            </div>
                        </div>
                    </div>`;
                menuBox.insertAdjacentHTML('beforeend', itemHtml);
            });
        });
}

// Function to handle shop selection change and save to localStorage
function handleShopChange() {
    const shopId = document.getElementById("shopSelect").value;
    if (shopId) {
        localStorage.setItem('selectedShopId', shopId);  // Save selected shop ID
        loadItemsForShop(shopId);
    } else {
        localStorage.removeItem('selectedShopId');  // Clear storage if no shop selected
        document.getElementById("menuBox").innerHTML = "";
        document.getElementById("createItemLink").href = "#";
    }
}

// On page load, check if a shop ID was previously selected
window.onload = function() {
    const savedShopId = localStorage.getItem('selectedShopId');
    if (savedShopId) {
        document.getElementById("shopSelect").value = savedShopId; // Set saved shop as selected
        loadItemsForShop(savedShopId); // Load items for the saved shop
    }
}

function toggleProfileCard() {
    const profileCard = document.querySelector('.profile-card');
    profileCard.style.display = profileCard.style.display === 'none' ? 'block' : 'none';
}