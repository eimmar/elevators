# Elevator Coding Challenge

Create an elevator controller!

This is a skeleton project with two interfaces that you must implement.

You are going to create an Elevator Controller and a number of Elevators that will be managed by the controller. There are a few extra classes already added to the project to get you up and running quickly.

## To Do

There are two interfaces to implement.

 * `Elevator` - this is the elevator itself and has a few public methods to take care of. There can be n number of elevators at the same time

 * `ElevatorController` - this is the elevator manager that keeps track of all the elevators running in the elevator shaft. There should be only one ElevatorController

## What We Expect

Implemented elevator and controller assumes that there is a controller on each floor which allows user to select any
floor he wants to go. If there are no elevator at the floor from which the request is made then the controller checks if there
are any moving elevators which are traveling the same direction. 
### Deliver Your Solution

Add the code to a github or bitbucket repository. You can also make an archive of the project and e-mail it to us. We would like to see your solution within 7 days.
 
## Build And Run (as is)

Project can be started with

    mvn package
    mvn spring-boot:run