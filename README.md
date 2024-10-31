# Cupcake Ordering System

## Overview
This project is a Cupcake Ordering System built with Java, SQL, and Maven. It allows users to create and manage cupcake orders, including selecting bottoms and toppings, adding items to a basket, and processing payments.
Administrators have additional capabilities to manage user accounts and view all orders.

## Features
- User authentication and session management
- Add cupcakes to a basket
- View and manage orders
- Admin functionalities to view all orders

## Technologies Used
- Java
- SQL (PostgreSQL)
- Maven
- Javalin (Web Framework)
- Thymeleaf

## Setup and Installation

### Prerequisites
- Java 11 or higher
- Maven
- PostgreSQL

### Database Setup
1. Create a PostgreSQL database.
2. Run the SQL script located at `src/main/resources/cupcake.sql` to set up the database schema and initial data.

### Application Setup
1. Clone the repository:
    ```sh
    git clone https://github.com/yourusername/cupcake-ordering-system.git
    cd cupcake-ordering-system
    ```
2. Configure the database connection in `src/main/resources/application.properties`.
3. Build the project using Maven:
    ```sh
    mvn clean install
    ```
4. Run the application:
    ```sh
    mvn exec:java
    ```

## Usage
- Access the application at `http://localhost:7000`.
- Use the provided UI to add cupcakes to your basket and manage orders.

## Project Structure
- `src/main/java/app/controllers`: Contains the controllers for handling HTTP requests.
- `src/main/java/app/entities`: Contains the entity classes.
- `src/main/java/app/persistence`: Contains the mappers for database operations.
- `src/main/resources`: Contains SQL scripts and configuration files.

## Contributing
1. Fork the repository.
2. Create a new branch (`git checkout -b feature-branch`).
3. Commit your changes (`git commit -am 'Add new feature'`).
4. Push to the branch (`git push origin feature-branch`).
5. Create a new Pull Request.

## License
This project is licensed under the MIT License.

## Kanban Board
Track the project progress on the [Kanban Board](https://github.com/users/MojoOno/projects/1).
