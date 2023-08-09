cmake_minimum_required(VERSION 3.10)

# Set the project name and version
project(StudentCLI VERSION 1.0)

# Specify the C++ standard
set(CMAKE_CXX_STANDARD 14)
set(CMAKE_CXX_STANDARD_REQUIRED True)

# Add an executable
add_executable(stg src/main.cpp src/commands.cpp src/cli.cpp)

# Set the output directory for the executable
set_target_properties(stg PROPERTIES
  RUNTIME_OUTPUT_DIRECTORY "${CMAKE_CURRENT_SOURCE_DIR}/grader_dir"
)

# Link with the Readline library
target_link_libraries(stg readline)
