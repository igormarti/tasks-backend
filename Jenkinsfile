pipeline {
    agent any
    stages {
        stage('Build Backend') {
            steps {
                sh 'mvn clean package -DskipTests=true'
            }
        }
        stage('Unit Tests') {
            steps {
                sh 'mvn test'
            }
        },
        stage('Sonar Analysis') {
            environment {
                scannerHome = tool 'SONARQUBE'
            }
            steps {
                withSonarQubeEnv('SONAR_LOCAL') {
                    sh "${scannerHome}/bin/sonar-scanner -e -Dsonar.projectKey=DeployBackEnd -Dsonar.host.url=http://localhost:9000 -Dsonar.login=c9c0ba6ef0cbe196f98514f4bda6830d972636f5 -Dsonar.java.binaries=target -Dsonar.coverage.exclusions=**/.mvn/**, **/src/test/**,**/model/**,**Application.java, **/controller/RootController.java"
                }
            }
        }
    }
}