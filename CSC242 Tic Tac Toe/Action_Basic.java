/* CSC242
 * Project 1
 * 02.10.2018
 * Yukun Chen, Zixiang Liu, Yifei Yang
 * Action_Basic.java

 * The Action_Advanced class
 * an action contains a number
 * it indicates the move
 * Use a separate class so that it is easier to do advanced
 */
public class Action_Basic {
  private int num;

	/**
	* Default empty Action constructor
	*/
	public Action_Basic() {
		super();
    this.num = 0;
	}

	/**
	* Default Action constructor
	*/
	public Action_Basic(int num) {
		super();
		this.num = num;
	}

	/**
	* Create string representation of Action for printing
	* @return
	*/
	@Override
	public String toString() {
		return String.valueOf(num+1);
	}

	/**
	* Returns value of num
	* @return
	*/
	public int getNum() {
		return num;
	}

	/**
	* Sets new value of num
	* @param
	*/
	public void setNum(int num) {
		this.num = num;
	}
}
