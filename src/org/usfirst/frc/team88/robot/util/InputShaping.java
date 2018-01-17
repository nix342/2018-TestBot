package org.usfirst.frc.team88.robot.util;

public class InputShaping {
	private static final double STICK_DEADZONE = 0.1;
	private static final double STICK_MAX = 0.97;
	private static final double POLY_A = 0.35;
	private static final double POLY_B = 0.5;
	private static final double POLY_C = 0.15;

	public static double applyDeadZone(double value) {
		if (Math.abs(value) < STICK_DEADZONE) {
			return 0.0;
		} else if (value > 0) {
			value = (value - STICK_DEADZONE) / (1 - STICK_DEADZONE);
		} else {
			value = (value + STICK_DEADZONE) / (1 - STICK_DEADZONE);
		}

		return value;
	}

	public static double applyMaxValue(double value){
		if(value >= STICK_MAX){
			return 1.0;
		}
		else if (value <= -STICK_MAX){
			return -1.0;
		}
		else{
			return value;
		}
	}

	public static double applySquare(double value) {
		double output;
		output = (value == 0.0 ? 0.0 : Math.pow(value, 3)/Math.abs(value));
		return (Math.abs(output) < 0.05 ? 0.0 : output);
	}
	
	public static double applyPoly(double value) {
		double output;
		
		output = POLY_A * Math.pow(value, 5) + POLY_B * Math.pow(value, 3) + POLY_C * value;
		
		if (Math.abs(output) < STICK_DEADZONE) {
			output = 0.0;
		}
		
		return output;
	}
}
