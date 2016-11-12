#!groovy
node {
    currentBuild.result = "SUCCESS"

    try {
        stage('checkout') {
            checkout scm
        }

        stage('build') {
            sh './gradlew clean jar'
        }

        stage('deploy') {

        }
    }

    catch (err) {
        currentBuild.result = "FAILURE"
        throw err
    }
}