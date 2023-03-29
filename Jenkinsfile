pipeline {

  agent any
  stages{

     stage('Git Checkout'){
         steps{
               git branch: 'main', url: 'https://github.com/anuj9689/django-notes-app.git'
         }
     }
     stage('UNIT Testing'){
        
         steps{
              sh 'mvn test'
         }
     }

  }
}

