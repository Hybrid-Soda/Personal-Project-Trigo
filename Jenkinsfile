pipeline {
    agent any
    environment {
        TARGET_HOST = "ubuntu@54.180.104.67"
        IMAGE_NAME = "spring-server"
        APP_PATH = "./src/main/resources"
        NEW_VERSION = "latest"
        LAST_VERSION = "latest"
    }
    stages {
        stage('Inject Secret') {
            steps {
                // 파일 복사 후 경로에 붙여넣기
                withCredentials([file(credentialsId: 'application-secret', variable: 'SECRET_YML')]) {
                    sh 'cp $SECRET_YML ${APP_PATH}/application-secret.yml || echo "Copy failed, continuing..."'
                }
            }
        }

        stage('Build') {
            steps {
                sh 'chmod +x ./gradlew' // 권한 부여
                sh './gradlew clean build -x test' // gradlew 빌드
            }
        }

        stage('Delete Docker Image') {
            steps {
                sh '''
                    ssh -o StrictHostKeyChecking=no ${TARGET_HOST} "docker rmi ${IMAGE_NAME}:${LAST_VERSION}"
                '''
            }
        }

        stage('Build Docker Image') {
            steps {
                sh '''
                    ssh -o StrictHostKeyChecking=no ${TARGET_HOST} "docker build -t ${IMAGE_NAME}:${NEW_VERSION} ."
                '''
            }
        }
    }
}