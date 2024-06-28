pipeline {
    agent any

    environment {
        FRONT_SERVER = 'nova-dev@125.6.36.57'
        DEPLOY_PATH = '/home/nova-dev'
        REPO_URL = 'https://github.com/nhnacademy-be6-supernova/novabook-back-store'
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
		    sh 'git reset --hard HEAD'
		    sh 'git clean -fdx'
                git(
                    url: REPO_URL,
                    branch: 'main',
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
	stage('Test') {
            steps {
                withEnv(["JAVA_OPTS=${env.JAVA_OPTS}"]) {
                    sh 'mvn test -Dsurefire.forkCount=1 -Dsurefire.useSystemClassLoader=false'
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
	'rm store-0.0.1-SNAPSHOT.jar'
        scp -o StrictHostKeyChecking=no -i \$PEM_FILE target/${ARTIFACT_NAME} ${server}:${deployPath}
	ssh -o StrictHostKeyChecking=no -i \$PEM_FILE ${server} 'fuser -k ${port}/tcp || true'
        ssh -o StrictHostKeyChecking=no -i \$PEM_FILE ${server} 'nohup /home/jdk-21.0.3+9/bin/java -jar ${deployPath}/${ARTIFACT_NAME} ${env.JAVA_OPTS} > ${deployPath}/store_app.log 2>&1 &'
        """
    }
}

def showLogs(server, deployPath) {
    withCredentials([sshUserPrivateKey(credentialsId: 'nova-dev', keyFileVariable: 'PEM_FILE')]) {
        sh """
        """
    }
}

def verifyDeployment(server, port) {
    sh """
    curl -s --head http://${server}:${port} | head -n 1
    """
}
