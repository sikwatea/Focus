package com.focus.main;

// general imports
import com.focus.studyhelper.FocusSession;
import com.focus.studyhelper.StudyMethod;
import com.focus.studyhelper.StudyMethodManager;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

// timer imports
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;
import com.focus.studyhelper.FocusSession.SessionStatus;

public class FocusView extends VBox {
    
    private StudyMethodManager methodManager;
    private TextField labelField;
    private ComboBox<StudyMethod> methodDropdown;
    private Spinner<Integer> durationSpinner;
    private Spinner<Integer> breaksSpinner;
    private Spinner<Integer> breakDurationSpinner;
    private Spinner<Integer> breakdownSpinner; // <--- NEW SPINNER
    private VBox breakdownBox; // Container to hide/show it
    private Timeline timeline;
    
    public FocusView() {
        this.methodManager = new StudyMethodManager(); 
        
        this.setPadding(new Insets(30));
        this.setSpacing(20);
        this.setAlignment(Pos.CENTER);
        this.setStyle("-fx-background-color: white;");

        showSetupForm();
    }

    private void showSetupForm() {
        this.getChildren().clear();
        
        Label header = new Label("Start a Focus Session");
        header.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #49654E; -fx-font-family: 'Montserrat';");

        VBox labelBox = createInputBox("Session Goal (Label):");
        labelField = new TextField();
        labelField.setPromptText("e.g., Math Review");
        labelBox.getChildren().add(labelField);

        VBox methodBox = createInputBox("Study Method:");
        methodDropdown = new ComboBox<>();
        methodDropdown.getItems().addAll(getAllMethods()); 
        methodDropdown.setPromptText("Select a technique...");
        methodDropdown.setStyle("-fx-background-color: #e8f5e9; -fx-text-fill: #49654E; -fx-font-size: 16px; -fx-background-radius: 8px;");
        methodDropdown.setPrefWidth(300);
        
        methodBox.getChildren().add(methodDropdown);

        HBox timeRow = new HBox(20);
        timeRow.setAlignment(Pos.CENTER);
        
        // set time
        VBox durationBox = createInputBox("Total Time (mins):");
        durationSpinner = new Spinner<>(1, 1440, 60); // Min 1, Max 1440, Default 60 | FOR DEBUG (actual time should be 5, 1440, 60)
        durationSpinner.setEditable(true);
        durationBox.getChildren().add(durationSpinner);

        // set breaks
        VBox breakCountBox = createInputBox("Breaks:");
        breaksSpinner = new Spinner<>(0, 24, 1); // Min 0, Max 48, Default 1 | FOR DEBUG (actual time should be 0, 48, 1)
        breaksSpinner.setEditable(true);
        breakCountBox.getChildren().add(breaksSpinner);

        timeRow.getChildren().addAll(durationBox, breakCountBox);

        HBox bottomRow = new HBox(20);
        bottomRow.setAlignment(Pos.CENTER);
        
        // break duration
        VBox breakDurBox = createInputBox("Break Length (mins):");
        breakDurationSpinner = new Spinner<>(0, 30, 5); // Min 1, Max 30, Default 5 | FOR DEBUG (actual time should be 0, 60, 1)
        breakDurationSpinner.setEditable(true);
        breakDurBox.getChildren().add(breakDurationSpinner);
        
        breakdownBox = createInputBox("Breakdown Length (mins):");
        breakdownSpinner = new Spinner<>(1, 60, 5); // Min 1, Max 60, Default 5 | FOR DEBUG (actual time should be 1, 60, 5)
        breakdownBox.getChildren().add(breakdownSpinner);
        
        breakdownBox.setVisible(false);
        breakdownBox.setManaged(false);
        
        bottomRow.getChildren().addAll(breakDurBox, breakdownBox);

        methodDropdown.setOnAction(e -> {
            boolean isFeynman = methodDropdown.getValue() != null && 
                                methodDropdown.getValue().getName().contains("Feynman");
            breakdownBox.setVisible(isFeynman);
            breakdownBox.setManaged(isFeynman);
        });
        
        Button startBtn = new Button("Start Focus Session");
        startBtn.setStyle("-fx-background-color: #49654E; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-background-radius: 8px;");
        startBtn.setPrefWidth(200);
        startBtn.setPrefHeight(50);
        
        startBtn.setOnAction(e -> handleStartSession());

        this.getChildren().addAll(header, labelBox, methodBox, timeRow, bottomRow, startBtn);
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

        // TIMER LOGIC 
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            // update backend
        	boolean isRunning = session.tick();

        	//update frontend
            timerLabel.setText(formatTime(session.getRemainingTimeSeconds()));
            phaseLabel.setText(session.getCurrentPhase() != null ? session.getCurrentPhase().toString() : "COMPLETED");

            // check if done
            if (!isRunning) {
                timeline.stop();
                timerLabel.setText("DONE!");
                timerLabel.setStyle("-fx-font-size: 64px; -fx-text-fill: #49654E; -fx-font-family: 'Montserrat';");
                pauseBtn.setDisable(true);
            }
        }));
        
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        
        pauseBtn.setOnAction(e -> {
            if (session.getStatus() == SessionStatus.RUNNING) {
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
            timeline.stop();
            session.completeSession();
            showSetupForm();
        });

        this.getChildren().addAll(phaseLabel, timerLabel, goalLabel, controls);
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
            new com.focus.studyhelper.PomodoroMethod("Pomodoro", "", null),
            new com.focus.studyhelper.FeynmanMethod("Feynman", "", null),
            new com.focus.studyhelper.ActiveRecallMethod("Active Recall", "", null)
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