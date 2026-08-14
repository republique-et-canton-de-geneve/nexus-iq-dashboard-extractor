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

## Building the application

With Maven 3 and Java 21+:

```shell
mvn clean package
```

## Running the application locally

### Pre-step 1: configuring a user in Nexus IQ

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

### Pre-step 2: integration

The project come with an .env.base to test locally copy it to .env and configure your value inside.

Do the following:

- Go to directory `.`
- Copy file `.env.base` (this file is under Git control)
  to a new file `.env` (this file is under Git ignore),
  in the same directory
- Edit file `.env` and replace the values set to `TO_BE_PROVIDED`
  by appropriate values.

You can test .env configuration with vscode plugin [humao.rest-client].

```http
GET {{$dotenv APP_NEXUS_IQ_URL}}api/v2/reports/applications
Accept: application/json
Authorization: Basic {{$dotenv APP_NEXUS_IQ_USERNAME}}:{{$dotenv APP_NEXUS_IQ_PASSWORD}}
```

That should return some json with application information:

```json
[
  {
    "stage": "release",
    "applicationId": "...",
    "evaluationDate": "2020-08-27T12:03:28.181+02:00",
    "latestReportHtmlUrl": "ui/links/application/.../latestReport/release",
    "reportHtmlUrl": "ui/links/application/.../report/...",
    "embeddableReportHtmlUrl": "ui/links/application/.../report/.../embeddable",
    "reportPdfUrl": "ui/links/application/.../report/.../pdf",
    "reportDataUrl": "api/v2/applications/.../reports/.../raw"
  },
  ...
```

### Running locally

There are several equivalent ways to do so.

#### With Maven

```shell
#load env variable
. .env
mvn spring-boot:run
```

#### With the JAR file

```shell
#load env variable
. .env
$JAVA_HOME/bin/java -jar target/nexus-iq-dashboard-extractor-<VERSION>.jar
```
### Output

Running the application generates a JSON output file `result_<DATE>.json` in
subdirectory `output`.
The JSON file consists in an array of reports.
Example of a report:

```json
{
    "organizationName": "SOME-DEPARTMENT-OF-THE-ORGANIZATION",
    "applicationName": "SOME-APPLICATION",
    "evaluationDate": "2025-09-26T09:07:02.681+02:00",
    "componentDisplayName": "org.apache.commons : commons-lang3 : 3.1",
    "componentIdentifierFormat": "maven",
    "componentIdentifierCoordinates": {
        "artifactId": "commons-lang3",
        "classifier": "",
        "extension": "jar",
        "groupId": "org.apache.commons",
        "version": "3.1"
    },
    "severityIssueSource": "cve",
    "severityIssueReference": "CVE-2025-48924",
    "severityIssueSeverity": 6.9,
    "severityIssueStatus": "Open",
    "severityIssueUrl": "https://www.cve.org/CVERecord?id=CVE-2025-48924",
    "severityIssueThreatCategory": "severe",
    "severityIssueCwe": "674"
},
```

## Running the application at État de Genève

The instructions for releasing the application, deploying it and making Splunk
leverage the output are provided in a separate Git project stored in
État de Genève's internal GitLab server.

Note that the file `application.yml` embedded in the JAR file can easily be
overridden, for example by using the JVM option
`-Dspring.config.location=file:path_to_file_application_yml`.

## Miscellaneous

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

## Limitation

This code do not work if you use http proxy to acces nexus iq.
