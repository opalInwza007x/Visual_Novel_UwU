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

public class Chapter2 extends Chapter {
    private ImageView arisaImage;
    private ImageView cashenImage;

    @Override
    public void startChapter(Stage primaryStage) {
        stateSetup(primaryStage);
    }
    
    @Override
	protected void stateSetup(Stage primaryStage) {
    	playBackgroundMusic("/resources/sound/bgChap2.mp3");
        setStoryTexts("/resources/texts/Chapter2.txt");

        ImageView background = setupBackground("/resources/background/BackgroundChapter2.png");
        TextFlow textBox = createTextFlow();
        Button nextButton = createNextButton(primaryStage, textBox);

        arisaImage = createSpeakerImage("อาริสา");
        cashenImage = createSpeakerImage("คเชน");
        updateSpeakerVisibility();

        // Stack text box background and text
        StackPane textBoxStack = createTextBoxStack(textBox);
        StackPane textBoxWithButton = createTextBoxWithButton(textBoxStack, nextButton);

        // Speaker images container
        HBox speakerPane = new HBox(80, cashenImage, arisaImage);
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
        primaryStage.setTitle("Visual Novel - Chapter 2");
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
                imagePath = "/resources/arisa/Arisa_shy3_darkMarkMark.png";
                width = 240;
                height = 290;
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
        
        try {
            if (currentSpeaker.equals("คเชน")) {
                String imagePath = getImagePath("คเชน", emotion);
                loadAndSetImage(cashenImage, imagePath);
            } else {
                String imagePath = getImagePath("อาริสา", emotion);
                loadAndSetImage(arisaImage, imagePath);
            }
        } catch (Exception e) {
            System.err.println("Failed to load image: " + e.getMessage());
            // Optionally load a fallback image
        }
    }

    private void loadAndSetImage(ImageView imageView, String imagePath) {
        try {
            // Try using class resource first (works in IDE)
            java.net.URL url = getClass().getResource(imagePath);
            
            // If that fails, try using class loader (better for JAR files)
            if (url == null) {
                url = getClass().getClassLoader().getResource(imagePath.startsWith("/") ? 
                    imagePath.substring(1) : imagePath);
            }
            
            // If we have a valid URL, set the image
            if (url != null) {
                imageView.setImage(new Image(url.toExternalForm()));
            } else {
                System.err.println("Could not find resource: " + imagePath);
                // Optionally set a default/placeholder image
            }
        } catch (Exception e) {
            System.err.println("Error loading image " + imagePath + ": " + e.getMessage());
        }
    }

    @Override
    public void updateSpeakerVisibility() {
        String currentSpeaker = storyTexts.getStoryTexts().get(currentTextIndex)[1];
        
        if (currentTextIndex == 0) {
        	arisaImage.setOpacity(0);
        }
        
        // Update speaker visibility without animations
        if (currentSpeaker.equals("คเชน")) {
            cashenImage.setOpacity(1.0);
            if (arisaImage.getOpacity() > 0) {
            	arisaImage.setOpacity(0.6);
            }
        } 
        else if (currentSpeaker.equals("อาริสา") || currentSpeaker.equals("???")) {
            cashenImage.setOpacity(0.6);
            if (arisaImage.getOpacity() == 0) {
            	FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), arisaImage);
                fadeIn.setFromValue(0.0);
                fadeIn.setToValue(1.0);
                fadeIn.play();
            }
            else {
            	arisaImage.setOpacity(1.0);
            }
        }
    }

    @Override
    public void setStoryTexts(String url) {
        storyTexts = new TextBase(url);
    }
    
    public void goToNextChapter(Stage primaryStage) {
    	// exitAnimation(primaryStage);
    	backgroundMusic.stop();
    	
    	Chapter3 chapter3 = new Chapter3();
        chapter3.startChapter(primaryStage);
    }
}