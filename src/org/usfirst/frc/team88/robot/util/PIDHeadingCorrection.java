package org.usfirst.frc.team88.robot.util;
import edu.wpi.first.wpilibj.PIDOutput;

public class PIDHeadingCorrection implements PIDOutput {

	private double headingCorrection;
	@Override
	public void pidWrite(double output) {
		headingCorrection = output;
	}
	
	public double getHeadingCorrection() {
		return headingCorrection;
	}

}
