import java.io.IOException;
import java.util.*;
import java.io.*;


public class AdvancedTTT {
  public static int depthLimit = 3;

  public static int[] getInput(State_Advanced s){
    int [] inputIndex = {0, 0};
    Scanner scanner = new Scanner(System.in);
    Action_Advanced input = new Action_Advanced(inputIndex);
    ArrayList<Action_Advanced> legalMoves = s.getActions();
    do{
      System.err.print("Your board selection: ");
      do{
        try{
          inputIndex[0] = scanner.nextInt() - 1;
        }catch (Exception e){
          System.err.println("Error reading from user");
          scanner.nextLine();
        }
      }while(inputIndex[0] < 0 || inputIndex[0] > 8);
      System.err.print("Your position selection: ");
      do{
        try{
          inputIndex[1] = scanner.nextInt() - 1;
        }catch (Exception e){
          System.err.println("Error reading from user");
          scanner.nextLine();
        }
      }while(inputIndex[1] < 0 || inputIndex[1] > 8);
      input = new Action_Advanced(inputIndex);
      if(!legalMoves.contains(input)){
        System.err.println("Your move is not legal, please try again!");
      }
    }while(!legalMoves.contains(input));
    return input.getNum();
  }

  public static void play(int choice){
    State_Advanced s = new State_Advanced();
    if(choice == 'x'){ // player first
      s = s.mark(getInput(s));
    }
    do{
      s.setLevel(0);
      Action_Advanced move_result = getMove(s);
      s = s.mark(move_result.getNum());
      System.err.println(s);
      if(s.isTerminal() != -2)
        break;
      s = s.mark(getInput(s));
      System.err.println(s);
    }while(s.isTerminal() == -2);
    System.err.println("Game Over!");
    System.err.println(s);
    System.err.println("The result is "+s.isTerminal());
  }

  public static Action_Advanced getMove(State_Advanced ss){
    State_Advanced s = (State_Advanced)deepClone(ss);
    int value;
    int [] tempAction = {0, 0};
    Action_Advanced move = new Action_Advanced(tempAction);
    if(s.getPlayer()){ // cross playing
      value = -2000;
      for(Action_Advanced a: s.getActions()){
        int temp = H_miniMax(s.mark(a.getNum()));
        if(temp>value){
          value = temp;
          move = a;
        }
      }
    }else{ // nought playing
      value = 2000;
      for(Action_Advanced a: s.getActions()){
        int temp = H_miniMax(s.mark(a.getNum()));
        if(temp<value){
          value = temp;
          move = a;
        }
      }
    }
    System.err.print("Computer move: ");
    System.out.print(move.num[0]+1);
    System.err.print(" ");
    System.out.print(move.num[1]+1);
    System.err.println();
    return move;
  }

  private static int H_miniMax(State_Advanced ss){
    int alpha = -5000; // highest utility
    int beta = 5000; // lowest utility
    State_Advanced s = (State_Advanced)deepClone(ss);
    int value = s.isTerminal();
    if(value != -2){ // if s is terminal state
       // make sure terminal state will always dominate the result
       // also make the algorithm prefer more steps to get to the same result
       // Where human player have higher chance of making error, while computer won't
      return value*(1000-s.getLevel()*10);
    }
    if (s.level <= depthLimit){
      if(s.getPlayer()){ // if cross is playing, max
        value = -2000;
        for(Action_Advanced a: s.getActions()){
          int temp = H_miniMax(s.mark(a.getNum()));
          if(temp > value){ // store the largest value
            value = temp;
          }
        }
      }else{ // if nought is playing, min
        value = 2000;
        for(Action_Advanced a: s.getActions()){
          int temp = H_miniMax(s.mark(a.getNum()));
          if(temp < value){ // store the smallest value
            value = temp;
          }
        }
      }
    return value;
    } else {
      return s.getScore();
    }
  }

  public static void main(String[] args) {
    depthLimit = Integer.parseInt(args[0]);
    System.err.print("Do you want to play x(cross) or o(nought):");
    int choice = 'x';
    try{
      choice = System.in.read();
    }catch (IOException e){
      System.err.println("Error reading from user");
    }
    play(choice);
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
