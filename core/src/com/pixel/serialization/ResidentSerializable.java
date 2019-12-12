package com.pixel.serialization;

import com.pixel.map.MapCoord;
import com.pixel.object.Resident;

import java.io.Serializable;

public class ResidentSerializable implements Serializable {

	public int level;
	public String name;
	public boolean educated;
	public MapCoord employer;

	public Resident getNonSerializableObject() {

		Resident resident = new Resident(name);
		resident.setEducated(educated);
		resident.setLevel(level);
		resident.setEmployerMapPosition(employer);

		return resident;
	}
}
