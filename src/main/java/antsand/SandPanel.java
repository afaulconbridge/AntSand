package antsand;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.LayoutManager;
import java.awt.Transparency;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JPanel;
import javax.swing.event.MouseInputListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SandPanel extends JPanel implements MouseInputListener {

	private static final long serialVersionUID = -6835039484170067535L;
	
	protected final Sim sim;
	
	protected final Map<SiteType, Integer> siteColours = new HashMap<>();
	
	protected int mouseX = -1;
	protected int mouseY = -1;
	protected boolean mouseDown = false;
	
	protected int penSize = 1;
	protected SiteType penMaterial = SiteType.SAND;
	
	protected double scaleRatio = 1.0;
	
	private Logger log = LoggerFactory.getLogger(getClass());
		
	public SandPanel(Sim sim) {
		super(true);
		this.sim = sim;

		//mark this as opaque
        this.setOpaque(true);
        
        //enable listening
        this.addMouseMotionListener(this);
        
        //pre-create colours
        siteColours.put(SiteType.AIR, getRGB(0,0,0,0));
        siteColours.put(SiteType.SAND, getRGB(237,201,175,0));
        //siteColours.put(SiteType.WATER, getRGB(64,164,223,0));
	}
	
    public int getPenSize() {
		return penSize;
	}

	public void setPenSize(int penSize) {
		this.penSize = penSize;
	}

	public SiteType getPenMaterial() {
		return penMaterial;
	}

	public void setPenMaterial(SiteType penMaterial) {
		this.penMaterial = penMaterial;
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
        		if (siteType != null) {
	        		int rgb = siteColours.get(siteType);
	        		img.setRGB(x,y, rgb);
        		}
        	}
        }
        
        //work out what resolution to scale to
        double frameRatio = ((double)getWidth())/((double)getHeight());
        double simRatio = ((double)sim.getWidth())/((double)sim.getHeight());
        if (frameRatio > simRatio) {
        	//use height
        	scaleRatio = ((double) getHeight() / (double) sim.getHeight());
        	int imgWidth = (int) (sim.getWidth() * scaleRatio);
        	int imgHeight = getHeight();
            gg.drawImage(img, 0,0, imgWidth,imgHeight, null);
        } else {
        	//use width
        	scaleRatio = ((double) getWidth() / (double) sim.getWidth());
        	int imgWidth = getWidth();
        	int imgHeight = (int) (sim.getHeight() * scaleRatio);
            gg.drawImage(img, 0,0, imgWidth,imgHeight, null);
        }
        
        //draw the pen if present
    }
    
    private int getRGB(int r, int g, int b, int a) {
		//int r = // red component 0...255
		//int g = // green component 0...255
		//int b = // blue component 0...255
		//int a = // alpha (transparency) component 0...255
		int col = (a << 24) | (r << 16) | (g << 8) | b;
    	return col;
    }

	@Override
	public void mouseClicked(MouseEvent e) {
		//not triggered
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		//not triggered
	}

	@Override
	public void mouseExited(MouseEvent e) {
		//not triggered
	}

	@Override
	public void mousePressed(MouseEvent e) {
		//not triggered
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		//not triggered
	}

	@Override
	public void mouseDragged(MouseEvent e) {
		log.info("mouse Dragged");
		mouseX = e.getX();
		mouseY = e.getY();
		applyMouseClicked();
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		log.info("mouse Moved");
		mouseX = e.getX();
		mouseY = e.getY();
	}
	
	protected void applyMouseClicked() {
		//convert panel coords to sim coords
		int simX = (int) (mouseX / scaleRatio);
		int simY = sim.getHeight()- ((int) (mouseY / scaleRatio));
		for (int x = simX-penSize/2; x <= simX+penSize/2; x++) {
			for (int y = simY-penSize/2; y <= simY+penSize/2; y++) {
				//check for radial distance too to make it circular
				int dx = x-simX;
				int dy = y-simY;
				if (Math.sqrt((dx*dx)+(dy*dy)) <= penSize/2) {
					sim.setSite(x,y, getPenMaterial());
				}
			}
		}
		this.repaint();
	}

}
