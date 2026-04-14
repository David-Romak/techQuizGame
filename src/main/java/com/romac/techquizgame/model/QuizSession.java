package com.romac.techquizgame.model;

import java.util.List;

/**
 * Represents an active quiz session.
 * Hey, what we're doing here is: storing the full shuffled question queue
 * server-side so the client can never cheat by inspecting the answer
 * before submitting. The client only ever sees one question at a time.
 */
public class QuizSession {

    private String sessionId;
    private List<Tech> queue;      // shuffled question list (server-side only)
    private int currentIndex;
    private int correct;
    private int wrong;
    private int streak;
    private int totalQuestions;

    public QuizSession() {}

    public QuizSession(String sessionId, List<Tech> queue, int totalQuestions) {
        this.sessionId = sessionId;
        this.queue = queue;
        this.currentIndex = 0;
        this.correct = 0;
        this.wrong = 0;
        this.streak = 0;
        this.totalQuestions = totalQuestions;
    }

    public boolean isFinished() {
        return currentIndex >= totalQuestions;
    }

    public Tech getCurrentTech() {
        if (isFinished()) return null;
        return queue.get(currentIndex);
    }

    public void advance() {
        currentIndex++;
    }

    // --- Getters & Setters ---

    public String getSessionId() { return sessionId; }
    public void setSessionId(String sessionId) { this.sessionId = sessionId; }

    public List<Tech> getQueue() { return queue; }
    public void setQueue(List<Tech> queue) { this.queue = queue; }

    public int getCurrentIndex() { return currentIndex; }
    public void setCurrentIndex(int currentIndex) { this.currentIndex = currentIndex; }

    public int getCorrect() { return correct; }
    public void setCorrect(int correct) { this.correct = correct; }

    public int getWrong() { return wrong; }
    public void setWrong(int wrong) { this.wrong = wrong; }

    public int getStreak() { return streak; }
    public void setStreak(int streak) { this.streak = streak; }

    public int getTotalQuestions() { return totalQuestions; }
    public void setTotalQuestions(int totalQuestions) { this.totalQuestions = totalQuestions; }
}
