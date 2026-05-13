package game.views;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import game.engine.Board;
import game.engine.Constants;
import game.engine.Game;
import game.engine.Role;
import game.engine.cards.Card;
import game.engine.cells.CardCell;
import game.engine.cells.Cell;
import game.engine.cells.ContaminationSock;
import game.engine.cells.ConveyorBelt;
import game.engine.cells.DoorCell;
import game.engine.cells.MonsterCell;
import game.engine.monsters.Monster;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
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

    private static Stage stage;
    private static StackPane menuPane;
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
        VBox layout = new VBox(15);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(start, controls, instructions, quit);

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
        Scene scene = new Scene(layout, 800, 600);//3andy (yassin) law h=800 beteb2a out of bounds msh bashoof haga
        stage.setTitle("DooR DasH: Scare vs Laugh");
        stage.setScene(scene);
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

        


        //game functionality >.<      


       
    
            

       

    }
     private StackPane createCell(Cell modelCell){

        StackPane pane = new StackPane();
        Rectangle bg = new Rectangle(60,60);      

        bg.setStroke(Color.BLACK);
        if(modelCell instanceof MonsterCell){
            bg.setFill(Color.LIGHTCORAL);

        }
        else if(modelCell instanceof CardCell){
            bg.setFill(Color.AQUAMARINE);
        }
        else if(modelCell instanceof ConveyorBelt){
            bg.setFill(Color.MAGENTA);
        }
        else if (modelCell instanceof ContaminationSock){
            bg.setFill(Color.DARKORCHID);
        }
        else
            bg.setFill(Color.PAPAYAWHIP);//el lon da esmo helw
            

        pane.getChildren().add(bg);

        return pane;
    }

        private int rowColToIndex(int row, int col) {
        int cols = Constants.BOARD_COLS;
        
        // Start by finding the base number for the row (e.g., Row 2 starts at 20)
        int index = row * cols;

        if (row % 2 == 1) {
            // Odd rows go right-to-left, so we reverse the column addition
            index += (cols - 1 - col);
        } else {
            // Even rows go left-to-right, so we add the column normally
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
        Rectangle pRect = new Rectangle(20, 20, Color.BLUEVIOLET); 
        Label pEnergy = new Label(player.getEnergy() + "");
        pEnergy.setStyle("-fx-font-size: 8px; -fx-text-fill: black; -fx-font-weight: bold;");
        playerUI.getChildren().addAll(pRect, pEnergy);
        
        monsterContainers.get(player.getPosition()).getChildren().add(playerUI);

        // 3. Draw Opponent in their new position
        StackPane opponentUI = new StackPane();
        Rectangle oRect = new Rectangle(20, 20, Color.MAGENTA); 
        Label oEnergy = new Label(opponent.getEnergy() + "");
        oEnergy.setStyle("-fx-font-size: 8px; -fx-text-fill: black; -fx-font-weight: bold;");
        opponentUI.getChildren().addAll(oRect, oEnergy);
        
        monsterContainers.get(opponent.getPosition()).getChildren().add(opponentUI);

        previousPlayerPos = player.getPosition();
        previousOpponentPos = opponent.getPosition();

        if (currentturnLabel != null) {
            currentturnLabel.setText("Current Turn: " + myGame.getCurrent().getName());

            
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
        
        Board board = myGame.getBoard();
        Cell[][] backendGrid = board.getBoardCells();

        // ── PRE-FILL THE ARRAYLIST SO WE DON'T GET OUT OF BOUNDS ERRORS ──
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
                }

                // ── THE OPTIMIZATION: Use .set() to place it in the correct index ──
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
                myGame.playTurn();
                diceRes.setText("Rolled: " + myGame.getRoll());
                updateMonsters(); 
             } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
                  if (Board.getCards().isEmpty()) {
                    Board.reloadCards();
                }
                Card topCardBeforeTurn = Board.getCards().get(0);
                int sizeBefore = Board.getCards().size();
                // Check for roll of 0 and if player is frozen or landed on opponent
                 if (myGame.getRoll() == 0) {
                    // Check if player is frozen
                    if(myGame.getCurrent().isFrozen()) {
                    myGame.getCurrent().setFrozen(false); // Unfreeze the player for the next turn
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Turn Skipped");
                    alert.setHeaderText("Freeze Effect!");
                    alert.setContentText("The player was frozen and skipped their turn.");
                    alert.showAndWait();}
                    //landed on opponent alert
                    else if(myGame.getPlayer().getPosition() == myGame.getOpponent().getPosition()){ 
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Turn Skipped");
                        alert.setHeaderText("Landed on opponent!");
                        alert.setContentText("You rolled a 0 and cannot move this turn.");
                        alert.showAndWait();
                    
                    }
                    //drawing card alert
                } else if (Board.getCards().size() < sizeBefore) {
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Card Drawn");
                    alert.setHeaderText("You drew: " + topCardBeforeTurn.getName());
                    alert.setContentText("Effect: " + topCardBeforeTurn.getDescription());
                    alert.showAndWait();
                }
                
           
            
        });

        activatePowerUpBtn.setOnAction(e -> {
            try {
                myGame.usePowerup();
                
                updateMonsters(); 
            } catch (Exception ex) {
                System.out.println(ex.getMessage());
            }
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
        leftSidebar.setPrefWidth(180);
        leftSidebar.setMinWidth(180); // <--- ADD THIS: strictly locks the width
        leftSidebar.setMaxWidth(180);
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
        rightSidebar.setPrefWidth(180);
        rightSidebar.setMinWidth(180); // <--- ADD THIS: strictly locks the width
        rightSidebar.setMaxWidth(180);
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

        // INCREASED WIDTH TO 1000 so the sidebars don't squish your board!
        Scene boardScene = new Scene(root, 1000, 800);
        stage.setScene(boardScene);

        // Call update monsters for initial starting pos 
        updateMonsters();
    
        
    }


} 

