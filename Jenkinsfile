pipeline {
    agent {
        label 'build'
    }
    stages {
        stage('Build') {
            steps {
                sh 'echo "Hello World"'
                sh 'mvn clean package'
                sh '''
                    echo "Multiline shell steps works too"
                    ls -lah
                '''
            }
        }
    }
}
