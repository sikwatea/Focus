package com.focus.main;

import com.focus.tasks.Task;
import com.focus.tasks.TaskList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class TaskView extends VBox {
    
    private TaskList taskManager;
    private TableView<Task> table;
    private ObservableList<Task> observableTasks;
    
    // Inputs
    private TextField titleField;
    private DatePicker datePicker;
    private ComboBox<String> typeBox; // dropdown for types
    
    public TaskView() {
        this.taskManager = new TaskList();
        this.setPadding(new Insets(30));
        this.setSpacing(20);
        this.setStyle("-fx-background-color: white;");
        this.setAlignment(Pos.TOP_CENTER);

        createHeader();
        createInputSection();
        createTaskTable();
        refreshTable();
    }

    private void createHeader() {
        Label title = new Label("Task Manager");
        title.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #49654E; -fx-font-family: 'Montserrat';");
        this.getChildren().add(title);
    }

    private void createInputSection() {
    	HBox inputBox = new HBox(15);
        inputBox.setAlignment(Pos.CENTER);
        inputBox.setPadding(new Insets(10));
        inputBox.setStyle("-fx-background-color: #f4f4f4; -fx-background-radius: 10;");

        titleField = new TextField();
        titleField.setPromptText("Enter task description...");
        titleField.setPrefWidth(250);
        titleField.setStyle("-fx-font-size: 14px;");

        datePicker = new DatePicker(LocalDate.now());
        datePicker.setPrefWidth(120);
        datePicker.setStyle("-fx-font-size: 14px;");

        typeBox = new ComboBox<>();
        typeBox.getItems().addAll("General", "Study");
        typeBox.setValue("General"); 
        typeBox.setPrefWidth(100);
        typeBox.setStyle("-fx-font-size: 14px;");

        Button addBtn = new Button("Add");
        addBtn.setStyle("-fx-background-color: #49654E; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;");
        addBtn.setOnAction(e -> handleAddTask());

        inputBox.getChildren().addAll(titleField, typeBox, datePicker, addBtn);
        this.getChildren().add(inputBox);
    }

    @SuppressWarnings("unchecked")
    private void createTaskTable() {
        table = new TableView<>();
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        table.setPrefHeight(400);

        table.setRowFactory(tv -> {
            TableRow<Task> row = new TableRow<Task>() {
                @Override
                protected void updateItem(Task item, boolean empty) {
                    super.updateItem(item, empty);
                    updateRowStyle(this);
                }
            };

            // listener to for visual change
            row.selectedProperty().addListener((obs, wasSelected, isSelected) -> updateRowStyle(row));

            return row;
        });
        
        // checkbox
        TableColumn<Task, Boolean> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(new PropertyValueFactory<>("completed"));
        colStatus.setPrefWidth(60);
        colStatus.setStyle("-fx-alignment: CENTER;");
        colStatus.setCellFactory(column -> new TableCell<Task, Boolean>() {
            
        	private final CheckBox checkBox = new CheckBox();

            {
                // checkbox listener
                checkBox.setOnAction(event -> {
                    Task task = getTableView().getItems().get(getIndex());
                    task.setCompleted(checkBox.isSelected()); 
                    taskManager.updateTask(); 
                    
                    table.refresh(); 
                });
            }
        	
            @Override
            protected void updateItem(Boolean item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    checkBox.setSelected(item);
                    setGraphic(checkBox);
                }
            }
        });

        // title col
        TableColumn<Task, String> colTitle = new TableColumn<>("Task");
        colTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        colTitle.setStyle("-fx-alignment: CENTER-LEFT; -fx-padding: 0 0 0 10;");

        // due date col
        TableColumn<Task, LocalDate> colDate = new TableColumn<>("Due Date");
        colDate.setCellValueFactory(new PropertyValueFactory<>("dueDate"));
        colDate.setPrefWidth(120);
        colDate.setStyle("-fx-alignment: CENTER;");
        colDate.setCellFactory(column -> new TableCell<Task, LocalDate>() {
        	private final DateTimeFormatter format = DateTimeFormatter.ofPattern("MMM dd, yyyy");
            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(format.format(item));
                    // highlight overdues
                    Task task = getTableView().getItems().get(getIndex());
                    if (item.isBefore(LocalDate.now()) && !task.isCompleted()) {
                        setTextFill(javafx.scene.paint.Color.RED);
                    } else {
                        setTextFill(javafx.scene.paint.Color.BLACK);
                    }
                }
            }
        });

        // type col
        TableColumn<Task, String> colType = new TableColumn<>("Type");
        colType.setCellValueFactory(new PropertyValueFactory<>("type"));
        colType.setPrefWidth(80);
        colType.setStyle("-fx-alignment: CENTER;");

        table.getColumns().addAll(colStatus, colTitle, colType, colDate);

        HBox controls = new HBox(15);
        controls.setAlignment(Pos.CENTER_RIGHT);

        Button deleteBtn = new Button("Delete Task");
        deleteBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-weight: bold;");
        deleteBtn.setOnAction(e -> handleDeleteTask());

        controls.getChildren().addAll(deleteBtn);

        table.setStyle(
                "-fx-selection-bar: #49654E; " +           
                "-fx-selection-bar-non-focused: #49654E;"  
            );
        
        this.getChildren().addAll(table, controls);
    }

    private void handleAddTask() {
        String title = titleField.getText().trim();
        LocalDate date = datePicker.getValue();
        String type = typeBox.getValue();

        if (title.isEmpty() || date == null) {
            showAlert("Please enter a task title and date.");
            return;
        }

        taskManager.addTask(title, date, type);
        refreshTable();
        titleField.clear();
    }

    private void handleDeleteTask() {
        Task selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Please select a task to delete.");
            return;
        }
        taskManager.deleteTask(selected);
        refreshTable();
    }

    private void refreshTable() {
    	// convert ArrayList from TaskList to ObservableList for table
        observableTasks = FXCollections.observableArrayList(taskManager.getTasks());
        table.setItems(observableTasks);
        table.refresh();
    }

    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
    
    private void updateRowStyle(TableRow<Task> row) {
        if (row.getItem() == null || row.isEmpty()) {
            row.setStyle("");
        } else if (row.isSelected()) {
            row.setStyle(""); 
        } else if (row.getItem().isCompleted()) {
            row.setStyle("-fx-background-color: #C8E6C9;");
        } else {
            row.setStyle("");
        }
    }
}