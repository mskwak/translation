pipeline {
    agent {
        label 'build'
    }
/*    
    environment {
        BRANCH = 'develop/hibernate'
        MVN = '/opt/maven/bin/mvn'
    }
*/
	stages {
		stage('Do-Build') {
		            environment {
		                BRANCH = 'develop/hibernate'
		                MVN = '/opt/maven/bin/mvn'
			   CREDENTIAL_ID = 'kdamduck77'
			   GIT_URL = 'https://github.com/mskwak/translation.git'
		            }
		            
		        steps {
				echo pwd()
				checkout([$class: 'GitSCM', branches: [[name: "${BRANCH}"]], doGenerateSubmoduleConfigurations: false, extensions: [], submoduleCfg: [], userRemoteConfigs: [[credentialsId: "${CREDENTIAL_ID}", url: "${GIT_URL}"]]])
				sh '"${MVN}" clean package jxr:jxr pmd:pmd pmd:cpd findbugs:findbugs checkstyle:checkstyle cobertura:cobertura -Dcobertura.report.format=xml'
		            }

			post {
				success {
			        		step([$class: 'hudson.plugins.cobertura.CoberturaPublisher', autoUpdateHealth: false, autoUpdateStability: false, coberturaReportFile: '**/coverage.xml', failUnhealthy: false, failUnstable: false, maxNumberOfBuilds: 0, onlyStable: false, sourceEncoding: 'ASCII', zoomCoverageChart: false])
			        		step([$class: 'hudson.plugins.checkstyle.CheckStylePublisher', pattern: 'target/checkstyle-result.xml', canRunOnFailed: true,])
			        		step([$class: 'hudson.plugins.pmd.PmdPublisher', pattern: '**/target/pmd.xml', canRunOnFailed: true,])
			        		step([$class: 'hudson.plugins.findbugs.FindBugsPublisher', pattern: '**/findbugsXml.xml', canRunOnFailed: true,])
				}
			}
		}

		stage('Engine-Build') {

		}
	}
}

/*
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
				
									
				////
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
*/

def test(command) {
	echo command
	
	def lsls = "ls -al".execute().text
	println(lsls)
	
/*	
	def lsls = "ls -al".execute().text
	def sb = new StringBuffer()
	lsls.waitForProcessOutput(sb, System.err)
	println(sb.toString())
	
	
	def before = "git rev-parse refs/remotes/origin/master^{commit}".execute().text
	
	"git fetch --tags --progress https://github.com/mskwak/translation.git +refs/heads/*:refs/remotes/origin/*".execute()
	
	def after = "git rev-parse refs/remotes/origin/master^{commit}".execute().text
*/
}

/*
git rev-parse refs/remotes/origin/master^{commit} 
# 3925dbc3e1e99e2dd0732a5935a7476d6fa36adc

git fetch --tags --progress https://github.com/mskwak/translation.git +refs/heads/*:refs/remotes/origin/*

git rev-parse refs/remotes/origin/master^{commit}
# dbbf7b3fd24daca2456c398dbcdb255446d22ed5

-> sha1 값이 같으면 빌드 수행 안함. 다르면 빌드 수행 함.
*/