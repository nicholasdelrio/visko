package edu.utep.trustlab.visko.knowledge;

import edu.utep.trustlab.visko.knowledge.universal.profiles.BrightnessTemperatureProfile;
import edu.utep.trustlab.visko.knowledge.universal.profiles.CoverageModelProfile;
import edu.utep.trustlab.visko.knowledge.universal.profiles.DuSumProfile;
import edu.utep.trustlab.visko.knowledge.universal.profiles.GravityDataProfile;
import edu.utep.trustlab.visko.knowledge.universal.profiles.GriddedGravityDataProfile;
import edu.utep.trustlab.visko.knowledge.universal.profiles.GriddedTimeProfile;
import edu.utep.trustlab.visko.knowledge.universal.profiles.VelocityModelProfile;

public class Generator3 {
	public static void main(String[] args){
		
		//create profiles
		BrightnessTemperatureProfile.create();
		CoverageModelProfile.create();
		DuSumProfile.create();
		GravityDataProfile.create();
		GriddedGravityDataProfile.create();
		GriddedTimeProfile.create();
		VelocityModelProfile.create();
	}
}
