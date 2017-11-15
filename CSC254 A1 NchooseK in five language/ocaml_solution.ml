(* CSC254 A1 *)
(* Zixiang Liu Partner: Yukun Chen *)

let rec combinations k n =
	if ((List.length n) = k) then
		(* Base case: when k=n.length, return n *)
		[n]
	else
		(* Inductive case: return {first*[choose (n-1) elements from rest]} concatinate with {choose n elements from rest} *)
		match n with
			| [] -> []
			| first::rest -> (List.map (fun temp -> first::temp) (combinations (k-1) rest)) @ (combinations k rest)

let k = int_of_string Sys.argv.(1);;    (* get input k & n*)
let n = int_of_string Sys.argv.(2);;

let rec create_list n =         (*convert n to list [1,2,...,n]*)
        if n==1 then [1]   
        else (create_list (n-1))@[n];;

let n_list = create_list (n);;  (*convert input n to list*)

let result = combinations k n_list;;  (*call combination k n*)

let result_in_line l = String.concat " " (List.map string_of_int l)     (*split arrays in lines, and split number by space*)
let result_in_lines l = String.concat "\n" (List.map result_in_line l)

let () = Printf.printf "%s" (result_in_lines result)    (*print the output in format*)
let () = Printf.printf("\n")
