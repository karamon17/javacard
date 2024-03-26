document.getElementById('createClientForm').addEventListener('submit', function(event) {
    event.preventDefault(); // Prevent the default form submission

    // Get form data
    const formData = {
        fullName: document.getElementById('fullName').value,
        email: document.getElementById('email').value,
        birthDate: document.getElementById('birthDate').value
    };

    // Send POST request to backend
    fetch('http://localhost:8080/clients', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData)
    })
        .then(response => {
            if (response.ok) {
                console.log('Client created successfully');
                document.getElementById('successMessage').style.display = 'block';
                document.getElementById('createClientForm').reset();
            } else {
                console.error('Failed to create client');
                // Optionally show an error message
            }
        })
        .catch(error => {
            console.error('Error:', error);
        });
});