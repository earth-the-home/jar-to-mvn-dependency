# Problem with maven install plugin
currently, we can install only one external jar into maven at once. so it requires manual installation of each jars within folder.

# Advantage of jar-to-mvn-dependency
we can install all external jars within specified folder into maven at once.

# Steps to use
1. download jar from "/target/jar-to-mvn-dependency-0.0.1-SNAPSHOT.jar" 
   Or you can clone project and build executable jar yourself
2. java -jar jar-to-mvn-dependency-0.0.1-SNAPSHOT.jar pathToExternalJarsFolder
