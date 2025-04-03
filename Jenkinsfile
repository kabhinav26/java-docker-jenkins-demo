pipeline {
    agent any
    
    tools {
        maven 'maven'
        jdk 'jdk'
    }
    
    environment {
        DOCKER_IMAGE = 'sbexample'
        DOCKER_TAG = "${BUILD_NUMBER}"
    }
    
    stages {
        stage('Install Dependencies') {
            steps {
                script {
                    // Install Homebrew if not present
                    sh '''
                        if ! command -v brew &> /dev/null; then
                            echo "Installing Homebrew..."
                            /bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"
                        fi
                        
                        # Add Homebrew to PATH
                        eval "$(/opt/homebrew/bin/brew shellenv)"
                        
                        # Install Docker if not present
                        if ! command -v docker &> /dev/null; then
                            echo "Installing Docker..."
                            brew install --cask docker
                        fi
                        
                        # Start Docker if not running
                        if ! docker info &> /dev/null; then
                            echo "Starting Docker..."
                            open -a Docker
                            # Wait for Docker to start
                            for i in {1..30}; do
                                if docker info &> /dev/null; then
                                    break
                                fi
                                sleep 1
                            done
                        fi
                        
                        # Verify installations
                        docker --version
                        mvn --version
                        java --version
                    '''
                }
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
                script {
                    sh '''
                        # Ensure Docker is running
                        eval "$(/opt/homebrew/bin/brew shellenv)"
                        
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
    }
    
    post {
        success {
            echo 'Pipeline completed successfully!'
            sh '''
                eval "$(/opt/homebrew/bin/brew shellenv)"
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