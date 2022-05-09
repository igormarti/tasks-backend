pipeline {
    agent any
    stages {
        // step responsable for make build project BackEnd
        stage('Build Backend') {
            steps {
                sh 'mvn clean package -DskipTests=true'
            }
        }
        // step responsable for execute unit tests in BackEnd
        stage('Unit Tests') {
            steps {
                sh 'mvn test'
            }
        }
        // step responsable for integration with SonarQube and wait Quality Gate response
        stage('Sonar Analysis') {
           steps {
               waitForQualityGate abortPipeline: true
           }
        }

    }
}