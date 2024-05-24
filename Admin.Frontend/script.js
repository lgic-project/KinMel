// Handle form submission
document.getElementById('signin-form').addEventListener('submit', function (event) {
    event.preventDefault(); // Prevent form submission

    // Get form values
    var email = document.getElementsByName('email')[0].value;
    var password = document.getElementsByName('password')[0].value;

    // Check if email or password is empty
    if (email === '' || password === '') {
        // Show Bootstrap popup message
        var alertElement = document.createElement('div');
        alertElement.className = 'alert alert-danger alert-dismissible fade show';
        alertElement.setAttribute('role', 'alert');
        alertElement.innerHTML = 'Some fields are empty.';
        alertElement.innerHTML += '<button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>';

        // Append the alert element to the form
        var form = document.getElementById('signin-form');
        form.appendChild(alertElement);
    } else {

        // Perform sign in logic here
        var xhr = new XMLHttpRequest();
        xhr.open('GET', 'http://192.168.1.67:8080/kinMel/login?username=' + email + '&password=' + password);
        xhr.onreadystatechange = function () {
            if (xhr.readyState === XMLHttpRequest.DONE) {
                if (xhr.status === 200) {
                    var response = JSON.parse(xhr.responseText);
                    if (response.token) {
                        console.log(response.token);
                        // Redirect to index.html
                        window.location.href = 'index.html';
                    } else {
                        // Show login failed message
                        var alertElement = document.createElement('div');
                        alertElement.className = 'alert alert-danger alert-dismissible fade show';
                        alertElement.setAttribute('role', 'alert');
                        alertElement.innerHTML = 'Login failed.';
                        alertElement.innerHTML += '<button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>';

                        // Append the alert element to the form
                        var form = document.getElementById('signin-form');
                        form.appendChild(alertElement);
                    }
                } else {
                    // Show error message
                    var alertElement = document.createElement('div');
                    alertElement.className = 'alert alert-danger alert-dismissible fade show';
                    alertElement.setAttribute('role', 'alert');
                    alertElement.innerHTML = 'An error occurred.';
                    alertElement.innerHTML += '<button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>';

                    // Append the alert element to the form
                    var form = document.getElementById('signin-form');
                    form.appendChild(alertElement);
                }
            }
        };
        xhr.send();
    }
});
