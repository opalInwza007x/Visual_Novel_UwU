package Util;

import javafx.animation.Timeline;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public interface HaveText {
	public abstract Timeline createTimeline(TextFlow textBox);
	public abstract void handleNextText(Stage primaryStage, TextFlow textBox, int fromAnswerBox);
	public abstract boolean isRunning();
	public abstract void playTalkingSound(String talking);
}
