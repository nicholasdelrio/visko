/*
Copyright (c) 2012, University of Texas at El Paso
All rights reserved.
Redistribution and use in source and binary forms, with or without modification, are permitted
provided that the following conditions are met:

	-Redistributions of source code must retain the above copyright notice, this list of conditions
	 and the following disclaimer.
	-Redistributions in binary form must reproduce the above copyright notice, this list of conditions
	 and the following disclaimer in the documentation and/or other materials provided with the distribution.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED
WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT,
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE
GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.*/


package edu.utep.trustlab.visko.knowledge;

import edu.utep.trustlab.visko.knowledge.universal.profiles.BrightnessTemperatureProfile;
import edu.utep.trustlab.visko.knowledge.universal.profiles.CIIXRAYDataProfile;
import edu.utep.trustlab.visko.knowledge.universal.profiles.CoverageModelProfile;
import edu.utep.trustlab.visko.knowledge.universal.profiles.DuSumProfile;
import edu.utep.trustlab.visko.knowledge.universal.profiles.GravityDataProfile;
import edu.utep.trustlab.visko.knowledge.universal.profiles.GriddedGravityDataProfile;
import edu.utep.trustlab.visko.knowledge.universal.profiles.GriddedTimeProfile;
import edu.utep.trustlab.visko.knowledge.universal.profiles.VelocityModelProfile;

public class Generator4 {
	public static void gen(){
		
		//create profiles
		BrightnessTemperatureProfile.create();
		CoverageModelProfile.create();
		DuSumProfile.create();
		GravityDataProfile.create();
		GriddedGravityDataProfile.create();
		GriddedTimeProfile.create();
		VelocityModelProfile.create();
		CIIXRAYDataProfile.create();
	}
}
