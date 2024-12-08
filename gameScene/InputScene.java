package gameScene;

import imageAction.BackgroundImageView;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import soundEffect.ClickSound;

public class InputScene {
	public static Scene create(Stage primaryStage, int k) {
		VBox inputPane = new VBox(20);
		inputPane.setAlignment(Pos.CENTER);

		// Tạo các nhãn và text field
		Label label = new Label("Chọn số lượng người chơi:");
		label.setStyle("-fx-font-size: 18px; -fx-text-fill: white;");

		TextField playerTextFiled = new TextField();
		playerTextFiled.setPromptText("Số lượng người chơi");
		playerTextFiled.setMaxWidth(200);

		TextField aiTextField = new TextField();
		aiTextField.setPromptText("Số lượng AI");
		aiTextField.setMaxWidth(200);

		// Tạo button chuyển cảnh
		Button next = new Button("Tiếp theo");
		next.setOnAction(e -> {
			ClickSound.play();
			int playerCount = parseInput(playerTextFiled.getText());
			int aiCount = parseInput(aiTextField.getText());
			int numOfPlayer = playerCount + aiCount;
			if (numOfPlayer <= 1 || numOfPlayer > 4) {
				return;
			} else {
				NamePlayerInputScene.create(primaryStage, numOfPlayer, aiCount, k);
			}
		});

		inputPane.getChildren().addAll(label, playerTextFiled, aiTextField, next);

		// Tạo nền riêng cho scene nhập số người chơi
		StackPane inputRoot = new StackPane();
		inputRoot.setPrefSize(1200, 700);
		inputRoot.setBackground(BackgroundImageView.set());

		inputRoot.getChildren().add(inputPane);

		return new Scene(inputRoot, 1200, 700);
	}

	// xử lý input
	public static int parseInput(String input) {
		if (input == null || input.trim().isEmpty()) {
			return 0;
		}
		return Integer.parseInt(input);
	}
}
