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
        // step responsable for integration with SonarQube and execute Sonar scanner to static analysis
        stage('Sonar Analysis') {
            environment {
                scannerHome = tool 'SONARQUBE'
            }
            steps {
                withSonarQubeEnv('SONAR_LOCAL') {
                    sh "${scannerHome}/bin/sonar-scanner -e -Dsonar.projectKey=DeployBackEnd -Dsonar.host.url=http://localhost:9000 -Dsonar.login=c9c0ba6ef0cbe196f98514f4bda6830d972636f5 -Dsonar.java.binaries=target -Dsonar.coverage.exclusions=**/.mvn/**,**/src/test/**,**/model/**,**Application.java,**/controller/RootController.java"
                }
            }
        }
        // step responsable for wait response SonarQube 
         stage('Quality Gate') {
           steps {
               waitForQualityGate abortPipeline: true
           }
        }
    }
}