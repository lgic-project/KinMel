// Assuming you're using fetch to retrieve the data from the API
fetch('http://localhost:8080/kinMel/users/role/3')
    .then(response => response.json())
    .then(data => {
        // Get the table body element
        const tableBody = document.querySelector('#myTable tbody');

        // Clear any existing rows
        tableBody.innerHTML = '';

        // Loop through the data array and create table rows
        data.data.forEach(seller => {
            const row = document.createElement('tr');
            row.id = `seller-row-${seller.userId}`;

            // Create table cells and populate them with seller data
            const idCell = document.createElement('td');
            idCell.textContent = seller.userId;
            row.appendChild(idCell);

            const nameCell = document.createElement('td');
            nameCell.textContent = `${seller.first_name} ${seller.last_name}`;
            row.appendChild(nameCell);

            const emailCell = document.createElement('td');
            emailCell.textContent = seller.email;
            row.appendChild(emailCell);

            const phoneCell = document.createElement('td');
            phoneCell.textContent = seller.phoneNumber;
            row.appendChild(phoneCell);

            const deleteCell = document.createElement('td');
            const deleteButton = document.createElement('button');
            deleteButton.textContent = 'Delete';
            deleteButton.classList.add('btn', 'btn-danger');
            // Add Bootstrap modal attributes to the delete button
deleteButton.setAttribute('data-bs-toggle', 'modal');
deleteButton.setAttribute('data-bs-target', '#exampleModal');
deleteButton.addEventListener('click', handleDeleteButtonClick);

// Function to handle delete button click
function handleDeleteButtonClick(event) {
  // Get the closest table row (assuming the button is inside the row)
  const selectedRow = event.target.closest('tr');
  
  // Access the ID of the selected row (assuming it has an ID attribute)
  const selectedRowId = selectedRow.id;
  console.log("Selected row ID:", selectedRowId);
  const id = parseInt(selectedRowId.split('-')[2]);

  // Set the data-seller-id attribute with the extracted integer ID
  const modalElement = document.getElementById("exampleModal");
  modalElement.dataset.sellerId = id;

  // You can call your existing deleteForm function here
  // deleteForm(selectedRowId); // Assuming deleteForm accepts the seller ID
}

// deleteButton.setAttribute('data-seller-id', seller.userId);

            deleteCell.appendChild(deleteButton);
            row.appendChild(deleteCell);

            // Append the row to the table body
            tableBody.appendChild(row);
        });
    })
    .catch(error => {
        console.error('Error fetching data:', error);
    });

// Function to handle delete seller button click
function deleteForm() {
    const modal = document.getElementById('exampleModal');

    const sellerId = modal.getAttribute('data-seller-id');
    // Implement your delete seller logic heres
    console.log(`Deleting seller with ID ${sellerId}`);

    // Send a PUT request to block the seller
    fetch(`http://localhost:8080/kinMel/users/block/${sellerId}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + getCookie('kinmel-token')
        }
    })
        .then(response => {
            if (response.ok) {
                console.log('Seller deleted successfully');

                // Show a success message to the user
                // You can use a toast or any other UI element to display the message
                // Reload the page
                location.reload();
            } else {
                console.error('Failed to delete seller');
                // Show an error message to the user
                // You can use a toast or any other UI element to display the message
            }
        })
        .catch(error => {
            console.error('Error deleting seller:', error);
            // Show an error message to the user
            // You can use a toast or any other UI element to display the message
        });
  }
  function getCookie(name) {
    var cookieArr = document.cookie.split(';');
    for (var i = 0; i < cookieArr.length; i++) {
        var cookiePair = cookieArr[i].split('=');
        if (name === cookiePair[0].trim()) {
            console.log(cookiePair[1]);
            return decodeURIComponent(cookiePair[1]);
        }
    }
    return null;
}