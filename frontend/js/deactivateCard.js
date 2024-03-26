document.getElementById('deactivateCardForm').addEventListener('submit', function(event) {
    event.preventDefault(); // Prevent the default form submission

    // Get card number
    const cardNumber = document.getElementById('cardNumber').value;

    // Send GET request to backend to deactivate card
    fetch(`http://localhost:8080/cards/deactivate?cardNumber=${cardNumber}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (response.ok) {
                return response.text();
            } else if (response.status === 404)
                return response.text();
            else {
                throw new Error('Failed to deactivate card.');
            }
        })
        .then(message => {
            // Show success message
            document.getElementById('message').style.display = 'block';
            document.getElementById('message').textContent = message;
        })
        .catch(error => {
            // Show error message
            document.getElementById('message').style.display = 'block';
            document.getElementById('message').textContent = error.message;
        });
});