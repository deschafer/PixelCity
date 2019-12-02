package com.pixel.city;

import com.badlogic.gdx.Gdx;
import com.pixel.city.Financials.Source;

import java.util.ArrayList;

public class FinancialManager {

	private static FinancialManager instance = new FinancialManager();
	private float balance = 50000.0f;
	private ArrayList<Source> sources;

	private float updateTimer = 0;
	private float updateTime = 1.0f;

	private FinancialManager() {
		sources = new ArrayList<>();
	}

	public static FinancialManager getInstance() {
		return instance;
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
}


