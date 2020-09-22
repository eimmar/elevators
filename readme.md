# Elevator Coding Challenge

Create an elevator controller!

This is a skeleton project with two interfaces that you must implement.

You are going to create an Elevator Controller and a number of Elevators that will be managed by the controller. There are a few extra classes already added to the project to get you up and running quickly.

### Solution

Implemented elevator and it's controller assumes that there is a button pad on each floor which allows user to select any
floor he wants to go. If there are no free elevators available then the controller checks for the nearest currently 
moving elevator which is traveling the same direction as the user wants to go.

## Build And Run (as is)

Project can be started with

    mvn package
    mvn spring-boot:run
    
## Testing
Testing can be done in two ways:
1. Using the GET endpoint `/rest/v1/request-elevator/{requestedFromFloor}/{requestedToFloor}` where
* `requestedFromFloor` is the floor from which the elevator is being requested
* `requestedToFloor` is the floor to which the user wants to go.
The endpoint returns id of the available elevator or null if no elevator is available.
Elevator travel log is being displayed to the console by the `ElevatorConsoleLogger` class.

2. Using the `src/test/kotlin/com/tingco/codechallenge/elevator/IntegrationTest.kt:simulateLoadedElevatorShaft` test method
where you can configure elevator requests and delays between each request by editing `elevatorRequests` pair list.
Elevator travel log is being displayed to the test console output by the `ElevatorConsoleLogger` class.
