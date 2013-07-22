package com.pandabounce.entities;

import com.badlogic.gdx.graphics.g2d.BitmapFont.HAlignment;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.pandabounce.Game;
import com.pandabounce.resources.Art;

public class GuiEffect {
	
	Panda panda;
	
	float x;
	float y;
	float fontY;
	float width;
	
	public GuiEffect(Panda panda){
		this.panda = panda;
		x = Game.SCREEN_HALF_WIDTH - Art.guiEffectBar.getRegionWidth()/2;
		y = 12;
		width = Art.guiEffectBar.getRegionWidth();
		fontY = y+Art.fontKomika24.getLineHeight() - 7;
	}
	
	public void draw(SpriteBatch spriteBatch){
		
		if(panda.effectType > 0){
			if(panda.effectTimer < 1f){
				if((panda.effectTimer*10)%2 > 1){
					return;
				}
			}
			
			spriteBatch.draw(Art.guiEffectBar, x, y);
			
			switch(panda.effectType){
			case 1:
				Art.fontKomika24Gold.drawWrapped(spriteBatch, "Confused", 
						x, fontY, width, HAlignment.CENTER);
				break;
			case 2:
				Art.fontKomika24Gold.drawWrapped(spriteBatch, "Slower World", 
						x, fontY, width, HAlignment.CENTER);
				break;
			case 3: 
				Art.fontKomika24Gold.drawWrapped(spriteBatch, "Lazy", 
						x, fontY, width, HAlignment.CENTER);
				break;
			case 4: 
				Art.fontKomika24Gold.drawWrapped(spriteBatch, "Faster World", 
						x, fontY, width, HAlignment.CENTER);
				break;
			case 5: 
				Art.fontKomika24Gold.drawWrapped(spriteBatch, "Agile Panda", 
						x, fontY, width, HAlignment.CENTER);
				break;
			case 6: 
				Art.fontKomika24Gold.drawWrapped(spriteBatch, "2x pts & dmg", 
						x, fontY, width, HAlignment.CENTER);
				break;
			case 7: 
				Art.fontKomika24Gold.drawWrapped(spriteBatch, "Berserk", 
						x, fontY, width, HAlignment.CENTER);
				break;
			case 8: 
				Art.fontKomika24Gold.drawWrapped(spriteBatch, "Invincible", 
						x, fontY, width, HAlignment.CENTER);
				break;
			}
		}
		
	}
	
}
