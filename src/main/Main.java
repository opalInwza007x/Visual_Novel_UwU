package main;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {

    private String[] storyTexts = {
        "น้องมีฮัก ฮักมีหน่อย อย่าให้น้องคอย... คอยอยู่ทุกมื่อ    \nLet's Go!!!",
        "มองมาตั้งนาน ส่องเบิ่งตั้งแต่เมื่อวานแล้ะ อ้ายมาแต่ใส เป็นจังได๋ถึงเป็นตาฮักจังซี่ล่ะ",
        "ใจมันตึ๊กตึ๊ก มันคึกมันคักก็มันแพ้ นี่ไม่ได้การและ ต้องเริ่มปฏิบัติการให้รู้ตัวซะบ่",
        "รีบเลย...    \nฟ่าวเลย...    \nบ่มีเวลามาเล่นตาเล่นหู จับไมค์เอื้อนเพลง ให้อ้ายได้รู้...",
    };

    private int currentTextIndex = 0; 
    private Timeline timeline;

    @Override
    public void start(Stage primaryStage) {
        // Menu screen layout using AnchorPane
        AnchorPane menuRoot = new AnchorPane();

        // "Start Game" button
        Button startButton = new Button("Start Game");
        startButton.setStyle("-fx-background-color: rgba(0, 255, 0, 0.7); -fx-text-fill: white; -fx-font-size: 20px; -fx-padding: 10px 20px; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-font-family: 'Courier New';");

        // Set action to show loading scene when clicked
        startButton.setOnAction(e -> showLoadingScene(primaryStage));

        // Positioning the Start Button
        AnchorPane.setTopAnchor(startButton, 250.0);
        AnchorPane.setLeftAnchor(startButton, 510.0);

        // Add the button to the menu layout
        menuRoot.getChildren().add(startButton);

        // Add the anime image at the right of the screen
        Image animeImage = new Image(getClass().getResource("/resources/animeGirl.jpeg").toExternalForm());
        ImageView animeImageView = new ImageView(animeImage);
        animeImageView.setFitHeight(648);
        animeImageView.setFitWidth(300); 
        AnchorPane.setTopAnchor(animeImageView, 0.0);
        AnchorPane.setRightAnchor(animeImageView, 0.0);

        menuRoot.getChildren().add(animeImageView);

        // Set up the menu scene
        Scene menuScene = new Scene(menuRoot, 968, 648);
        menuScene.setFill(Color.BLACK);
        primaryStage.setTitle("Visual Novel - Menu");
        primaryStage.setScene(menuScene);
        primaryStage.show();
    }

    // Function to show loading scene before the game scene
    private void showLoadingScene(Stage primaryStage) {
        // Create a new root layout for the loading screen
        StackPane loadingRoot = new StackPane();
        loadingRoot.setStyle("-fx-background-color: black;");

        // Create a loading text animation
        Text loadingText = new Text("Loading...");
        loadingText.setStyle("-fx-font-size: 40px; -fx-fill: white; -fx-font-family: 'Courier New';");
        loadingRoot.getChildren().add(loadingText);

        // Animate the loading text (making it blink)
        Timeline loadingTimeline = new Timeline(
            new KeyFrame(Duration.seconds(0.5), e -> loadingText.setOpacity(loadingText.getOpacity() == 1 ? 0 : 1))
        );
        loadingTimeline.setCycleCount(Timeline.INDEFINITE);
        loadingTimeline.play();

        // Set up the scene for loading screen
        Scene loadingScene = new Scene(loadingRoot, 968, 648);
        primaryStage.setScene(loadingScene);

        // Simulate a delay before transitioning to the game scene
        Timeline delayTimeline = new Timeline(new KeyFrame(Duration.seconds(2), e -> showGameScene(primaryStage)));
        delayTimeline.play();
    }

    // Function to show the game scene (visual novel)
    private void showGameScene(Stage primaryStage) {
        VBox root = new VBox(10);

        Image backgroundImage = new Image(getClass().getResource("/resources/classroomTest.jpg").toExternalForm());
        ImageView background = new ImageView(backgroundImage);
        background.setFitWidth(968);
        background.setFitHeight(486);

        TextArea textBox = new TextArea();
        textBox.setEditable(false);
        textBox.setText(storyTexts[currentTextIndex]);
        textBox.setWrapText(true);
        textBox.setPadding(new Insets(10));
        Font font = Font.loadFont(getClass().getResourceAsStream("/resources/Prompt-ExtraLight.ttf"), 18);
        textBox.setFont(font);
        textBox.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7); -fx-text-fill: black; -fx-font-size: 25px;");
        textBox.setPrefHeight(162);

        Button nextButton = new Button("Next");
        nextButton.setStyle("-fx-background-color: rgba(255, 0, 0, 0.7); -fx-text-fill: white; -fx-font-size: 16px; -fx-padding: 10px 20px; -fx-border-radius: 5px; -fx-background-radius: 5px; -fx-font-family: 'Courier New';");

        StackPane textBoxWithButton = new StackPane();
        textBoxWithButton.getChildren().add(textBox);
        StackPane.setAlignment(nextButton, Pos.BOTTOM_RIGHT);
        textBoxWithButton.getChildren().add(nextButton);

        timeline = createTimeline(textBox);
        timeline.play();

        nextButton.setOnAction(event -> {
            currentTextIndex = (currentTextIndex + 1) % storyTexts.length;
            textBox.clear();
            timeline.stop();
            timeline = createTimeline(textBox);
            timeline.play();
        });

        root.getChildren().addAll(background, textBoxWithButton);

        Scene gameScene = new Scene(root, 968, 648);
        gameScene.setFill(Color.BLACK);
        primaryStage.setTitle("Visual Novel");
        primaryStage.setScene(gameScene);
    }

    private Timeline createTimeline(TextArea textBox) {
        Timeline timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);

        timeline.getKeyFrames().add(new KeyFrame(Duration.millis(33), e -> {
            String currentText = storyTexts[currentTextIndex];
            int length = textBox.getText().length();
            
            if (length < currentText.length()) {
                char nextChar = currentText.charAt(length);
                textBox.appendText(String.valueOf(nextChar));
                
                // Check if the current character is a space
                if (nextChar == ' ') {
                    timeline.stop();  // Stop the current typing animation
                    
                    // Introduce a delay (0.5 seconds) after space
                    Timeline delayTimeline = new Timeline(new KeyFrame(Duration.seconds(0.33), ev -> {
                        // Continue with the typing after the delay
                        timeline.play();
                    }));
                    delayTimeline.setCycleCount(1);
                    delayTimeline.play();
                }
            } else {
                timeline.stop();  // Stop when the text is fully displayed
            }
        }));

        return timeline;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
