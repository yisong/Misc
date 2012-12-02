// -*- mode:C; tab-width:2; indent-tabs-mode:nil;  -*-
// $Id: launchpadData.h,v 1.1 2011/10/23 20:10:29 zahorjan Exp $

#ifndef LAUNCHPADDDATA_H
#define LAUNCHPADDATA_H

typedef struct {
  char*  buttonLabel;
  char*  startDirectory;
  char*  cmd;
  char*  args[];
} launchpad_data_t;

extern int getNButtons();
extern launchpad_data_t** getButtons();  // Returns a pointer to an(the) array of button descriptors.
                                         // (That array has size getNButtons().)

#endif // LAUNCHPADDATA_H
