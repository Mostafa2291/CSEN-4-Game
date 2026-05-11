package game.views;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

        HBox roleout = new javafx.scene.layout.HBox(50);
        roleout.setAlignment(javafx.geometry.Pos.CENTER);

        //javafx.scene.Scene rolScene = new javafx.scene.Scene(roleout,800,600);




        Button start = new Button("Start Game!");
        Button quit = new Button("Quit");
        Button controls = new Button("Controls");
        Button laugher = new Button("Laugher");
        Button scarer = new Button("Scarer");
        Button back = new Button("Go back");



        layout.getChildren().addAll(start, controls, quit);

        Scene scene = new Scene(layout, 800, 600);


        roleout.getChildren().addAll(laugher,scarer);

        // start.setBackground(null);
        // quit.setBackground(null);
        // controls.setBackground(null);
        start.setPadding(new Insets(10));
        quit.setPadding(new Insets(10));
        controls.setPadding(new Insets(10));
        back.setPadding(new Insets(10, 20, 10, 20));


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

        start.setOnMouseEntered(e -> start.setText("> Start Game !!"));
        start.setOnMouseExited(e ->  start.setText("Start Game!"));



        back.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e){
                stage.getScene().setRoot(layout);;
            }
        });
        back.setOnMouseEntered(e -> back.setText("> Go Back"));
        back.setOnMouseExited(e ->  back.setText("Go Back"));



        quit.setOnAction(e -> stage.close() );
        quit.setOnMouseEntered(e -> quit.setText("> Quit :(") );
        quit.setOnMouseExited(e -> quit.setText("Quit"));




    }

  


    

}
