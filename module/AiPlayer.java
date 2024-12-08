package module;

public class AiPlayer extends Player {

	public AiPlayer() {
		super();
	}

	public Deck getCardToPlay(int n) {
		Deck temp = new Deck();
		temp = clone();
		temp.shuffleDeck();
		n--;
		while (n > 0 && temp.size() > 0) {
			temp.removeCard(0);
			n--;
		}
		return temp;
	}
}