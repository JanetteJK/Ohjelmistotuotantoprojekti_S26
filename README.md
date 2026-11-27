# Flashers - flashcard application for studying

# Overview and Project Objectives

Flashers is a software which allows for the easy creation of and use of digital flashcards. By handling the flashcards digitally, they can be shared with users remotely and in multiples. This way, students and teachers can share their study materials with other students without unnecessary waste of resources or time.

The target audience for the application is students and teachers. Users are able to create an account, log in and create and view their own flashcards. They can also delete useless flashcards.


# Technologies and Dependencies

| Area           | Technology       |
|----------------|------------------|
| Language       | Java JDK 25      |
| Build tool     | Maven            |
| User Interface | JavaFX 20        |
| Database       | MariaDB          |
| Testing        | JUnit 5, Mockito |
| Code coverage  | JaCoCo           |
| CI/CD          | Jenkins          |
| Code Cleanup   | SonarQube        |


# Design and Development Methodology

The main program uses the MVC model, with multiple controller classes to handle the individual pages of the program.


# Functional Testing
## Unit testing
Unit test were implemented by using JUnit 5. Mockito was used to mock database and especially JavaFX components to prevent tests from opening GUI windows.
## Jenkins
Jenkins was integrated to for CI/CD. The pipeline is as follows: Git repository >> Jenkins >> Maven build >> JUnit tests >> JACOCO  >> Docker image >> Docker Hub
## Localisation
Localisation was implemented by using resource bundles. The application supports English, Finnish, Korean and Japanese. The user can change the language before signing in. The database localisation is handled by a translation table, which contains the translations for the flashcards.

# Set-Up
## Requirements

##### Install:
- Java JDK 25
- Maven
- MariaDB

##### For Docker setup:
- Docker Desktop
- Xming

## Getting the application
- Clone repository
  - git clone <https://github.com/JanetteJK/Ohjelmistotuotantoprojekti_S26.git> 
- Set up database using the sql script that can be found in Documents
- Build and run the project
  - mvn clean install
- Launch the application from the Main class

### Supported languages
- English
- Finnish
- Korean
- Japanese
