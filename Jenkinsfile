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
        DOCKER_HOST = 'unix:///Users/abhinav/.docker/run/docker.sock'
    }
    
    stages {
        stage('Verify Docker') {
            steps {
                sh '''
                    echo "Docker socket permissions:"
                    ls -l /Users/abhinav/.docker/run/docker.sock
                    echo "Docker version:"
                    docker version
                    echo "Docker info:"
                    docker info
                '''
            }
        }

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
                    sh "docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} ."
                }
            }
        }
        
        stage('Stop and Remove Old Container') {
            steps {
                script {
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
                    sh "docker run -d -p 8083:8083 --name sbexample-app ${DOCKER_IMAGE}:${DOCKER_TAG}"
                }
            }
        }
    }
    
    post {
        always {
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