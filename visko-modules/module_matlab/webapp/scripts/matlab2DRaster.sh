#!/bin/bash

# Script to produce raster image from MAT-file file
# TARGET MODULE: matlab
# INPUT DATA: MAT-file file
# Author: Paulo Pinheiro 2012

# Arguments: <<inFile>> <<outFile>> <<modulePath>> <<selectedColor>> 

# Check arguments
if [ $# -ne 4 ] ; then
    echo 1>&2 "USAGE:  <<infile>> <<outfile>> <<modulePath>> <<selectedColor>>"
    exit 127
fi

# set variables
inFile=$1
outFile=$2
modulePath=$3
selectedColor=$4
#output_dir=${base_url}/output

# download requested file into the output file and replace url by local_path/file_name
#if [ ! -d "${output_dir}" ]; then
#  mkdir ${output_dir}
#fi
#cd ${output_dir}
#pwd
#after_slash=${output_dir}/${inputFile##*/}
#curl -L -O ${inputFile}

matlab_exec=/Applications/MATLAB_R2012a.app/bin/matlab
matlab_code=${modulePath}/scripts/app_code
#full_outputFile=${output_dir}/${outputFile}

cd ${matlab_code}
pwd

command="matlab_2D_Raster('"${inFile}"','"${outFile}"','"${selectedColor}"')"
echo ${command}
${matlab_exec} -nodisplay -nosplash -r ${command}

