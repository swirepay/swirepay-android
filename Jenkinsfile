pipeline {
	agent { label 'z-java-agent' }
	environment {
		GIT_SSH_COMMAND = "ssh -o StrictHostKeyChecking=no"
		SONAR_LOGIN = credentials('SONAR_LOGIN')
		SONAR_HOST = "https://sonarqube.swirepay.com"
		CODECOMMIT_URL = "bitbucket.org/zetametrics/swirepay-android/src/"
	}
	options { timestamps() }
	stages {
		stage('Bit Bucket push') {
		    when {
			    anyOf {
				    branch 'develop'
					branch 'release/*'
          allOf {
            branch 'master'
            triggeredBy 'user'
				}
			}
    }  
		steps {
      withCredentials([
          usernamePassword(
              credentialsId: 'bitbucket-https',
              usernameVariable: 'GIT_USERNAME',
              passwordVariable: 'GIT_PASSWORD'
          )
      ])  {
        sh("""
          git remote add aws https://${CODECOMMIT_URL}
          git checkout ${env.GIT_BRANCH}
          git remote -v
          git branch -a
          git push -f https://${GIT_USERNAME}:${GIT_PASSWORD}@${CODECOMMIT_URL} ${env.GIT_BRANCH}
        """)
        }
      }
    }
  }
}