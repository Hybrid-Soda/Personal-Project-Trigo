pipeline {
    agent any
    environment {
        TARGET_HOST = "ubuntu@54.180.104.67"
        IMAGE_NAME = "spring-server"
        INSTANCE_PATH = "/home/ubuntu/app"
        RESOURCE_PATH = "/src/main/resources"
        NEW_VERSION = "latest"
        LAST_VERSION = "latest"
    }
    stages {
        // 작업 경로 : /var/jenkins_home/workspace/backend
        stage('Inject Secret') {
            steps {
                sshagent(credentials: ['ssh-credential']) {
                    // 파일 복사 후 경로에 붙여넣기
                    withCredentials([file(credentialsId: 'application-secret', variable: 'SECRET_YML')]) {
                        sh 'cp $SECRET_YML ${RESOURCE_PATH}/application-secret.yml || echo "Copy failed, continuing..."'
                    }
                }
            }
        }

        stage('Build') {
            steps {
                sshagent(credentials: ['ssh-credential']) {
                    sh 'chmod +x ./gradlew' // 권한 부여
                    sh './gradlew clean build -x test' // gradlew 빌드
                }
            }
        }

        // 빌드 파일 경로 : /var/jenkins_home/workspace/backend/build/libs/*.jar
        stage('Delete Docker Image') {
            steps {
                // 현재 경로 : /home/ubuntu
                sshagent(credentials: ['ssh-credential']) {
                    sh '''
                        ssh -o StrictHostKeyChecking=no ${TARGET_HOST} "docker rmi ${IMAGE_NAME}:${LAST_VERSION} || echo "Delete failed, continuing...""
                    '''
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                // 현재 경로 : /home/ubuntu
                sshagent(credentials: ['ssh-credential']) {
                    sh '''
                        ssh -o StrictHostKeyChecking=no ${TARGET_HOST} "docker build -f ./Dockerfile -t ${IMAGE_NAME}:${NEW_VERSION} ."
                    '''
                }
            }
        }
    }
}