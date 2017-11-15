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
printf("Enter n:");
int n = getint();
int cp=2;
while (1){
if (!(n>0))
break;
int found=0;
int cf1=2;
int cfx=4;
int cf1s=cf1*cf1;
while (1){
if (!(cf1s<=cp))
break;
int cf2=2;
int pr=cf1*cf2;
while (1){
if (!(pr<=cp))
break;
if (pr==cp){
found=1;
}
cf2=cf2+1;
pr=cf1*cf2;
}
cf1=cf1+1;
cf1s=cf1*cf1;
}
if (found==0){
putint(cp);
n=n-1;
}
cp=cp+1;
}
return 0;
}