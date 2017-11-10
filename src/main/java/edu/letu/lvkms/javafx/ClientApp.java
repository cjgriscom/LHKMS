package edu.letu.lvkms.javafx;

import static edu.letu.lvkms.javafx.FXUtil.actionButton;
import static edu.letu.lvkms.javafx.FXUtil.background;
import static edu.letu.lvkms.javafx.FXUtil.labeledVField;
import static edu.letu.lvkms.javafx.FXUtil.padded;
import static edu.letu.lvkms.javafx.FXUtil.showAlert;

import org.controlsfx.control.BreadCrumbBar;

import javafx.application.Application;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
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

	private final TreeItem<String> root = new TreeItem<String>("Home");
	private final TreeItem<String> contentTree = new TreeItem<String>("Content");
	private final TreeItem<String> viewTree = new TreeItem<String>("Views");
	private final TreeItem<String> screenTree = new TreeItem<String>("Screens");
	private final BreadCrumbBar<String> breadcrumb = new BreadCrumbBar<>(root);
	private final StackPane container = new StackPane();
	
	private final HomePage homePage = new HomePage(this, container);
	
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
		
		prim.getChildren().addAll(
				connectionBar, 
				background(padded(breadcrumb,5), Color.WHITESMOKE),
				background(padded(container),Color.LIGHTGRAY));
		
		breadcrumb.selectedCrumbProperty().addListener((e,o,n) -> {
			container.getChildren().clear();
			container.getChildren().add(getFrameFromTreeItem(n));
		});
		
		rebuildTree();
		breadcrumb.setSelectedCrumb(root);
		container.getChildren().clear();
		container.getChildren().add(getFrameFromTreeItem(root));
		
		VBox.setVgrow(container, Priority.ALWAYS);
		container.prefWidthProperty().bind(stage.widthProperty());
		container.maxWidthProperty().bind(stage.widthProperty());
		
		Scene scene = new Scene(prim);
		stage.titleProperty().bind(titleBar);
		stage.setScene(scene);
		stage.setWidth(800);
		stage.setHeight(600);
		titleBar.set("Longview Hall Kiosk Management System");
		stage.show();
	}
	
	
	private Node getFrameFromTreeItem(TreeItem<String> item) {
		System.out.println(item);
		if (item == root) {
			return homePage;
		} else if (item.getParent() == root) {
			if (item == contentTree) {
				return new Label("C");
			} else if (item == viewTree) {
				return new Label("V");
				
			} else if (item == screenTree) {
				return new Label("S");
				
			}
		}
		return new Region();
		 
	}

	// Data Management
	@SuppressWarnings("unchecked")
	public void rebuildTree() {
		root.getChildren().clear();
		
		TreeItem<String> screen1 = new TreeItem<String>("Screen 1");
		TreeItem<String> screen2 = new TreeItem<String>("Screen 2");
		screenTree.getChildren().addAll(screen1, screen2);

		root.getChildren().addAll(contentTree, viewTree, screenTree);
	}
	
	// Event Handlers
	
	public void processLogin(ActionEvent e) {
		showAlert("Login", "Login Successful", null);
	}
	
	public void processQuit(ActionEvent e) {
		stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
	}
	
	public void handleEditContent(ActionEvent e) {
		breadcrumb.setSelectedCrumb(contentTree);
	}
	public void handleEditViews(ActionEvent e) {
		breadcrumb.setSelectedCrumb(viewTree);
	}
	public void handleEditScreens(ActionEvent e) {
		breadcrumb.setSelectedCrumb(screenTree);
	}
	
	
	
	
	
	
	
	// FX Utilities
	public FlowPane flowBar(ReadOnlyDoubleProperty widthBinding, Node... children) {
		FlowPane bar = padded(new FlowPane());
		bar.setAlignment(Pos.CENTER);
		bar = background(bar, Color.LIGHTGRAY);
		bar.prefWidthProperty().bind(widthBinding);
		
		bar.getChildren().addAll(children);
		
		return bar;
	}
	
	public Region barButton(Button b) {
		b.setPrefWidth(100);
		VBox box = padded(new VBox(b));
		b.setAlignment(Pos.BOTTOM_CENTER);
		box.setAlignment(Pos.BOTTOM_CENTER);
		return box;
	}
}
