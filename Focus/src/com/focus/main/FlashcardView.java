package com.focus.main;

import com.focus.flashcards.Flashcard;
import com.focus.flashcards.FlashcardSet;
import com.focus.flashcards.FlashcardService;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.scene.text.TextAlignment;

import java.util.Optional;

public class FlashcardView extends VBox {
    
    private FlashcardService service; // set handler rani
    
    // all ui containers
    private VBox deckSelectionView;
    private VBox deckEditorView;
    private VBox reviewView;
    
    private ListView<FlashcardSet> deckListView;
    private TableView<Flashcard> cardTable;
    
    private FlashcardSet currentSet;
    private int currentReviewIndex = 0;
    private boolean isShowingAnswer = false;

    public FlashcardView() {
        this.service = new FlashcardService();
        this.setPadding(new Insets(20));
        this.setStyle("-fx-background-color: white;");
        this.setAlignment(Pos.CENTER);

        // opens default view deck selection
        createDeckSelectionView();
    
        showDeckSelection();
    }

    // DECK SELECTION VIEW
    private void createDeckSelectionView() {
        deckSelectionView = new VBox(20);
        deckSelectionView.setAlignment(Pos.CENTER);
        
        Label header = new Label("Flashcard Decks");
        header.setStyle("-fx-font-size: 28px; -fx-font-weight: bold; -fx-text-fill: #49654E; -fx-font-family: 'Montserrat';");
        
        deckListView = new ListView<>();
        deckListView.getItems().addAll(service.getSets());
        deckListView.setPrefHeight(300);
        deckListView.setStyle("-fx-font-size: 16px; -fx-border-color: #49654E; -fx-border-radius: 5;");
        
        HBox controls = new HBox(15);
        controls.setAlignment(Pos.CENTER);
        
        Button openBtn = createButton("Open Deck", "#49654E");
        openBtn.setOnAction(e -> {
            FlashcardSet selected = deckListView.getSelectionModel().getSelectedItem();
            if (selected != null) showDeckEditor(selected);
        });
        
        Button createBtn = createButton("New Deck", "#8BA889");
        createBtn.setOnAction(e -> handleCreateDeck());
        
        Button deleteBtn = createButton("Delete Deck", "#e74c3c");
        deleteBtn.setOnAction(e -> handleDeleteDeck());
        
        controls.getChildren().addAll(openBtn, createBtn, deleteBtn);
        deckSelectionView.getChildren().addAll(header, deckListView, controls);
    }

    // DECK EDITOR VIEW
    private void showDeckEditor(FlashcardSet set) {
        this.currentSet = set;
        this.getChildren().clear();
        
        deckEditorView = new VBox(20);
        deckEditorView.setAlignment(Pos.TOP_CENTER);
        deckEditorView.setPadding(new Insets(10));
        
        HBox headerBox = new HBox(20);
        headerBox.setAlignment(Pos.CENTER_LEFT);
        Button backBtn = createButton("← Back", "#7f8c8d");
        backBtn.setOnAction(e -> showDeckSelection());
        
        Label title = new Label("Editing: " + set.getName());
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #49654E;");
        
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        
        Button reviewBtn = createButton("Start Review ►", "#49654E");
        reviewBtn.setOnAction(e -> startReview());
        
        headerBox.getChildren().addAll(backBtn, title, spacer, reviewBtn);
        
        // inputs
        HBox inputBox = new HBox(10);
        inputBox.setAlignment(Pos.CENTER);
        TextField qField = new TextField(); qField.setPromptText("Question"); qField.setPrefWidth(200);
        TextField aField = new TextField(); aField.setPromptText("Answer"); aField.setPrefWidth(200);
        Button addBtn = createButton("Add Card", "#8BA889");
        
        addBtn.setOnAction(e -> {
            if (!qField.getText().isEmpty() && !aField.getText().isEmpty()) {
                set.addFlashcard(new Flashcard(qField.getText(), aField.getText()));
                service.saveChanges();
                cardTable.getItems().setAll(set.getCards());
                qField.clear();
                aField.clear();
            }
        });
        
        inputBox.getChildren().addAll(qField, aField, addBtn);
        
        // table containing q and a
        cardTable = new TableView<>();
        cardTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        
        TableColumn<Flashcard, String> colQ = new TableColumn<>("Question");
        colQ.setCellValueFactory(new PropertyValueFactory<>("question"));
        
        TableColumn<Flashcard, String> colA = new TableColumn<>("Answer");
        colA.setCellValueFactory(new PropertyValueFactory<>("answer"));
        
        cardTable.getColumns().addAll(colQ, colA);
        cardTable.getItems().setAll(set.getCards());
        
        // delete cards (questions)
        Button delCardBtn = createButton("Delete Selected Card", "#e74c3c");
        delCardBtn.setOnAction(e -> {
            Flashcard selected = cardTable.getSelectionModel().getSelectedItem();
            if (selected != null) {
                set.removeFlashcard(selected);
                service.saveChanges();
                cardTable.getItems().setAll(set.getCards());
            }
        });

        deckEditorView.getChildren().addAll(headerBox, inputBox, cardTable, delCardBtn);
        this.getChildren().add(deckEditorView);
    }

    // REVIEW MODE VIEW
    private void startReview() {
        if (currentSet.getCards().isEmpty()) {
            showAlert("This deck is empty! Add some cards first.");
            return;
        }
        
        this.currentReviewIndex = 0;
        this.isShowingAnswer = false;
        showReviewCard();
    }
    
    private void showReviewCard() {
        this.getChildren().clear();
        reviewView = new VBox(30);
        reviewView.setAlignment(Pos.CENTER);
        
        Label progressLabel = new Label("Card " + (currentReviewIndex + 1) + " of " + currentSet.size());
        progressLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: #7f8c8d;");
        
        // cards view
        StackPane cardPane = new StackPane();
        cardPane.setPrefSize(400, 250);
        cardPane.setMaxSize(400, 250);
        cardPane.setStyle("-fx-background-color: white; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.2), 10, 0, 0, 0); -fx-background-radius: 10; -fx-border-color: #49654E; -fx-border-width: 2; -fx-border-radius: 10;");
        
        Flashcard currentCard = currentSet.getCards().get(currentReviewIndex);
        
        Label cardText = new Label(isShowingAnswer ? currentCard.getAnswer() : currentCard.getQuestion());
        cardText.setWrapText(true);
        cardText.setTextAlignment(TextAlignment.CENTER);
        cardText.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: #2c3e50;");
        
        if (isShowingAnswer) {
            cardPane.setStyle(cardPane.getStyle() + "-fx-background-color: #e8f5e9;"); // Light green for answer
        }
        
        cardPane.getChildren().add(cardText);
        
        // flips the card
        cardPane.setOnMouseClicked(e -> {
            isShowingAnswer = !isShowingAnswer;
            showReviewCard(); 
        });
        
        Label instruction = new Label("Click card to flip");
        instruction.setStyle("-fx-font-size: 12px; -fx-text-fill: #95a5a6;");

        // controls for the view
        HBox controls = new HBox(50);
        controls.setAlignment(Pos.CENTER);
        
        Button exitBtn = createButton("Stop", "#e74c3c");
        exitBtn.setOnAction(e -> showDeckEditor(currentSet));
        
        Button nextBtn = createButton("Next Card", "#49654E");
        nextBtn.setOnAction(e -> {
            if (currentReviewIndex < currentSet.size() - 1) {
                currentReviewIndex++;
                isShowingAnswer = false;
                showReviewCard();
            } else {
                showAlert("You finished the deck!");
                showDeckEditor(currentSet);
            }
        });
        
        controls.getChildren().addAll(exitBtn, nextBtn);
        
        reviewView.getChildren().addAll(progressLabel, cardPane, instruction, controls);
        this.getChildren().add(reviewView);
    }

    // helpers
    private void showDeckSelection() {
        this.getChildren().clear();
        deckListView.getItems().setAll(service.getSets()); 
        this.getChildren().add(deckSelectionView);
    }
    
    private void handleCreateDeck() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("New Deck");
        dialog.setHeaderText("Create a new Flashcard Set");
        dialog.setContentText("Deck Name:");
        
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(name -> {
            if (!name.trim().isEmpty()) {
                service.addSet(name);
                showDeckSelection();
            }
        });
    }
    
    private void handleDeleteDeck() {
        FlashcardSet selected = deckListView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Please select a deck to delete.");
            return;
        }
        service.deleteSet(selected);
        showDeckSelection();
    }
    
    private Button createButton(String text, String color) {
        Button btn = new Button(text);
        btn.setStyle(String.format("-fx-background-color: %s; -fx-text-fill: white; -fx-font-weight: bold; -fx-cursor: hand;", color));
        btn.setPrefWidth(120);
        return btn;
    }
    
    private void showAlert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}