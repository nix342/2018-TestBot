/**
 * 
 */
package org.usfirst.frc.team88.robot;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * @author TJ2
 *
 */
public class SharpIR extends AnalogInput {

private static final int OVERSAMPLE_BITS = 4;
private static final int AVERAGE_BITS = 8;
private static final double K = 5.107;
	
	/**
	 * @param channel
	 */
	public SharpIR(int channel) {
		super(channel);
		this.setOversampleBits(OVERSAMPLE_BITS);
		this.setAverageBits(AVERAGE_BITS);
	}
	
	public double getDistance() {
		double distance = 0;
		double voltage = getAverageVoltage();

		distance = (K / voltage) - 0.42;
		
		SmartDashboard.putNumber("Intake/IR Sensor Distance", distance );
		SmartDashboard.putNumber("Intake/IR Sensor Voltage", voltage);

		return distance;
	}
	
	public double pidGet() {
		return getDistance();
	}

}
