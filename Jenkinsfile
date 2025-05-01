pipeline {
    agent any
    environment {
        TARGET_HOST = "ubuntu@13.209.48.78"
        IMAGE_NAME = "spring"
        UBUNTU_HOME = "/var/lib/jenkins"
        BUILD_FILE = "trigo-0.0.1-SNAPSHOT.jar"
    }
    // sh 작업 경로: ${JENKINS_HOME}/workspace/backend
    // ssh 작업 경로: /home/ubuntu
    // 빌드 파일 경로: ${JENKINS_HOME}/workspace/backend/build/libs/*.jar
    stages {
        stage('Inject Secret') {
            steps {
                sshagent(credentials: ['ssh-credential']) {
                    // 리소스 디렉토리 보장
                    sh "mkdir -p src/main/resources"
                    // 파일 복사 후 경로에 붙여넣기
                    withCredentials([
                        file(credentialsId: 'application-prod', variable: 'PROD_YML'),
                        file(credentialsId: 'application-secret', variable: 'SECRET_YML')
                    ]) {
                        sh "cp -f \$PROD_YML src/main/resources/application-prod.yml"
                        sh "cp -f \$SECRET_YML src/main/resources/application-secret.yml"
                    }
                }
            }
        }

        stage('Build') {
            steps {
                sshagent(credentials: ['ssh-credential']) {
                    sh "chmod +x ./gradlew"            // 권한 부여
                    sh "./gradlew clean build -x test" // gradlew 빌드
                }
            }
        }

        stage('Docker Compose Down') {
            steps {
                sshagent(credentials: ['ssh-credential']) {
                    script {
                        // 1) 실행 중인 컨테이너 ID 조회
                        def running = sh(
                            script: """
                                ssh -o StrictHostKeyChecking=no ${TARGET_HOST} \\
                                    "docker compose -f ./backend/docker-compose.yml ps -q"
                            """,
                            returnStdout: true
                        ).trim()

                        // 2) ID가 존재할 때만 down 실행
                        if (running) {
                            echo "Found running containers (${running.replaceAll('\\n', ', ')}), bringing down…"
                            sh """
                                ssh -o StrictHostKeyChecking=no ${TARGET_HOST} \\
                                    "docker compose -f ./backend/docker-compose.yml down"
                            """
                        } else {
                            echo "No running containers found, skipping 'docker compose down'."
                        }
                    }
                }
            }
        }

        stage('Remove Docker Image') {
            steps {
                sshagent(credentials: ['ssh-credential']) {
                    script {
                        // 이미지 존재 여부 확인
                        def imageId = sh(
                            script: """
                                ssh -o StrictHostKeyChecking=no ${TARGET_HOST} \\
                                    "docker images -q ${IMAGE_NAME}:latest"
                            """,
                            returnStdout: true
                        ).trim()

                        if (imageId) {
                            echo "Found image ${IMAGE_NAME}:latest (${imageId}), removing…"
                            sh """
                                ssh -o StrictHostKeyChecking=no ${TARGET_HOST} \\
                                    "docker rmi ${IMAGE_NAME}:latest"
                            """
                        } else {
                            echo "No image ${IMAGE_NAME}:latest found on ${TARGET_HOST}, skipping removal."
                        }
                    }
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                sshagent(credentials: ['ssh-credential']) {
                    // Dockerfile 과 JAR 파일을 같은 디렉토리로 복사
                    script {
                        parallel (
                            "Copy Dockerfile": {
                                sh "cp -f Dockerfile \$JENKINS_HOME/Dockerfile"
                            },
                            "Copy JAR File": {
                                sh "cp -f ./build/libs/${BUILD_FILE} \$JENKINS_HOME/${BUILD_FILE}"
                            }
                        )
                    }
                    // Dockerfile 불러오기 및 Image 빌드
                    sh """
                        ssh -o StrictHostKeyChecking=no ${TARGET_HOST} "cd /var/lib/jenkins && docker build -f Dockerfile -t ${IMAGE_NAME}:latest ."
                    """
                }
            }
        }

        stage('Deploy'){
            steps{
                sshagent (credentials: ['ssh-credential']){
                   sh """
                        ssh -o StrictHostKeyChecking=no ${TARGET_HOST} "docker compose -f ./backend/docker-compose.yml up -d"
                   """
                }
            }
        }
    }
}