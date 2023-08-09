#!/usr/bin/env bash

# Set default values for the flags
TABLE_FORMAT=true
ASSIGNMENT=""
STUDENT_NAMES=()

# Parse the command line options
while getopts "a:t" opt; do
    case $opt in
        a)
            ASSIGNMENT="$OPTARG"
            ;;
        t)
            TABLE_FORMAT=false
            ;;
        \?)
            echo "Invalid option: -$OPTARG" >&2
            exit 1
            ;;
    esac
done

# Shift the positional parameters to remove the parsed options
shift $((OPTIND-1))

# Parse the student names from the remaining command line arguments
while [ $# -gt 0 ]; do
    STUDENT_NAMES+=("$1")
    shift
done

echo ${STUDENT_NAMES}

# Set the path of the grades.csv file
GRADES_FILE="../gradebook/grades.csv"

if [ "$TABLE_FORMAT" == "true" ]; then
    # Print all grades in table format
    column -s, -t < "$GRADES_FILE" | less -S
else
    # Check if an assignment argument was provided
    if [ -z "$ASSIGNMENT" ]; then
        # Print all student grades in alphabetical order if no assignment argument was provided.
        declare -A GRADES
        while IFS=, read -r STUDENT_NAME GRADE; do
            GRADES[$STUDENT_NAME]=$GRADE
        done < <(tail -n +2 "$GRADES_FILE")

        for STUDENT_NAME in "${!GRADES[@]}"; do
            echo "$STUDENT_NAME: ${GRADES[$STUDENT_NAME]}"
        done | sort

        exit 0 
    fi

    # Read the grades from the grades.csv file for a specific assignment
    declare -A GRADES
    while IFS=, read -r STUDENT_NAME GRADE; do
        GRADES[$STUDENT_NAME]=$GRADE
    done < <(tail -n +2 "$GRADES_FILE" | awk -F, -v assignment="$ASSIGNMENT" '{print $1 "," $assignment}')

    if [ ${#STUDENT_NAMES[@]} -eq 0 ]; then
        # Print all student grades in alphabetical order for a specific assignment if no student names were provided.
        for STUDENT_NAME in "${!GRADES[@]}"; do
            echo "$STUDENT_NAME: ${GRADES[$STUDENT_NAME]}"
        done | sort

        exit 0 
    fi

    # Print only specified student grades in alphabetical order for a specific assignment if student names were provided.
    for STUDENT_NAME in "${STUDENT_NAMES[@]}"; do 
        echo "$STUDENT_NAME: ${GRADES[$STUDENT_NAME]}"
    done | sort 
fi

