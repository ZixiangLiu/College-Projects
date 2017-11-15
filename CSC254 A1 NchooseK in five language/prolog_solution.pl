/* 
* CSC254
* A1
* Zixiang Liu
* Partner: Yukun Chen
*
* In the program:
*		length is true when length of L equals K
* 		(_,_,_,[]) specifies that nck is in the form of three values and a list
* 		use similar algorithm with other languages, ni is the lower limit
* 		n is upper limit, k is list length, Ii is the current part in list, while Recur is the part of the list where we need to recursively obtain
*/

combinations(K, N, L) :- length(L, K), nck(K, 1, N, L).
	
nck(_, _, _, []).
nck(K, Ni, N, [Ii|Recur]) :- 
	L = [Ii|Recur],
	length(L, K),
	between(Ni, N, Ii),
	Nii is Ii + 1,
	Ki is K -1,
	nck(Ki, Nii, N, Recur).
