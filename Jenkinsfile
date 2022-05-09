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
                sleep time: 5000, unit: 'MILLISECONDS'
                script {
                        def response = waitForQualityGate()
                        if (response.status != 'OK') {
                            error "Pipeline aborted due to quality gate failure: ${response.status}"
                        }
                }
           }
        }
        // step responsable for make deploy in tomcat
        stage('Deploy BackEnd') {
            steps {
                deploy adapters: [tomcat8(credentialsId: 'tomcat_login', path: '', url: 'http://localhost:8081/')], contextPath: 'tasks-backend', onFailure: false, war: 'target/tasks-backend.war'
            }
        }
        // step responsable for make tests in API
        stage('Deploy ApiTests') {
            steps {
                dir('api-tests'){
                    git credentialsId: 'login_github', url: 'https://github.com/igormarti/tasks-api-tests'
                    sh 'mvn test'
                }
            }
        }
    }
}