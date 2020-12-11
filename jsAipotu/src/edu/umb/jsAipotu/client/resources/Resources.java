package edu.umb.jsAipotu.client.resources;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.TextResource;

public interface Resources extends ClientBundle {
	public static final Resources INSTANCE = GWT.create(Resources.class);
	
	@Source("default.greenhouse")
	public TextResource defaultGreenhouse();
	
	@Source("black.gif")
	public ImageResource blackFlowerImage();
	
	@Source("blank.gif")
	public ImageResource blankFlowerImage();
	
	@Source("blue.gif")
	public ImageResource blueFlowerImage();
	
	@Source("gray.gif")
	public ImageResource grayFlowerImage();
	
	@Source("green.gif")
	public ImageResource greenFlowerImage();
	
	@Source("orange.gif")
	public ImageResource orangeFlowerImage();
	
	@Source("purple.gif")
	public ImageResource purpleFlowerImage();
	
	@Source("red.gif")
	public ImageResource redFlowerImage();
	
	@Source("white.gif")
	public ImageResource whiteFlowerImage();
	
	@Source("yellow.gif")
	public ImageResource yellowFlowerImage();
	
	@Source("GeneticCode.gif")
	public ImageResource geneticCodeImage();
}
