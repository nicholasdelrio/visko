#! /bin/bash

# set name of visko distribution directory
if [ $# -eq 0 ]; then
	viskodir="visko"	
else
	viskodir=$1
fi

# Update existing visko SVN trunk or checkout entire trunk if empty
if [ -d visko-trunk ]; then
	echo "Updating visko trunk"
	svn update visko-trunk
else
	echo "Checking out visko trunk"
	svn checkout http://svn.code.sf.net/p/visko/code/trunk visko-trunk
fi

# Delete existing distribution directory if exists
if [ -d $viskodir ]; then
	echo "Deleting existing distribution directory"
	rm -rf $viskodir
fi

# Create new distribution directory
echo "Creating new distribution directory $viskodir"
mkdir $viskodir

# 1. copy core visko directories
echo "Copying core visko directories"
cp -R ./visko-trunk/visko/* $viskodir

# 2. copy module directories
echo "Copying module directories"
cp -R ./visko-trunk/visko-modules/* $viskodir

# 3. copy build directories
echo "Copying build directories"
cp -R ./visko-trunk/visko-build $viskodir

# Creating zip distribution
viskozip="${viskodir}.zip"
echo "Creating $viskozip"
zip -r $viskozip $viskodir
