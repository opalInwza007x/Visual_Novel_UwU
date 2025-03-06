package main;

import java.util.Arrays;

import Util.Chapter;
import Util.TextBase;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import logic.GameLogic;

public class ChoiceMinigame extends Chapter{

	@Override
	protected void startChapter(Stage primaryStage) {
		stateSetup(primaryStage);
	}
	
	@Override
	protected void stateSetup(Stage primaryStage) {
		playBackgroundMusic("/resources/sound/bgChap1.mp3");
        setStoryTexts("src/resources/texts/minigameT1.txt");
		
        ImageView background = setupBackground("/resources/background/classroomTest.jpg");
        TextFlow textBox = createTextFlow();
        Button nextButton = createNextButton(primaryStage, textBox);
        
     // Stack text box background and text
        StackPane textBoxStack = createTextBoxStack(textBox);
        StackPane textBoxWithButton = createTextBoxWithButton(textBoxStack, nextButton);
        
        // Speaker images container
        stackPane = new StackPane(background);
        timeline = createTimeline(textBox);
        timeline.play();
        
        // Setup root directly
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));
        root.getChildren().addAll(stackPane, textBoxWithButton);
        
        primaryStage.setScene(new Scene(root, 968, 648, Color.BLACK));
        primaryStage.setTitle("Minigame - Wow");
	}

	@Override
	protected void updateCharacterImages() {
		
	}

	@Override
	protected ImageView createSpeakerImage(String speaker) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void updateSpeakerVisibility() {
		// TODO Auto-generated method stub
		
	}

	@Override
    public void setStoryTexts(String url) {
        storyTexts = new TextBase(url);
    }

	@Override
	public void goToNextChapter(Stage primaryStage) {
    	// exitAnimation(primaryStage);
    	backgroundMusic.stop();
    	
    	Chapter2 chapter2 = new Chapter2();
        chapter2.startChapter(primaryStage);
    }

	@Override
    public void handleNextText(Stage primaryStage, TextFlow textBox, int fromAnswerBox) {
        // If animation is running and user clicks Next, just show full text immediately
        if (isRunning()) {
            timeline.stop();
            // Replace with direct text update without animation
            updateTextBoxInstantly(textBox);
            return;
        }
        
        if (fromAnswerBox == 0) {
        	if (!"ask2".equals(storyTexts.getStoryTexts().get(currentTextIndex)[TextBase.readingStatusIndex])) {
                currentTextIndex++;
            }
        } 
        else {
        	// Advance text index
            GameLogic.getInstance().setArisaLike(GameLogic.getInstance().getArisaLike() + Integer.parseInt(fromAnswerBox == 1?storyTexts.getStoryTexts().get(currentTextIndex)[TextBase.quesion1Index]:storyTexts.getStoryTexts().get(currentTextIndex)[TextBase.quesion2Index]));
            currentTextIndex++;
        }
        	
        System.out.println(GameLogic.getInstance().getArisaLike());
        if (currentTextIndex < storyTexts.getStoryTexts().size()) {
            updateSpeakerVisibility();
            playEffectSound(storyTexts.getStoryTexts().get(currentTextIndex)[TextBase.soundEffectIndex]);
            updateCharacterImages();
            
            if ("ask2".equals(storyTexts.getStoryTexts().get(currentTextIndex)[TextBase.readingStatusIndex])) {
                createAnswerBoxFor2(primaryStage, textBox);
            }
            
            timeline.stop();
            timeline = createTimeline(textBox);
            timeline.play();
        } else {
            goToNextChapter(primaryStage);
        }
    }
	
	@Override
	public void createAnswerBoxFor2(Stage primaryStage, TextFlow textBox) {
        // Remove existing answer box if it exists
        if (choiceBoxStack != null && stackPane.getChildren().contains(choiceBoxStack)) {
            stackPane.getChildren().remove(choiceBoxStack);
        }
        
        Button answerButton1 = createButton(
            storyTexts.getStoryTexts().get(currentTextIndex)[TextBase.answer1Index],
            "rgba(0, 128, 255, 0.8)",
            18
        );
        
        Button answerButton2 = createButton(
            storyTexts.getStoryTexts().get(currentTextIndex)[TextBase.answer2Index],
            "rgba(0, 128, 255, 0.8)",
            18
        );

        String buttonStyle = "-fx-background-radius: 30; -fx-text-fill: white; -fx-font-weight: bold; " +
                             "-fx-padding: 15 30; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.6), 5, 0, 0, 1);";

        answerButton1.setStyle(answerButton1.getStyle() + buttonStyle);
        answerButton2.setStyle(answerButton2.getStyle() + buttonStyle);

        VBox answerBox = new VBox(10);

        // Set up vertical box for buttons
        answerBox.setAlignment(Pos.CENTER);
        answerBox.getChildren().addAll(answerButton1, answerButton2);
        answerBox.setPadding(new Insets(20));
        answerBox.setSpacing(20);

        // Create background for choice box
        Rectangle choiceBoxBg = new Rectangle(300, 150);
        choiceBoxBg.setArcWidth(30);
        choiceBoxBg.setArcHeight(30);
        choiceBoxBg.setFill(Color.rgb(20, 20, 60, 0.8));

        // Add glow effect to the choice box
        DropShadow choiceBoxShadow = new DropShadow();
        choiceBoxShadow.setColor(Color.CORNFLOWERBLUE);
        choiceBoxShadow.setRadius(20);
        choiceBoxShadow.setSpread(0.2);
        choiceBoxBg.setEffect(choiceBoxShadow);

        // Stack the choice box elements
        choiceBoxStack = new StackPane(choiceBoxBg, answerBox);

        StackPane.setAlignment(choiceBoxStack, Pos.CENTER);
        stackPane.getChildren().add(choiceBoxStack);

        // Set up button actions
        answerButton1.setOnAction(event -> {
            if (textBox != null) {
                textBox.getChildren().clear();
            }

            stackPane.getChildren().remove(choiceBoxStack);
            choiceBoxStack = null; // Clear the reference
            
            handleNextText(primaryStage, textBox, 1);
        });

        answerButton2.setOnAction(event -> {
            if (textBox != null) {
                textBox.getChildren().clear();
            }

            stackPane.getChildren().remove(choiceBoxStack);
            choiceBoxStack = null; // Clear the reference
            
            handleNextText(primaryStage, textBox, 2);
        });
    }
}
