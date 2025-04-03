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
        stage('Verify Environment') {
            steps {
                sh '''
                    echo "Checking Docker installation..."
                    which docker || (echo "Docker not found" && exit 1)
                    docker --version
                    docker ps
                    
                    echo "Checking Java and Maven..."
                    mvn --version
                    java --version
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
        
        stage('Docker Build and Deploy') {
            steps {
                sh '''
                    # Build the image
                    docker build -t ${DOCKER_IMAGE}:${DOCKER_TAG} .
                    
                    # Stop and remove existing container if running
                    CONTAINER_ID=$(docker ps -q -f name=sbexample-app)
                    if [ ! -z "$CONTAINER_ID" ]; then
                        echo "Stopping existing container..."
                        docker stop $CONTAINER_ID
                        docker rm $CONTAINER_ID
                    fi
                    
                    # Run new container
                    echo "Starting new container..."
                    docker run -d -p 8083:8083 --name sbexample-app ${DOCKER_IMAGE}:${DOCKER_TAG}
                    
                    # Verify container is running
                    echo "Verifying container status..."
                    docker ps | grep sbexample-app
                '''
            }
        }
    }
    
    post {
        success {
            echo 'Pipeline completed successfully!'
            sh '''
                echo "Container logs:"
                docker logs sbexample-app
            '''
        }
        failure {
            echo 'Pipeline failed!'
        }
        always {
            cleanWs()
        }
    }
} 