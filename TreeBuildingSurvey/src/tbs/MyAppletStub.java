package tbs;

import java.applet.AppletContext;
import java.applet.AppletStub;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Hashtable;

import javax.swing.JApplet;

public class MyAppletStub implements AppletStub {
	private Hashtable<String, String> _properties;
	private JApplet _applet;

	/**
	 * Creates a new MyAppletStub instance and initializes 
	 *  parameters.
	 * modified from 
	 * http://java.sun.com/developer/technicalArticles/Programming/TurningAnApplet/
	 * 
	 * @param argv[] Command line arguments passed to Main
	 * @param an Applet instance.
	 * 
	 * admin is boolean - true = admin mode (many trees for viewing); false = student mode
	 * scoreMany is boolean - student mode that just makes a file of scored trees for grading
	 */
	public MyAppletStub (JApplet a, String admin, String scoreMany, String studentDataString) {
		_applet = a;
		_properties = new Hashtable<String, String>();
		_properties.put("Browser", "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.0; SLCC1; .NET CLR 2.0.50727; .NET CLR 3.5.21022; .NET CLR 3.5.30729; MDDC; InfoPath.2; .NET CLR 3.0.30729)");
		_properties.put("Admin", admin);
		_properties.put("ScoreMany", scoreMany);
//		_properties.put("student", "GUEST+=2010-01-27 19:24:52+=O:0:Bat:0:34:false:():():#O:1:Beetle:0:60:false:():():#O:2:Bird:0:86:false:():():#O:3:Butterfly:323:187:true:():():#O:4:Crocodile:360:160:true:():():#O:5:Fish:638:266:true:():():#O:6:Horseshoe Crab:535:212:true:():():#O:7:Human:0:216:false:():():#O:8:Jellyfish:0:242:false:():():#O:9:Leech:349:238:true:():():#O:10:Lizard:0:294:false:():():#O:11:Millipede:0:320:false:():():#O:12:Rat:0:346:false:():():#O:13:Shark:0:372:false:():():#O:14:Snail:0:398:false:():():#O:15:Spider:0:424:false:():():#O:16:Squirrel:0:450:false:():():#O:17:Starfish:0:476:false:():():#O:18:Turtle:0:502:false:():():#O:19:Whale:0:528:false:():():#E:20::40:556:false:():():#E:20::40:556:false:():():#+=I put the organisms in alphabetical order, because I'm a librarian.+=The ones that looked most like the other ones were related to the 'ones' they  looked most like, by the principle of+=3,3,3,3,3,3,3,3,3,3,3,3,3,+=section 02+=");
		if (!studentDataString.equals("")) _properties.put("student", studentDataString);
	}

	/**
	 * Calls the applet's resize
	 * @param width
	 * @param height
	 * @return void
	 */
	public void appletResize(int width, int height) {
		_applet.resize (width, height);
	}

	/**
	 * Returns the applet's context, which is 
	 * null in this case. This is an area where more
	 * creative programming
	 * work can be done to try and provide a context
	 * @return AppletContext Always null
	 */ 
	public AppletContext getAppletContext () {
		return null;
	}

	/**
	 * Returns the CodeBase. If a host parameter
	 * isn't provided
	 * in the command line arguments, the URL is based
	 * on InetAddress.getLocalHost(). 
	 * The protocol is "file:"
	 * @return URL
	 */
	public java.net.URL getCodeBase() {
		String host;
		if ( (host=getParameter("host")) == null ) {
			try {
				host = InetAddress.getLocalHost().getHostName();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}

		java.net.URL u  = null;
		try {
			u = new java.net.URL ("file://"+host);
		} catch (Exception e) { }
		return u;
	}

	/**
	 * Returns getCodeBase
	 * @return URL
	 */
	public java.net.URL getDocumentBase() {
		return getCodeBase();
	}

	/**
	 * Returns the corresponding command line value
	 * @return String
	 */
	public String getParameter (String p) {
		return (String)_properties.get (p);
	}

	/**
	 * Applet is always true
	 * @return boolean True
	 */
	public boolean isActive () {
		return true;
	}
}