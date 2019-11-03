package com.pixel.map.visualizer;

import com.pixel.map.object.MapObject;
import java.util.ArrayList;
import java.util.HashMap;

public class VisualizerFactory {

	private HashMap<String, MapObject> registeredTypes;
	private HashMap<String, ArrayList<Visualizer>> sortedVisualizers;     // lists for each different visualizer

	private static VisualizerFactory instance;

	// TODO: needs to be thread safe
	// TODO: singleton initialization

	private VisualizerFactory() {
		registeredTypes = new HashMap<>();
		sortedVisualizers = new HashMap<>();
	}

	public static VisualizerFactory getInstance() {
		if (instance == null) instance = new VisualizerFactory();
		return instance;
	}

	public Visualizer createVisualizer(String objectName) {

		// this needs to be thread safe
		// this edits the sorted objects, so we will need to protect this
		synchronized (this) {

			// we look in the hash map to see if there is a visualizer list made
			if (sortedVisualizers.containsKey(objectName)) {

				Visualizer visualizer = null;

				// then a list of visualizers exists
				ArrayList<Visualizer> list = sortedVisualizers.get(objectName);

				// now we check if a visualizer is available
				if (!list.isEmpty()) {

					// pop from the front of the list
					visualizer = list.get(0);
					list.remove(0);
				}
				// otherwise, we need to create a new visualizer
				else {

					visualizer = new Visualizer(0, 0, registeredTypes.get(objectName));
				}
				return visualizer;
			}
			return null;
		}
	}

	public void returnVisualizer(Visualizer visualizer) {

		// this needs to be thread safe
		// this edits the sorted objects, so we will need to protect this
		synchronized (this) {

			// first, ensure the object is removed from the stage
			visualizer.remove();

			// attempt to return a visualizer back to a list
			if (sortedVisualizers.containsKey(visualizer.getName())) {

				// then we return this object directly into this list
				ArrayList<Visualizer> list = sortedVisualizers.get(visualizer.getName());
				list.add(visualizer);
			}
		}
	}

	public void registerMapObject(MapObject object) {

		// this needs to be thread safe
		// this edits the sorted objects, so we will need to protect this
		synchronized (this) {

			// register the object
			registeredTypes.put(object.getName(), object);

			// create a new list of visualizers for it
			sortedVisualizers.put(object.getName(), new ArrayList<>());
		}
	}
}
