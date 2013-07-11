; ***********************************************************************
; Description: Read an ESRI grid formated file
; For a data format description, see http://en.wikipedia.org/wiki/ESRI_grid
;
; NOTE: The function silently assumes a regular latitude/longitude grid and 
; sets the metadata accordingly. It is the user's responsibility to transform
; the grid or data if necessary.
;*************************************************************************

undef("esriGridReader")

function esriGridReader(filename)
begin

 	; read in all numbers
 	raw =  asciiread(filename, -1, "float")
	  
	; first 6 numbers are header
	ncols = floattoint(raw(0)) ; number of columns
	nrows = floattoint(raw(1)) ; number of rows
	xllcorner    = raw(2) ; x coord of lower left corner
	yllcorner    = raw(3) ; y coord of lower left corner
	cellsize     = raw(4) ; cellsize
	NODATA_value = raw(5) ; Fill value

	; reshape the rest of the data
	data = onedtond(raw(6::),(/nrows,ncols/))
							   
	; create longitude
	lon = xllcorner + (ispan(1,ncols,1) - 0.5) * cellsize
	lon@units = "degrees_east"

	; create latitude, reverse to fit data
	lat = yllcorner + (ispan(1,nrows,1) - 0.5) * cellsize
	lat = lat(::-1)
	lat@units = "degrees_north"

	; add coorinates to data
	data!0 = "lat"
	data&lat = lat

	data!1 = "lon"
	data&lon = lon
																  
	; attach the remaining metadata
	data@_FillValue = NODATA_value
	data@cellsize = cellsize
	data@xllcorner = xllcorner
	data@yllcorner = yllcorner
	return(data)
end