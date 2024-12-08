package gameScene;

import gameLogic.TienLenMienNam;
import imageAction.BackgroundImageView;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import soundEffect.ClickSound;

public class NamePlayerInputScene {
	public static void create(Stage primaryStage, int numPlayers, int numAi, int k) {
		TienLenMienNam tienLenMienNam = new TienLenMienNam(numPlayers, numAi);
		VBox root = new VBox(10);
		root.setAlignment(Pos.CENTER);
		root.setStyle("-fx-padding: 25;");

		Label label = new Label("Nhập tên người chơi:");
		label.setStyle("-fx-font-size: 18px; -fx-text-fill: white;");

		// Các trường nhập tên người chơi
		TextField[] playerNameFields = new TextField[numPlayers];
		for (int i = 0; i < numPlayers; i++) {
			playerNameFields[i] = new TextField();
			playerNameFields[i].setMaxWidth(200);
			playerNameFields[i].setPromptText("Tên người chơi " + (i + 1));
		}

		// Nút "Bắt đầu chơi"
		Button startGameButton = new Button("Bắt đầu chơi");

		startGameButton.setOnAction(e -> {
			ClickSound.play();
			// Kiểm tra tên người chơi
			for (int i = 0; i < numPlayers; i++) {
				if (playerNameFields[i].getText().isEmpty()) {
					playerNameFields[i].setStyle("-fx-border-color: red;");
					return; // Không tiếp tục nếu tên không hợp lệ
				}
				tienLenMienNam.getPlayer().get(i).setPlayerName(playerNameFields[i].getText());
			}

			// Chuyển sang màn hình chơi game
			if (k == 0) {
				TienLenMienNamScene gameScene = new TienLenMienNamScene(tienLenMienNam, primaryStage);
				Scene gameSceneObject = gameScene.createScene(); // Sử dụng GameScene để tạo gameScene
				primaryStage.setScene(gameSceneObject);
			} else {
				TienLenMienNamBasic gameScene = new TienLenMienNamBasic(tienLenMienNam, primaryStage);
				Scene gameSceneObject = gameScene.createScene(); // Sử dụng GameScene để tạo gameScene
				primaryStage.setScene(gameSceneObject);
			}
		});

		root.getChildren().addAll(label);
		root.getChildren().addAll(playerNameFields);
		root.getChildren().addAll(startGameButton);
		// Tái sử dụng nền cũ
		root.setBackground(BackgroundImageView.set());

		Scene playerNameScene = new Scene(root, 1200, 700);
		primaryStage.setScene(playerNameScene);
	}
}
