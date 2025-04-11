 pipeline {
 agent any
 environment {
 IMAGE_NAME  "test-express-app"
 DOCKER_REGISTRY  "ykharrat848"
 }
 stages {
 stage('Check Docker') {
 steps {
 sh 'docker version'
 }
 }
 stage('Construire lʼimage Docker') {
 steps {
 sh 'docker build -t $IMAGE_NAME:latest .'
 }
 }
 stage('Authentification Docker & Push') {
 steps {
 script {
 withCredentials([usernamePassword(credentialsId: 'docker-hub-c
 redentials', usernameVariable: 'DOCKER_USER', passwordVariable: 'DOCKER_
 PASS')]) {
 sh 'echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin'
 sh 'docker tag $IMAGE_NAME:latest $DOCKER_REGISTRY/$IM
 AGE_NAME:latest'
 sh 'docker push $DOCKER_REGISTRY/$IMAGE_NAME:latest'
 sh 'docker logout'
 }
 }