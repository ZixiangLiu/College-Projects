# version 0.1 of A4
# CSC254
# Yukun Chen & Zixiang Liu
# A4
# based on Python 2.7.13

# Change Log
# V0.0 can get outputs of two shell command
# V0.1 can get useful outputs of dwarfdump
# V0.2 can get useful outputs of objdump -d
# V0.3 add module to convert row to html files
# V0.4 put useful strings in dwarfdump in object 
# V0.5 put useful strings in objdump in object
# V0.6 get all .c and .h files names, and fix dictionary bug with repeat address
# V0.7 get all .c and .h file texts
# V0.8 assign file name to each dwarf_obj
# V0.9 add c file range to each dwarf_obj

import subprocess
import sys
import re
from Row import row

# class to store useful dwarf dump strings
class dwarf:
	def __init__(self, instring):
		self.a_addr = hex(int(instring[4:10],16))
		self.c_addr = int(instring[13:17])
		self.filename = None
		if len(instring) < 25:
			self.label = None
		else:
			self.label = instring[25:]

	def print_info(self):
		print "{} {} {}".format(self.a_addr, self.c_addr, self.label)

	def assign_file(self, filename):
		self.filename = filename

	def print_info_name(self):
		print "{} {} {} {}".format(self.a_addr, self.c_addr, self.label, self.filename)

class obj:
	def __init__(self, instring):
		self.a_addr = hex(int(instring[2:8],16))
		self.nothing = False 	# True if no assembly code
		self.no_arg = False		# True if assembly operation has no arg
		tempstring = instring[32:].split()
		if len(tempstring) < 1:
			self.nothing = True
		elif len(tempstring) < 2:
			self.no_arg = True
			self.a_code = tempstring
		else:
			self.a_code = tempstring[:2]

	def print_info(self):
		if self.nothing:
			print "{}".format(self.a_addr)
		else:
			if self.no_arg:
				print "{} {}".format(self.a_addr, self.a_code)
			else:
				print "{} {} {}".format(self.a_addr, self.a_code[0], self.a_code[1])

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
printing_useful = False
if printing_useful:
	for i in dwarf_useful:
		print i
	for j in obj_useful:
		print j

# here parse dwarf dump infos
dwarf_objs = {}
original_order = []
for i in dwarf_useful:
	temp_obj = dwarf(i)
	if temp_obj.a_addr not in original_order:
		original_order.append(temp_obj.a_addr)
	if temp_obj.a_addr in dwarf_objs:
		dwarf_objs[temp_obj.a_addr].append(temp_obj)
	else:
		dwarf_objs[temp_obj.a_addr] = [temp_obj]

dwarf_order = sorted(dwarf_objs)

# here parse obj dump infors
obj_objs = {}
for i in obj_useful:
	temp_obj = obj(i)
	obj_objs[temp_obj.a_addr] = temp_obj

# here to print all objects to test info aquired
printing_objs = False
if printing_objs:
	for i in dwarf_order:
		print i
		for j in dwarf_objs[i]:
			j.print_info()
	print
	for i, j in dwarf_objs.iteritems():
		for k in j:
			k.print_info()
	print 
	for i, j in obj_objs.iteritems():
		print i
		j.print_info()

# here to get all the .c and .h files and there corresponding text
file_names = []
for i, j in dwarf_objs.iteritems():
	for k in j:
		if k.label:
			templist = re.findall(r".*\.[c|h]", k.label) # if it has a file name in label
			if templist:
				name = templist[0].split("/")[-1]
				k.assign_file(name)
				if name not in file_names:
					file_names.append(name)

# here get all texts of .c and .h files
texts = {}
for name in file_names:
	texts[name] = open(name, 'r').read().split("\n")

# test printing all file texts
printing_texts = False
if printing_texts:
	for i, j in texts.iteritems():
		print i
		for k in j:
			print k
			print

# here is block to assign file name to each dwarf object
tempname = None
for i in original_order:
	for k in dwarf_objs[i]:
		if k.filename:
			tempname = k.filename
		else:
			k.assign_file(tempname)

# here is to test if the file name label is correct
printing_file_names = True
if printing_file_names:
	for i in original_order:
		for k in dwarf_objs[i]:
			k.print_info_name()

# here is block to generate rows
temp_addr = None
for i in dwarf_order:
	for j in dwarf_objs[i]:
		c_codes = []
		a_codes = []


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