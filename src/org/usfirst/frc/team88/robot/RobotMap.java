package org.usfirst.frc.team88.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	// Drive
	public static final int leftTalonMaster = 31;
	public static final int rightTalonMaster = 32;
	
	public static final int[] rightFollowers = {21, 22, 23};
	public static final int[] leftFollowers = {24, 25, 26};
}

