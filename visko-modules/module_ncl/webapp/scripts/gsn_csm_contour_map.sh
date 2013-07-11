#!/bin/sh

# Check arguments
if [ $# -ne 15 ] ; then
	echo 1>&2 "Usage: inFileName outFileName plotVariable font lbOrientation cnLevelSpacingF colorTable cnLinesOn cnFillOn latVariable lonVariable indexOfX indexOfY indexOfZ scriptsPath"
	exit 127
fi

netCDFFilePath=$1
outputFilePath=$2
plotVariable=$3
font=$4
lbOrientation=$5
cnLevelSpacingF=$6
colorTable=$7
cnLinesOn=$8
cnFillOn=$9
latVariable=${10}
lonVariable=${11}
indexOfX=${12}
indexOfY=${13}
indexOfZ=${14}
scriptsPath=${15}

useNCLFile=$scriptsPath/gsn_csm_contour_map.ncl

ncl netCDFFilePath=\"$netCDFFilePath\" outputFilePath=\"$outputFilePath\" plotVariable=\"$plotVariable\" font=\"$font\" lbOrientation=\"$lbOrientation\" cnLevelSpacingF=\"$cnLevelSpacingF\" colorTable=\"$colorTable\" cnLinesOn=\"$cnLinesOn\" cnFillOn=\"$cnFillOn\" latVariable=\"$latVariable\" lonVariable=\"$lonVariable\" indexOfX=\"$indexOfX\" indexOfY=\"$indexOfY\" indexOfZ=\"$indexOfZ\" $useNCLFile <<$end
$blank
$blank
$blank
$end
