sCSC254
A1
Zixiang Liu
Partner: Yukun Chen

To run:
Python:
    $$ python python_solution.py k n
Swift
    $$ swift swift_solution.swift k n
Ocaml:
    $$ ocaml ocaml_solution.ml k n
Ada:
    $$ gnatmake ada_solution.adb
    $$ ./ada_solution k n
Prolog:
    Option1: use semicolon to get all results
    $$ swipl
    ?- [prolog_solution].
    ?- combinations(k, n, L).
    Then press semicolon until the end

    Option2: output all result at once
    $$ swipl
    ?- [prolog_solution].
    ?- combinations(k, n, L), writeln(L), fail.
    Then all the results appear at the same time

Comments:
Python:
	very easy in writing. 
    Python is very familiar to me, and python is not strict in syntax, so writing python is easy. For one of the outputs of combinations, the list is always increasing. Thus by using recursion, we fill the first element and pass k-1 to the recursive fuction. The base case will be when k is 1 and we can fill the lists from further end to first element. 

    Advantanges:
        easy to write and read
        easy to use list and easy to iterate through lists
        a pleasrue in coding
    Disadvantages:
        not the fastest programming language

Swift:
	quite easy in writing.
    Swift is very similar to java in some ways. As a derived language from objective-C, swift can be called a cousin of jave, who is very similar to C++. It also has well defined list and standard functision, thus writing is not much different from that of python. However, the strict syntax of swift did cost me many time to figure out. 

    Advantages:
        well defined list just like python
        neat and concise
    Disadvantages:
        very slow in compiling
        syntax is too strict

Prolog: 
	extremely easy and intuitive to write, quite hard to understand the concept.
    To write Prolog, I must think logically. Instead of building a route step by step constuctions to result, logic programming ask me to write what i want first(rules), and then what the result must satisfy(more rules). At last, use facts to let the interpreter find the results. The algorithm is the same: the total length of the list is k initially, and k-1 on each recursion. At the end, the program find results itself.
    
    Advantages:
        Extremely short solution
        very fast
    Disadvantages: 
        takes time to learn the syntax
        quite hard to express equations in math

Ocaml:
	very hard to understand, relatively easy to write
    I had similar experience of writing Racket when writing Ocaml. Instead of parentheses(rackets), Ocaml functions are in blocks. After get used to the way everything is written(function var1 var2, and function const var can be a different function with only one variables), I can implement the same algorithm as before. In practise, List multiplication, which is not typically found in other languages, proved to be a more concise and much faster solution than iterater through and concatenate lists.

    Advantages:
        Super fast
        flexibility in implementing results
    Disadvantages:
        compiler is very strict on syntax
        looks very different from other languages

Ada:
	hard to write, hard to understand, hard to debugg, but easy to make sure the program run smoothly in every condition(or it will bug).
    As an ECE student, I have done VHDL, Verilog and microcontroller languages that has similiar syntax with Ada. Declare variable and write runtime stuff between begin and end. It is accurate, in that you can use natural for 0 and positive integers; or procedure cannot return a value, only function can. You also have to use some variable to catch the return value of a function. It is easy to set limits, so it is equally easy to generate errors. For example, because the default Integer is int32, for n > 12 cases the program actually overflows and quit. But on the other hand, it is very fast for n <= 12 when it runs, and used minimal amount of storage. 

    Advantages:
        Almost as fast as C
        easy to limit values to certain range
    Disadvantages:
        less freedom in writing code
        takes long time to implement code, and need even more time to optimize


Times:
OCaml and Prolog are almost as quick, python and swift are slower, espasially in large n and swift takes a long time to compile. Ada is very fast, but because n is limited to 12 and output is almost instant, it should be around some speed as OCaml and Prolog.

Iterators:
Iterators are extremely important. For whichever language, iterator plus recursion give us enough power to express the output combination in abtrary length. It also enables us to modify individual values within lists one by one. In conclusion, iterators are fundamentals of this project. 
