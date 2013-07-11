#!/bin/bash

# Script to produce raster image
# TARGET MODULE: grdimage
# INPUT DATA: GMT netCDF gridded dataset
# Author: Nicholas Del Rio 2012

# Arguments: <<infile>> <<outfile>> <<T>> <<R>> <<workspace>> <<C>> <<J>> <<B>>

# Check arguments
if [ $# -ne 8 ] ; then
    echo 1>&2 "USAGE: <<infile>> <<outfile>> <<T>> <<R>> <<workspace>> <<C>> <<J>> <<B>>"
    exit 127
fi

# set variables
infile=$1
outfile=$2
colorrange=$3
region=$4
workspace=$5
colorPallet=$6
projection=$7
boundaryAnnotationInterval=$8

colorsFile=$workspace/colors.cpt

makecpt -C$colorPallet -T$colorrange > $colorsFile
grdimage $infile -J$projection -P -B$boundaryAnnotationInterval -C$colorsFile > $outfile

rm $colorsFile