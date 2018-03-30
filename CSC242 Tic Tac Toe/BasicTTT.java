/*  CSC242
 *  Project 1
    02.10.2018
    Yukun Chen, Zixiang Liu, Yifei Yang
    AdvancedTTT.java
*/
import java.io.IOException;
import java.util.*;

public class BasicTTT {

  /*
   * function used to read move from user
   * used System.err.print to print instructions
   */
  public static int getInput(State_Basic s){
    System.err.print("Your move: ");
    int input = 0;
    ArrayList<Integer> legalMoves = s.getActionsIndex();
    do{
      try{
        input = System.in.read()-'1';
      }catch (IOException e){
        System.err.println("Error reading from user");
      }
    }while(!legalMoves.contains(input));
    return input;
  }

  /*
   * body of TTT game
   * terminate after game over and print game result
   */
  public static void play(int choice){
    State_Basic s = new State_Basic();
    if(choice == 'x' || choice == 'X'){ // player first
      s = s.mark(getInput(s));
    }
    do{
      s = s.mark(getMove(s).getNum());
      System.err.println(s);
      if(s.isTerminal() != -2){
        break;
      }
      s = s.mark(getInput(s));
      System.err.println(s);
    }while(s.isTerminal() == -2);
    System.err.println("Game Over!");
    System.err.println(s);
    if (s.isTerminal() == 1)      // print the final result
      System.err.println("Cross Win!");
    else if (s.isTerminal() == -1)
      System.err.println("Nought Win!");
    else
      System.err.println("Game Draw.");
  }

  /*
   * from all applicable moves of current state
   * return the best option
   */
  public static Action_Basic getMove(State_Basic s){
    int value = 0;
    Action_Basic move = new Action_Basic(0);
    if(s.getPlayer()){ // cross playing
      value = -1;
      for(Action_Basic a: s.getActions()){
        int temp = miniMax(s.mark(a.getNum()));
        if(temp>value){
          value = temp;
          move = a;
        }
      }
    }else{ // nought playing
      value = 1;
      for(Action_Basic a: s.getActions()){
        int temp = miniMax(s.mark(a.getNum()));
        if(temp<value){
          value = temp;
          move = a;
        }
      }
    }
    System.err.print("Computer move: ");
    System.out.print(move);
    System.err.println();
    return move;
  }

  /*
   * body of minimax algorithm
   * if terminal state, return
   * if max player playing, do max and vice versa
   */
  private static int miniMax(State_Basic s){
    int value = s.isTerminal();
    if(value != -2){ // if s is terminal state
      return value;
    }
    if(s.getPlayer()){ // if cross is playing, max
      value = -1;
      for(Action_Basic a: s.getActions()){
        int temp = miniMax(s.mark(a.getNum()));
        if(temp > value){ // store the largest value
          value = temp;
        }
      }
    }else{ // if nought is playing, min
      value = 1;
      for(Action_Basic a: s.getActions()){
        int temp = miniMax(s.mark(a.getNum()));
        if(temp < value){ // store the smallest value
          value = temp;
        }
      }
    }
    return value;
  }

  /*
   * main function, ask user to play x or o
   * and call play function
   */
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    while (true){
      System.err.print("Do you want to play x(cross) or o(nought):");
      int choice = scanner.next().charAt(0);
      play(choice);
      System.err.println("\n\nLet's play a new one!\n");
      }
  }
}
