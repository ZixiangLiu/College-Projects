/*  CSC242
    Project 1
    02.10.2018
    Yukun Chen, Zixiang Liu
    AdvancedTTT.java
*/
    
import java.io.IOException;
import java.util.*;
import java.io.*;


public class AdvancedTTT {
  public static int depthLimit = 4;   // default depth limit = 4

  // function to get input and return as int array
  public static int[] getInput(State_Advanced s){
    int [] inputIndex = {0, 0};
    Scanner scanner = new Scanner(System.in);
    Action_Advanced input = new Action_Advanced(inputIndex);  // transfer input into action
    ArrayList<Action_Advanced> legalMoves = s.getActions();   // get legal move of current state
    do{
      System.err.print("Your board selection: ");
      do{
        try{
          inputIndex[0] = scanner.nextInt() - 1;
        }catch (Exception e){
          System.err.println("Error reading from user");  // throw error exeption and prompt user enter again
          scanner.nextLine();
        }
      }while(inputIndex[0] < 0 || inputIndex[0] > 8);
      System.err.print("Your position selection: ");
      do{
        try{
          inputIndex[1] = scanner.nextInt() - 1;
        }catch (Exception e){
          System.err.println("Error reading from user");  // throw error exeption and prompt user enter again
          scanner.nextLine();
        }
      }while(inputIndex[1] < 0 || inputIndex[1] > 8); 
      input = new Action_Advanced(inputIndex);
      if(!legalMoves.contains(input)){        // if legal move doesn't contain input, prompt user to try again
        System.err.println("Your move is not legal, please try again!");
      }
    }while(!legalMoves.contains(input));
    return input.getNum();    // return input as int array [board, position]
  }

  // function to play one round game
  public static void play(int choice){
    State_Advanced s = new State_Advanced();
    if(choice == 'x'){        // if player choose x, player first
      s = s.mark(getInput(s));    // get input from user and mark it as next move
    }
    do{                   // loop for a round: computer first move, and then user move
      s.setLevel(0);      // set current node as root
      Action_Advanced move_result = getMove(s);   // call getMove method to get best move result
      s = s.mark(move_result.getNum());   // mark the best move, set it as new board 
      System.err.println(s);
      if(s.isTerminal() != -2)  // if new board is terminal state, break it 
        break;
      s = s.mark(getInput(s));  // else, get input from user and mark it as next move
      System.err.println(s);
    }while(s.isTerminal() == -2);   // if not reach terminal state, start next round
    System.err.println("Game Over!");
    System.err.println(s);
    if (s.isTerminal() == 1)      // print the final result
      System.err.println("Cross Win!");
    else if (s.isTerminal() == -1)
      System.err.println("Nought Win!");
    else
      System.err.println("Game Draw.");
  }

  // function to get next move from computer
  public static Action_Advanced getMove(State_Advanced ss){
    State_Advanced s = (State_Advanced)deepClone(ss); // get deep clone of current state, to avoid 
    int value;
    int [] tempAction = {0, 0};
    Action_Advanced move = new Action_Advanced(tempAction);
    if(s.getPlayer()){  // cross playing
      value = -2000;    // set the local maximunm of children to -infinity
      for(Action_Advanced a: s.getActions()){     // loop for all children
        int temp = H_miniMax(s.mark(a.getNum()));
        if(temp > value){       // if child's value > local max, update local max and record move
          value = temp;
          move = a;
        }
        s.alpha = s.alpha > value? s.alpha: value;    // check if local max > alpha and update alpha
        if (s.beta <= s.alpha)
          break;
      }
    }else{            // nought playing
      value = 2000;   // set the local minimum of children to infinity
      for(Action_Advanced a: s.getActions()){     // loop for all children
        int temp = H_miniMax(s.mark(a.getNum()));
        if(temp < value){         // if child;s value < local min, update local min and record move
          value = temp;
          move = a;
        }
        s.beta = s.beta < value? s.beta: value;   // check if local min < beta and update beta
        if (s.beta <= s.alpha)
          break;
      }
    }
    System.err.print("Computer move: ");      // print computer move
    System.out.print(move.num[0]+1);
    System.err.print(" ");
    System.out.print(move.num[1]+1);
    System.err.println();
    return move;
  }

  // Heuristic minimax function
  private static int H_miniMax(State_Advanced ss){
    State_Advanced s = (State_Advanced)deepClone(ss);
    int value = s.isTerminal();
    if(value != -2){ // if s is terminal state
      return value * (1000-s.level*10);   // make sure terminal state will always dominate the result
                                          // also give some consideration to level since user may not always make the best move
    }
    if (s.level <= depthLimit){       // if not reach depth limit, return recursive result
      if(s.getPlayer()){ // if cross is playing, max
        value = -2000;
        for(Action_Advanced a: s.getActions()){
          int temp = H_miniMax(s.mark(a.getNum()));
          if(temp > value){ // store the largest value
            value = temp;
          }
          s.alpha = s.alpha > value? s.alpha: value;
          if (s.beta <= s.alpha)
            break;
        }
      }else{ // if nought is playing, min
        value = 2000;
        for(Action_Advanced a: s.getActions()){
          int temp = H_miniMax(s.mark(a.getNum()));
          if(temp < value){ // store the smallest value
            value = temp;
          }
          s.beta = s.beta < value? s.beta: value;
          if (s.beta <= s.alpha)
            break;
        }
      }
    return value;
    } else {
      return s.getScore();    // if reach depth limit, return heuristic score calculated by current state
    }
  }

  public static void main(String[] args) {
    try{
      depthLimit = Integer.parseInt(args[0]);
    }catch(ArrayIndexOutOfBoundsException e){
      System.err.println("Difficulty level not selected, default level: 4");
    }
    System.err.print("Do you want to play x(cross) or o(nought):");
    int choice = 'x';
    try{
      choice = System.in.read();
    }catch (IOException e){
      System.err.println("Error reading from user");
    }
    play(choice);
  }


  // function to deep clone an object 
  public static Object deepClone(Object object) {
    try {
     ByteArrayOutputStream baos = new ByteArrayOutputStream();
     ObjectOutputStream oos = new ObjectOutputStream(baos);     // translate object to byte stream
     oos.writeObject(object);                                   // copy byte stream
     ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
     ObjectInputStream ois = new ObjectInputStream(bais);     // write byte stream into new object
     return ois.readObject();
    }
    catch (Exception e) {
     e.printStackTrace();
     return null;
    }
  }
}
