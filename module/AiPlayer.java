package module;

import gameLogic.TienLenMienNam;
import soundEffect.ClickSound;

public class AiPlayer extends Player {

	public AiPlayer() {
		super();
		this.playerName = "AI";
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

	public boolean AiPlay(TienLenMienNam tienLenMienNam) {
		Deck temp = new Deck();
		int m = 1;
		if (tienLenMienNam.getLastPlay().size() > 0) {
			m = tienLenMienNam.getLastPlay().size();
		}
		int k = 100;
		if (m > tienLenMienNam.getNowPlayer().getCards().size()) {
			k = -1;
		}
		while (k > 0) {
			temp = ((AiPlayer) tienLenMienNam.getNowPlayer()).getCardToPlay(m).clone();
			tienLenMienNam.getSelectionCard().removeDeck();
			for (int l = 0; l < m; l++) {
				try {
					tienLenMienNam.getSelectionCard().addCard(temp.getCard(l));
				} catch (Exception e) {
					System.out.println("Loi");
					temp.printDeck();
				}
			}
			if (tienLenMienNam.isHit()) {

				break;
			}
			k--;
		}
		if (k > 10) {
			ClickSound.play();
			tienLenMienNam.playGame();
			System.out.println("AI đã đánh!!");
			return true;
		} else {
			ClickSound.play();
			tienLenMienNam.skip();
			return false;
		}
	}
}