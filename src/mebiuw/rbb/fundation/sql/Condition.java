package mebiuw.rbb.fundation.sql;

/*
 * 用于描述条件比较大小的
 */
public class Condition {
	public ConditionItem[] conditions;
	
	public Condition(ConditionItem[] conditions) {
		
		this.conditions = conditions;
	}

	public ConditionItem[] getConditions() {
		return conditions;
	}
	
	public String toString(){
		StringBuilder sb=new StringBuilder();
		sb.append(this.conditions[0].toString());
		for(int i=1;i<this.conditions.length;i++){
			sb.append(","+this.conditions[i].toString());
		}
		return sb.toString();
		
	}
	
	public String toMessageEntry(){
		StringBuilder sb=new StringBuilder(this.conditions[0].toMessageString());
		for(int i=1;i<this.conditions.length;i++){
			sb.append("*"+this.conditions[i].toMessageString());
		}
		return sb.toString();
	}

	
}
