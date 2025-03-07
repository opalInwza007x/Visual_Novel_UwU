package logic;

public class GameLogic {
	private static GameLogic instance;
	private boolean haveMeat;
	private int currentChapter;
	private double arisaLike;
	
	private GameLogic() {
		setHaveMeat(false);
		setCurrentChapter(0);
		setArisaLike(0);
	}
	
	public static GameLogic getInstance() {
		if (instance == null) {
			instance = new GameLogic();
		}
		return instance;
	}
	
	public boolean isHaveMeat() {
		return haveMeat;
	}
	
	public void setHaveMeat(boolean haveMeat) {
		this.haveMeat = haveMeat;
	}
	
	public int getCurrentChapter() {
		return currentChapter;
	}
	
	public void setCurrentChapter(int currentChapter) {
		this.currentChapter = currentChapter;
	}
	
	public double getArisaLike() {
		return arisaLike;
	}
	
	public void setArisaLike(double arisaLike) {
		this.arisaLike = arisaLike;
	}
	
	
}
