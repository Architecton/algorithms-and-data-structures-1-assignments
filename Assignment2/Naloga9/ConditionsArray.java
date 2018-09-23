import java.util.ArrayList;

class ConditionsArray {
	private ArrayList<Condition> conditions;
	
	public ConditionsArray() {
		this.conditions = new ArrayList<Condition>();
	}
	
	public void addCondition(Condition condition) {
		this.conditions.add(condition);
	}
	
	public ArrayList<Condition> getConditions(){
		return this.conditions;
	}
}