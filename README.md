# Open Brewery DB API

An API that allows users to search the open brewery DB.

## Getting Started
In order to run this application locally: 
1. run BreweriesApplication as a spring boot app.
2. Use http://localhost:8080/ as the base address
3. head to http://localhost:8080/swagger-ui/index.html#/ for the docs on the available endpoints.

## Testing

All unit tests in this project use Wiremock to set up a mock dependency. 
Do not run the unit tests while you are running the acutal application or they will all fail.