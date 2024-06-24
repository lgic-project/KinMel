// Fetch data from API
fetch('http://localhost:8080/kinMel/categories')
    .then(response => response.json())
    .then(data => {
        // Get the table body element
        const tableBody = document.querySelector('#myTable tbody');

        // Loop through the data and create table rows
        data.data.forEach(category => {
            // Create a new table row
            const row = document.createElement('tr');

            // Create table cells for each category property
            const categoryIdCell = document.createElement('td');
            categoryIdCell.textContent = category.category_id;
            row.appendChild(categoryIdCell);

            const categoryNameCell = document.createElement('td');
            categoryNameCell.textContent = category.category_name;
            row.appendChild(categoryNameCell);

            const categoryDescriptionCell = document.createElement('td');
            categoryDescriptionCell.textContent = category.category_description;
            row.appendChild(categoryDescriptionCell);

            // Create table cell for image path
            // const imagePathCell = document.createElement('td');
            // imagePathCell.textContent = category.imagePath;
            // row.appendChild(imagePathCell);

            // Create table cell for image
            // Create table cell for image
            const imageCell = document.createElement('td');
            const image = document.createElement('img');
            image.src = `http://localhost:8080/${category.imagePath}`;
            image.alt = 'Category Image';
            image.style.borderRadius = '50%';
            image.style.width = '50px';
            image.style.height = '50px';
            imageCell.appendChild(image);
            row.appendChild(imageCell);

            // Create the Update button
            const updateButton = document.createElement('button');
            updateButton.textContent = 'Update';
            updateButton.className = 'btn btn-danger';

            updateButton.addEventListener('click', () => {
                const categoryData = {
                    category_id: category.category_id,
                    category_name: category.category_name,
                    category_description: category.category_description,
                    imagePath: category.imagePath
                };

                 localStorage.setItem('categoryData', JSON.stringify(categoryData));
                 console.log(categoryData);

                window.location.href = 'update_category.html';
                // Encode data as JSON string





            });
            row.appendChild(updateButton);

            // Append the row to the table body
            tableBody.appendChild(row);
        });
    })
    .catch(error => {
        console.error('Error:', error);
    });



function populateUpdateForm(categoryData) {
    document.getElementById('update-category-id').value = categoryData.category_id;
    document.getElementById('update-category-name').value = categoryData.category_name;
    document.getElementById('update-category-description').value = categoryData.category_description;
    document.getElementById('update-category-image-preview').src = `http://localhost:8080/${categoryData.imagePath}`; // Adjust image path as needed
}


let loginForm = document.getElementById("addCategory");

loginForm.addEventListener("submit", (e) => {
    event.preventDefault();
    var title = document.getElementById('title').value;
    var description = document.getElementById('content').value;
    var image = document.getElementById('choosefile').files[0];

    console.log("Hi");
    console.log(title, description, image);

    if (image && image.type.startsWith('image/')) {
        var reader = new FileReader();
        reader.onloadend = function () {
            var base64Image = reader.result.split(',')[1];
            var imageFormat = image.type.split('/')[1]; // Extract image format from MIME type

            sendData(title, description, base64Image, imageFormat);
            console.log(imageFormat);
        };
        reader.readAsDataURL(image);
    } else {
        alert('Please select a valid image file.');
    }

    if (image && image.type.startsWith('image/')) {
        var reader = new FileReader();
        reader.onloadend = function () {
            var base64Image = reader.result.split(',')[1];
            // sendData(title, description, base64Image);
        };
        reader.readAsDataURL(image);
    } else {
        alert('Please select a valid image file.');
    }

});

function sendData(title, description, base64Image, imageFormat) {
    var data = {
        categoryName: title,
        categoryDescription: description,
        categoryImage: base64Image,
        imageFormat: imageFormat
    };
    console.log(imageFormat);
    console.log(base64Image);

    fetch('http://localhost:8080/kinMel/categories', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'Authorization': 'Bearer ' + getCookie('kinmel-token')
        },
        body: JSON.stringify(data)
    })
        .then(response => response.json())
        .then(data => {
            // Handle response from the API

            // Handle response from the API
            if (data.status === 200 && data.message?.startsWith("Category Added of Type :")) {
                // Clear the form
                document.getElementById('title').value = '';
                document.getElementById('content').value = '';
                document.getElementById('choosefile').value = '';
                const previewDiv = document.getElementById('preview');
                previewDiv.innerHTML = '';
                // Show a popup message
                alert('Category added successfully');
            }


            console.log(data);
        })
        .catch(error => {
            // Handle error
            document.getElementById('title').value = '';
            document.getElementById('content').value = '';
            document.getElementById('choosefile').value = '';
            const previewDiv = document.getElementById('preview');
            previewDiv.innerHTML = '';

            // Show a popup message
            alert('Failed to add category');
            console.error(error);
        });
}

function checkCategoryAdded(text) {
    const regex = /Category Added of Type:/i; // Case-insensitive match
    return regex.test(text);
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
