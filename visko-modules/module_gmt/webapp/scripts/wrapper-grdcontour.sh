#!/bin/bash

# Script to produce contour map
# TARGET MODULE: grdcontour
# INPUT DATA: GMT netCDF gridded dataset
# Author: Nicholas Del Rio 2012

# Arguments: <<infile>> <<outfile>> <<C>> <<A>> <<J>> <<S>> <B>> <<Wa>> <<Wc>>

# Check arguments
if [ $# -ne 9 ] ; then
    echo 1>&2 "USAGE:  <<infile>> <<outfile>> <<C>> <<A>> <<J>> <<S>> <B>> <<Wa>> <<Wc>>"
    exit 127
fi

# set variables
infile=$1
outfile=$2
contourInterval=$3
annotationInterval=$4
projection=$5
smoothing=$6
boundaryAnnotationInterval=$7
annotationPen=$8
contourPen=$9

# produce contour ps file out of gridded data
# P flag is for portrait orientation, B2 flag is for ticks every two units
grdcontour $infile -J$projection -P -S$smoothing -B$boundaryAnnotationInterval -C$contourInterval -A$annotationInterval -Wa$annotationPen -Wc$contourPen  > $outfile