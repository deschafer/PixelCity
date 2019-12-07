package com.pixel.serialization;

import com.pixel.city.City;
import com.pixel.city.FinancialManager;
import com.pixel.scene.GameScene;

import java.io.*;

public class SerializationManager {

	private static SerializationManager instance = new SerializationManager();

	private SerializationManager() { }

	public static SerializationManager getInstance() {
		return instance;
	}

	public void serialize(String filename) {
		// Serialization
		try {
			filename += ".city";

			FileOutputStream file = new FileOutputStream(filename);
			ObjectOutputStream out = new ObjectOutputStream(file);

			// serialize the map
			GameScene.getInstance().getGameMap().serialize(out);

			// serialize the city class
			//out.writeObject(City.getInstance());

			// write the financial class
			//out.writeObject(FinancialManager.getInstance());

			// Process description
			//
			// first save the city and financial classes
			//
			// then we save the game map and all associated objects

			out.close();
			file.close();
		} catch (IOException ex) {
			System.out.println("IOException during serialization " + ex.getMessage());
		}
	}

	public void deserialize(String filename) {

		filename = "city.city";

		// Deserialization
		try
		{


			// Process description
			//
			// first load in the city and financial classes
			// add the list of loaded in residents to another list that we can access later
			//
			// then we need to load in the game map
			// after this step is completed, we need to access a list of saved zonecells that need to be assigned
			// to their respective zones
			// and we also need to then assign the residents back to their buildings

			// where are these lists going to reside?
			// the loaded in residents can reside in the city class
			// the loaded in zone cells that need to be assigned can be located in the map class



			// Reading the object from a file
			FileInputStream file = new FileInputStream(filename);
			ObjectInputStream in = new ObjectInputStream(file);

			// Method for deserialization of object
			City city = (City) in.readObject();
			City.createFromSerializable(city);

			// Method for deserialization of object
			FinancialManager financialManager = (FinancialManager) in.readObject();
			FinancialManager.createFromSerializable(financialManager);

			in.close();
			file.close();

			City.getInstance();
			FinancialManager.getInstance();
		}

		catch(IOException ex)
		{
			System.out.println("IOException during deserialization " + ex.getMessage());
		}

		catch(ClassNotFoundException ex)
		{
			System.out.println("ClassNotFoundException is caught");
		}
	}

}
