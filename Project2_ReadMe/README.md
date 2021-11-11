# Project2_ReadMe #

## Project Description ##  
ReadMe.com is a single page application with the main objective of creating an online book store that allows users to search and save/purchase a book online based on Title, Author, ISBN, Publisher or Genre. 

## Technologies Used ##  
* Java (Hibernate, Jackson Databind, Lombok, Log4J, JUnit, Mockito)
* Spring Framework (Spring Boot Starter, Data, MVC, ORM, Java Mail)
* Angular 2+ (TypeScript, HTML, CSS, Bootstrap)
* PostgreSQL
* AWS (RDS, EC2, S3)
* Jenkins (CI Pipeline)

## Features ##
* Login/Logout
  - Login also includes a "forgot password" link that prompts the user for their username. If the username exists in the database, the application will send a new password to the email address associated with the provided username.
* Registration
  - The form asks for the user's first name, last name, email, and username.
  - Upon successful registration, the application will send the new user an email, giving them their temporary password.
    - Passwords are encrypted with the SHA-256 algorithm and securely stored in the database.
* Users can view their profile, view past orders, add books to their cart, or add books to their bookmarks.
  - User Profile: 
    - Displays their full name, email address, and username. 
    - Users have option to change their password. On a successful change, the application sends an email to the user to let them know their password has been updated.
  - Past Orders:
    - Displays all the books purchased by the user.
  - Cart:
    - Displays the books that have been added to the cart for purchase.
    - Users have the option to move books to their Bookmarks or remove them.
    - Users can checkout their books.
  - Bookmarks:
    - Displays the books that have been saved to view for later.
    - Users have the option to move books to their Cart or remove them.
* Users can search for books and filter their search results by author, title, ISBN, publisher, and genre.
  - Users will see a catalog page of the books that match the search keyword.
  - When a user selects a book from the catalog, they will be redirected to a more detailed page, where they can view the following information:
    - Title
    - Price
    - Book cover
    - Summary
    - Author
    - Genre
    - Publisher
    - Publication date
    - 'Add To Cart' option
    - 'Add To Bookmarks' option
