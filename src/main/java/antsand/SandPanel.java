package antsand;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;

public class SandPanel extends JPanel {

	private static final long serialVersionUID = -6835039484170067535L;
	
	protected final Sim sim;
	
	protected final Map<SiteType, Integer> siteColours = new HashMap<>();
		
	public SandPanel(Sim sim) {
		super(true);
		this.sim = sim;

		//mark this as non-opaque
        this.setOpaque(false);
        
        //pre-create colours
        siteColours.put(SiteType.AIR, getRGB(0,0,0,0));
        siteColours.put(SiteType.SAND, getRGB(237,201,175,0));
        //siteColours.put(SiteType.WATER, getRGB(64,164,223,0));
	}
	
    @Override
    protected void paintComponent(Graphics g) {
    	super.paintComponent(g);
    	Graphics2D gg = (Graphics2D) g;
    	
    	//black background
        //gg.setColor(Color.white);
        //gg.fillRect(0, 0, getWidth(), getHeight());
        
        BufferedImage img = gg.getDeviceConfiguration().createCompatibleImage(sim.getWidth(), sim.getHeight(), Transparency.OPAQUE);
        
        for (int x = 0; x < sim.getWidth(); x++) {
        	for (int y = 0; y < sim.getHeight(); y++) {
        		//flip the y axis between sim and screen
        		SiteType siteType = sim.getSite(x,sim.getHeight()-y);
        		int rgb = siteColours.get(siteType);
        		img.setRGB(x,y, rgb);
        	}
        }
        
        //work out what resolution to scale to
        double frameRatio = ((double)getWidth())/((double)getHeight());
        double simRatio = ((double)sim.getWidth())/((double)sim.getHeight());
        if (frameRatio > simRatio) {
        	//use height
        	double scaleRatio = ((double) getHeight() / (double) sim.getHeight());
        	int imgWidth = (int) (sim.getWidth() * scaleRatio);
        	int imgHeight = getHeight();
            gg.drawImage(img, 0,0, imgWidth,imgHeight, null);
        } else {
        	//use width
        	double scaleRatio = ((double) getWidth() / (double) sim.getWidth());
        	int imgWidth = getWidth();
        	int imgHeight = (int) (sim.getHeight() * scaleRatio);
            gg.drawImage(img, 0,0, imgWidth,imgHeight, null);
        }
    }
    
    private int getRGB(int r, int g, int b, int a) {
		//int r = // red component 0...255
		//int g = // green component 0...255
		//int b = // blue component 0...255
		//int a = // alpha (transparency) component 0...255
		int col = (a << 24) | (r << 16) | (g << 8) | b;
    	return col;
    }

}
