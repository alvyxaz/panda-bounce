package com.pandabounce.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.math.Rectangle;
import com.pandabounce.MyGame;
import com.pandabounce.controls.Input;
import com.pandabounce.resources.Art;

public class GuideScreen extends BaseScreen {

	private Rectangle next;
	private Rectangle back;

	public int currentPage = 0;
	private static final int MAIN = 0;
	private static final int COMBO = 1;
	private static final int HEDGEHOG = 2;
	private static final int WASP = 3;
	private static final int SURPRISE_BOX = 4;
	
	private int titleY;
	private int pictureY;
	private int textY;
	
	private float transitionOpacity = 0;
	
	public GuideScreen(MyGame game) {
		super(game);
		
		next = new Rectangle(MyGame.SCREEN_WIDTH - 10 - Art.guiButtonSmall.getRegionWidth(), 10,
				Art.guiButtonSmall.getRegionWidth(), Art.guiButtonSmall.getRegionHeight());
		back = new Rectangle(10, 10,
				Art.guiButtonSmall.getRegionWidth(), Art.guiButtonSmall.getRegionHeight());
		
		titleY = MyGame.SCREEN_HEIGHT-20;
		pictureY = titleY - Art.hedgehog.getRegionHeight() - 50;
		textY = pictureY - 30;
	}

	@Override
	public void draw(float deltaTime) {
		spriteBatch.begin();

		spriteBatch.disableBlending();
		spriteBatch.draw(Art.background, 0, 0);
		spriteBatch.enableBlending();
		
		
		
		switch(currentPage){
		case MAIN:
			drawTitle("Main Goal");
			spriteBatch.draw(Art.star, 
					MyGame.SCREEN_HALF_WIDTH - Art.star.getRegionWidth()/2, pictureY);
			drawText("Pick up stars to collect as many points as possible.\n\n" +
					"If you pick up two stars in one slide, you'll start a combo.\n\n");
			break;
		case COMBO:
			drawTitle("COMBO");
			// Draw multiplier
			int multiplierX = MyGame.SCREEN_HALF_WIDTH - Art.guiMultiplierBar.getRegionWidth()/2;
			int multiplierY = pictureY;
			spriteBatch.draw(Art.guiMultiplierBar, multiplierX, multiplierY);
			Art.fontKomika24Gold.draw(spriteBatch, "6", multiplierX + 50, multiplierY + 40);

			drawText("Every additional star picked up in a combo mode increases combo multiplier \n\n" +
					"Getting hit disables combo.\n\n" +
					"If you don't pick up a star within 2 seconds, multiplier is divided by two");
			
			break;
		case HEDGEHOG:
			drawTitle("HEDGEHOGS");
			spriteBatch.draw(Art.hedgehog, 
					MyGame.SCREEN_HALF_WIDTH - Art.hedgehog.getRegionWidth()/2, pictureY);
			drawText("Cute creatures, but avoid them if you don't want to be hurt.");
			break;
		case WASP:
			drawTitle("WASP");
			spriteBatch.draw(Art.bee[0], 
					MyGame.SCREEN_HALF_WIDTH - Art.bee[0].getRegionWidth()/2, pictureY);
			drawText("Not dangerous, unless angry.\n\n If there's an exclamation mark - prepare to dodge");
			break;
		case SURPRISE_BOX:
			drawTitle("SURPRISE BOX");
			spriteBatch.draw(Art.box, 
					MyGame.SCREEN_HALF_WIDTH - Art.box.getRegionWidth()/2, pictureY-10);
			drawText("After picking up this box, you'll receive a random effect.\n\n " +
					"Effects can be either negative or positive\n\n" +
					"Mitigating negative effects takes practice");
			break;
		}
		
		// Back
		spriteBatch.draw(Art.guiButtonSmall, back.x, back.y);
		if(currentPage != MAIN){
			Art.fontKomika24Gold.drawWrapped(spriteBatch, "Back", 
					back.x, back.y+ back.height - 16, back.width, HAlignment.CENTER);
		} else {
			Art.fontKomika24Gold.drawWrapped(spriteBatch, "Menu", 
					back.x, back.y+ back.height - 16, back.width, HAlignment.CENTER);
		}
		// Next
		spriteBatch.draw(Art.guiButtonSmall, next.x, next.y);
		if(currentPage < SURPRISE_BOX){
			Art.fontKomika24Gold.drawWrapped(spriteBatch, "Next", 
					next.x, next.y+ next.height - 16, next.width, HAlignment.CENTER);
		} else {
			Art.fontKomika24Gold.drawWrapped(spriteBatch, "Menu", 
					next.x, next.y+ next.height - 16, next.width, HAlignment.CENTER);
		}
		
		if(transitionOpacity != 1 && transitionOpacity != 0){
			spriteBatch.setColor(0, 0, 0, transitionOpacity);
			spriteBatch.draw(Art.px, 0, 0, MyGame.SCREEN_WIDTH,
					MyGame.SCREEN_HEIGHT);
			spriteBatch.setColor(Color.WHITE);
		}
		spriteBatch.end();
	}
	
	public void drawText(String text){
		Art.fontKomika24Gold.setScale(1f);
		Art.fontKomika24Gold.drawWrapped(spriteBatch, text, 
				10, textY, MyGame.SCREEN_WIDTH-20, HAlignment.CENTER);
		Art.fontKomika24Gold.setScale(1f);
	}
	
	public void drawTitle(String title){
		Art.fontKomika24Gold.setScale(1.4f);
		Art.fontKomika24Gold.drawWrapped(spriteBatch, title, 
				10, titleY, MyGame.SCREEN_WIDTH-20, HAlignment.CENTER);
		Art.fontKomika24Gold.setScale(1f);
	}

	@Override
	public void update(float deltaTime) {
		if(Input.isReleasing(next)){
			if(currentPage == SURPRISE_BOX){
				this.switchScreenTo(game.screenTitle);
			} else {
				currentPage ++;
			}
		}
		if(Input.isReleasing(back)){
			if(currentPage == MAIN){
				this.switchScreenTo(game.screenTitle);
			} else {
				currentPage --;
			}
		}
	}

	@Override
	public void prepare() {
		
	}

	@Override
	public boolean beforeScreenSwitch(float deltaTime) {
		transitionOpacity += deltaTime * 2;
		if (transitionOpacity > 1) {
			transitionOpacity = 0;
			currentPage = 0; 
			return true;
		}
		return false;
	}

}
