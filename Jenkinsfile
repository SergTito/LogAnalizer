pipeline {
    agent any

    environment {
        IMAGE_NAME = 'sergtito/log-analytics-system'
        DOCKER_CREDENTIALS_ID = 'docker-hub-credentials'
    }

    stages {
        stage('Login to Docker Hub') {
            steps {
                withDockerRegistry([credentialsId: DOCKER_CREDENTIALS_ID, url: '']) {
                    sh 'echo "Docker login successful!"'
                }
            }
        }
    }
}
