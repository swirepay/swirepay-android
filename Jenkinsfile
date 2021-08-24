pipeline {
	agent { label 'z-java-agent' }
	environment {
		GIT_SSH_COMMAND = "ssh -o StrictHostKeyChecking=no"
		SONAR_LOGIN = credentials('SONAR_LOGIN')
		SONAR_HOST = "https://sonarqube.swirepay.com"
		GITHUB_URL = "github.com/swirepay/swirepay-android"
	}
	options { timestamps() }
	stages {
		stage('Bit Bucket push') {
		    when {
			    anyOf {
				    branch 'develop'
					branch 'task/*'
          allOf {
            branch 'master'
            triggeredBy 'user'
				}
			}
    }  
		steps {
      withCredentials([
          usernamePassword(
              credentialsId: 'github_credential',
              usernameVariable: 'GIT_USERNAME',
              passwordVariable: 'GIT_PASSWORD'
          )
      ])  {
        sh("""
          git checkout ${env.GIT_BRANCH}
          git remote -v
          git branch -a
          git push -f https://${GIT_USERNAME}:${GIT_PASSWORD}@${GITHUB_URL} ${env.GIT_BRANCH}
        """)
        }
      }
    }
  }
}
