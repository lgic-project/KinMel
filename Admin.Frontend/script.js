// Handle form submission

window.addEventListener('load', function () {
    // Check if kinmel-token cookie is present and has a value
    var token = getCookie('kinmel-token');
    if (token) {
        console.log(token);
        // Redirect to index.html
        window.location.href = 'dashboard.html';
    }
});

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


document.getElementById('signin-form').addEventListener('submit', function (event) {
    event.preventDefault(); // Prevent form submission

    // Get form values
    var email = document.getElementsByName('email')[0].value;
    var password = document.getElementsByName('password')[0].value;

    // Check if email or password is empty
    if (email === '' || password === '') {
        // Show Bootstrap popup message
        showAlert('Some fields are empty.', 'danger');
    } else {
        // Perform sign in logic here
        var xhr = new XMLHttpRequest();
        console.log(email);
        console.log(password);
        xhr.open('GET', 'http://localhost:8080/kinMel/login?username=' + email + '&password=' + password);
        xhr.onreadystatechange = function () {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status === 200) {
                    var response = JSON.parse(xhr.responseText);
                    if (response.token) {
                        console.log(response.token);
                        // Save token in a cookie
                        document.cookie = "kinmel-token=" + response.token + "; path=/";
                        // Redirect to index.html
                        console.log(response.token);
                        window.location.href = 'dashboard.html';
                    } else {
                        // Handle failed login case (when response.token is falsy)
                        showAlert('Login failed.', 'danger');
                    }
                } else if (xhr.status === 401 || xhr.status === 403) {
                    // Handle unauthorized or forbidden case (assuming these status codes indicate failed login)
                    showAlert('Invalid email or password.', 'danger');
                } else {

                    showAlert('An error occurred.', 'danger');
                }
            }
        };
        xhr.send();
    }
});

function showAlert(message, type) {
    // Remove any existing alert
    var existingAlert = document.querySelector('.alert');
    if (existingAlert) {
        existingAlert.remove();
    }

    // Create the alert element
    var alertElement = document.createElement('div');
    alertElement.className = `alert alert-${type} alert-dismissible fade show`;
    alertElement.setAttribute('role', 'alert');
    alertElement.innerHTML = message;
    alertElement.innerHTML += '<button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>';

    // Append the alert element to the form
    var form = document.getElementById('signin-form');
    form.appendChild(alertElement);
}