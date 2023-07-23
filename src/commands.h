// commands.h
#ifndef COMMAND_H
#define COMMAND_H

#include <cstdlib> // for system()
#include <iostream>
#include <string>
#include <sys/stat.h>
#include <vector>

class Command {
public:
  virtual ~Command() {}
  virtual void execute(const std::vector<std::string> &args) const = 0;
};

class MultiGraderCommand : public Command {
public:
  void execute(const std::vector<std::string> &args) const override;
};

class DirectoryCommand : public Command {
public:
  void execute(const std::vector<std::string> &args) const override;
};

#endif
