package com.focus.flashcards;

import java.util.ArrayList;
import java.util.List;

public class FlashcardSet {
    private String name;
    private List<Flashcard> cards = new ArrayList<>();

    public FlashcardSet(String name) {
        this.name = name;
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public List<Flashcard> getCards() { return cards; }

    public void addFlashcard(Flashcard card) { cards.add(card); }

    public boolean removeFlashcard(int index) {
        if(index >= 0 && index < cards.size()) {
            cards.remove(index);
            return true;
        }
        return false;
    }

    public boolean editFlashcard(int index, String question, String answer) {
        if(index >= 0 && index < cards.size()) {
            Flashcard c = cards.get(index);
            c.setQuestion(question);
            c.setAnswer(answer);
            return true;
        }
        return false;
    }

    public int size() { return cards.size(); }
}
