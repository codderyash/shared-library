def call(body) {
    def config = [:]
    body.resolveStrategy = Closure.DELEGATE_FIRST
    body.delegate = config
    body()
    node {


        stage('Checkout') {
             checkout scm

              checkout([$class: 'GitSCM', branches: [[name: '*/main']],
                                  userRemoteConfigs: [[url: 'https://github.com/codderyash/terraform-pipeline-test-2.git']],
                                  extensions: [[$class: 'RelativeTargetDirectory', relativeTargetDir: './terraform-pipeline-test-2']]])
         }
        
      
        
       stage("run script"){
        script{
            input "Do you want to deploy all resources to aws?"

            dir('./terraform-pipeline-test-2'){
  withCredentials([[$class: 'AmazonWebServicesCredentialsBinding',
                    credentialsId: '16552b1e-b971-4018-93f4-abee8968c654',
                    accessKeyVariable: 'AWS_ACCESS_KEY_ID', 
                    secretKeyVariable: 'AWS_SECRET_ACCESS_KEY'
                ]]){
                  
                    bat 'script2.sh all destroy'
                }
            }
           
        }
       }
    
        stage("Cleanup") {
            cleanWs()
        }       
    }
}
