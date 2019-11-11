package com.pixel.city.Financials;

import com.pixel.map.object.MapObject;

public class Source {

	private MapObject sourceObject;
	private float change = 0.0f;
	private boolean expense = false;
	private boolean valid = true;

	public Source(MapObject sourceObject, float change) {
		this.sourceObject = sourceObject;
		this.change = change;

		if(change < 0) {
			expense = true;
		}
	}

	//
	// Act()
	// In this context, the financial manager adds this to the current balance
	// will return a positive or negative float
	//
	public float act()
	{
		if(sourceObject == null) {
			valid = false;
			return 0;
		}

		return change;
	}

	public boolean isValid() {
		return valid;
	}
	public void setInvalid() {
		valid = false;
	}
}
