package edu.umb.jsVGL.client.PhenotypeImages;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import GeneticModels.Phenotype;
import GeneticModels.Trait;
import VGL.VGLII;

public class PhenotypeImageMaker {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ArrayList<Phenotype> phenotypes;
	private Graphics2D g2d;
	public static Graphics2D g = null; // this is to be used for the display


	public ImageIcon makeImage(ArrayList<Phenotype> phenotypes) {
		this.phenotypes = phenotypes;
		BufferedImage image = 
			new BufferedImage(VGLII.PHENO_IMAGE_WIDTH, VGLII.PHENO_IMAGE_HEIGHT, BufferedImage.TYPE_3BYTE_BGR);
		g2d = (Graphics2D)image.createGraphics();

		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, VGLII.PHENO_IMAGE_WIDTH, VGLII.PHENO_IMAGE_HEIGHT);

		String Original = "Original" ; 
		String Body = "Body";
		String Eye = "Eyes";
		String Antenna = "Antenna";
		String Wing = "Wing" ; 
		String Leg = "Leg"; 
		String Important = null; // this is the part we stick on the end to get build the bug
		int WingNumber = 2 ;
		int LegNumber = 6 ;
		int AntennaNumber = 2 ; 
		int SL =0 ; // THIS is to re-adjust the short leg when its being printed
		int LL = 0 ; // THIS is to re-adjust the zigzag leg when its being printed
		int ZL = 0;
		int ZA = 0 ;
		int BWU = 0; // THIS IS TO MOVE THE BENT WING UP
		int BWS = 0 ; // this is to move the bent wing side ways 
		int LW = 0 ; // TO ADJUST LONG WING
		int LW1 = 0 ;
		int LW2 = 0 ; 
		int LW3 = 0 ; 

		int SW = 0  ; // TO ADJUST SHORT WING 
		int SWU = 0;//TO ADJUST SHORT WING 
		int SWUU = 0 ; //TO ADJUST SHORT WING 
		int BB = 0 ; 
		int BF = 0 ;
		int BK  = 0;
		int BL = 0 ; 
		int BP = 0 ;
		int BZ = 0 ; 
		int BS = 0 ; 
		int EB = 0;


		// new stuff
		Phenotype p = findPhenotypeMatching("Body", "Shape");
		if ( p == null ){
			Body = Body+Original;
		} else {
			Important = p.toString();

			Important = FindLast(Important);
			Body =Body+Important;

			if (Important.compareTo("Bent")== 0 ){
				BB = 8 ;

			}
			if (Important.compareTo("Forked")== 0 ){
				BF = 2 ; 
			}
			if (Important.compareTo("Knobbed")== 0 ){
				BK = 5 ;
			}

			if (Important.compareTo("Long")== 0 ){
				BL = 5 ;
			}


			if (Important.compareTo("Short")== 0 ){
				BS = 6 ;
			}

			if (Important.compareTo("Pointy")== 0 ){
				BP = 2 ; 
			}

			if (Important.compareTo("Zigzag")== 0 ){
				BZ = 5; 
			}

		}

		p = findPhenotypeMatching("Body", "Color");
		if ( p == null ){
			Body = Body+"Black";
		} else {
			Important = p.toString();
			Important = FindLast(Important);
			Body =Body+Important;
		}

		p = findPhenotypeMatching("Eye", "Color");
		if ( p == null ){
			Eye = Eye+"Black";
		} else {
			Important = p.toString();

			Important = FindLast(Important);
			Eye =Eye+Important;
			if (Important.compareTo("Black")== 0 ){
				EB = 4 ;
			}
		}

		p = findPhenotypeMatching("Antenna", "Shape");
		if ( p == null ){
			Antenna = Antenna+Original;
		} else {
			Important = p.toString();
			Important = FindLast(Important);
			Antenna = Antenna+Important;
		}

		p = findPhenotypeMatching("Antenna", "Color");
		if ( p == null ){
			Antenna = Antenna+"Black";
		} else {
			Important = p.toString();
			Important = FindLast(Important);
			Antenna = Antenna+Important;
		}

		p = findPhenotypeMatching("Antenna", "Number");
		if ( p == null ){

		} else {
			Important = p.toString();

			Important= FindLast(Important);
			AntennaNumber = getIntFromString(Important); // reused prof whites code ( thank you )
		}

		p = findPhenotypeMatching("Wing", "Shape");
		if ( p == null ){
			Wing = Wing+Original; 
		} else {
			Important = p.toString();
			Important = FindLast(Important);
			Wing = Wing+Important;
			if (Important.compareTo("Bent")== 0 ){
				BWS = 8 ;
				BWU = 30;
			}
			if (Important.compareTo("Short")== 0 ){
				SW =  53 ;
				SWU = 10 ;
				SWUU = 20 ;
			}
			if (Important.compareTo("Long")== 0 ){
				LW = 75 ; 
				LW1 = 60  ;
				LW2 = 24 ; 
				LW3 = 50 ; 
			}
		}



		p = findPhenotypeMatching("Wing", "Color");
		if ( p == null ){
			Wing= Wing+"Gray";
		} else {
			Important = p.toString();
			Important = FindLast(Important);
			Wing =Wing+Important;
		}

		p = findPhenotypeMatching("Wing", "Number");
		if ( p == null ){

		} else {
			Important = p.toString();
			Important = FindLast(Important);
			WingNumber = getIntFromString(Important);
		}


		p = findPhenotypeMatching("Leg", "Shape");

		if ( p == null ){
			Leg = Leg+Original;
		} else {
			Important = p.toString();
			Important = FindLast(Important);
			Leg = Leg+Important;
			if(Important.compareTo("Short")== 0 ){
				SL = 28;
			}
			if (Important.compareTo("Long")==0 ){
				LL = 20;
				ZA = 6 ; 
			}
			if (Important.compareTo("Zigzag")==0 ){
				ZL = 5;
			}
		}

		p = findPhenotypeMatching("Leg", "Color");

		if ( p == null ){
			Leg = Leg+"Black";
		} else {
			Important = p.toString();
			Important = FindLast(Important);
			Leg = Leg+Important;
		}

		p = findPhenotypeMatching("Leg", "Number");	
		if ( p == null ){
		} else {
			Important = p.toString();
			Important = FindLast(Important);
			LegNumber =getIntFromString(Important);
		}

		BufferedImage BodyImg=null; // for body
		BufferedImage EyeColor=null; // eye color
		BufferedImage Leg1=null; // leg org color
		BufferedImage LegRight=null; // for legright
		BufferedImage LegLeft=null; // for legleft
		BufferedImage Antenna1=null; // for org antenna
		BufferedImage AntennaRight=null; // right side antennas
		BufferedImage AntennaLeft=null; // left side antennas
		BufferedImage Wing1 = null;// org wing image to be fliped and scalled
		BufferedImage WingRight=null; // RIGHT WING
		BufferedImage WingLeft=null; // LEFT WING 

		Body = Body+".gif" ;
		Eye  =Eye+".gif";
		Antenna = Antenna+".gif";
		if (Leg.compareTo("LegOriginalBlack") == 0 ){
			Leg = Leg+".GIF";
		} else {
			Leg = Leg+".gif";
		}
		Wing = Wing+".gif" ; 
		BodyImg = loadImage("Body/"+Body); 
		EyeColor = loadImage("Eyes/"+Eye);
		Wing1 = loadImage("Wings/"+Wing);
		Leg1 = loadImage("Legs/"+Leg);
		Antenna1 = loadImage("Antenna/"+Antenna);

		RemoveBackGround(Antenna1);
		RemoveBackGround(EyeColor);
		RemoveBackGround(Leg1);
		RemoveBackGround(BodyImg);

		RemoveBackGround(Wing1);

		LegLeft = zoomOut(Leg1,4);
		LegRight = flipImage(zoomOut(Leg1,4));
		AntennaRight = TranslucentImage(Antenna1);
		AntennaLeft = TranslucentImage(flipImage(Antenna1));

		WingRight =  TranslucentImage(flipImage(zoomOut(Wing1,2)));
		WingLeft =  TranslucentImage(zoomOut(Wing1,2));

		switch(AntennaNumber){
		case(0):{

			break;
		}
		case(1):{
			g2d.drawImage(AntennaRight, null, 455 , 85);
			break;
		}
		case(2):{

			g2d.drawImage(AntennaLeft, null, 400 , 85);
			g2d.drawImage(AntennaRight, null, 455 , 85);
			break;
		}

		case(3):{
			g2d.drawImage(AntennaLeft, null, 400 , 85);
			g2d.drawImage(AntennaRight, null, 455 , 85);
			g2d.drawImage(AntennaRight, null, 445 , 85);
			break;
		}
		case(4):{
			g2d.drawImage(AntennaLeft, null, 400 , 85);
			g2d.drawImage(AntennaRight, null, 455 , 85);
			g2d.drawImage(AntennaRight, null, 445 , 85);
			g2d.drawImage(AntennaLeft, null, 410 , 85);
			break;
		}
		case(5):{
			g2d.drawImage(AntennaLeft, null, 400 , 85);
			g2d.drawImage(AntennaRight, null, 455 , 85);
			g2d.drawImage(AntennaRight, null, 445 , 85);
			g2d.drawImage(AntennaLeft, null, 410 , 85);

			AntennaLeft = rotateImage(AntennaLeft,-20);
			AntennaRight = rotateImage(AntennaRight,20);
			g2d.drawImage(AntennaLeft, null, 387 , 90);
			break;
		}
		case(6):{
			g2d.drawImage(AntennaLeft, null, 400 , 85);
			g2d.drawImage(AntennaRight, null, 455 , 85);
			g2d.drawImage(AntennaRight, null, 445 , 85);
			g2d.drawImage(AntennaLeft, null, 410 , 85);

			AntennaLeft = rotateImage(AntennaLeft,-20);
			AntennaRight = rotateImage(AntennaRight,20);
			g2d.drawImage(AntennaLeft, null, 387 , 90);
			g2d.drawImage(AntennaRight, null, 465 , 90);
			break;
		}

		}


		switch(LegNumber){
		case(0):{

			break;
		}
		case(1):{
			g2d.drawImage(rotateImage(LegLeft,5+(ZA)), null, (ZL)+289+(SL)-(LL), 175);
			break;
		}
		case(2):{
			g2d.drawImage(rotateImage(LegLeft,5+(ZA)), null, (ZL)+289+(SL)-(LL), 175);
			g2d.drawImage(rotateImage(LegRight,-5-(ZA)), null, (LL)+400-(SL), 175);
			break;
		}

		case(3):{
			g2d.drawImage(rotateImage(LegLeft,5+(ZA)), null, (ZL)+289+(SL)-(LL), 175);
			g2d.drawImage(rotateImage(LegRight,-5-(ZA)), null, (LL)+400-(SL), 175);

			LegLeft = rotateImage(LegLeft,200);

			g2d.drawImage(LegLeft, null, 398-(SL+5)+LL, 220);

			break;
		}
		case(4):{
			g2d.drawImage(rotateImage(LegLeft,5+(ZA)), null, (ZL)+289+(SL)-(LL), 175);
			g2d.drawImage(rotateImage(LegRight,-5-(ZA)), null, (LL)+400-(SL), 175);

			LegLeft = rotateImage(LegLeft,200);
			LegRight = rotateImage(LegRight,160);

			g2d.drawImage(LegLeft, null, 398-(SL+5)+LL, 220);
			g2d.drawImage(LegRight, null, 290+(SL+5)-LL, 220);
			break;
		}
		case(5):{
			g2d.drawImage(rotateImage(LegLeft,5+(ZA)), null, (ZL)+289+(SL)-(LL), 175);
			g2d.drawImage(rotateImage(LegRight,-5-(ZA)), null, (LL)+400-(SL), 175);


			LegLeft = rotateImage(LegLeft,200);
			LegRight = rotateImage(LegRight,160);
			g2d.drawImage(LegLeft, null, 398-(SL+5)+LL, 220);
			g2d.drawImage(LegRight, null, 290+(SL+5)-LL, 220);

			g2d.drawImage(LegLeft, null, 398-(SL+5)+LL, 250);
			break;
		}
		case(6):{
			g2d.drawImage(rotateImage(LegLeft,5+(ZA)), null, (ZL)+289+(SL)-(LL), 175);
			g2d.drawImage(rotateImage(LegRight,-5-(ZA)), null, (LL)+400-(SL), 175);


			LegLeft = rotateImage(LegLeft,200);
			LegRight = rotateImage(LegRight,160);


			g2d.drawImage(LegLeft, null, 398-(SL+5)+LL, 220);
			g2d.drawImage(LegRight, null, 290+(SL+5)-LL, 220);


			g2d.drawImage(LegLeft, null, 398-(SL+5)+LL, 250);
			g2d.drawImage(LegRight, null, 290+(SL+5)-LL , 250);
			break;
		}

		}



		g2d.drawImage(BodyImg, null, 420+(BB)-(BF)+(BK)+(BL)+(BP)+(BZ)+(BS), 148);

		g2d.drawImage(EyeColor, null, 420, 148);

		switch(WingNumber){
		case(0):{

			break;
		}
		case(1):{

			g2d.drawImage(WingRight, null, 273+(BWS)-(SW)+(LW), 60-(BWU)-(SWU));
			break;
		}
		case(2):{

			g2d.drawImage(WingRight, null, 273+(BWS)-(SW)+(LW), 60-(BWU)-(SWU));

			g2d.drawImage(WingLeft, null, -33-(BWS)+(SW)-(LW), 60-(BWU)-(SWU));	
			break;
		}

		case(3):{

			g2d.drawImage(rotateImage(WingLeft,25), null, -31+(BWS)+(SW)-(LW), 3-(BWU)-(LW2));
			g2d.drawImage(rotateImage(WingRight,-25), null, 271-(BWS)-(SW)+(LW), 3-(BWU)-(LW2));
			g2d.drawImage(rotateImage(WingRight,25), null, 256+(BWS)-(SW)+(LW1), 110-(BWU/2)-(SWUU)+(LW3));

			break;
		}
		case(4):{
			g2d.drawImage(rotateImage(WingLeft,25), null, -31+(BWS)+(SW)-(LW), 3-(BWU)-(LW2));
			g2d.drawImage(rotateImage(WingLeft,-25), null, -16-(BWS)+(SW)-(LW1), 110-(BWU/2)-(SWUU)+(LW3));



			g2d.drawImage(rotateImage(WingRight,-25), null, 271-(BWS)-(SW)+(LW), 3-(BWU)-(LW2));
			g2d.drawImage(rotateImage(WingRight,25), null, 256+(BWS)-(SW)+(LW1), 110-(BWU/2)-(SWUU)+(LW3));


			break;
		}
		case(5):{
			g2d.drawImage(rotateImage(WingLeft,25), null, -31+(BWS)+(SW)-(LW), 3-(BWU)-(LW2));
			g2d.drawImage(WingLeft, null, -33-(BWS)+(SW)-(LW), 60-(BWU)-(SWU));	
			g2d.drawImage(rotateImage(WingLeft,-25), null, -16-(BWS)+(SW)-(LW1), 110-(BWU/2)-(SWUU)+(LW3));

			g2d.drawImage(rotateImage(WingRight,-25), null, 271-(BWS)-(SW)+(LW), 3-(BWU)-(LW2));
			g2d.drawImage(rotateImage(WingRight,25), null, 256+(BWS)-(SW)+(LW1), 110-(BWU/2)-(SWUU)+(LW3));

			break;
		}
		case(6):{
			RemoveBackGround(WingLeft);
			RemoveBackGround(WingRight);
			g2d.drawImage(rotateImage(WingLeft,25), null, -31+(BWS)+(SW)-(LW), 3-(BWU)-(LW2));
			g2d.drawImage(WingLeft, null, -33-(BWS)+(SW)-(LW), 60-(BWU)-(SWU));	
			g2d.drawImage(rotateImage(WingLeft,-25), null, -16-(BWS)+(SW)-(LW1), 110-(BWU/2)-(SWUU)+(LW3));

			g2d.drawImage(rotateImage(WingRight,-25), null, 271-(BWS)-(SW)+(LW), 3-(BWU)-(LW2));
			g2d.drawImage(WingRight, null, 273+(BWS)-(SW)+(LW), 60-(BWU)-(SWU));
			g2d.drawImage(rotateImage(WingRight,25), null, 256+(BWS)-(SW)+(LW1), 110-(BWU/2)-(SWUU)+(LW3));


			break;
		}

		}


		g2d.dispose();

		return new ImageIcon(image);
	}

	public static BufferedImage rotateImage(BufferedImage img, int x ) {

		int w = img.getWidth();

		int h = img.getHeight();

		BufferedImage dimg = new BufferedImage(w, h,
				img.getColorModel().getTransparency());

		Graphics2D g = dimg.createGraphics();

		g.rotate(Math.toRadians(x), w / 2, h / 2); // change angle here

		g.drawImage(img, null, 0, 0);

		g.dispose();

		return dimg;

	}

	public void RemoveBackGround(  BufferedImage x){

		Graphics g =  x.getGraphics();
		BufferedImage dimg = x;
		Color color = Color.white;
		Graphics2D g2 = dimg.createGraphics();
		g2.setComposite(AlphaComposite.Src);
		g2.drawImage( x, null, 0, 0);
		g2.dispose();
		for(int i = 0; i < dimg.getHeight(); i++) {
			for(int j = 0; j < dimg.getWidth(); j++) {
				if(dimg.getRGB(j, i) == color.getRGB()) {

					dimg.setRGB(j, i, 0xff0000);
				}
			}
		}


	}
	public static BufferedImage flipImage(BufferedImage img) {   

		int w = img.getWidth();   
		int h = img.getHeight();   
		BufferedImage fimg = new BufferedImage(w, h, img.getColorModel().getTransparency());   
		Graphics2D g = fimg.createGraphics();   
		g.drawImage(img, 0, 0, w, h, w, 0, 0, h, null); 
		g.dispose(); 

		return fimg;   
	}  
	
	public static BufferedImage zoomOut(BufferedImage g, int scale) {
		int width = g.getWidth() / scale;
		int height = g.getHeight() / scale;

		BufferedImage zOut = new BufferedImage(width, height, java.awt.Transparency.TRANSLUCENT);

		for(int i=0; i<width; i++)
			for(int j=0; j<height; j++)
				zOut.setRGB(i, j, g.getRGB(i*scale, j*scale));

		return zOut;
	}

	public static BufferedImage TranslucentImage(BufferedImage x){
		BufferedImage temp = new BufferedImage(x.getWidth(), x.getHeight(),
				BufferedImage.TYPE_INT_ARGB);
		g = temp.createGraphics();
		g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, 0.6f));
		g.drawImage(x, null, 0, 0);
		g.dispose();

		return temp;

	}

	public static BufferedImage loadImage(String ref) {   
		BufferedImage temp = null;  

		try {  

			URL saveImageURL = PhenotypeImageMaker.class.getResource(ref);
			temp = ImageIO.read(saveImageURL);
		} catch (Exception e) {   
			e.printStackTrace();   
		}   
		return temp;   
	}  


	private String FindLast(String x){ // this is to get the last part of the string p to get all the info out
		x.replace(" ",""); // replace all blanks
		int L = x.length();
		String temp = "";
		String Rev = "";
		for ( int i = L-1 ; i > 0 ; i--){ // get the last part 
			if( x.charAt(i) == ':'){break;}
			else {
				temp = temp+x.charAt(i);// save the last part 
			}}
		for(int i = temp.length() -1 ; i >= 0 ; i--) // flip back to org
		{
			Rev = Rev + temp.charAt(i);
		} 
		return Rev;}// retrun the last part

	//see if the organism has a specified phenotype
	// for this body part and type & return it
	// if not, return null
	private Phenotype findPhenotypeMatching(String bodyPart, String type) {
		Phenotype result = null;
		for (int i = 0; i < phenotypes.size(); i++) {
			Phenotype p = phenotypes.get(i);
			Trait t = p.getTrait();
			if (t.getBodyPart().equals(bodyPart) && t.getType().equals(type)) {
				result = p;
				break;
			}
		}
		return result;
	}


	private int getIntFromString(String s) {
		if (s.equals("No")) return 0;
		if (s.equals("One")) return 1;
		if (s.equals("Two")) return 2;
		if (s.equals("Three")) return 3;
		if (s.equals("Four")) return 4;
		if (s.equals("Five")) return 5;
		if (s.equals("Six")) return 6;
		else return 0;
	}
}
