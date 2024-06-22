fetch('http://localhost:8080/kinMel/category/details', {
    headers: {
        'Authorization': 'Bearer ' + getCookie('kinmel-token')
    }
})
    .then(response => response.json())
    .then(data => {
        // Process the response data
        console.log(data);

        const categoryNames = data.data.map(item => item.categoryName);
        const totalOrders = data.data.map(item => item.totalOrdersInEachCategory);

        const ctx = document.getElementById('categoryChart').getContext('2d');
        const categoryChart = new Chart(ctx, {
            type: 'bar', // or 'pie' for a pie chart
            data: {
                labels: categoryNames,
                datasets: [{
                    label: 'Total Orders',
                    data: totalOrders,
                    backgroundColor: [
                        '#FF6384',
                        '#36A2EB',
                        '#FFCE56',
                        '#9966FF',
                        '#4BC0C0',
                        '#FF9F40'
                    ],
                    borderColor: [
                        '#FF6384',
                        '#36A2EB',
                        '#FFCE56',
                        '#9966FF',
                        '#4BC0C0',
                        '#FF9F40'
                    ],
                    borderWidth: 1
                }]
            },
            options: {
                scales: {
                    y: {
                        beginAtZero: true
                    }
                }
            }
        });
    })
    .catch(error => {
        console.error('Error:', error);
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