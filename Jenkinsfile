pipeline {
  agent any

  environment {
    DOCKERHUB_CREDENTIALS = credentials('dockerhub-creds-id') 
    DOCKER_IMAGE = "aminaghannem/test-java-app"
    VERSION = "${env.BUILD_NUMBER}"
    BUILD_DATE = new Date().format('yyyyMMdd-HHmmss')
  }

  tools {
    maven 'M3' 
    jdk 'JDK8'
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
    }

    stage('Test') {
      steps {
        sh 'mvn test'
      }
    }

    stage('Build Docker Image') {
      steps {
        script {
          def tag = "latest"
            sh "docker build -t ${DOCKER_IMAGE}:${VERSION}-${BUILD_DATE} ."
        }
      }
    }


    stage('Push to Docker Hub') {
        steps {
            withCredentials([usernamePassword(credentialsId: 'dockerhub-creds-id', passwordVariable: 'DOCKER_PASSWORD', usernameVariable: 'DOCKER_USERNAME')]) {
                sh "echo ${DOCKER_PASSWORD} | docker login -u ${DOCKER_USERNAME} --password-stdin"
                sh "docker tag ${DOCKER_IMAGE}:${VERSION}-${BUILD_DATE} ${IMAGE_NAME}:latest"
                sh "docker push ${DOCKER_IMAGE}:${VERSION}-${BUILD_DATE}"
            }
        }
    }
  }

  post {
    success {
      echo 'Pipeline completed successfully.'
    }
    failure {
      echo 'Pipeline failed.'
    }
  }
}
