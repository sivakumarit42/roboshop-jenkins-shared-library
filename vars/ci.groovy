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
                git branch: 'main', url: "https://github.com/sivakumarit42/${component}"
                // aboveline is pipeline syntax
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

            if (BRANCH_NAME ==~ "PR-.*"){
                stage('Code Quality') {
                    common.codequality()
                }
            }

            if(env.GTAG == "true") {
                stage('Package') {
                    common.prepareArtifacts()
                }
                stage('Artifact Upload') {
                    common.artifactUpload()
                }
            }


        } catch (e) {
            mail body: "<h1>${component} - Pipeline Failed \n ${BUILD_URL}</h1>", from: 'sivakumarit42@gmail.com', subject: "${component} - Pipeline Failed", to: 'sivakumarit42@gmail.com',  mimeType: 'text/html'
        }

    }
}






//duplicated ci.groovy i.e same as c_scripted.groovy because i couldnt find the error. i did like this later will see what happend exactly