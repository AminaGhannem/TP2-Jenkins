pipeline {
  agent any

  environment {
    DOCKERHUB_CREDENTIALS = credentials('dockerhub-creds-id') 
    DOCKER_IMAGE = "aminaghannem2003/test-java-app"
  }

  stages {
    stage('Checkout') {
      steps {
        git 'https://github.com/AminaGhannem/TP2-Jenkins.git'
      }
    }

    stage('Build with Maven') {
      steps {
        sh 'mvn clean install'
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
          sh "docker build -t ${DOCKER_IMAGE}:${tag} ."
        }
      }
    }

    stage('Push Docker Image') {
      steps {
        script {
          docker.withRegistry('', DOCKERHUB_CREDENTIALS) {
            sh "docker push ${DOCKER_IMAGE}:latest"
          }
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
