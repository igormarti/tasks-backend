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
        // step responsable for wait Quality Gate response in the SonarQube 
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
        // step responsable for make deploy BackEnd in the tomcat
        stage('Deploy BackEnd') {
            steps {
                deploy adapters: [tomcat8(credentialsId: 'tomcat_login', path: '', url: 'http://localhost:8081/')], contextPath: 'tasks-backend', onFailure: false, war: 'target/tasks-backend.war'
            }
        }
        // step responsable for make tests in the API
        stage('API Tests') {
            steps {
                dir('api-tests'){
                    git credentialsId: 'login_github', url: 'https://github.com/igormarti/tasks-api-tests'
                    sh 'mvn test'
                }
            }
        }
        // step responsable for make deploy FrontEnd
        stage('Deploy FrontEnd') {
            steps {
                dir('tasks-frontend'){
                    git credentialsId: 'login_github', url: 'https://github.com/igormarti/tasks-frontend'
                    sh 'mvn clean package'
                    deploy adapters: [tomcat8(credentialsId: 'tomcat_login', path: '', url: 'http://localhost:8081/')], contextPath: 'tasks', onFailure: false, war: 'target/tasks.war'
                }
            }
        }
         // step responsable for make automated tests in the FrontEnd
        stage('Automated Tests') {
            steps {
                dir('tasks-functional-test'){
                    git credentialsId: 'login_github', url: 'https://github.com/igormarti/tasks-functional-test'
                    sh 'mvn test'
                }
            }
        }
         // step responsable for make deploy Database, BackEnd and FrontEnd in the production
        stage('Deploy Production') {
            steps {
               sh  'docker-compose build'
               sh  'docker-compose up -d'
            }
        }
        // step responsable for make HealthCheck in the FrontEnd
        stage('Health Check') {
            steps {
                sleep(25)
                dir('tasks-functional-test'){
                    sh 'mvn verify -Dskip.surefire.tests'
                }
            }
        }
    }
    post {
        always {
            junit allowEmptyResults: true, testResults: 'target/surefire-reports/*.xml, api-tests/target/surefire-reports/*.xml, tasks-functional-test/target/surefire-reports/*.xml, tasks-functional-test/target/failsafe-reports/*.xml'
        }
    }
}