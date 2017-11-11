package edu.letu.lvkms.javafx;

import static edu.letu.lvkms.javafx.FXUtil.actionButton;
import static edu.letu.lvkms.javafx.FXUtil.background;
import static edu.letu.lvkms.javafx.FXUtil.labeledVField;
import static edu.letu.lvkms.javafx.FXUtil.padded;
import static edu.letu.lvkms.javafx.FXUtil.showAlert;

import org.controlsfx.control.BreadCrumbBar;

import edu.letu.lvkms.structure.CompleteDatabasePipeline;
import javafx.application.Application;
import javafx.beans.binding.StringExpression;
import javafx.beans.binding.When;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
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
	
	final ObjectProperty<CompleteDatabasePipeline> db = new SimpleObjectProperty<>();
	private final StringProperty mainTitle = 
			new SimpleStringProperty("Longview Hall Kiosk Management System");
	
	private final StringProperty titleBar = new SimpleStringProperty();
	private final TextField address = new TextField();
	private final TextField username = new TextField();
	private final TextField password = new TextField();
	
	private final Button login = actionButton(new Button("Log In"), this::processLogin);
	private final Button quit = actionButton(new Button("Quit"), this::processQuit);

	private final BreadCrumbBar<ContainerChild> breadcrumb = new BreadCrumbBar<>();
	private final StackPane container = new StackPane();
	
	private final TreeItem<ContainerChild> home = 
			buildTreeItem(new HomePage(this, container));
	private final TreeItem<ContainerChild> contentEditor = 
			buildTreeItem(new ContentEditor(this, container));
	private final TreeItem<ContainerChild> viewsEditor = 
			buildTreeItem(new ViewsEditor(this, container));
	private final TreeItem<ContainerChild> screensEditor = 
			buildTreeItem(new ScreensEditor(this, container));

	
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
			container.getChildren().add(n.getValue());
			setSubtitle(n.getValue().subTitle());
		});
		
		
		VBox.setVgrow(container, Priority.ALWAYS);
		container.prefWidthProperty().bind(stage.widthProperty());
		container.maxWidthProperty().bind(stage.widthProperty());
		
		Scene scene = new Scene(prim);
		stage.titleProperty().bind(titleBar);
		stage.setScene(scene);
		stage.setWidth(800);
		stage.setHeight(600);
		stage.getIcons().add(new Image(getClass().getResourceAsStream("/icon.png")));
		setSubtitle(null);
		stage.show();
		
		// !Begin!
		rebuildTree();
		breadcrumb.setSelectedCrumb(home);
	}
	

	// Data Management
	@SuppressWarnings("unchecked")
	public void rebuildTree() {
		
		home.getChildren().clear();
		contentEditor.getChildren().clear();
		viewsEditor.getChildren().clear();
		screensEditor.getChildren().clear();

		home.getChildren().addAll(contentEditor, viewsEditor, screensEditor);
		
		breadcrumb.setSelectedCrumb(home); // TODO is this needed
	}
	
	// Event Handlers
	
	public void processLogin(ActionEvent e) {
		showAlert("Login", "Login Successful", null);
	}
	
	public void processQuit(ActionEvent e) {
		stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
	}
	
	public void handleEditContent(ActionEvent e) {
		breadcrumb.setSelectedCrumb(contentEditor);
	}
	public void handleEditViews(ActionEvent e) {
		breadcrumb.setSelectedCrumb(viewsEditor);
	}
	public void handleEditScreens(ActionEvent e) {
		breadcrumb.setSelectedCrumb(screensEditor);
	}
	
	
	
	// Child Helper Methods
	public void setSubtitle(StringExpression sub) {
		this.titleBar.unbind();
		if (sub == null) {
			titleBar.bind(mainTitle);
		} else {
			titleBar.bind(
					mainTitle
					.concat(new When(sub.isEmpty()).then(" ").otherwise(" - "))
					.concat(sub)
					);
		}
	}
	
	
	
	// FX Utilities
	public final TreeItem<ContainerChild> buildTreeItem(ContainerChild ci) {
		TreeItem<ContainerChild> ti = new TreeItem<>(ci);
		return ti;
	}
	
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
