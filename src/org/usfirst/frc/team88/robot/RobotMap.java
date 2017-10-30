package org.usfirst.frc.team88.robot;

/**
 * The RobotMap is a mapping from the ports sensors and actuators are wired into
 * to a variable name. This provides flexibility changing wiring, makes checking
 * the wiring easier and significantly reduces the number of magic numbers
 * floating around.
 */
public class RobotMap {
	// For example to map the left and right motors, you could define the
	// following variables to use with your drivetrain subsystem.
	// public static int leftMotor = 1;
	// public static int rightMotor = 2;

	// If you are using multiple modules, make sure to define both the port
	// number and the module. For example you with a rangefinder:
	// public static int rangefinderPort = 1;
	// public static int rangefinderModule = 1;
	// Drive
	public static final int lefttalon1 = 12;
	public static final int lefttalon2 = 11;
	public static final int lefttalon3 = 14;
	public static final int lefttalon4 = 8;
	public static final int righttalon1 = 13;
	public static final int righttalon2 = 9;
	public static final int righttalon3 = 15;
	public static final int righttalon4 = 10;
	
	public static final int shifterHigh = 0;
	public static final int shifterLow = 1;
	
	public static final int[] leftTalons = {12, 11, 14, 8};
	public static final int[] rightTalons = {13, 9, 15, 10};
}

