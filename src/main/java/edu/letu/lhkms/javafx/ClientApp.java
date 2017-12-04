package edu.letu.lhkms.javafx;

import static edu.letu.lhkms.javafx.FXUtil.actionButton;
import static edu.letu.lhkms.javafx.FXUtil.background;
import static edu.letu.lhkms.javafx.FXUtil.labeledVField;
import static edu.letu.lhkms.javafx.FXUtil.padded;
import static edu.letu.lhkms.javafx.FXUtil.showAlert;

import java.io.IOException;
import java.util.TreeMap;
import java.util.UUID;

import org.controlsfx.control.BreadCrumbBar;
import org.json.JSONObject;

import edu.letu.lhkms.HTTPUtil;
import edu.letu.lhkms.javafx.pages.HomePage;
import edu.letu.lhkms.javafx.pages.editors.ContentEditor;
import edu.letu.lhkms.javafx.pages.editors.ScreensEditor;
import edu.letu.lhkms.javafx.pages.editors.ViewsEditor;
import edu.letu.lhkms.javafx.pages.editors.content.ContentItemEditor;
import edu.letu.lhkms.javafx.pages.editors.content.ScreenItemEditor;
import edu.letu.lhkms.javafx.pages.editors.content.ViewItemEditor;
import edu.letu.lhkms.structure.CompleteDatabasePipeline;
import edu.letu.lhkms.structure.Content;
import edu.letu.lhkms.structure.Screen;
import edu.letu.lhkms.structure.View;
import javafx.application.Application;
import javafx.beans.binding.StringExpression;
import javafx.beans.binding.When;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
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
	
	public final ObjectProperty<CompleteDatabasePipeline> db = new SimpleObjectProperty<>();
	public final BooleanProperty loaded = new SimpleBooleanProperty(false);
	
	
	
	
	private final StringProperty mainTitle = 
			new SimpleStringProperty("Longview Hall Kiosk Management System");
	
	private final StringProperty titleBar = new SimpleStringProperty();
	private final TextField address = new TextField("localhost:8080");
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
	
	private final TreeMap<UUID, TreeItem<ContainerChild>> cachedContainers = new TreeMap<>();
	
	private Stage stage;
	
	public static void main(String[] args) {
		ClientApp.launch(args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		this.stage = stage;
		VBox prim = new VBox();
		
		// TODO ADD LOGINS
		username.disableProperty().set(true);
		password.disableProperty().set(true);
		//
		
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
		FXUtil.addStylesheet(scene, "/clientapp.css");
		stage.titleProperty().bind(titleBar);
		stage.setScene(scene);
		stage.setWidth(800);
		stage.setHeight(600);
		stage.getIcons().add(new Image(getClass().getResourceAsStream("/icon.png")));
		setSubtitle(null);
		stage.show();
		
		// !Begin!
		rebuildTree(); // Blank tree
		breadcrumb.setSelectedCrumb(home);
	}
	
	public TreeItem<ContainerChild> getTreeItemByUUID(UUID uuid) {
		return cachedContainers.get(uuid);
	}
	
	public ContainerChild getContainerByUUID(UUID uuid) {
		return cachedContainers.get(uuid).getValue();
	}

	// Data Management
	@SuppressWarnings("unchecked")
	public void rebuildTree() {
		
		home.getChildren().clear();
		contentEditor.getChildren().clear();
		viewsEditor.getChildren().clear();
		screensEditor.getChildren().clear();

		home.getChildren().addAll(contentEditor, viewsEditor, screensEditor);
		
		if (loaded.get()) {
			// Build subtrees
			for (Content c : db.get().contentList()) {
				TreeItem<ContainerChild> child;
				if (cachedContainers.containsKey(c.getContentID())) {
					child = getTreeItemByUUID(c.getContentID());
				} else {
					cachedContainers.put(c.getContentID(), 
							child = new TreeItem<>(new ContentItemEditor(c, container)));
				}
				contentEditor.getChildren().add(child);
			}
			
			for (View v : db.get().viewList()) {
				TreeItem<ContainerChild> child;
				if (cachedContainers.containsKey(v.getViewID())) {
					child = getTreeItemByUUID(v.getViewID());
				} else {
					cachedContainers.put(v.getViewID(), 
							child = new TreeItem<>(new ViewItemEditor(v, container)));
				}
				viewsEditor.getChildren().add(child);
			}
			
			for (Screen s: db.get().screenList()) {
				TreeItem<ContainerChild> child;
				if (cachedContainers.containsKey(s.getScreenID())) {
					child = getTreeItemByUUID(s.getScreenID());
				} else {
					cachedContainers.put(s.getScreenID(), 
							child = new TreeItem<>(new ScreenItemEditor(s, db.get().viewList(), container)));
				}
				screensEditor.getChildren().add(child);
			}
			// TODO the rest
		} else {
			breadcrumb.setSelectedCrumb(home); 
			
		}
		// TODO validate current breadcrumb and backtrack if needed***
	}
	
	public boolean loadDatabase() {
		String address = this.address.getText();
		
		try {
			String jsonDB = HTTPUtil.sendGet("http://" + address + "/getDatabase");
			db.set(new CompleteDatabasePipeline(new JSONObject(jsonDB)));
			loaded.set(true);  // It's important that this comes before rebuildTree
			// Otherwise it won't populate the breadcrumb
			rebuildTree();
		} catch (IOException | RuntimeException e) {
			e.printStackTrace();
			showAlert("Error", "Error connecting to server", null);
			loaded.set(false);
			return false;
		}
		
		return true;
	}
	
	public void commitDB() {
		String address = this.address.getText();
		try {
			String resp = HTTPUtil.sendPost("http://" + address + "/setDatabase", db.get().serialize().toString());
			if (resp.equals("Success")) {
				// Do nothing
			} else {

				showAlert("Error", "Error commiting database changes", resp);
			}
		} catch (IOException e) {
			e.printStackTrace();
			showAlert("Error", "Error commiting database changes", null);
		} catch (RuntimeException e) {
			e.printStackTrace();
			showAlert("Error", "Error commiting database changes", null);
		}
	}
	
	// Event Handlers
	
	public void processLogin(ActionEvent e) {
		if (loadDatabase())
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
	public void handleEditItem(UUID itemID) {
		breadcrumb.setSelectedCrumb(getTreeItemByUUID(itemID));
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
