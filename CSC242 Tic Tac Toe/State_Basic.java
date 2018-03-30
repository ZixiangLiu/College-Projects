/* CSC242
 * Project 1
 * 02.10.2018
 * Yukun Chen, Zixiang Liu, Yifei Yang
 * Action_Advanced.java

 * The state class
 * a state contains a board configuration and a player status
 * board:
     int array of length 9, -2 is no mark, 1 is a cross, -1 is a nought
 * player:
     true is crosses, who also plays first in the beginning of the game
     false is noughts
 *
 */
import java.util.*;

public class State_Basic {
  private int[] board;
  private boolean player;

	/**
	* Default empty State_Basic constructor
	*/
	public State_Basic() {
		super();
    this.board = new int[9]; // initialize to all -2's
    for(int i = 0; i < 9; i++){
      this.board[i] = -2;
    }
    this.player = true; // cross plays first
	}

  /**
  * function to mark the current step of state and return the next state
  */
  public State_Basic mark(int move){
    int[] temp = this.board.clone();
    if(this.player){
      temp[move] = 1;
    }else{
      temp[move] = -1;
    }
    return new State_Basic(temp, !this.player);
  }

	/**
	* Default State_Basic constructor
	*/
	public State_Basic(int[] board, boolean player) {
		super();
		this.board = board;
		this.player = player;
	}

  /**
	* Generate possible actions
	* return the resulting states in an arraylist
	*/
  public ArrayList<Action_Basic> getActions(){
    ArrayList<Action_Basic> actions = new ArrayList<Action_Basic>();
    for(int i = 0; i < 9; i++){
      if(this.board[i] == -2){
        actions.add(new Action_Basic(i));
      }
    }
    return actions;
  }

  public ArrayList<Integer> getActionsIndex(){
    ArrayList<Integer> actions = new ArrayList<Integer>();
    for(int i = 0; i < 9; i++){
      if(this.board[i] == -2){
        actions.add(i);
      }
    }
    return actions;
  }

  /**
	* Determine if the state is a terminal state
	* -2 for non terminal
	* 1 for cross win
	* 0 for draw
	* -1 for nought win
	*/
  public int isTerminal(){
    // vertical and horizontals
    for(int i = 0; i< 3; i++){
      if((this.board[i*3] == this.board[i*3+1]) && (this.board[i*3+1] == this.board[i*3+2])){
        if(this.board[i*3] != -2){// return whoever wins
          return this.board[i*3];
        }
      }
      if((this.board[i] == this.board[i+3]) && (this.board[i+3] == this.board[i+6])){
        if(this.board[i] != -2){// return whoever wins
          return this.board[i];
        }
      }
    }

    // diagonals
    if((this.board[0] == this.board[4]) && (this.board[4] == this.board[8])){
      if(this.board[0] != -2){// return whoever wins
        return this.board[0];
      }
    }

    if((this.board[2] == this.board[4]) && (this.board[4] == this.board[6])){
      if(this.board[2] != -2){// return whoever wins
        return this.board[2];
      }
    }

    // is the board filled
    boolean filled = true;
    for(int i = 0; i< 9; i++){
      if(this.board[i] == -2){
        filled = false;
        break;
      }
    }
    if(filled){ // nobody wins and the game ends, draw
      return 0;
    }else{ // nobody wins and the game has not end, nonterminal state
      return -2;
    }
  }

  /**
  * used to print board
  * Return X for 1, O for -1, E for -2
  * @return
  */
  private String translate(int a){
    if(a == 1){
      return "X ";
    }else if(a == -1){
      return "O ";
    }else{
      return "E ";
    }
  }

	/**
	* Create string representation of State_Basic for printing
	* @return
	*/
	@Override
	public String toString() {
    StringBuilder stringBuilder = new StringBuilder();

    stringBuilder.append("The current player is the game is ");
    if(this.player){
      stringBuilder.append("Crosses\n");
    }else{
      stringBuilder.append("Noughts\n");
    }

    for(int i = 0; i < 3; i++){
      for(int j = 0; j < 3; j++){
        stringBuilder.append(translate(this.board[i*3+j]));
      }
      stringBuilder.append("\n");
    }
		return stringBuilder.toString();
	}

	/**
	* Returns value of t
	* @return
	*/
	public int[] getBoard() {
		return this.board;
	}

	/**
	* Sets new value of t
	* @param
	*/
	public void setBoard(int[] board) {
		this.board = board;
	}

	/**
	* Returns value of player
	* @return
	*/
	public Boolean getPlayer() {
		return player;
	}

	/**
	* Sets new value of player
	* @param
	*/
	public void setPlayer(boolean player) {
		this.player = player;
	}
}
