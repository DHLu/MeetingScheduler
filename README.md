# Meeting Scheduler

## How to run it

There are two ways.
1. Build the project and run it with input file path as parameter: `java -jar meeting-scheduler-0.1-jar-with-dependencies.jar PATH_TO_INPUT`

2. Use maven and run `mvn exec:java -Dexec.mainClass=dlu.meetingscheduler.Main -Dexec.args=src/main/resources/meetings` at the right directory. 