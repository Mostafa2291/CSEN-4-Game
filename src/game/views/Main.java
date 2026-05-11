package game.views;

import java.io.IOException;
import java.util.ArrayList;

import game.engine.Board;
import game.engine.Constants;
import game.engine.Game;
import game.engine.Role;
import game.engine.cells.CardCell;
import game.engine.cells.Cell;
import game.engine.cells.ContaminationSock;
import game.engine.cells.ConveyorBelt;
import game.engine.cells.MonsterCell;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
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
        Scene scene = new Scene(layout, 800, 800);
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
            bg.setFill(Color.BLUE);

        }
        else if(modelCell instanceof CardCell){
            bg.setFill(Color.RED);
        }
        else if(modelCell instanceof ConveyorBelt){
            bg.setFill(Color.GREEN);
        }
        else if (modelCell instanceof ContaminationSock){
            bg.setFill(Color.ORANGE);
        }
        else
            bg.setFill(Color.YELLOW);

        pane.getChildren().add(bg);

        return pane;
    }

    private void startGameBoard(){
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        // grid.setHgap(2); // 2 pixels of horizontal space between cells
        // grid.setVgap(2); // 2 pixels of vertical space between cells
        // grid.setStyle("-fx-background-color: black; -fx-padding: 2;");
        Board board = myGame.getBoard();
        Cell [] [] backendGrid = board.getBoardCells();

        for(int row = 0 ; row< Constants.BOARD_ROWS ; row++){
            for(int col = 0; col < Constants.BOARD_COLS ; col++){
                Cell backendCell = backendGrid[row][col];

                StackPane uiCell = createCell(backendCell);
                int gridRow = (Constants.BOARD_ROWS -1 ) - row ;
                grid.add(uiCell, col, gridRow);
            }
        }
        Scene boardScene = new Scene(grid, 800,800);
        stage.setScene(boardScene);

    }

}