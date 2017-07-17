pipeline {
    agent {
        label 'build'
    }
    stages {
        stage('Build') {
            steps {
                sh 'echo $var'
                sh 'mvn clean package pmd:pmd pmd:cpd findbugs:findbugs checkstyle:checkstyle'
            }
        }
    }
}
