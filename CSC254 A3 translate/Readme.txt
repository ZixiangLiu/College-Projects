Course: CSC254
Assignment: No.3
Name: Yukun Chen, Zixiang Liu

Compile and testing method:
	$$ ocaml translator.ml
	--copy the output after warning part, and paste it in file named translated.c
	$$ gcc translated.c -o test
	$$ ./test

Brief Introduction of each part:
	Translate the parse tree into a syntax tree: 
		The parse tree to AST part are written following professor provided skeleton. Because AST is essentially a list of ast_s, the program is appending each statement generated in ast_ize_S into the list in ast_ize_SL method. Other methods are creating ast_e that fills ast_s. 
		Match statement is a every useful and powerful feature. It avoids extansive if and switch statement if this is written in imperative language. 
		Additional error message is generated at ast_ize_P and ast_ize_SL just to make sure the debugging are more efficient. 
        an Additional method ast_ize_bool is used because both "for" and "check" requires the statement to return bool(we call it a "judgement" for any statement that should return bool). By calling this method, the error of non-judgement written in code can be detected before go into a c compiler. 

	Translate the AST into an equivalent program in C: 
		Basically, this part is to turn each element in AST into equivalent C grammar. 
		Since we are going to record the variables assigned but unused, we have to first initialize a special list named var_used to store this information. During translating, since there is not any global variable in Ocaml syntax, we are going to pass this list to each element during the translating process, and meanwhile update it. When we first read or assign a variable, we are going to add it to the list but mark it unused. Once this variable is used in expression, we are going to change its label into used. Until we finish the translating process, we are going to print the warning include all the variables in the list marked as unused.
		For translation, we are going to follow the AST syntax, and write a group of recursive function to translate them from top to bottom. We need to translate each part of grammar into its sub components, and add proper connection in C syntax.
		Also, before the translation, we are going to add the header file and three special help functions: getint() to prompt user enter an integer, putint() to print an integer in one line, and devide() to handle the case when denonminator equals to zero. After this helper functions, we are going to add the transfered C code.
		At last, we are going to print the warning, which will show all the variables assigned but unused, and the actual C code.

Bonus:
	added for loop and reminder operation. similar to divide, reminder has a method to handle issue if the second number is zero.

Check statement is not a judgement:
    Zixiangs-MacBook-Pro:Desktop zixiangliu$ ocaml bonus.ml 
    Exception: Failure "This is not a judgement. Error in check process. ".

For divide by zero:
C file:
    #include "stdio.h"
    #include "stdlib.h"

    int divide(int n1, int n2){
        if(n2 != 0){
            return (int) (n1/n2);
        }else{
            printf("Divded by 0 at %d/%d, the result is set to 1.\n", n1, n2);
            return 1;
        }
    }

    int getint(){
    	int oneint;
    	int result = scanf("%d", &oneint);
    	if (result == EOF){
        	printf("Unexpected ending in read process.\n");
    	}else if (result == 0){
        	printf("Input type is not integer.\n");
    	}
    	return oneint;
    }

    int reminder(int n1, int n2){
        if(n2 != 0){
            return (int) (n1%n2);
        }else{
            printf("Reminder by 0 at %d%%%d, the result is set to 1.\n", n1, n2);
            return 1;
        }
    }

    void putint(int n){
        printf("%d\n", n);
    }

    int main () {
    int a=1;
    int b=0;
    putint(divide(a, b));
    return 0;
    }
    Zixiangs-MacBook-Pro:Desktop zixiangliu$ gcc test.c
    Zixiangs-MacBook-Pro:Desktop zixiangliu$ ./a.out
    Divded by 0 at 1/0, the result is set to 1.
    1

For reminder by zero:
C file:
    #include "stdio.h"
    #include "stdlib.h"

    int divide(int n1, int n2){
        if(n2 != 0){
            return (int) (n1/n2);
        }else{
            printf("Divded by 0 at %d/%d, the result is set to 1.\n", n1, n2);
            return 1;
        }
    }

    int getint(){
    	int oneint;
    	int result = scanf("%d", &oneint);
    	if (result == EOF){
        	printf("Unexpected ending in read process.\n");
    	}else if (result == 0){
        	printf("Input type is not integer.\n");
    	}
    	return oneint;
    }

    int reminder(int n1, int n2){
        if(n2 != 0){
            return (int) (n1%n2);
        }else{
            printf("Reminder by 0 at %d%%%d, the result is set to 1.\n", n1, n2);
            return 1;
        }
    }

    void putint(int n){
        printf("%d\n", n);
    }

    int main () {
    int a=1;
    int b=0;
    putint(reminder(a, b));
    return 0;
    }
    Zixiangs-MacBook-Pro:Desktop zixiangliu$ gcc test.c
    Zixiangs-MacBook-Pro:Desktop zixiangliu$ ./a.out
    Reminder by 0 at 1%0, the result is set to 1.
    1