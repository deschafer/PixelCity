package com.pixel.city.Financials;

import com.pixel.map.object.MapObject;
import com.pixel.serialization.SourceSerializable;

import java.io.Serializable;

public class Source implements Serializable {

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
		synchronized (this) {

			if (sourceObject == null) {
				valid = false;
				return 0;
			}

			return change;
		}
	}

	public void setChange(float change) {
		synchronized (this) {
			this.change = change;
		}
	}

	public boolean isValid() {
		synchronized (this) {
			return valid;
		}
	}
	public void setInvalid() {
		synchronized (this) {
			valid = false;
		}
	}

	public SourceSerializable getSerializableObject() {

		SourceSerializable sourceSerializable = new SourceSerializable();
		sourceSerializable.change = change;
		sourceSerializable.expense = expense;
		sourceSerializable.valid = valid;

		return sourceSerializable;
	}
}
