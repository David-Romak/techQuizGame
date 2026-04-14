package com.romac.techquizgame.controller;

import com.romac.techquizgame.model.QuizDTOs.AnswerRequest;
import com.romac.techquizgame.model.QuizDTOs.AnswerResponse;
import com.romac.techquizgame.model.QuizDTOs.QuestionResponse;
import com.romac.techquizgame.model.QuizDTOs.ScoreSummary;
import com.romac.techquizgame.service.QuizService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * The HTTP layer. Hey, what we're doing here is: keeping this class THIN —
 * it does nothing but receive HTTP requests, delegate to QuizService,
 * and return responses. Zero business logic lives here.
 *
 * @RestController = @Controller + @ResponseBody
 * Every return value is automatically serialized to JSON by Jackson.
 *
 * API surface:
 *   POST /api/quiz/start           → start new session, get first question
 *   GET  /api/quiz/question/{id}   → get current/next question
 *   POST /api/quiz/answer          → submit answer, get result
 *   GET  /api/quiz/score/{id}      → get final score summary
 */
@RestController
@RequestMapping("/api/quiz")
@CrossOrigin(origins = "*") // allow frontend served from same or different port
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    /**
     * POST /api/quiz/start
     * Creates a new session and returns the first question.
     */
    @PostMapping("/start")
    public ResponseEntity<QuestionResponse> startQuiz() {
        return ResponseEntity.ok(quizService.startSession());
    }

    /**
     * GET /api/quiz/question/{sessionId}
     * Returns the current question for an existing session.
     * Called after processing each answer to fetch the next question.
     */
    @GetMapping("/question/{sessionId}")
    public ResponseEntity<QuestionResponse> getQuestion(@PathVariable String sessionId) {
        return ResponseEntity.ok(quizService.nextQuestion(sessionId));
    }

    /**
     * POST /api/quiz/answer
     * Body: { "sessionId": "...", "answer": "React" }
     * Returns whether the answer was correct + the correct name.
     */
    @PostMapping("/answer")
    public ResponseEntity<AnswerResponse> submitAnswer(@RequestBody AnswerRequest request) {
        return ResponseEntity.ok(quizService.checkAnswer(request));
    }

    /**
     * GET /api/quiz/score/{sessionId}
     * Returns final score summary when quiz is complete.
     */
    @GetMapping("/score/{sessionId}")
    public ResponseEntity<ScoreSummary> getScore(@PathVariable String sessionId) {
        return ResponseEntity.ok(quizService.getScore(sessionId));
    }
}
