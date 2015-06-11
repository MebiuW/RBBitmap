package mebiuw.rbb.fundation.sql;

public class ConditionItem {
	private String type;
	private double valuea, valueb;

	/**
	 * 二元表达式
	 * 
	 * @param type
	 * @param valuea
	 * @param valueb
	 */
	public ConditionItem(String type, double valuea, double valueb) {
		super();
		this.type = type;
		this.valuea = valuea;
		this.valueb = valueb;
	}

	public String getType() {
		return type;
	}

	public double getValuea() {
		return valuea;
	}

	public double getValueb() {
		return valueb;
	}

	/**
	 * 一元表达式
	 * 
	 * @param type
	 * @param valuea
	 */
	public ConditionItem(String type, double valuea) {
		super();
		this.type = type;
		this.valuea = valuea;
	}
	
	public String toString(){
		return this.type+":"+this.valuea;
	}
	public String toMessageString(){
		return this.type+"*"+this.valuea;
	}
}