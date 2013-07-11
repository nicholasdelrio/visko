;********************************************************************
; Name: timeSeriesPlotter
; Description: generate Time Series Plot with groups of Y variables.
; Each Y variable group corresponds to either left or right Y Axis.
;*********************************************************************

load "$NCARG_ROOT/lib/ncarg/nclscripts/csm/gsn_code.ncl"
load "$NCARG_ROOT/lib/ncarg/nclscripts/csm/gsn_csm.ncl"
load "$NCARG_ROOT/lib/ncarg/nclscripts/csm/contributed.ncl"

begin
	print(netCDFFilePath)
	print(outputFilePath)
	print(lPlotVariablesList)
	print(rPlotVariablesList)
	print(xDimName)
	print(xDimSize)
	print(title)
	print(yLAxisLabel)
	print(yRAxisLabel)
							
	;**************************************************
	; check inputs
	;**************************************************																															         
																																	
	if(isvar("netCDFFilePath") .and. isvar("outputFilePath") .and. isvar("lPlotVariablesList") .and. isvar("rPlotVariablesList") .and. isvar("xDimName") .and. isvar("xDimSize") .and. isvar("title") .and. isvar("yLAxisLabel") .and. isvar("yRAxisLabel")) then
		xDimSizeInteger = stringtointeger(xDimSize)
	
		lPlotVariableNames = str_split(lPlotVariablesList, ",")
		rPlotVariableNames = str_split(rPlotVariablesList, ",")

		if(rPlotVariableNames(0) .eq. "NULL") then
			existsRightAxis = False;
		else
			existsRightAxis = True;
		end if

		lPlotVariableCount = dimsizes(lPlotVariableNames)
		rPlotVariableCount = dimsizes(rPlotVariableNames)

		timeSeriesData = addfile(netCDFFilePath, "r")

		; left plot data
		lPlotData = new((/lPlotVariableCount, xDimSizeInteger/), double)

		do i = 0, lPlotVariableCount - 1
			plotVariableName = lPlotVariableNames(i);
			plotVariable = timeSeriesData->$plotVariableName$
			lPlotData(i,:) = plotVariable(:)
		end do

		; right plot data
		rPlotData = new ((/rPlotVariableCount, xDimSizeInteger/), double)

		if(existsRightAxis) then
			do i = 0, rPlotVariableCount - 1
				plotVariableName = rPlotVariableNames(i);
				plotVariable = timeSeriesData->$plotVariableName$
				rPlotData(i,:) = plotVariable(:)
			end do
		end if

		; make time array
		times = ispan(1, xDimSizeInteger, 1)
		times@long_name = "Time"

		; resources for "left" variable
		resL					= True
		resL@tiMainString		= title
		resL@xyDashPattern		= 0												; Make curves all solid
		resL@xyMarkLineMode		= "MarkLines"
		resL@xyMarkers			= (/6, 11, 16, 11, 10/)							;different markers
		resL@xyMarkerColors		= (/"blue", "red", "green", "orange", "cyan"/)	; change line color
		resL@tiYAxisString		= yLAxisLabel

		; resources for legend appended to resL
		resL@pmLegendDisplayMode	= "Always"	; Display a legend
		resL@pmLegendWidthF			= 0.3		; Decrease width
		resL@pmLegendHeightF		= 0.1		; Decrease height
		resL@lgBoxMinorExtentF		= 0.1		; Shorten the legend lines
		resL@lgLabelFontHeightF		= 0.010		; Change the font size

		; resources for panel_plot
		resL@gsnDraw	= False		; don't draw
		resL@gsnFrame	= False		; don't advance frame

		; resources for "right" variable
		resR					= True
		resR@xyDashPattern		= 0
		resR@xyMarkLineMode		= "MarkLines"
		resR@xyMarkers			= (/7, 8, 2, 0, 10, 12/)							;different markers
		resR@xyMarkerColors		= (/"blue", "red", "green", "orange", "cyan"/)		; change line color
		resR@xyMarkerColors		= (/"orange", "green", "purple", "red", "blue"/)
		resR@tiYAxisString		= yRAxisLabel

		wks		= gsn_open_wks ("ps", outputFilePath)							; open workstation
		plot	= gsn_csm_xy2 (wks, times, lPlotData, rPlotData, resL, resR)	; create plot

		gsn_panel(wks, plot, (/1,1/),False)             ; now draw as one plot
	
	else
		print("Need to Specify All Input Variables")
	end if
end