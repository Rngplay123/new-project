pipeline {

  agent any
  stages{

     stage('Git Checkout'){
         steps{
               git branch: 'main', url: https://github.com/Rngplay123/new-project.git
         }
     }
     stage('UNIT Testing'){
        
         steps{
              sh 'gradle test'
         }
     }

  }
}

