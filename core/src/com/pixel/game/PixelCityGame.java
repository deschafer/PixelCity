package com.pixel.game;

import com.badlogic.gdx.graphics.Texture;
import com.pixel.scene.GameScene;

public class PixelCityGame extends AbstractGame
{

	public PixelCityGame(int width, int height) {

	}

	public void create()
	{
		// initialize all game assets

		// so far, we need the road texture and we need the grass texture
		PixelAssetManager.manager.load(PixelAssetManager.roadwayNorthSouth, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.roadwayEastWest, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.grass, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.roadwayEndWest, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.roadwayEndEast, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.roadwayEndSouth, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.roadwayEndNorth, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.cornerEastNorth, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.cornerNorthEast, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.cornerNorthWest, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.cornerWestNorth, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.intersectionFour, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.intersectionThreeSouth, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.intersectionThreeEast, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.intersectionThreeWest, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.intersectionThreeNorth, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.residentialZoning, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.commercialZoning, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.officeZoning, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.residentialBase22, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.residentialBase14, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.residentialBase30, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.residentialBase37, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.residentialStory32, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.residentialStory38, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.residentialStory39, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.residentialStory43, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.residentialStory44, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.residentialStory45, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.residentialStory47, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.residentialStory48, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.residentialStory49, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.residentialStory50, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.residentialStory51, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.residentialStory52, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.residentialStory53, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.residentialStory54, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.residentialStory55, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.residentialStory56, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.residentialRoof57, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.residentialRoof58, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.residentialRoof59, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.residentialRoof60, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.residentialRoof61, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.residentialRoof62, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.residentialRoof63, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.residentialRoof64, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.residentialRoof65, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.residentialRoof66, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.residentialRoof67, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.residentialRoof68, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.residentialRoof69, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.residentialRoof70, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.residentialRoof71, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.residentialRoof72, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.residentialRoof73, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.residentialRoof74, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.residentialRoof75, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.residentialRoof76, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.residentialRoof77, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.residentialRoof80, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.residentialRoof81, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.residentialRoof82, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.residentialRoof83, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.residentialRoof84, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.residentialRoof88, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.residentialRoof89, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.residentialRoof90, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.residentialRoof91, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.residentialRoof96, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.residentialRoof97, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.residentialRoof98, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.residentialRoof104, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.residentialRoof105, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.residentialRoof112, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.commercialBase18, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.commercialBase20, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.commercialBase34, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.commercialBase04, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.commercialBase108, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.commercialBase113, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.commercialBase115, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.commercialBase109, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.commercialBase101, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.commercialStory00, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.commercialStory07, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.commercialStory08, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.commercialStory16, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.commercialStory24, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.commercialRoof05, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.commercialRoof06, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.commercialRoof13, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.commercialRoof79, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.commercialRoof86, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.commercialRoof87, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.commercialRoof91, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.commercialRoof111, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.commercialRoof119, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.commercialRoof118, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.commercialRoof120, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.commercialRoof121, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.commercialRoof126, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.commercialRoof127, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.commercialRoof128, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.officeBase02, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.officeBase10, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.officeBase109, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.officeBase125, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.officeBase117, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.officeBase123, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.officeStory07, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.officeStory15, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.officeStory23, Texture.class);
		PixelAssetManager.manager.load(PixelAssetManager.officeStory31, Texture.class);

		PixelAssetManager.manager.finishLoading();

		super.create();

		setActiveScreen( GameScene.getInstance() );
	}
}