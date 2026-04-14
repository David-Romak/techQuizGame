# Tech Icon Quiz

A Spring Boot quiz game that shows a technology logo and asks the player to name it. The backend manages quiz sessions and scoring, and the frontend is a lightweight static HTML/CSS/JavaScript client served by the same application.

## Overview

- Spring Boot serves both the web page and the REST API
- The quiz contains 20 randomly selected technologies per session
- Answers are checked on the server, not in the browser
- After each answer, the app automatically advances to the next question after 2 seconds
- Final results include correct answers, wrong answers, percentage score, and a grade

## Tech Stack

- Java 21
- Spring Boot 4
- Maven Wrapper (`./mvnw`)
- Plain HTML, CSS, and JavaScript

## Running the App

Start the application:

```bash
./mvnw spring-boot:run
```

Then open:

```text
http://localhost:8080
```

Run tests:

```bash
./mvnw test
```

Build the jar:

```bash
./mvnw package
```

Run the packaged jar:

```bash
java -jar target/techQuizGame-0.0.1-SNAPSHOT.jar
```

## How the App Works

1. The browser loads `index.html` from the Spring Boot app.
2. `app.js` immediately sends `POST /api/quiz/start`.
3. The backend creates a new in-memory quiz session, shuffles the tech list, and returns the first question.
4. The frontend renders the icon, focuses the input field, and waits for the user to submit an answer.
5. When the user answers, the browser sends `POST /api/quiz/answer`.
6. The backend checks the answer, updates the session score and streak, and returns the result.
7. The frontend shows the correct answer and automatically loads the next question after 2 seconds.
8. When all questions are complete, the browser requests `GET /api/quiz/score/{sessionId}` and displays the final score screen.

## API

### `POST /api/quiz/start`

Starts a new quiz session and returns the first question.

Example response:

```json
{
  "sessionId": "7d58f707-8dc1-4d6f-89f4-b5f41046e21d",
  "questionNumber": 1,
  "totalQuestions": 20,
  "iconUrl": "https://cdn.jsdelivr.net/gh/devicons/devicon/icons/react/react-original.svg",
  "category": "Frontend",
  "correct": 0,
  "wrong": 0,
  "streak": 0,
  "finished": false
}
```

### `GET /api/quiz/question/{sessionId}`

Returns the current next question for an existing session.

### `POST /api/quiz/answer`

Checks the submitted answer for the current question.

Request body:

```json
{
  "sessionId": "7d58f707-8dc1-4d6f-89f4-b5f41046e21d",
  "answer": "React"
}
```

Example response:

```json
{
  "correct": true,
  "correctName": "React",
  "sessionId": "7d58f707-8dc1-4d6f-89f4-b5f41046e21d",
  "newCorrect": 1,
  "newWrong": 0,
  "streak": 1,
  "quizFinished": false
}
```

### `GET /api/quiz/score/{sessionId}`

Returns the final score summary for a completed quiz session.

Example response:

```json
{
  "correct": 14,
  "wrong": 6,
  "total": 20,
  "percentage": 70,
  "grade": "Stack Master 🔥"
}
```

## Project Structure

```text
src/main/java/com/romac/techquizgame
├── TechQuizGameApplication.java
├── HomeController.java
├── controller
│   └── QuizController.java
├── model
│   ├── QuizDTOs.java
│   ├── QuizSession.java
│   └── Tech.java
└── service
    ├── QuizService.java
    └── TechLibrary.java

src/main/resources
├── application.yaml
└── static
    ├── app.js
    ├── index.html
    └── styles.css
```

## Backend Design

- `QuizController` exposes the REST endpoints
- `QuizService` contains the quiz rules, answer checking, and session state
- `TechLibrary` is the source of truth for the available technologies and icon URLs
- `QuizSession` stores the active in-memory state for one player session
- `QuizDTOs` defines the JSON request and response shapes

Sessions are stored in memory with a `ConcurrentHashMap`, so restarting the server clears active quizzes.

## Frontend Behavior

- The page is served from `src/main/resources/static`
- The icon is shown immediately without blur
- The answer input auto-focuses on first load and after each next question
- The score bar and counters update after each answer
- Correct and wrong answers show different visual feedback
- The quiz advances automatically after 2 seconds
- The end screen allows restarting a new quiz session

## Adding More Technologies

To extend the quiz, add more entries in `src/main/java/com/romac/techquizgame/service/TechLibrary.java`.

Example:

```java
tech("Deno", "denojs/denojs-original.svg", List.of(), "Backend")
```

Icons are loaded from the Devicon CDN, so each item needs:

- a display name
- an icon path
- optional aliases
- a category

## Notes

- This app currently uses in-memory storage only
- There is no authentication or persistence
- If the Devicon CDN is unavailable, the quiz can still run but image loading may fail
