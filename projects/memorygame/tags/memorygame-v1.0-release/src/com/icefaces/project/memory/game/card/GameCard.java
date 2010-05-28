package com.icefaces.project.memory.game.card;

import com.icesoft.faces.context.effects.Appear;
import com.icesoft.faces.context.effects.Effect;

public class GameCard {
	public static final int PIXEL_WIDTH = 60;
	public static final int PIXEL_HEIGHT = 60;
	
	private int indexType;
	private String fileName;
	private String frontImage;
	private String backImage;
	private boolean isFlipped = false;
	
	private Effect flipEffect;
	
	public GameCard(int indexType, String fileName,
				    String frontImage, String backImage) {
		this.indexType = indexType;
		this.fileName = fileName;
		this.frontImage = frontImage;
		this.backImage = backImage;
		
		init();
	}
	
	public GameCard(GameCard clone) {
		this(clone.getIndexType(),
			 clone.getFileName(),
			 clone.getFrontImage(),
			 clone.getBackImage());
	}
	
	protected void init() {
		flipEffect = new Appear();
		flipEffect.setDuration(0.35f);
		flipEffect.setFired(true);
	}
	
	public void prepEffect() {
		flipEffect.setFired(false);
	}

	public int getIndexType() {
		return indexType;
	}

	public void setIndexType(int indexType) {
		this.indexType = indexType;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	
	public String getFrontImage() {
		return frontImage;
	}

	public void setFrontImage(String frontImage) {
		this.frontImage = frontImage;
	}

	public String getBackImage() {
		return backImage;
	}

	public void setBackImage(String backImage) {
		this.backImage = backImage;
	}
	
	public boolean getIsFlipped() {
		return isFlipped;
	}

	public void setIsFlipped(boolean isFlipped) {
		this.isFlipped = isFlipped;
	}

	public Effect getFlipEffect() {
		return flipEffect;
	}

	public void setFlipEffect(Effect flipEffect) {
		this.flipEffect = flipEffect;
	}
	
	public int getPixelWidth() {
		return PIXEL_WIDTH;
	}

	public int getPixelHeight() {
		return PIXEL_HEIGHT;
	}

	public String getImage() {
		return isFlipped ? getFrontImage() : getBackImage();
	}
	
	public void flip() {
		prepEffect();
		isFlipped = !isFlipped;
	}
	
	public void unflip() {
		flip();
	}
}
