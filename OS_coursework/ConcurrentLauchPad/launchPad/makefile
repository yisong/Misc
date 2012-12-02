CC= gcc
MKDEP= gccmakedep

VARDIR= ../var
SRCS= testgtk.c launchpad.c launchpadData.c
OBJS= $(patsubst %.c,%.o,$(SRCS))
PROGRAMS=testgtk launchpad

CFLAGS= -Wall -O0  -g -I$(VARDIR)/include ${USERFLAGS} `pkg-config --cflags --libs glib-2.0` `pkg-config --cflags gtk+-2.0` 
LDFLAGS = -L$(VARDIR)/lib -lpthread -lrt  `pkg-config --libs gtk+-2.0` `pkg-config --libs gthread-2.0`

SRCS= launchpad.c launchpadData.c testgtk.c

launchpad: launchpad.o launchpadData.o
	gcc $(CFLAGS) launchpad.c launchpadData.c -o launchpad $(LDFLAGS) -lcse451p2 

testgtk: testgtk.o
	gcc $(CFLAGS) testgtk.c -o testgtk $(LDFLAGS)

%.o : %.c
	$(CC) $(CFLAGS) -c $<

%.d: %.c
	@set -e; rm -f $@; \
	$(CC) -MM $(CFLAGS) $< > $@.$$$$; \
	sed 's,\($*\)\.o[ :]*,\1.o $@ : ,g' < $@.$$$$ > $@; \
	rm -f $@.$$$$

include $(SRCS:.c=.d)

.PHONY: all clean cleanall depend
all:	testgtk launchpad

clean:
	@rm -f ${OBJS} ${PROGRAMS} testqueue testtaskqueue *.d

cleanall: clean # depend

depend:
	@${MKDEP} -- ${CFLAGS} -- ${SRCS}
# DO NOT DELETE
