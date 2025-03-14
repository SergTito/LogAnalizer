pipeline {
    agent any

    environment {
        IMAGE_NAME = 'sergtito/log-analytics-system'
        DOCKER_CREDENTIALS_ID = 'docker-hub-credentials'
    }

    stages {
        stage('Checkout Code') {
            steps {
                git branch: 'master', url: 'https://github.com/SergTito/LogAnalizer.git'
            }
        }

        stage('Login to Docker Hub') {
            steps {
                withDockerRegistry([credentialsId: DOCKER_CREDENTIALS_ID, url: '']) {
                    sh 'echo "Docker login successful!"'
                }
            }
        }
    }
}
