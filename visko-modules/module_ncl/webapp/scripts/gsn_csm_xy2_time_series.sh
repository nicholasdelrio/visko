#!/bin/sh

# Check arguments
if [ $# -ne 10 ] ; then
	echo 1>&2 "Usage: netCDFFilePath, outputFilePath, lPlotVariablesList, rPlotVariablesList, xDimName, xDimSize, title, yLAxisLabel, yRAxisLabel scriptsPath"
	exit 127
fi

netCDFFilePath=$1
outputFilePath=$2
lPlotVariablesList=$3
rPlotVariablesList=$4
xDimName=$5
xDimSize=$6
title=$7
yLAxisLabel=$8
yRAxisLabel=$9
scriptsPath=${10}

useNCLFile=$scriptsPath/gsn_csm_xy2_time_series.ncl

ncl netCDFFilePath=\"$netCDFFilePath\" outputFilePath=\"$outputFilePath\" lPlotVariablesList=\"$lPlotVariablesList\" rPlotVariablesList=\"$rPlotVariablesList\" xDimName=\"$xDimName\" xDimSize=\"$xDimSize\" title=\"$title\" yLAxisLabel=\"$yLAxisLabel\" yRAxisLabel=\"$yRAxisLabel\" $useNCLFile <<$end
$blank
$blank
$blank
$end
