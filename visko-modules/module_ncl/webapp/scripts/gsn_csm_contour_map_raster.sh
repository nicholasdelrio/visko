#!/bin/sh

# Check arguments
if [ $# -ne 12 ] ; then
	echo 1>&2 "Usage: inFileName outFileName plotVariable font lbOrientation colorTable latVariable lonVariable indexOfX indexOfY indexOfZ scriptsPath"
	exit 127
fi

netCDFFilePath=$1
outputFilePath=$2
plotVariable=$3
font=$4
lbOrientation=$5
colorTable=$6
latVariable=$7
lonVariable=$8
indexOfX=${9}
indexOfY=${10}
indexOfZ=${11}
scriptsPath=${12}

useNCLFile=$scriptsPath/gsn_csm_contour_map_raster.ncl

ncl netCDFFilePath=\"$netCDFFilePath\" outputFilePath=\"$outputFilePath\" plotVariable=\"$plotVariable\" font=\"$font\" lbOrientation=\"$lbOrientation\" colorTable=\"$colorTable\" latVariable=\"$latVariable\" lonVariable=\"$lonVariable\" indexOfX=\"$indexOfX\" indexOfY=\"$indexOfY\" indexOfZ=\"$indexOfZ\" $useNCLFile <<$end
$blank
$blank
$blank
$blank
$end
