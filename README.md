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


After these steps, you should have an executable named stg in the gradebook directory within the current source directory.

## Interface

- Navigate the *grader* directory and run *stg*
```
cd grader
./stg
```

- This will activate the grading inferace where you can prompt the built in commands for grading:

```
(base) lorenzomendoza@Lorenzos-Air grader_dir % ./stg
> --view-grades -t -a Homework4 
Homework4
Calvin: 48 / 60 points.
Robert: 60 / 60 points.
> --grader 
.  ..
> --grader Homework4
Usage: ../scripts/grader.sh ASSIGNMENT [STUDENT]...
> --grader Homework4 -A
Grading Calvin's Homework4...
allProfit test 1 passed.
allProfit test 2 passed.
allProfit test 3 passed.
allProfit test 4 passed.
allProfit test 5 passed.
massDecline test 1 passed.
massDecline test 2 passed.
massDecline test 3 passed.
...
```

## Command

- --grader or -g
- -- view_grades or -d

  
