pipeline {
    agent {
        label 'build'
    }
    stages {
        stage('Build') {
            steps {
				def thing = load 'Thing.groovy'
				echo thing.doStuff()
                
                sh 'echo $var'
                sh 'mvn clean package jxr:jxr pmd:pmd pmd:cpd findbugs:findbugs checkstyle:checkstyle'
                
                script {
					def thing = load 'Thing.groovy'
					echo thing.doStuff()
                }
            }
        }
    }
}
