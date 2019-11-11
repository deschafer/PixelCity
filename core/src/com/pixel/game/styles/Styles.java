package com.pixel.game.styles;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class Styles {

	public static Label.LabelStyle testFinanceLabelStyle;
	public static Label.LabelStyle mapToolCostLabelStyle;


	public static void initialize() {

		// parameters for generating a custom bitmap font
		FreeTypeFontGenerator fontGenerator =
			   new FreeTypeFontGenerator(Gdx.files.internal("assets/kenneyFonts/Fonts/Kenney Blocks.ttf"));
		FreeTypeFontGenerator.FreeTypeFontParameter fontParameters = new FreeTypeFontGenerator.FreeTypeFontParameter();
		fontParameters.size = 36;
		fontParameters.color = Color.WHITE;
		fontParameters.borderWidth = 2;
		fontParameters.borderColor = Color.BLACK;
		fontParameters.borderStraight = true;
		fontParameters.minFilter = Texture.TextureFilter.Linear;
		fontParameters.magFilter = Texture.TextureFilter.Linear;

		BitmapFont customFont = fontGenerator.generateFont(fontParameters);

		testFinanceLabelStyle = new Label.LabelStyle();
		testFinanceLabelStyle.font = customFont;

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

	}

}
