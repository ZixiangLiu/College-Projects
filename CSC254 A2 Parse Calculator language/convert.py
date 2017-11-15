f = open('test.txt', 'r')
lines = f.readlines()
mystr = '\t'.join([line.strip() for line in lines])
print(mystr)