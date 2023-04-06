def call() {
    if (!env.sonar_extra_opts) {
        env.sonar_extra_opts = ""
    }
    pipeline {
        agent any

        stages {

            stage('Compile/Build') {
                steps {
                    sh 'env'
                    sh 'exit 1'
                    //To fail the job
                    script {
                        common.compile()
                    }
                }
            }

            stage('Test Cases') {
                steps {
                    script {
                        common.testcases()
                    }
                }
            }

            stage('Code Quality') {
                steps {
                    script {
                        common.codequality()
                    }
                }
            }
        }
        post {
            failure {
                mail body: "<h1>${component} - Pipeline Failed \n ${BUILD_URL}</h1>", from: 'sivakumarit42@gmail.com', subject: "${component} - Pipeline Failed", to: 'sivakumarit42@gmail.com',  mimeType: 'text/html'
            }
        }

    }
}