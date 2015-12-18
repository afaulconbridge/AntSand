package antsand;

import java.util.HashMap;
import java.util.Map;

public class Sim {

	private final int width;
	private final int height;
	
	private Map<XY<Integer>, SiteType> map = new HashMap<>();
	
	public Sim(int width, int height) {
		super();
		this.width = width;
		this.height = height;
		

        for (int x = 0; x < getWidth(); x++) {
        	for (int y = 0; y < getHeight()/4; y++) {
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

}
