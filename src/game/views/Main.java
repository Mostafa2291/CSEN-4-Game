package game.views;
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
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.layout.VBox;
import javafx.scene.layout.HBox;

public class Main extends Application {

    private static Stage stage;
    private static StackPane menuPane;
      public static void main(String[] args) { //runs the game dont touch <3
        launch(args);
    }


    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        menuPane = new StackPane();
        StackPane roleSelectRoot = new StackPane();

        

       VBox layout = new VBox(15); // 15px gap between buttons
        layout.setAlignment(javafx.geometry.Pos.CENTER); // Put the menu in the middle

        HBox roleout = new HBox(50);
        roleout.setAlignment(javafx.geometry.Pos.CENTER);

        //javafx.scene.Scene rolScene = new javafx.scene.Scene(roleout,800,600);




        Button start = new Button("Start Game!");
        Button quit = new Button("Quit");
        Button controls = new Button("Controls");
        Button laugher = new Button("Laugher");
        Button scarer = new Button("Scarer");
        Button back = new Button("Go back");
        Button instructions = new Button("Instructions");
        Button backinstructions = new Button("Go back to main menue");



        layout.getChildren().addAll(start, controls, quit,instructions);

        Scene scene = new Scene(layout, 800, 600);


        roleout.getChildren().addAll(laugher,scarer);

        // start.setBackground(null);
        // quit.setBackground(null);
        // controls.setBackground(null);
        start.setPadding(new Insets(10));
        quit.setPadding(new Insets(10));
        controls.setPadding(new Insets(10));
        back.setPadding(new Insets(10, 20, 10, 20));
        instructions.setPadding(new Insets(10));


        StackPane.setAlignment(back, javafx.geometry.Pos.BOTTOM_LEFT);
        StackPane.setMargin(back, new Insets(20));

        



        


    BackgroundSize bSize = new BackgroundSize(1.0, 1.0, true, true, false, true);
        
    BackgroundImage BI = new BackgroundImage(
        new Image("file:Resources/Images/background2.jpg"), 
        BackgroundRepeat.NO_REPEAT, 
        BackgroundRepeat.NO_REPEAT,
        BackgroundPosition.CENTER, 
        bSize
    );
        layout.setBackground( new Background(BI));
    

    HBox backgroundSplitter = new HBox(0);
            // Create two Panes that each take up half the screen
    Pane leftPane = new Pane();
    Pane rightPane = new Pane();
    leftPane.prefWidthProperty().bind(stage.widthProperty().divide(2));
    rightPane.prefWidthProperty().bind(stage.widthProperty().divide(2));

    // Create ImageViews that SCALE to the height but keep ratio
    ImageView leftView = new ImageView(new Image("file:Resources/Images/laugher.jpg"));
    leftView.setPreserveRatio(true);
    leftView.fitHeightProperty().bind(stage.heightProperty());

    ImageView rightView = new ImageView(new Image("file:Resources/Images/scarer1.jpg"));
    rightView.setPreserveRatio(true);
    rightView.fitHeightProperty().bind(stage.heightProperty());

    // Add them to their respective panes
    leftPane.getChildren().add(leftView);
    rightPane.getChildren().add(rightView);

    // Add panes to the HBox
    backgroundSplitter.getChildren().addAll(leftPane, rightPane);
    
    roleSelectRoot.getChildren().add(backgroundSplitter);

    roleSelectRoot.getChildren().addAll(roleout, back);





        stage.setTitle("DooR DasH: Scare vs Laugh");
        stage.setScene(scene);
        stage.show();   




        
        start.setOnAction(new EventHandler<ActionEvent>() {
            public void handle (ActionEvent actionEvent){
                stage.getScene().setRoot(roleSelectRoot);
            }  
        });
        StackPane instructionpane = new StackPane();
        instructionpane.getChildren().add(new Label("GAME INSTRUCTIONS"));
        instructions.setOnAction(e -> {
                stage.getScene().setRoot(instructionpane);
            });
        instructions.setOnMouseEntered(e -> instructions.setText("Game instructions"));
        instructions.setOnMouseExited(e -> instructions.setText("Instructions"));
        Label instructionTitle = new Label("GAME INSTRUCTIONS");
        instructionTitle.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        instructionpane.setAlignment(instructionTitle, Pos.TOP_CENTER);
        instructionpane.setMargin(instructionTitle, new Insets(20));
        start.setOnMouseEntered(e -> start.setText("> Start Game !!"));
        start.setOnMouseExited(e ->  start.setText("Start Game!"));
        TextArea instructionstxt = new TextArea();
        instructionstxt.setText("Welcome to DooR DasH: Scare vs Laugh Touchdown 👾⚡\n" + //
                        "\n" + //
                        "Step onto the Floor of Monstropolis, where only the smartest, fastest, and most daring monster will claim victory. Whether you fight with terrifying screams or unstoppable laughter, your mission is simple:\n" + //
                        "\n" + //
                        "Reach Boo’s Door with at least 1000 energy before your rival does.How the Game Works\n" + //
                                                        "\n" + //
                                                        "\n" + //
                                                        "Move across a wild 100-cell board filled with:\n" + //
                                                        "\n" + //
                                                        "\n" + //
                                                        "Energy Doors 🚪\n" + //
                                                        "\n" + //
                                                        "\n" + //
                                                        "Conveyor Belts ⚙️\n" + //
                                                        "\n" + //
                                                        "\n" + //
                                                        "Contamination Socks 🧦\n" + //
                                                        "\n" + //
                                                        "\n" + //
                                                        "Monster Cells 👹\n" + //
                                                        "\n" + //
                                                        "\n" + //
                                                        "Mystery Cards 🎴\n" + //
                                                        "\n" + //
                                                        "\n" + //
                                                        "\n" + //
                                                        "\n" + //
                                                        "Every turn:\n" + //
                                                        "\n" + //
                                                        "\n" + //
                                                        "Optionally activate your monster’s special powerup\n" + //
                                                        "\n" + //
                                                        "\n" + //
                                                        "Roll the dice 🎲\n" + //
                                                        "\n" + //
                                                        "\n" + //
                                                        "Move across the Floor\n" + //
                                                        "\n" + //
                                                        "\n" + //
                                                        "Trigger the effect of the cell you land on\n" + //
                                                        "\n" + //
                                                        "\n" + //
                                                        "\n" + //
                                                        "\n" + //
                                                        "But be careful — one lucky card or dangerous sock can completely flip the game. \n" + //
                                                        "Choose Your Monster Style\n" + //
                                                        "Every monster has a unique personality and strategy:\n" + //
                                                        "\n" + //
                                                        "\n" + //
                                                        "Dashers blaze through the board at incredible speed.\n" + //
                                                        "\n" + //
                                                        "\n" + //
                                                        "Dynamos gain massive energy but suffer massive losses.\n" + //
                                                        "\n" + //
                                                        "\n" + //
                                                        "Multitaskers sacrifice movement for stronger energy control.\n" + //
                                                        "\n" + //
                                                        "\n" + //
                                                        "Schemers manipulate everyone around them and steal energy like pros.\n" + //
                                                        "\n" + //
                                                        "\n" + //
                                                        "Master your monster’s strengths… or get outplayed. ⚡ \n" + //
                                                        "Doors Decide Everything\n" + //
                                                        "Landing on the right door boosts your entire team’s energy.\n" + //
                                                        "Landing on the wrong one? Your whole team pays the price.\n" + //
                                                        "Shields, confusion cards, steals, swaps, and monster abilities create nonstop chaos where no lead is ever safe. 🔥 \n" + //
                                                        "Victory Awaits\n" + //
                                                        "To win, you must:\n" + //
                                                        "\n" + //
                                                        "\n" + //
                                                        "Reach the final cell: Boo’s Door (Cell 99)\n" + //
                                                        "\n" + //
                                                        "\n" + //
                                                        "Have 1000+ energy\n" + //
                                                        "\n" + //
                                                        "\n" + //
                                                        "Outsmart your opponent before they do the same\n" + //
                                                        "\n" + //
                                                        "\n" + //
                                                        "One monster enters the spotlight.\n" + //
                                                        "One monster powers Monstropolis.\n" + //
                                                        "Will you scare… or make them laugh? 🎭");
        instructionstxt.setEditable(false);  // read only
        instructionstxt.setWrapText(true);
        instructionstxt.setStyle("-fx-font-size: 14px;");
        instructionstxt.setMaxWidth(600);
        instructionstxt.setMaxHeight(400);
        instructionpane.getChildren().addAll(instructionTitle,instructionstxt,backinstructions);
        BackgroundImage instBackgroundImage = new BackgroundImage(
        new Image("file:Resources/Images/inst.jpg"), 
        BackgroundRepeat.NO_REPEAT, 
        BackgroundRepeat.NO_REPEAT,
        BackgroundPosition.CENTER, 
        bSize
        
    );
        instructionpane.setBackground( new Background(instBackgroundImage));





        backinstructions.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e){
                stage.getScene().setRoot(layout);;
            }
        });
        back.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e){
                stage.getScene().setRoot(layout);;
            }
        });
        instructionpane.setAlignment(backinstructions, javafx.geometry.Pos.BOTTOM_LEFT);
        instructionpane.setMargin(backinstructions, new Insets(20));

        back.setOnMouseEntered(e -> back.setText("> Go Back"));
        back.setOnMouseExited(e ->  back.setText("Go Back"));



        quit.setOnAction(e -> stage.close() );
        quit.setOnMouseEntered(e -> quit.setText("> Quit :(") );
        quit.setOnMouseExited(e -> quit.setText("Quit"));




    }

   


    

}
