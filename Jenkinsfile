pipeline {
    agent {
        docker {
            image 'cimg/openjdk:17.0'
            args '-u root -v /var/run/docker.sock:/var/run/docker.sock'
        }
    }
    
    environment {
        DOCKER_IMAGE = 'sbexample'
        DOCKER_TAG = "${BUILD_NUMBER}"
    }
    
    stages {
        stage('Setup Docker') {
            steps {
                sh '''
                    # Install Docker
                    curl -fsSL https://get.docker.com -o get-docker.sh
                    sh get-docker.sh
                    
                    # Verify Docker installation
                    docker --version
                    docker ps
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