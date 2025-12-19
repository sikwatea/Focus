package com.focus.flashcards;

import java.io.Serializable;

public class Flashcard implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String question;
    private String answer;

    public Flashcard(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getQuestion() { return question; }
    public String getAnswer() { return answer; }

    public void setQuestion(String question) { this.question = question; }
    public void setAnswer(String answer) { this.answer = answer; }

    public String display() {
        return "Q: " + question + " | A: " + answer;
    }

    @Override
    public String toString() {
        return display();
    }
}

// removed hash code stuff