package main;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.ParallelTransition;
import javafx.animation.PauseTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.RadialGradient;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import Util.Chapter;
import Util.TextBase;

public class Chapter3 extends Chapter {
    private ImageView arisaImage;
    private ImageView cashenImage;
    private ImageView strangeUncleImage;

    @Override
    public void startChapter(Stage primaryStage) {
        stateSetup(primaryStage);
    }
    
    @Override
	protected void stateSetup(Stage primaryStage) {
    	playBackgroundMusic("/resources/sound/bgChap1.mp3");
        setStoryTexts("src/resources/texts/Chapter3.txt");

        ImageView background = setupBackground("/resources/background/BackgroundChapter3.jpg");
        TextFlow textBox = createTextFlow();
        Button nextButton = createNextButton(primaryStage, textBox);

        arisaImage = createSpeakerImage("อาริสา");
        cashenImage = createSpeakerImage("คเชน");
        strangeUncleImage = createSpeakerImage("ลุงเจ้าของบูธที่ดูแปลก ๆ ");
        updateSpeakerVisibility();

        // Stack text box background and text
        StackPane textBoxStack = createTextBoxStack(textBox);
        StackPane textBoxWithButton = createTextBoxWithButton(textBoxStack, nextButton);

        // Speaker images container
        HBox speakerPane = new HBox(80,strangeUncleImage, cashenImage, arisaImage);
        speakerPane.setAlignment(Pos.BOTTOM_CENTER);

        stackPane = new StackPane(background, speakerPane);
        timeline = createTimeline(textBox);
        timeline.play();

        // Setup root directly
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        root.getChildren().addAll(stackPane, textBoxWithButton);

        // Setup scene directly
        enterAnimation(root, textBox);
        primaryStage.setScene(new Scene(root, 968, 648, Color.BLACK));
        primaryStage.setTitle("Visual Novel - Chapter 3");
    }
    
    @Override
    public ImageView createSpeakerImage(String speaker) {
        String imagePath;
        int width, height;

        switch (speaker) {
            case "คเชน":
                imagePath = "/resources/cashen/cashen_normal.png";
                width = 200;
                height = 300;
                break;
            case "เพื่อน":
                imagePath = "/resources/friend/friend_normal.png";
                width = 260;
                height = 300;
                break;
            case "อาริสา":
                imagePath = "/resources/arisa/Arisa_smile.png";
                width = 230;
                height = 290;
                break;
            case "ลุงเจ้าของบูธที่ดูแปลก ๆ ":
                imagePath = "/resources/strangeUncle/strangeUncle.png";
                width = 390;
                height = 300;
                break;
            default:
                System.out.println("Unknown speaker: " + speaker);
                return new ImageView(); // TODO Unknown speaker
        }

        ImageView img = createImageView(imagePath, width, height);
        
        DropShadow dropShadow = new DropShadow();
        dropShadow.setColor(Color.BLACK);
        dropShadow.setRadius(10);
        dropShadow.setSpread(0.1);
        img.setEffect(dropShadow);

        return img;
    }

    @Override
    public void updateCharacterImages() {
        String currentSpeaker = storyTexts.getStoryTexts().get(currentTextIndex)[1];
        String emotion = storyTexts.getStoryTexts().get(currentTextIndex)[0];

        if (currentSpeaker.equals("คเชน")) {
            cashenImage.setImage(new Image(getClass().getResource(getImagePath("คเชน", emotion)).toExternalForm()));
        } 
        else if(currentSpeaker.equals("อาริสา")) {
            arisaImage.setImage(new Image(getClass().getResource(getImagePath("อาริสา", emotion)).toExternalForm()));
        }
    }

    @Override
    public void updateSpeakerVisibility() {
        String currentSpeaker = storyTexts.getStoryTexts().get(currentTextIndex)[1];
        String status = storyTexts.getStoryTexts().get(currentTextIndex)[TextBase.readingStatusIndex];
        
        if (currentTextIndex == 0) {
            strangeUncleImage.setOpacity(0);
        }
        
        if (status.equals("event")) {
            strangeUncleImage.setOpacity(1.0);
        } 
        else if (status.equals("event2")) {
        	createTimeSkipAnimation();
            strangeUncleImage.setOpacity(0);
        }
        
        if (currentSpeaker.equals("คเชน")) {
            cashenImage.setOpacity(1.0);
            arisaImage.setOpacity(0.6);
            if (strangeUncleImage.getOpacity() > 0) {
                strangeUncleImage.setOpacity(0.6);
            }
        } 
        else if (currentSpeaker.equals("อาริสา")) {
            cashenImage.setOpacity(0.6);
            arisaImage.setOpacity(1.0);
            if (strangeUncleImage.getOpacity() > 0) {
                strangeUncleImage.setOpacity(0.6);
            }
        }
        else if (currentSpeaker.equals("ลุงเจ้าของบูธที่ดูแปลก ๆ ")) {
            cashenImage.setOpacity(0.6);
            arisaImage.setOpacity(0.6);
            strangeUncleImage.setOpacity(1.0);
        }
    }

    @Override
    public void setStoryTexts(String url) {
        storyTexts = new TextBase(url);
    }
    
    public void goToNextChapter(Stage primaryStage) {
    	// exitAnimation(primaryStage);
    	backgroundMusic.stop();
    	
    	Chapter4 chapter4 = new Chapter4();
        chapter4.startChapter(primaryStage);
    }
    
    private void createTimeSkipAnimation() {
        // Create a black overlay rectangle for the time skip effect
        Rectangle timeSkipOverlay = new Rectangle(968, 486);
        timeSkipOverlay.setFill(Color.BLACK);
        timeSkipOverlay.setOpacity(0);
        
        // Create clock transition text
        Text timeSkipText = new Text("หลักจากนั้นทั้งคู่ก็ไปเต้นหน้าฮ้าน!!!...");
        timeSkipText.setFont(Font.font("Serif", FontWeight.BOLD, 30));
        timeSkipText.setFill(Color.WHITE);
        timeSkipText.setOpacity(0);
        
        // Add the overlay and text to stackPane
        stackPane.getChildren().addAll(timeSkipOverlay, timeSkipText);
        
        // Ensure the text is centered
        StackPane.setAlignment(timeSkipText, Pos.CENTER);
        
        // Hide Strange Uncle
        FadeTransition hideUncle = new FadeTransition(Duration.seconds(0.5), strangeUncleImage);
        hideUncle.setToValue(0);
        
        // Fade in the overlay
        FadeTransition fadeInOverlay = new FadeTransition(Duration.seconds(1), timeSkipOverlay);
        fadeInOverlay.setFromValue(0);
        fadeInOverlay.setToValue(0.8);
        
        // Fade in the text
        FadeTransition fadeInText = new FadeTransition(Duration.seconds(1), timeSkipText);
        fadeInText.setFromValue(0);
        fadeInText.setToValue(1);
        
        // Pause to show the time skip message
        PauseTransition pause = new PauseTransition(Duration.seconds(2.5));
        
        // Fade out the text
        FadeTransition fadeOutText = new FadeTransition(Duration.seconds(1), timeSkipText);
        fadeOutText.setFromValue(1);
        fadeOutText.setToValue(0);
        
        // Fade out the overlay
        FadeTransition fadeOutOverlay = new FadeTransition(Duration.seconds(1), timeSkipOverlay);
        fadeOutOverlay.setFromValue(0.8);
        fadeOutOverlay.setToValue(0);
        
        // Create sequential transition for the entire time skip
        SequentialTransition timeSkipSequence = new SequentialTransition(
            hideUncle,
            fadeInOverlay,
            fadeInText,
            pause,
            fadeOutText,
            fadeOutOverlay
        );
        
        // After animation completes, remove the added elements
        timeSkipSequence.setOnFinished(e -> {
            stackPane.getChildren().removeAll(timeSkipOverlay, timeSkipText);
        });
        
        // Play the time skip animation
        timeSkipSequence.play();
    }
}