package FuzzyCompetence;

public class Interval {
    private  final double min;
    private  final double max;
  
    

	private boolean isInterval;
    
    
    
    public boolean isInterval() {
		return isInterval;
	}




	public void setInterval(boolean isInterval) {
		this.isInterval = isInterval;
	}




	public Interval(double min, double max, boolean isInterval) {
        if (Double.isInfinite(min) || Double.isInfinite(max))
            throw new IllegalArgumentException("Endpoints must be finite");
        if (Double.isNaN(min) || Double.isNaN(max))
            throw new IllegalArgumentException("Endpoints cannot be NaN");
        	
        // convert -0.0 to +0.0
        if (min == 0.0) min = 0.0;
        if (max == 0.0) max = 0.0;

        if (min <= max) {
            this.min = min;
            this.max = max;
            this.isInterval=isInterval;
        }
        else throw new IllegalArgumentException("Illegal interval");
        
       
    }

    
 

	public double getMin() {
		return min;
	}

	public double getMax() {
		return max;
	}

	public double min() { 
        return min;
    }

    
    public double max() { 
        return max;
    }

    
    public boolean intersects(Interval that) {
        if (this.max < that.min) return false;
        if (that.max < this.min) return false;
        return true;
    }
    
    public Interval getIntersection(Interval that) {
		   
    	if(this.max<that.min) return new Interval(0,0,true);
    	if(this.max>that.min)  return new Interval(that.min,this.max,true);
		
    	return new Interval(0,0,true);
    	
    	
    	
    }
    
    public Interval getUnion(Interval that) {
    	
    	
    	
		return new Interval(this.min, that.max,true);
    	
    	
    }
    public boolean contains(double x) {
        return (min <= x) && (x <= max);
    }

    
    public double length() {
        return max - min;
    }

   
    public String toString() {
        return "[" + min + ", " + max + "]";
    }

  
    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null) return false;
        if (other.getClass() != this.getClass()) return false;
        Interval that = (Interval) other;
        return this.min == that.min && this.max == that.max;
    }

   
   
}
