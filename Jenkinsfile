pipeline{
    agent {
        label 'azure-collider-vm'
    }

    stages{
        stage('build'){
            steps{
                dir('code-colab-compiler-backend/code-collab/'){
                    sh 'mvn clean package'
                }
            }
        }
        stage('deploy'){
            steps{
                dir("code-colab-compiler-backend/code-collab/target"){
                    sh '''
                        cp code-collab.jar /home/collider-backend/
                        cd /home/collider-backend/
                       '''
                }
            }
        }
    }
}