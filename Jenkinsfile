pipeline {
    agent any
    environment {
        TARGET_HOST = "ubuntu@54.180.104.67"
        IMAGE_NAME = "spring-server"
        APP_PATH = "/var/jenkins_home/backend"
        NEW_VERSION = "latest"
        LAST_VERSION = "latest"
    }
    stages {
        stage('Inject Secret') {
            steps {
                sshagent(credentials: ['ssh-credential']) {
                    // 파일 복사 후 경로에 붙여넣기
                    withCredentials([file(credentialsId: 'application-secret', variable: 'SECRET_YML')]) {
                        sh 'cp $SECRET_YML ${APP_PATH}/src/main/resources/application-secret.yml || echo "Copy failed, continuing..."'
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

        stage('Delete Docker Image') {
            steps {
                sshagent(credentials: ['ssh-credential']) {
                    sh '''
                        ssh -o StrictHostKeyChecking=no ${TARGET_HOST} "docker rmi ${IMAGE_NAME}:${LAST_VERSION} || echo "Delete failed, continuing...""
                    '''
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                sshagent(credentials: ['ssh-credential']) {
                    sh '''
                        ssh -o StrictHostKeyChecking=no ${TARGET_HOST} "docker build -f ${APP_PATH}/Dockerfile -t ${IMAGE_NAME}:${NEW_VERSION} ."
                    '''
                }
            }
        }
    }
}