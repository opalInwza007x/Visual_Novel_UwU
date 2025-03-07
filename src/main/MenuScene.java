package main;

import javafx.animation.*;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.paint.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.*;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MenuScene {
    private static final String MENU_BUTTON_STYLE = """
            -fx-background-color: rgba(255, 255, 255, 0.1);
            -fx-text-fill: white;
            -fx-border-color: white;
            -fx-border-width: 1px;
            -fx-background-radius: 5px;
            -fx-border-radius: 5px;
            -fx-font-family: 'Helvetica';
            -fx-font-size: 18px;
            -fx-font-weight: bold;
            -fx-cursor: hand;
            """;

    private static final String MENU_BUTTON_HOVER_STYLE = """
            -fx-background-color: rgba(255, 255, 255, 0.3);
            -fx-text-fill: white;
            -fx-border-color: white;
            -fx-border-width: 1px;
            -fx-background-radius: 5px;
            -fx-border-radius: 5px;
            -fx-font-family: 'Helvetica';
            -fx-font-size: 18px;
            -fx-font-weight: bold;
            -fx-cursor: hand;
            -fx-effect: dropshadow(gaussian, rgba(255,255,255,0.5), 10, 0, 0, 0);
            """;

    public void showMenuScene(Stage primaryStage) {
        // Create a StackPane as the root to layer elements
        StackPane menuRoot = new StackPane();

        // Forest Background
        ImageView forestBackground = createImageView("/resources/background/Dark_Forest.jpeg", 968, 648);
        GaussianBlur blur = new GaussianBlur(3);
        forestBackground.setEffect(blur);

        // Semi-transparent overlay for better text readability
        Rectangle overlay = new Rectangle(968, 648);
        overlay.setFill(Color.rgb(0, 0, 0, 0.6));

        // Create main content container
        HBox mainContent = new HBox();
        mainContent.setAlignment(Pos.CENTER);

        // Left side content (Title and Menu)
        VBox leftContent = new VBox(30);
        leftContent.setAlignment(Pos.CENTER_LEFT);
        leftContent.setPadding(new Insets(0, 0, 0, 30));
        leftContent.setMaxWidth(484); // Half of total width

        // Game Title with custom style
        Text gameTitle = new Text("ดูแลผีปอบ\nของนายด้วยนะ");
        gameTitle.setFont(Font.loadFont(getClass().getResourceAsStream("/resources/font/Prompt-ExtraLight.ttf"), 64));
        gameTitle.setStyle("""
                -fx-fill: white;
                -fx-effect: dropshadow(gaussian, black, 20, 0, 0, 0);
                """);
        gameTitle.setWrappingWidth(400);

        // Description text
        Text description = new Text("เมื่อเธอชวนไปกินส้มตำโจะโจ๊ะก่อนเที่ยงคืน มันจะเป็นยังไงกันแน่... หรือว่าความร้อนแรงจะมาเต็มแบบดุดันไม่เกรงใจใคร!");
        description.setFont(Font.loadFont(getClass().getResourceAsStream("/resources/font/Prompt-ExtraLight.ttf"), 18));
        description.setStyle("""
                -fx-fill: white;
                -fx-opacity: 0.8;
                """);
        description.setWrappingWidth(400);

        // Create VBox for menu buttons
        VBox menuButtons = new VBox(20);
        menuButtons.setAlignment(Pos.CENTER_LEFT);
        menuButtons.setPadding(new Insets(10, 0, 0, 0));

        // Create menu buttons
        Button startButton = createMenuButton("Start Game");
        Button exitButton = createMenuButton("Exit");

        menuButtons.getChildren().addAll(startButton, exitButton);

        // Add hover animations
        addButtonAnimations(startButton);
        addButtonAnimations(exitButton);

        // Button actions
        startButton.setOnAction(e -> toNextChapter(primaryStage));
        exitButton.setOnAction(e -> System.exit(0));

        // Add elements to left content
        leftContent.getChildren().addAll(gameTitle, description, menuButtons);

        // Right side content (Anime Girl)
        ImageView animeGirl = createImageView("/resources/arisa/Arisa_shy3_dark.png", 450, 600);
        animeGirl.setPreserveRatio(true);

        // Add gradient overlay to right side for smooth transition
        LinearGradient gradient = new LinearGradient(0, 0, 1, 0, true, CycleMethod.NO_CYCLE,
                new Stop(0, Color.rgb(0, 0, 0, 0.7)), new Stop(1, Color.TRANSPARENT));
        Rectangle gradientOverlay = new Rectangle(100, 648);
        gradientOverlay.setFill(gradient);

        // Add elements to main content
        mainContent.getChildren().addAll(leftContent, animeGirl);

        // Version text
        Text versionText = new Text("v1.0.0");
        versionText.setStyle("""
                -fx-font-family: 'Helvetica';
                -fx-font-size: 14px;
                -fx-fill: white;
                -fx-font-weight: bold;
                """);
        StackPane.setAlignment(versionText, Pos.BOTTOM_RIGHT);
        StackPane.setMargin(versionText, new Insets(0, 10, 10, 0));

        // Add all elements to the root
        menuRoot.getChildren().addAll(forestBackground, overlay, mainContent, versionText);

        // Create and style the scene
        Scene scene = new Scene(menuRoot, 968, 648);
        scene.setFill(Color.BLACK);

        // Configure stage
        primaryStage.setScene(scene);
        primaryStage.setTitle("Visual Novel");
        primaryStage.setResizable(false);
        primaryStage.show();

        // Add fade-in animation
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), menuRoot);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);
        fadeIn.play();
    }

    private Button createMenuButton(String text) {
        Button button = new Button(text);
        button.setPrefWidth(200);
        button.setPrefHeight(40);
        button.setStyle(MENU_BUTTON_STYLE);
        return button;
    }

    private void addButtonAnimations(Button button) {
        button.setOnMouseEntered(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(200), button);
            scale.setToX(1.1);
            scale.setToY(1.1);
            scale.play();
            button.setStyle(MENU_BUTTON_HOVER_STYLE);
        });

        button.setOnMouseExited(e -> {
            ScaleTransition scale = new ScaleTransition(Duration.millis(200), button);
            scale.setToX(1);
            scale.setToY(1);
            scale.play();
            button.setStyle(MENU_BUTTON_STYLE);
        });
    }

    private ImageView createImageView(String path, double width, double height) {
        ImageView imageView = new ImageView(new Image(getClass().getResource(path).toExternalForm()));
        imageView.setFitWidth(width);
        imageView.setFitHeight(height);
        return imageView;
    }

    private void toNextChapter(Stage primaryStage) {
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), primaryStage.getScene().getRoot());
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);

        fadeOut.setOnFinished(e -> new Chapter7().startChapter(primaryStage));

        fadeOut.play();
    }
}