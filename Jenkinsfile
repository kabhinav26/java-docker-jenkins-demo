pipeline {
    agent any
    
    tools {
        maven 'maven'
        jdk 'jdk'
    }
    
    triggers {
        pollSCM('* * * * *')  // Poll SCM every minute (can be adjusted)
    }
    
    environment {
        DOCKER_IMAGE = 'sbexample'
        DOCKER_TAG = "${BUILD_NUMBER}"
    }
    
    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }
        
        stage('Build') {
            steps {
                sh 'mvn clean package -DskipTests'
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
                    // Build new Docker image
                    sh "docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} ."
                }
            }
        }
        
        stage('Stop and Remove Old Container') {
            steps {
                script {
                    // Stop and remove existing container if it exists
                    sh '''
                        if [ "$(docker ps -q -f name=sbexample-app)" ]; then
                            docker stop sbexample-app
                            docker rm sbexample-app
                        fi
                    '''
                }
            }
        }
        
        stage('Run New Container') {
            steps {
                script {
                    // Run new container with the latest image
                    sh "docker run -d -p 8083:8083 --name sbexample-app ${DOCKER_IMAGE}:${DOCKER_TAG}"
                }
            }
        }
    }
    
    post {
        always {
            // Clean up workspace
            cleanWs()
        }
        success {
            echo 'Pipeline completed successfully!'
        }
        failure {
            echo 'Pipeline failed!'
        }
    }
} 