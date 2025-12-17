package com.focus.tasks;

import java.util.Scanner;
import java.time.LocalDate;

public class TaskManager {
    public void start() {
    	Scanner sc = new Scanner(System.in);
        TaskList list = new TaskList();
        int choice = 0;
        
        while (choice != 5) {
            System.out.println("\n=== TO DO LIST ===");
            System.out.println("1. Add Task");
            System.out.println("2. View Tasks");
            System.out.println("3. Complete Task");
            System.out.println("4. Delete Task");
            System.out.println("5. Exit");
            System.out.print("Choice: ");

            try {
                choice = Integer.parseInt(sc.nextLine());

                switch (choice) {
                    case 1:
                        System.out.print("Title: ");
                        String title = sc.nextLine();

                        System.out.print("Due date (YYYY-MM-DD): ");
                        LocalDate date = LocalDate.parse(sc.nextLine());

                        list.addTask(title, date);
                        break;

                    case 2:
                        list.showTasks();
                        break;

                    case 3:
                        list.showTasks();
                        System.out.print("Task #: ");
                        list.completeTask(Integer.parseInt(sc.nextLine()) - 1);
                        break;

                    case 4:
                        list.showTasks();
                        System.out.print("Task #: ");
                        list.deleteTask(Integer.parseInt(sc.nextLine()) - 1);
                        break;

                    case 5:
                        System.out.println("Tasks saved. Goodbye!");
                        break;

                    default:
                        System.out.println("Invalid option.");
                }

            } catch (Exception e) {
                System.out.println("Invalid input. Try again.");
            }
        }
        sc.close();
    }
}
