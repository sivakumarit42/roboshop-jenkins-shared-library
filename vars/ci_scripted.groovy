def call() {
    if (!env.sonar_extra_opts) {
        env.sonar_extra_opts=""
    }

    if(env.TAG_NAME ==~ ".*") {
        env.GTAG = "true"
    } else {
        env.GTAG = "false"
    }
    node('workstation') {

        try {

            stage('Check Out Code') {
                cleanWs()
                git branch: 'main', url: 'https://github.com/sivakumarit42/cart'
                //we have to check out(clone)the directory in scripted pipeline,but declarative pipeline it is automatically clone the directory
            }

            sh 'env'

            if (env.BRANCH_NAME != "main") {
                stage('Compile/Build') {
                    common.compile()
                }
            }

            println GTAG
            println BRANCH_NAME

            if(env.GTAG != "true" && env.BRANCH_NAME != "main") {
                stage('Test Cases') {
                    common.testcases()
                }
            }


            stage('Code Quality') {
                common.codequality()
            }
        } catch (e) {
            mail body: "<h1>${component} - Pipeline Failed \n ${BUILD_URL}</h1>", from: 'sivakumarit42@gmail.com', subject: "${component} - Pipeline Failed", to: 'sivakumarit42@gmail.com',  mimeType: 'text/html'
        }

    }
}

