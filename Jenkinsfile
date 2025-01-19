pipeline {
    agent any
    environment {
        IMAGE_NAME = "spring-server"
    }
    stages {
        stage('Build') {
            steps {
                sh 'chmod +x gradlew' // 권한 부여
                sh "./gradlew clean build -x test" // 테스트 없이 빌드
            }
        }
    }
}