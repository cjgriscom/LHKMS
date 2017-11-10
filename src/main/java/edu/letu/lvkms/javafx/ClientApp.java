package edu.letu.lvkms.javafx;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ClientApp extends Application {
	
	private final StringProperty titleBar = new SimpleStringProperty();
	private final TextField address = new TextField();
	private final TextField username = new TextField();
	private final TextField password = new TextField();
	
	private final Button login = actionButton(new Button("Log In"), this::processLogin);
	private final Button quit = actionButton(new Button("Quit"), this::processQuit);
	
	private Stage stage;
	
	public static void main(String[] args) {
		ClientApp.launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		this.stage = stage;
		VBox prim = new VBox();
		
		HBox connectionBar = new HBox();
		
		connectionBar.getChildren().add(flowBar(stage.widthProperty(),
				labeledVField("Server Address", address),
				labeledVField("Username", username),
				labeledVField("Password", password),
				barButton(login),
				barButton(quit)
				));
		
		prim.getChildren().addAll(connectionBar);
		
		Scene scene = new Scene(prim);
		stage.titleProperty().bind(titleBar);
		stage.setScene(scene);
		stage.setWidth(800);
		stage.setHeight(600);
		titleBar.set("Longview Hall Kiosk Management System");
		stage.show();
	}
	
	public FlowPane flowBar(ReadOnlyDoubleProperty widthBinding, Node... children) {
		FlowPane bar = padded(new FlowPane());
		bar.setAlignment(Pos.CENTER);
		bar.setBackground(new Background(new BackgroundFill(Color.GRAY, CornerRadii.EMPTY, Insets.EMPTY)));
		bar.prefWidthProperty().bind(widthBinding);
		
		bar.getChildren().addAll(children);
		
		return bar;
	}
	
	
	
	// Event Handlers
	
	public void processLogin(ActionEvent e) {
		showAlert("Login", "Login Successful", null);
	}
	
	public void processQuit(ActionEvent e) {
		stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
	}
	
	
	
	
	
	
	
	// FX Utilities
	public void showAlert(String title, String header, String body) {
		Alert alert = new Alert(AlertType.INFORMATION);
		if (title != null) alert.setTitle(title);
		if (header != null) alert.setHeaderText(header);
		if (body != null) alert.setContentText(body);

		alert.showAndWait();
	}
	
	public Button actionButton(Button b, EventHandler<ActionEvent> handler) {
		b.setOnAction(handler);
		return b;
	}
	
	public Region spacer() {
		Region sp = new Region();
		sp.setPrefWidth(5);
		sp.setPrefHeight(5);
		return sp;
	}
	
	public Node barButton(Button b) {
		b.setPrefWidth(100);
		VBox box = padded(new VBox(b));
		b.setAlignment(Pos.BOTTOM_CENTER);
		box.setAlignment(Pos.BOTTOM_CENTER);
		return box;
	}
	
	public Button defaultWidths(Button b) {
		b.setPrefWidth(200);
		b.setAlignment(Pos.BOTTOM_CENTER);
		return b;
	}
	
	public <T extends Region> T padded(T r) {
		r.setPadding(new Insets(3));
		return r;
	}
	
	public VBox labeledVField(String label, TextField field) {
		return padded(new VBox(new Label(label), field));
	}
}
