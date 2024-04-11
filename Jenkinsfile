def label = "shopagent"
def mvn_version = 'M2'
podTemplate(label: label, yaml: '''
apiVersion: v1
kind: Pod
metadata:
  labels:
    app: build
  annotations:
    sidecar.istio.io/inject: "false"
spec:
  containers:
  - name: build
    image: kumararj/eos-jenkins-agent-base:latest
    command:
    - cat
    tty: true
    volumeMounts:
    - name: dockersock
      mountPath: /var/run/docker.sock
  volumes:
  - name: dockersock
    hostPath:
      path: /var/run/docker.sock
'''
) {
    node(label) {
        stage('Checkout SCM') {
          git credentialsId: 'git', url: 'https://github.com/microservices-demo/carts.git', branch: 'master'
          container('build') {
        stage('Build a Maven project') {
          //withEnv( ["PATH+MAVEN=${tool mvn_version}/bin"] ) {
          //sh "mvn clean package"
          //  }
          sh './mvnw -DskipTests clean package'
        //sh 'mvn clean package'
        }
          }
        }
        stage('Sonar Scan') {
          container('build') {
        stage('Sonar Scan') {
          withSonarQubeEnv('sonar') {
            sh './mvnw verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar -Dsonar.projectKey=sock-shop_service'
          }
        }
          }
        }

    environment {
        NEXUS_VERSION = 'nexus3'
        NEXUS_PROTOCOL = 'http'
        NEXUS_URL = 'http://nexus.k4m.in/'
        NEXUS_REPOSITORY = 'sock-shop-release-local'
        NEXUS_REPO_ID    = 'sock-shop-release-local'
        NEXUS_CREDENTIAL_ID = 'nexuslogin'
        ARTVERSION = "${env.BUILD_ID}"
    }
    stage('Publish to Nexus Repository Manager') {
      steps {
        script {
          pom = readMavenPom file: 'pom.xml'
          filesByGlob = findFiles(glob: "target/*.${pom.packaging}")
          echo "${filesByGlob[0].name} ${filesByGlob[0].path} ${filesByGlob[0].directory} ${filesByGlob[0].length} ${filesByGlob[0].lastModified}"
          artifactPath = filesByGlob[0].path
          artifactExists = fileExists artifactPath
          if (artifactExists) {
            echo "*** File: ${artifactPath}, group: ${pom.groupId}, packaging: ${pom.packaging}, version ${pom.version} ARTVERSION"
            nexusArtifactUploader(
                            nexusVersion: NEXUS_VERSION,
                            protocol: NEXUS_PROTOCOL,
                            nexusUrl: NEXUS_URL,
                            groupId: pom.groupId,
                            version: ARTVERSION,
                            repository: NEXUS_REPOSITORY,
                            credentialsId: NEXUS_CREDENTIAL_ID,
                            artifacts: [
                                [artifactId: pom.artifactId,
                                classifier: '',
                                file: artifactPath,
                                type: pom.packaging],
                                [artifactId: pom.artifactId,
                                classifier: '',
                                file: 'pom.xml',
                                type: 'pom']
                            ]
                        )
          }
            else {
            error "*** File: ${artifactPath}, could not be found"
            }
        }
      }
    }
    // stage('Docker Build') {
    //       container('build') {
    //     stage('Build Image') {
    //       docker.withRegistry( 'https://registry.hub.docker.com', 'docker' ) {
    //         def customImage = docker.build('kumararj/eos-micro-services-admin:latest')
    //         customImage.push()
    //       }
    //     }
    //       }
    // }

    //     stage('Helm Chart') {
    //       container('build') {
    //         dir('charts') {
    //           withCredentials([usernamePassword(credentialsId: 'jfrog', usernameVariable: 'username', passwordVariable: 'password')]) {
    //           sh '/usr/local/bin/helm package micro-services-admin'
    //           sh '/usr/local/bin/helm push-artifactory micro-services-admin-1.0.tgz https://k4meos.jfrog.io/artifactory/eos-helm-local --username $username --password $password'
    //           }
    //         }
    //       }
    //     }
    }
}
