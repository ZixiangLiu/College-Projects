-- CSC254
-- Zixiang Liu
-- A1
-- Partner: Yunkun Chen

with Ada.Text_IO;
with Ada.Integer_Text_IO;
with Ada.Command_line;

procedure ada_solution is
	n	: Positive;
	k	: Positive;
	type mymatrix is array(Positive range<>, Positive range<>) of Integer;

	-- recursive Factorial function, used to get the total number of combinations of n choose k
	function Factorial(number: Positive) return Positive is
		output	: Positive := 1;
	begin
		if number > 1 then
			output := number * Factorial(number-1);
		end if;
		return output;
	end Factorial;

	-- get the total number of combinations of n choose k
	function choice_number(n, k: Positive) return Positive is
		output	: Positive;
	begin
		output := Factorial(n)/(Factorial(n-k)*Factorial(k));
		return output;
	end choice_number;

	-- the main recursive function to fill the 2D array
	-- ni is lower limit, depend on current value
	-- n is upper limit, will not change
	-- k is the length of list
	-- ki is the current index horizontally
	-- ii is the current index vertially
	-- matrix is the 2d array output
	function nck(ni, n, k, ki, ii	: IN		Integer; 
		matrix			: IN OUT 	mymatrix) return Natural is
		j	: Natural := 0;
		oldii	: Natural := 0;
		newii	: Natural := 0;
	begin
		if ki = k then
			for1:
			for i in Integer range ni..n loop
				matrix(j+ii, ki) := i;
				j := j + 1;
			end loop for1;
			return j;
		else
			oldii := ii;
			newii := ii;
			for2:
			for i in Integer range ni..n loop
				j := nck(i+1, n, k, ki+1, newii, matrix);
				for3:
				for l in Integer range 0..j-1 loop
				       matrix(newii+l, ki) := i;
				end loop for3;
		 		newii := newii + j;
			end loop for2;
			return newii - oldii;
		end if;
	end nck;

	-- it initialize an array of length of total number of combinatison with zeros
	-- then use nck function to fill it
	function  n_choose_k(n, k: Positive) return mymatrix is
		length 	: Positive := choice_number(n, k);
		matrix	: mymatrix(1..length, 1..k);
	begin
		for1:
		for i in Integer range 1..length loop
			for2:
			for j in Integer range 1..k loop
				matrix(i, j) := 0;
			end loop for2;
		end loop for1;
		length := nck(1, n, k, 1, 1, matrix);
		return matrix;
	end n_choose_k;

	-- print the 2d array neatly 
	procedure print_matrix(matrix: mymatrix) is
	begin
		for1:
		for i in Integer range 1..matrix'Length(1) loop
			for2:
			for j in Integer range 1..matrix'Length(2) loop
				Ada.Integer_Text_IO.Put(matrix(i, j));
			end loop for2;
			Ada.Text_IO.New_Line;
		end loop for1;
	end print_matrix;
begin
	-- get command line argument
	k := Integer'Value(Ada.Command_line.Argument(1));
	n := Integer'Value(Ada.Command_line.Argument(2));
	print_matrix(n_choose_k(n, k));
end ada_solution;
