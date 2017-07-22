pipeline {
    agent {
        label 'build'
    }
    stages {
        stage('Build') {
            steps {
				//def thing = load 'Thing.groovy'
				// echo thing.doStuff()

                sh 'echo $var'
                sh 'mvn clean package jxr:jxr pmd:pmd pmd:cpd findbugs:findbugs checkstyle:checkstyle'

                script {
					def thing = load 'Thing.groovy'
					echo thing.doStuff()
					
					step([$class: 'hudson.plugins.checkstyle.CheckStylePublisher', pattern: '**/target/checkstyle-result.xml', unstableTotalAll:'0'])
					step([$class: 'hudson.plugins.pmd.PmdPublisher', pattern: '**/target/pmd.xml', unstableTotalAll:'0'])
					step([$class: 'hudson.plugins.findbugs.FindBugsPublisher', pattern: '**/findbugsXml.xml', unstableTotalAll:'0'])
                }                
            }
        }
    }
}
