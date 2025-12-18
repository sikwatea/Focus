package com.focus.tasks;

import java.util.*;
import java.io.*;
import java.time.LocalDate;

public class TaskList {
    private ArrayList<Task> tasks;
    private final String FILE_NAME = "tasks.dat";

    public TaskList() {
        loadTasks();
    }

    // added String type so that we can edit type
    public void addTask(String title, LocalDate date, String type) {
        if (type.equals("Study")) {
            tasks.add(new StudyTask(title, date));
        } else {
            tasks.add(new Task(title, date));
        }
        saveTasks();
    }

    // instead of printing tasks diretso kay gi return ra ang list
    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void updateTask() {
        saveTasks();
    }

    public void deleteTask(Task task) {
        if (task != null) {
            tasks.remove(task);
            saveTasks();
        }
    }

    private void saveTasks() {
        try (ObjectOutputStream out =
                     new ObjectOutputStream(new FileOutputStream(FILE_NAME))) {
            out.writeObject(tasks);
        } catch (IOException e) {
            System.out.println("Error saving tasks: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void loadTasks() {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            tasks = new ArrayList<>();
            return;
        }

        try (ObjectInputStream in =
                     new ObjectInputStream(new FileInputStream(file))) {
            tasks = (ArrayList<Task>) in.readObject();
        } catch (Exception e) {
            System.out.println("Error loading tasks: " + e.getMessage());
            tasks = new ArrayList<>();
        }
    }
}
