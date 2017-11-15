// CSC254
// A2
// Zixiang Liu
// partner: Yukun Chen

#include <iostream> // to use std::cout
#include <string>   // to use string related functions
#include "scan.h"

char token_image[100];
int pointer = 0;
int error_pointer = 0;
bool error_flag = false;

bool myisalpha(int c){
    return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
}

bool myisdigit(int c){
    return c >= '0' && c <= '9';
}

token scan() {
    int c = ' ';
    int i = 0;

    while (c == ' '){
        c = std::cin.get();
        pointer++;
    }
    if (myisalpha(c)){
        do {
            token_image[i++] = c;
            c = std::cin.get();
            pointer++;
        }while(myisdigit(c) || myisalpha(c) || c == '_');
        token_image[i] = '\0';
        if (std::string(token_image) == std::string("read")) return t_read;
        else if (std::string(token_image) == std::string("write")) return t_write;
        else if (std::string(token_image) == std::string("if")) return t_if;
        else if (std::string(token_image) == std::string("fi")) return t_fi;
        else if (std::string(token_image) == std::string("do")) return t_do;
        else if (std::string(token_image) == std::string("od")) return t_od;
        else if (std::string(token_image) == std::string("check")) return t_check;
        else return t_id;
    }else if(myisdigit(c)){
    	do {
    		token_image[i++] = c;
    		c = std::cin.get();
            pointer++;
    	} while(myisdigit(c));
    	token_image[i] = '\0';
    	return t_literal;
    }else switch(c){
        case ':':
        	c = std::cin.get();
            pointer++;
        	if (c == '='){
        		c = std::cin.get();
                pointer++;
        		return t_gets;
        	}else{
        		std::cout << "Error: \"=\" expected" << std::endl;
            	exit(EXIT_FAILURE);
        	}
        case '=': 
            c = std::cin.get();
            pointer++;
            if (c == '='){
                c = std::cin.get();
                pointer++;
                return t_eq;
            }else{
                std::cout << "error: \"=\" expected" << std::endl;
                exit(EXIT_FAILURE);
            }
        case '<':
            c = std::cin.get();
            pointer++;
            if (c == '>'){
                c = std::cin.get();
                pointer++;
                return t_uneq;
            }else if(c == '='){
                c = std::cin.get();
                pointer++;
                return t_smeq;
            }else{
                return t_sm;
            }
        case '>':
            c = std::cin.get();
            pointer++;
            if(c == '='){
                c = std::cin.get();
                pointer++;
                return t_lareq;
            }else{
                return t_lar;
            }
        case '$':
            c = std::cin.get();
            pointer++;
            if (c == '$'){
                c = std::cin.get();
                pointer++;
                return t_eof;
            }else{
                std::cout << "Error: \"$\" expected" << std::endl;
                exit(EXIT_FAILURE);
            }
        case '+': c = std::cin.get(); pointer++; return t_add;
        case '-': c = std::cin.get(); pointer++; return t_sub;
        case '*': c = std::cin.get(); pointer++; return t_mul;
        case '/': c = std::cin.get(); pointer++; return t_div;
        case '(': c = std::cin.get(); pointer++; return t_lparen;
        case ')': c = std::cin.get(); pointer++; return t_rparen;
        case '\0': c = std::cin.get(); pointer++; return t_eof;
        default:
            std::cout << "Error: unexpected input \"" << c << "\"" << std::endl;
            exit(EXIT_FAILURE);
    }
}
