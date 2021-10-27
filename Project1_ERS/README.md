# Project1_ERS #

## Project Description ##  
A full stack expense reimbursement application that essentially allows a business to track reimbursement requests that were submitted by their employees and a way for financial managers to approve or deny those requests. The application implements a front controller design pattern that provides a centralized request handler that authenticates users, dispatches different types of requests from users, and updates front-end displays in response to those requests. Users will interact with the application's login portal to sign into their specific account to access the services of the application. If a user doesn't already have an account, they can register for one via the registration form, which will be sent to the application's web container (Apache Tomcat Server). The front controller will process the information and save the user credentials with encryption to a PostgreSQL database. All interactions with the front-end application is logged using Log4J to provide accurate context about what the user is doing and what is happening behind-the-scenes in the back-end logic.  

## Technologies Used ##  
* Java (JDBC, Servlets, Apache Tomcat, Jackson Databind, Log4J, JUnit, Mockito)
* JavaScript, HTML, CSS
* PostgreSQL
* AWS RDS

## Features ##

## Usage ##
