pipeline {
    agent any
    tools {
        maven 'Maven3'
        jdk 'JDK-25'
    }

    environment {
        PATH = "C:\\Program Files\\DockerDesktop\\resources\\bin;${env.PATH}"
        DOCKERHUB_CREDENTIALS_ID = 'docker'
        DOCKERHUB_REPO = 'sinyalohis/flashcard'
        DOCKER_IMAGE_TAG = 'v1'
    }
    stages {
        stage ('check'){
            steps{
                git branch: 'main',
                    url: 'https://github.com/JanetteJK/Ohjelmistotuotantoprojekti_S26.git'
            }
        }
        stage ('build'){
            steps{
                bat 'java -version'
                bat 'mvn -version'
                bat 'mvn clean install'
            }
        }

        stage('test') {
            steps{
                bat 'mvn test'
            }
        }
        stage('jacoco'){
            steps{
                jacoco()
            }
        }

    }
}