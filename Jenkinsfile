pipeline {
    agent {
        label 'build'
    }
    stages {
        stage('Build') {
            steps {
                sh 'mvn clean package pmd:pmd pmd:cpd findbugs:findbugs checkstyle:checkstyle'
            }
        }
    }
}
