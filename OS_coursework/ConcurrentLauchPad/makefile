# $Id: makefile.bak,v 1.3 2011/10/23 23:19:23 zahorjan Exp $

CC= gcc
MKDEP= gccmakedep

VARDIR= var
SRCS= testqueue.c testtaskqueue.c testeventmanager.c factorydemo.c factorytypeerr.c
OBJS= $(patsubst %.c,%.o,$(SRCS))
PROGRAMS=$(patsubst %.c,%,$(SRCS))

CFLAGS= -c -Wall -O0 -g  -I$(VARDIR)/include ${USERFLAGS}
LDFLAGS = -L $(VARDIR)/lib -lcse451p2 -lpthread -lrt


LIBSRCS= $(wildcard $(VARDIR)/src/*.c)
LIBOBJS= $(patsubst %.c,%.o,$(LIBSRCS))

LIBNAME= cse451p2
LIBFILENAME=$(VARDIR)/lib/lib$(LIBNAME).a

testqueue: testqueue.o $(LIBFILENAME)
	${CC} testqueue.o -o testqueue ${LDFLAGS}

testhash: testHash.o $(LIBFILENAME)
	${CC} testHash.o -o testhash ${LDFLAGS} 

testtaskqueue: testtaskqueue.o $(LIBFILENAME)
	${CC} testtaskqueue.o -o testtaskqueue ${LDFLAGS}

testeventmanager: testeventmanager.o $(LIBFILENAME)
	${CC} testeventmanager.o -o testeventmanager ${LDFLAGS}

factorydemo: factorydemo.o $(LIBFILENAME)
	${CC} factorydemo.o -o factorydemo ${LDFLAGS}

factorytypeerr: factorytypeerr.o $(LIBFILENAME)
	${CC} factorytypeerr.o -o factorytypeerr ${LDFLAGS}

.PHONY: lib
lib: $(LIBFILENAME)

VPATH = .:$(VARDIR)/src

$(LIBFILENAME): $(LIBOBJS)
	@rm -f $(LIBFILENAME)
	ar r $(LIBFILENAME) $^

%.d: %.c
	@set -e; rm -f $@; \
	$(CC) -MM $(CFLAGS) $< > $@.$$$$; \
	sed 's,\($*\)\.o[ :]*,\1.o $@ : ,g' < $@.$$$$ > $@; \
	rm -f $@.$$$$

include $(SRCS:.c=.d)

%.o : %.c
	$(CC) $(CFLAGS) -c $< -o $@

.PHONY: all clean cleanlib cleanall depend

all:	lib testqueue testtaskqueue testeventmanager factorydemo factorytypeerr

clean:
	@rm -f ${OBJS} ${PROGRAMS} *~ *.d

cleanlib:
	@rm -f ${LIBOBJS} $(LIBFILENAME) $(VARLIB)/src/*~ $(VARLIB)/include/*~

cleanall: clean cleanlib # depend

depend:
	@${MKDEP} -- ${CFLAGS} -- ${SRCS} ${LIBSRCS}
# DO NOT DELETE
