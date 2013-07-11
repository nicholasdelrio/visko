#!/bin/bash

# Script to produce plot of 2D data
# TARGET MODULE: psxy
# INPUT DATA: Tabular space delimited ASCII data
# Author: Nicholas Del Rio 2012

# Arguments: <<infile>> <<outfile>> <<N>>

# Check arguments
if [ $# -ne 3 ] ; then
    echo 1>&2 "USAGE:  <<infile>> <<outfile>> <<N>>"
    exit 127
fi

# set variables
infile=$1
outfile=$2
nan=$3

grd2xyz $infile -E -N$nan > $outfile