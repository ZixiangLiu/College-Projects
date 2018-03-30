Project 1
CSC242
Collaborator: Yukun Chen, Zixiang Liu & Yifei Yang

Compile and testing method:
    Basic TTT:
        $$ javac BasicTTT.java
        $$ java BasicTTT
    Advanced TTT:
        $$ javac AdvancedTTT.java
        $$ java AdvancedTTT DepthLimit
            DepthLimit is an integer, which set the depth of the state-based tree
            Setting the value up to 7 will give resonable response time, number beyond 7 will significantly slow down the process
            The code can also run without inputing DepthLimit as follow, the system will set the value as 4 in default
                $$ java AdvancedTTT

Demo for Basic TTT (Output of one round play):
    Do you want to play x(cross) or o(nought):x
    Your move: 5
    Computer move: 1
    The current player is the game is Crosses
    O E E 
    E X E 
    E E E 

    Your move: 3
    The current player is the game is Noughts
    O E X 
    E X E 
    E E E 

    Computer move: 7
    The current player is the game is Crosses
    O E X 
    E X E 
    O E E 

    Your move: 4
    The current player is the game is Noughts
    O E X 
    X X E 
    O E E 

    Computer move: 6
    The current player is the game is Crosses
    O E X 
    X X O 
    O E E 

    Your move: 8
    The current player is the game is Noughts
    O E X 
    X X O 
    O X E 

    Computer move: 2
    The current player is the game is Crosses
    O O X 
    X X O 
    O X E 

    Your move: 9
    The current player is the game is Noughts
    O O X 
    X X O 
    O X X 

    Game Over!
    The current player is the game is Noughts
    O O X 
    X X O 
    O X X 

    Game Draw.



Demo for Advanced TTT (Output of one round play):
    Do you want to play x(cross) or o(nought):x
    Your board selection: 5
    Your position selection: 5
    Computer move: 5 1
    The current player is the game is Crosses
          |      |      |
          |      |      |
          |      |      |
    ------+------+------
          |O     |      |
          |  X   |      |
          |      |      |
    ------+------+------
          |      |      |
          |      |      |
          |      |      |
    ------+------+------
    Legal board is 1 

    Your board selection: 1
    Your position selection: 5
    The current player is the game is Noughts
          |      |      |
      X   |      |      |
          |      |      |
    ------+------+------
          |O     |      |
          |  X   |      |
          |      |      |
    ------+------+------
          |      |      |
          |      |      |
          |      |      |
    ------+------+------
    Legal board is 5 

    Computer move: 5 3
    The current player is the game is Crosses
          |      |      |
      X   |      |      |
          |      |      |
    ------+------+------
          |O   O |      |
          |  X   |      |
          |      |      |
    ------+------+------
          |      |      |
          |      |      |
          |      |      |
    ------+------+------
    Legal board is 3 

    Your board selection: 3
    Your position selection: 7
    The current player is the game is Noughts
          |      |      |
      X   |      |      |
          |      |X     |
    ------+------+------
          |O   O |      |
          |  X   |      |
          |      |      |
    ------+------+------
          |      |      |
          |      |      |
          |      |      |
    ------+------+------
    Legal board is 7 

    Computer move: 7 1
    The current player is the game is Crosses
          |      |      |
      X   |      |      |
          |      |X     |
    ------+------+------
          |O   O |      |
          |  X   |      |
          |      |      |
    ------+------+------
    O     |      |      |
          |      |      |
          |      |      |
    ------+------+------
    Legal board is 1 

    Your board selection: 1
    Your position selection: 3
    The current player is the game is Noughts
        X |      |      |
      X   |      |      |
          |      |X     |
    ------+------+------
          |O   O |      |
          |  X   |      |
          |      |      |
    ------+------+------
    O     |      |      |
          |      |      |
          |      |      |
    ------+------+------
    Legal board is 3 

    Computer move: 3 9
    The current player is the game is Crosses
        X |      |      |
      X   |      |      |
          |      |X   O |
    ------+------+------
          |O   O |      |
          |  X   |      |
          |      |      |
    ------+------+------
    O     |      |      |
          |      |      |
          |      |      |
    ------+------+------
    Legal board is 9 

    Your board selection: 9
    Your position selection: 1
    The current player is the game is Noughts
        X |      |      |
      X   |      |      |
          |      |X   O |
    ------+------+------
          |O   O |      |
          |  X   |      |
          |      |      |
    ------+------+------
    O     |      |X     |
          |      |      |
          |      |      |
    ------+------+------
    Legal board is 1 

    Computer move: 1 7
    The current player is the game is Crosses
        X |      |      |
      X   |      |      |
    O     |      |X   O |
    ------+------+------
          |O   O |      |
          |  X   |      |
          |      |      |
    ------+------+------
    O     |      |X     |
          |      |      |
          |      |      |
    ------+------+------
    Legal board is 7 

    Your board selection: 7
    Your position selection: 7
    The current player is the game is Noughts
        X |      |      |
      X   |      |      |
    O     |      |X   O |
    ------+------+------
          |O   O |      |
          |  X   |      |
          |      |      |
    ------+------+------
    O     |      |X     |
          |      |      |
    X     |      |      |
    ------+------+------
    Legal board is 7 

    Computer move: 7 3
    The current player is the game is Crosses
        X |      |      |
      X   |      |      |
    O     |      |X   O |
    ------+------+------
          |O   O |      |
          |  X   |      |
          |      |      |
    ------+------+------
    O   O |      |X     |
          |      |      |
    X     |      |      |
    ------+------+------
    Legal board is 3 

    Your board selection: 3
    Your position selection: 3
    The current player is the game is Noughts
        X |      |    X |
      X   |      |      |
    O     |      |X   O |
    ------+------+------
          |O   O |      |
          |  X   |      |
          |      |      |
    ------+------+------
    O   O |      |X     |
          |      |      |
    X     |      |      |
    ------+------+------
    Legal board is 3 

    Computer move: 3 1
    The current player is the game is Crosses
        X |      |O   X |
      X   |      |      |
    O     |      |X   O |
    ------+------+------
          |O   O |      |
          |  X   |      |
          |      |      |
    ------+------+------
    O   O |      |X     |
          |      |      |
    X     |      |      |
    ------+------+------
    Legal board is 1 

    Your board selection: 1
    Your position selection: 9
    The current player is the game is Noughts
        X |      |O   X |
      X   |      |      |
    O   X |      |X   O |
    ------+------+------
          |O   O |      |
          |  X   |      |
          |      |      |
    ------+------+------
    O   O |      |X     |
          |      |      |
    X     |      |      |
    ------+------+------
    Legal board is 9 

    Computer move: 9 7
    The current player is the game is Crosses
        X |      |O   X |
      X   |      |      |
    O   X |      |X   O |
    ------+------+------
          |O   O |      |
          |  X   |      |
          |      |      |
    ------+------+------
    O   O |      |X     |
          |      |      |
    X     |      |O     |
    ------+------+------
    Legal board is 7 

    Your board selection: 7
    Your position selection: 2
    The current player is the game is Noughts
        X |      |O   X |
      X   |      |      |
    O   X |      |X   O |
    ------+------+------
          |O   O |      |
          |  X   |      |
          |      |      |
    ------+------+------
    O X O |      |X     |
          |      |      |
    X     |      |O     |
    ------+------+------
    Legal board is 2 

    Computer move: 2 7
    The current player is the game is Crosses
        X |      |O   X |
      X   |      |      |
    O   X |O     |X   O |
    ------+------+------
          |O   O |      |
          |  X   |      |
          |      |      |
    ------+------+------
    O X O |      |X     |
          |      |      |
    X     |      |O     |
    ------+------+------
    Legal board is 7 

    Your board selection: 7
    Your position selection: 8
    The current player is the game is Noughts
        X |      |O   X |
      X   |      |      |
    O   X |O     |X   O |
    ------+------+------
          |O   O |      |
          |  X   |      |
          |      |      |
    ------+------+------
    O X O |      |X     |
          |      |      |
    X X   |      |O     |
    ------+------+------
    Legal board is 8 

    Computer move: 8 2
    The current player is the game is Crosses
        X |      |O   X |
      X   |      |      |
    O   X |O     |X   O |
    ------+------+------
          |O   O |      |
          |  X   |      |
          |      |      |
    ------+------+------
    O X O |  O   |X     |
          |      |      |
    X X   |      |O     |
    ------+------+------
    Legal board is 2 

    Your board selection: 2
    Your position selection: 4
    The current player is the game is Noughts
        X |      |O   X |
      X   |X     |      |
    O   X |O     |X   O |
    ------+------+------
          |O   O |      |
          |  X   |      |
          |      |      |
    ------+------+------
    O X O |  O   |X     |
          |      |      |
    X X   |      |O     |
    ------+------+------
    Legal board is 4 


    Computer move: 4 9
    The current player is the game is Crosses
        X |      |O   X |
      X   |X     |      |
    O   X |O     |X   O |
    ------+------+------
          |O   O |      |
          |  X   |      |
        O |      |      |
    ------+------+------
    O X O |  O   |X     |
          |      |      |
    X X   |      |O     |
    ------+------+------
    Legal board is 9 

    Your board selection: 9
    Your position selection: 9
    The current player is the game is Noughts
        X |      |O   X |
      X   |X     |      |
    O   X |O     |X   O |
    ------+------+------
          |O   O |      |
          |  X   |      |
        O |      |      |
    ------+------+------
    O X O |  O   |X     |
          |      |      |
    X X   |      |O   X |
    ------+------+------
    Legal board is 9 

    Computer move: 9 5
    The current player is the game is Crosses
        X |      |O   X |
      X   |X     |      |
    O   X |O     |X   O |
    ------+------+------
          |O   O |      |
          |  X   |      |
        O |      |      |
    ------+------+------
    O X O |  O   |X     |
          |      |  O   |
    X X   |      |O   X |
    ------+------+------
    Legal board is 5 

    Your board selection: 5
    Your position selection: 2
    The current player is the game is Noughts
        X |      |O   X |
      X   |X     |      |
    O   X |O     |X   O |
    ------+------+------
          |O X O |      |
          |  X   |      |
        O |      |      |
    ------+------+------
    O X O |  O   |X     |
          |      |  O   |
    X X   |      |O   X |
    ------+------+------
    Legal board is 2 

    Computer move: 2 2
    The current player is the game is Crosses
        X |  O   |O   X |
      X   |X     |      |
    O   X |O     |X   O |
    ------+------+------
          |O X O |      |
          |  X   |      |
        O |      |      |
    ------+------+------
    O X O |  O   |X     |
          |      |  O   |
    X X   |      |O   X |
    ------+------+------
    Legal board is 2 

    Your board selection: 2
    Your position selection: 6
    The current player is the game is Noughts
        X |  O   |O   X |
      X   |X   X |      |
    O   X |O     |X   O |
    ------+------+------
          |O X O |      |
          |  X   |      |
        O |      |      |
    ------+------+------
    O X O |  O   |X     |
          |      |  O   |
    X X   |      |O   X |
    ------+------+------
    Legal board is 6 

    Computer move: 6 4
    The current player is the game is Crosses
        X |  O   |O   X |
      X   |X   X |      |
    O   X |O     |X   O |
    ------+------+------
          |O X O |      |
          |  X   |O     |
        O |      |      |
    ------+------+------
    O X O |  O   |X     |
          |      |  O   |
    X X   |      |O   X |
    ------+------+------
    Legal board is 4 

    Your board selection: 4
    Your position selection: 1
    The current player is the game is Noughts
        X |  O   |O   X |
      X   |X   X |      |
    O   X |O     |X   O |
    ------+------+------
    X     |O X O |      |
          |  X   |O     |
        O |      |      |
    ------+------+------
    O X O |  O   |X     |
          |      |  O   |
    X X   |      |O   X |
    ------+------+------
    Legal board is 1 

    Computer move: 1 4
    The current player is the game is Crosses
        X |  O   |O   X |
    O X   |X   X |      |
    O   X |O     |X   O |
    ------+------+------
    X     |O X O |      |
          |  X   |O     |
        O |      |      |
    ------+------+------
    O X O |  O   |X     |
          |      |  O   |
    X X   |      |O   X |
    ------+------+------
    Legal board is 4 

    Your board selection: 4
    Your position selection: 7
    The current player is the game is Noughts
        X |  O   |O   X |
    O X   |X   X |      |
    O   X |O     |X   O |
    ------+------+------
    X     |O X O |      |
          |  X   |O     |
    X   O |      |      |
    ------+------+------
    O X O |  O   |X     |
          |      |  O   |
    X X   |      |O   X |
    ------+------+------
    Legal board is 7 

    Computer move: 7 9
    The current player is the game is Crosses
        X |  O   |O   X |
    O X   |X   X |      |
    O   X |O     |X   O |
    ------+------+------
    X     |O X O |      |
          |  X   |O     |
    X   O |      |      |
    ------+------+------
    O X O |  O   |X     |
          |      |  O   |
    X X O |      |O   X |
    ------+------+------
    Legal board is 9 

    Your board selection: 9
    Your position selection: 2
    The current player is the game is Noughts
        X |  O   |O   X |
    O X   |X   X |      |
    O   X |O     |X   O |
    ------+------+------
    X     |O X O |      |
          |  X   |O     |
    X   O |      |      |
    ------+------+------
    O X O |  O   |X X   |
          |      |  O   |
    X X O |      |O   X |
    ------+------+------
    Legal board is 2 

    Computer move: 2 8
    The current player is the game is Crosses
        X |  O   |O   X |
    O X   |X   X |      |
    O   X |O O   |X   O |
    ------+------+------
    X     |O X O |      |
          |  X   |O     |
    X   O |      |      |
    ------+------+------
    O X O |  O   |X X   |
          |      |  O   |
    X X O |      |O   X |
    ------+------+------
    Legal board is 8 

    Your board selection: 8
    Your position selection: 8
    The current player is the game is Noughts
        X |  O   |O   X |
    O X   |X   X |      |
    O   X |O O   |X   O |
    ------+------+------
    X     |O X O |      |
          |  X   |O     |
    X   O |      |      |
    ------+------+------
    O X O |  O   |X X   |
          |      |  O   |
    X X O |  X   |O   X |
    ------+------+------
    Legal board is 8 

    Computer move: 8 6
    The current player is the game is Crosses
        X |  O   |O   X |
    O X   |X   X |      |
    O   X |O O   |X   O |
    ------+------+------
    X     |O X O |      |
          |  X   |O     |
    X   O |      |      |
    ------+------+------
    O X O |  O   |X X   |
          |    O |  O   |
    X X O |  X   |O   X |
    ------+------+------
    Legal board is 6 

    Your board selection: 6
    Your position selection: 8
    The current player is the game is Noughts
        X |  O   |O   X |
    O X   |X   X |      |
    O   X |O O   |X   O |
    ------+------+------
    X     |O X O |      |
          |  X   |O     |
    X   O |      |  X   |
    ------+------+------
    O X O |  O   |X X   |
          |    O |  O   |
    X X O |  X   |O   X |
    ------+------+------
    Legal board is 8 

    Computer move: 8 1
    The current player is the game is Crosses
        X |  O   |O   X |
    O X   |X   X |      |
    O   X |O O   |X   O |
    ------+------+------
    X     |O X O |      |
          |  X   |O     |
    X   O |      |  X   |
    ------+------+------
    O X O |O O   |X X   |
          |    O |  O   |
    X X O |  X   |O   X |
    ------+------+------
    Legal board is 1 

    Your board selection: 1
    Your position selection: 1
    The current player is the game is Noughts
    X   X |  O   |O   X |
    O X   |X   X |      |
    O   X |O O   |X   O |
    ------+------+------
    X     |O X O |      |
          |  X   |O     |
    X   O |      |  X   |
    ------+------+------
    O X O |O O   |X X   |
          |    O |  O   |
    X X O |  X   |O   X |
    ------+------+------
    Legal board is 1 

    Game Over!
    The current player is the game is Noughts
    X   X |  O   |O   X |
    O X   |X   X |      |
    O   X |O O   |X   O |
    ------+------+------
    X     |O X O |      |
          |  X   |O     |
    X   O |      |  X   |
    ------+------+------
    O X O |O O   |X X   |
          |    O |  O   |
    X X O |  X   |O   X |
    ------+------+------
    Legal board is 1 

    Cross Win!

