pipeline {
    agent {
        docker { image 'lachlanevenson/k8s-kubectl:v1.23.7' }
    }

    tools {
        maven 'M3'
        jdk 'JDK8'
    }

    environment {
        DOCKER_HUB = credentials('dockerhub-creds-id')
        IMAGE_NAME = 'aminaghannem/test-java-app'
        VERSION = "${env.BUILD_NUMBER}"
        BUILD_DATE = new Date().format('yyyyMMdd-HHmmss')
        FULL_IMAGE = "${IMAGE_NAME}:${VERSION}-${BUILD_DATE}"
        HELM_CHART_PATH = './mon-app'  
    }

    stages {
        stage('Checkout Code') {
            steps {
                checkout scm
                sh 'git --version'
                echo "Code checked out from ${env.GIT_URL}"
            }
        }

        stage('Build with Maven') {
            steps {
                script {
                    try {
                        sh 'mvn --version'
                        sh 'mvn clean package -DskipTests'
                        archiveArtifacts artifacts: 'target/*.jar', fingerprint: true
                    } catch (e) {
                        echo "Build failed: ${e}"
                        currentBuild.result = 'FAILURE'
                        error('Maven build failed')
                    }
                }
            }
            post {
                success {
                    echo 'Maven build completed successfully!'
                    stash includes: 'target/*.jar', name: 'app-jar'
                }
            }
        }

        stage('Test') {
            steps {
                sh 'mvn test'
            }
            post {
                always {
                    junit 'target/surefire-reports/*.xml'
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    try {
                        sh 'docker --version'
                        sh "docker build -t ${FULL_IMAGE} ."
                    } catch (e) {
                        echo "Docker build failed: ${e}"
                        currentBuild.result = 'FAILURE'
                        error('Docker build failed')
                    }
                }
            }
        }

        stage('Push to Docker Hub') {
            steps {
                withCredentials([usernamePassword(credentialsId: 'dockerhub-creds-id', passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
                    sh "echo ${DOCKER_PASSWORD} | docker login -u ${DOCKER_USERNAME} --password-stdin"
                    sh "docker tag ${FULL_IMAGE} ${IMAGE_NAME}:latest"
                    sh "docker push ${FULL_IMAGE}"
                    sh "docker push ${IMAGE_NAME}:latest"
                }
            }
        }

        stage('Check kubectl Version') {
            steps {
                sh 'kubectl version --client'
            }
        }

        stage('Deploy with Helm') {
            steps {
                script {
                    sh "helm version"
                    sh "helm upgrade --install mon-app ${HELM_CHART_PATH} --set image.repository=${IMAGE_NAME} --set image.tag=${VERSION}-${BUILD_DATE}"
                }
            }
        }

    }

    post {
        always {
            echo 'Pipeline completed - cleaning up'
        }
        success {
            echo 'Pipeline succeeded!'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
}
