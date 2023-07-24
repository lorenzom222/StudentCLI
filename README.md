# StudentCLI

StudentCLI is a command-line interface for managing student assignments.

## Building

To build StudentCLI, you'll need to have CMake and a C++ compiler installed on your system.

1. Open a terminal or command prompt and navigate to the directory containing the source code.

2. Create a new directory to hold the build files:

```
mkdir build
cd build
```

3. Run CMake to generate the build system files:

```
cmake …
```

4. Build the project using the generated build system:
```
make
```


After these steps, you should have an executable named `stg` in the `build/gradebook` directory (or in the directory specified by the `STG_OUTPUT_DIRECTORY` variable).

## Usage

To use StudentCLI, run the `stg` executable from the command line. You can use the `-m` or `--multi-grader` option to grade an assignment using the multi-grader script, or the `-d` or `--directory` option to create a new assignment directory.

For example, to grade an assignment named `assignment1`, you can run the following command:

```
./stg -m assignment1

```

To create a new assignment directory named `assignment2`, you can run the following command:

```
./stg -d assignment2
```


For more information, run `stg` with the `-h` or `--help` option to display a help message.
