document.getElementById('createCardForm').addEventListener('submit', function(event) {
    event.preventDefault(); // Prevent the default form submission

    // Get form data
    const email =  document.getElementById('email').value;

    // Send POST request to backend
    fetch(`http://localhost:8080/cards/generate?email=${email}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        },
    })
        .then(response => {
            if (response.ok) {
                console.log('Card created successfully');
                document.getElementById('errorMessage').style.display = 'none';
                document.getElementById('successMessage').style.display = 'block';
                // document.getElementById('createCardForm').reset();
            }
            else if (response.status === 404) {
                console.error('Клиент не найден');
                document.getElementById('successMessage').style.display = 'none';
                document.getElementById('errorMessage').style.display = 'block';
                document.getElementById('createCardForm').reset();// Optionally show an error message
            }
            else {
                console.error('Failed to create Card');
                // Optionally show an error message
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
});