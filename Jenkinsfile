pipeline {
    agent any
    tools {
        maven 'MAVEN3.9'
        jdk 'JDK17'
    }
    stages {
        stage('Fetch Code') {
            steps {
                git branch: 'master', url: 'https://github.com/enumahin/metadata-microservice.git'
            }
        }
        stage('Test') {
            steps {
                sh 'mvn test'
            }
        }
        stage('Checkstyle') {
            steps {
                sh 'mvn checkstyle:checkstyle'
            }
        }
        stage("SonarQube Analysis") {
            environment {
                scannerHome = tool 'sonar6.2'
            }
            steps {
                withSonarQubeEnv('SonarQubeServer') {
                    sh '''
                        ${scannerHome}/bin/sonar-scanner \\
                        -Dsonar.projectKey=metadata-microservice \\
                        -Dsonar.projectName=metadata-microservice \\
                        -Dsonar.projectVersion=2.0 \\
                        -Dsonar.sources=src/ \\
                        -Dsonar.java.binaries=target/test-classes/ \\
                        -Dsonar.junit.reportsPath=target/surefire-reports/ \\
                        -Dsonar.jacoco.reportsPath=target/jacoco.exec \\
                        -Dsonar.checkstyle.reportsPath=target/checkstyle-result.xml
                    '''
                }
            }
        }
        stage('Quality Gate') {
            steps {
                timeout(time: 1, unit: 'HOURS') {
                    waitForQualityGate abortPipeline: true
                }
            }
        }
        stage('Build') {
            steps {
                sh 'mvn install -DskipTests'
            }
            //post {
                //success {
                   // echo 'Archiving...'
                   //echo "Renaming artifacts..."
                   //sh 'mkdir -p versions'
                  // sh '''
                        //for file in target/*.jar; do
                            //filename=$(basename "$file")
                            // base=$(basename "$file" .jar)
                            //base="${filename%%-*}"
                            // cp "$file" "versions/${base}.${BUILD_ID}.jar"
                            //mv "$file" "${base}.jar"
                        //done
                   // '''
                    //// archiveArtifacts artifacts: '**/target/*.jar', followSymlinks: false
                //}
            //}
        }
        stage('Publish to Nexus') {
            steps {
                script {
                    def jarFiles = findFiles(glob: 'target/*.jar')
                    def artifactsList = []
                    
                    jarFiles.each { jarFile ->
                        def fileName = jarFile.name
                        // Extract the artifact ID from the jar filename (removing version and extension)
                        def artifactId = fileName.replaceAll(/-\d+.*\.jar$/, '')
                        
                        artifactsList.add([
                            artifactId: artifactId,
                            classifier: '',
                            file: "target/${fileName}",
                            type: 'jar'
                        ])
                    }
                    
                    echo 'Deploying to Nexus Repository....'
                    nexusArtifactUploader(
                        nexusVersion: 'nexus3',
                        protocol: 'http',
                        nexusUrl: 'https://nexus.alienworkspace.dev',
                        groupId: 'com.alienworkspace.cdr',
                        version: "${BUILD_ID}-${BUILD_TIMESTAMP}",
                        repository: 'cdr-metadata-repo',
                        credentialsId: 'nexuslogin',
                        artifacts: artifactsList
                    )
                }
            }
        }
    }
}