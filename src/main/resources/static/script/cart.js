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

function updateCartTotal(totalQuantity) {
    const cartQtyElement = document.querySelector('.cart-total-qty');
    if (cartQtyElement) {
        cartQtyElement.textContent = totalQuantity;
    }

    const cartItemCountElement = document.querySelector('.cart-quantity span');
    if (cartItemCountElement) {
        cartItemCountElement.textContent = totalQuantity;
    }
}

function increaseQuantity(cartItemId) {
    const userId = document.getElementById('userIdField').value;

    fetch(`/user/${userId}/cart/ajax-increase?cartItemId=${cartItemId}`, {
        method: 'POST',
    })
    .then(response => response.json())
    .then(data => {
        if (data.quantity !== undefined) {
            document.getElementById(`quantity-${cartItemId}`).textContent = data.quantity;

            // ✅ Always update checkbox data-quantity
            const checkbox = document.querySelector(`.item-checkbox[data-item-id='${cartItemId}']`);
            if (checkbox) {
                checkbox.setAttribute('data-quantity', data.quantity);
            }
        }
        if (data.totalQuantity !== undefined) {
            updateCartTotal(data.totalQuantity);
        }

        updateSummary();
    })
    .catch(error => console.error('Error:', error));
}

function decreaseQuantity(cartItemId) {
    const userId = document.getElementById('userIdField').value;

    fetch(`/user/${userId}/cart/ajax-decrease?cartItemId=${cartItemId}`, {
        method: 'POST',
    })
    .then(response => response.json())
    .then(data => {
        if (data.quantity !== undefined) {
            const quantityElement = document.getElementById(`quantity-${cartItemId}`);
            if (data.quantity === 0) {
                quantityElement.closest('.selected-dish-box').remove();
            } else {
                quantityElement.textContent = data.quantity;

                // ✅ Update the checkbox's data-quantity
                const checkbox = document.querySelector(`.item-checkbox[data-item-id='${cartItemId}']`);
                if (checkbox) {
                    checkbox.setAttribute('data-quantity', data.quantity);
                }
            }
        }
        if (data.totalQuantity !== undefined) {
            updateCartTotal(data.totalQuantity);
        }

        updateSummary();
    })
    .catch(error => console.error('Error:', error));
}

function deleteItem(cartItemId) {
    const userId = document.getElementById('userIdField').value;

    fetch(`/user/${userId}/cart/ajax-delete?cartItemId=${cartItemId}`, {
        method: 'POST',
    })
    .then(response => response.json())
    .then(data => {
        const itemElement = document.getElementById(`quantity-${cartItemId}`);
        if (itemElement) {
            itemElement.closest('.selected-dish-box').remove();
        }
        if (data.totalQuantity !== undefined) {
            updateCartTotal(data.totalQuantity);
        }
        updateSummary();
    })
    .catch(error => console.error('Error:', error));
}

function updateSummary() {
    let totalQuantity = 0;
    let totalPrice = 0;

    document.querySelectorAll('.item-checkbox:checked').forEach(checkbox => {
        const price = parseFloat(checkbox.getAttribute('data-price')) || 0;
        const quantity = parseInt(checkbox.getAttribute('data-quantity')) || 0;
        if (quantity > 0) {
            totalQuantity += quantity;
            totalPrice += price * quantity;
        }
    });

    const summaryQuantity = document.getElementById('summary-quantity');
    if (summaryQuantity) summaryQuantity.textContent = totalQuantity;

    const summaryButton = document.getElementById('summary-quantity-button');
    if (summaryButton) summaryButton.textContent = totalQuantity;

    const summaryPrice = document.getElementById('summary-price');
    if (summaryPrice) summaryPrice.textContent = totalPrice.toFixed(2);
}

function saveSelectedItems() {
    const selectedItems = Array.from(document.querySelectorAll('.item-checkbox:checked'))
        .map(checkbox => ({
            id: checkbox.getAttribute('data-item-id'), // ✅ Use data-item-id
            quantity: parseInt(checkbox.getAttribute('data-quantity')) || 1
        }));
    localStorage.setItem('selectedItems', JSON.stringify(selectedItems));
}

function restoreSelectedItems() {
    const selectedItems = JSON.parse(localStorage.getItem('selectedItems') || '[]');
    
    document.querySelectorAll('.item-checkbox').forEach(checkbox => {
        const savedItem = selectedItems.find(item => item.id === checkbox.getAttribute('data-item-id')); // ✅ Use data-item-id
        if (savedItem) {
            checkbox.checked = true;
            checkbox.setAttribute('data-quantity', savedItem.quantity);
        }
    });

    updateSummary();
}

function clearSelectedItemsOnCheckout() {
    localStorage.removeItem('selectedItems');
}

async function submitCheckoutForm() {
    const userId = document.getElementById('userIdField').value;

    const selectedItems = Array.from(document.querySelectorAll('.item-checkbox:checked'))
        .map(checkbox => ({
            itemId: parseInt(checkbox.getAttribute('data-item-id')),
            quantity: parseInt(checkbox.getAttribute('data-quantity')) || 1
        }));

    if (selectedItems.length === 0) {
        alert("Please select items to check out!");
        return;
    }

    try {
        const response = await fetch(`/api/payment/checkout?userId=${userId}`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({ items: selectedItems })
        });

        if (!response.ok) {
            throw new Error("Failed to checkout");
        }

        const stripeResponse = await response.json();

        window.location.href = stripeResponse.sessionUrl;

    } catch (error) {
        console.error("Error:", error);
        alert("Failed to checkout during payment processing.");
    }
}


const canteenName = localStorage.getItem("selectedCanteenName");

if (canteenName) {
    document.querySelector(".header .hleft div:nth-child(2)").textContent = canteenName;
}
