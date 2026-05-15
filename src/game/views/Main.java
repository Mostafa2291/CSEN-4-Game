package game.views;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Arrays;
import java.util.Optional;

import javafx.scene.input.KeyCode;
import game.engine.Board;
import game.engine.Constants;
import game.engine.Game;
import game.engine.Role;
import game.engine.cards.Card;
import game.engine.cards.ConfusionCard;
import game.engine.cells.CardCell;
import game.engine.cells.Cell;
import game.engine.cells.ContaminationSock;
import game.engine.cells.ConveyorBelt;
import game.engine.cells.DoorCell;
import game.engine.cells.MonsterCell;
import game.engine.exceptions.InvalidMoveException;
import game.engine.exceptions.OutOfEnergyException;
import game.engine.monsters.Monster;
import game.engine.monsters.Schemer;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*; // Imports ColumnConstraints and RowConstraints automatically!
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class Main extends Application {

    private ArrayList<HBox> monsterContainers = new ArrayList<>();
    private int previousPlayerPos = 0;
    private int previousOpponentPos = 0;

    private Label diceRes;
    private Label currentturnLabel;
    private Label playerStatusLabel;
    private Label opponentStatusLabel;
    private Scene mainMenuScene;
    private VBox layout;
    private static Stage stage;
    private static Game myGame;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;

        // ── Shared background size ────────────────────────────────────────────
        BackgroundSize bSize = new BackgroundSize(1.0, 1.0, true, true, false, true);

        // ── Buttons ───────────────────────────────────────────────────────────
        Button start            = new Button("Start Game!");
        Button quit             = new Button("Quit");
        Button controls         = new Button("Controls");
        Button laugher          = new Button("Laugher");
        Button scarer           = new Button("Scarer");
        Button back             = new Button("Go Back");
        Button instructions     = new Button("Instructions");
        Button backInstructions = new Button("Go back to main menu");

        // Padding
        start.setPadding(new Insets(10));
        quit.setPadding(new Insets(10));
        controls.setPadding(new Insets(10));
        instructions.setPadding(new Insets(10));
        back.setPadding(new Insets(10, 20, 10, 20));
        backInstructions.setPadding(new Insets(10, 20, 10, 20));

        // ── Main Menu ─────────────────────────────────────────────────────────
        layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(start, instructions, quit);

        BackgroundImage menuBG = new BackgroundImage(
            new Image("file:Resources/Images/background2.jpg"),
            BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER, bSize
        );
        layout.setBackground(new Background(menuBG));

        // ── Role Select Screen ────────────────────────────────────────────────
        StackPane roleSelectRoot = new StackPane();

        // Split background: laugher left, scarer right
        HBox backgroundSplitter = new HBox(0);
        Pane leftPane  = new Pane();
        Pane rightPane = new Pane();
        leftPane.prefWidthProperty().bind(stage.widthProperty().divide(2));
        rightPane.prefWidthProperty().bind(stage.widthProperty().divide(2));

        ImageView leftView = new ImageView(new Image("file:Resources/Images/laugher.jpg"));
        leftView.setPreserveRatio(true);
        leftView.fitHeightProperty().bind(stage.heightProperty());

        ImageView rightView = new ImageView(new Image("file:Resources/Images/scarer1.jpg"));
        rightView.setPreserveRatio(true);
        rightView.fitHeightProperty().bind(stage.heightProperty());

        leftPane.getChildren().add(leftView);
        rightPane.getChildren().add(rightView);
        backgroundSplitter.getChildren().addAll(leftPane, rightPane);

        HBox roleButtons = new HBox(50);
        roleButtons.setAlignment(Pos.CENTER);
        roleButtons.getChildren().addAll(laugher, scarer);

        StackPane.setAlignment(back, Pos.BOTTOM_LEFT);
        StackPane.setMargin(back, new Insets(20));

        roleSelectRoot.getChildren().addAll(backgroundSplitter, roleButtons, back);

        // ── Instructions Screen ───────────────────────────────────────────────
        StackPane instructionPane = new StackPane();
        Label instructionTitle = new Label("GAME INSTRUCTIONS");
        instructionTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        StackPane.setAlignment(instructionTitle, Pos.TOP_CENTER);
        StackPane.setMargin(instructionTitle, new Insets(20));

        TextArea instructionsTxt = new TextArea(
            "Welcome to DooR DasH: Scare vs Laugh Touchdown 👾⚡\n\n" +
            "Step onto the Floor of Monstropolis, where only the smartest, fastest, and most daring monster will claim victory.\n\n" +
            "Reach Boo's Door with at least 1000 energy before your rival does.\n\n" +
            "──────────────────────────────\n" +
            "HOW THE GAME WORKS\n" +
            "──────────────────────────────\n" +
            "Move across a wild 100-cell board filled with:\n" +
            "  🚪 Energy Doors\n" +
            "  ⚙️  Conveyor Belts\n" +
            "  🧦 Contamination Socks\n" +
            "  👹 Monster Cells\n" +
            "  🎴 Mystery Cards\n\n" +
            "Every turn:\n" +
            "  • Optionally activate your monster's special powerup\n" +
            "  • Roll the dice 🎲\n" +
            "  • Move across the Floor\n" +
            "  • Trigger the effect of the cell you land on\n\n" +
            "But be careful — one lucky card or dangerous sock can completely flip the game.\n\n" +
            "──────────────────────────────\n" +
            "CHOOSE YOUR MONSTER STYLE\n" +
            "──────────────────────────────\n" +
            "  ⚡ Dashers      — blaze through the board at incredible speed.\n" +
            "  💥 Dynamos      — gain massive energy but suffer massive losses.\n" +
            "  🔀 Multitaskers — sacrifice movement for stronger energy control.\n" +
            "  🎭 Schemers     — manipulate everyone and steal energy like pros.\n\n" +
            "──────────────────────────────\n" +
            "DOORS DECIDE EVERYTHING\n" +
            "──────────────────────────────\n" +
            "Landing on the right door boosts your entire team's energy.\n" +
            "Landing on the wrong one? Your whole team pays the price.\n" +
            "Shields, confusion cards, steals, swaps, and monster abilities create nonstop chaos where no lead is ever safe. 🔥\n\n" +
            "──────────────────────────────\n" +
            "VICTORY\n" +
            "──────────────────────────────\n" +
            "To win, you must:\n" +
            "  • Reach the final cell: Boo's Door (Cell 99)\n" +
            "  • Have 1000+ energy\n" +
            "  • Outsmart your opponent before they do the same\n\n" +
            "One monster enters the spotlight.\n" +
            "One monster powers Monstropolis.\n" +
            "Will you scare… or make them laugh? 🎭"
        );
        instructionsTxt.setEditable(false);
        instructionsTxt.setWrapText(true);
        instructionsTxt.setStyle("-fx-font-size: 14px;");
        instructionsTxt.setMaxWidth(600);
        instructionsTxt.setMaxHeight(400);

        StackPane.setAlignment(backInstructions, Pos.BOTTOM_LEFT);
        StackPane.setMargin(backInstructions, new Insets(20));

        BackgroundImage instBG = new BackgroundImage(
            new Image("file:Resources/Images/inst.jpg"),
            BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER, bSize
        );
        instructionPane.setBackground(new Background(instBG));
        instructionPane.getChildren().addAll(instructionTitle, instructionsTxt, backInstructions);

        // ── Scene ─────────────────────────────────────────────────────────────
        mainMenuScene = new Scene(layout, 800, 600);
        stage.setTitle("DooR DasH: Scare vs Laugh");
        stage.setScene(mainMenuScene);
        stage.show();

        // ── Button Actions ────────────────────────────────────────────────────
        start.setOnAction(e -> stage.getScene().setRoot(roleSelectRoot));
        start.setOnMouseEntered(e -> start.setText("> Start Game !!"));
        start.setOnMouseExited(e  -> start.setText("Start Game!"));

        instructions.setOnAction(e -> stage.getScene().setRoot(instructionPane));
        instructions.setOnMouseEntered(e -> instructions.setText("Game Instructions"));
        instructions.setOnMouseExited(e  -> instructions.setText("Instructions"));

        back.setOnAction(e -> stage.getScene().setRoot(layout));
        back.setOnMouseEntered(e -> back.setText("> Go Back"));
        back.setOnMouseExited(e  -> back.setText("Go Back"));

        backInstructions.setOnAction(e -> stage.getScene().setRoot(layout));
        quit.setOnAction(e -> stage.close());
        quit.setOnMouseEntered(e -> quit.setText("> Quit :("));
        quit.setOnMouseExited(e  -> quit.setText("Quit"));

        laugher.setOnAction(e -> {
            try{
                myGame = new Game(Role.LAUGHER);
                System.out.println("Game loaded :D");
                startGameBoard();
            } catch(IOException ex){
                System.out.println("Failed to Load game ");
            }
        } );

        scarer.setOnAction(e ->{
             try{
                myGame = new Game(Role.SCARER);
                System.out.println("Game loaded :D");
                startGameBoard();
            } catch(IOException ex){
                System.out.println("Failed to Load game ");
            }
        } );

    }

    private StackPane createCell(Cell modelCell){
        StackPane pane = new StackPane();
        
        String cssColor;
        if(modelCell instanceof MonsterCell){
            cssColor = "lightcoral";
        }
        else if(modelCell instanceof CardCell){
            cssColor = "aquamarine";
        }
        else if(modelCell instanceof ConveyorBelt){
            cssColor = "magenta";
        }
        else if (modelCell instanceof ContaminationSock){
            cssColor = "darkorchid";
        }
        else {
            cssColor = "papayawhip";
          
        }

        pane.setStyle("-fx-background-color: " + cssColor + "; -fx-border-color: black; -fx-border-width: 0.5px;");
        
        // ── RESPONSIVE FIX: Allow the pane to grow indefinitely instead of setting a fixed 60x60 ──
        pane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        return pane;
    }

    private int rowColToIndex(int row, int col) {
        int cols = Constants.BOARD_COLS;
        int index = row * cols;
        if (row % 2 == 1) {
            index += (cols - 1 - col);
        } else {
            index += col;
        }
        return index;
    }

    private void updateMonsters(){

        if (monsterContainers.get(previousPlayerPos) != null) {
            monsterContainers.get(previousPlayerPos).getChildren().clear();
        }
        if (monsterContainers.get(previousOpponentPos) != null) {
            monsterContainers.get(previousOpponentPos).getChildren().clear();
        }

        Monster player = myGame.getPlayer();
        Monster opponent = myGame.getOpponent();

        // 2. Draw Player in their new position
        StackPane playerUI = new StackPane();
        Rectangle pRect = new Rectangle();
        Rectangle pImage = new Rectangle();

        //setting player image
        if(myGame.getPlayer().getName().equals("Mike Wazowski")){
            pRect.setFill(Color.GREEN);
            ImageView playeri = new ImageView(new Image("file:Resources/Images/Mike Wazowski.jpg"));
            pImage.setFill(new ImagePattern(playeri.getImage()));

        }
        else if(myGame.getPlayer().getName().equals("James P. Sullivan")){
            pRect.setFill(Color.BLUE);
            ImageView playeri = new ImageView(new Image("file:Resources/Images/James p.Sullivan.jpg"));
            pImage.setFill(new ImagePattern(playeri.getImage()));
        }
        else if(myGame.getPlayer().getName().equals("Randall Boggs")){
            pRect.setFill(Color.PURPLE);
                ImageView playeri = new ImageView(new Image("file:Resources/Images/Randall Boggs.jpg"));
                pImage.setFill(new ImagePattern(playeri.getImage()));
        }
        else if(myGame.getPlayer().getName().equals("Celia Mae")){
            pRect.setFill(Color.PINK);
            ImageView playeri = new ImageView(new Image("file:Resources/Images/Celia Mae.jpg"));
            pImage.setFill(new ImagePattern(playeri.getImage()));
        }
        else if(myGame.getPlayer().getName().equals("Roz")){
            pRect.setFill(Color.GRAY);
            ImageView playeri = new ImageView(new Image("file:Resources/Images/Roz.jpg"));
            pImage.setFill(new ImagePattern(playeri.getImage()));
        }
      
        else if(myGame.getPlayer().getName().equals("Fungus")){
             pRect.setFill(Color.WHITE);
            ImageView playeri = new ImageView(new Image("file:Resources/Images/Fungus.jpg"));
            pImage.setFill(new ImagePattern(playeri.getImage()));
        }
         else if(myGame.getPlayer().getName().equals("Henry J.Waternoose")){
             pRect.setFill(Color.WHITE);
            ImageView playeri = new ImageView(new Image("file:Resources/Images/Henry J.Waternoose.jpg"));
            pImage.setFill(new ImagePattern(playeri.getImage()));
        }
        else if(myGame.getPlayer().getName().equals("Yeti")){
             pRect.setFill(Color.WHITE);
            ImageView playeri = new ImageView(new Image("file:Resources/Images/Yeti.jpg"));
            pImage.setFill(new ImagePattern(playeri.getImage()));
      
        }



        // ── RESPONSIVE FIX: Bind monster size to a percentage of the screen width ──
        pRect.widthProperty().bind(stage.widthProperty().multiply(0.015)); 
        pRect.heightProperty().bind(stage.widthProperty().multiply(0.015)); 
        pImage.widthProperty().bind(stage.widthProperty().multiply(0.015));
        pImage.heightProperty().bind(stage.widthProperty().multiply(0.015));
    

        Label pEnergy = new Label(player.getEnergy() + "");
        pEnergy.setStyle("-fx-font-size: 8px; -fx-text-fill: black; -fx-font-weight: bold;");
        playerUI.getChildren().addAll(pRect, pEnergy,pImage);
        
        monsterContainers.get(player.getPosition()).getChildren().add(playerUI);

        // 3. Draw Opponent in their new position
        StackPane opponentUI = new StackPane();
        Rectangle oRect = new Rectangle(); 
        Rectangle oImage = new Rectangle();
        oRect.setFill(Color.MAGENTA);
        // ── RESPONSIVE FIX: Bind monster size to a percentage of the screen width ──
        oRect.widthProperty().bind(stage.widthProperty().multiply(0.015)); 
        oRect.heightProperty().bind(stage.widthProperty().multiply(0.015)); 
        oImage.widthProperty().bind(stage.widthProperty().multiply(0.015));
        oImage.heightProperty().bind(stage.widthProperty().multiply(0.015));

        Label oEnergy = new Label(opponent.getEnergy() + "");
        oEnergy.setStyle("-fx-font-size: 8px; -fx-text-fill: black; -fx-font-weight: bold;");
        opponentUI.getChildren().addAll(oRect, oEnergy,oImage);
        
        monsterContainers.get(opponent.getPosition()).getChildren().add(opponentUI);

        previousPlayerPos = player.getPosition();
        previousOpponentPos = opponent.getPosition();

        if (currentturnLabel != null) {
            currentturnLabel.setText("Current Turn: " + myGame.getCurrent().getName());

             //setting opponent image 
         if(myGame.getOpponent().getName().equals("Mike Wazowski")){
            oRect.setFill(Color.GREEN);
            ImageView opponenti = new ImageView(new Image("file:Resources/Images/Mike Wazowski.jpg"));
            oImage.setFill(new ImagePattern(opponenti.getImage()));

        }
        else if(myGame.getOpponent().getName().equals("James P. Sullivan")){
            oRect.setFill(Color.BLUE);
            ImageView opponenti = new ImageView(new Image("file:Resources/Images/James p.Sullivan.jpg"));
            oImage.setFill(new ImagePattern(opponenti.getImage()));
        }
        else if(myGame.getOpponent().getName().equals("Randall Boggs")){
            oRect.setFill(Color.PURPLE);
                ImageView opponenti = new ImageView(new Image("file:Resources/Images/Randall Boggs.jpg"));
                oImage.setFill(new ImagePattern(opponenti.getImage()));
        }
        else if(myGame.getOpponent().getName().equals("Celia Mae")){
            oRect.setFill(Color.PINK);
            ImageView opponenti = new ImageView(new Image("file:Resources/Images/Celia Mae.jpg"));
            oImage.setFill(new ImagePattern(opponenti.getImage()));
        }
        else if(myGame.getOpponent().getName().equals("Roz")){
            oRect.setFill(Color.GRAY);
            ImageView opponenti = new ImageView(new Image("file:Resources/Images/Roz.jpg"));
            oImage.setFill(new ImagePattern(opponenti.getImage()));
        }
      
        else if(myGame.getOpponent().getName().equals("Fungus")){
             oRect.setFill(Color.WHITE);
            ImageView opponenti = new ImageView(new Image("file:Resources/Images/Fungus.jpg"));
            oImage.setFill(new ImagePattern(opponenti.getImage()));
        }
         else if(myGame.getOpponent().getName().equals("Henry J.Waternoose")){
             oRect.setFill(Color.WHITE);
            ImageView opponenti = new ImageView(new Image("file:Resources/Images/Henry J.Waternoose.jpg"));
            oImage.setFill(new ImagePattern(opponenti.getImage()));
        }
        else if(myGame.getOpponent().getName().equals("Yeti")){
             oRect.setFill(Color.WHITE);
            ImageView opponenti = new ImageView(new Image("file:Resources/Images/Yeti.jpg"));
            oImage.setFill(new ImagePattern(opponenti.getImage()));
      
        }
            
            // ── Format Player Stats ──
            String pStats = "Name: " + player.getName() + "\n\n" +
                            "Original Role: " + player.getOriginalRole() + "\n" +
                            "Current Role: " + player.getRole() + "\n" +
                            "Type: " + player.getClass().getSimpleName() + "\n\n" +
                            "Energy: " + player.getEnergy() + "\n" +
                            "Position: " + player.getPosition() + "\n\n" +
                            "Status:\n" + 
                            "Confusion Turns: " + player.getConfusionTurns() + "\n" + 
                            "Frozen: " + player.isFrozen() + "\n" +
                            "Active Shield: " + player.isShielded();
            playerStatusLabel.setText(pStats);

            // ── Format Opponent Stats ──
            String oStats = "Name: " + opponent.getName() + "\n\n" +
                            "Original Role: " + opponent.getOriginalRole() + "\n" +
                            "Current Role: " + opponent.getRole() + "\n" +
                            "Type: " + opponent.getClass().getSimpleName() + "\n\n" +
                            "Energy: " + opponent.getEnergy() + "\n" +
                            "Position: " + opponent.getPosition() + "\n\n" +
                            "Status:\n" + 
                            "Confusion Turns: " + opponent.getConfusionTurns() + "\n" + 
                            "Frozen: " + opponent.isFrozen() + "\n" +
                            "Active Shield: " + opponent.isShielded();
            opponentStatusLabel.setText(oStats);
        }
    }

   private void startGameBoard() {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        
        // ── RESPONSIVE FIX: Apply percentage constraints to the Grid! ──
        for (int i = 0; i < Constants.BOARD_COLS; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / Constants.BOARD_COLS);
            grid.getColumnConstraints().add(colConst);
        }
        for (int i = 0; i < Constants.BOARD_ROWS; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(100.0 / Constants.BOARD_ROWS);
            grid.getRowConstraints().add(rowConst);
        }
        
        Board board = myGame.getBoard();
        Cell[][] backendGrid = board.getBoardCells();

        monsterContainers.clear();
        for (int i = 0; i < Constants.BOARD_SIZE; i++) {
            monsterContainers.add(null);
        }

        for (int row = 0; row < Constants.BOARD_ROWS; row++) {
            for (int col = 0; col < Constants.BOARD_COLS; col++) {
                
                Cell backendCell = backendGrid[row][col];
                StackPane uiCell = createCell(backendCell);
                
                int index = rowColToIndex(row, col);

                Label numberLabel = new Label(String.valueOf(index));
                numberLabel.setStyle("-fx-font-size: 10px; -fx-font-weight: bold;");
                StackPane.setAlignment(numberLabel, Pos.TOP_LEFT);
                uiCell.getChildren().add(numberLabel);

                if (backendCell instanceof DoorCell) {
                    Label energyLabel = new Label(((DoorCell) backendCell).getEnergy() + " E");
                    energyLabel.setStyle("-fx-font-size: 10px;");
                    StackPane.setAlignment(energyLabel, Pos.CENTER);
                    uiCell.getChildren().add(energyLabel);
                        ImageView closedDoorImage = new ImageView(new Image("file:Resources/Images/closed door.jpeg"));
                        closedDoorImage.fitWidthProperty().bind(uiCell.widthProperty().multiply(0.3));
                        closedDoorImage.fitHeightProperty().bind(uiCell.widthProperty().multiply(0.3));
                        uiCell.getChildren().add(closedDoorImage);
                        uiCell.setAlignment(closedDoorImage,Pos.BOTTOM_RIGHT);
                
                }

                HBox charactersBox = new HBox(5);
                charactersBox.setAlignment(Pos.BOTTOM_CENTER);
                monsterContainers.set(index, charactersBox); 
                uiCell.getChildren().add(charactersBox);

                int gridRow = (Constants.BOARD_ROWS - 1) - row;
                grid.add(uiCell, col, gridRow);
            }
        }
      
        // ── BUTTON CONTROLS ──
        Button rollDiceBtn = new Button("Roll Dice");
        Button activatePowerUpBtn = new Button("Activate PowerUp!");

        rollDiceBtn.setOnAction(e -> {
            try {
                Card currentCard = myGame.getBoard().getCards().getFirst();
                int deckSizeBefore = myGame.getBoard().getCards().size();
                Monster activeMonster = myGame.getCurrent();
                Boolean hadShield = activeMonster.isShielded();
                int initialE = activeMonster.getEnergy();
                 
               
                myGame.playTurn();
                diceRes.setText("Rolled: " + myGame.getRoll());

      
               
                if(activeMonster.getPosition() == Constants.WINNING_POSITION && activeMonster.getEnergy()>= Constants.WINNING_ENERGY){
                    ButtonType mainMenu= new ButtonType("Main Menu");
                    ButtonType gameQuit = new ButtonType("Quit game :-( ");

                    Alert gameAlert = new Alert(AlertType.CONFIRMATION);
                    gameAlert.setTitle("Game over");
                    gameAlert.setHeaderText("The " + activeMonster.getOriginalRole() + "'s Won the game!!! ");
                    gameAlert.setContentText(activeMonster.getName() + " Won!!" + "\n"
                     + myGame.getPlayer().getName() + "'s energy: " + myGame.getPlayer().getEnergy() + "\n"
                    +  myGame.getOpponent().getName() + "'s energy: " +  myGame.getOpponent().getEnergy()  );
                    gameAlert.getButtonTypes().setAll(gameQuit, mainMenu);
                    Optional <ButtonType> result = gameAlert.showAndWait();

                    if(result.isPresent() && result.get() == mainMenu){
                        mainMenuScene.setRoot(layout);
                        stage.setScene(mainMenuScene);
                        stage.sizeToScene(); 
                        stage.centerOnScreen();
                    }
                    else if(result.isPresent() && result.get() == gameQuit){
                        stage.close();
                    }
                }
    

                if(initialE>activeMonster.getEnergy()){
                    Alert damageAlert = new Alert(AlertType.INFORMATION);
                    damageAlert.setTitle("Damage Alert !");
                    damageAlert.setHeaderText(activeMonster.getName() +" sustained damage :-(");
                    damageAlert.setContentText(activeMonster.getName() + " Lost " + (initialE-activeMonster.getEnergy()) + " Energy");
                    damageAlert.showAndWait();
                }
                if(!activeMonster.isShielded() && hadShield){
                    Alert shieldAlert = new Alert(AlertType.INFORMATION);
                    shieldAlert.setTitle("Shield Alert");
                    shieldAlert.setHeaderText("You used your shield ");
                    shieldAlert.setContentText(activeMonster.getName() +" used their shield to block the negative energy effect !!");
                    shieldAlert.showAndWait();
                }
                if(myGame.getRoll() ==0 ){
                    Alert frozAlert = new Alert(AlertType.ERROR);
                    frozAlert.setTitle("FREEZE !");
                    frozAlert.setHeaderText("You are Frozen !");
                    frozAlert.setContentText("Turn is skipped because you are frozen ! better luck next time :P");
                    frozAlert.showAndWait();
                }
                if(myGame.getBoard().getCards().size()< deckSizeBefore){
                    Alert cardAlert = new Alert(AlertType.INFORMATION);
                    cardAlert.setTitle("LANDED ON CARD CELL !!");
                    if(currentCard instanceof ConfusionCard){
                        cardAlert.setHeaderText(activeMonster.getName() + " drew " + currentCard.getName() + " this is a " + currentCard.getClass().getSimpleName());
                        cardAlert.setContentText("Both monsters are confused causing them to swap roles for " + activeMonster.getConfusionTurns() + " Turns");
                    }
                    else{
                    cardAlert.setHeaderText(activeMonster.getName() +" drew " + currentCard.getName() + " this is a " + currentCard.getClass().getSimpleName());
                    cardAlert.setContentText("This card does the following: " + currentCard.getDescription());
                    }
                    cardAlert.showAndWait();
                }
               
                updateMonsters();
            } catch (InvalidMoveException ex) {
                Alert  oppLand = new Alert(AlertType.ERROR);
                oppLand.setTitle("Invalid Move!");
                oppLand.setHeaderText("Cannot land on opponent !!");
                oppLand.setContentText("Invalid move :-)");
                oppLand.showAndWait();
            }
        });

        activatePowerUpBtn.setOnAction(e -> {
            try {
                myGame.usePowerup();
                updateMonsters(); 
            } catch (OutOfEnergyException ex) {
                Alert energyAlert = new Alert(AlertType.ERROR);
                energyAlert.setTitle("Energy Alert ! ");
                energyAlert.setHeaderText("Not enough energy");
                energyAlert.setContentText(myGame.getCurrent().getName() + " doesnt have enough energy to activate powerup :-( ");
                energyAlert.showAndWait();
            }
        });

// ── CUSTOM PAUSE MENU OVERLAY ──
        VBox pauseOverlay = new VBox();
        pauseOverlay.setAlignment(Pos.CENTER);
        pauseOverlay.setStyle("-fx-background-color: rgba(0, 0, 0, 0.6);"); 
        pauseOverlay.setVisible(false); 

        VBox pauseMenuBox = new VBox(20);
        pauseMenuBox.setAlignment(Pos.CENTER);
        pauseMenuBox.setMaxSize(300, 250); 
        pauseMenuBox.setStyle("-fx-background-color: white; -fx-padding: 30px; -fx-border-color: #333; -fx-border-width: 3px; -fx-background-radius: 10px; -fx-border-radius: 10px;");

        Label pauseTitle = new Label("GAME PAUSED");
        pauseTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        Button resumeBtn = new Button("Resume Game");
        Button quitToMenuBtn = new Button("Quit to Main Menu");
        resumeBtn.setPrefWidth(150);
        quitToMenuBtn.setPrefWidth(150);

        pauseMenuBox.getChildren().addAll(pauseTitle, resumeBtn, quitToMenuBtn);
        pauseOverlay.getChildren().add(pauseMenuBox);

        resumeBtn.setOnAction(e -> pauseOverlay.setVisible(false)); 
        quitToMenuBtn.setOnAction(e -> {
            pauseOverlay.setVisible(false); 
            mainMenuScene.setRoot(layout);
            stage.setScene(mainMenuScene);
        });





        HBox controlsBox = new HBox(20);
        controlsBox.setAlignment(Pos.CENTER);
        controlsBox.setPadding(new Insets(10));
        controlsBox.getChildren().addAll(rollDiceBtn, activatePowerUpBtn);

        // ── FINAL LAYOUT ──
        BorderPane root = new BorderPane();
        root.setCenter(grid);
        root.setBottom(controlsBox);

        // 1. Create Left Sidebar for Player
        VBox leftSidebar = new VBox(10);
        leftSidebar.setPadding(new Insets(15));
        // ── RESPONSIVE FIX: Bind width to 20% of the entire window ──
        leftSidebar.prefWidthProperty().bind(root.widthProperty().multiply(0.20)); 
        leftSidebar.setStyle("-fx-background-color: #e0f7fa; -fx-border-color: lightgray; -fx-border-width: 0 1 0 0;");
        Label pTitle = new Label("🎮 PLAYER");
        pTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        playerStatusLabel = new Label();
        playerStatusLabel.setStyle("-fx-font-size: 14px; -fx-line-spacing: 5px;");
        playerStatusLabel.setWrapText(true);
        leftSidebar.getChildren().addAll(pTitle, playerStatusLabel);

        // 2. Create Right Sidebar for Opponent
        VBox rightSidebar = new VBox(10);
        rightSidebar.setPadding(new Insets(15));
        // ── RESPONSIVE FIX: Bind width to 20% of the entire window ──
        rightSidebar.prefWidthProperty().bind(root.widthProperty().multiply(0.20)); 
        rightSidebar.setStyle("-fx-background-color: #fce4ec; -fx-border-color: lightgray; -fx-border-width: 0 0 0 1;");
        Label oTitle = new Label("👾 OPPONENT");
        oTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        opponentStatusLabel = new Label();
        opponentStatusLabel.setStyle("-fx-font-size: 14px; -fx-line-spacing: 5px;");
        opponentStatusLabel.setWrapText(true);
        rightSidebar.getChildren().addAll(oTitle, opponentStatusLabel);

        // 3. Create Top Bar for Turn and Dice Roll
        VBox topCenterBox = new VBox(5);
        topCenterBox.setAlignment(Pos.CENTER);
        topCenterBox.setPadding(new Insets(10));
        currentturnLabel = new Label("Current Turn: " + myGame.getCurrent().getName());
        currentturnLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        diceRes = new Label("Rolled: -");
        diceRes.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: darkred;");
        topCenterBox.getChildren().addAll(currentturnLabel, diceRes);

        // 4. Attach everything to the BorderPane
        root.setLeft(leftSidebar);
        root.setRight(rightSidebar);
        root.setTop(topCenterBox);
        StackPane basePane = new StackPane();
        basePane.getChildren().addAll(root,pauseOverlay);
        Scene boardScene = new Scene(basePane, 1000, 800);
        stage.setScene(boardScene);

        // ── CHEAT CODES ──
        boardScene.setOnKeyPressed(event -> {
            Monster activeMonster = myGame.getCurrent();
            if (event.getCode() == KeyCode.W) {
                myGame.getCurrent().setPosition(99);
                System.out.println("CHEAT ACTIVATED: Teleported to Cell 99!");
                updateMonsters(); 

                if(activeMonster.getPosition() == Constants.WINNING_POSITION && activeMonster.getEnergy()>= Constants.WINNING_ENERGY){
                    ButtonType mainMenu= new ButtonType("Main Menu");
                    ButtonType gameQuit = new ButtonType("Quit game :-( ");

                    Alert gameAlert = new Alert(AlertType.CONFIRMATION);
                    gameAlert.setTitle("Game over");
                    gameAlert.setHeaderText("The " + activeMonster.getOriginalRole() + "'s Won the game!!! ");
                    gameAlert.setContentText(activeMonster.getName() + " Won!!" + "\n"
                     + myGame.getPlayer().getName() + "'s energy: " + myGame.getPlayer().getEnergy() + "\n"
                    +  myGame.getOpponent().getName() + "'s energy: " +  myGame.getOpponent().getEnergy()  );
                    gameAlert.getButtonTypes().setAll(gameQuit, mainMenu);
                    Optional <ButtonType> result = gameAlert.showAndWait();

                    if(result.isPresent() && result.get() == mainMenu){
                        mainMenuScene.setRoot(layout);
                        stage.setScene(mainMenuScene);
                        stage.sizeToScene(); 
                        stage.centerOnScreen();
                    }
                    else if(result.isPresent() && result.get() == gameQuit){
                        stage.close();
                    }
                }
                
            } else if (event.getCode() == KeyCode.E) {
                myGame.getCurrent().setEnergy(myGame.getCurrent().getEnergy() + 1000);
                System.out.println("CHEAT ACTIVATED: +1000 Energy!");
                updateMonsters(); 
                
                if(activeMonster.getPosition() == Constants.WINNING_POSITION && activeMonster.getEnergy()>= Constants.WINNING_ENERGY){
                    ButtonType mainMenu= new ButtonType("Main Menu");
                    ButtonType gameQuit = new ButtonType("Quit game :-( ");

                    Alert gameAlert = new Alert(AlertType.CONFIRMATION);
                    gameAlert.setTitle("Game over");
                    gameAlert.setHeaderText("The " + activeMonster.getOriginalRole() + "'s Won the game!!! ");
                   gameAlert.setContentText(activeMonster.getName() + " Won!!" + "\n"
                     + myGame.getPlayer().getName() + "'s energy: " + myGame.getPlayer().getEnergy() + "\n"
                    +  myGame.getOpponent().getName() + "'s energy: " +  myGame.getOpponent().getEnergy()  );
                    gameAlert.getButtonTypes().setAll( mainMenu,gameQuit);
                    Optional <ButtonType> result = gameAlert.showAndWait();

                    if(result.isPresent() && result.get() == mainMenu){
                        mainMenuScene.setRoot(layout);
                        stage.setScene(mainMenuScene);
                        stage.sizeToScene(); 
                        stage.centerOnScreen();
                    }
                    else if(result.isPresent() && result.get() == gameQuit){
                        stage.close();
                    }
                }
            }
            else if (event.getCode() == KeyCode.ESCAPE){
                pauseOverlay.setVisible(!pauseOverlay.isVisible());

            }
           
        });

        // Call update monsters for initial starting pos 
        updateMonsters();
    }
}