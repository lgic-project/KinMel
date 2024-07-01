// Assuming you're using fetch to retrieve the data from the API
fetch('http://localhost:8080/kinMel/categories/request', {
    method: 'GET',
    headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + getCookie('kinmel-token')
    }
})
.then(response => response.json())
.then(data => {
    // Get the table body element
    const tableBody = document.querySelector('#myTable1 tbody');

    // Clear any existing rows
    tableBody.innerHTML = '';

    // Loop through the data array and create table rows
    data.data.forEach(catreq => {
        const row = document.createElement('tr');
        row.id = `catreq-${catreq.category_id}`;

        // Create table cells and populate them with category data
        const idCell = document.createElement('td');
        idCell.textContent = catreq.category_id;
        row.appendChild(idCell);

        const nameCell = document.createElement('td');
        nameCell.textContent = catreq.category_name;
        row.appendChild(nameCell);

        const descriptionCell = document.createElement('td');
        descriptionCell.textContent = catreq.category_description;
        row.appendChild(descriptionCell);

        const imageCell = document.createElement('td');
        const image = document.createElement('img');
        image.src = `http://localhost:8080/${catreq.imagePath}`;
        image.alt = 'Category Image';
        image.style.width = '50px';
        image.style.height = '50px';
        imageCell.appendChild(image);
        row.appendChild(imageCell);

        const approveCell = document.createElement('td');
        const approveButton = document.createElement('button');
        approveButton.textContent = 'Approve';
        approveButton.classList.add('btn', 'btn-success');
        approveButton.setAttribute('data-bs-toggle', 'modal');
        approveButton.setAttribute('data-bs-target', '#approveModal');
        approveButton.addEventListener('click', handleApproveButtonClick);

        function handleApproveButtonClick(event) {
            // Get the closest table row (assuming the button is inside the row)
            const selectedRow = event.target.closest('tr');
            // Access the ID of the selected row (assuming it has an ID attribute)
            const selectedRowId = selectedRow.id;
            console.log("Selected row ID:", selectedRowId);
            const id = parseInt(selectedRowId.split('-')[1]);
            // Set the data-seller-id attribute with the extracted integer ID
            const modalElement = document.getElementById("approveModal");
            modalElement.dataset.sellerId = id;
        }

        approveCell.appendChild(approveButton);
        row.appendChild(approveCell);

        const deleteCell = document.createElement('td');
        const deleteButton = document.createElement('button');
        deleteButton.textContent = 'Delete';
        deleteButton.classList.add('btn', 'btn-danger');
        deleteButton.setAttribute('data-bs-toggle', 'modal');
        deleteButton.setAttribute('data-bs-target', '#deleteModal');
        deleteButton.addEventListener('click', handleDeleteButtonClick);

        function handleDeleteButtonClick(event) {
            // Get the closest table row (assuming the button is inside the row)
            const selectedRow = event.target.closest('tr');
            // Access the ID of the selected row (assuming it has an ID attribute)
            const selectedRowId = selectedRow.id;
            console.log("Selected row ID:", selectedRowId);
            const id = parseInt(selectedRowId.split('-')[1]);
            // Set the data-seller-id attribute with the extracted integer ID
            const modalElement = document.getElementById("deleteModal");
            modalElement.dataset.sellerId = id;
        }

        deleteCell.appendChild(deleteButton);
        row.appendChild(deleteCell);

        // Append the row to the table body
        tableBody.appendChild(row);
    });
})
.catch(error => {
    console.error('Error fetching data:', error);
});

function deleteForm() {
    const modal = document.getElementById('deleteModal');
    const sellerId = modal.getAttribute('data-seller-id');
    console.log(`Deleting customer with ID ${sellerId}`);
    // Implement your delete seller logic here
    fetch(`http://localhost:8080/kinMel/categories/reject/${sellerId}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + getCookie('kinmel-token')
        }
    })
    .then(response => response.json())
    .then(data => {
        if (data.status === 200 && data.data === 'Rejected the category') {
            console.log(data.statusValue);
            alert(data.data); // Show the success message
            // Optionally, remove the approved row from the table
            const bootstrapModal = bootstrap.Modal.getInstance(modal);
            bootstrapModal.hide();

            // Remove the backdrop manually
            const backdrop = document.querySelector('.modal-backdrop');
            if (backdrop) {
                backdrop.parentNode.removeChild(backdrop);
            }
            const row = document.getElementById(`catreq-${sellerId}`);
            if (row) {
                row.remove();
            }
        } else {
            console.error('Failed to remove');
            alert('Failed to remove category request');
        }
    })
    .catch(error => {
        console.error('Error rejecting category request:', error);
        alert('Error rejecting category request');
    });

}

function approveForm() {
    const modal = document.getElementById('approveModal');
    const sellerId = parseInt(modal.getAttribute('data-seller-id'), 10);
    console.log(`Approving customer with ID ${sellerId}`);

    fetch(`http://localhost:8080/kinMel/categories/approve/${sellerId}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + getCookie('kinmel-token')
        }
    })
    .then(response => response.json())
    .then(data => {
        if (data.status === 200 && data.data === 'Successfully approved') {
            console.log(data.statusValue);
            alert(data.data); // Show the success message
            // Optionally, remove the approved row from the table
            const bootstrapModal = bootstrap.Modal.getInstance(modal);
            bootstrapModal.hide();

            // Remove the backdrop manually
            const backdrop = document.querySelector('.modal-backdrop');
            if (backdrop) {
                backdrop.parentNode.removeChild(backdrop);
            }
            const row = document.getElementById(`catreq-${sellerId}`);
            if (row) {
                row.remove();
            }
        } else {
            console.error('Failed to approve category request');
            alert('Failed to approve category request');
        }
    })
    .catch(error => {
        console.error('Error approving category request:', error);
        alert('Error approving category request');
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
