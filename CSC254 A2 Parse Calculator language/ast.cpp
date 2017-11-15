// CSC254
// A2
// Yukun Chen
// Zixiang Liu
// AST class

#include <iostream>
#include <list> 
#include <string>
#include "scan.h"

using namespace std;


class node{
private:
	ttype t;
	string n;
public:
	list<node*> children;
	node(ttype it, string in) : t(it), n(in) {}
	void setNode(ttype, string);
	void appChild(node*);
	void preChild(node*);
	void printAllChildren();
	void printTree();
};

void node::setNode(ttype input_type, string input_name){
	t = input_type;
	n = input_name;
}

void node::appChild(node *child){
	children.push_back(child);
}

void node::preChild(node *child){
	children.push_front(child);
}

void node::printAllChildren(){
	list<node*>::iterator i;
	for (i = children.begin(); i != children.end(); i++)
		(*i)->printTree();
}

void node::printTree(){
	switch (t){
		case pp:
			cout << "(program\n[";
			printAllChildren();
			cout << "]\n)\n";
			break;
		case sl:
			printAllChildren();
			break;
		case s:
			printAllChildren();
			cout << ")\n";
			break;
		case id:
			cout << "(id \"" << n << "\")"; // (id "someId")
			break;
		case assign:
			cout << "(:= ";
			break;
		case lp:
			cout << "(";
			break;
		case rp:
			cout << ")";
			break;
		case read:
			cout << "(read ";
			break;
		case write:
			cout << "(write ";
			break;
		case tif:
			cout << "(if\n";
			break;
		case fi:
			cout << "]\n";
			break;
		case tdo:
			cout << "(do\n[\n";
			break;
		case od:
			cout << "]\n";
			break;
		case check:
			cout << "(check ";
			break;
		case re:
			printAllChildren();
			break;
		case exp:
			printAllChildren();
			break;
		case et:
			printAllChildren();
			break;
		case tt:
			printAllChildren();
			break;
		case ter:
			printAllChildren();
			break;
		case ft:
			printAllChildren();
			break;
		case fact:
			printAllChildren();
			break;
		case lit:
			cout << "(num \"" << n << "\")"; // (id "someId")
			break;
		case pluss:
			cout << "(+ ";
			break;
		case minuss:
			cout << "(- ";
			break;
		case lsp:
			cout << "\n[";
			break;
		case times:
			cout << "(* ";
		case tdivides:
			cout << "(\\ ";
		case aop:
		case mop:
		case rop:
			printAllChildren();
			break;
		case eq:
			cout << "(== ";
			break;
		case uneq:
			cout << "(!= ";
			break;
		case sm:
			cout << "(< ";
			break;
		case lar:
			cout << "(> ";
			break;
		case smeq:
			cout << "(<= ";
			break;
		case lareq:
			cout << "(>= ";
			break;
	}

}

void freeTree(node *root);
void freeAllChildren(node *root);

void freeAllChildren(node *root){
	list<node*>::iterator i;
	for (i = root->children.begin(); i != root->children.end(); i++)
		freeTree((*i));
}

void freeTree(node *root){
	freeAllChildren(root);
	free(root);
}










