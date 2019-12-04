package com.pixel.serialization;

import com.pixel.city.City;
import com.pixel.city.FinancialManager;
import com.pixel.scene.GameScene;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class SerializationManager {


	public void serialize(String filename) {
		// Serialization
		try {
			filename += ".city";

			FileOutputStream file = new FileOutputStream(filename);
			ObjectOutputStream out = new ObjectOutputStream(file);

			// serialize the map
			GameScene.getInstance().getGameMap().serialize(out);

			// serialize the city class
			out.writeObject(City.getInstance());

			// write the financial class
			out.writeObject(FinancialManager.getInstance());

			out.close();
			file.close();
		}
		catch (IOException ex) {
			System.out.println("IOException during serialization " + ex.getMessage());
		}
	}

	public void deserialize() {

	}

}
