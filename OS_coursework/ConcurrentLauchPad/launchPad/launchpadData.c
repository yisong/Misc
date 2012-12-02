// -*- mode:C; tab-width:2; indent-tabs-mode:nil;  -*-
// $Id: launchpadData.c,v 1.1 2011/10/23 20:10:29 zahorjan Exp $

#include <stdlib.h>

#include "launchpadData.h"

// This not-perfectly-convenient method of providing the button data
// is forced upon us by the limitations of the C compiler.
// (NOTE: This synatx uses what GNU says is a GNU extension to C.  It might not work
//        with non-GNU compilers.}

static launchpad_data_t emacsButtonData = 
  { "Edit",              // displayed button text
    NULL,                // working directory for cmd (NULL means whatever directory the launchpad execution is in)
    "emacs",             // executable name
    {"emacs", NULL}      // NULL-terminated argument array, suitable for passing to exec() (arg 0 is the executable name)
  };
static launchpad_data_t firefoxButtonData =
  { "Browse",
    NULL,
    "firefox",
    {"firefox", "www.cs.washington.edu", NULL}
  };
static launchpad_data_t makeButtonData =
  { "Build",
    "/home/zahorjan/cse451/10sp/projectEvents/launchPad",
    "make",
    {"make", NULL}
  };
static launchpad_data_t lsButtonData =
  { "Home",
    "/home/zahorjan",
    "ls",
    {"ls", NULL}
  };

#define NBUTTONS 4

launchpad_data_t* buttons[NBUTTONS] = { &emacsButtonData,
                                        &firefoxButtonData,
                                        &makeButtonData,
                                        &lsButtonData
                                      };

int getNButtons() {
  return NBUTTONS;
}

launchpad_data_t** getButtons() {
  return buttons;
}


