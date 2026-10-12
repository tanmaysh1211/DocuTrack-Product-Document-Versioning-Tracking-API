pipeline {
    agent any

    tools {
        jdk 'jdk-17'
        maven 'maven-3'
    }

    options {
        timestamps()
        disableConcurrentBuilds()
    }

    stages {
        stage('Checkout') {
            steps { checkout scm }
        }

        stage('Test') {
            steps { sh 'mvn --batch-mode clean verify' }
            post {
                always {
                    junit testResults: 'target/surefire-reports/*.xml', allowEmptyResults: true
                    archiveArtifacts artifacts: 'target/site/jacoco/**', allowEmptyArchive: true
                }
            }
        }

        stage('Container Build') {
            steps { sh 'docker build -t design-ledger-api:${BUILD_NUMBER} .' }
        }
    }
}
