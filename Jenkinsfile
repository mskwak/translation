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

				test 'ls -al'
				
									
				//
				step([$class: 'hudson.plugins.checkstyle.CheckStylePublisher', pattern: 'target/checkstyle-result.xml', canRunOnFailed: true,])
				step([$class: 'hudson.plugins.pmd.PmdPublisher', pattern: '**/target/pmd.xml', canRunOnFailed: true,])
				step([$class: 'hudson.plugins.findbugs.FindBugsPublisher', pattern: '**/findbugsXml.xml', canRunOnFailed: true,])
				
					
                script {
					def thing = load 'Thing.groovy'
					echo thing.doStuff()
					
					test('ls -al')
					
					//step([$class: 'hudson.plugins.checkstyle.CheckStylePublisher', pattern: '**/target/checkstyle-result.xml', unstableTotalAll:'0'])
					//step([$class: 'hudson.plugins.pmd.PmdPublisher', pattern: '**/target/pmd.xml', unstableTotalAll:'0'])
					//step([$class: 'hudson.plugins.findbugs.FindBugsPublisher', pattern: '**/findbugsXml.xml', unstableTotalAll:'0'])					
                }                
            }
        }
    }
}

def test(command) {
	echo command
}