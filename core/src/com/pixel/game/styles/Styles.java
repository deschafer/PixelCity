package com.pixel.game.styles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.pixel.game.PixelAssetManager;

public class Styles {

	public static Label.LabelStyle balanceLabelStyle;
	public static Label.LabelStyle mainMenuTitleLabelStyle;
	public static Label.LabelStyle mapToolCostLabelStyle;
	public static ImageButton.ImageButtonStyle closeButtonImageButtonStyle;
	public static TextButton.TextButtonStyle mainMenuTextButtonStyle;
	public static Label.LabelStyle highlightedLabelStyle;
	public static Label.LabelStyle greenBalanceLabelStyle;
	public static Label.LabelStyle redBalanceLabelStyle;
	public static TextButton.TextButtonStyle dialogButtonStyle;


	public static void initialize() {

		// parameters for generating a custom bitmap font
		FreeTypeFontGenerator fontGenerator =
			   new FreeTypeFontGenerator(Gdx.files.internal("assets/kenneyFonts/Fonts/Kenney Blocks.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter fontParameters = new FreeTypeFontGenerator.FreeTypeFontParameter();
		fontParameters.size = 24;
		fontParameters.color = Color.WHITE;
		fontParameters.borderWidth = 2;
		fontParameters.borderColor = Color.BLACK;
		fontParameters.borderStraight = true;
		fontParameters.minFilter = Texture.TextureFilter.Linear;
		fontParameters.magFilter = Texture.TextureFilter.Linear;

		BitmapFont customFont = fontGenerator.generateFont(fontParameters);

		balanceLabelStyle = new Label.LabelStyle();
		balanceLabelStyle.font = customFont;

		fontGenerator =
			   new FreeTypeFontGenerator(Gdx.files.internal("assets/kenneyFonts/Fonts/Kenney High.ttf"));
		fontParameters = new FreeTypeFontGenerator.FreeTypeFontParameter();
		fontParameters.size = 28;
		fontParameters.color = Color.WHITE;
		fontParameters.borderWidth = 2;
		fontParameters.borderColor = Color.BLACK;
		fontParameters.borderStraight = true;
		fontParameters.minFilter = Texture.TextureFilter.Linear;
		fontParameters.magFilter = Texture.TextureFilter.Linear;

		customFont = fontGenerator.generateFont(fontParameters);

		mapToolCostLabelStyle = new Label.LabelStyle();
		mapToolCostLabelStyle.font = customFont;

		fontParameters.size = 96;
		customFont = fontGenerator.generateFont(fontParameters);
		mainMenuTitleLabelStyle = new Label.LabelStyle();
		mainMenuTitleLabelStyle.font = customFont;

		closeButtonImageButtonStyle = new ImageButton.ImageButtonStyle();
		closeButtonImageButtonStyle.up = new TextureRegionDrawable(new TextureRegion((Texture)PixelAssetManager.manager.get(PixelAssetManager.closeButton)));
		closeButtonImageButtonStyle.down = new TextureRegionDrawable(new TextureRegion((Texture)PixelAssetManager.manager.get(PixelAssetManager.closeButton)));

		mainMenuTextButtonStyle = new TextButton.TextButtonStyle();
		Texture   buttonTex   = PixelAssetManager.manager.get(PixelAssetManager.blueRectangle);
		NinePatch buttonPatch = new NinePatch(buttonTex, 5,5,5,5);
		mainMenuTextButtonStyle.up    = new NinePatchDrawable( buttonPatch );
		fontParameters.size = 32;
		fontParameters.borderWidth = 1;
		customFont = fontGenerator.generateFont(fontParameters);
		mainMenuTextButtonStyle.font      = customFont;
		mainMenuTextButtonStyle.fontColor = Color.WHITE;

		dialogButtonStyle = new TextButton.TextButtonStyle();
		buttonTex   = PixelAssetManager.manager.get(PixelAssetManager.blueRectangle);
		buttonPatch = new NinePatch(buttonTex, 5,5,5,5);
		dialogButtonStyle.up = new NinePatchDrawable( buttonPatch );
		fontParameters.size = 20;
		fontParameters.borderWidth = 1;
		customFont = fontGenerator.generateFont(fontParameters);
		dialogButtonStyle.font      = customFont;
		dialogButtonStyle.fontColor = Color.WHITE;

		fontGenerator =
			   new FreeTypeFontGenerator(Gdx.files.internal("assets/fonts/OpenSans.ttf"));
		fontParameters = new FreeTypeFontGenerator.FreeTypeFontParameter();
		fontParameters.size = 18;
		fontParameters.color = Color.WHITE;
		fontParameters.borderWidth = 1;
		fontParameters.borderColor = Color.BLACK;

		customFont = fontGenerator.generateFont(fontParameters);
		highlightedLabelStyle = new Label.LabelStyle();
		highlightedLabelStyle.font = customFont;

		fontParameters = new FreeTypeFontGenerator.FreeTypeFontParameter();
		fontParameters.color = Color.GREEN;
		fontParameters.borderWidth = 1;
		fontParameters.borderColor = Color.BLACK;

		customFont = fontGenerator.generateFont(fontParameters);
		greenBalanceLabelStyle = new Label.LabelStyle();
		greenBalanceLabelStyle.font = customFont;

		fontParameters = new FreeTypeFontGenerator.FreeTypeFontParameter();
		fontParameters.color = Color.RED;
		fontParameters.borderWidth = 1;
		fontParameters.borderColor = Color.BLACK;

		customFont = fontGenerator.generateFont(fontParameters);
		redBalanceLabelStyle = new Label.LabelStyle();
		redBalanceLabelStyle.font = customFont;

	}

}
