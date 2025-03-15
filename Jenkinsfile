pipeline {
    agent any
    stages {
        stage('Clone Repository') {
            steps {
                checkout([
                    $class: 'GitSCM',
                    branches: [[name: '*/main']],
                    userRemoteConfigs: [[
                        url: 'https://github.com/SergTito/LogAnalizer.git',
                        credentialsId: 'github-token'
                    ]]
                ])
            }
        }
    }
}
