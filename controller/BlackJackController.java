package controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import module.Deck;
import module.Player;

public class BlackJackController {
	private Deck deck; // Bộ bài
	private Player player; // Người chơi
	private Player dealer; // Nhà cái

	@FXML
	private Label playerHandLabel;

	@FXML
	private Label dealerHandLabel;

	@FXML
	private Label statusLabel;

	@FXML
	private Button hitButton;

	@FXML
	private Button standButton;

	@FXML
	private Button dealButton;

	@FXML
	public void initialize() {
		resetGame();
		statusLabel.setText("Click 'Deal' to start a new game.");
	}

	@FXML
	private void handleDeal() {
		resetGame();
		dealInitialCards();
		updateUI();
		statusLabel.setText("Game started! Your turn.");
		enableButtons();
	}

	@FXML
	private void handleHit() {
		player.drawCard(deck);
		updateUI();

		if (player.isBust()) {
			statusLabel.setText("You bust! Dealer wins.");
			disableButtons();
			showDealerCards();
		}
	}

	@FXML
	private void handleStand() {
		while (dealer.calculateHandValue() < 17) {
			dealer.drawCard(deck);
		}

		determineWinner();
		disableButtons();
		updateUI();
	}

	private void resetGame() {
		deck = new Deck();
		createAndShuffleDeck();

		player = new Player("Player");
		dealer = new Player("Dealer");

		playerHandLabel.setText("");
		dealerHandLabel.setText("");
		statusLabel.setText("");
		disableButtons();
	}

	private void dealInitialCards() {
		for (int i = 0; i < 2; i++) {
			player.drawCard(deck);
			dealer.drawCard(deck);
		}
	}

	private void determineWinner() {
		int playerTotal = player.calculateHandValue();
		int dealerTotal = dealer.calculateHandValue();

		if (dealer.isBust() || playerTotal > dealerTotal) {
			statusLabel.setText("You win!");
		} else if (playerTotal == dealerTotal) {
			statusLabel.setText("It's a tie!");
		} else {
			statusLabel.setText("Dealer wins!");
		}

		showDealerCards();
	}

	private void updateUI() {
		playerHandLabel.setText("Player Hand: " + player.getHand() + " (Total: " + player.calculateHandValue() + ")");

		if (!hitButton.isDisabled()) {
			dealerHandLabel.setText("Dealer Hand: [" + dealer.getHand().get(0) + ", ???]");
		} else {
			showDealerCards();
		}
	}

	private void showDealerCards() {
		dealerHandLabel.setText("Dealer Hand: " + dealer.getHand() + " (Total: " + dealer.calculateHandValue() + ")");
	}

	private void disableButtons() {
		hitButton.setDisable(true);
		standButton.setDisable(true);
	}

	private void enableButtons() {
		hitButton.setDisable(false);
		standButton.setDisable(false);
	}

	private void createAndShuffleDeck() {
		String[] suits = { "hearts", "diamonds", "clubs", "spades" };
		String[] values = { "2", "3", "4", "5", "6", "7", "8", "9", "10", "jack", "queen", "king", "ace" };

		for (String suit : suits) {
			for (String value : values) {
				deck.addCard(new Cardofblackjack(value, suit));
			}
		}

		deck.shuffleDeck();
	}
}
