node {
    environment {
        WEBHOOK_URL = credentials("DISCORD_WEBHOOK")
    }
    try {
        stage('Start') {
            // slackSend (channel: '#jenkins', color: '#FFFF00', message: "STARTED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")
        }

        stage('git sslVerify off') {
            sh(script: "git config --global http.sslVerify false || true")
        }

        stage('git source Pull') {
            checkout scm
        }

        stage('gradle credentials') {
            sh(script: "../gradleCredentials/credentials.sh")
        }

        stage("Docker Image Delete") {
            sh(script: "docker rmi ${IMAGE_NAME}:latest  || true")
            sh(script: 'docker rmi $(docker images -f "dangling=true" -q) || true')
        }

        stage("SpringBoot BootJar") {
            sh(script: "chmod 775 .")
            sh(script: "./gradlew clean bootJar")
        }

        stage("Docker Image tag") {
            try {
              sh "docker build --no-cache -t ${DOCKER_HUB_USER}/${IMAGE_NAME}:latest ."

            }catch (e) {
              print(e)
            }
        }

        stage("Docker run"){
            sh(script: "docker stop ${IMAGE_NAME} || true")
            sh(script: "docker rm ${IMAGE_NAME} || true")
            sh(script: "docker rmi ${IMAGE_NAME} || true")
            sh(script:"docker run --network ${DOCKER_NETWORK} -m 3g --env JAVA_OPTS='-Dspring.profiles.active=${SPRING_PROFILE} -Dfile.encoding=UTF-8 -Djasypt.encryptor.password=${DJASYPT_PASSWORD} -Xmx8192m -XX:MaxMetaspaceSize=1024m' --user root -d -e TZ=Asia/Seoul --name ${IMAGE_NAME} ${DOCKER_HUB_USER}/${IMAGE_NAME}:latest")
        }

    } catch(e) {
        print(e)
    } finally{
        try{
            success {
                discordSend description: "Jenkins 빌드가 성공했습니다.",
                            footer: "Jenkins 빌드가 성공했습니다.",
                            link: env.BUILD_URL,
                            result: currentBuild.currentResult,
                            title: "Jenkins Build",
                            webhookURL: env.WEBHOOK_URL
            }
        }catch(e){
            print(e)
        }
    }

}