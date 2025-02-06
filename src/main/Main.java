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

    /*** Game Data ***/
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
        showMenuScene(primaryStage);
    }

    /*** Scene Management ***/
    private void showMenuScene(Stage primaryStage) {
        AnchorPane menuRoot = new AnchorPane();
        
        // Start Button
        Button startButton = createButton("Start Game", "rgba(0, 255, 0, 0.7)", 20);
        startButton.setOnAction(e -> showLoadingScene(primaryStage));
        AnchorPane.setTopAnchor(startButton, 250.0);
        AnchorPane.setLeftAnchor(startButton, 130.0);
        menuRoot.getChildren().add(startButton);
        
        // Anime Image
        ImageView animeImageView = createImageView("/resources/animeGirl.jpeg", 450, 648);
        AnchorPane.setRightAnchor(animeImageView, 0.0);
        menuRoot.getChildren().add(animeImageView);

        primaryStage.setScene(new Scene(menuRoot, 968, 648, Color.BLACK));
        primaryStage.setTitle("Visual Novel - Menu");
        primaryStage.show();
    }

    private void showLoadingScene(Stage primaryStage) {
        StackPane loadingRoot = new StackPane();
        loadingRoot.setStyle("-fx-background-color: black;");

        Text loadingText = new Text("Loading...");
        loadingText.setStyle("-fx-font-size: 40px; -fx-fill: white; -fx-font-family: 'Courier New';");
        loadingRoot.getChildren().add(loadingText);

        Timeline loadingTimeline = new Timeline(
            new KeyFrame(Duration.seconds(0.5), e -> loadingText.setOpacity(loadingText.getOpacity() == 1 ? 0 : 1))
        );
        loadingTimeline.setCycleCount(Timeline.INDEFINITE);
        loadingTimeline.play();

        primaryStage.setScene(new Scene(loadingRoot, 968, 648));
        new Timeline(new KeyFrame(Duration.seconds(2), e -> showGameScene(primaryStage))).play();
    }

    private void showGameScene(Stage primaryStage) {
        VBox root = new VBox(10);
        ImageView background = createImageView("/resources/classroomTest.jpg", 968, 486);
        TextArea textBox = createTextArea();
        Button nextButton = createButton("Next", "rgba(255, 0, 0, 0.7)", 16);
        
        nextButton.setOnAction(event -> handleNextText(primaryStage, textBox));
        StackPane textBoxWithButton = new StackPane(textBox, nextButton);
        StackPane.setAlignment(nextButton, Pos.BOTTOM_RIGHT);

        timeline = createTimeline(textBox);
        timeline.play();

        root.getChildren().addAll(background, textBoxWithButton);
        primaryStage.setScene(new Scene(root, 968, 648, Color.BLACK));
    }

    private void showNextScene(Stage primaryStage) {
        StackPane nextSceneRoot = new StackPane();
        nextSceneRoot.setStyle("-fx-background-color: black;");
        Text endText = new Text("End of the Scene...");
        endText.setStyle("-fx-font-size: 40px; -fx-fill: white; -fx-font-family: 'Courier New';");
        nextSceneRoot.getChildren().add(endText);
        primaryStage.setScene(new Scene(nextSceneRoot, 968, 648));
    }

    /*** Helper Functions ***/
    private ImageView createImageView(String path, double width, double height) {
        ImageView imageView = new ImageView(new Image(getClass().getResource(path).toExternalForm()));
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        return imageView;
    }

    private Button createButton(String text, String color, int fontSize) {
        Button button = new Button(text);
        button.setStyle(String.format("-fx-background-color: %s; -fx-text-fill: white; -fx-font-size: %dpx;", color, fontSize));
        return button;
    }

    private TextArea createTextArea() {
        TextArea textBox = new TextArea();
        textBox.setEditable(false);
        textBox.setWrapText(true);
        textBox.setPadding(new Insets(10));
        textBox.setFont(Font.loadFont(getClass().getResourceAsStream("/resources/Prompt-ExtraLight.ttf"), 18));
        textBox.setPrefHeight(162);
        return textBox;
    }

    private void handleNextText(Stage primaryStage, TextArea textBox) {
        if (currentTextIndex < storyTexts.length - 1) {
            currentTextIndex++;
            textBox.clear();
            timeline.stop();
            timeline = createTimeline(textBox);
            timeline.play();
        } else {
            showNextScene(primaryStage);
        }
    }

    private Timeline createTimeline(TextArea textBox) {
        String currentText = storyTexts[currentTextIndex];  // ข้อความที่จะแสดง
        Timeline timeline = new Timeline();
        
        for (int i = 0; i < currentText.length(); i++) {
            final int index = i;  // ต้องใช้ตัวแปร final สำหรับ Lambda
            timeline.getKeyFrames().add(new KeyFrame(Duration.millis(33 * (i + 1)), e -> {
                textBox.appendText(String.valueOf(currentText.charAt(index)));
            }));
        }

        return timeline;  // ไม่ต้อง setCycleCount เพราะจะหยุดเองเมื่อครบข้อความ
    }

    public static void main(String[] args) {
        launch(args);
    }
}