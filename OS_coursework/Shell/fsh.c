#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/wait.h>

static void run (char *line, size_t len, int* ret);

int main (int argc, char** argv) {

  int ret = 0;
  while (1) {
    fprintf(stdout, "CSE451Shell%% ");
    char *line = NULL;
    size_t len = 0; 
    if (getline(&line, &len, stdin) != -1) {   
      run(line, len, &ret);
    }
  }
}

static void run (char *line, size_t len, int* ret) {

  int wordcount = 0;
  len = strlen(line);
  char *wordarray[len];
  char linecpy[len + 1];

  strcpy(linecpy, line);
  if (linecpy[len - 1] == '\n') {
    linecpy[len - 1] = '\0';
  }

  wordarray[wordcount] = strtok(line, " \t\n");
  while (wordarray[wordcount] != NULL) {
    wordcount++;
    wordarray[wordcount] = strtok(NULL, " \t\n");
  }

  if (wordcount == 0) {
    free(line);
    return;
  }
    
  const char *cmd = wordarray[0];
  if(strcmp(cmd, "exit")== 0) {
    if (wordcount > 1) {
      *ret = atoi(wordarray[1]);
    } 
    free(line);
    exit(*ret);
  } else if (strcmp(cmd, "cd")==0) {
    if (wordcount > 1) {
      *ret = chdir(wordarray[1]);
    } else {
      *ret = chdir(getenv("HOME"));
    }
    if (*ret == -1) {
      perror(linecpy);
    }
  } else if (strcmp(cmd, ".")==0) {
    if (wordcount > 1) {
      FILE *f = fopen(wordarray[1], "r");
      if (f == NULL) {
	*ret = -1;
	perror(linecpy);
      } else {
	char *line2 = NULL;
	while (getline(&line2, &len, f) != -1) {
	  run(line2, len, ret);
	  line2 = NULL;
	}
	free(line2);
	fclose(f);
      }
    }
  } else {
    int pid;
    if ((pid = fork()) == 0 ) {
      execvp(cmd, wordarray);
      perror(linecpy);
      exit(*ret);
    }

    while (pid != wait(ret))
      ;
  }

  free(line);
}


