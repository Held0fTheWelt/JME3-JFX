package com.jme3x.jfx;

import javafx.event.EventHandler;
import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.Region;

import com.jme3.app.SimpleApplication;
import com.jme3.math.ColorRGBA;
import com.jme3x.jfx.cursor.proton.ProtonCursorProvider;

public class TestDragDrop extends SimpleApplication {
	private static boolean	assertionsEnabled;

	public static void main(final String[] args) {
		System.out.println("testd&d");
		assert TestDragDrop.enabled();
		if (!TestDragDrop.assertionsEnabled) {
			throw new RuntimeException("Assertions must be enabled (vm args -ea");
		}
		new TestDragDrop().start();
	}

	private static boolean enabled() {
		TestDragDrop.assertionsEnabled = true;
		return true;
	}

	@Override
	public void simpleInitApp() {
		this.setPauseOnLostFocus(false);
		this.flyCam.setDragToRotate(true);
		this.viewPort.setBackgroundColor(ColorRGBA.Red);

		final GuiManager testguiManager = new GuiManager(this.guiNode, this.assetManager, this, false, new ProtonCursorProvider(this, this.assetManager, this.inputManager));
		/**
		 * 2d gui, use the default input provider
		 */
		this.inputManager.addRawInputListener(testguiManager.getInputRedirector());

		final AbstractWindow targetwindow = new AbstractWindow() {

			@Override
			protected Region innerInit() throws Exception {
				final Label target = new Label("Drag target");

				target.setOnDragEntered(new EventHandler<DragEvent>() {
					@Override
					public void handle(final DragEvent event) {
						System.out.println("Drag enterd!");
					}
				});

				target.setOnDragExited(new EventHandler<DragEvent>() {
					@Override
					public void handle(final DragEvent event) {
						System.out.println("Drag enterd!");
					}
				});

				target.setOnDragOver(new EventHandler<DragEvent>() {
					@Override
					public void handle(final DragEvent event) {
						event.acceptTransferModes(TransferMode.COPY);
					}
				});

				target.setOnDragDropped(new EventHandler<DragEvent>() {

					@Override
					public void handle(final DragEvent event) {
						System.out.println("Dropped " + event.getDragboard().getString());
					}
				});
				return target;
			}

			@Override
			protected void afterInit() {
				this.setSize(300, 200);
			}
		};

		final AbstractWindow sourceWindow = new AbstractWindow() {

			@Override
			protected Region innerInit() throws Exception {
				final Label target = new Label("Drag source");
				target.setOnDragDetected(new EventHandler<MouseEvent>() {

					@Override
					public void handle(final MouseEvent event) {
						final Dragboard db = target.startDragAndDrop(TransferMode.MOVE);
						final ClipboardContent content = new ClipboardContent();
						content.putString("Dragdropped Text");
						db.setContent(content);
					}
				});
				return target;
			}

			@Override
			protected void afterInit() {
				this.setSize(300, 200);
				this.setLayoutX(310);
			}
		};

		testguiManager.attachHudAsync(targetwindow);
		testguiManager.attachHudAsync(sourceWindow);

	}
}
