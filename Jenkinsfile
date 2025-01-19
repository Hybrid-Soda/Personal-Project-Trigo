pipeline {
    agent any
    environment {
        IMAGE_NAME = "spring-server"
        APP_PATH = "./src/main/resources"
    }
    stages {
        stage('Replace Build') {
            steps {
                // 파일 복사 후 경로에 붙여넣기
                withCredentials([file(credentialsId: 'application-secret', variable: 'SECRET_YML')]) {
                    sh 'cp $SECRET_YML ${APP_PATH}/application-secret.yml'
                }
            }
        }

        stage('Build') {
            steps {
                sh 'chmod +x gradlew' // 권한 부여
                sh "./gradlew clean build" // 테스트 없이 빌드
            }
        }
    }
}