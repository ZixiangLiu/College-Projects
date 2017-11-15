class row:
	def __init__(self, row_name, column1, column2):
		# row_name works as an identifier of the current row
		# 		can be a choose between int & String
		# column1 & column2 are list of lines that should fit into two rows
		self.row_name = row_name
		self.column1 = column1
		self.column2 = column2

	def print_row(self):
		print "New Row with name: {}".format(self.row_name)
		maxi = max(len(self.column1), len(self.column2))
		for i in range(maxi):
			print "{:<60s} {}".format(self.column1[i] if i < len(self.column1) else " ", self.column2[i] if i < len(self.column2) else " ")