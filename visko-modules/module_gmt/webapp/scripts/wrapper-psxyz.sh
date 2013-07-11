#!/bin/bash

# Script to produce contour map
# TARGET MODULE: grdcontour
# INPUT DATA: GMT netCDF gridded dataset
# Author: Nicholas Del Rio 2012

# Arguments: <<infile>> <<outfile>> <<B>> <<J>> <<JZ>> <<R>> <<E>> <<S>> <<W>> <<G>>

# Check arguments
if [ $# -ne 10 ] ; then
    echo 1>&2 "USAGE:  <<infile>> <<outfile>> <<B>> <<J>> <<JZ>> <<R>> <<E>> <<S>> <<W>> <<G>>"
    exit 127
fi

# set variables
infile=$1
outfile=$2
boundaryAnnotationInterval=$3
projection=$4
height=$5
region=$6
azimuthElevation=$7
plotSymbol=$8
penAttributes=$9
filling=${10}

psxyz -B$boundaryAnnotationInterval -J$projection -JZ$height -R$region -E$azimuthElevation -S$plotSymbol $infile -W$penAttributes -G$filling > $outfile