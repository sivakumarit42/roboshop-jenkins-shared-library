def call() {
    if (!env.sonar_extra_opts) {
        env.sonar_extra_opts = ""
    }
    pipeline {
        agent any

        stages {

            stage('Compile/Build') {
                steps {
                    mail bcc: '', body: 'test', cc: '', from: 'sivakumarit42@gmail.com', replyTo: '', subject: 'test', to: 'sivakumarit42@gmail.com'
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

    }
}