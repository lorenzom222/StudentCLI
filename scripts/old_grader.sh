#!/usr/bin/env bash

# Helper function to print a student's grade to the grades.csv file
print_grade() {
    # Check if the grades.csv file exists
    if [ ! -f grades.csv ]; then
        # Create the grades.csv file with a header row
        echo "Student Name,${ASSIGNMENT}" > grades.csv
    fi

    # Check if the student already has a row in the grades.csv file
    if grep -q "^${STUDENT_NAME}," grades.csv; then
        # Update the student's grade for this assignment
        sed -i '' "s|^${STUDENT_NAME},.*|${STUDENT_NAME},${GRADE}|" grades.csv
        echo "Updated ${STUDENT_NAME}'s grade for ${ASSIGNMENT} to ${GRADE}"
    else
        # Append the student's grade to the grades.csv file
        echo "${STUDENT_NAME},${GRADE}" >> grades.csv
        echo "Appended ${STUDENT_NAME}'s grade for ${ASSIGNMENT} as ${GRADE}"
    fi
}



# Helper function to copy output files to the student's folder
copy_output_files() {
    # Copy all output files to the student's folder.
    mkdir -p "${RUNS}/${STUDENT_NAME}"
    cp "$LOG" "$REPORT" "${RUNS}/${STUDENT_NAME}"
}

# Helper function to delete temporary files
delete_temp_files() {
    # delete the original files and copied student files.
    rm "$LOG" "$REPORT" *.java *.class
}

# Helper function to log a message with a timestamp
log() {
    TIMESTAMP=$(date +"%Y-%m-%d %H:%M:%S")
    echo "[$TIMESTAMP] $@" >> grader.log
}

# Helper function to grade a single student
grade() {
    STUDENT_NAME="$1"

    log "Grading student $STUDENT_NAME for assignment $ASSIGNMENT"

    ZIPFILE="${ASSIGNMENT}/${STUDENT_NAME}-${ASSIGNMENT}.zip"

    # Unzip the student's files into their assignment folder
    unzip -o $ZIPFILE -d ${ASSIGNMENT}/${STUDENT_NAME}${ASSIGNMENT}

    # Copy student's files from their assignment folder to the root directory
    cp ${ASSIGNMENT}/${STUDENT_NAME}${ASSIGNMENT}/* .

    # Get the name of the Java file from the copied files
    STUCLASS=$(basename $(ls *.java) .java)

    mkdir -p $STUDENT

    LOG="LOG.txt"
    REPORT="REPORT.txt"
    CASES="CASES.txt"
    ALLPASSES=0
    ALLTOTAL=0

    echo "" > $LOG
    echo "" > $REPORT
    echo "" > $CASES

    # Compile their program.
    javac ${STUCLASS}.java

    if [ $? -ne 0 ]; then
        log "Compilation failed for student $STUDENT_NAME"
        echo "COMPILATION FAILED.  ABORTING.";
        exit -1;
    fi;

    for CPATH in $INPUTS/*; do
        PASSES=0
        TOTAL=0
        CATEGORY=$(basename $CPATH)
        #CAUTION -- change these variables at your peril!
        # The next two lines ensure the student directory exists and is empty.
        rm -rf $STUDENT/$CATEGORY
        mkdir -p $STUDENT/$CATEGORY

        for TEST in $CPATH/*; do
            NAME=$(basename $TEST .txt)
            cat $TEST | timeout 10s java $STUCLASS > ${STUDENT}/$CATEGORY/${NAME}_out.txt
            if [ $? -ne 0 ]; then # if timeout then fail and exit script
                log "Test $NAME timed out for student $STUDENT_NAME in category $CATEGORY"
                echo "$CATEGORY test $NAME timeout.";
                MSG="$CATEGORY test $NAME failed."
                echo $MSG >>$CASES            
                echo "$PASSES / $TOTAL tests passed in CATEGORY $CATEGORY" >>$REPORT
                ALLPASSES=$((ALLPASSES + PASSES))
                ALLTOTAL=$((ALLTOTAL + TOTAL))
                echo "$((ALLPASSES * 2)) / $((ALLTOTAL * 2)) points." >>$REPORT
                
                copy_output_files
                GRADE=$(tail -n 1 $REPORT) # Get student's grade from last line of REPORT file.
                print_grade

                delete_temp_files
                
                exit -1;
            else # else do the comparision
                # Compare only unique numeric values in the two files within a certain range of the target number and format the numbers to have two decimal places
                # Eliminates: Irrelevant Strings, Rounding mismatch, and Trailing 0s
                diff <(awk '$1 >= 5 &&$1 <= 15 {printf "%.2f\n", $1}' <(grep -oE '[0-9]+(\.[0-9]+)?' $TARGETS/$CATEGORY/${NAME}_out.txt | sort -n | uniq)) <(awk '$1 >= 5 &&$1 <= 15 {printf "%.2f\n", $1}' <(grep -oE '[0-9]+(\.[0-9]+)?' ${STUDENT}/$CATEGORY/${NAME}_out.txt | sort -n | uniq)) 2>>$LOG
                if [ $? -eq 0 ]; then
                    PASSES=$((PASSES + 1))
                    MSG="$CATEGORY test $NAME passed."
                else
                    log "Test $NAME failed for student $STUDENT_NAME in category $CATEGORY"
                    MSG="$CATEGORY test $NAME failed."
                fi
            fi
            echo $MSG
            echo $MSG >>$CASES            
            TOTAL=$((TOTAL + 1))
        done

        echo "$PASSES / $TOTAL tests passed in CATEGORY $CATEGORY" >>$REPORT
        ALLPASSES=$((ALLPASSES + PASSES))
        ALLTOTAL=$((ALLTOTAL + TOTAL))
    done

    echo "$((ALLPASSES * 2)) / $((ALLTOTAL * 2)) points." >>$REPORT
    
    copy_output_files
    GRADE=$(tail -n 1 $REPORT) # Get student's grade from last line of REPORT file.

    print_grade

    delete_temp_files
}

# Check for the correct number of arguments
if [ $# -lt 2 ]; then
    echo "Usage: $0 ASSIGNMENT [STUDENT]..."
    exit 1
fi

# Set the variables from the arguments
ASSIGNMENT=$(pwd)
shift

# Check if the -a flag is present
ALL_STUDENTS=false
if [ "$1" == "-a" ]; then
    ALL_STUDENTS=true
    shift
fi

ROOT="testCases";
INPUTS=${ROOT}/inputs
TARGETS=${ROOT}/targets
STUDENT=${ROOT}/students
RUNS=${ROOT}/runs

if [ "$ALL_STUDENTS" == "true" ]; then
    # Print the list of student zip files in alphabetical order
    echo "List of students to be graded in alphabetical order:"
    ls ${ASSIGNMENT}/*.zip | sort | xargs -n 1 basename | cut -d '-' -f 1

    # Process all student zip files in the assignment directory in alphabetical order
    for ZIPFILE in $(ls ${ASSIGNMENT}/*.zip | sort); do
        # Get the student name from the zip file name
        STUDENT_NAME=$(basename ${ZIPFILE%.zip} | cut -d '-' -f 1)

        grade $STUDENT_NAME
    done
else
    # Process only specified student zip files in alphabetical order
    for STUDENT_NAME in "$@"; do
        grade $STUDENT_NAME
    done
fi
