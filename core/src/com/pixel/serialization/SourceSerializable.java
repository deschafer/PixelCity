package com.pixel.serialization;

import com.pixel.city.FinancialManager;
import com.pixel.city.Financials.Source;
import com.pixel.map.object.MapObject;

import java.io.Serializable;

public class SourceSerializable implements Serializable {

	public float change;
	public boolean expense;
	public boolean valid;

	public Source getNonSerializableObject(MapObject owningObject) {

		Source source = new Source(owningObject, change);
		return source;
	}
}
