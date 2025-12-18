package com.focus.main;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Pane;
import javafx.animation.ScaleTransition;
import javafx.util.Duration;

public class Main extends Application {
    private BorderPane mainLayout;
    private FocusView focusView;
    private FlashcardView flashcardView;
    private TaskView taskView;
    @Override
    public void start(Stage primaryStage) {
    	
        mainLayout = new BorderPane();
        mainLayout.setLeft(createSideMenu());
        mainLayout.setCenter(createHomeView());
        Scene scene = new Scene(mainLayout, 900, 600);
        
        Image icon = new Image("focus.png");
		primaryStage.getIcons().add(icon);
        
        primaryStage.setTitle("Focus App - Form habits, Find your flow");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    private VBox createSideMenu() {
        VBox sidebar = new VBox(15); 
        sidebar.setPadding(new Insets(20));
        sidebar.setStyle("-fx-background-color: #49654E;");
        sidebar.setPrefWidth(200); 

        Label appTitle = new Label("F.OCUS");
        appTitle.setStyle("-fx-text-fill: white; -fx-font-size: 24px; -fx-font-weight: bold; -fx-font-family: 'Montserrat';");

        Button btnHome = createNavButton("Home");
        Button btnTasks = createNavButton("Task Manager");
        Button btnFlashcards = createNavButton("Flashcards");
        Button btnFocus = createNavButton("Focus Mode");

        btnHome.setOnAction(e -> mainLayout.setCenter(createHomeView()));

        // placeholder events
        btnTasks.setOnAction(e -> {
        	if (taskView == null) {
        		taskView = new TaskView();
            }
            mainLayout.setCenter(taskView); 
        });
        
        btnFlashcards.setOnAction(e -> {
        	if (flashcardView == null) {
        		flashcardView = new FlashcardView();
            }
            mainLayout.setCenter(flashcardView);
        });
        
        btnFocus.setOnAction(e -> {
            if (focusView == null) {
                focusView = new FocusView();
            }
            mainLayout.setCenter(focusView);
        });

        sidebar.getChildren().addAll(appTitle, btnHome, btnTasks, btnFlashcards, btnFocus);

        return sidebar;
    }

    private Button createNavButton(String text) {
        Button btn = new Button(text);
        btn.setPrefWidth(160);
        btn.setPrefHeight(40);
        btn.setStyle("-fx-background-color: white; -fx-background-radius: 7px; -fx-text-fill: #49654E; -fx-font-size: 14px;");

        btn.setOnMouseEntered(e -> {
            btn.setStyle("-fx-background-color: #e8f5e9; -fx-background-radius: 7px; -fx-text-fill: #49654E; -fx-font-size: 14px;");
            ScaleTransition st = new ScaleTransition(Duration.millis(200), btn);
            st.setToX(1.1);
            st.setToY(1.1); 
            st.play();
        });

        btn.setOnMouseExited(e -> {
            btn.setStyle("-fx-background-color: white; -fx-background-radius: 7px; -fx-text-fill: #49654E; -fx-font-size: 14px;");
            ScaleTransition st = new ScaleTransition(Duration.millis(200), btn);
            st.setToX(1.0);
            st.setToY(1.0);
            st.play();
        });

        return btn;
    }
    
    private Pane createHomeView() {
        VBox homeView = new VBox(20); 
        homeView.setAlignment(Pos.CENTER); 
        homeView.setStyle("-fx-background-color: white;"); 

        Label welcomeLabel = new Label("Welcome to F.ocus");
        welcomeLabel.setStyle("-fx-font-size: 32px; -fx-text-fill: #49654E; -fx-font-weight: bold; -fx-font-family: 'Montserrat';");

        Label subLabel = new Label("Select a Tool from the sideabr to start");
        subLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #8BA889; -fx-font-family: 'Montserrat';");

        homeView.getChildren().addAll(welcomeLabel, subLabel);
        return homeView;
    }
}
