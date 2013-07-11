#!/bin/bash

# Script to produce plot of 2D data
# TARGET MODULE: psxy
# INPUT DATA: Tabular space delimited ASCII data
# Author: Nicholas Del Rio 2012

# Arguments: <<infile>> <<outfile>> <<R>> <<S>> <<J>> <<B>> <<G>> <<B>>

# Check arguments
if [ $# -ne 7 ] ; then
    echo 1>&2 "USAGE:  <<infile>> <<outfile>> <<R>> <<S>> <<J>> <<B>> <<G>> <<B>>"
    exit 127
fi

# set variables
infile=$1
outfile=$2
region=$3
plottingSymbolAndSize=$4
projection=$5
plottingSymbolFill=$6
boundaryAnnotationInterval=$7

psxy $infile -P -R$region -S$plottingSymbolAndSize -J$projection -G$plottingSymbolFill -B$boundaryAnnotationInterval > $outfile