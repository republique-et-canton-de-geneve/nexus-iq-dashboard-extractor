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

## Pre-step 1: configuring a user in Nexus IQ

Do the following:
- As an administrator, log on to the Nexus IQ GUI
- Mentally select an existing user or create an ad hoc user. In the latter case:
  - Click on `System Preferences` > `Users` > `Create User`
- Mentally select an existing role or create an ad hoc role. At État de Genève,
  we create an ad hoc role with permission `View IQ elements`
- Make sure the above user has the above role:
  - Click on `Orgs and Policies`
  - Select the Root Organization
  - Click on tab `Access`
  - In sub-pane `Access`, make sure the user has the role

## Pre-step 2: configuring the application's property file

Do the following:
- Go to directory `src/main/resources`
- Copy file `application-base.yml` (this file is under Git control)
  to a new file `application.yml` (this file is under Git ignore),
  in the same directory
- Edit file `application.yml` and replace the values set to `TO_BE_PROVIDED`
  by appropriate values.

## Running locally

There are several equivalent ways to do so.
They all generate an output file named `result_<DATE>.json` in
subdirectory `output`.

### With Maven

```
mvn spring-boot:run
```
Possibly with option `-Dspring-boot.run.jvmArguments="-Dcodegen.skip"`.

### With the JAR file

```
$JAVA_HOME/bin/java -jar target/nexus-iq-dashboard-extractor-<VERSION>.jar
```

### With IntelliJ

If you use the IntelliJ IDE, running the application from there is the most
convenient way.

Just run class `Application`.

# Running the application at État de Genève

The instructions for releasing the application, deploying it and making Splunk
leverage the output are provided in a separate Git project stored in
État de Genève's internal GitLab server.

Note that the file `application.yml` embedded in the JAR file can easily be
overridden, for example by using the JVM option
`-Dspring.config.location=file:path_to_file_application_yml`.

# Miscellaneous

Nexus IQ's Open API is quite large and this application uses only a very small
fraction of it.
Therefore, in order to speed up compilation, no usage is made of the
`openapi-generator-maven-plugin` which is commonly used to generate the
marshalling classes and the service classes.
Instead the marshalling classes have been manually created and added to the
source classes.

The sources of this project include a GitHub workflow that implements the
project's continuous integration.
In particular, the workflow spawns a Sonar analysis; the detected issues
can be viewed in the
[SonarCloud project](https://sonarcloud.io/project/overview?id=republique-et-canton-de-geneve_nexus-iq-dashboard-extractor).
