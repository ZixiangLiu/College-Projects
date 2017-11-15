# version 0.1 of A4
# CSC254
# Yukun Chen & Zixiang Liu
# A4
# based on Python 2.7.13

# V0.0 can get outputs of two shell command
# V0.1 can get useful outputs of dwarfdump
# V0.2 can get useful outputs of objdump -d

import subprocess
import sys
import re

# get name of my program
program = str(sys.argv[1])

# run objdump -d myprogram
obj_result = subprocess.check_output(['objdump', '-d', program])

# run dwarfdump myprogram
dwarf_result = subprocess.check_output(['dwarfdump', program])

# print results for testing
printing_shell_results = False
if printing_shell_results:
	print obj_result
	print 
	print dwarf_result

# match for useful paragraphs
dwarf_useful = re.findall(r"^[0-9a-f]x[0-9a-f]{8}.*$", dwarf_result, re.M|re.I)
obj_useful = re.findall(r"^  [0-9a-f]{6}:.*$", obj_result, re.M|re.I)

# print useful strings for testing
printing_useful = True
if printing_useful:
	for i in dwarf_useful:
		print i
	for j in obj_useful:
		print j