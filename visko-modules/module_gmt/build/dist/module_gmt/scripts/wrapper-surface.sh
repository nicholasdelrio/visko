#!/bin/bash

# Script to generate gridded data
# TARGET MODULE: surface
# INPUT DATA: Tabular space delimited ASCII data
# Author: Nicholas Del Rio 2012

# Arguments: <<infile>> <<outfile>> <I>> <<T>> <<C>> <<R>>

# Check arguments
if [ $# -ne 6 ] ; then
    echo 1>&2 "USAGE:  <<infile>> <<outfile>> <I>> <<T>> <<C>> <<R>>"
    exit 127
fi

# set variables
infile=$1
outfile=$2
gridspacing=$3
tension=$4
convergenceLimit=$5
region=$6
tmpfile=${infile}.tmp

blockmean $infile -R$region -I$gridspacing > $tmpfile
surface -R$region -I$gridspacing -G$outfile -T$tension -C$convergenceLimit $tmpfile

# remove temp file
rm $tmpfile