package acme;
import cartago.*;

public class Counter extends Artifact{
	public void init(int startValue) {
		this.defineObsProperty("value", startValue);
	}
	
	@OPERATION 
	public void inc() {
		ObsProperty p = getObsProperty("value");
		p.updateValue(p.intValue() + 1);
		signal("update", "Hey I update the artifact");
	}
}
