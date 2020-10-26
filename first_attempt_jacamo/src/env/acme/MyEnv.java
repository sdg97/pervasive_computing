package acme;

import cartago.*;

public class MyEnv extends Artifact{
	public void init(int startValue) {
		this.defineObsProperty("count", startValue);
	}
	
	@OPERATION 
	public void inc() {
		ObsProperty p = getObsProperty("count");
		p.updateValue(p.intValue() + 1);
		
	}
	
	@OPERATION 
	public void dec() {
		ObsProperty p = getObsProperty("count");
		p.updateValue(p.intValue() + 1);
		
	}
}
