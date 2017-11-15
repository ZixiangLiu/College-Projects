Course: CSC254
Assignment: No.1
Name: Yukun Chen, Zixiang Liu

Compile and testing method:
	$$ make parser
	$$ ./parser filename


Brief Introduction of each part:
	Translate the code we provide into C++:

	Extend the language with if and do/check statements:
		First, we need to extend the scanner to enable scanning of additional symbols.
		Next, we need to extend the parser to enhance the grammer. At first, we found that it's really hard to consider all the situations at once and pass all the test case. Then we figured out a FIRST, FOLLOW table which is really helpful to include all the situations. 

	Implement exception-based syntax error recovery:
		First we need to store the line information of the code file, so that we can keep trace on the line and column for each token. Then we need to add a pointer to figure out which part of the code we have already gone through. When the parser encounter an error, it will call the error function which can print out the location of the syntax error.
		Also, for error recovery, we built a FIRST, FOLLOW table as instructed. When the scanner scans that the next token doesn't belong to either of the table, it will delete the token it scans until reaching one of the token in either table. If it's in the FIRST table, it will continue parsing as usual. If it's in the FOLLOW table, it will create error code indicating that it is skipping the current state and jump to the next one.

	Output a syntax tree with the structure suggested:
