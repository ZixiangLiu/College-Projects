'''
CSC254
Yukun Chen
A1
Partner: Zixiang Liu
'''

import sys
  
def combinations(k, n):		# combinations of k, n
	result = []
	if k == 1:				# when k==1, put n in result set
		for i in n:
			result.append([i])
	else:
		for i in range(len(n)):		# loop for all values in n
			for j in combinations(k-1, n[i+1:]):	# put [n, combination of index after n] in result set
				temp = [n[i]]
				temp.extend(j)
				result.append(temp)
	return result

k = int(sys.argv[1])		# get parameters from standard input
n = int(sys.argv[2])

if k>n:			# k shall be smaller or equal to n
	print("k shall not be larger than n")
else:
	items = [x for x in range(1,n+1)]	# create the array for n
	for n in combinations(k, items):	# call combination function
		print(n)
