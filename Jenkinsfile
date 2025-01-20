pipeline {
    agent any
    environment {
        TARGET_HOST = "ubuntu@54.180.104.67"
        IMAGE_NAME = "spring"
        UBUNTU_HOME = "/home/ubuntu/app"
        JENKINS_HOME = "/var/jenkins_home"
        NEW_VERSION = "latest"
        LAST_VERSION = "latest"
        BUILD_FILE = "trigo-0.0.1-SNAPSHOT.jar"
    }
    stages {
        // 작업 경로 : ${JENKINS_HOME}/workspace/backend
        stage('Inject Secret') {
            steps {
                sshagent(credentials: ['ssh-credential']) {
                    // 파일 복사 후 경로에 붙여넣기
                    withCredentials([file(credentialsId: 'application-secret', variable: 'SECRET_YML')]) {
                        sh 'cp $SECRET_YML /src/main/resources/application-secret.yml || echo "Copy failed, continuing..."'
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

        // 빌드 파일 경로 : ${JENKINS_HOME}/workspace/backend/build/libs/*.jar = ${UBUNTU_HOME}/workspace/backend/build/libs/*.jar
        stage('Delete Docker Image') {
            steps {
                sshagent(credentials: ['ssh-credential']) {
                    sh '''
                        ssh -o StrictHostKeyChecking=no ${TARGET_HOST} "docker rmi ${IMAGE_NAME}:${LAST_VERSION} || echo "Delete failed, continuing...""
                    '''
                }
            }
        }

        // 작업 경로 : /home/ubuntu
        stage('Build Docker Image') {
            steps {
                sshagent(credentials: ['ssh-credential']) {
                    sh 'cp Dockerfile ${JENKINS_HOME}/Dockerfile'
                    sh 'cp ./build/libs/${BUILD_FILE} ${JENKINS_HOME}/${BUILD_FILE}'
                    sh '''
                        ssh -o StrictHostKeyChecking=no ${TARGET_HOST} "docker build -f ${UBUNTU_HOME}/Dockerfile -t ${IMAGE_NAME}:${NEW_VERSION} ."
                    '''
                }
            }
        }
    }
}