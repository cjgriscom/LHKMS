package edu.letu.lhkms.javafx;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class FXUtil {

	public static <T extends Region> T background(T r, Color color) {
		r.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
		return r;
	}
	
	public static void showAlert(String title, String header, String body) {
		Alert alert = new Alert(AlertType.INFORMATION);
		if (title != null) alert.setTitle(title);
		if (header != null) alert.setHeaderText(header);
		if (body != null) alert.setContentText(body);

		alert.showAndWait();
	}
	
	public static Button actionButton(Button b, EventHandler<ActionEvent> handler) {
		b.setOnAction(handler);
		return b;
	}
	
	public static Region spacer() {
		Region sp = new Region();
		sp.setPrefWidth(5);
		sp.setPrefHeight(5);
		return sp;
	}
	
	public static Region forcePadded(Node b) {
		VBox box = padded(new VBox(b));
		return box;
	}
	
	public static <T extends Region> T padded(T r, int px) {
		r.setPadding(new Insets(px));
		return r;
	}
	
	public static <T extends Region> T padded(T r) {
		return padded(r, 3);
	}
	
	public static Region vspring() {
		Region s = new Region();
		GridPane.setVgrow(s, Priority.ALWAYS);
		VBox.setVgrow(s, Priority.ALWAYS);
		return s;
	}
	
	public static Region hspring() {
		Region s = new Region();
		GridPane.setHgrow(s, Priority.ALWAYS);
		HBox.setHgrow(s, Priority.ALWAYS);
		return s;
	}
	
	public static VBox labeledVField(String label, TextField field) {
		return padded(new VBox(new Label(label), field));
	}
}
