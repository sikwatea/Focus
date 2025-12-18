package com.focus.tasks;

import java.time.LocalDate;

// separated from task kay it's not public
public class StudyTask extends Task {
    private static final long serialVersionUID = 1L;

    public StudyTask(String title, LocalDate dueDate) {
        super(title, dueDate);
    }

    @Override
    public String getType() {
        return "Study";
    }
}