def call(body) {
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()
    node {


        stage('Checkout') {
             checkout scm

                bat 'git submodule update --init --recursive'
         }
        
      
        
       stage("run script"){
        script{
            input "Do you want to deploy all resources to aws?"

            dir('./terraform-pipeline-test-2'){
  withCredentials([[$class: 'AmazonWebServicesCredentialsBinding',
                    credentialsId: 'AWS Credentials',
                    accessKeyVariable: 'AWS_ACCESS_KEY_ID', 
                    secretKeyVariable: 'AWS_SECRET_ACCESS_KEY'
                ]]){
                  
                    bat 'script2.sh all destroy'
                }
            }
           
        }
       }
    
             
    }
}
