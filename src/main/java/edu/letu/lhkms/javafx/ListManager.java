package edu.letu.lhkms.javafx;

import static edu.letu.lhkms.javafx.FXUtil.padded;

import java.util.function.Supplier;

import edu.letu.lhkms.structure.InteractiveList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

public class ListManager extends VBox {
	private final Button moveUp = padded(new Button("Move Up"));
	private final Button moveDown = padded(new Button("Move Down"));
	private final Button newB = padded(new Button("New"));
	private final Button delete = padded(new Button("Delete"));
	private final Button edit = padded(new Button("Edit"));
	
	private final ListView<?> list;
	private final Supplier<InteractiveList> target;
	private final Runnable callback;
	
	public ListManager(ListView<?> list, Supplier<InteractiveList> target, Runnable callback) {
		this.list = list;
		this.target = target;
		this.callback = callback;
		moveUp.setOnAction(wrapEvent((i) -> {return target.get().canMoveUp(i) ? target.get().moveUp(i) : -1;}));
		moveDown.setOnAction(wrapEvent((i) -> {return target.get().canMoveDown(i) ? target.get().moveDown(i) : -1;}));
		// TODO other buttons
		this.getChildren().addAll(
				FXUtil.padded(newB),
				FXUtil.padded(edit),
				FXUtil.padded(moveUp),
				FXUtil.padded(moveDown),
				FXUtil.padded(delete)
				);
	}
	
	private EventHandler<ActionEvent> wrapEvent(ExtendedEventHandler h) {
		return (e) -> {
			int i = list.getSelectionModel().getSelectedIndex();
			if (target.get().exists(i)) {
				int newInd = h.process(i);
				if (newInd > -1) {
					callback.run();
					list.getSelectionModel().select(newInd);
				}
			}
		};
	}
	
	interface ExtendedEventHandler {
		public abstract int process(int selected);
	}
}
