// parse.cpp
#include "cli.h"

CommandLineInterface::CommandLineInterface() {
  // Initialize Readline library
  rl_attempted_completion_function = completion;

  // Initialize commands
  commands["-m"] = std::make_unique<MultiGraderCommand>();
  commands["--multi-grader"] = std::make_unique<MultiGraderCommand>();
  commands["-d"] = std::make_unique<DirectoryCommand>();
  commands["--directory"] = std::make_unique<DirectoryCommand>();
}

void CommandLineInterface::run() {
  while (true) {
    // Use Readline to read user input with tab completion
    char *input = readline("> ");
    if (!input) {
      break;
    }
    std::string line(input);
    free(input);

    if (line.empty()) {
      continue;
    }
    if (line == "quit" || line == "exit") {
      break;
    }
    std::istringstream iss(line);
    std::string arg;
    if (iss >> arg) {
      auto it = commands.find(arg);
      if (it != commands.end()) {
        std::vector<std::string> args;
        while (iss >> arg) {
          args.push_back(arg);
        }
        it->second->execute(args);
      } else if (arg == "-h" || arg == "--help") {
        print_help();
      } else {
        std::cerr << "myprogram: invalid command\n";
        print_help();
      }
    }
  }
}

char **CommandLineInterface::completion(const char *text, int start, int end) {
  // Generate completion matches
  rl_attempted_completion_over = 1;
  return rl_completion_matches(text, generator);
}

char *CommandLineInterface::generator(const char *text, int state) {
  static int index;
  static std::vector<std::string> matches;

  if (state == 0) {
    index = 0;
    matches.clear();

    // Check if the user is entering the ASSIGNMENT argument
    if (rl_point > 0 && rl_line_buffer[rl_point - 1] == ' ') {
      // Generate a list of possible directory completion matches
      std::string path(text);
      std::size_t pos = path.find_last_of('/');
      if (pos != std::string::npos) {
        path = path.substr(0, pos + 1);
      } else {
        path.clear();
      }
      DIR *dir = opendir(path.empty() ? "." : path.c_str());
      if (dir) {
        struct dirent *entry;
        while ((entry = readdir(dir)) != nullptr) {
          if (entry->d_type == DT_DIR &&
              std::strncmp(text + path.size(), entry->d_name,
                           strlen(text) - path.size()) == 0) {
            matches.push_back(path + entry->d_name);
          }
        }
        closedir(dir);
      }
    } else {
      // Generate a list of possible command completion matches
      if (std::strncmp(text, "-m", strlen(text)) == 0) {
        matches.push_back("-m");
      }
      if (std::strncmp(text, "--multi-grader", strlen(text)) == 0) {
        matches.push_back("--multi-grader");
      }
      if (std::strncmp(text, "-d", strlen(text)) == 0) {
        matches.push_back("-d");
      }
      if (std::strncmp(text, "--directory", strlen(text)) == 0) {
        matches.push_back("--directory");
      }
    }
  }

  if (index < matches.size()) {
    return strdup(matches[index++].c_str());
  } else {
    return nullptr;
  }
}

void CommandLineInterface::print_help() const {
  std::cout << "Usage: myprogram [options]\n";
  std::cout << "\n";
  std::cout << "Options:\n";
  std::cout << "  -h, --help     Show this help message and exit\n";
  std::cout << "  -m, --multi-grader ASSIGNMENT\n";
  std::cout << "                 Grade the specified assignment using "
               "multi-grader\n";
  std::cout << "  -d, --directory ASSIGNMENT\n";
  std::cout << "                 Create a new assignment directory with "
               "the specified name\n";
}
