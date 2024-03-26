document.getElementById('getClientForm').addEventListener('submit', function(event) {
    event.preventDefault(); // Prevent the default form submission

    // Get client email
    const clientEmail = document.getElementById('clientEmail').value;

    // Send GET request to backend to get client info
    fetch(`http://localhost:8080/clients/${clientEmail}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('Клиент не найден в базе данных.');
            }
        })
        .then(data => {
            // Show client info block
            document.getElementById('error').style.display = 'none';
            document.getElementById('clientInfo').style.display = 'block';

            // Update client info on the page
            document.getElementById('fullName').textContent = data.fullName;
            document.getElementById('email').textContent = data.email;

            // Send another GET request to backend to get client cards
            fetch(`http://localhost:8080/cards/${clientEmail}`, {
                method: 'GET',
                headers: {
                    'Content-Type': 'application/json'
                }
            })
                .then(response => {
                    if (response.ok) {
                        return response.json();
                    } else {
                        throw new Error('Error fetching client cards.');
                    }
                })
                .then(cardsData => {
                    // Update client cards on the page
                    const clientCardsUl = document.getElementById('clientCards');
                    clientCardsUl.innerHTML = ''; // Clear previous cards

                    if (Array.isArray(cardsData)) {
                        cardsData.forEach(card => {
                            const cardElement = document.createElement('li');
                            cardElement.textContent = `Card Number: ${card.cardNumber}`;
                            clientCardsUl.appendChild(cardElement);
                        });
                    } else {
                        throw new Error('Client cards data is not an array.');
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                });
        })
        .catch(error => {
            // Hide client info block
            document.getElementById('clientInfo').style.display = 'none';

            // Show error message
            document.getElementById('error').style.display = 'block';
            document.getElementById('error').textContent = error.message;
        });
});
