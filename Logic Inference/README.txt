Project 2
CSC242
Collaborator: Yukun Chen, Zixiang Liu & Yifei Yang

Compile and testing method:
    Modus Ponens Example:
        $$ javac ModusPonensKB.java
        $$ java ModusPonensKB
    Wumpus World Example:
        $$ javac WumpusWorldKB.java
        $$ java WumpusWorldKB
    Horn Clauses Example:
        $$ javac HornClausesKB.java
        $$ java HornClausesKB
    Liar and Truth-tellers (a):
        $$ javac LiarsKB_a.java
        $$ java LiarsKB_a
    Liar and Truth-tellers (b):
        $$ javac LiarsKB_b.java
        $$ java LiarsKB_b

Sample Output:
    Modus Ponens Example:
        Given: 
        P
        (IMPLIES P Q)

        Using TT-entails.
        We have: 
        P true
        (NOT P) false
        Q true
        (NOT Q) false
        Runtime of TT-entails: 49.34ms

        Using Resolution.
        We have: 
        Found 3 Clauses.
        P true
        Found 3 Clauses.
        (NOT P) false
        Found 5 Clauses.
        Q true
        Found 3 Clauses.
        (NOT Q) false
        Runtime of Resolution: 36.11ms
    
  Wumpus World Example:
        Given: 
        (NOT P1,1)
        (IFF B1,1 (OR P1,2 P2,1))
        (IFF B2,1 (OR P1,2 (OR P2,2 P3,1)))
        (NOT B1,1)
        B2,1

        Using TT-entails.
        We have: 
        P1,2 false
        (NOT P1,2) true
        P2,1 false
        (NOT P2,1) true
        P1,1 false
        (NOT P1,1) true
        B1,1 false
        (NOT B1,1) true
        B2,1 true
        (NOT B2,1) false
        P2,2 false
        (NOT P2,2) false
        P3,1 false
        (NOT P3,1) false
        Runtime of TT-entails: 54.55ms

        Using Resolution.
        We have: 
        Found 305 Clauses.
        P1,2 false
        Found 27 Clauses.
        (NOT P1,2) true
        Found 305 Clauses.
        P2,1 false
        Found 27 Clauses.
        (NOT P2,1) true
        Found 305 Clauses.
        P1,1 false
        Found 11 Clauses.
        (NOT P1,1) true
        Found 305 Clauses.
        B1,1 false
        Found 11 Clauses.
        (NOT B1,1) true
        Found 11 Clauses.
        B2,1 true
        Found 305 Clauses.
        (NOT B2,1) false
        Found 362 Clauses.
        P2,2 false
        Found 306 Clauses.
        (NOT P2,2) false
        Found 362 Clauses.
        P3,1 false
        Found 306 Clauses.
        (NOT P3,1) false
        Runtime of Resolution: 6484.30ms

    Horn Clauses Example:
        Given: 
        (IMPLIES mythical (NOT mortal))
        (IMPLIES (NOT mythical) (AND mortal mammal))
        (IMPLIES (OR (NOT mortal) mammal) horned)
        (IMPLIES horned magical)

        Using TT-entails.
        We have: 
        magical true
        (NOT magical) false
        mammal false
        (NOT mammal) false
        mythical false
        (NOT mythical) false
        horned true
        (NOT horned) false
        mortal false
        (NOT mortal) false
        Runtime of TT-entails: 49.44ms

        Using Resolution.
        We have: 
        Found 25 Clauses.
        magical true
        Found 21 Clauses.
        (NOT magical) false
        Found 24 Clauses.
        mammal false
        Found 22 Clauses.
        (NOT mammal) false
        Found 24 Clauses.
        mythical false
        Found 23 Clauses.
        (NOT mythical) false
        Found 26 Clauses.
        horned true
        Found 21 Clauses.
        (NOT horned) false
        Found 23 Clauses.
        mortal false
        Found 24 Clauses.
        (NOT mortal) false
        Runtime of Resolution: 78.38ms

    Extra Credit:
    Liar and Truth-tellers (a):
    	Given: 
        (IMPLIES Amy is truth-teller (AND Cal is truth-teller Amy is truth-teller))
        (IMPLIES (NOT Amy is truth-teller) (NOT (AND Cal is truth-teller Amy is truth-teller)))
        (IMPLIES Bob is truth-teller (NOT Cal is truth-teller))
        (IMPLIES (NOT Bob is truth-teller) Cal is truth-teller)
        (IMPLIES Cal is truth-teller (OR Bob is truth-teller (NOT Amy is truth-teller)))
        (IMPLIES (NOT Cal is truth-teller) (NOT (OR Bob is truth-teller (NOT Amy is truth-teller))))

        Using TT-entails.
        We have: 
        Cal is truth-teller true
        (NOT Cal is truth-teller) false
        Amy is truth-teller false
        (NOT Amy is truth-teller) true
        Bob is truth-teller false
        (NOT Bob is truth-teller) true
        Runtime of TT-entails: 4.17ms

        Using Resolution.
        We have: 
        Cal is truth-teller true
        (NOT Cal is truth-teller) false
        Amy is truth-teller false
        (NOT Amy is truth-teller) true
        Bob is truth-teller false
        (NOT Bob is truth-teller) true
        Runtime of Resolution: 144.72ms

    Liar and Truth-tellers (b):
        Given: 
        (IMPLIES Amy is truth-teller (NOT Cal is truth-teller))
        (IMPLIES (NOT Amy is truth-teller) Cal is truth-teller)
        (IMPLIES Bob is truth-teller (AND Amy is truth-teller Cal is truth-teller))
        (IMPLIES (NOT Bob is truth-teller) (NOT (AND Amy is truth-teller Cal is truth-teller)))
        (IMPLIES Cal is truth-teller (AND Amy is truth-teller Cal is truth-teller))
        (IMPLIES (NOT Cal is truth-teller) (NOT (AND Amy is truth-teller Cal is truth-teller)))

        Using TT-entails.
        We have: 
        Cal is truth-teller false
        (NOT Cal is truth-teller) true
        Amy is truth-teller true
        (NOT Amy is truth-teller) false
        Bob is truth-teller false
        (NOT Bob is truth-teller) true
        Runtime of TT-entails: 3.46ms

        Using Resolution.
        We have: 
        Cal is truth-teller false
        (NOT Cal is truth-teller) true
        Amy is truth-teller true
        (NOT Amy is truth-teller) false
        Bob is truth-teller false
        (NOT Bob is truth-teller) true
        Runtime of Resolution: 98.90ms

