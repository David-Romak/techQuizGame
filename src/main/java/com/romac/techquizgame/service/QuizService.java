package com.romac.techquizgame.service;

import com.romac.techquizgame.model.QuizDTOs.AnswerRequest;
import com.romac.techquizgame.model.QuizDTOs.AnswerResponse;
import com.romac.techquizgame.model.QuizDTOs.QuestionResponse;
import com.romac.techquizgame.model.QuizDTOs.ScoreSummary;
import com.romac.techquizgame.model.QuizSession;
import com.romac.techquizgame.model.Tech;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The brain of the quiz.
 *
 * Hey, what we're doing here is: storing sessions in a ConcurrentHashMap
 * (an in-memory, thread-safe key-value store). Each session gets a unique UUID
 * so multiple players can quiz simultaneously without stepping on each other.
 *
 * In production you'd swap this for Redis or a DB — but the interface stays the same.
 * That's the beauty of keeping business logic in a @Service layer.
 */
@Service
public class QuizService {

    private static final int DEFAULT_QUIZ_LENGTH = 20;

    // sessionId → QuizSession  (our in-memory "database")
    private final Map<String, QuizSession> sessions = new ConcurrentHashMap<>();

    private final TechLibrary library;

    // Spring injects TechLibrary here automatically (Constructor Injection)
    public QuizService(TechLibrary library) {
        this.library = library;
    }

    // ─────────────────────────────────────────────────────────────────────────
    // PUBLIC API
    // ─────────────────────────────────────────────────────────────────────────

    /**
     * Start a brand-new quiz session.
     * Shuffles the full tech library and slices QUIZ_LENGTH questions.
     */
    public QuestionResponse startSession() {
        String sessionId = UUID.randomUUID().toString();

        List<Tech> allTechs = new ArrayList<>(library.getAllTechs());
        Collections.shuffle(allTechs);
        List<Tech> queue = allTechs.subList(0, Math.min(DEFAULT_QUIZ_LENGTH, allTechs.size()));

        QuizSession session = new QuizSession(sessionId, new ArrayList<>(queue), DEFAULT_QUIZ_LENGTH);
        sessions.put(sessionId, session);

        return buildQuestionResponse(session);
    }

    /**
     * Advance to the next question in an existing session.
     * Called by the client after it has processed an answer.
     */
    public QuestionResponse nextQuestion(String sessionId) {
        QuizSession session = getSession(sessionId);
        return buildQuestionResponse(session);
    }

    /**
     * Check the user's answer against the current question.
     * Normalizes both strings to lowercase and trims whitespace.
     * Also checks against any registered aliases.
     */
    public AnswerResponse checkAnswer(AnswerRequest request) {
        QuizSession session = getSession(request.getSessionId());

        if (session.isFinished()) {
            throw new IllegalStateException("Quiz already finished");
        }

        Tech current = session.getCurrentTech();
        String userAnswer = normalize(request.getAnswer());
        boolean isCorrect = isAnswerCorrect(userAnswer, current);

        // Update session state
        if (isCorrect) {
            session.setCorrect(session.getCorrect() + 1);
            session.setStreak(session.getStreak() + 1);
        } else {
            session.setWrong(session.getWrong() + 1);
            session.setStreak(0);
        }

        // Advance the pointer AFTER scoring (so the next /question call gets the next one)
        session.advance();

        return new AnswerResponse(
            isCorrect,
            current.getName(),         // always return the correct name
            session.getSessionId(),
            session.getCorrect(),
            session.getWrong(),
            session.getStreak(),
            session.isFinished()
        );
    }

    /**
     * Build the final score summary for the end screen.
     */
    public ScoreSummary getScore(String sessionId) {
        QuizSession session = getSession(sessionId);
        return new ScoreSummary(
            session.getCorrect(),
            session.getWrong(),
            session.getTotalQuestions()
        );
    }

    // ─────────────────────────────────────────────────────────────────────────
    // PRIVATE HELPERS
    // ─────────────────────────────────────────────────────────────────────────

    private QuizSession getSession(String sessionId) {
        QuizSession session = sessions.get(sessionId);
        if (session == null) {
            throw new NoSuchElementException("Session not found: " + sessionId);
        }
        return session;
    }

    private QuestionResponse buildQuestionResponse(QuizSession session) {
        if (session.isFinished()) {
            // Signal to frontend that quiz is done — no icon needed
            return new QuestionResponse(
                session.getSessionId(),
                session.getTotalQuestions(),
                session.getTotalQuestions(),
                null, null,
                session.getCorrect(),
                session.getWrong(),
                session.getStreak(),
                true
            );
        }

        Tech current = session.getCurrentTech();
        return new QuestionResponse(
            session.getSessionId(),
            session.getCurrentIndex() + 1,  // 1-based
            session.getTotalQuestions(),
            current.getIconUrl(),
            current.getCategory(),
            session.getCorrect(),
            session.getWrong(),
            session.getStreak(),
            false
        );
    }

    private boolean isAnswerCorrect(String normalizedAnswer, Tech tech) {
        if (normalize(tech.getName()).equals(normalizedAnswer)) return true;
        for (String alias : tech.getAliases()) {
            if (normalize(alias).equals(normalizedAnswer)) return true;
        }
        return false;
    }

    /**
     * Normalize: lowercase + trim.
     * Hey, what we're doing here is: making the comparison case-insensitive
     * so "REACT", "React", "react" all count as correct.
     */
    private String normalize(String s) {
        return s == null ? "" : s.trim().toLowerCase();
    }
}
