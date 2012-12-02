CC= gcc
CFLAGS= -Wall -O  -g
SRCS= e2undel.c
OBJS= e2undel.o
PROGRAM= e2undel
MKDEP= gccmakedep

${PROGRAM}:	${OBJS}
	${CC} ${CFLAGS} ${OBJS} -o ${PROGRAM}

%.o : %.c
	$(CC) $(CFLAGS) -c $<

clean:
	rm -f ${OBJS} ${PROGRAM}

depend:
	${MKDEP} ${CFLAGS} ${SRCS}
