package com.pixel.city;

import com.badlogic.gdx.Gdx;
import com.pixel.city.Financials.Source;
import com.pixel.serialization.FinancialSerializable;
import com.pixel.serialization.SourceSerializable;

import java.io.Serializable;
import java.util.ArrayList;

public class FinancialManager implements Serializable {

	private static FinancialManager instance = new FinancialManager();
	private float balance = 50000.0f;
	private ArrayList<Source> sources;

	private float updateTimer = 0;
	private static float updateTime = 1.0f;

	private FinancialManager() {
		sources = new ArrayList<>();
	}

	public static FinancialManager getInstance() {
		return instance;
	}

	public static void createFromSerializable(FinancialManager manager) {
		instance = manager;
	}

	public static void reset() {
		instance = new FinancialManager();
	}

	public void update() {

		synchronized (this) {

			updateTimer += Gdx.graphics.getDeltaTime();

			if (updateTimer >= updateTime) {

				ArrayList<Source> invalidSources = new ArrayList<>();

				for (Source source : sources) {

					// remove the source if its no longer valid
					if(!source.isValid()) {
						invalidSources.add(source);
					}
					// otherwise, grab the value of the source
					else {
						balance += source.act();
					}
				}

				for (Source source : invalidSources) {
					sources.remove(source);
				}

				updateTimer = 0;
			}
		}
	}

	public void addSource(Source source) {
		synchronized (this) {
			sources.add(source);
		}
	}
	public void removeSource(Source source) {
		synchronized (this) {
			sources.remove(source);
		}
	}
	public float getBalance() {
		synchronized (this) {
			return balance;
		}
	}
	public void addFunds(float amount) {
		synchronized (this) {
			balance += amount;
		}
	}
	public void withdrawFunds(float amount) {
		synchronized (this) {
			balance -= amount;
		}
	}
	public float getRevenue() {
		float revenue = 0;

		for (Source source : sources) {
			revenue += source.act();
		}
		return revenue;
	}

	public FinancialSerializable getSerializableObject() {

		FinancialSerializable serializable = new FinancialSerializable();
		serializable.balance = balance;

		// we do not replicate the sources because we need to get them from the buildings

		return serializable;
	}

	public void createFromSerializable(FinancialSerializable serializable) {

		balance = serializable.balance;

		// we do not replicate the sources because we need to get them from the buildings
	}
}


