pipeline {
    agent any

    environment {
        FRONT_SERVER = 'nova-dev@125.6.36.57'
        DEPLOY_PATH = '/home/nova-dev'
        REPO_URL = 'https://github.com/nhnacademy-be6-supernova/novabook-back-store.git'
        ARTIFACT_NAME = 'store-0.0.1-SNAPSHOT.jar'
        JAVA_OPTS = '-XX:+EnableDynamicAgentLoading -XX:+UseParallelGC'
    }

    tools {
        jdk 'jdk-21'
        maven 'maven-3.9.7'
    }

    stages {
        stage('Checkout') {
            steps {
                git(
                    url: REPO_URL,
                    branch: 'feature/Jenkins',
                    credentialsId: 'nova-dev'
                )
            }
        }
        stage('Build') {
            steps {
                withEnv(["JAVA_OPTS=${env.JAVA_OPTS}"]) {
                    sh 'mvn clean package -DskipTests=true'
                }
            }
        }
        stage('Add SSH Key to Known Hosts') {
            steps {
                script {
                    def remoteHost = '125.6.36.57'
                    sh """
                        mkdir -p ~/.ssh
                        ssh-keyscan -H ${remoteHost} >> ~/.ssh/known_hosts
                    """
                }
            }
        }
        stage('Deploy to Front Server') {
            steps {
                deployToServer(FRONT_SERVER, DEPLOY_PATH, 8090)
                showLogs(FRONT_SERVER, DEPLOY_PATH)
            }
        }
        stage('Verification') {
            steps {
                verifyDeployment(FRONT_SERVER, 8090)
            }
        }
    }
    post {
        success {
            echo 'Deployment succeeded!'
        }
        failure {
            echo 'Deployment failed!'
        }
    }
}

def deployToServer(server, deployPath, port) {
    withCredentials([sshUserPrivateKey(credentialsId: 'nova-dev', keyFileVariable: 'PEM_FILE')]) {
        sh """
        scp -o StrictHostKeyChecking=no -i \$PEM_FILE target/${ARTIFACT_NAME} ${server}:${deployPath}
        ssh -o StrictHostKeyChecking=no -i \$PEM_FILE ${server} 'nohup java -jar ${deployPath}/${ARTIFACT_NAME} --server.port=${port} ${env.JAVA_OPTS} > ${deployPath}/app.log 2>&1 &'  // 애플리케이션을 백그라운드에서 실행
        """
    }
}

def showLogs(server, deployPath) {
    withCredentials([sshUserPrivateKey(credentialsId: 'nova-dev', keyFileVariable: 'PEM_FILE')]) {
        sh """
        ssh -o StrictHostKeyChecking=no -i \$PEM_FILE ${server} 'tail -n 100 ${deployPath}/app.log'
        """
    }
}

def verifyDeployment(server, port) {
    sh """
    curl -s --head http://${server}:${port} | head -n 1
    """
}
