import sys, re
total = int(sys.argv[1])

for i in range(10, 100, 10):
    f = open('output.result')
    pf = open('output.parallel.result')
    output = open('xent-' + str(i)+'p.ara', 'w')
    poutput = open('xent-' + str(i)+'p.en', 'w')
    limit = total * i / 100
    count = 0

    while count < limit:
        line = f.readline()
        pline = pf.readline()
        count += len(re.findall(r'\n| ', line))
        output.write(line)
        poutput.write(pline)
    output.close()
    poutput.close()
    f.close()
    pf.close()
