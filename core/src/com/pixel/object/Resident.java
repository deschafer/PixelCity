package com.pixel.object;

import com.pixel.map.MapCoord;
import com.pixel.map.object.building.Building;
import com.pixel.serialization.ResidentSerializable;

import java.io.Serializable;

public class Resident implements Serializable {

	private int level = 0;
	private String name;
	private Building residence = null;
	private Building employer = null;
	private float happiness = 0.0f;
	private boolean educated = false;
	private MapCoord employerMapPosition;

	public Resident(String name) {
		this.name = name;
	}

	public Building getEmployer() {
		return employer;
	}

	public void setEmployer(Building employer) {

		this.employer = employer;
		if(residence != null) {
			residence.updateHappiness();
		}
	}

	public void setEducated(boolean educated) {
		this.educated = educated;
	}

	public boolean isEducated() {
		return educated;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public void levelUp() { level++; }

	public Building getResidence() {
		return residence;
	}

	public boolean isUnemployed() {
		return employer == null;
	}

	public void setResidence(Building residence) {
		this.residence = residence;
	}

	public float getHappiness() {
		return happiness;
	}

	public void setHappiness(float happiness) {
		this.happiness = happiness;
	}

	public MapCoord getEmployerMapPosition() {
		return employerMapPosition;
	}

	public void setEmployerMapPosition(MapCoord employerMapPosition) {
		this.employerMapPosition = employerMapPosition;
	}

	public ResidentSerializable getSerializableObject() {
		ResidentSerializable serializable = new ResidentSerializable();
		serializable.educated = educated;
		serializable.level = level;
		serializable.name = name;

		if (employer != null) {
			serializable.employer = employer.getMapPosition();
		}

		return serializable;
	}
}

