# Mastermind Game API

The API facilitates the creation of a game session, adding players, making guesses, and retrieving game history.

## Overview
The Mastermind Game API is a robust and scalable solution developed using Spring Boot and Spring Data JPA for backend processes and database interactions, respectively. The API is designed to facilitate the creation of a game session, adding players, making guesses, and retrieving game history. It supports multiple players and sessions, making it scalable and flexible for enhancements like real-time multiplayer gameplay.

The purpose of this application is to provide a platform for users to play the classic game of Mastermind in a digital format. It solves the problem of needing physical game components and allows users to play the game from anywhere, at any time. The key features of this application include player creation, game creation, making guesses, retrieving game history, providing hints, stopping the game timer, and updating game settings.

## Prerequisites
Before running the project, ensure you have the following installed:
* Java 11
* Maven (for dependency management and running the project)
* MySQL Server (for the database)

## Installation
1. Clone the repository to your local machine using `git clone <repository_url>`.
2. Navigate to the project directory using `cd <project_directory>`.
3. Install the dependencies using Maven with the command `mvn clean install`.

## Running the Application
To run the Mastermind Game API, use the following commands:
```  
mvn spring-boot:run
```
The API will be available at http://localhost:8080/api/.

## Database Setup
The game data is stored in a MySQL database. Initialize your database using the provided SQL scripts located in the sql folder:

* For production environments: Run mastermind-schema-prod.sql
* For testing environments: Run mastermind-schema-test.sql

These scripts create the necessary tables and insert seed data for initial setup.

## Configuration
Configure the database connection settings in the application.properties file for both development and testing environments:
```
spring.datasource.url=${DB_URL}
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}
```
Replace ${DB_URL}, ${DB_USERNAME}, and ${DB_PASSWORD} with your database URL, username, and password.

## API Endpoints
Interact with the game using the following endpoints, or use the api-requests.http file:

### Player Endpoints
* POST /api/player: Creates a new player. The request body should contain a JSON object with the player's name. 
* GET /api/player/{playerId}/score: Retrieves the score of a player with the given ID. 
* GET /api/player/{playerId}/games: Retrieves the game history of a player with the given ID.

### Game Endpoints
* POST /api/game: Starts a new game. The request body should contain a JSON object with the player IDs and the difficulty level. 
* POST /api/game/{gameId}/guess: Makes a guess in the game with the given ID. The request body should contain a JSON object with the player ID and the guess sequence. 
* GET /api/game/{gameId}/history: Retrieves the history of the game with the given ID. 
* GET /api/game/{gameId}/hint: Retrieves a hint for the game with the given ID.
* POST /api/game/{gameId}/stop-timer: Stops the timer for the game with the given ID. 
* PUT /api/game/{gameId}/settings: Updates the settings for the game with the given ID. The request body should contain a JSON object with the new settings.

* Please replace {playerId} and {gameId} with the actual IDs when making requests.

## Testing
Run the unit tests configured in the src/test/java directory to ensure functionality:
```
mvn test
```

## Implemented Extensions
In addition to the base functionality, the following creative extensions have been implemented:

1. **Real-time Multiplayer Functionality**: The API supports multiple players and sessions, allowing for real-time multiplayer gameplay.

2. **Hint System**: A hint system has been implemented to assist players in making their next guess. The hint system varies based on the difficulty level of the game.

3. **Game Timer**: A game timer has been implemented to track the duration of each game. Players can stop the timer at any time.

4. **Game Settings Update**: Players can update the settings of a game, allowing for customization and flexibility.

5. **Player Score Tracking**: The API keeps track of each player's score, providing a competitive aspect to the game.

## Future Extensions
Future enhancements include:
* Enhanced security features like authentication and HTTPS.
* More complex scoring algorithms.