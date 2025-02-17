pipeline {
    agent any
    environment {
        TARGET_HOST = "ubuntu@43.200.176.180"
        IMAGE_NAME = "spring"
        UBUNTU_HOME = "/var/lib/jenkins"
        JENKINS_HOME = "/var/jenkins_home"
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
                    withCredentials([
                        file(credentialsId: 'application-prod', variable: 'PROD_YML'),
                        file(credentialsId: 'application-secret', variable: 'SECRET_YML')
                    ]) {
                        sh "cp -f \$PROD_YML ${JENKINS_HOME}/workspace/backend/src/main/resources/application-prod.yml"
                        sh "cp -f \$SECRET_YML ${JENKINS_HOME}/workspace/backend/src/main/resources/application-secret.yml"
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
                        parallel (
                            "Stop Containers": {
                                sh "ssh -o StrictHostKeyChecking=no ${TARGET_HOST} 'docker compose -f ./backend/docker-compose.yml down || true'"
                            },
                            "Remove Docker Image": {
                                sh "ssh -o StrictHostKeyChecking=no ${TARGET_HOST} 'docker rmi -f ${IMAGE_NAME}:latest'"
                            }
                        )
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
                                sh "cp -f Dockerfile ${JENKINS_HOME}/Dockerfile"
                            },
                            "Copy JAR File": {
                                sh "cp -f ./build/libs/${BUILD_FILE} ${JENKINS_HOME}/${BUILD_FILE}"
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