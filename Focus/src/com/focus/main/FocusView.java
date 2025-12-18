package com.focus.main;

//general imports
import com.focus.studyhelper.FocusSession;
import com.focus.studyhelper.StudyMethod;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

//timer import
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import com.focus.studyhelper.FocusSession.SessionStatus;

public class FocusView extends VBox {
    
	private List<FocusSession> sessionHistory = new ArrayList<>();
    private final String HISTORY_FILE = "focus_history.dat";
    
    private TextField labelField;
    private ComboBox<StudyMethod> methodDropdown;
    private Spinner<Integer> durationSpinner;
    private Spinner<Integer> breaksSpinner;
    private Spinner<Integer> breakDurationSpinner;
    private Spinner<Integer> breakdownSpinner;
    private VBox breakdownBox; 
    
    private Timeline timeline;
    
    public FocusView() {
        loadHistory();
        
        this.setPadding(new Insets(30));
        this.setSpacing(20);
        this.setAlignment(Pos.CENTER);
        this.setStyle("-fx-background-color: white;");

        showSetupForm();
    }

    private void showSetupForm() {
    	try {
	        this.getChildren().clear();
	        
	        Label header = new Label("Start a Focus Session");
	        header.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #49654E; -fx-font-family: 'Montserrat';");
	
	        VBox labelBox = createInputBox("Session Goal (Label):");
	        labelField = new TextField();
	        labelField.setPromptText("e.g., Math Review");
	        labelBox.getChildren().add(labelField);
	
	        VBox methodBox = createInputBox("Study Method:");
	        HBox methodRow = new HBox(10);
	        
	        methodDropdown = new ComboBox<>();
	        methodDropdown.getItems().addAll(getAllMethods()); 
	        methodDropdown.setPromptText("Select a technique...");
	        methodDropdown.setStyle("-fx-background-color: #e8f5e9; -fx-text-fill: #49654E; -fx-font-size: 16px; -fx-background-radius: 8px;");
	        methodDropdown.setPrefWidth(260);
	        
	        Button infoBtn = new Button("?");
	        infoBtn.setStyle("-fx-background-color: #8BA889; -fx-text-fill: white; -fx-font-weight: bold; -fx-background-radius: 20px;");
	        infoBtn.setTooltip(new Tooltip("View Method Details"));
	        infoBtn.setOnAction(e -> showMethodInfo());
	        
	        methodRow.getChildren().addAll(methodDropdown, infoBtn);
	        methodBox.getChildren().add(methodRow);
	
	        HBox timeRow = new HBox(20);
	        timeRow.setAlignment(Pos.CENTER);
	        
	        // set time
	        VBox durationBox = createInputBox("Total Time (mins):");
	        durationSpinner = new Spinner<>(5, 1440, 60); // Min 1, Max 1440, Default 60 | FOR DEBUG (actual time should be 5, 1440, 60)
	        durationSpinner.setEditable(true);
	        durationBox.getChildren().add(durationSpinner);
	
	        // set breaks
	        VBox breakCountBox = createInputBox("Breaks:");
	        breaksSpinner = new Spinner<>(0, 48, 1); // Min 0, Max 48, Default 1 | FOR DEBUG (actual time should be 0, 48, 1)
	        breaksSpinner.setEditable(true);
	        breakCountBox.getChildren().add(breaksSpinner);
	
	        timeRow.getChildren().addAll(durationBox, breakCountBox);
	
	        HBox bottomRow = new HBox(20);
	        bottomRow.setAlignment(Pos.CENTER);
	        
	        // break duration
	        VBox breakDurBox = createInputBox("Break Length (mins):");
	        breakDurationSpinner = new Spinner<>(0, 60, 1); // Min 1, Max 30, Default 5 | FOR DEBUG (actual time should be 0, 60, 1)
	        breakDurationSpinner.setEditable(true);
	        breakDurBox.getChildren().add(breakDurationSpinner);
	        
	        breakdownBox = createInputBox("Breakdown Length (mins):");
	        breakdownSpinner = new Spinner<>(1, 60, 5); // Min 1, Max 60, Default 5 | FOR DEBUG (actual time should be 1, 60, 5)
	        breakdownSpinner.setEditable(true);
	        breakdownBox.getChildren().add(breakdownSpinner);
	        
	        breakdownBox.setVisible(false);
	        breakdownBox.setManaged(false);
	        
	        bottomRow.getChildren().addAll(breakDurBox, breakdownBox);
	
	        // listener for feynman
	        methodDropdown.setOnAction(e -> {
	            boolean isFeynman = methodDropdown.getValue() != null && 
	                                methodDropdown.getValue().getName().contains("Feynman");
	            breakdownBox.setVisible(isFeynman);
	            breakdownBox.setManaged(isFeynman);
	        });
	        
	        HBox actionRow = new HBox(20);
	        actionRow.setAlignment(Pos.CENTER);
	        
	        Button startBtn = new Button("Start Focus Session");
	        startBtn.setStyle("-fx-background-color: #49654E; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-background-radius: 8px;");
	        startBtn.setPrefWidth(200);
	        startBtn.setPrefHeight(45);
	        startBtn.setOnAction(e -> handleStartSession());
	
	        Button historyBtn = new Button("History");
	        historyBtn.setStyle("-fx-background-color: #ecf0f1; -fx-text-fill: #49654E; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 8px;");
	        historyBtn.setPrefWidth(100);
	        historyBtn.setPrefHeight(45);
	        historyBtn.setOnAction(e -> showHistoryView());
	        
	        actionRow.getChildren().addAll(startBtn, historyBtn);
	        
	        this.getChildren().addAll(header, labelBox, methodBox, timeRow, bottomRow, actionRow);
    	} catch (Exception e) {
            e.printStackTrace(); 
            showAlert("Error building UI: " + e.getMessage());
    	}
    }

	private void showHistoryView() {
        this.getChildren().clear();
        
        Label header = new Label("Session History");
        header.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #49654E;");
        
        TableView<FocusSession> table = new TableView<>();
        
        TableColumn<FocusSession, String> colDate = new TableColumn<>("Date");
        colDate.setCellValueFactory(new PropertyValueFactory<>("startDate"));
        colDate.setPrefWidth(120);
        colDate.setStyle("-fx-alignment: CENTER;"); 
        
        TableColumn<FocusSession, String> colLabel = new TableColumn<>("Label");
        colLabel.setCellValueFactory(new PropertyValueFactory<>("label")); 
        colLabel.setPrefWidth(150);
        colLabel.setStyle("-fx-padding: 0 0 0 10;"); 
        
        TableColumn<FocusSession, String> colMethod = new TableColumn<>("Method");
        colMethod.setCellValueFactory(new PropertyValueFactory<>("methodName")); 
        colMethod.setPrefWidth(100);
        colMethod.setStyle("-fx-alignment: CENTER;");

        TableColumn<FocusSession, Integer> colTime = new TableColumn<>("Duration");
        colTime.setCellValueFactory(new PropertyValueFactory<>("totalDurationMinutes"));
        colTime.setPrefWidth(90);
        colTime.setStyle("-fx-alignment: CENTER;");
        colTime.setCellFactory(column -> new TableCell<FocusSession, Integer>() {
            @Override
            protected void updateItem(Integer item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else {
                    setText(item + " mins"); 
                }
            }
        });
        
        TableColumn<FocusSession, Integer> colDistractions = new TableColumn<>("Distractions");
        colDistractions.setCellValueFactory(new PropertyValueFactory<>("distractionCount")); 
        colDistractions.setPrefWidth(100);
        colDistractions.setStyle("-fx-alignment: CENTER;");

        TableColumn<FocusSession, Object> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
        colStatus.setPrefWidth(120);
        colStatus.setStyle("-fx-alignment: CENTER; -fx-font-weight: bold;");
        colStatus.setCellFactory(column -> new TableCell<FocusSession, Object>() {
            @Override
            protected void updateItem(Object item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item.toString());
                    
                    if (item.toString().equals("COMPLETED")) {
                        setTextFill(javafx.scene.paint.Color.web("#49654E")); 
                    } else if (item.toString().contains("UNFINISHED") || item.toString().contains("ABANDONED")) {
                        setTextFill(javafx.scene.paint.Color.web("#e74c3c")); 
                    } else {
                        setTextFill(javafx.scene.paint.Color.BLACK);
                    }
                }
            }
        });

        table.getColumns().addAll(colDate, colLabel, colMethod, colTime, colDistractions, colStatus);

        table.setFixedCellSize(35);
        table.setPrefHeight(400);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY); 

        table.setStyle(
            "-fx-base: white; " +
            "-fx-control-inner-background: white; " +
            "-fx-table-cell-border-color: transparent; " +
            "-fx-table-header-border-color: transparent; " +
            "-fx-padding: 5;"
        );

        // Add Data
        table.getItems().addAll(sessionHistory);
        table.setPlaceholder(new Label("No sessions completed yet."));

        Button backBtn = new Button("Back");
        backBtn.setStyle("-fx-background-color: #8BA889; -fx-text-fill: white; -fx-font-weight: bold;");
        backBtn.setOnAction(e -> showSetupForm());
        
        this.getChildren().addAll(header, table, backBtn);
    }

	private void showMethodInfo() {
        StudyMethod method = methodDropdown.getValue();
        if (method == null) {
            showAlert("Please select a method first.");
            return;
        }
        
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Method Details");
        alert.setHeaderText(method.getName());
        
        StringBuilder content = new StringBuilder(method.getDescription() + "\n\nSteps:\n");
        for(String step : method.getSteps()) {
            content.append(step).append("\n");
        }
        
        alert.setContentText(content.toString());
        alert.showAndWait();
    }
    
    // session logic for GUI
    private void handleStartSession() {
        if (methodDropdown.getValue() == null) {
            showAlert("Please select a Study Method.");
            return;
        }
        if (labelField.getText().isEmpty()) {
            showAlert("Please enter a Session Goal.");
            return;
        }

        int breakdownVal = breakdownSpinner.getValue();
        
        FocusSession newSession = new FocusSession(
                labelField.getText(),
                methodDropdown.getValue(),
                durationSpinner.getValue(),
                breaksSpinner.getValue(),
                breakDurationSpinner.getValue(),
                breakdownVal
            );

        switchToTimerView(newSession);
    }

    private void switchToTimerView(FocusSession session) {
        this.getChildren().clear(); 
        
        // Phase label
        Label phaseLabel = new Label(session.getCurrentPhase().toString());
        phaseLabel.setStyle("-fx-font-size: 24px; -fx-text-fill: #8BA889; -fx-font-weight: bold;");

        // Timer label
        Label timerLabel = new Label(formatTime(session.getRemainingTimeSeconds()));
        timerLabel.setStyle("-fx-font-size: 96px; -fx-text-fill: #49654E; -fx-font-weight: bold; -fx-font-family: 'Montserrat';");

        // Goal label
        Label goalLabel = new Label("Goal: " + labelField.getText()); 
        goalLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: #555;");

        Button pauseBtn = new Button("Pause");
        styleControlButton(pauseBtn);
        
        Button stopBtn = new Button("Stop Session");
        styleControlButton(stopBtn);
        stopBtn.setStyle("-fx-background-color: #e74c3c; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-background-radius: 8px;");

        HBox controls = new HBox(20, pauseBtn, stopBtn);
        controls.setAlignment(Pos.CENTER);

        // timer logic
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            boolean isRunning = session.tick();

            // update labels
            timerLabel.setText(formatTime(session.getRemainingTimeSeconds()));
            phaseLabel.setText(session.getCurrentPhase() != null ? session.getCurrentPhase().toString() : "COMPLETED");

            if (session.getCurrentPhase() == FocusSession.SessionPhase.BREAK) {
                if (!pauseBtn.isDisabled()) {
                    pauseBtn.setDisable(true);
                    pauseBtn.setText("Break Time");
                }
            } else {
                if (pauseBtn.isDisabled() && session.getStatus() == SessionStatus.RUNNING) {
                    pauseBtn.setDisable(false);
                    pauseBtn.setText("Pause");
                }
            }

            // Check if done
            if (!isRunning) {
                timeline.stop();
                timerLabel.setText("DONE!");
                timerLabel.setStyle("-fx-font-size: 64px; -fx-text-fill: #49654E; -fx-font-family: 'Montserrat';");
                pauseBtn.setDisable(true);
                
                // Auto-save to history
                sessionHistory.add(session);
                saveHistory();
            }
        }));
        
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        
        // pause logic
        pauseBtn.setOnAction(e -> {
            if (session.getStatus() == SessionStatus.RUNNING) {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Session Interrupted");
                dialog.setHeaderText("Focus Interrupted");
                dialog.setContentText("Reason for pausing:");

                Optional<String> result = dialog.showAndWait();
                String reason = result.orElse("User Paused"); 

                session.logDistraction(reason);

                session.pause();
                timeline.pause();
                pauseBtn.setText("Resume");
                
            } else {
                session.resume();
                timeline.play();
                pauseBtn.setText("Pause");
            }
        });

        stopBtn.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("End Session");
            alert.setHeaderText("Stop this session early?");
            alert.setContentText("This will mark the session as UNFINISHED.");
            
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                timeline.stop();
                
                if (session.getStatus() != SessionStatus.COMPLETED) {
                    session.abandonSession();
                    sessionHistory.add(session);
                    saveHistory();
                }
                
                showSetupForm();
            }
        });

        this.getChildren().addAll(phaseLabel, timerLabel, goalLabel, controls);
    }

    private void saveHistory() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(HISTORY_FILE))) {
            out.writeObject(sessionHistory);
            System.out.println("History saved.");
        } catch (IOException e) {
            System.out.println("Error saving history: " + e.getMessage());
        }
    }
    
    @SuppressWarnings("unchecked")
    private void loadHistory() {
        File file = new File(HISTORY_FILE);
        if (!file.exists()) return; 
        
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            sessionHistory = (List<FocusSession>) in.readObject();
            System.out.println("History loaded: " + sessionHistory.size() + " sessions.");
        } catch (InvalidClassException | ClassNotFoundException e) {
            System.out.println("Corrupted/Old history file detected. Deleting...");
            
            sessionHistory = new ArrayList<>(); 
        } catch (Exception e) {
            System.out.println("Error loading history: " + e.getMessage());
            sessionHistory = new ArrayList<>(); 
        }
    }
    
    // format for timer
    private String formatTime(long totalSeconds) {
        long minutes = totalSeconds / 60;
        long seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }
    
    private void styleControlButton(Button btn) {
        btn.setPrefWidth(120);
        btn.setPrefHeight(40);
        btn.setStyle("-fx-background-color: #49654E; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-background-radius: 8px;");
    }
    
    private java.util.List<StudyMethod> getAllMethods() {
        return java.util.Arrays.asList(
            new com.focus.studyhelper.PomodoroMethod(
                "Pomodoro", 
                "A study method using a timer to break work into intervals, "
                + "the study session is traditionally set at 25 mins, separated by short breaks.", 
                Arrays.asList(
                    "1. Choose a task to be accomplished.", 
                    "2. Set the timer.", 
                    "3. Set the number and length of breaks.", 
                    "4. Start the timer and take short breaks in between."
                )
            ),
            new com.focus.studyhelper.FeynmanMethod(
                "Feynman", 
                "A study method that involves \"breaking down\" a concept in simple terms "
                + "to identify gaps in your understanding.", 
                Arrays.asList(
                    "1. Choose a concept you want to learn.",
                    "2. Set the timer.",
                    "3. Set the number and length of breaks (Optional).", 
                    "4. Set the breakdown timer.",
                    "5. Explain it as if teaching a child and identify your gaps.",
                    "6. Review again."
                )
            ),
            new com.focus.studyhelper.ActiveRecallMethod(
                "Active Recall", 
                "A study method which claims the need to actively stimulate memory during the "
                + "learning process (Pairing this method with flashcards work best).", 
                Arrays.asList(
                    "1. Set the timer.", 
                    "3. Set the number and length of breaks.",
                    "2. Review the material briefly.", 
                    "3. Recite or write down everything you remember.",
                    "4. Check your accuracy.",
                    "5. Repeat for the parts you missed."
                )
            )
        );
    }
    
    private VBox createInputBox(String title) {
        VBox box = new VBox(5);
        Label lbl = new Label(title);
        lbl.setStyle("-fx-text-fill: #8BA889; -fx-font-weight: bold;");
        box.getChildren().add(lbl);
        box.setAlignment(Pos.CENTER_LEFT);
        box.setMaxWidth(300);
        return box;
    }
    
    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}