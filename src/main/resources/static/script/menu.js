
window.onload= function(){
    const shopSelect= document.getElementById("shopSelect");
    const savedShopId = localStorage.getItem('selectedShopId');
    const ownerId = document.getElementById("ownerIdField").value;
    const shopId = savedShopId || selectFirstShop();

    if(shopSelect && shopId){
        shopSelect.value = shopId;
        setOrderLink(ownerId, shopId);
        loadItemsForShop(shopId);
    }
}

function setOrderLink(ownerId, shopId){
    const viewOrderLink = document.getElementById("viewOrderLink");
    viewOrderLink.href= `/owner/${ownerId}/orders?shopId=${shopId}`;
}

// Select the first shop from dropdown and save to localStorage
function selectFirstShop() {
    const shopSelect = document.getElementById("shopSelect");
    if (shopSelect.options.length > 0) {
        const firstShopId = shopSelect.options[0].value;
        shopSelect.selectedIndex = 0;
        localStorage.setItem('selectedShopId', firstShopId); // save to local storage
        return firstShopId;
    }
    return null;
}


function handleShopChange() {
    const shopId = document.getElementById("shopSelect").value;
    const ownerId = document.getElementById("ownerIdField").value;

    if (shopId) {
        localStorage.setItem('selectedShopId', shopId); // save the selected shop to local storage
        loadItemsForShop(shopId);
        setOrderLink(ownerId, shopId); // Update order link with new shopId
    } else {
        localStorage.removeItem('selectedShopId');
        clearMenuBox();
    }

    // Style the selector of the shops
    const selectElement = document.getElementById('shopSelect');
    if (selectElement.value) {
        selectElement.classList.add('selected-shop');
    } else {
        selectElement.classList.remove('selected-shop');
    }
}

// Load items for the selected shop
function loadItemsForShop(shopId) {
    const createItemLink = document.getElementById("createItemLink");
    const ownerId = document.getElementById("ownerIdField").value;
    createItemLink.href = `/owner/shop/${shopId}/create-item?ownerId=${ownerId}`;

    fetch(`/owner/shop/${shopId}/items`)
        .then(response => response.json())
        .then(items => {
            const menuBox = document.getElementById("menuBox");
            menuBox.innerHTML = ""; // Clear previous items
            
            // Display the latest items first
            items.reverse().forEach(item => {
                menuBox.insertAdjacentHTML('beforeend', generateItemHTML(item, shopId));
            });
        })
        .catch(error => console.error('Error loading items:', error));
}

// Update generateItemHTML function in menu.js

function generateItemHTML(item, shopId) {
    const ownerId = document.getElementById("ownerIdField").value;
    return `
        <div class="menu-card">
            <img src="${item.imageUrl}" alt="${item.name}" class="menu-image">
            <div class="toggle">
                <label class="switch">
                    <input type="checkbox" ${item.available ? 'checked' : ''}>
                    <span class="slider"></span>
                    <span class="status">${item.available ? 'ON' : 'OFF'}</span>
                </label>
            </div>
            <div class="edit-item">
                <a href="/owner/shop/${shopId}/edit-item/${item.itemId}?ownerId=${ownerId}">
                    <img src="/icons/icons8-edit-50 (1).png" alt="edit">
                    <span class="edit-tooltip">Edit</span>
                </a>
            </div>
            <div class="menu-info">
                <p>${item.name}</p>
                <div class="info-bottom">
                    <div class="rating">
                        <img src="/images/rating-star.png" alt="Rating Star">
                    </div>
                    <div class="price-update">
                        <img class="thai-baht" src="/icons/icons8-thai-baht-24.png" alt="Thai Baht Icon">
                        <span class="price">${item.price.toFixed(2)}</span>
                    </div>
                </div>
            </div>
        </div>`;
}


// Clear the menu box content and reset the create item link
function clearMenuBox() {
    document.getElementById("menuBox").innerHTML = "";
    document.getElementById("createItemLink").href = "#";
}

// Add a newly created item to the menu box dynamically
function addItemToMenuBox(item, shopId) {
    const menuBox = document.getElementById("menuBox");
    if (menuBox) {
        menuBox.insertAdjacentHTML('beforeend', generateItemHTML(item, shopId));
    } else {
        console.error("Error: menuBox element not found on the page.");
    }
}

// Toggle visibility of the profile card
function toggleProfileCard() {
    const profileCard = document.querySelector('.profile-card');
    profileCard.style.display = profileCard.style.display === 'none' ? 'block' : 'none';
}
