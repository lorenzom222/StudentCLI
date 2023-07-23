// command.cpp
#include "commands.h"

void MultiGraderCommand::execute(const std::vector<std::string> &args) const {
  if (args.size() != 1) {
    std::cerr << "myprogram: option requires an argument -- 'm'\n";
    return;
  }
  std::string command = "./multi-grader.sh ";
  command += args[0];
  system(command.c_str());
}

void DirectoryCommand::execute(const std::vector<std::string> &args) const {
  if (args.size() != 1) {
    std::cerr << "myprogram: option requires an argument -- 'd'\n";
    return;
  }
  // Create Assignments directory if it doesn't exist
  mkdir("Assignments", 0755);

  // Create new assignment directory
  std::string path = "Assignments/" + args[0];
  mkdir(path.c_str(), 0755);
}
