package com.romac.techquizgame.model;

/**
 * Data Transfer Objects — these are what travel over the wire as JSON.
 * Hey, what we're doing here is: separating our internal model (QuizSession, Tech)
 * from what we expose to the client. The client never sees the answer stored
 * in Tech until AFTER they submit.
 */
public class QuizDTOs {

    // ----- Outbound: sent TO the client -----

    /** Sent when the client requests the next question */
    public static class QuestionResponse {
        private String sessionId;
        private int questionNumber;   // 1-based for display
        private int totalQuestions;
        private String iconUrl;
        private String category;
        private int correct;
        private int wrong;
        private int streak;
        private boolean finished;

        public QuestionResponse() {}

        // Convenience constructor
        public QuestionResponse(String sessionId, int questionNumber, int totalQuestions,
                                String iconUrl, String category,
                                int correct, int wrong, int streak, boolean finished) {
            this.sessionId = sessionId;
            this.questionNumber = questionNumber;
            this.totalQuestions = totalQuestions;
            this.iconUrl = iconUrl;
            this.category = category;
            this.correct = correct;
            this.wrong = wrong;
            this.streak = streak;
            this.finished = finished;
        }

        public String getSessionId() { return sessionId; }
        public void setSessionId(String s) { this.sessionId = s; }
        public int getQuestionNumber() { return questionNumber; }
        public void setQuestionNumber(int n) { this.questionNumber = n; }
        public int getTotalQuestions() { return totalQuestions; }
        public void setTotalQuestions(int n) { this.totalQuestions = n; }
        public String getIconUrl() { return iconUrl; }
        public void setIconUrl(String s) { this.iconUrl = s; }
        public String getCategory() { return category; }
        public void setCategory(String s) { this.category = s; }
        public int getCorrect() { return correct; }
        public void setCorrect(int n) { this.correct = n; }
        public int getWrong() { return wrong; }
        public void setWrong(int n) { this.wrong = n; }
        public int getStreak() { return streak; }
        public void setStreak(int n) { this.streak = n; }
        public boolean isFinished() { return finished; }
        public void setFinished(boolean b) { this.finished = b; }
    }

    /** Sent after the client submits an answer */
    public static class AnswerResponse {
        private boolean correct;
        private String correctName;   // always returned so frontend can show it
        private String sessionId;
        private int newCorrect;
        private int newWrong;
        private int streak;
        private boolean quizFinished;

        public AnswerResponse() {}

        public AnswerResponse(boolean correct, String correctName, String sessionId,
                              int newCorrect, int newWrong, int streak, boolean quizFinished) {
            this.correct = correct;
            this.correctName = correctName;
            this.sessionId = sessionId;
            this.newCorrect = newCorrect;
            this.newWrong = newWrong;
            this.streak = streak;
            this.quizFinished = quizFinished;
        }

        public boolean isCorrect() { return correct; }
        public void setCorrect(boolean b) { this.correct = b; }
        public String getCorrectName() { return correctName; }
        public void setCorrectName(String s) { this.correctName = s; }
        public String getSessionId() { return sessionId; }
        public void setSessionId(String s) { this.sessionId = s; }
        public int getNewCorrect() { return newCorrect; }
        public void setNewCorrect(int n) { this.newCorrect = n; }
        public int getNewWrong() { return newWrong; }
        public void setNewWrong(int n) { this.newWrong = n; }
        public int getStreak() { return streak; }
        public void setStreak(int n) { this.streak = n; }
        public boolean isQuizFinished() { return quizFinished; }
        public void setQuizFinished(boolean b) { this.quizFinished = b; }
    }

    // ----- Inbound: received FROM the client -----

    /** Client sends this when submitting an answer */
    public static class AnswerRequest {
        private String sessionId;
        private String answer;

        public AnswerRequest() {}

        public String getSessionId() { return sessionId; }
        public void setSessionId(String s) { this.sessionId = s; }
        public String getAnswer() { return answer; }
        public void setAnswer(String s) { this.answer = s; }
    }

    /** Final score summary sent when quiz is complete */
    public static class ScoreSummary {
        private int correct;
        private int wrong;
        private int total;
        private int percentage;
        private String grade;

        public ScoreSummary(int correct, int wrong, int total) {
            this.correct = correct;
            this.wrong = wrong;
            this.total = total;
            this.percentage = total > 0 ? (int) Math.round((correct * 100.0) / total) : 0;
            this.grade = computeGrade(this.percentage);
        }

        private String computeGrade(int pct) {
            if (pct >= 90) return "Tech God 🏆";
            if (pct >= 70) return "Stack Master 🔥";
            if (pct >= 50) return "Getting There 💪";
            return "Keep Practicing 😅";
        }

        public int getCorrect() { return correct; }
        public int getWrong() { return wrong; }
        public int getTotal() { return total; }
        public int getPercentage() { return percentage; }
        public String getGrade() { return grade; }
    }
}
