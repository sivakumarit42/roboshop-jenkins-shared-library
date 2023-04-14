def compile() {
    if(app_lang == "nodejs") {
        sh 'npm install'
    }
    if(app_lang == "maven") {
       // sh 'mvn package ; mv target/${component}-1.0.jar ${component}.jar'
        sh 'mvn package'
    }
}

def testcases() {
    // npm test
    // mvn test
    // python -m unittests
    // go test
    sh 'echo OK'
}


def codequality() {
    withAWSParameterStore(credentialsId: 'PARAM1', naming: 'absolute', path: '/sonarqube', recursive: true, regionName: 'us-east-1') {
        //sh 'sonar-scanner -Dsonar.host.url=http://172.31.6.242:9000 -Dsonar.login=${SONARQUBE_USER} -Dsonar.password=${SONARQUBE_PASS} -Dsonar.projectKey=${component} ${sonar_extra_opts} -Dsonar.qualitygate.wait=true'
        sh 'echo OK'
    }

}


//def prepareArtifacts() {
//    sh 'echo ${TAG_NAME} >VERSION'
//    if (app_lang == "maven") {
//        sh 'zip -r ${component}-${TAG_NAME}.zip ${component}.jar schema VERSION'
//    } else {
//        sh 'zip -r ${component}-${TAG_NAME}.zip * -x Jenkinsfile'
//    }
//
//}
//
//def artifactUpload() {
//    env.NEXUS_USER = sh ( script: 'aws ssm get-parameter --name prod.nexus.user --with-decryption | jq .Parameter.Value | xargs', returnStdout: true ).trim()
//    env.NEXUS_PASS = sh ( script: 'aws ssm get-parameter --name prod.nexus.pass --with-decryption | jq .Parameter.Value | xargs', returnStdout: true ).trim()
//    wrap([$class: 'MaskPasswordsBuildWrapper',
//          varPasswordPairs: [[password: NEXUS_PASS],[password: NEXUS_USER]]]) {
//        sh 'echo ${TAG_NAME} >VERSION'
//        sh 'curl -v -u ${NEXUS_USER}:${NEXUS_PASS} --upload-file ${component}-${TAG_NAME}.zip http://172.31.12.4:8081/repository/${component}/${component}-${TAG_NAME}.zip'
//
//    }
// }
def prepareArtifacts() {
    sh 'echo ${TAG_NAME} >VERSION'
    if (app_lang == "nodejs" || app_lang == "angular") {
        sh 'zip -r ${component}-${TAG_NAME}.zip * -x Jenkinsfile'
    }

}

def artifactUpload() {
    sh 'echo ${TAG_NAME} >VERSION'
    if (app_lang == "nodejs" || app_lang == "angular") {
        sh 'curl -v -u admin:admin123 --upload-file ${component}-${TAG_NAME}.zip http://172.31.12.4:8081/repository/${component}/${component}-${TAG_NAME}.zip'
    }

}
