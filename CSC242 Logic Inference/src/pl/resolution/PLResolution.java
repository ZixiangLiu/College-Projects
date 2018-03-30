/*
 * Part2 of Project 2
 * PL-Resolution
 */
 package pl.resolution;

import pl.core.*;
import pl.cnf.*;
import pl.util.*;

public class PLResolution {
  /*
   * Function developed based on AIMA 7.12
   * Slight variation is made because resolvent can only be one clause,
   * so in this implementation Clause instead of ArraySet<Clause> is used
   * as the type of resolvent
   */
  public boolean resolution(KB kb, Sentence alpha){
    ArraySet<Clause> clauses = new ArraySet<Clause>();
    ArraySet<Clause> newClauses = new ArraySet<Clause>();

    // add all kb clauses
    for (Sentence sentence : kb.sentences()){
      clauses.addAll(CNFConverter.convert(sentence));
    }

    // add negate of goal into clauses
    clauses.addAll(CNFConverter.convert(new Negation(alpha)));

    while(true){
      for (Clause c1 : clauses){
        for (Clause c2 : clauses){
          Clause resolvent = resolve(c1, c2);
          if(resolvent.isEmpty()){
            System.out.println("Found " + clauses.size() + " Clauses.");
            return true;
          }
          newClauses.add(resolvent);
        }
      }
      if (subset(newClauses, clauses)){
        System.out.println("Found " + clauses.size() + " Clauses.");
        return false;
      }
      clauses.addAll(newClauses);
    }
  }

  /*
   * resolve two clause to new ones
   * if c1 = "A or B", c2 = "not A or C", return "B or C"
   * when two clauses cannot resolve a new clause, the function return c1
   * since ArraySet.add always check if the item is already in the set so
   * returning c1 will not affect the correctness of the program
   */
  public Clause resolve(Clause c1, Clause c2){
    ArraySet<Clause> clauses = new ArraySet<Clause>();
    Clause newClause = new Clause();
    boolean resolved = false;

    for (Literal l : c1){
      Literal nl = new Literal(l);
      if (c2.contains(nl)){
        resolved = true;
        for (Literal l1 : c1){
          if (!l1.equals(l)){
            newClause.add(l1);
          }
        }
        for (Literal l2 : c2){
          if (!l2.equals(nl)){
            newClause.add(l2);
          }
        }
        break;
      }
    }

    if (resolved){
      return newClause;
    }
    else{
      return c1;
    }
  }

  /*
   * check if smallSet is a subset of the larger set.
   * for each element in smallSet, check if it is in the largeSet
  */
  public boolean subset(ArraySet<Clause> smallSet, ArraySet<Clause> largeSet){
    for (Clause cl : smallSet){
      if (!largeSet.contains(cl)){
        return false;
      }
    }
    return true;
  }
}
