// CSC254
// A2
// Zixiang Liu
// partner: Yukun Chen
#include <iostream>
#include <fstream>
#include <string>
#include "scan.h"
#include "ast.cpp"

using namespace std;

const char* names[] = {"read", "write", "id", "literal", "gets", "add", "sub", 
						"mul", "div", "lparen", "rparen", "eof", "if", "fi", "do",
                        "od", "check", "eq", "uneq", "sm", "lar", "smeq", "lareq"};

static token input_token;
static bool debugg = true;

void error(){
    error_flag = true;
	std::cout << "Syntax error around column " << error_pointer << "\n";
}

string match(token expected){
    string output = " ";
	if (input_token == expected){
        error_pointer = pointer;
        //std::cout << "matched " << names[input_token];
		if (input_token == t_id || input_token == t_literal)
			output = string(token_image);
		//std::cout << "\n";
		input_token = scan();
	}
	else{
        error_flag = true;
        std::cout << "Match error, expect " << names[expected] << " but see " << names[input_token] << "\n";
        std::cout << "Error fixed by skipping token: " << names[input_token] << "\n";
        error();
    }
    return output;
}

bool contain(token test, token library[], int range){
    for (int i=0; i<range; i++){
        if (test == library[i]){
            return true;
        }
    }
    return false;
}

void pass(token token_discard){
    error();
    std::cout << "Error fixed by delete " << names[input_token];
    error_pointer = pointer;
    if (input_token == t_id || input_token == t_literal)
        std::cout << ": " << token_image;
    std::cout << "\n";
    input_token = scan();
}

node* program ();
node* stmt_list ();
node* stmt ();
node* expr ();
node* expr_tail ();
node* rel ();
node* rel_op ();
node* term_tail ();
node* term ();
node* factor_tail ();
node* factor ();
node* add_op ();
node* mul_op ();


node* program () {
    node *root = new node(pp, "program");

    token FIRST [] = {t_id, t_read, t_write, t_if, t_do, t_check, t_eof};
    while (!contain(input_token, FIRST, 7)){
        pass(input_token);
    }
    switch (input_token) {
        case t_id:
        case t_read:
        case t_write:
        case t_if:
        case t_do:
        case t_check:
        case t_eof:
            //std::cout << "predict program --> stmt_list eof\n";
            root->appChild(stmt_list ());
            if (debugg) std::cout << "end of program\n";
            if (!error_flag){
                root->printTree();
                freeTree(root);
            }
            

            match (t_eof);
            break;
        default: error ();
    }

    return root;
}

node* stmt_list () {
    node *current = new node(sl, "sl");

    token FIRST [] = {t_id, t_read, t_write, t_if, t_do, t_check, t_eof, t_od, t_fi};
    while (!contain(input_token, FIRST, 9)){
        pass(input_token);
    }
    switch (input_token) {
        case t_id:
        case t_read:
        case t_write:
        case t_if:
        case t_do:
        case t_check:
            //std::cout << "predict stmt_list --> stmt stmt_list\n";
            current->appChild(stmt ());
            current->appChild(stmt_list ());
            break;
        case t_fi:
        case t_od:
        case t_eof:
            //std::cout << "predict stmt_list --> epsilon\n";
            break;          /*  epsilon production */
        default: error ();
    }
    return current;
}

node* stmt () {
    node *current = new node(s, "s");

    token FIRST [] = {t_id, t_read, t_write, t_if, t_do, t_check};
    token FOLLOW [] = {t_eof, t_fi, t_od};
    while (!contain(input_token, FIRST, 6) && !contain(input_token, FOLLOW, 3)){
        pass(input_token);
    }
    if(contain(input_token, FIRST, 6)){
        switch (input_token) {
            case t_id:
                //std::cout << "predict stmt --> id gets rel\n";
                current->appChild(new node(id, match (t_id)));
                current->preChild(new node(assign, match (t_gets)));
                current->appChild(rel ());
                break;
            case t_read:
                //std::cout << "predict stmt --> read id\n";
                current->appChild(new node(read, match (t_read)));
                current->appChild(new node(id, match (t_id)));
                break;
            case t_write:
                //std::cout << "predict stmt --> write rel\n";
                current->appChild(new node(write, match (t_write)));
                current->appChild(rel ());
                break;
            case t_if:
                //std::cout << "predict stmt --> if rel stmt_list fi\n";
                current->appChild(new node(tif, match (t_if)));
                current->appChild(rel ());
                current->appChild(new node(lsp, "["));
                current->appChild(stmt_list ());
                current->appChild(new node(fi, match (t_fi)));
                break;
            case t_do:
                //std::cout << "predict stmt --> do stmt_list od\n";
                current->appChild(new node(tdo, match (t_do)));
                current->appChild(stmt_list ());
                current->appChild(new node(od, match (t_od)));
                break;
            case t_check:
                //std::cout << "predict stmt --> check rel\n";
                current->appChild(new node(check, match (t_check)));
                current->appChild(rel ());
                break;
            default: error ();
        }
    }
    else{
        error();
        std::cout << "Error fixed by parsing next element, skip stmt\n";
    }
    return current;
}

node* rel () {
    node *current = new node(re, "relation");

    token FIRST [] = {t_lparen, t_id, t_literal};
    token FOLLOW [] = {t_id, t_read, t_write, t_if, t_do, t_check, t_eof, t_od, t_fi, t_rparen};
    while (!contain(input_token, FIRST, 3) && !contain(input_token, FOLLOW, 10)){
        pass(input_token);
    }
    if(contain(input_token, FIRST, 3)){
        switch (input_token) {
            case t_id:
            case t_literal:
            case t_lparen:
                //std::cout << "predict rel --> expr expr_tail\n";
                current->appChild(expr ());
                current->appChild(expr_tail ());
                break;
            default: error ();
        }
    }
    else{
        error();
        std::cout << "Error fixed by parsing next element, skip rel\n";
    }
    return current;
}

node* expr () {
    node *current = new node(exp, "expression");

    token FIRST [] = {t_lparen, t_id, t_literal};
    token FOLLOW [] = {t_id, t_read, t_write, t_if, t_do, t_check, t_eof, t_od, t_fi, t_rparen, t_eq, t_uneq, t_sm, t_smeq, t_lar, t_lareq};
    while (!contain(input_token, FIRST, 3) && !contain(input_token, FOLLOW, 16)){
        pass(input_token);
    }
    if(contain(input_token, FIRST, 3)){
        switch (input_token) {
            case t_id:
            case t_literal:
            case t_lparen:
                //std::cout << "predict expr --> term term_tail\n";
                current->appChild(term ());
                current->preChild(term_tail ());
                break;
            default: error ();
        }
    }
    else{
        error();
        std::cout << "Error fixed by parsing next element, skip expr\n";
    }
    return current;
}

node* expr_tail () {
    node *current = new node(et, "expression_tail");

    token FIRST [] = {t_id, t_read, t_write, t_if, t_do, t_check, t_eof, t_od, t_fi, t_rparen, t_eq, t_uneq, t_sm, t_smeq, t_lar, t_lareq};
    while (!contain(input_token, FIRST, 16)){
        pass(input_token);
    }
    switch (input_token) {
        case t_eq:
        case t_uneq:
        case t_sm:
        case t_lar:
        case t_smeq:
        case t_lareq:
            //std::cout << "predict expr_tail --> rel_op expr\n";
            current->appChild(rel_op ());
            current->appChild(expr ());
            break;
        case t_rparen:
        case t_id:
        case t_read:
        case t_write:
        case t_do:
        case t_if:
        case t_check:
        case t_fi:
        case t_od:
        case t_eof:
            //std::cout << "predict expr_tail --> epsilon\n";
            break;          /*  epsilon production */
        default: error ();
    }
    return current;
}

node* term_tail () {
    node *current = new node(tt, "term_tail");

    token FIRST [] = {t_id, t_read, t_write, t_if, t_do, t_check, t_eof, t_od, t_fi, t_rparen, t_eq, t_uneq, t_sm, t_smeq, t_lar, t_lareq, t_add, t_sub};
    while (!contain(input_token, FIRST, 18)){
        pass(input_token);
    }
    switch (input_token) {
        case t_add:
        case t_sub:
            //std::cout << "predict term_tail --> add_op term term_tail\n";
            current->appChild(add_op ());
            current->appChild(term ());
            current->appChild(term_tail ());
            break;
        case t_rparen:
        case t_id:
        case t_read:
        case t_write:
        case t_eq:
        case t_uneq:
        case t_sm:
        case t_lar:
        case t_smeq:
        case t_lareq:
        case t_do:
        case t_if:
        case t_check:
        case t_fi:
        case t_od:
        case t_eof:
            //std::cout << "predict term_tail --> epsilon\n";
            break;          /*  epsilon production */
        default: error ();
    }
    return current;
}

node* term () {
    node *current = new node(ter, "term");

    token FIRST [] = {t_lparen, t_id, t_literal};
    token FOLLOW [] = {t_id, t_read, t_write, t_if, t_do, t_check, t_eof, t_od, t_fi, t_rparen, t_add, t_sub};
    while (!contain(input_token, FIRST, 3) && !contain(input_token, FOLLOW, 12)){
        pass(input_token);
    }
    if(contain(input_token, FIRST, 3)){
        switch (input_token) {
            case t_id:
            case t_literal:
            case t_lparen:
                //std::cout << "predict term --> factor factor_tail\n";
                current->appChild(factor ());
                current->preChild(factor_tail ());
                break;
            default: error ();
        }
    }
    else{
        error();
        std::cout << "Error fixed by parsing next element, skip term\n";
    }
    return current;
}

node* factor_tail () {
    node *current = new node(ft, "factor_tail");

    token FIRST [] = {t_id, t_read, t_write, t_if, t_do, t_check, t_eof, t_od, t_fi, t_rparen, t_eq, t_uneq, t_sm, t_smeq, t_lar, t_lareq, t_add, t_sub, t_mul, t_div};
    while (!contain(input_token, FIRST, 20)){
        pass(input_token);
    }
    switch (input_token) {
        case t_mul:
        case t_div:
            //std::cout << "predict factor_tail --> mul_op factor factor_tail\n";
            current->appChild(mul_op ());
            current->appChild(factor ());
            current->appChild(factor_tail ());
            break;
        case t_add:
        case t_sub:
        case t_rparen:
        case t_id:
        case t_read:
        case t_write:
        case t_eq:
        case t_uneq:
        case t_sm:
        case t_lar:
        case t_smeq:
        case t_lareq:
        case t_do:
        case t_if:
        case t_check:
        case t_fi:
        case t_od:
        case t_eof:
            //std::cout << "predict factor_tail --> epsilon\n";
            break;          /*  epsilon production */
        default: error ();
    }
    return current;
}

node* factor () {
    node *current = new node(fact, "factor");

    token FIRST [] = {t_lparen, t_id, t_literal};
    token FOLLOW [] = {t_id, t_read, t_write, t_if, t_do, t_check, t_eof, t_od, t_fi, t_rparen, t_eq, t_uneq, t_sm, t_smeq, t_lar, t_lareq, t_add, t_sub, t_mul, t_div};
    while (!contain(input_token, FIRST, 3) && !contain(input_token, FOLLOW, 20)){
        pass(input_token);
    }
    if(contain(input_token, FIRST, 3)){
        switch (input_token) {
            case t_id :
                //std::cout << "predict factor --> id\n";
                current->appChild(new node(id, match (t_id)));
                break;
            case t_literal:
                //std::cout << "predict factor --> literal\n";
                current->appChild(new node(lit, match (t_literal)));
                break;
            case t_lparen:
                //std::cout << "predict factor --> lparen expr rparen\n";
                current->appChild(new node(lp, match (t_lparen)));
                current->appChild(expr ());
                current->appChild(new node(rp, match (t_rparen)));
                break;
            default: error ();
        }
    }
    else{
        error();
        std::cout << "Error fixed by parsing next element, skip factor\n";
    }
    return current;
}

node* add_op () {
    node *current = new node(aop, "add_up");

    token FIRST [] = {t_add, t_sub};
    token FOLLOW [] = {t_lparen, t_id, t_literal};
    while (!contain(input_token, FIRST, 2) && !contain(input_token, FOLLOW, 3)){
        pass(input_token);
    }
    if(contain(input_token, FIRST, 2)){
        switch (input_token) {
            case t_add:
                //std::cout << "predict add_op --> add\n";
                current->appChild(new node(pluss, match (t_add)));
                break;
            case t_sub:
                //std::cout << "predict add_op --> sub\n";
                current->appChild(new node(minuss, match (t_sub)));
                break;
            default: error ();
        }
    }
    else{
        error();
        std::cout << "Error fixed by parsing next element, skip all_op\n";
    }
    return current;
}

node* mul_op () {
    node *current = new node(mop, "multiple_up");

    token FIRST [] = {t_mul, t_div};
    token FOLLOW [] = {t_lparen, t_id, t_literal};
    while (!contain(input_token, FIRST, 2) && !contain(input_token, FOLLOW, 3)){
        pass(input_token);
    }
    if(contain(input_token, FIRST, 2)){
        switch (input_token) {
            case t_mul:
                //std::cout << "predict mul_op --> mul\n";
                current->appChild(new node(times, match (t_mul)));
                break;
            case t_div:
                //std::cout << "predict mul_op --> div\n";
                current->appChild(new node(tdivides, match (t_div)));
                break;
            default: error ();
        }
    }
    else{
        error();
        std::cout << "Error fixed by parsing next element, skip mul_op\n";
    }
    return current;
}

node* rel_op () {
    node *current = new node(rop, "add_up");

    token FIRST [] = {t_eq, t_uneq, t_sm, t_smeq, t_lar, t_lareq};
    token FOLLOW [] = {t_lparen, t_id, t_literal};
    while (!contain(input_token, FIRST, 6) && !contain(input_token, FOLLOW, 3)){
        pass(input_token);
    }
    if(contain(input_token, FIRST, 6)){
        switch (input_token) {
            case t_eq:
                std::cout <<  "predict rel_op --> eq\n";
                current->appChild(new node(eq, match (t_eq)));
                break;
            case t_uneq:
                std::cout <<  "predict rel_op --> uneq\n";
                current->appChild(new node(uneq, match (t_uneq)));
                break;
            case t_sm:
                std::cout <<  "predict rel_op --> sm\n";
                current->appChild(new node(sm, match (t_sm)));
                break;
            case t_lar:
                std::cout <<  "predict rel_op --> lar\n";
                current->appChild(new node(lar, match (t_lar)));
                break;
            case t_smeq:
                std::cout <<  "predict rel_op --> smeq\n";
                current->appChild(new node(smeq, match (t_smeq)));
                break;
            case t_lareq:
                std::cout <<  "predict rel_op --> lareq\n";
                current->appChild(new node(lareq, match (t_lareq)));
                break;
            default: error ();
        }
    }
    else{
        error();
        std::cout << "Error fixed by parsing next element, skip rel_op\n";
    }
    return current;
}

int main (int argc, char *argv[]) {
    input_token = scan ();
    node *root = program ();
    root->printTree();
    return 0;
}
