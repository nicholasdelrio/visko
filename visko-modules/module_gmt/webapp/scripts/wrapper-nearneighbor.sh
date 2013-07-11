#!/bin/bash

# Script to generate gridded data
# TARGET MODULE: nearneighbor
# INPUT DATA: Tabular space delimited ASCII data
# Author: Nicholas Del Rio 2012

# Arguments: <<infile>> <<outfile>> <I>> <<S>> <<region>>

# Check arguments
if [ $# -ne 5 ] ; then
    echo 1>&2 "USAGE:  <<infile>> <<outfile>> <I>> <<S>> <<region>>"
    exit 127
fi

# set variables
infile=$1
outfile=$2
gridspacing=$3
searchradius=$4
region=$5

nearneighbor -R$region -I$gridspacing -S$searchradius -G$outfile $infile