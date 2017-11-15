(*******************************************************************
    LL(1) parser generator, syntax tree builder for an extended
    calculator language, and (skeleton of an) interpreter for the
    generated syntax trees.  

    For CSC 2/454, Fall 2017
    Michael L. Scott

    If compiled and run, will execute "main()".
    Alternatively, can be "#use"-ed (or compiled and then "#load"-ed)
    into the top-level interpreter,
 *******************************************************************)

(*******************************************************************
CSC254
Group:
Yukun Chen
Zixiang Liu

bonus: 	reminder operator %
		for loop

*******************************************************************)

open List;;
(* The List library includes a large collection of useful functions.
   In the provided code, I've used assoc, exists, filter, find,
   fold_left, hd, length, map, and rev
*)

open Str;;      (* for split *)
(* The Str library provides a few extra string-processing routines.
   In particular, it provides "split", which I use to break program
   input into whitespace-separated words.  Note, however, that this
   library is not automatically available.  If you are using the
   top-level interpreter, you have to say
        #load "str.cma";;
   If you are generating an executable fron the shell, you have to say
        ocamlc str.cma interpreter.ml
*)

(* Surprisingly, compose isn't built in.  It's included in various
   widely used commercial packages, but not in the core libraries. *)
let compose f g x = f (g x);;

type symbol_productions = (string * string list list);;
type grammar = symbol_productions list;;
type parse_table = (string * (string list * string list) list) list;;
(*                  nt        predict_set   rhs *)

let calc_gram : grammar =
  [ ("P",  [["SL"; "$$"]])
  ; ("SL", [["S"; "SL"]; []])
  ; ("S",  [ ["id"; ":="; "E"]; ["read"; "id"]; ["write"; "E"]])
  ; ("E",  [["T"; "TT"]])
  ; ("T",  [["F"; "FT"]])
  ; ("TT", [["ao"; "T"; "TT"]; []])
  ; ("FT", [["mo"; "F"; "FT"]; []])
  ; ("ao", [["+"]; ["-"]])
  ; ("mo", [["*"]; ["/"]])
  ; ("F",  [["id"]; ["num"]; ["("; "E"; ")"]])
  ];;

(* for bonus, the language is extended with reminder(%) and for loop 
   for loop is in the form of:

   for id := 0 id < 10 id := id + 1
   		(body of for loop)
   rof

*)
let ecg : grammar =
  [ ("P",  [["SL"; "$$"]])
  ; ("SL", [["S"; "SL"]; []])
  ; ("S",  [ ["id"; ":="; "E"]; ["read"; "id"]; ["write"; "E"]; 
  			 ["for"; "id"; ":="; "E"; "R"; "id"; ":="; "E"; "SL"; "rof"];
             ["if"; "R"; "SL"; "fi"]; ["do"; "SL"; "od"]; ["check"; "R"]
           ])
  ; ("R",  [["E"; "ET"]])
  ; ("E",  [["T"; "TT"]])
  ; ("T",  [["F"; "FT"]])
  ; ("F",  [["id"]; ["num"]; ["("; "E"; ")"]])
  ; ("ET", [["ro"; "E"]; []])
  ; ("TT", [["ao"; "T"; "TT"]; []])
  ; ("FT", [["mo"; "F"; "FT"]; []])
  ; ("ro", [["=="]; ["<>"]; ["<"]; [">"]; ["<="]; [">="]])
  ; ("ao", [["+"]; ["-"]])
  ; ("mo", [["*"]; ["/"]; ["%"]])
  ];;

(* is e a member of list l? *)
let member e l = exists ((=) e) l;;

(* OCaml has a built-in quicksort; this was just an exercise. *)
let rec sort l =
  let rec partition pivot l left right =
    match l with
    | []        -> (left, right)
    | c :: rest -> if (compare c pivot) < 0
                   then partition pivot rest (c :: left) right
                   else partition pivot rest left (c :: right) in
  match l with
  | []        -> l
  | h :: []   -> l
  | h :: rest -> let (left, right) = partition h rest [] [] in
                 (sort left) @ [h] @ (sort right);;

(* leave only one of any consecutive identical elements in list *)
let rec unique l =
  match l with
  | []             -> l
  | h :: []        -> l
  | a :: b :: rest -> if a = b (* structural equivalence *)
                      then unique (b :: rest)
                      else a :: unique (b :: rest);;

let unique_sort l = unique (sort l);;

(* Return all individual productions in grammar. *)
let productions gram : (string * string list) list =
  let prods (lhs, rhss) = map (fun rhs -> (lhs, rhs)) rhss in
  fold_left (@) [] (map prods gram);;

(* Return all symbols in grammar. *)
let gsymbols gram : string list =
  unique_sort
    (fold_left (@) [] (map (compose (fold_left (@) []) snd) gram));;

(* Return all elements of l that are not in excluded.
   Assume that both lists are sorted *)
let list_minus l excluded =
  let rec helper rest te rtn =
    match rest with
    | []     -> rtn
    | h :: t -> match te with
                | []       -> (rev rest) @ rtn
                | h2 :: t2 -> match compare h h2 with
                              | -1 -> helper t te (h :: rtn)
                              |  0 -> helper t t2 rtn
                              |  _ -> helper rest t2 rtn in
  rev (helper l excluded []);;

(* Return just the nonterminals. *)
let nonterminals gram : string list = map fst gram;;

(* Return just the terminals. *)
let terminals gram : string list =
    list_minus (gsymbols gram) (unique_sort (nonterminals gram));;

(* Return the start symbol.  Throw exception if grammar is empty. *)
let start_symbol gram : string = fst (hd gram);;

let is_nonterminal e gram = member e (nonterminals gram);;

let is_terminal e gram = member e (terminals gram);;

let union s1 s2 = unique_sort (s1 @ s2);;

(* return suffix of lst that begins with first occurrence of sym
   (or [] if there is no such occurrence) *)
let rec suffix sym lst = 
  match lst with
  | [] -> []
  | h :: t -> if h = sym (* structural equivalence *)
              then lst else suffix sym t;;

(* Return a list of pairs.
   Each pair consists of a symbol A and a list of symbols beta
   such that for some alpha, A -> alpha B beta. *)
type right_context = (string * string list) list;;
let get_right_context (b:string) gram : right_context =
  fold_left (@) []
            (map (fun prod ->
                    let a = fst prod in
                    let rec helper accum rhs =
                      let b_beta = suffix b rhs in
                      match b_beta with
                      | [] -> accum
                      | x :: beta  -> (* assert x = b *)
                                      helper ((a, beta) :: accum) beta in
                    helper [] (snd prod))
                 (productions gram));;

(* parser generator starts here *)

type symbol_knowledge = string * bool * string list * string list;;
type knowledge = symbol_knowledge list;;
let symbol_field (s, e, fi, fo) = s;;
let eps_field (s, e, fi, fo) = e;;
let first_field (s, e, fi, fo) = fi;;
let follow_field (s, e, fi, fo) = fo;;

let initial_knowledge gram : knowledge =
  map (fun a -> (a, false, [], [])) (nonterminals gram);;

let get_symbol_knowledge (a:string) (kdg:knowledge) : symbol_knowledge =
  find (fun (s, e, fi, fo) -> s = a) kdg;;

(* Can word list w generate epsilon based on current estimates?
   if w is an empty list, yes
   if w is a single terminal, no
   if w is a single nonterminal, look it up
   if w is a non-empty list, "iterate" over elements *)
let rec generates_epsilon (w:string list) (kdg:knowledge) gram : bool =
  match w with
  | [] -> true
  | h :: t -> if is_terminal h gram then false
              else eps_field (get_symbol_knowledge h kdg)
                   && generates_epsilon t kdg gram;;

(* Return FIRST(w), based on current estimates.
   if w is an empty list, return []  [empty set]
   if w is a single terminal, return [w]
   if w is a single nonterminal, look it up
   if w is a non-empty list, "iterate" over elements *)
let rec first (w:string list) (kdg:knowledge) gram : (string list) =
  match w with
  | [] -> []
  | x :: _ when is_terminal x gram -> [x]
  | x :: rest -> let s = first_field (get_symbol_knowledge x kdg) in
                 if generates_epsilon [x] kdg gram
                 then union s (first rest kdg gram)
                 else s;;

let follow (a:string) (kdg:knowledge) : string list =
  follow_field (get_symbol_knowledge a kdg);;

let rec map3 f l1 l2 l3 =
  match (l1, l2, l3) with
  | ([], [], []) -> []
  | (h1 :: t1, h2 :: t2, h3 :: t3) -> (f h1 h2 h3) :: map3 f t1 t2 t3
  | _ -> raise (Failure "mismatched_lists");;

(* Return knowledge structure for grammar.
   Start with (initial_knowledge grammar) and "iterate",
   until the structure doesn't change.
   Uses (get_right_context B gram), for all nonterminals B,
   to help compute follow sets. *)
let get_knowledge gram : knowledge =
  let nts : string list = nonterminals gram in
  let right_contexts : right_context list
     = map (fun s -> get_right_context s gram) nts in
  let rec helper kdg =
    let update : symbol_knowledge -> symbol_productions
                   -> right_context -> symbol_knowledge
          = fun old_sym_kdg sym_prods sym_right_context ->
      let my_first s = first s kdg gram in
      let my_eps s = generates_epsilon s kdg gram in
      let filtered_follow p = if my_eps (snd p)
                              then (follow (fst p) kdg)
                              else [] in
      (
        symbol_field old_sym_kdg,       (* nonterminal itself *)
        (eps_field old_sym_kdg)         (* previous estimate *)
            || (fold_left (||) false (map my_eps (snd sym_prods))),
        union (first_field old_sym_kdg) (* previous estimate *)
            (fold_left union [] (map my_first (snd sym_prods))),
        union (union
                (follow_field old_sym_kdg)  (* previous estimate *)
                (fold_left union [] (map my_first
                                      (map (fun p ->
                                              match snd p with
                                              | [] -> []
                                              | h :: t -> [h])
                                           sym_right_context))))
              (fold_left union [] (map filtered_follow sym_right_context))
      ) in    (* end of update *)
    let new_kdg = map3 update kdg gram right_contexts in
    (* body of helper: *)
    if new_kdg = kdg then kdg else helper new_kdg in
  (* body of get_knowledge: *)
  helper (initial_knowledge gram);;

let get_parse_table (gram:grammar) : parse_table =
  let kdg = get_knowledge gram in
  map (fun (lhs, rhss) ->
        (lhs, (map (fun rhs ->
                      (union (first rhs kdg gram)
                             (if (generates_epsilon rhs kdg gram)
                              then (follow lhs kdg) else []),
                      rhs))
                   rhss)))
      gram;;

(* convert string to list of char *)
let explode (s:string) : char list =
  let rec exp i l = if i < 0 then l else exp (i-1) (s.[i] :: l) in
  exp (String.length s - 1) [];;

(* convert list of char to string
   (This uses imperative features.  It used to be a built-in.) *)
let implode (l:char list) : string =
  let res = Bytes.create (length l) in
  let rec imp i l =
    match l with
    | [] -> Bytes.to_string res
    | c :: l -> Bytes.set res i c; imp (i + 1) l in
  imp 0 l;;

(*******************************************************************
   scanner:
 *******************************************************************)

type token = string * string;;
(*         category * name *)

let tokenize (program:string) : token list =
  let get_id prog =
    let rec gi tok p =
        match p with
        | c :: rest when (('a' <= c && c <= 'z')
                       || ('A' <= c && c <= 'Z')
                       || ('0' <= c && c <= '9') || (c = '_'))
            -> gi (c :: tok) rest
        | _ -> (implode (rev tok), p) in
    gi [] prog in
  let get_int prog =
    let rec gi tok p =
        match p with
        | c :: rest when ('0' <= c && c <= '9')
            -> gi (c :: tok) rest
        | _ -> (implode (rev tok), p) in
    gi [] prog in
  let get_num prog =
    let (tok, rest) = get_int prog in
    match rest with
    | '.' :: c :: r when ('0' <= c && c <= '9')
        -> let (tok2, rest2) = get_int (c :: r) in
           ((tok ^ "." ^ tok2), rest2)
    | _ -> (tok, rest) in
  let rec get_error tok prog =
    match prog with
    | [] -> (implode (rev tok), prog)
    | c :: rest ->
        match c with
        | ':' | '+' | '-' | '*' | '/' | '%' | '(' | ')' | '_' (* for bonus *)
        | '<' | '>' | '=' | 'a'..'z' | 'A'..'Z' | '0'..'9'
            -> (implode (rev tok), prog)
        | _ -> get_error (c :: tok) rest in
  let rec skip_space prog =
    match prog with
    | [] -> []
    | c :: rest -> if (c = ' ' || c = '\n' || c = '\r' || c = '\t')
                      then skip_space rest else prog in
  let rec tkize toks prog =
    match prog with
    | []                 -> (("$$" :: toks), [])
    | '\n' :: rest
    | '\r' :: rest
    | '\t' :: rest
    | ' ' :: rest        -> tkize toks (skip_space prog)
    | ':' :: '=' :: rest -> tkize (":=" :: toks) rest
    | '-' :: rest        -> tkize ("-"  :: toks) rest
    | '+' :: rest        -> tkize ("+"  :: toks) rest
    | '/' :: rest        -> tkize ("/"  :: toks) rest
    | '%' :: rest        -> tkize ("%"  :: toks) rest (* for bonus *)
    | '*' :: rest        -> tkize ("*"  :: toks) rest
    | '(' :: rest        -> tkize ("("  :: toks) rest
    | ')' :: rest        -> tkize (")"  :: toks) rest
    | '<' :: '>' :: rest -> tkize ("<>" :: toks) rest
    | '<' :: '=' :: rest -> tkize ("<=" :: toks) rest
    | '<' :: rest        -> tkize ("<"  :: toks) rest
    | '>' :: '=' :: rest -> tkize (">=" :: toks) rest
    | '>' :: rest        -> tkize (">"  :: toks) rest
    | '=' :: '=' :: rest -> tkize ("==" :: toks) rest
    | h :: t -> match h with
           | '0'..'9' -> let (t, rest) = get_num prog in
                         tkize (t :: toks) rest
           | 'a'..'z'
           | 'A'..'Z'
           | '_'      -> let (t, rest) = get_id prog in
                         tkize (t :: toks) rest
           | c        -> let (t, rest) = get_error [c] t in
                         tkize (t :: toks) rest in
  let (toks, _) = (tkize [] (explode program)) in
  let categorize tok =
    match tok with
    | "check" | "do" | "for" | "rof" | "fi" (* also added for bonus *)
    | "if"    | "od" | "read" | "write"
    | ":=" | "+"  | "-"  | "*"  | "/"  | "%"  | "("  | ")"
    | "<"  | "<=" | ">"  | ">=" | "==" | "<>" | "$$"
        -> (tok, tok)
    | _ -> match tok.[0] with
           | '0'..'9' -> ("num", tok)
           | 'a'..'z'
           | 'A'..'Z' | '_' -> ("id", tok)
           | _ -> ("error", tok) in
  map categorize (rev toks);;

(*******************************************************************
   The main parse routine below returns a parse tree (or PT_error if
   the input program is syntactically invalid).  To build that tree it
   employs a simplified version of the "attribute stack" described in
   Section 4.5.2 (pages 50-55) on the PLP companion site.
  
   When it predicts A -> B C D, the parser pops A from the parse stack
   and then, before pushing D, C, and B (in that order), it pushes a
   number (in this case 3) indicating the length of the right hand side.
   It also pushes A into the attribute stack.
  
   When it matches a token, the parser pushes this into the attribute
   stack as well.
  
   Finally, when it encounters a number (say k) in the stack (as opposed
   to a character string), the parser pops k+1 symbols from the
   attribute stack, joins them together into a list, and pushes the list
   back into the attribute stack.
  
   These rules suffice to accumulate a complete parse tree into the
   attribute stack at the end of the parse.
  
   Note that everything is done functionally.  We don't really modify
   the stacks; we pass new versions to a tail recursive routine.
 *******************************************************************)

(* Extract grammar from parse-tab, so we can invoke the various routines
   that expect a grammar as argument. *)
let grammar_of (parse_tab:parse_table) : grammar =
    map (fun p -> (fst p, (fold_left (@) [] (map (fun (a, b) -> [b])
                                                 (snd p))))) parse_tab;;

type parse_tree =   (* among other things, parse_trees are *)
| PT_error          (* the elements of the attribute stack *)
| PT_id of string
| PT_num of string
| PT_term of string
| PT_nt of (string * parse_tree list);;
    
(* Pop rhs-len + 1 symbols off the attribute stack,
   assemble into a production, and push back onto the stack. *)
let reduce_1_prod (astack:parse_tree list) (rhs_len:int) : parse_tree list =
  let rec helper atk k prod =
    match (k, atk) with
    | (0, PT_nt(nt, []) :: rest) -> PT_nt(nt, prod) :: rest
    | (n, h :: rest) when n <> 0 -> helper rest (k - 1) (h :: prod)
    | _ -> raise (Failure "expected nonterminal at top of astack") in
   helper astack rhs_len [];;

let sum_ave_prog = "read a read b sum := a + b write sum write sum / 2";;

let primes_prog = "
     read n
     cp := 2
     do check n > 0
         found := 0
         cf1 := 2
         cf1s := cf1 * cf1
         do check cf1s <= cp
             cf2 := 2
             pr := cf1 * cf2
             do check pr <= cp
                 if pr == cp
                     found := 1
                 fi
                 cf2 := cf2 + 1
                 pr := cf1 * cf2
             od
             cf1 := cf1 + 1
             cf1s := cf1 * cf1
         od
         if found == 0
             write cp
             n := n - 1
         fi
         cp := cp + 1
     od";;

type parse_action = PA_error | PA_prediction of string list;;
(* Double-index to find prediction (list of RHS symbols) for
   nonterminal nt and terminal t.
   Return PA_error if not found. *)
let get_parse_action (nt:string) (t:string) (parse_tab:parse_table) : parse_action =
    let rec helper l =
        match l with
        | [] -> PA_error
        | (fs, rhs) :: rest -> if member t fs then PA_prediction(rhs)
                               else helper rest in
    helper (assoc nt parse_tab);;

type ps_item =
| PS_end of int
| PS_sym of string;;

(* Parse program according to grammar.
   [Commented-out code would
       print predictions and matches (imperatively) along the way.]
   Return parse tree if the program is in the language; PT_error if it's not. *)
let parse (parse_tab:parse_table) (program:string) : parse_tree =
  let die msg = begin
                  print_string ("syntax error: " ^ msg);
                  print_newline ();
                  PT_error 
                end in
  let gram = grammar_of parse_tab in
  let rec helper pstack tokens astack =
    match pstack with
    | [] ->
        if tokens = [] then
          (* assert: astack is nonempty *)
          hd astack
        else die "extra input beyond end of program"
    | PS_end(n) :: ps_tail ->
        helper ps_tail tokens (reduce_1_prod astack n)
    | PS_sym(tos) :: ps_tail ->
        match tokens with
        | [] -> die "unexpected end of program"
        | (term, tok) :: more_tokens ->
           (* if tok is an individual identifier or number,
              term will be a generic "id" or "num" *)
          if is_terminal tos gram then
            if tos = term then
              begin
              (*
                print_string ("   match " ^ tos);
                print_string
                    (if tos <> term      (* value comparison *)
                         then (" (" ^ tok ^ ")") else "");
                print_newline ();
              *)
                helper ps_tail more_tokens
                       (( match term with
                          | "id"  -> PT_id tok
                          | "num" -> PT_num tok
                          | _     -> PT_term tok ) :: astack)
              end
                       (* note push of tok into astack *)
            else die ("expected " ^ tos ^ " ; saw " ^ tok)
          else (* nonterminal *)
            match get_parse_action tos term parse_tab with
            | PA_error -> die ("no prediction for " ^ tos
                               ^ " when seeing " ^ tok)
            | PA_prediction(rhs) ->
                begin
                (*
                  print_string ("   predict " ^ tos ^ " ->");
                  print_string (fold_left (fun a b -> a ^ " " ^ b) "" rhs);
                  print_newline ();
                *)
                  helper ((fold_left (@) [] 
                                    (map (fun s -> [PS_sym(s)]) rhs))
                              @ [PS_end(length rhs)] @ ps_tail)
                         tokens (PT_nt(tos, []) :: astack)
                end in
  helper [PS_sym(start_symbol gram)] (tokenize program) [];;

let cg_parse_table = get_parse_table calc_gram;;

let ecg_parse_table = get_parse_table ecg;;

(* 
this function is used to print a parse tree, used in debugging
 *)
let rec print_parse_tree (pt:parse_tree) = 
  match pt with
  | PT_nt (fhd, stl) -> print_string ("Non_terminal: " ^ fhd ^ " start:");
          print_newline ();
          ignore (map print_parse_tree stl);
          print_string ("Non_terminal: " ^ fhd ^ " end.");
          print_newline ();
  | PT_id strarg ->  print_string ("Id: " ^ strarg);
              print_newline ();
  | PT_num strarg ->  print_string ("Num: " ^ strarg);
              print_newline ();
  | PT_term strarg ->  print_string ("Term: " ^ strarg);
              print_newline ();
  | PT_error -> print_string "Error here";
              print_newline ();;

(* comment the following line to avoid print parse tree *)
(*print_parse_tree (parse ecg_parse_table primes_prog);;*)

(*******************************************************************
  Everything above this point in the file is complete and (I think)
  usable as-is.  The rest of the file, from here down, is a skeleton
  for the extra code you need to write.  It has been excised from my
  working solution to the assignment.  You are welcome, of course, to
  use a different organization if you prefer.  This is provided in the
  hope you may find it useful.
 *******************************************************************)

(*
the following block is used as a reference when writing the rest of the code

type parse_tree =   (* among other things, parse_trees are *)
| PT_error          (* the elements of the attribute stack *)
| PT_id of string
| PT_num of string
| PT_term of string
| PT_nt of (string * parse_tree list);;
*)


(* a new constructor of ast_e is generated for parentheses*)
type ast_sl = ast_s list
and ast_s =
| AST_error
| AST_assign of (string * ast_e)
| AST_read of string
| AST_write of ast_e
| AST_if of (ast_e * ast_sl)
| AST_do of ast_sl
| AST_check of ast_e
| AST_for of (string * ast_e *  ast_e *    ast_e *    ast_sl)
(* for loop:  id       initial  boundary   increment  statement inside*)
and ast_e =
| AST_binop of (string * ast_e * ast_e)
| AST_id of string
| AST_num of string
| AST_paren of ast_e;;

(* 
main function to translate parse tree to ast 
Used same structure with professor. 
*)
let rec ast_ize_P (p:parse_tree) : ast_sl = (* if the parse tree node is P*)
  match p with
  | PT_nt ("P", [sl; PT_term "$$"]) -> ast_ize_SL sl
  | PT_nt (somestr, somelist) -> raise (Failure ("Malformed parse tree in ast_ize_SL. Expected P, but see nonterminal " ^ somestr))
  | PT_term termval -> raise (Failure ("Malformed parse tree in ast_ize_P. Expected P, but see term " ^ termval))
  | PT_id idname -> raise (Failure ("Malformed parse tree in ast_ize_P. Expected P, but see id " ^ idname))
  | PT_num numval -> raise (Failure ("Malformed parse tree in ast_ize_P. Expected P, but see num " ^ numval))
  | _ -> raise (Failure "Malformed parse tree in ast_ize_P.")

and ast_ize_SL (sl:parse_tree) : ast_sl = (* if the parse tree node is SL*)
  match sl with
  | PT_nt ("SL", []) -> []
  | PT_nt ("SL", [s; sl]) -> ([ast_ize_S s] @ (ast_ize_SL sl))
  | PT_nt (somestr, somelist) -> raise (Failure ("Malformed parse tree in ast_ize_SL. Expected SL, but see nonterminal " ^ somestr))
  | PT_term termval -> raise (Failure ("Malformed parse tree in ast_ize_SL. Expected SL, but see term " ^ termval))
  | PT_id idname -> raise (Failure ("Malformed parse tree in ast_ize_SL. Expected SL, but see id " ^ idname))
  | PT_num numval -> raise (Failure ("Malformed parse tree in ast_ize_SL. Expected SL, but see num " ^ numval))
  | _ -> raise (Failure "Malformed parse tree in ast_ize_SL. ")

and ast_ize_S (s:parse_tree) : ast_s = (* if the parse tree node is S*)
  match s with
  | PT_nt ("S", [PT_id lhs; PT_term ":="; expr]) (*assign*)
        -> AST_assign (lhs, (ast_ize_expr expr))
  | PT_nt ("S", [PT_term "read"; PT_id idname]) (*read*)
        -> AST_read idname
  | PT_nt ("S", [PT_term "write"; expr]) (*write*)
        -> AST_write (ast_ize_expr expr)
  | PT_nt ("S", [PT_term "if"; reln; sl; PT_term "fi"]) (*if*)
        -> AST_if ((ast_ize_expr reln), (ast_ize_SL sl))
  | PT_nt ("S", [PT_term "do"; sl; PT_term "od"]) (*do*)
        -> AST_do (ast_ize_SL sl)
  | PT_nt ("S", [PT_term "check"; reln]) (*check*)
        -> AST_check (ast_ize_bool (reln, "Error in check process. "))
        (* 	for loop is extended
        	["for"; "id"; ":="; "E"; ";"; "R"; ";"; "id"; ":="; "E"; ";"; "SL"; "rof"] *)
  | PT_nt ("S", [PT_term "for"; PT_id id1; PT_term ":="; expr1; reln;
  				 PT_id id2; PT_term ":="; expr2; sl; PT_term "rof"])
  		-> 	if (not (id1 = id2)) then raise (Failure "For loop has to be in the form of \"for id := initial id relation id increment\" where all three ids must match\n")
  			else AST_for (id1, (ast_ize_expr expr1), (ast_ize_bool (reln, "Error in for process. ")), (ast_ize_expr expr2), (ast_ize_SL sl))
  | _ -> raise (Failure "Malformed parse tree in ast_ize_S. ")

and ast_ize_expr (e:parse_tree) : ast_e = (* if the parse tree node is R, E, T, F*)
  (* e is an R, E, T, or F parse tree node *)
  match e with
  | PT_nt ("R", [expr; expr_tail]) -> ast_ize_reln_tail (ast_ize_expr expr) expr_tail (*relation*)
  | PT_nt ("E", [t; tt]) -> ast_ize_expr_tail (ast_ize_expr t) tt (*expression*)
  | PT_nt ("T", [f; ft]) -> ast_ize_expr_tail (ast_ize_expr f) ft (*term*)
  | PT_nt ("F", [PT_id idname]) -> AST_id idname (*factor, one of the three productions*)
  | PT_nt ("F", [PT_num numval]) -> AST_num numval
  | PT_nt ("F", [PT_term "("; reln; PT_term ")"]) -> AST_paren (ast_ize_expr reln) (*note here the AST_paren is new from professor's originalcode*)
  | _ -> raise (Failure "Malformed parse tree in ast_ize_expr. ")

(* the following method is used to check if the relation is a judgement or not 
   only for and check need this expression
*)
and ast_ize_bool (e:parse_tree * string) : ast_e = 
  match e with 
  | (PT_nt ("R", [expr; expr_tail]), str) -> 
    (match expr_tail with 
        | PT_nt ("ET", []) -> raise (Failure ("This is not a judgement. " ^ str))
        | _ -> ast_ize_reln_tail (ast_ize_expr expr) expr_tail)
  | _ -> raise (Failure "Malformed parse tree in ast_ize_expr. ")

and ast_ize_reln_tail (lhs:ast_e) (tail:parse_tree) : ast_e = (* if the parse tree node is ET*)
  (* lhs in an inherited attribute.
     tail is an ET parse tree node *)
  match tail with
  | PT_nt ("ET", []) -> lhs
  | PT_nt ("ET", [PT_nt ("ro", [PT_term termval]); expr]) -> AST_binop (termval, lhs, (ast_ize_expr expr)) (*if expresion tail is not empty, 
  																											build AST_binop and put lhs in front*)
  | _ -> raise (Failure "Malformed parse tree in ast_ize_reln_tail. ")

and ast_ize_expr_tail (lhs:ast_e) (tail:parse_tree) : ast_e = (* if the parse tree node is TT, FT*)
  (* lhs in an inherited attribute.
     tail is a TT or FT parse tree node *)
  match tail with
  | PT_nt ("TT", []) -> lhs (*if tt is epsilon*)
  | PT_nt ("TT", [PT_nt ("ao", [PT_term termval]); t; tt]) -> ast_ize_expr_tail (AST_binop (termval, lhs, (ast_ize_expr t))) tt (*if there is a tail*)
  | PT_nt ("FT", []) -> lhs
  | PT_nt ("FT", [PT_nt ("mo", [PT_term termval]); f; ft]) -> ast_ize_expr_tail (AST_binop (termval, lhs, (ast_ize_expr f))) ft
  | _ -> raise (Failure "Malformed parse tree in ast_ize_expr_tail. ")
;;

(* 
This is a reference to write print function

type ast_sl = ast_s list
and ast_s =
| AST_error
| AST_assign of (string * ast_e)
| AST_read of string
| AST_write of ast_e
| AST_if of (ast_e * ast_sl)
| AST_do of ast_sl
| AST_check of ast_e
| AST_for of (string * ast_e *  ast_e *    ast_e *    ast_sl)
(* for loop:  id       initial  boundary   increment  statement inside*)
and ast_e =
| AST_binop of (string * ast_e * ast_e)
| AST_id of string
| AST_num of string
| AST_paren of ast_e;;
 *)

(* print functions to test AST *)
(* because ast_s and ast_e are different types, two functions are writen to print them all *)
let rec print_aste (aste:ast_e) = 
	match aste with
	| AST_binop (opval, aste1, aste2) -> print_aste aste1; print_string opval; print_aste aste2;
	| AST_id idname -> print_string idname
	| AST_num numval -> print_string numval
	| AST_paren reln -> print_string "(";
						print_aste reln;
						print_string ")";;

(* use "map print_ast astrootlist" to print whole AST of astrootlist *)
let rec print_ast (ast:ast_s) = 
	match ast with
	| AST_error -> print_string "Error in ast\n"
	| AST_assign (idname, assignval)-> 	print_string (idname ^ ":="); 
										print_aste assignval;
	| AST_read idname -> print_string ("read\n" ^ idname)
	| AST_write aste -> print_string "write\n"; 
						print_aste aste;
	| AST_if (aste, astl) -> 	print_string "if{\n";
								print_aste aste;
								ignore (map print_ast astl);
								print_string "\n}\n";
	| AST_do astl -> 	print_string "do{\n";
						ignore (map print_ast astl);
						print_string "\n}\n";
	| AST_check aste -> print_string "check:\n";
						print_aste aste;
	| AST_for (idname, aste1, aste2, aste3, sl) -> 	print_string ("for ("^idname^"=");
													print_aste aste1;
													print_string ";";
													print_aste aste2;
													print_string (";"^idname^"=");
													print_aste aste3;
													print_string "){\n";
													ignore (map print_ast sl);
													print_string "\n}\n";;

(* the following is a wrapper *)
let print_astsl astsl = map print_ast astsl;;

(* this line print the ast of primes program *)
(* print_astsl (ast_ize_P (parse ecg_parse_table primes_prog));; *)

(*******************************************************************
    Translate to C
 *******************************************************************)

(*  var_used is a variable to record variables' assignment and usement.
    It's a tuple list, with first element in each tuple record the name
    of variables, and second variable false for variable assigned but not
    used and true for variable assigned and used*)
type var_used = (string * bool) list;;

(* function to print var_used in readable format *)
let rec print_var (var:var_used) =
    match var with
    |[] -> ();
    |(str, bl)::var_r ->
        Printf.printf "%s: %B\n" str bl;
        print_var var_r;;

(* function to check var_used and return all the unused variable in warning format *)
let rec check_var (var:var_used)(error:string) : string=
    match var with
    |[] -> "\n";
    |(str, bl)::var_r ->
        if not bl then error^"Warning: variable "^str^" assigned but not used.\n"^(check_var var_r "")
        else error^(check_var var_r "");;

(*  function to check whether value included in the left part of a two element tuple list
    it will be used to check whether the variable has assigned or not *)
let rec exists_left list value = List.exists (fun (x, _) -> x = value) list;

(*  function to change the bool value of string * bool tuple list if string match the keyword
    it will be used to mark the variable if it has been used for at least once *)
and turn_right_to_true (var:var_used)(id:string) : var_used = 
    match var with
    |[]->[]
    |(str, bl)::var_r ->
        if str=id then var_r @ [(id, true)]
        else (str, bl)::turn_right_to_true var_r id;

(* function to translate the program *)
and translate (ast:ast_sl) : string * string =
    (* it will call translate_sl function and initiate an empty var_used list *)
    match (translate_sl ast []) with (var, ast_sl) ->
    (check_var var "",
    (* header file *)
    "#include \"stdio.h\"\n" ^
    "#include \"stdlib.h\"\n\n" ^
    "int divide(int n1, int n2){
    if(n2 != 0){
        return (int) (n1/n2);
    }else{
        printf(\"Divded by 0 at %d/%d, the result is set to 1.\\n\", n1, n2);
        return 1;
    }
}

int getint(){
	int oneint;
	int result = scanf(\"%d\", &oneint);
	if (result == EOF){
    	printf(\"Unexpected ending in read process.\\n\");
	}else if (result == 0){
    	printf(\"Input type is not integer.\\n\");
	}
	return oneint;
}

int reminder(int n1, int n2){
    if(n2 != 0){
        return (int) (n1%n2);
    }else{
        printf(\"Reminder by 0 at %d%%%d, the result is set to 1.\\n\", n1, n2);
        return 1;
    }
}

void putint(int n){
    printf(\"%d\\n\", n);
}\n\n" ^
    "int main () {\n" ^
    ast_sl ^
    "return 0;\n}\n");

(* function to translate sl by calling list of s *)
and translate_sl (ast:ast_sl)(var:var_used) : var_used * string =
    match ast with
    (* loop through the sl list and call s until there is no s in sl *)
    |[] -> (var, "")
    |ast_s::ast_sl ->
        match (translate_s ast_s var) with (var_used1, string1) ->
            match (translate_sl ast_sl var_used1) with (var_used2, string2) ->
                (var_used2, string1 ^ string2);

(* function to translate s *)
and translate_s (ast:ast_s)(var:var_used) : var_used * string = 
    match ast with
    | AST_error -> ([], "Error");
    | AST_assign ast_assign -> translate_assign ast_assign var;
    | AST_read ast_read -> translate_read ast_read var;
    | AST_write ast_write -> translate_write ast_write var;
    | AST_if ast_if -> translate_if ast_if var;
    | AST_do ast_do -> translate_do ast_do var;
    | AST_check ast_check -> translate_check ast_check var;
    | AST_for ast_for -> translate_for ast_for var;

(* function to translate assign *)
and translate_assign (ast:string * ast_e)(var:var_used) : var_used * string = 
    match ast with (str, ast_e) ->
        match translate_expr ast_e var with (var_e, str_e) ->
            (* if variable has not been assigned yet, add the variable in var_used *)
            if not (exists_left var str) then 
                (var_e @ [(str, false)], "int " ^ str ^ "=" ^ str_e ^ ";\n")
            (* if variable already in var_used, just pass it to next AST node *)
            else (var_e, str ^ "=" ^ str_e ^ ";\n");

(* function to translate assign used in for loop *)
and translate_forassign (ast:string * ast_e)(var:var_used) : var_used * string = 
    match ast with (str, ast_e) ->
        match translate_expr ast_e var with (var_e, str_e) ->
            (* if variable has not been assigned yet, add the variable in var_used *)
            if not (exists_left var str) then 
                (var_e @ [(str, false)], "int " ^ str ^ "=" ^ str_e) (* in for loop, no need for new line *)
            (* if variable already in var_used, just pass it to next AST node *)
            else (var_e, str ^ "=" ^ str_e);

(* function to translate read *)
and translate_read (ast:string)(var:var_used) : var_used * string =
    (* similar to assign, if variable has not been assigned, add to var_used *)
    if not (exists_left var ast) then 
        (var @ [(ast, false)],
        "printf(\"Enter " ^ ast ^ ":\");\n" ^
        "int " ^ ast ^ " = getint();\n")
    else (var,
        "printf(\"Enter " ^ ast ^ ":\");\n" ^
        ast ^ " = getint();\n");

(* function to translate write *)
and translate_write (ast:ast_e)(var:var_used) : var_used * string =
    match translate_expr ast var with (var_e, str_e) ->
        (var_e, "putint(" ^ str_e ^ ");\n");

(* function to translate if *)
and translate_if (ast:ast_e * ast_sl)(var:var_used) : var_used * string =
    match ast with (ast_e, ast_sl) ->
        match (translate_expr ast_e var) with (var_e, str_e) ->
            match (translate_sl ast_sl var_e) with (var_sl, str_sl) ->
                (var_sl, "if (" ^ str_e ^ "){\n" ^
                str_sl ^ "}\n");

(* function to translate do *)
and translate_do (ast:ast_sl)(var:var_used) : var_used * string =
    match translate_sl ast var with (var_sl, str_sl) ->
        (* for calculator language, we assume do is used pairing with check *)
        (var_sl, "while (1){\n" ^ str_sl ^ "}\n");

(* function to translate check *)
and translate_check (ast:ast_e)(var:var_used) : var_used * string =
    match translate_expr ast var with (var_e, str_e) ->
        (var_e, "if (!(" ^ str_e ^ "))\nbreak;\n");

and translate_for (ast:(string * ast_e * ast_e * ast_e * ast_sl))(var:var_used) : var_used * string =
	match ast with (id1, ast_e1, reln, ast_e2, ast_sl) ->
		match (translate_forassign (id1, ast_e1) var) with (var_e1, str_e1) -> 
			match (translate_expr reln var_e1) with (var_reln, str_reln) -> 
				match (translate_forassign (id1, ast_e2) var_reln) with (var_e2, str_e2) -> 
					match (translate_sl ast_sl var_e2) with (var_sl, str_sl) ->
						(var_sl, "for ("^str_e1^";"^str_reln^";"^str_e2^") {\n"^str_sl^"}\n");
		

(* function to translate expression *)
and translate_expr (ast:ast_e)(var:var_used) : var_used * string =
    match ast with
    | AST_id id -> 
        (* if variable in expression is assigned, marked it as used *)
        if exists_left var id then (turn_right_to_true var id, id)
        (* else, raise error *)
        else raise (Failure "variable used before assigned")
    | AST_num num -> (var, num)
    | AST_paren ast_e ->
        (match (translate_expr ast_e var) with (var_p, str_p) ->
            (var_p, "("^str_p^")"))
    | AST_binop (str, ast_e1, ast_e2) ->
        match (translate_expr ast_e1 var) with (var_e1, str_e1) ->
            match (translate_expr ast_e2 var_e1) with (var_e2, str_e2) ->
                match str with
                |"<>" -> (var_e2, str_e1 ^ "!=" ^ str_e2)
                |"/" -> (var_e2, "divide("^str_e1^", "^str_e2^")")
                |"%" -> (var_e2, "reminder("^str_e1^", "^str_e2^")")
                |_ -> (var_e2, str_e1 ^ str ^ str_e2);;

let to_c prog = translate (ast_ize_P (parse ecg_parse_table prog));;

(*****************************************************************
 test using prime program 
*****************************************************************)
let primes_prog1 = "
     read n
     cp := 2
     do check n > 0
         found := 0
         cf1 := 2
         cfx := 4
         cf1s := cf1 * cf1 * cfz
         do check cf1s <= cp
             cf2 := 2
             pr := cf1 * cf2
             do check pr <= cp
                 if pr == cp
                     found := 1
                 fi
                 cf2 := cf2 + 1
                 pr := cf1 * cf2
             od
             cf1 := cf1 + 1
             cf1s := cf1 * cf1
         od
         if found == 0
             write cp
             n := n - 1
         fi
         cp := cp + 1
     od";;

let (my_warnings, my_C_prog) = to_c primes_prog1;;

print_string(my_warnings);;
print_newline ();;
print_string(my_C_prog);;

(*****************************************************************
   the following program is used to test bonus functions 
   uncomment to print c string
*****************************************************************)

(* let bonus_prog = "  read a 
          c := 0
          for i := 1 i <> 5 i := i + 1
            c := a % i + c
            write c
          rof
          write c 
          ";;

(* Here the test program contains for loop and reminder operator *)
print_string "\n\nNow start test case for bonus functions.\n\n";;
(* print_parse_tree (parse ecg_parse_table bonus_prog);; *)
let (bonus_warnings, bonus_C_prog) = to_c bonus_prog;;

print_string(bonus_warnings);;
print_newline ();;
print_string(bonus_C_prog);; *)

(*****************************************************************
   The following program test divide by 0
   it is handled in C
 *****************************************************************)

(* let divide0 = "a := 1 b := 0 write a/b";;
print_string "\n\nNow start test case for divide by zero.\n\n";;
let (divide_warnings, divide_C_prog) = to_c divide0;;

print_string(divide_warnings);;
print_newline ();;
print_string(divide_C_prog);; *)

(*****************************************************************
   The following program test reminder by 0
   it is handled in C
 *****************************************************************)
 
(* let reminder0 = "a := 1 b := 0 write a%b";;
print_string "\n\nNow start test case for reminder by zero.\n\n";;
let (reminder_warnings, reminder_C_prog) = to_c reminder0;;

print_string(reminder_warnings);;
print_newline ();;
print_string(reminder_C_prog);; *)

(*****************************************************************
   The following program test check a non judgement statement
   it is handled in C
 *****************************************************************)
 
(* let judgement0 = " read n
                  do 
                      check n + 1
                  od
                  write n";;
print_string "\n\nNow start test case for check non judgement.\n\n";;
let (judgement_warnings, judgement_C_prog) = to_c judgement0;;

print_string(judgement_warnings);;
print_newline ();;
print_string(judgement_C_prog);; *)

