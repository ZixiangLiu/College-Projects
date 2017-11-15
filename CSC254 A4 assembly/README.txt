A4
CSC254
Yukun Chen & Zixiang Liu
A4
based on Python 2.7.13

This program contains 4 parts: v16.py for the main code and the code to run in interpreter, Row.py is class for a row object, header and footer are codes used to generate HTML file. 
##### MAKEFILE!!!!! ########

The implementation of the code is as follow:
    1.  Read standard outputs of dwarfdump and objdump into strings. 
    2.  Use re(python module for regular expression) to find all useful lines in those results
    3.  Convert each useful line into an object
    4.  In the order as they were inputed, extract .c/.h file names and assign to each object
    5.  Put lines of dwarf object with ET label into previous object, which has the same source file name and source file address
    6.  Find all repeat lines and give it a label grey
    7.  Acquire text from source files, and split them by new line
    8.  In assembly address increasing order, assign each block of assembly code and corresponding c code into a row
    9.  Use rows to write html
    10. Use header as the header of HTML, and footer as the footer of the HTML
    11. If row is grey, print in grey
    12. Replace all characters that's conflict with HTML syntax
    13. Output HTML file
    14. Submit the assignment!