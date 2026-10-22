# Ohjelmistotuotantoprojekti_S26

# 1. Overview and Project Objectives

Flashers is a software which allows for the easy creation of and use of digital flashcards. By handling the flashcards digitally, they can be shared with users remotely and in multiples. This way, students and teachers can share their study materials with other students without unnecessary waste of resources or time.

The target audience for the application is students and teachers. Users are able to create an account, log in and create and view their own flashcards. They can also delete useless flashcards.

# 2. Technologies and Dependencies
▪ Frontend () --> JavaFX 20

▪ Backend () --> Java JDK 25

▪ Runtime () --> Maven

▪ Database () --> MariaDb

▪ Testing () --> JUnit 5

▪ Dependencies () --> JaCoCo, Jenkins, Mockito

▪ Other tools or frameworks (e.g., authentication, APIs)


# 3. Design and Development Methodology

The main program uses the MVC model, with multiple controller classes to handle the individual pages of the program.

# 4. Functional Testing
## Unit testing
Unit test were implemented by using JUnit 5. Mockito was used to mock database and especially JavaFX components to prevent tests from opening GUI windows.
## Jenkins
Jenkins was integrated to for CI/CD. The pipeline is as follows: Git repository >> Jenkins >> Maven build >> JUnit tests >> JACOCO  >> Docker image >> Docker Hub

# 5. Set-Up
### Requirements
- Java JDK 25
- Maven
- MariaDB

### Getting the application
- Clone repository
  - git clone <https://github.com/JanetteJK/Ohjelmistotuotantoprojekti_S26.git> 
- Set up database using the sql script that can be found in Documents
- Build and run the project
  - mvn clean install
- Launch the application from the Main class
