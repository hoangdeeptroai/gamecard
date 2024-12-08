package gameScene;

import java.io.IOException;
import java.util.List;

//import javax.smartcardio.Card;

import controller.TienLenMienNamController;
import gameLogic.TienLenMienNam;
import imageAction.BackOfCard;
import imageAction.BackgroundImageView;
import imageAction.CardImg;
import javafx.animation.PauseTransition;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import module.AiPlayer;
import module.CardofTienLen;
import module.Deck;
import module.Player;
import module.Player.PlayerState;
import soundEffect.ClickSound;

public class TienLenMienNamScene {
	private TienLenMienNam tienLenMienNam;
	private StackPane centerPane;
	private StackPane bottomPane;
	private StackPane topPane;
	private StackPane leftPane;
	private StackPane rightPane;
	private HBox buttonBox1;
	private Stage primaryStage;

	public TienLenMienNamScene(TienLenMienNam tienLenMienNam, Stage primaryStage) {
		this.tienLenMienNam = tienLenMienNam;
		this.primaryStage = primaryStage;
		;
	}

	public Scene createScene() {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gameScene/TienLenMienNamScene.fxml"));
			Parent root = loader.load();
			TienLenMienNamController controller = loader.getController();
			((Pane) root).setBackground(BackgroundImageView.set());

			// Khởi tạo các pane
			centerPane = controller.getCenterPane();
			bottomPane = controller.getBottomPane();
			topPane = controller.getTopPane();
			leftPane = controller.getLeftPane();
			rightPane = controller.getRightPane();

			// Cài đặt các phần tử giao diện
			bottomPane.setPadding(new Insets(10, 0, 10, 0));
			topPane.setPadding(new Insets(10, 0, 10, 0));
			leftPane.setPadding(new Insets(80, 150, 80, 150));
			rightPane.setPadding(new Insets(80, 150, 80, 150));

			// Tạo nút và các thành phần giao diện khác
			createActionButtons();
			createCenterPack();

			return new Scene(root, 1200, 700);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	private void createActionButtons() {
		// Tạo nút "Hit" và "Skip"
		Button hitButton = new Button("Hit");
		Button skipButton = new Button("Skip");

		// Thiết lập kích thước và khoảng cách
		hitButton.setPrefSize(100, 40);
		skipButton.setPrefSize(100, 40);

		// Tạo HBox để chứa các nút (chỉ tạo 1 lần)
		buttonBox1 = new HBox(20); // Khoảng cách giữa các nút
		buttonBox1.getChildren().addAll(skipButton, hitButton);
		buttonBox1.setPadding(new Insets(0, 0, 0, 0));

		// Thêm hành động cho các nút
		hitButton.setOnAction(e -> {
			if (tienLenMienNam.isHit()) {
				hit();
			}
		});

		skipButton.setOnAction(e -> {
			skip();
		});

		// Thêm HBox vào bottomPane
		centerPane.getChildren().add(buttonBox1);
	}

	private void hit() {
		ClickSound.play();
		tienLenMienNam.playGame();

		if (tienLenMienNam.isTheEnd()) {
			lastPlayCardShow();
			tienLenMienNam.resetGame();
			Button newGame = new Button("New Game");
			Label gameOver = new Label("GAME OVER!!!");
			VBox endGame = new VBox(5, newGame, gameOver); // 10 là khoảng cách giữa các Label
			endGame.setStyle("-fx-background-color: white;"); // Đặt nền trắng
			endGame.setAlignment(Pos.CENTER);

			newGame.setOnAction(e -> {
				updateScene(centerPane, bottomPane, topPane, leftPane, rightPane);
			});
			centerPane.getChildren().add(endGame);
		} else {
			// Xóa bộ bài đã chọn và cập nhật scene chính
			tienLenMienNam.getSelectionCard().removeDeck();
			updateScene(centerPane, bottomPane, topPane, leftPane, rightPane); // Cập nhật lại scene chơi chính
		}
	}

	private void skip() {
		ClickSound.play();
		tienLenMienNam.skip();
		updateScene(centerPane, bottomPane, topPane, leftPane, rightPane);
	}

	private void createCenterPack() {
		ImageView cardImage = BackOfCard.create(0, 0);
		cardImage.setOnMouseClicked(e -> {
			ClickSound.play();
			updateScene(centerPane, bottomPane, topPane, leftPane, rightPane);
		});
		centerPane.getChildren().add(cardImage);
	}

	public void updateScene(StackPane centerPane, StackPane bottomPane, StackPane topPane, StackPane leftPane,
			StackPane rightPane) {

		playerCardShow();

		lastPlayCardShow();

		ifAiTurn();

		createActionButtons();
	}

	private void playerCardShow() {
		// Lấy danh sách bài đã lưu
		List<Player> player = tienLenMienNam.getPlayer();

		// Danh sách các vị trí đích
		StackPane[] destinations = { bottomPane, rightPane, topPane, leftPane };

		// Vòng lặp hiển thị bài của các người chơi
		for (int i = 0; i < player.size(); i++) {
			destinations[i].getChildren().clear();
			for (int j = 0; j < player.get(i).size(); j++) {
				CardofTienLen card = player.get(i).getCards().get(j);
				int size = player.get(i).size();
				if (i == tienLenMienNam.getNowPlayerNum()) {
					while (tienLenMienNam.getNowPlayer().getPlayerState() != PlayerState.TRONG_VONG) {
						tienLenMienNam.nextPlayer();
					}
					if (tienLenMienNam.getNowPlayer() != null && tienLenMienNam.getNowPlayer().getCards() != null) {
						if (tienLenMienNam.getNowPlayer() instanceof AiPlayer) {
							destinations[i].getChildren().add(BackOfCard.create(j, size));
						} else {
							ImageView cardImg = CardImg.create(j, size, card);
							cardImg.setOnMouseClicked(e -> {
								ClickSound.play();
								if (tienLenMienNam.getSelectionCard().getCards().contains(card)) {
									tienLenMienNam.deselectCard(card);
									cardImg.setTranslateY(0);
								} else {
									tienLenMienNam.setSelectionCard(card);
									cardImg.setTranslateY(-10);
								}
							});

							destinations[i].getChildren().add(cardImg);
						}
					}
				} else {
					destinations[i].getChildren().add(BackOfCard.create(j, size));
				}
			}
		}
	}

	private void lastPlayCardShow() {
		Deck lastPlay = tienLenMienNam.getLastPlay();
		centerPane.getChildren().clear();
		for (int i = 0; i < lastPlay.size(); i++) {
			centerPane.getChildren().add(CardImg.create(i, lastPlay.size(), lastPlay.getCard(i)));
		}
	}

	private void ifAiTurn() {
		if (tienLenMienNam.getNowPlayer() instanceof AiPlayer) {
			PauseTransition pause = new PauseTransition();
			pause.setOnFinished(event -> extracted());
			pause.play(); // Bắt đầu thực hiện khoảng dừng
		}
	}

	private void extracted() {
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
			tienLenMienNam.getSelectionCard().removeDeck();
		} else {
			ClickSound.play();
			tienLenMienNam.skip();
		}
		updateScene(centerPane, bottomPane, topPane, leftPane, rightPane);
	}
}