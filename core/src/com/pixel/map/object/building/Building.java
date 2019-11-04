package com.pixel.map.object.building;

import com.pixel.game.PixelAssetManager;

public class Building {

	public enum BuildingType {
		RESIDENTIAL(PixelAssetManager.residentialZoning, "ResidentialZoning"),
		COMMERCIAL(PixelAssetManager.commercialZoning, "CommercialZoning"),
		OFFICE(PixelAssetManager.officeZoning, "OfficeZoning");

		private String zoneTexture;
		private String zoneName;
		BuildingType(String zoneTexture, String zoneName) {
			this.zoneTexture = zoneTexture;
			this.zoneName = zoneName;
		}

		public String getZoneTexture() {
			return zoneTexture;
		}

		public String getZoneName() {
			return zoneName;
		}
	}


}
