# Log Analyzer

## Prerequisites

- Java 8
- Maven 3 or Higher version (Tested with 3.6.2)

## Build the project

Run `mvn clean install`.

This command will run all the test cases and build an executable jar file inside the target folder.

Make sure to use `logs-analyzer-1.0-SNAPSHOT-jar-with-dependencies.jar` as the executable.

## Running the project

### Command
```bash
java -jar <jar-file-path> <log-file-path>
```

If you are in the project home run the following command to
see the result of the provided example log file.

```bash
 java -jar target/logs-analyzer-1.0-SNAPSHOT-jar-with-dependencies.jar programming-task-example-data.log
```

## Assumptions

- The log format is not changed (For any other log format the log line creator can be extended).
- URL is a combination of the `URL Path + HTTP method`.
- Logs with 3XX, and 4XX status codes are also considered valid requests.
- The log file is not large so all the constructed objects for the log file can be stored in memory until the analysis is completed.