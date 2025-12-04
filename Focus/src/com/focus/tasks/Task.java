package com.focus.tasks;

import java.time.LocalDate;

public class Task {
    private String title;
    private boolean completed;
    private LocalDate dueDate;

    public Task(String title, LocalDate dueDate, boolean completed) {
        this.title = title;
        this.dueDate = dueDate;
        this.completed = completed;
    }

    public String getTitle() {
        return title;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public void markCompleted() {
        this.completed = true;
    }

   
    public String toDataString() {
        return title + "|" + dueDate + "|" + completed;
    }

    @Override
    public String toString() {
        return String.format("%-20s | %-12s | %s",
                title, dueDate, (completed ? "✔" : "✖"));
    }
}



