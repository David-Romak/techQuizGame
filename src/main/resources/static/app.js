const state = {
    sessionId: null,
    currentQuestion: null,
    quizFinished: false,
    isLoading: false,
    advanceTimerId: null
};

const elements = {
    mainCard: document.getElementById("main-card"),
    endScreen: document.getElementById("end-screen"),
    endEmoji: document.getElementById("end-emoji"),
    endTitle: document.getElementById("end-title"),
    endStats: document.getElementById("end-stats"),
    questionNum: document.getElementById("question-num"),
    progressBar: document.getElementById("progress-bar"),
    iconBox: document.getElementById("icon-box"),
    techIcon: document.getElementById("tech-icon"),
    streakBadge: document.getElementById("streak-badge"),
    revealName: document.getElementById("reveal-name"),
    answerInput: document.getElementById("answer-input"),
    submitBtn: document.getElementById("submit-btn"),
    scoreCorrect: document.getElementById("score-correct"),
    scoreWrong: document.getElementById("score-wrong"),
    scoreTotal: document.getElementById("score-total")
};

async function apiRequest(url, options = {}) {
    const response = await fetch(url, {
        headers: {
            "Content-Type": "application/json"
        },
        ...options
    });

    if (!response.ok) {
        let message = "Something went wrong. Please try again.";

        try {
            const errorBody = await response.text();
            if (errorBody) {
                message = errorBody;
            }
        } catch (_) {
            // Ignore parse issues and keep the generic message.
        }

        throw new Error(message);
    }

    return response.json();
}

function setBusy(isBusy) {
    state.isLoading = isBusy;
    elements.submitBtn.disabled = isBusy || state.quizFinished;
    elements.answerInput.disabled = isBusy || state.quizFinished;

    if (!isBusy && !state.quizFinished) {
        window.requestAnimationFrame(() => {
            focusAnswerInput();
        });
    }
}

function clearAdvanceTimer() {
    if (state.advanceTimerId !== null) {
        window.clearTimeout(state.advanceTimerId);
        state.advanceTimerId = null;
    }
}

function focusAnswerInput() {
    if (elements.answerInput.disabled) {
        return;
    }

    elements.answerInput.focus({ preventScroll: true });
    elements.answerInput.setSelectionRange(0, elements.answerInput.value.length);
}

function showMessage(message, tone = "wrong") {
    elements.revealName.textContent = message;
    elements.revealName.classList.remove("hidden", "correct", "wrong");
    elements.revealName.classList.add(tone);
}

function resetRevealState() {
    elements.iconBox.classList.remove("reveal-correct", "reveal-wrong");
    elements.revealName.textContent = "—";
    elements.revealName.classList.remove("correct", "wrong");
    elements.revealName.classList.add("hidden");
    clearAdvanceTimer();
}

function updateScoreboard(correct, wrong) {
    elements.scoreCorrect.textContent = String(correct);
    elements.scoreWrong.textContent = String(wrong);
    elements.scoreTotal.textContent = String(correct + wrong);
}

function updateProgress(questionNumber, totalQuestions, isFinished = false) {
    const completed = isFinished ? totalQuestions : Math.max(questionNumber - 1, 0);
    const width = totalQuestions > 0 ? (completed / totalQuestions) * 100 : 0;
    elements.progressBar.style.width = `${width}%`;
}

function updateStreak(streak) {
    if (streak > 1) {
        elements.streakBadge.textContent = `🔥 x${streak}`;
        elements.streakBadge.classList.add("show");
        return;
    }

    elements.streakBadge.classList.remove("show");
}

function renderQuestion(question) {
    state.currentQuestion = question;
    state.sessionId = question.sessionId;
    state.quizFinished = Boolean(question.finished);

    if (question.finished) {
        loadScore();
        return;
    }

    elements.mainCard.style.display = "flex";
    elements.endScreen.classList.remove("visible");

    resetRevealState();
    updateScoreboard(question.correct, question.wrong);
    updateProgress(question.questionNumber, question.totalQuestions, false);
    updateStreak(question.streak);

    elements.questionNum.textContent = `Question ${question.questionNumber} / ${question.totalQuestions}`;
    elements.techIcon.src = question.iconUrl || "";
    elements.techIcon.alt = question.category
        ? `${question.category} technology logo`
        : "Tech icon";

    elements.answerInput.value = "";
    focusAnswerInput();
}

function renderAnswer(answer) {
    state.quizFinished = Boolean(answer.quizFinished);

    updateScoreboard(answer.newCorrect, answer.newWrong);
    updateStreak(answer.streak);

    elements.iconBox.classList.remove("reveal-correct", "reveal-wrong");
    elements.iconBox.classList.add(answer.correct ? "reveal-correct" : "reveal-wrong");
    showMessage(`${answer.correctName} · next in 2s`, answer.correct ? "correct" : "wrong");
    elements.answerInput.disabled = true;
    elements.submitBtn.disabled = true;

    clearAdvanceTimer();
    state.advanceTimerId = window.setTimeout(() => {
        if (answer.quizFinished) {
            loadScore();
            return;
        }

        loadNextQuestion();
    }, 2000);
}

function renderScore(summary) {
    updateScoreboard(summary.correct, summary.wrong);
    updateProgress(summary.total, summary.total, true);

    elements.mainCard.style.display = "none";
    elements.endScreen.classList.add("visible");

    elements.endTitle.textContent = "Quiz Complete!";
    elements.endEmoji.textContent = summary.percentage >= 70 ? "🏆" : "💻";
    elements.endStats.innerHTML = [
        `<div><strong>Correct:</strong> ${summary.correct}</div>`,
        `<div><strong>Wrong:</strong> ${summary.wrong}</div>`,
        `<div><strong>Score:</strong> ${summary.percentage}%</div>`,
        `<div><strong>Grade:</strong> ${summary.grade}</div>`
    ].join("");
}

function renderRequestError(error) {
    resetRevealState();
    updateStreak(0);
    showMessage(error.message || "Unable to load quiz data.", "wrong");
    elements.techIcon.removeAttribute("src");
}

async function startQuiz() {
    clearAdvanceTimer();
    setBusy(true);
    state.quizFinished = false;
    elements.mainCard.style.display = "flex";
    elements.endScreen.classList.remove("visible");

    try {
        const question = await apiRequest("/api/quiz/start", {
            method: "POST"
        });
        renderQuestion(question);
    } catch (error) {
        renderRequestError(error);
    } finally {
        setBusy(false);
    }
}

async function submitAnswer() {
    if (state.isLoading || state.quizFinished || !state.sessionId) {
        return;
    }

    const answer = elements.answerInput.value.trim();
    if (!answer) {
        showMessage("Type an answer before checking.", "wrong");
        focusAnswerInput();
        return;
    }

    setBusy(true);

    try {
        const result = await apiRequest("/api/quiz/answer", {
            method: "POST",
            body: JSON.stringify({
                sessionId: state.sessionId,
                answer
            })
        });
        renderAnswer(result);
    } catch (error) {
        showMessage(error.message || "Unable to submit answer.", "wrong");
    } finally {
        setBusy(false);
    }
}

async function loadNextQuestion() {
    if (state.isLoading || !state.sessionId) {
        return;
    }

    if (state.quizFinished) {
        await loadScore();
        return;
    }

    setBusy(true);

    try {
        const question = await apiRequest(`/api/quiz/question/${state.sessionId}`);
        renderQuestion(question);
    } catch (error) {
        showMessage(error.message || "Unable to load the next question.", "wrong");
    } finally {
        setBusy(false);
    }
}

async function loadScore() {
    if (state.isLoading || !state.sessionId) {
        return;
    }

    setBusy(true);

    try {
        const summary = await apiRequest(`/api/quiz/score/${state.sessionId}`);
        renderScore(summary);
    } catch (error) {
        showMessage(error.message || "Unable to load your final score.", "wrong");
    } finally {
        setBusy(false);
    }
}

elements.submitBtn.addEventListener("click", submitAnswer);
elements.answerInput.addEventListener("keydown", (event) => {
    if (event.key === "Enter") {
        event.preventDefault();
        submitAnswer();
    }
});

elements.techIcon.addEventListener("error", () => {
    elements.techIcon.removeAttribute("src");
    if (elements.revealName.classList.contains("hidden")) {
        showMessage("Icon failed to load.", "wrong");
    }
});

window.startQuiz = startQuiz;

startQuiz();
