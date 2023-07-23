#include <cstdlib> // for system()
#include <cstring> // for strcmp()
#include <iostream> // for std::cin, std::cout
#include <sstream> // for std::istringstream
#include <string> // for std::string, std::getline

class CommandLineInterface {
public:
    void run() {
        while (true) {
            std::cout << "> ";
            std::string line;
            if (!std::getline(std::cin, line)) {
                break;
            }
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
            } else {
                std::cerr << "myprogram: invalid command\n";
                print_help();
            }
        }
    }

private:
    void print_help() const {
        std::cout << "Usage: myprogram [options]\n";
        std::cout << "\n";
        std::cout << "Options:\n";
        std::cout << "  -h, --help     Show this help message and exit\n";
        std::cout << "  -m, --multi-grader ASSIGNMENT\n";
        std::cout << "                 Grade the specified assignment using multi-grader\n";
    }

    void run_multi_grader(const std::string& assignment) const {
        std::string command = "./multi-grader.sh ";
        command += assignment;
        system(command.c_str());
    }

    bool parse_command_line(const std::string& line, std::string& command, std::string& assignment) const {
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
