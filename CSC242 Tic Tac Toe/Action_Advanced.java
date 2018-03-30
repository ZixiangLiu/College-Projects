public class Action_Advanced {
	public int[] num = new int [2];

	/**
	* Default empty Action constructor
	*/
	public Action_Advanced() {
		super();
		int [] temp = {0, 0};
		this.num = temp;
	}

	/**
	* Default Action constructor
	*/
	public Action_Advanced(int[] num) {
		super();
		this.num = num;
	}

	/**
	* Create string representation of Action for printing
	* @return
	*/
	@Override
	public String toString() {
		return String.valueOf(num[0]) + " " + String.valueOf(num[1]);
	}

	/**
	* Returns value of num
	* @return
	*/
	public int[] getNum() {
		return num;
	}

	/**
	* Sets new value of num
	* @param
	*/
	public void setNum(int[] num) {
		this.num = num;
	}

	/**
	* Override equals method for evaluation
	*/
	@Override
	public boolean equals(Object obj){
		if (obj == null) {
	        return false;
	    }
	    if (!Action_Advanced.class.isAssignableFrom(obj.getClass())) {
	        return false;
	    }
	    final Action_Advanced other = (Action_Advanced) obj;
		if (this.num[0] == other.num[0] && this.num[1] == other.num[1])
			return true;
		return false;
	}
}
