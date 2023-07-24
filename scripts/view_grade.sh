#!/usr/bin/env bash

# Check for the correct number of arguments
if [ $# -lt 1 ]; then
    echo "Usage: $0 [-a] [-t]"
    exit 1
fi

# Set default values for the flags
ALL_STUDENTS=false
TABLE_FORMAT=false

# Parse the command line options
while getopts "at" opt; do
    case $opt in
        a)
            ALL_STUDENTS=true
            ;;
        t)
            TABLE_FORMAT=true
            ;;
        \?)
            echo "Invalid option: -$OPTARG" >&2
            exit 1
            ;;
    esac
done

# Shift the positional parameters to remove the parsed options
shift $((OPTIND-1))

if [ "$TABLE_FORMAT" == "true" ]; then
    # Print all grades in table format
    column -s, -t < grades.csv | less -S
else
    # Read the grades from the grades.csv file for a specific assignment
    ASSIGNMENT="$1"
    declare -A GRADES
    while IFS=, read -r STUDENT_NAME GRADE; do
        GRADES[$STUDENT_NAME]=$GRADE
    done < <(tail -n +2 grades.csv | awk -F, -v assignment="$ASSIGNMENT" '{print $1 "," $assignment}')

    if [ "$ALL_STUDENTS" == "true" ]; then
        # Print all student grades in alphabetical order
        for STUDENT_NAME in "${!GRADES[@]}"; do
            echo "$STUDENT_NAME: ${GRADES[$STUDENT_NAME]}"
        done | sort
    else
        # Print only specified student grades in alphabetical order
        for STUDENT_NAME in "$@"; do
            echo "$STUDENT_NAME: ${GRADES[$STUDENT_NAME]}"
        done | sort
    fi
fi
