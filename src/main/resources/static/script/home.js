
document.addEventListener("DOMContentLoaded", () => {
   // Load default canteen ID if it exists
   const defaultCanteenId = localStorage.getItem("defaultCanteenId");
   if (defaultCanteenId) {
       const canteenSelect = document.getElementById("canteenSelect");
       canteenSelect.value = defaultCanteenId;
       fetchShopsAndItems(); // Automatically load shops and items for the default canteen
   }
});

function fetchShopsAndItems() {
   const canteenSelect = document.getElementById("canteenSelect");
   const canteenId = canteenSelect.value;
   const selectedCanteenName = canteenSelect.options[canteenSelect.selectedIndex].text;
   
   document.getElementById("selectedCanteenName").innerText = " " + selectedCanteenName;

   if (canteenId) {
       // Save selected canteen ID as default
       localStorage.setItem("defaultCanteenId", canteenId);
       fetchShops(canteenId);
       fetchRecommendedDishes(canteenId);
   } else {
       clearContainers();
   }
}

function fetchShops(canteenId) {
   fetch(`/user/canteen/shops/${canteenId}`)
       .then(response => response.json())
       .then(data => displayShops(data))
       .catch(error => console.error('Error fetching shops:', error));
}

function fetchRecommendedDishes(canteenId) {
   fetch(`/user/canteen/shops/${canteenId}/items`)
       .then(response => response.json())
       .then(data => displayRecommendedDishes(data))
       .catch(error => console.error('Error fetching recommended dishes:', error));
}

function clearContainers() {
   document.getElementById("shopContainer").innerHTML = "";
   document.getElementById("recommendedFoodContainer1").innerHTML = "";
   document.getElementById("recommendedFoodContainer2").innerHTML = "";
}

function displayShops(shops) {
   const shopContainer = document.getElementById("shopContainer");
   shopContainer.innerHTML = "";
   shops.forEach(shop => {
       const shopBox = `
           <div class="recommended-food-box">
               <div class="item-pic"><img src="${shop.picture}" alt="${shop.name}"></div>
               <div class="item-title">
                   <div class="item-name">
                       <div class="dish-name">${shop.name}</div>
                       <div class="div-rating-star"><img src="/images/5stars.png"></div>
                   </div>
               </div>
           </div>`;
       shopContainer.innerHTML += shopBox;
   });
}

let allItems = []; // Store all items fetched for current canteen

function fetchRecommendedDishes(canteenId) {
    fetch(`/user/canteen/shops/${canteenId}/items`)
        .then(response => response.json())
        .then(data => {
            allItems = data; // Store fetched items for filtering
            displayRecommendedDishes(allItems); // Display all items initially
        })
        .catch(error => console.error('Error fetching recommended dishes:', error));
}

function filterByCategory(category) {
    const recommendedText = document.getElementById("recommendedText");
    const categoryButtons = document.querySelectorAll(".category-button");

    // Remove "active" class from all buttons
    categoryButtons.forEach(button => button.classList.remove("active"));

    // Set the text and filter items based on category
    if (category === '') {
        recommendedText.textContent = "Recommended Dishes";
        displayRecommendedDishes(allItems);
        // Add "active" class to the "Recommended Dishes" button
        document.querySelector(".category-button:nth-child(1)").classList.add("active");
    } else {
        recommendedText.textContent = category;
        const filteredItems = allItems.filter(item => item.category === category);
        displayRecommendedDishes(filteredItems);

        // Find and activate the button for the selected category
        categoryButtons.forEach(button => {
            if (button.textContent === category) {
                button.classList.add("active");
            }
        });
    }
}


function displayRecommendedDishes(items) {
    const recommendedFoodContainer1 = document.getElementById("recommendedFoodContainer1");
    const recommendedFoodContainer2 = document.getElementById("recommendedFoodContainer2");
    recommendedFoodContainer1.innerHTML = "";
    recommendedFoodContainer2.innerHTML = "";

    items.forEach(item => {
        const itemBox = `
            <div class="recommended-food-box" onclick="viewItem(${item.itemId})">
                <div class="item-pic"><img src="${item.imageUrl}" alt="${item.name}"></div>
                <div class="item-title">
                    <div class="item-name">
                        <div class="dish-name">${item.name}</div>
                        <div class="div-rating-star"><img src="/images/5stars.png"></div>
                    </div>
                    <div class="item-price">
                        <div class="thai-baht-icon"><img src="/icons/icons8-thai-baht-24.png"></div> 
                        ${item.price.toFixed(2)}
                    </div>
                </div>
            </div>`;
        recommendedFoodContainer1.innerHTML += itemBox;
        recommendedFoodContainer2.innerHTML += itemBox;
    });
}

function viewItem(itemId) {
    if (userId) {
        window.location.href = `/user/${userId}/item/${itemId}`;
    } else {
        console.error("User ID is not available");
    }
}


// function viewItem(itemId){
//    window.location.href=`/user/item/${itemId}`;
// }
