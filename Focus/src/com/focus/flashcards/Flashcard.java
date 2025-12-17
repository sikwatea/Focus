package com.focus.flashcards;

public class Flashcard {
	 private String question;
	 private String answer;

	 public Flashcard() {
	     this.question = "";
	     this.answer = "";
	 }

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

	 @Override
	 public boolean equals(Object obj) {
	     if (this == obj) return true;
	     if (!(obj instanceof Flashcard)) return false;
	     Flashcard other = (Flashcard) obj;
	     return question.equals(other.question) && answer.equals(other.answer);
	 }

	 @Override
	 public int hashCode() {
	     return question.hashCode() + answer.hashCode();
	 }
	}
