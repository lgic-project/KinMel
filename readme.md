# KinMel Ecommerce App

KinMel is an ecommerce application developed for a college project at LA Grandee International College. The app features user authentication, authorization using JWT tokens, and various ecommerce functionalities.

## Technologies Used

- **Backend**: Spring Boot
- **Frontend**: Android Development with Java
- **Database**: SQL Server 2019

## Features

- **User Authentication**: Secure user authentication mechanism implemented using Spring Security and JWT tokens.
  
- **Authorization**: Role-based access control ensuring authorized access to different parts of the application.
  
- **Ecommerce Functionalities**: Includes features typical of an ecommerce platform such as browsing products, adding to cart, checkout, and order management.
  
- **Database Integration**: Utilizes SQL Server 2019 for robust and scalable data storage.

## Installation

To run the KinMel Ecommerce App locally, follow these steps:

1. **Clone Repository**:
    ```bash
    git clone https://github.com/lgic-project/KinMel.git
    ```

2. **Backend Setup**:

    - Ensure you have Java and Maven installed.
    - Navigate to the backend directory.
    - Configure `application.properties` with your SQL Server database details.
    - Run the Spring Boot application:
      ```bash
      mvn spring-boot:run
      ```

3. **Frontend Setup**:

    - Open the Android project in Android Studio (frontend directory).
    - Connect the frontend to the running backend server by updating API URLs if necessary.
    - Build and run the Android app on an emulator or a physical device.

## Usage

- Register a user account or log in using existing credentials.
- Browse through available products, add items to the cart.
- Proceed to checkout and place orders.
- View order history and manage account details.

## Contributors

This project was developed by:

- [Suman Devkota](https://github.com/Suman8175) (@Suman8175)
- [Arpan Pokhrel](https://github.com/arpan-asus) (@arpan-asus)
- [Navina Budhathoki](https://github.com/navinamagar) (@navinamagar)

