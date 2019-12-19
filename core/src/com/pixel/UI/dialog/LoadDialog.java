package com.pixel.UI.dialog;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.pixel.UI.element.PTextButton;
import com.pixel.UI.label.PLabel;
import com.pixel.game.PixelAssetManager;
import com.pixel.game.PixelCityGame;
import com.pixel.game.styles.Styles;
import com.pixel.scene.GameScene;
import com.pixel.scene.MenuScene;
import com.pixel.serialization.SerializationManager;

import java.io.IOException;
import java.util.ArrayList;

public class LoadDialog extends PDialog {

	private PTextButton loadButton;
	private PTextButton deleteButton;
	private ScrollPane scrollPane;
	private PLabel selectedLabel;
	private Table scrollTable = new Table();

	public LoadDialog() {
		super("Load a Saved Game", PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin));

		setModal(true);

		Label.LabelStyle style = ((Skin) PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin)).get(Label.LabelStyle.class);
		style.font.setColor(0,0,1,1);

		Table table = scrollTable;
		ArrayList<String> readStrings = new ArrayList<>();

		// we read in all of the strings in the atlas file
		FileHandle file = Gdx.files.local(GameScene.savedGamesAtlasPath);
		String fileString = file.readString();

		String line = "";
		for (int i = 0; i < fileString.length(); i++) {
			char readChar = fileString.charAt(i);
			if (readChar == '\n') {
				// then we add the string to a list that needs to have labels created for it
				readStrings.add(line);
				// clear our string
				line = "";
			} else
				line += readChar;
		}

		// add a spacer before the list
		table.add().size(10, 10);
		table.row();

		// then, using our new table, we create new labels for every string in the list and add it to this table
		for (String string : readStrings) {
			PLabel newLabel = new PLabel(string,
				   ((Skin) PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin)).get(Label.LabelStyle.class),
				   Styles.highlightedLabelStyle);

			// set the handler for when this label is selected from the pane
			newLabel.addListener(new ClickListener() {
				@Override
				public void clicked(InputEvent e, float x, float y) {
					if (e != null && e.getType().equals(InputEvent.Type.touchUp)) {

						// we then toggle this as the selected string
						if (selectedLabel == newLabel) {
							clearSelectedLabel();

						} else {
							clearSelectedLabel();
							selectedLabel = newLabel;
							newLabel.highlight();
						}

						System.out.println("Clicked on label " + string);
					}
				}
			});

			// add the label to the table
			table.add(newLabel).size(200, 20).padLeft(5);
			table.add().expandX();
			table.row();
		}

		// add a spacer before the list
		table.add().size(10, 10);
		table.row();

		scrollPane = new ScrollPane(table, (Skin) PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin));
		getContentTable().add(scrollPane).size(250, 300).pad(10,2,5,2).center();
		getContentTable().row();

		loadButton = new PTextButton("Load Selected File", (Skin) PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin));
		loadButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent e, float x, float y) {
				if (e != null && e.getType().equals(InputEvent.Type.touchUp)) {

					// then we attempt to load the file selected
					loadSelectedFile();
					System.out.println("Load Button");
				}
			}
		});


		deleteButton = new PTextButton("Delete Selected File",  (Skin) PixelAssetManager.manager.get(PixelAssetManager.defaultUISkin));
		deleteButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent e, float x, float y) {
				if (e != null && e.getType().equals(InputEvent.Type.touchUp)) {

					// then we attempt to delete the file selected
					deleteSelectedFile();
					System.out.println("Delete Button");
				}
			}
		});
		getContentTable().add(loadButton);
		getContentTable().row();
		getContentTable().add(deleteButton).padTop(2);
	}

	private void clearSelectedLabel() {
		if (selectedLabel != null) {
			selectedLabel.clearHighlight();
			selectedLabel = null;
		}
	}

	private void loadSelectedFile() {

		if (selectedLabel != null) {
			GameScene.reset();
			PixelCityGame.setActiveScreen(GameScene.getInstance());
			GameScene.getInstance().setNewGame(selectedLabel.getText().toString());

			if (!SerializationManager.getInstance().deserialize(selectedLabel.getText().toString())) {
				PixelCityGame.setActiveScreen(MenuScene.getInstance());
			}
			else {
				remove();
			}
		} else {
			System.out.println("No file selected to load.");
		}
	}

	private void deleteSelectedFile() {
		if (selectedLabel != null) {

			// attempt to open the file in the directory
			try {
				FileHandle file = Gdx.files.local(GameScene.savedGamesDirPath + selectedLabel.getText().toString() + ".city");
				file.delete();
			} catch (Exception e) {
				System.out.println("File could not be deleted: " + e.getMessage());
			}

			// we also remove from the atlas file the name of the file
			FileHandle atlasFile = Gdx.files.local(GameScene.savedGamesAtlasPath);
			String fileString = atlasFile.readString();
			fileString = fileString.replace(selectedLabel.getText().toString() + '\n', "");
			atlasFile.writeString(fileString, false);

			// we should also remove the label from the table as well
			scrollTable.removeActor(selectedLabel);
			selectedLabel = null;

		} else {
			System.out.println("No file selected to delete.");
		}
	}
}
