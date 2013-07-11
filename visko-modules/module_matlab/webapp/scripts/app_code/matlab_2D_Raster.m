% renders a 2D Matrix inside of inputFile into a TIFF-format file

function matlab_2D_Raster(inputFile, outputFile, selectedColor)

disp('[matlab_2D_Raster]'); 
disp('input parameters:');
disp(['  => inputFile: ',inputFile]);
disp(['  => outputFile: ',outputFile]);
disp(['  => selectedColor: ',selectedColor]);

load(inputFile);
disp(['loaded ',inputFile]);
whos;

% take a look at image
imagesc(img001);
colormap (selectedColor);
colorbar;
axis on;
print('-djpeg', outputFile);
disp(['wrote ' outputFile]);

close all;

exit;
