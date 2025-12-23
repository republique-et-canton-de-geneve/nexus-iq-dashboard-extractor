# nexus-iq-dashboard-extractor

A Java application to retrieve the applications' vulnerabilities detected by
Nexus IQ (aka CLM, aka Sonatype Lifecycle). 

At the State of Geneva, the complete use case is the following:
1. The continuous integration environment (at the State of Geneva, is consists in
   the GitLab CI pipelines of the various applications) performs the Nexus IQ
   analysis of the applications and sends the vulnerabilities reports to the
   Nexus IQ server
1. A scheduled process starts this application
1. This application invokes the Nexus IQ REST API to retrieve the vulnerabilities
   of all applications and stores the retrieved JSON output into a result file
1. A Splunk agent, configured to retrieve the data from the result file, loads and
   indexes the result file
1. On Splunk, a previously configured dashboard displays the vulnerabilities to
   the end user.

# Building the application

With Maven 3 and Java 21+:
```
mvn clean package
```

After the first execution of the above command, the API classes have been generated,
so the following command can be used to speed up the compilation process:
```
mvn package -Dcodegen.skip
```

# Running the application locally

## Pre-step : configuring the application: property file

Do the following:
- Go to directory `src/main/resources`
- Copy file `application-base.yml` (this file is under Git control)
  to a new file `application.yml` (this file is under Git ignore),
  in the same directory
- Edit file `application.yml`, provide the missing values;
  their value is `TO_BE_PROVIDED`.
  For the particular case of property `app.trustStorePassword`, don't do anything yet

## Running locally

There are several ways to do so.

### Maven

```
mvn spring-boot:run
```
Possibly with option `-Dspring-boot.run.jvmArguments="-Dcodegen.skip"`.

### JAR

```
$JAVA_HOME/bin/java -jar target/nexus-iq-dashboard-extractor-<VERSION>.jar
```

### IntelliJ

If you use the IntelliJ IDE, running the application from there is the most
convenient way.

Just run class `Application`.

## Miscellaneous

Nexus IQ's Open API is quite large and this application uses only a very small
fraction of it.
Therefore, in order to speed up compilation, no usage is made of the
`openapi-generator-maven-plugin` which is commonly used to generate the
marshalling classes and the service classes.
Instead the marshalling classes have been manually created and added to the
source classes.
