// cli.h
#ifndef CLI_H
#define CLI_H

#include "commands.h"
#include <cstring> // for strcmp()
#include <dirent.h>
#include <map>
#include <memory>
#include <readline/history.h>
#include <readline/readline.h>
#include <sstream>
#include <string>

class CommandLineInterface {
public:
  CommandLineInterface();
  void run();

private:
  static char **completion(const char *text, int start, int end);
  static char *generator(const char *text, int state);
  void print_help() const;

private:
  std::map<std::string, std::unique_ptr<Command>> commands;
};

#endif
