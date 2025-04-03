pipeline {
    agent any
    
    tools {
        maven 'maven'
        jdk 'jdk'
    }
    
    triggers {
        pollSCM('* * * * *')  // Poll every minute
    }
    
    environment {
        DOCKER_IMAGE = 'sbexample'
        DOCKER_TAG = "${BUILD_NUMBER}"
    }
    
    stages {
        stage('Build Code') {
            steps {
                echo 'Building application...'
                sh 'mvn clean package -DskipTests'
            }
        }
        
        stage('Run Tests') {
            steps {
                echo 'Running tests...'
                sh 'mvn test'
            }
            post {
                always {
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }
        
        stage('Build Docker Image') {
            steps {
                echo 'Building Docker image...'
                sh "docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} ."
            }
        }
        
        stage('Deploy Container') {
            steps {
                echo 'Stopping existing container if running...'
                sh '''
                    if docker ps -q -f name=sbexample-app; then
                        docker stop sbexample-app
                        docker rm sbexample-app
                    fi
                '''
                
                echo 'Starting new container...'
                sh "docker run -d -p 8083:8083 --name sbexample-app ${DOCKER_IMAGE}:${DOCKER_TAG}"
                
                echo 'Verifying deployment...'
                sh 'docker ps | grep sbexample-app'
            }
        }
    }
    
    post {
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
} 