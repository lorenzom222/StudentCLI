#!/bin/bash

# Check for the correct number of arguments
if [ $# -ne 1 ]; then
    echo "Usage: $0 ASSIGNMENT"
    exit 1
fi

# Set the variables from the arguments
ASSIGNMENT="$1"

ROOT="testCases";
INPUTS=${ROOT}/inputs
TARGETS=${ROOT}/targets
STUDENT=${ROOT}/students
RUNS=${ROOT}/runs

# Print the list of student zip files in alphabetical order
echo "List of students to be graded in alphabetical order:"
ls ${ASSIGNMENT}/*.zip | sort | xargs -n 1 basename | cut -d '-' -f 1

# Process all student zip files in the assignment directory in alphabetical order
for ZIPFILE in $(ls ${ASSIGNMENT}/*.zip | sort); do
    # Get the student name from the zip file name
    STUDENT_NAME=$(basename ${ZIPFILE%.zip} | cut -d '-' -f 1)

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
                echo "$CATEGORY test $NAME timeout.";
                MSG="$CATEGORY test $NAME failed."
                echo $MSG >> $CASES            
                echo "$PASSES / $TOTAL tests passed in CATEGORY $CATEGORY" >> $REPORT
                ALLPASSES=$((ALLPASSES + PASSES))
                ALLTOTAL=$((ALLTOTAL + TOTAL))
                echo "$((ALLPASSES * 2)) / $((ALLTOTAL * 2)) points." >> $REPORT
                
                # Copy all output files to the student's folder.
                mkdir -p "${RUNS}/${STUDENT_NAME}"
                cp "$LOG" "$REPORT" "${RUNS}/${STUDENT_NAME}"

                # Append student's grade to grade.txt file.
                GRADE=$(tail -n 1 $REPORT) # Get student's grade from last line of REPORT file.
                echo "$STUDENT_NAME: $GRADE" >> grade.txt # Append student's name and grade to grade.txt file.

                # delete the original files and copied student files.
                rm "$LOG" "$REPORT" *.java *.class
                
                exit -1;
            else # else do the comparision
                # Compare only unique numeric values in the two files within a certain range of the target number and format the numbers to have two decimal places
                # Eliminates: Irrelevant Strings, Rounding mismatch, and Trailing 0s
                diff <(awk '$1 >= 5 && $1 <= 15 {printf "%.2f\n", $1}' <(grep -oE '[0-9]+(\.[0-9]+)?' $TARGETS/$CATEGORY/${NAME}_out.txt | sort -n | uniq)) <(awk '$1 >= 5 && $1 <= 15 {printf "%.2f\n", $1}' <(grep -oE '[0-9]+(\.[0-9]+)?' ${STUDENT}/$CATEGORY/${NAME}_out.txt | sort -n | uniq)) 2>> $LOG
                if [ $? -eq 0 ]; then
                    PASSES=$((PASSES + 1))
                    MSG="$CATEGORY test $NAME passed."
                else
                    MSG="$CATEGORY test $NAME failed."
                fi
            fi
            echo $MSG
            echo $MSG >> $CASES            
            TOTAL=$((TOTAL + 1))
        done
        echo "$PASSES / $TOTAL tests passed in CATEGORY $CATEGORY" >> $REPORT
        ALLPASSES=$((ALLPASSES + PASSES))
        ALLTOTAL=$((ALLTOTAL + TOTAL))
    done
    echo "$((ALLPASSES * 2)) / $((ALLTOTAL * 2)) points." >> $REPORT
    cat $REPORT

    # Copy all output files to the student's folder.
    mkdir -p "${RUNS}/${STUDENT_NAME}"
    cp "$LOG" "$REPORT" "${RUNS}/${STUDENT_NAME}"

    # Append student's grade to grade.txt file.
    GRADE=$(tail -n 1 $REPORT) # Get student's grade from last line of REPORT file.
    echo "$STUDENT_NAME: $GRADE" >> grade.txt # Append student's name and grade to grade.txt file.

    # delete the original files and copied student files.
    rm "$LOG" "$REPORT" *.java *.class
done

