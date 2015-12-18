package antsand;

import java.util.Objects;

public class XY<T> {

	public final T x;
	public final T y;
	
	public XY(T x, T y) {
		this.x = x;
		this.y = y;
	}
	
	public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        
        XY<?> other = (XY<?>) obj;
        
		if (this.x.equals(other.x) 
				&& this.y.equals(other.y)) {
			return true;
		} else {
			return false;
		}	
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(x, y);
	}
	
	@Override
	public String toString() {
		return "XY("+x+","+y+")";
	}
	
}
