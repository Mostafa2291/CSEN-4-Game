package game.views;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import java.util.Optional;

// Animation & Audio & Video Imports
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.util.Duration;

// Drawing Shapes for Arrows
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;

// Effect for Tinting Doors
import javafx.scene.effect.ColorAdjust;

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
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*; 
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {

    private ArrayList<HBox> monsterContainers = new ArrayList<>();
    private int previousPlayerPos = 0;
    private int previousOpponentPos = 0;
    private ImageView[] doorImageViews;

    private Label diceRes;
    private Label currentturnLabel;
    private Label playerStatusLabel;
    private Label opponentStatusLabel;
    private Scene mainMenuScene;
    
    private StackPane layout; 
    
    private double screenWidth;
    private double screenHeight;
    private static Stage stage;
    private static Game myGame;

    // Global Music & Video Players
    private static MediaPlayer menuMusicPlayer;
    private static MediaPlayer gameMusicPlayer;
    private static MediaPlayer videoPlayer;
    
    // Easter Egg Tracker
    private boolean gameBeaten = false;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Main.stage = stage;

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

        // Music Toggle Button
        Button toggleMusicBtn   = new Button("🔊 Mute Music");

        // Padding
        start.setPadding(new Insets(10));
        quit.setPadding(new Insets(10));
        controls.setPadding(new Insets(10));
        instructions.setPadding(new Insets(10));
        toggleMusicBtn.setPadding(new Insets(10));
        back.setPadding(new Insets(10, 20, 10, 20));
        backInstructions.setPadding(new Insets(10, 20, 10, 20));

        // ── LOAD MENU & GAME MUSIC ────────────────────────────────────────────
        try {
            String menuMusicPath = new java.io.File("Resources/Audio/menu_music.wav").getAbsolutePath();
            Media menuMedia = new Media(new java.io.File(menuMusicPath).toURI().toString());
            menuMusicPlayer = new MediaPlayer(menuMedia);
            menuMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE); 
            menuMusicPlayer.setVolume(0.3); 
            menuMusicPlayer.play();
        } catch (Throwable ex) {
            System.out.println("Menu music failed to load. Ensure menu_music.wav is in Resources/Audio/");
        }
        
        try {
            String gameMusicPath = new java.io.File("Resources/Audio/game_music.wav").getAbsolutePath();
            Media gameMedia = new Media(new java.io.File(gameMusicPath).toURI().toString());
            gameMusicPlayer = new MediaPlayer(gameMedia);
            gameMusicPlayer.setCycleCount(MediaPlayer.INDEFINITE); 
            gameMusicPlayer.setVolume(0.3);
        } catch (Throwable ex) {
            System.out.println("Game music failed to load. Ensure game_music.wav is in Resources/Audio/");
        }

        // Toggle Menu Music Action
        toggleMusicBtn.setOnAction(e -> {
            if (menuMusicPlayer != null) {
                boolean isMuted = menuMusicPlayer.isMute();
                menuMusicPlayer.setMute(!isMuted);
                toggleMusicBtn.setText(!isMuted ? "🔇 Unmute Music" : "🔊 Mute Music");
            }
        });

        // ── Main Menu ─────────────────────────────────────────────────────────
        VBox menuButtons = new VBox(15);
        menuButtons.setAlignment(Pos.CENTER);
        menuButtons.getChildren().addAll(start, instructions, quit);
        
        layout = new StackPane();
        
        StackPane.setAlignment(toggleMusicBtn, Pos.BOTTOM_LEFT);
        StackPane.setMargin(toggleMusicBtn, new Insets(20));

        // ── EASTER EGG VIDEO SETUP (Hidden) ──
        StackPane videoOverlay = new StackPane();
        videoOverlay.setStyle("-fx-background-color: black;");
        videoOverlay.setVisible(false);

        MediaView videoView = new MediaView();
        videoView.fitWidthProperty().bind(layout.widthProperty().multiply(0.8));
        videoView.fitHeightProperty().bind(layout.heightProperty().multiply(0.8));
        videoView.setPreserveRatio(true);

        Button closeVideoBtn = new Button("Close Video");
        closeVideoBtn.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        StackPane.setAlignment(closeVideoBtn, Pos.TOP_RIGHT);
        StackPane.setMargin(closeVideoBtn, new Insets(20));

        videoOverlay.getChildren().addAll(videoView, closeVideoBtn);

        closeVideoBtn.setOnAction(e -> {
            if (videoPlayer != null) videoPlayer.stop();
            videoOverlay.setVisible(false);
            if (menuMusicPlayer != null && !menuMusicPlayer.isMute()) menuMusicPlayer.play();
        });

        // Add all elements to the Main Menu layout
        layout.getChildren().addAll(menuButtons, toggleMusicBtn, videoOverlay);

        BackgroundImage menuBG = new BackgroundImage(
            new Image("file:Resources/Images/background2.jpg"),
            BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER, bSize
        );
        layout.setBackground(new Background(menuBG));

        // ── Role Select Screen ────────────────────────────────────────────────
        StackPane roleSelectRoot = new StackPane();

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

        // ── GET MONITOR DIMENSIONS ──
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        screenWidth = screenBounds.getWidth() * 0.80; 
        screenHeight = screenBounds.getHeight() * 0.80;

        // ── INTERACTIVE INSTRUCTIONS CUTSCENE ─────────────────────────────────
        Pane interactiveInstructionPane = new Pane();
        interactiveInstructionPane.setStyle("-fx-background-color: black;");

        ImageView instBg = new ImageView(new Image("file:Resources/Images/mike_sulley.jpg"));
        instBg.fitWidthProperty().bind(stage.widthProperty());
        instBg.fitHeightProperty().bind(stage.heightProperty());
        instBg.setPreserveRatio(false);

        Label sulleyBubble = new Label();
        sulleyBubble.setStyle("-fx-background-color: white; -fx-padding: 20px; -fx-background-radius: 20px; -fx-border-radius: 20px; -fx-border-color: #333; -fx-border-width: 4px; -fx-font-size: 18px; -fx-font-weight: bold;");
        sulleyBubble.setWrapText(true);
        sulleyBubble.setMaxWidth(400);
        sulleyBubble.layoutXProperty().bind(stage.widthProperty().multiply(0.05));
        sulleyBubble.layoutYProperty().bind(stage.heightProperty().multiply(0.10));
        sulleyBubble.setVisible(false);

        Label mikeBubble = new Label();
        mikeBubble.setStyle("-fx-background-color: white; -fx-padding: 20px; -fx-background-radius: 20px; -fx-border-radius: 20px; -fx-border-color: #333; -fx-border-width: 4px; -fx-font-size: 18px; -fx-font-weight: bold;");
        mikeBubble.setWrapText(true);
        mikeBubble.setMaxWidth(400);
        mikeBubble.layoutXProperty().bind(stage.widthProperty().multiply(0.55));
        mikeBubble.layoutYProperty().bind(stage.heightProperty().multiply(0.40));
        mikeBubble.setVisible(false);

        Label continuePrompt = new Label("Press [ENTER] to continue... Press [ESC] to skip");
        continuePrompt.setStyle("-fx-text-fill: white; -fx-font-size: 20px; -fx-font-weight: bold; -fx-background-color: rgba(0,0,0,0.6); -fx-padding: 10px;");
        continuePrompt.setLayoutX(20);
        continuePrompt.layoutYProperty().bind(stage.heightProperty().subtract(80));

        backInstructions.setLayoutX(20);
        backInstructions.setLayoutY(20);

        interactiveInstructionPane.getChildren().addAll(instBg, sulleyBubble, mikeBubble, continuePrompt);
        Scene instructionScene = new Scene(interactiveInstructionPane, screenWidth, screenHeight);

        String[][] dialogues = {
            {"MIKE", "Welcome to DooR DasH: Scare vs Laugh Touchdown! 👾⚡"},
            {"SULLEY", "Step onto the Floor of Monstropolis... reach Boo's Door with 1000 energy before your rival does."},
            {"MIKE", "Move across a 100-cell board! Watch out for Energy Doors, Conveyor Belts, and... ugh... Contamination Socks."},
            {"SULLEY", "Every turn, you can use a powerup, roll the dice, and move. But beware, cards and socks change everything."},
            {"MIKE", "Pick your style! Dashers are fast, Dynamos get huge energy, Multitaskers balance it, and Schemers... well, they scheme."},
            {"SULLEY", "To win, hit Boo's Door at cell 99 with 1000+ energy. Will you scare… or make them laugh? also, i heard something weird happens if you press CTRL + F after winning a game...."}
        };

        int[] state = {0}; 
        boolean[] isTyping = {false};
        String[] fullCurrentText = {""};
        Timeline[] typingTimeline = {new Timeline()};
        Random randAudio = new Random(); 
        
        MediaPlayer tempPlayer = null;
        try {
            String absolutePath = new java.io.File("Resources/Audio/mumble.wav").getAbsolutePath();
            Media sound = new Media(new java.io.File(absolutePath).toURI().toString());
            tempPlayer = new MediaPlayer(sound);
            tempPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        } catch (Throwable ex) {
            System.out.println("AUDIO FAILED TO LOAD:");
        }
        final MediaPlayer finalMumble = tempPlayer;

        Runnable playNextDialogue = () -> {
            if (state[0] >= dialogues.length) {
                if (finalMumble != null) finalMumble.stop();
                if (menuMusicPlayer != null && !menuMusicPlayer.isMute()) menuMusicPlayer.play(); // <-- RESUME MUSIC
                stage.setScene(mainMenuScene); 
                return;
            }

            isTyping[0] = true;
            String speaker = dialogues[state[0]][0];
            fullCurrentText[0] = dialogues[state[0]][1];

            Label activeBubble = speaker.equals("MIKE") ? mikeBubble : sulleyBubble;
            Label inactiveBubble = speaker.equals("MIKE") ? sulleyBubble : mikeBubble;

            inactiveBubble.setVisible(false);
            activeBubble.setVisible(true);
            activeBubble.setText(""); 

            if (finalMumble != null) {
                finalMumble.stop();
                double randomStartTime = randAudio.nextDouble() * 20.0;
                finalMumble.seek(Duration.seconds(randomStartTime));

                if (speaker.equals("MIKE")) {
                    finalMumble.setRate(1.6);
                } else {
                    finalMumble.setRate(1.0);
                }
                finalMumble.play();
            }

            typingTimeline[0].stop();
            typingTimeline[0].getKeyFrames().clear();

            for (int i = 0; i < fullCurrentText[0].length(); i++) {
                final int charIndex = i;
                KeyFrame frame = new KeyFrame(Duration.millis(35 * i), e -> {
                    activeBubble.setText(fullCurrentText[0].substring(0, charIndex + 1));
                });
                typingTimeline[0].getKeyFrames().add(frame);
            }

            KeyFrame endFrame = new KeyFrame(Duration.millis(35 * fullCurrentText[0].length()), e -> {
                isTyping[0] = false;
                if (finalMumble != null) finalMumble.pause(); 
            });
            typingTimeline[0].getKeyFrames().add(endFrame);
            typingTimeline[0].play();
        };

        // Handle Keys for Instructions Scene
        instructionScene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                if (isTyping[0]) {
                    typingTimeline[0].stop();
                    Label activeBubble = dialogues[state[0]][0].equals("MIKE") ? mikeBubble : sulleyBubble;
 
                    activeBubble.setText(fullCurrentText[0]);
                    isTyping[0] = false;
                    if (finalMumble != null) finalMumble.pause();
                } else {
                 
                    state[0]++;
                    playNextDialogue.run();
                }
            } else if (event.getCode() == KeyCode.ESCAPE) {
                 typingTimeline[0].stop();
                 if (finalMumble != null) finalMumble.stop(); 
                 if (menuMusicPlayer != null && !menuMusicPlayer.isMute()) menuMusicPlayer.play(); // <-- RESUME MUSIC
                 stage.setScene(mainMenuScene);
            } else if (event.getCode() == KeyCode.F11) {
                 stage.setFullScreen(!stage.isFullScreen());
            }
        });

        // ── Scene Setup ───────────────────────────────────────────────────────
        mainMenuScene = new Scene(layout, screenWidth, screenHeight);

        // Main Menu Key Handlers (F11 and the Ctrl+F Easter Egg)
        mainMenuScene.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.F11) {
                stage.setFullScreen(!stage.isFullScreen());
            } 
            // ── HIDDEN EASTER EGG (CTRL + F) ──
            else if (event.isControlDown() && event.getCode() == KeyCode.F) {
                if (gameBeaten) { // Only works if they have completed a game!
                    try {
                        if (menuMusicPlayer != null) menuMusicPlayer.pause();
                    
                        String videoPath = new java.io.File("Resources/Video/whatisthis.mp4").getAbsolutePath();
                        Media videoMedia = new Media(new java.io.File(videoPath).toURI().toString());
                        
                        if (videoPlayer != null) {
                            videoPlayer.stop();
                        }
                        
                        videoPlayer = new MediaPlayer(videoMedia);
                        videoView.setMediaPlayer(videoPlayer);
                        videoOverlay.setVisible(true);
                        videoPlayer.play();

                        // Auto-close when the video finishes
                        videoPlayer.setOnEndOfMedia(() -> {
                            videoOverlay.setVisible(false);
                            if (menuMusicPlayer != null && !menuMusicPlayer.isMute()) menuMusicPlayer.play();
                         });
                    } catch (Exception ex) {
                        System.out.println("Video failed to load. Make sure whatisthis.mp4 is inside Resources/Video/");
                    }
                }
            }
        });

        stage.setTitle("DooR DasH: Scare vs Laugh");
        stage.setScene(mainMenuScene);
        stage.centerOnScreen(); 
        stage.show();

        // ── Button Actions ────────────────────────────────────────────────────
        start.setOnAction(e -> stage.getScene().setRoot(roleSelectRoot));
        start.setOnMouseEntered(e -> start.setText("> Start Game !!"));
        start.setOnMouseExited(e  -> start.setText("Start Game!"));

        instructions.setOnAction(e -> {
            sulleyBubble.setVisible(false);
            mikeBubble.setVisible(false);
            if (menuMusicPlayer != null) menuMusicPlayer.pause(); // <-- PAUSE MUSIC
            stage.setScene(instructionScene);
            state[0] = 0;
            playNextDialogue.run(); 
        });

        instructions.setOnMouseEntered(e -> instructions.setText("Game Instructions"));
        instructions.setOnMouseExited(e  -> instructions.setText("Instructions"));

        back.setOnAction(e -> stage.getScene().setRoot(layout));
        back.setOnMouseEntered(e -> back.setText("> Go Back"));
        back.setOnMouseExited(e  -> back.setText("Go Back"));

        backInstructions.setOnAction(e -> {
            if (finalMumble != null) finalMumble.stop();
            if (typingTimeline[0] != null) typingTimeline[0].stop();
            if (menuMusicPlayer != null && !menuMusicPlayer.isMute()) menuMusicPlayer.play(); // <-- RESUME MUSIC
            stage.setScene(mainMenuScene);
            mainMenuScene.setRoot(layout);
        });

        quit.setOnAction(e -> stage.close());
        quit.setOnMouseEntered(e -> quit.setText("> Quit :("));
        quit.setOnMouseExited(e  -> quit.setText("Quit"));

        laugher.setOnAction(e -> {
            try{
                if (menuMusicPlayer != null) menuMusicPlayer.stop(); 
                if (gameMusicPlayer != null) {
                   
                    gameMusicPlayer.seek(Duration.ZERO); 
                    gameMusicPlayer.play();
                }
                
                myGame = new Game(Role.LAUGHER);
                System.out.println("Game loaded :D");
         
                startGameBoard();
            } catch(IOException ex){
                System.out.println("Failed to Load game ");
            }
        } );

        scarer.setOnAction(e ->{
             try{
                
                if (menuMusicPlayer != null) menuMusicPlayer.stop(); 
                if (gameMusicPlayer != null) {
                    gameMusicPlayer.seek(Duration.ZERO); 
                    gameMusicPlayer.play();
                }
                
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
            cssColor = "GreenYellow";
        }
        else if (modelCell instanceof DoorCell){
            // ── CHANGED: Cell background turns Green for Laugher doors and Red for Scarer doors! ──
            if (((DoorCell) modelCell).getRole() == Role.LAUGHER) {
                cssColor = "palegreen";
            } else {
                cssColor = "lightsalmon";
            }
        }
        else {
            cssColor = "papayawhip";
        }

        pane.setStyle("-fx-background-color: " + cssColor + "; -fx-border-color: black; -fx-border-width: 0.5px;");
        pane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        pane.setMinSize(0, 0); 
        
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
        if (doorImageViews != null && doorImageViews[player.getPosition()] != null) {
            doorImageViews[player.getPosition()].setImage(new Image("file:Resources/Images/open door.jpeg"));
        }
        if (doorImageViews != null && doorImageViews[opponent.getPosition()] != null) {
            doorImageViews[opponent.getPosition()].setImage(new Image("file:Resources/Images/open door.jpeg"));
        }

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
         else if(myGame.getPlayer().getName().equals("Henry J. Waternoose")){
             pRect.setFill(Color.WHITE);
             ImageView playeri = new ImageView(new Image("file:Resources/Images/Henry J.Waternoose.jpg"));
            pImage.setFill(new ImagePattern(playeri.getImage()));
        }
        else if(myGame.getPlayer().getName().equals("Yeti")){
             pRect.setFill(Color.WHITE);
             ImageView playeri = new ImageView(new Image("file:Resources/Images/Yeti.jpg"));
            pImage.setFill(new ImagePattern(playeri.getImage()));
        }

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
         else if(myGame.getOpponent().getName().equals("Henry J. Waternoose")){
             oRect.setFill(Color.WHITE);
             ImageView opponenti = new ImageView(new Image("file:Resources/Images/Henry J.Waternoose.jpg"));
            oImage.setFill(new ImagePattern(opponenti.getImage()));
        }
        else if(myGame.getOpponent().getName().equals("Yeti")){
             oRect.setFill(Color.WHITE);
             ImageView opponenti = new ImageView(new Image("file:Resources/Images/Yeti.jpg"));
            oImage.setFill(new ImagePattern(opponenti.getImage()));
        }

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
        ArrayList<String> availableImages = new ArrayList<>();
        availableImages.add("file:Resources/Images/Roz.jpg");
        availableImages.add("file:Resources/Images/Fungus.jpg");
        availableImages.add("file:Resources/Images/Henry J.Waternoose.jpg");
        availableImages.add("file:Resources/Images/Yeti.jpg");
        availableImages.add("file:Resources/Images/Mike Wazowski.jpg");
        availableImages.add("file:Resources/Images/James p.Sullivan.jpg");
        availableImages.add("file:Resources/Images/Randall Boggs.jpg");
        availableImages.add("file:Resources/Images/Celia Mae.jpg");
        String pName = myGame.getPlayer().getName();
        String oName = myGame.getOpponent().getName();
        String pFile = pName.equals("James P. Sullivan") ?
        "James p.Sullivan" : 
                       (pName.equals("Henry J. Waternoose") ? "Henry J.Waternoose" : pName);
        String oFile = oName.equals("James P. Sullivan") ? "James p.Sullivan" : 
                       (oName.equals("Henry J. Waternoose") ? "Henry J.Waternoose" : oName);
        availableImages.remove("file:Resources/Images/" + pFile + ".jpg");
        availableImages.remove("file:Resources/Images/" + oFile + ".jpg");
        
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
        doorImageViews = new ImageView[Constants.BOARD_SIZE];
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

                if(backendCell instanceof MonsterCell){
                    if (!availableImages.isEmpty()) {
                        Random rand = new Random();
                        String chosenPath = availableImages.get(rand.nextInt(availableImages.size()));
                        Image chosen = new Image(chosenPath);
                        ImageView monsterImage = new ImageView(chosen);
                        monsterImage.fitWidthProperty().bind(grid.widthProperty().divide(Constants.BOARD_COLS).multiply(0.6));
                        monsterImage.fitHeightProperty().bind(grid.heightProperty().divide(Constants.BOARD_ROWS).multiply(0.6));
                        uiCell.getChildren().add(monsterImage);
                        uiCell.setAlignment(monsterImage, Pos.CENTER);
                        availableImages.remove(chosenPath);
                    }
                }
                

                if(backendCell instanceof CardCell){
                    ImageView cardImage = new ImageView(new Image("file:Resources/Images/Card.jpg"));
                    cardImage.fitWidthProperty().bind(grid.widthProperty().divide(Constants.BOARD_COLS).multiply(0.6));
                    cardImage.fitHeightProperty().bind(grid.heightProperty().divide(Constants.BOARD_ROWS).multiply(0.6));
                    uiCell.getChildren().add(cardImage);
                    uiCell.setAlignment(cardImage,Pos.CENTER);
                }

                if(backendCell instanceof ConveyorBelt){
                    ImageView conveyorImage = new ImageView(new Image("file:Resources/Images/Conveyer.jpg"));
                    conveyorImage.fitWidthProperty().bind(grid.widthProperty().divide(Constants.BOARD_COLS).multiply(0.6));
                    conveyorImage.fitHeightProperty().bind(grid.heightProperty().divide(Constants.BOARD_ROWS).multiply(0.6));
                    uiCell.getChildren().add(conveyorImage);
                    uiCell.setAlignment(conveyorImage,Pos.CENTER);
                }
                if(backendCell instanceof ContaminationSock){
                    ImageView sockImage = new ImageView(new Image("file:Resources/Images/Sock.jpg"));
                    sockImage.fitWidthProperty().bind(grid.widthProperty().divide(Constants.BOARD_COLS).multiply(0.6));
                    sockImage.fitHeightProperty().bind(grid.heightProperty().divide(Constants.BOARD_ROWS).multiply(0.6));
                    uiCell.getChildren().add(sockImage);
                    uiCell.setAlignment(sockImage,Pos.CENTER);
                }

                if (backendCell instanceof DoorCell) {
                    Label energyLabel = new Label(((DoorCell) backendCell).getEnergy() + " E");
                    energyLabel.setStyle("-fx-font-size: 10px;");
                    StackPane.setAlignment(energyLabel, Pos.CENTER);
                    uiCell.getChildren().add(energyLabel);
                    
                    ImageView closedDoorImage = new ImageView(new Image("file:Resources/Images/closed door.jpeg"));
                    
                    ColorAdjust doorTint = new ColorAdjust();
                    if (((DoorCell) backendCell).getRole() == Role.LAUGHER) {
                        doorTint.setHue(0.4);
                    } else {
                        doorTint.setHue(-0.3);
                    }
                    closedDoorImage.setEffect(doorTint);
                    
                    closedDoorImage.fitWidthProperty().bind(grid.widthProperty().divide(Constants.BOARD_COLS).multiply(0.3));
                    closedDoorImage.fitHeightProperty().bind(grid.heightProperty().divide(Constants.BOARD_ROWS).multiply(0.3));
                    
                    uiCell.getChildren().add(closedDoorImage);
                    uiCell.setAlignment(closedDoorImage,Pos.BOTTOM_RIGHT);
                    doorImageViews[index] = closedDoorImage;
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
                        // ── TRIGGER EASTER EGG UNLOCK ──
                        gameBeaten = true;
                        
                        if (gameMusicPlayer != null) gameMusicPlayer.stop(); // Stop game music
                        if (menuMusicPlayer != null && !menuMusicPlayer.isMute()) menuMusicPlayer.play(); // Resume menu music
                        
                        mainMenuScene.setRoot(layout);
                        stage.setScene(mainMenuScene);
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
  
        VBox pauseOverlay  = new VBox();
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
            if (gameMusicPlayer != null) gameMusicPlayer.stop(); // Stop game music
            if (menuMusicPlayer != null && !menuMusicPlayer.isMute()) menuMusicPlayer.play(); // Resume menu music
            
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
        // ── ARROW DRAWING LAYER ──
        StackPane centerStack = new StackPane();
        Pane arrowPane = new Pane();
        arrowPane.setMouseTransparent(true);
        
        int[] conveyorStarts = {6, 22, 44, 52, 66};
        int[] conveyorEffects = {25, 15, 11, 10, 2};
        for(int i=0; i<conveyorStarts.length; i++) {
            drawArrow(arrowPane, grid, conveyorStarts[i], conveyorStarts[i] + conveyorEffects[i], true);
        }
        
        int[] sockStarts = {32, 42, 74, 84, 98};
        int[] sockEffects = {-6, -12, -10, -14, -22};
        for(int i=0; i<sockStarts.length; i++) {
            drawArrow(arrowPane, grid, sockStarts[i], sockStarts[i] + sockEffects[i], false);
        }

        centerStack.getChildren().addAll(grid, arrowPane);
        root.setCenter(centerStack);
        
        root.setBottom(controlsBox);

        VBox leftSidebar = new VBox(10);
        leftSidebar.setPadding(new Insets(15));
        leftSidebar.prefWidthProperty().bind(root.widthProperty().multiply(0.20));
        leftSidebar.setStyle("-fx-background-color: #e0f7fa; -fx-border-color: lightgray; -fx-border-width: 0 1 0 0;");
        Label pTitle = new Label("🎮 PLAYER");
        pTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        playerStatusLabel = new Label();
        playerStatusLabel.setStyle("-fx-font-size: 14px; -fx-line-spacing: 5px;");
        playerStatusLabel.setWrapText(true);
        leftSidebar.getChildren().addAll(pTitle, playerStatusLabel);
        
        VBox rightSidebar = new VBox(10);
        rightSidebar.setPadding(new Insets(15));
        rightSidebar.prefWidthProperty().bind(root.widthProperty().multiply(0.20));
        rightSidebar.setStyle("-fx-background-color: #fce4ec; -fx-border-color: lightgray; -fx-border-width: 0 0 0 1;");
        Label oTitle = new Label("👾 OPPONENT");
        oTitle.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        opponentStatusLabel = new Label();
        opponentStatusLabel.setStyle("-fx-font-size: 14px; -fx-line-spacing: 5px;");
        opponentStatusLabel.setWrapText(true);
        rightSidebar.getChildren().addAll(oTitle, opponentStatusLabel);
        
        VBox topCenterBox = new VBox(5);
        topCenterBox.setAlignment(Pos.CENTER);
        topCenterBox.setPadding(new Insets(10));
        currentturnLabel = new Label("Current Turn: " + myGame.getCurrent().getName());
        currentturnLabel.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
        diceRes = new Label("Rolled: -");
        diceRes.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: darkred;");
        topCenterBox.getChildren().addAll(currentturnLabel, diceRes);

        root.setLeft(leftSidebar);
        root.setRight(rightSidebar);
        root.setTop(topCenterBox);
        // ── GAME MUSIC TOGGLE BUTTON ──
        Button toggleGameMusicBtn = new Button("🔊 Mute Music");
        toggleGameMusicBtn.setStyle("-fx-font-size: 12px; -fx-font-weight: bold;");
        
        toggleGameMusicBtn.setOnAction(e -> {
            if (gameMusicPlayer != null) {
                boolean isMuted = gameMusicPlayer.isMute();
                gameMusicPlayer.setMute(!isMuted);
                toggleGameMusicBtn.setText(!isMuted ? "🔇 Unmute Music" : "🔊 Mute Music");
            }
       
         });
        
        StackPane basePane = new StackPane();
        StackPane.setAlignment(toggleGameMusicBtn, Pos.BOTTOM_LEFT);
        StackPane.setMargin(toggleGameMusicBtn, new Insets(20));
        
        basePane.getChildren().addAll(root, toggleGameMusicBtn, pauseOverlay);
        Scene boardScene = new Scene(basePane, screenWidth, screenHeight);
        stage.setScene(boardScene);
        
        // ── CHEAT CODES & PAUSE TOGGLE ──
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
                        // ── TRIGGER EASTER EGG UNLOCK ──
               
                         gameBeaten = true;
                        
                        if (gameMusicPlayer != null) gameMusicPlayer.stop(); // Stop game music
                        if (menuMusicPlayer != null && !menuMusicPlayer.isMute()) menuMusicPlayer.play();
                        // Resume menu music
                        
                        mainMenuScene.setRoot(layout);
                        stage.setScene(mainMenuScene);
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
                        // ── TRIGGER EASTER EGG UNLOCK ──
                        gameBeaten = true;
                        
                        if (gameMusicPlayer != null) gameMusicPlayer.stop();
                        // Stop game music
                        if (menuMusicPlayer != null && !menuMusicPlayer.isMute()) menuMusicPlayer.play();
                        // Resume menu music
                        
                        mainMenuScene.setRoot(layout);
                        stage.setScene(mainMenuScene);
                    }
                    else if(result.isPresent() && result.get() == gameQuit){
                        stage.close();
                    }
                }
            }
            else if (event.getCode() == KeyCode.ESCAPE){
                pauseOverlay.setVisible(!pauseOverlay.isVisible());
            }
            else if (event.getCode() == KeyCode.F11) {
                stage.setFullScreen(!stage.isFullScreen());
            }
        });

        // Call update monsters for initial starting pos 
        updateMonsters();
    }
    
    // ── METHOD TO DRAW DYNAMIC ARROWS ACROSS THE BOARD ──
    private void drawArrow(Pane pane, GridPane grid, int startIndex, int endIndex, boolean isConveyor) {
        int startRow = startIndex / Constants.BOARD_COLS;
        int startCol = (startRow % 2 == 1) ? (Constants.BOARD_COLS - 1) - (startIndex % Constants.BOARD_COLS) : startIndex % Constants.BOARD_COLS;
        int startGridRow = (Constants.BOARD_ROWS - 1) - startRow;

        int endRow = endIndex / Constants.BOARD_COLS;
        int endCol = (endRow % 2 == 1) ? (Constants.BOARD_COLS - 1) - (endIndex % Constants.BOARD_COLS) : endIndex % Constants.BOARD_COLS;
        int endGridRow = (Constants.BOARD_ROWS - 1) - endRow;

        Line line = new Line();
        line.setStrokeWidth(5);
        line.setStroke(isConveyor ? Color.SPRINGGREEN : Color.CRIMSON);
        line.setOpacity(0.8);
        line.getStrokeDashArray().addAll(10d, 10d); // Dashed lines so it looks like a track

        line.startXProperty().bind(grid.widthProperty().multiply((startCol + 0.5) / Constants.BOARD_COLS));
        line.startYProperty().bind(grid.heightProperty().multiply((startGridRow + 0.5) / Constants.BOARD_ROWS));
        line.endXProperty().bind(grid.widthProperty().multiply((endCol + 0.5) / Constants.BOARD_COLS));
        line.endYProperty().bind(grid.heightProperty().multiply((endGridRow + 0.5) / Constants.BOARD_ROWS));

        Polygon arrowhead = new Polygon();
        arrowhead.setFill(isConveyor ? Color.SPRINGGREEN : Color.CRIMSON);
        arrowhead.setOpacity(0.8);

        Runnable updateArrowHead = () -> {
            double sx = line.getStartX();
            double sy = line.getStartY();
            double ex = line.getEndX();
            double ey = line.getEndY();
            if (sx == ex && sy == ey) return;
            
            double angle = Math.atan2(ey - sy, ex - sx);
            double pullBack = 15.0; // Keeps arrowhead from covering cell numbers
            double targetX = ex - pullBack * Math.cos(angle);
            double targetY = ey - pullBack * Math.sin(angle);
            
            double size = 15.0;
            double angle1 = angle - Math.PI / 6.0; 
            double angle2 = angle + Math.PI / 6.0; 
            
            double p1x = targetX - size * Math.cos(angle1);
            double p1y = targetY - size * Math.sin(angle1);
            
            double p2x = targetX - size * Math.cos(angle2);
            double p2y = targetY - size * Math.sin(angle2);
            
            arrowhead.getPoints().setAll(
                targetX, targetY,
                p1x, p1y,
                p2x, p2y
            );
        };
        
        line.startXProperty().addListener((obs, oldV, newV) -> updateArrowHead.run());
        line.startYProperty().addListener((obs, oldV, newV) -> updateArrowHead.run());
        line.endXProperty().addListener((obs, oldV, newV) -> updateArrowHead.run());
        line.endYProperty().addListener((obs, oldV, newV) -> updateArrowHead.run());

        pane.getChildren().addAll(line, arrowhead);
    }
}
