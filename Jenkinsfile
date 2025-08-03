pipeline{
    agent {
        label 'azure-collider-vm'
    }

    stages{
        stage('build'){
            steps{
                sh 'mvn clean package'
            }
        }
        stage('deploy'){
            steps{
                dir("target"){
                    sh '''
                        cp code-collab.jar /home/collider-backend/
                        cd /home/collider-backend/
                       '''
                }
            }
        }
    }
}