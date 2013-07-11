#!/bin/bash

# Script to produce molecular structure image from PDB file
# TARGET MODULE: jmol
# INPUT DATA: pdb file
# Author: Paulo Pinheiro 2012

# Arguments: <<infile>> <<outfile>>

# Check arguments
if [ $# -ne 3 ] ; then
    echo 1>&2 "USAGE:  <<infile>> <<outfile>> <<module-path>>"

EOF
    exit 127
fi

# set variables
infile=$1
outfile=$2
modpath=$3

# The following echo command writes the script to be ingested by Jmol with the instruction to load ${infile}.
# Any additional Jmol script instruction should be added inside this echo command.
echo 'load ' ${infile} '; select all; wireframe off; select protein; trace 40; select protein and helix; trace off; ribbons;' > ${modpath}/scripts/jmol-script-ribbon

# executes Jmol in silent mode generating ${outfile}
java -jar ${modpath}/WEB-INF/lib/Jmol.jar -ions ${modpath}/scripts/jmol-script-ribbon -w JPEG:${outfile} > ${modpath}/output/output.txt