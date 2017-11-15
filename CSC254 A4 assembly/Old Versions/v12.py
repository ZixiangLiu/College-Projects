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
# V0.9 add c file range to ecah dwarf_obj
# V1.0 add rows!
# V1.1 fix additional row error
# V1.2 fix indentation bug
# V1.3 fix ET bug

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
        self.c_start = 0
        self.c_end = 0
        self.end_a_addr = False
        if len(instring) < 25:
            self.label = None
        else:
            self.label = instring[25:]
        if self.label == "ET":
            self.end_a_addr = True

    def print_info(self):
        print "{} {} {}".format(self.a_addr, self.c_addr, self.label)

    def assign_file(self, filename):
        self.filename = filename

    def print_info_name(self):
        print "{} {} {} {}".format(self.a_addr, self.c_addr, self.label, self.filename)

    def assign_c_line(self, start, end):
        self.c_start = start
        self.c_end = end

    def print_info_c_line(self):
        if self.end_a_addr:
            print "{} {} {} {} ET".format(self.a_addr, self.c_start, self.c_end, self.filename)
        else:
            print "{} {} {} {}".format(self.a_addr, self.c_start, self.c_end, self.filename)

class obj:
    def __init__(self, instring):
        self.a_addr = hex(int(instring[2:8],16))
        self.nothing = False    # True if no assembly code
        self.no_arg = False     # True if assembly operation has no arg
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

ETs = []
sorted_dwarf_objs = dwarf_objs.copy()
for i in range(len(dwarf_order)):
    if sorted_dwarf_objs[dwarf_order[i]][0].end_a_addr:
        # if sorted_dwarf_objs[dwarf_order[i]][0].c_addr == sorted_dwarf_objs[dwarf_order[i-1]][0].c_addr:
        ETs.append(dwarf_order[i])
        sorted_dwarf_objs[dwarf_order[i-1]].extend(sorted_dwarf_objs[dwarf_order[i]])

for i in ETs:
    dwarf_order.remove(i)
    sorted_dwarf_objs.pop(i)

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
        for j in sorted_dwarf_objs[i]:
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
printing_file_names = False
if printing_file_names:
    for i in original_order:
        for k in dwarf_objs[i]:
            k.print_info_name()

printing_file_names_sorted = False
if printing_file_names_sorted:
    for i in dwarf_order:
        for k in sorted_dwarf_objs[i]:
            k.print_info_name()

# here sort dwarf_objs in c line number order
# groups is dictionary of file name key, dictionary(c_address key, object values) values
groups = {}
for i, j in dwarf_objs.iteritems():
    for k in j:
        if k.filename in groups:
            if k.c_addr in groups[k.filename]:
                groups[k.filename][k.c_addr].append(k)
            else:
                groups[k.filename][k.c_addr] = [k]
        else:
            groups[k.filename] = {}
            groups[k.filename][k.c_addr] = [k]

for i, j in groups.iteritems():
    file_start = 0
    file_end = len(texts[i])-1
    c_order = sorted(j)

    for k in range(len(c_order)):
        if k == 0:
            for l in j[c_order[k]]:
                l.assign_c_line(file_start, c_order[k])
        else:
            for l in j[c_order[k]]:
                l.assign_c_line(c_order[k-1]+1, c_order[k])

# here is testing the assigning of c lines are correct
printing_c_line = False
if printing_c_line:
    for i, j in dwarf_objs.iteritems():
        for k in j:
            k.print_info_c_line()
    print 
    for i in dwarf_order:
        for j in sorted_dwarf_objs[i]:
            j.print_info_c_line()

# here is block to generate rows
debugging_rows = False

temp_addr = None
rows = []
for i in range(len(dwarf_order)):
    c_codes = []
    a_codes = []
    c_start = []
    c_end = []
    if i < len(dwarf_order)-1:
        addr_end = dwarf_order[i+1]
    else:
        addr_end = 0
    filename = sorted_dwarf_objs[dwarf_order[i]][0].filename
    for j in sorted_dwarf_objs[dwarf_order[i]]:
        c_start.append(j.c_start)
        c_end.append(j.c_end)
        if j.end_a_addr:
            addr_end = j.a_addr
    for j in range(min(c_start), max(c_end)+1):
        c_codes.append(texts[filename][j-1])
    for j in range(int(dwarf_order[i], 16), int(addr_end, 16)):
        l = hex(j)
        if l in obj_objs:
            if not obj_objs[l].nothing:
                if obj_objs[l].no_arg:
                    a_codes.append(obj_objs[l].a_code[0])
                else:
                    a_codes.append("{} {}".format(obj_objs[l].a_code[0], obj_objs[l].a_code[1]))
    rows.append(row("", c_codes, a_codes))
    if debugging_rows:
        print "a_addr is {}".format(dwarf_order[i])
        rows[-1].print_row()

        




# block to write html file
writing_file = True
if writing_file:
    html_file = open('assembly.html', 'w')

    # add header and footer into html file
    header = open('header', 'r')
    footer = open('footer', 'r')
    html_file.write(header.read())
    header.close()

    # rows = []

    # # small demo
    # rows.append(row("", ["Code1", "Code2"], ["Assm1"]))
    # rows.append(row("tag1", ["Code3", "Code4"], ["Assm2", "Assm3", "Assm4"]))

    # add row elements into html file
    for row in rows:
        html_file.write("\t\t<tr>\n\t\t\t<td>\n")
        for code in row.column1:
            html_file.write("\t\t\t\t" + code.replace(' ', "&nbsp;") + "<br>\n")
        html_file.write("\t\t\t</td>\n\t\t\t<td>\n")
        for assm in row.column2:
            html_file.write("\t\t\t\t" + assm.replace(' ', "&nbsp;") + "<br>\n")
        html_file.write("\t\t\t</td>\n\t\t</tr>\n")


    html_file.write(footer.read())
    footer.close()
    html_file.close()