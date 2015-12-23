package antsand;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Sim {

	private final int width;
	private final int height;
	
	private Map<Integer, Map<Integer, SiteType>> map = new HashMap<>();
	
	private Logger log = LoggerFactory.getLogger(getClass());
	
	public Sim(int width, int height) {
		super();
		this.width = width;
		this.height = height;
		

        for (int x = 0; x < getWidth(); x++) {
        	for (int y = (3*getHeight())/4; y < getHeight(); y++) {
        		if (!map.containsKey(x))
        			map.put(x, new HashMap<>());
        		map.get(x).put(y, SiteType.SAND);
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
		if (map.containsKey(x)) {
			if (map.get(x).containsKey(y)) {
				return map.get(x).get(y);
			}
		}
		
		return SiteType.AIR;		 
	}
	
	public void setSite(int x, int y, SiteType siteType) {
		synchronized(map) {
			if (!map.containsKey(x)) {
				map.put(x, new HashMap<>());
			}
			map.get(x).put(y, siteType);
		}
	}
	
	public synchronized void update() {
		Map<Integer, Map<Integer, SiteType>> nextMap = new HashMap<>();

		//log.info("Update running");
		
		synchronized(map) {
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
						//but will settle on bottom of sim
						if (y > 0 && getSite(x,y-1) == SiteType.AIR) {
							newSiteType = SiteType.AIR;	
							//log.info("sand fallen");					
						}
					}
					
					if (newSiteType != SiteType.AIR) {
		        		if (!nextMap.containsKey(x))
		        			nextMap.put(x, new HashMap<>());
		        		nextMap.get(x).put(y, newSiteType);
					}
				}
			}
			
			map = nextMap;
		}
	}

}
