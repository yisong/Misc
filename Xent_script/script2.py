import sys

inf = open('in-domain.result')
outf = open('out-domain.result')
output = open('output.raw', 'w')
paraf = open(sys.argv[1])
paraoutput = open('output.parallel.raw', 'w')

i = 0

while 1:
    line = inf.readline()
    inf.readline()
    inline = inf.readline()
    outf.readline()
    outf.readline()
    outline = outf.readline()
    if not inline:
        break
    pass
    a = (float)(inline.split()[5])
    b = (float)(outline.split()[5])
    inf.readline()
    outf.readline()
    output.write((str)(a - b) + " " + (str)(i) + " " + line)
    paraoutput.write((str)(a - b) + " " + (str)(i) + " " + paraf.readline())
    i+=1

inf.close()
outf.close()
output.close()
paraf.close()
paraoutput.close()
