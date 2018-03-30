/*
 * The state class
 * a state contains a board configuration and a player status
 * board:
     2d int array of length 9*9, -2 is no mark, 1 is a cross, -1 is a nought
 * player:
     true is crosses, who also plays first in the beginning of the game
     false is noughts
 *
 */
import java.util.*;
import java.io.*;

public class State_Advanced implements Serializable{
  public int[][] board;
  public boolean player;
  public int[] legalBoard;
  public int level;

  /**
  * Default empty State_Advanced constructor
  */
  public State_Advanced() {
    super();
    this.board = new int[9][9]; // initialize to all -2's
    for(int i = 0; i < 9; i++){
      for(int j = 0; j < 9; j++){
        this.board[i][j] = -2;
      }
    }
    int [] temp = {0, 1, 2, 3, 4, 5, 6, 7, 8};
    this.legalBoard = temp;
    this.level = 0;
    this.player = true; // cross plays first
  }

  public State_Advanced(State_Advanced another) {
    super();
    this.board = another.getBoard();
    this.player = another.getPlayer();
    this.legalBoard = another.getLegalBoard();
    this.level = another.getLevel();
  }


  /**
  * Default State_Advanced constructor
  */
  public State_Advanced(int[][] board, boolean player, int[] legalBoard, int level) {
    super();
    this.board = board;
    this.player = player;
    this.legalBoard = legalBoard;
    this.level = level;
  }


  public State_Advanced mark(int[] move){
    State_Advanced s = (State_Advanced)deepClone(this);
    int [][] tempBoard = s.board;
    if(s.player){
      tempBoard[move[0]][move[1]] = 1;
    }else{
      tempBoard[move[0]][move[1]] = -1;
    }
    int[] tempLegalBoard;
    boolean haveSpace = false;
    for (int i = 0; i < 9; i++){
      if (tempBoard[move[1]][i] == -2)
        haveSpace = true;
    }
    if (haveSpace){
      int [] temp = {move[1]};
      tempLegalBoard = temp;
    }else{
      int [] temp = {0, 1, 2, 3, 4, 5, 6, 7, 8};
      tempLegalBoard = temp;
    }
    int tempLevel = ++s.level;
    return new State_Advanced(tempBoard, !s.player, tempLegalBoard, tempLevel);
  }

  /**
  * Generate possible
  * return the resulting states in an arraylist
  */
  public ArrayList<Action_Advanced> getActions(){
    ArrayList<Action_Advanced> actions = new ArrayList<Action_Advanced>();
    for (int i : legalBoard){
      for(int j = 0; j < 9; j++){
        if(this.board[i][j] == -2){
          int [] temp = {i, j};
          actions.add(new Action_Advanced(temp));
        }
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
    for(int i = 0; i < 9; i++){
      // vertical and horizontals
      for(int j = 0; j< 3; j++){
        if((this.board[i][j*3] == this.board[i][j*3+1]) && (this.board[i][j*3+1] == this.board[i][j*3+2])){
          if(this.board[i][j*3] != -2){// return whoever wins
            return this.board[i][j*3];
          }
        }
        if((this.board[i][j] == this.board[i][j+3]) && (this.board[i][j+3] == this.board[i][j+6])){
          if(this.board[i][j] != -2){// return whoever wins
            return this.board[i][j];
          }
        }
      }

      // diagonals
      if((this.board[i][0] == this.board[i][4]) && (this.board[i][4] == this.board[i][8])){
        if(this.board[i][0] != -2){// return whoever wins
          return this.board[i][0];
        }
      }

      if((this.board[i][2] == this.board[i][4]) && (this.board[i][4] == this.board[i][6])){
        if(this.board[i][2] != -2){// return whoever wins
          return this.board[i][2];
        }
      }
    }

    // is the board filled
    boolean filled = true;
    outerloop:
    for(int i = 0; i < 9; i++){
      for(int j = 0; j < 9; j++){
        if(this.board[i][j] == -2){
          filled = false;
          break outerloop;
        }
      }
    }
    if(filled){ // nobody wins and the game ends, draw
      return 0;
    }else{ // nobody wins and the game has not end, nonterminal state
      return -2;
    }
  }

  public int getScore() {
    int score = 0;

    // for each board
    // count the value of each one and add up 
    for(int i = 0; i < 9; i++){

      // horizontals
      for(int j = 0; j < 3; j++){
        score += lineHeuristic(this.board[i][j*3], this.board[i][j*3+1], this.board[i][j*3+2]);
      }

      // verticals
      for(int j = 0; j < 3; j++){
        score += lineHeuristic(this.board[i][j], this.board[i][j+3], this.board[i][j+6]);
      }

      // diagonals
      score += lineHeuristic(this.board[i][0], this.board[i][4], this.board[i][8]);
      score += lineHeuristic(this.board[i][2], this.board[i][4], this.board[i][6]);
    }
    return score;
  }

  public int lineHeuristic(int a, int b, int c){
    int xvalue = 0;
    int ovalue = 0;

    // count total number of x and o
    if(a == 1){
      xvalue += 1;
    }else if(a == -1){
      ovalue += 1;
    }

    if(b == 1){
      xvalue += 1;
    }else if(b == -1){
      ovalue += 1;
    }

    if(c == 1){
      xvalue += 1;
    }else if(c == -1){
      ovalue += 1;
    }

    // if the line has no mark or both mark, it is meaningless
    if((ovalue > 0 && xvalue > 0)||(xvalue == 0 && ovalue == 0)){
      return 0;
    }

    // if only have x
    if(xvalue != 0 && ovalue == 0){
      return xvalue;
    }

    // if only have o
    if(ovalue != 0 && xvalue == 0){
      return -ovalue;
    }

    System.err.println("Line Heuristic encounter inexhaustive case with x = "+ xvalue + " and o = " + ovalue);
    return 0;
  }

  /**
  * Return X for 1, O for -1, E for -2
  * @return
  */
  private String translate(int a){
    if(a == 1){
      return "X ";
    }else if(a == -1){
      return "O ";
    }else{
      return "  ";
    }
  }

  /**
  * Create string representation of State_Advanced for printing
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

    int [][] array = new int [9][9];
    for (int i = 0; i < 9; i++){
      for(int j = 0; j < 9; j++){
        array[i][j] = 9*i+j;
      }
    }
    for(int i = 0; i < 9; i++){
      for(int j = 0; j < 3; j++){
        for(int k = 0; k < 3; k++){
          stringBuilder.append(translate(this.board[(int)Math.floor((double)i/3)*3+j][(i%3)*3+k]));
        }
        stringBuilder.append("|");
      }
      if (i%3 == 2)
        stringBuilder.append("\n------+------+------\n");
      else
        stringBuilder.append("\n");
    }
    stringBuilder.append("Legal board is ");
    for(int a: this.legalBoard){
      stringBuilder.append(a+1);
      stringBuilder.append(" ");
    }
    stringBuilder.append("\n");
    return stringBuilder.toString();
  }

  /**
  * Returns value of t
  * @return
  */
  public int[][] getBoard() {
    return board.clone();
  }

  /**
  * Sets new value of t
  * @param
  */
  public void setBoard(int[][] board) {
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
  * Returns baord legal to play
  * @return
  */
  public int[] getLegalBoard() {
    return legalBoard.clone();
  }

  /**
  * Sets new value of player
  * @param
  */
  public void setPlayer(boolean player) {
    this.player = player;
  }

  /**
  * Returns board level
  * @return
  */
  public int getLevel() {
    return level;
  }

  /**
  * Sets board level
  * @param
  */
  public void setLevel(int level) {
    this.level = level;
  }

  public static Object deepClone(Object object) {
    try {
     ByteArrayOutputStream baos = new ByteArrayOutputStream();
     ObjectOutputStream oos = new ObjectOutputStream(baos);
     oos.writeObject(object);
     ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
     ObjectInputStream ois = new ObjectInputStream(bais);
     return ois.readObject();
    }
    catch (Exception e) {
     e.printStackTrace();
     return null;
    }
  }
}
