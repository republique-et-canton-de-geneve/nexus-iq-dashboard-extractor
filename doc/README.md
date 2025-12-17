The JSON file contains Nexus IQ's REST API definition in OpenApi format.

# Upgrading the API

The Nexus IQ API evolves as new Nexus IQ versions are released.
Here is the procedure for upgrade.

## Step 1: download the latest version

Download the latest version at URL
https://sonatype.github.io/sonatype-documentation/api/iq/latest/iq-api.json.

Replace file `iq-api.json` with the downloaded file.

## Step 2: fix the contract

Without the modification described below, command `mvn clean compile` will
generate the serialization Java classes, but it will fail to compile.
The compilation error originates from the following JSON fragment in
file `iq-api.json` :
```
    "EpssData": {
        "type": "object",
            "properties": {
                "current_score": {
                    "type": "number",
                    "format": "double",
                    "writeOnly": true
               	},
                "currentScore": {
                    "type": "number",
                    "format": "double"
                }
            }
        }
    },
```
Properties `current_score` and `current_score` have very similar names.
Generation ends up with a class `EppsData` with 2 properties having the same
name `currentScore`, which yields the compilation error.

In order to overcome the problem, you must edit file `iq-api.json` and
remove 5 lines, starting at the line containing `"current_score"`.
This will create no trouble at run time, as class EpssData is not used by
the use case implemented in this application.
