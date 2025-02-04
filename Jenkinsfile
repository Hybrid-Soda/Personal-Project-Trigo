pipeline {
    agent any
    environment {
        TARGET_HOST = "ubuntu@trigo365.shop"
        IMAGE_NAME = "spring"
        UBUNTU_HOME = "/home/ubuntu/app"
        JENKINS_HOME = "/var/jenkins_home"
        NEW_VERSION = "latest"
        LAST_VERSION = "latest"
        BUILD_FILE = "trigo-0.0.1-SNAPSHOT.jar"
    }
    // sh 작업 경로: ${JENKINS_HOME}/workspace/backend
    // ssh 작업 경로: /home/ubuntu
    // 빌드 파일 경로: ${JENKINS_HOME}/workspace/backend/build/libs/*.jar
    stages {
        stage('Inject Secret') {
            steps {
                sshagent(credentials: ['ssh-credential']) {
                    // 파일 복사 후 경로에 붙여넣기
                    withCredentials([file(credentialsId: 'application-prod', variable: 'PROD_YML'), file(credentialsId: 'application-secret', variable: 'SECRET_YML')]) {
                        sh 'cp $PROD_YML ${JENKINS_HOME}/workspace/backend/src/main/resources/application-prod.yml || echo "Already"'
                        sh 'cp $SECRET_YML ${JENKINS_HOME}/workspace/backend/src/main/resources/application-secret.yml || echo "Already"'
                    }
                }
            }
        }

        stage('Build') {
            steps {
                sshagent(credentials: ['ssh-credential']) {
                    sh 'chmod +x ./gradlew'            // 권한 부여
                    sh './gradlew clean build -x test' // gradlew 빌드
                }
            }
        }

        stage('Delete Docker Image') {
            steps {
                sshagent(credentials: ['ssh-credential']) {
                    sh '''
                        ssh -o StrictHostKeyChecking=no ${TARGET_HOST} "docker rmi ${IMAGE_NAME} || echo "Delete failed, continuing...""
                    '''
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                sshagent(credentials: ['ssh-credential']) {
                    // Dockerfile 과 JAR 파일을 같은 디렉토리로 복사
                    sh 'cp Dockerfile ${JENKINS_HOME}/Dockerfile'
                    sh 'cp ./build/libs/${BUILD_FILE} ${JENKINS_HOME}/${BUILD_FILE}'
                    // Dockerfile 불러오기 및 Image 빌드
                    sh '''
                        ssh -o StrictHostKeyChecking=no ${TARGET_HOST} "cd app/ && docker build -f Dockerfile -t ${IMAGE_NAME}:${NEW_VERSION} ."
                    '''
                }
            }
        }

        stage('Deploy'){
            steps{
                sshagent (credentials: ['ssh-credential']){
                   sh '''
                        ssh -o StrictHostKeyChecking=no ${TARGET_HOST} "docker compose -f ./spring/docker-compose.yml down"
                        ssh -o StrictHostKeyChecking=no ${TARGET_HOST} "docker compose -f ./spring/docker-compose.yml up -d"
                   '''
                }
            }
        }
    }
}