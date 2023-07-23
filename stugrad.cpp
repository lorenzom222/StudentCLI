#include <cstdlib>  // for system()
#include <cstring>  // for strcmp()
#include <dirent.h> // for opendir(), readdir(), closedir()
#include <iostream> // for std::cin, std::cout
#include <readline/history.h>
#include <readline/readline.h>
#include <sstream> // for std::istringstream
#include <string>  // for std::string, std::getline
#include <sys/stat.h>
#include <vector>
// g++ stugrad.cpp -o stugrad -lreadline

class CommandLineInterface {
public:
  CommandLineInterface() {
    // Initialize Readline library
    rl_attempted_completion_function = completion;
  }

  void run() {
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
      std::string command;
      std::string assignment;
      if (!parse_command_line(line, command, assignment)) {
        continue;
      }
      if (command == "-m" || command == "--multi-grader") {
        run_multi_grader(assignment);
      } else if (command == "-d" || command == "--directory") {
        create_directory(assignment);
      } else {
        std::cerr << "myprogram: invalid command\n";
        print_help();
      }
    }
  }

private:
  static char **completion(const char *text, int start, int end) {
    // Generate completion matches
    rl_attempted_completion_over = 1;
    return rl_completion_matches(text, generator);
  }

  static char *generator(const char *text, int state) {
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

  void print_help() const {
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

  void run_multi_grader(const std::string &assignment) const {
    std::string command = "./multi-grader.sh ";
    command += assignment;
    system(command.c_str());
  }

  void create_directory(const std::string &name) const {
    // Create Assignments directory if it doesn't exist
    mkdir("Assignments", 0755);

    // Create new assignment directory
    std::string path = "Assignments/" + name;
    mkdir(path.c_str(), 0755);
  }

  bool parse_command_line(const std::string &line, std::string &command,
                          std::string &assignment) const {
    command.clear();
    assignment.clear();
    std::istringstream iss(line);
    std::string arg;
    while (iss >> arg) {
      if (arg == "-h" || arg == "--help") {
        print_help();
        return false;
      } else if (arg == "-m" || arg == "--multi-grader") {
        command = arg;
        if (iss >> arg) {
          assignment = arg;
        } else {
          std::cerr << "myprogram: option requires an argument -- 'm'\n";
          return false;
        }
      } else if (arg == "-d" || arg == "--directory") {
        command = arg;
        if (iss >> arg) {
          assignment = arg;
        } else {
          std::cerr << "myprogram: option requires an argument -- 'd'\n";
          return false;
        }
      } else {
        std::cerr << "myprogram: unrecognized option '" << arg << "'\n";
        return false;
      }
    }
    return true;
  }
};

int main() {
  CommandLineInterface cli;
  cli.run();
  return 0;
}
