package com.focus.studyhelper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Distraction {
    private String reason;
    private LocalDateTime timestamp;

    public Distraction(String reason) {
        this.reason = reason;
        this.timestamp = LocalDateTime.now();
    }

    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        return "[" + timestamp.format(formatter) + "] " + reason;
    }
}