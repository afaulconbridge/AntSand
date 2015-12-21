package antsand;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Sim {

	private final int width;
	private final int height;
	
	private Map<XY<Integer>, SiteType> map = new HashMap<>();
	
	private Logger log = LoggerFactory.getLogger(getClass());
	
	public Sim(int width, int height) {
		super();
		this.width = width;
		this.height = height;
		

        for (int x = 0; x < getWidth(); x++) {
        	for (int y = (3*getHeight())/4; y < getHeight(); y++) {
        		XY<Integer> xy = new XY<>(x,y);
        		map.put(xy, SiteType.SAND);
        	}
        }
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public SiteType getSite(int x, int y) {
		XY<Integer> xy = new XY<>(x,y);
		if (map.containsKey(xy)) {
			return map.get(xy);
		} else {
			return SiteType.AIR;
		} 
	}
	
	public void update() {
		Map<XY<Integer>, SiteType> nextMap = new HashMap<>();

		//log.info("Update running");
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				SiteType siteType = getSite(x,y);
				SiteType newSiteType = siteType;
				
				if (siteType == SiteType.AIR) {
					//air goes to sand if sand falls into it
					if (getSite(x,y+1) == SiteType.SAND) {
						newSiteType = SiteType.SAND;
						//log.info("sand falling");
					}
				} else if (siteType == SiteType.SAND) {
					//sand will fall into air
					if (getSite(x,y-1) == SiteType.AIR) {
						newSiteType = SiteType.AIR;	
						//log.info("sand fallen");					
					}
				}
				
				if (newSiteType != SiteType.AIR) {
	        		XY<Integer> xy = new XY<>(x,y);
					nextMap.put(xy, newSiteType);
				}
			}
		}
		
		map = nextMap;
	}

}
