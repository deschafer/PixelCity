package com.pixel.object;

import com.pixel.map.object.building.Building;

import java.io.Serializable;

public class Resident implements Serializable {

	private int level = 0;
	private String name;
	private Building residence = null;
	private Building employer = null;
	private float happiness = 0.0f;
	private boolean educated = false;

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
}

