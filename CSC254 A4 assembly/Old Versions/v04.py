# version 0.1 of A4
# CSC254
# Yukun Chen & Zixiang Liu
# A4
# based on Python 2.7.13

# V0.0 can get outputs of two shell command
# V0.1 can get useful outputs of dwarfdump
# V0.2 can get useful outputs of objdump -d
# V0.3 add module to convert row to html files
# V0.4 put useful strings in dwarfdump in object 
# V0.5 put useful strings in objdump in object

import subprocess
import sys
import re
from Row import row

# class to store useful dwarf dump strings
class dwarf:
	def __init__(self, instring):
		self.a_addr = hex(instring[4:10])
		self.c_addr = int(instring[13:17])
		if len(instring) < 25:
			self.label = None
		else:
			self.label = instring[25:]

	def print_info(self):
		print "{} {} {}".format(self.a_addr, self.c_addr, self.label)

class obj:
	def __init__(self, instring):
		self.a_addr = instring[2:8]
		self

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

# here parse dwarf dump infos
dwarf_objs = []
for i in dwarf_useful:
	dwarf_objs.append(dwarf(i))

for j in dwarf_objs:
	j.print_info()




# block to write html file
writing_file = False
if writing_file:
	html_file = open('assembly.html', 'w')

	# add header and footer into html file
	header = open('header', 'r')
	footer = open('footer', 'r')
	html_file.write(header.read())
	header.close()

	rows = []

	# small demo
	rows.append(row("", ["Code1", "Code2"], ["Assm1"]))
	rows.append(row("tag1", ["Code3", "Code4"], ["Assm2", "Assm3", "Assm4"]))

	# add row elements into html file
	for row in rows:
		html_file.write("\t\t<tr>\n\t\t\t<td>\n")
		for code in row.column1:
			html_file.write("\t\t\t\t" + code + "<br>\n")
		html_file.write("\t\t\t</td>\n\t\t\t<td>\n")
		for assm in row.column2:
			html_file.write("\t\t\t\t" + assm + "<br>\n")
		html_file.write("\t\t\t</td>\n\t\t</tr>\n")


	html_file.write(footer.read())
	footer.close()
	html_file.close()