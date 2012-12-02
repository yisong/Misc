import sys, re
from random import *

f = open(sys.argv[2])
lines = f.readlines()
f.close()

n = len(lines)
count = 0
total = int(sys.argv[1])

output = open("out-domain.subset", "w")
while count < total:
    s = lines[randint(0, n-1)]
    count += len(re.findall(r'\n| ', s))
    output.write(s)
output.close()
