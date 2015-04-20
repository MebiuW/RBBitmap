package mebiuw.rbb.fundation.sql;

/*
 * 用于描述条件比较大小的
 */
public class Condition {
	private ConditionItem[] conditions;
	
	public Condition(ConditionItem[] conditions) {
		
		this.conditions = conditions;
	}

	public ConditionItem[] getConditions() {
		return conditions;
	}

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
	}
}
