
document.addEventListener('DOMContentLoaded', () => {
    const priceElements = document.querySelectorAll('.item-price');
    priceElements.forEach(element => {
        const price = parseFloat(element.textContent);
        if (!isNaN(price)) {
            element.textContent = price.toFixed(2);
        }
    });

    restoreSelectedItems();

    document.querySelectorAll('.item-checkbox').forEach(checkbox => {
        checkbox.addEventListener('change', () => {
            saveSelectedItems();
            updateSummary();
        });
    });

    const checkoutButton = document.getElementById('checkout-button');
    if (checkoutButton) {
        checkoutButton.addEventListener('click', clearSelectedItemsOnCheckout);
    }
});

function updateSummary() {
    let totalQuantity = 0;
    let totalPrice = 0;

    document.querySelectorAll('.item-checkbox:checked').forEach(checkbox => {
        const price = parseFloat(checkbox.getAttribute('data-price')) || 0;
        const quantity = parseInt(checkbox.getAttribute('data-quantity')) || 0;
        totalQuantity += quantity;
        totalPrice += price * quantity;
    });

    document.getElementById('summary-quantity').textContent = totalQuantity;
    document.getElementById('summary-quantity-button').textContent = totalQuantity;
    document.getElementById('summary-price').textContent = totalPrice.toFixed(2);
}

function saveSelectedItems() {
    const selectedItems = Array.from(document.querySelectorAll('.item-checkbox:checked'))
        .map(checkbox => checkbox.getAttribute('data-id'));
    localStorage.setItem('selectedItems', JSON.stringify(selectedItems));
}

function restoreSelectedItems() {
    const selectedItems = JSON.parse(localStorage.getItem('selectedItems') || '[]');
    document.querySelectorAll('.item-checkbox').forEach(checkbox => {
        if (selectedItems.includes(checkbox.getAttribute('data-id'))) {
            checkbox.checked = true;
        }
    });
    updateSummary();
}

function clearSelectedItemsOnCheckout() {
            localStorage.removeItem('selectedItems');
}

function submitCheckoutForm() {
    const selectedItems = Array.from(document.querySelectorAll('.item-checkbox:checked'))
        .map(checkbox => checkbox.getAttribute('data-id'));

    
    if (selectedItems.length === 0) {
        alert("Please select items to check out!");
        return; // Stop further execution if no items are selected
    }

    const checkoutForm = document.getElementById("checkoutForm");

    // Remove any previous item inputs
    document.querySelectorAll(".selected-item-id").forEach(input => input.remove());

    // Add hidden inputs for each selected item ID
    selectedItems.forEach(itemId => {
        const input = document.createElement("input");
        input.type = "hidden";
        input.name = "selectedItemIds";
        input.value = itemId;
        input.classList.add("selected-item-id");
        checkoutForm.appendChild(input);
    });

    // Submit the form
    checkoutForm.submit();
}

const canteenName = localStorage.getItem("selectedCanteenName");

// Display the canteen name in the appropriate location
if (canteenName) {
    document.querySelector(".header .hleft div:nth-child(2)").textContent = canteenName;
}
