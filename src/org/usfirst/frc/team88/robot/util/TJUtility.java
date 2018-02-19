package org.usfirst.frc.team88.robot.util;

public class TJUtility {
	private static final double POLY_A = 0.35;
	private static final double POLY_B = 0.5;
	private static final double POLY_C = 0.15;

	public static double deadZone(double value, double deadzone) {
		if (Math.abs(value) < deadzone) {
			return 0.0;
		} else if (value > 0) {
			value = (value - deadzone) / (1 - deadzone);
		} else {
			value = (value + deadzone) / (1 - deadzone);
		}

		return value;
	}

	public static double maxValue(double value, double max) {
		if (value >= max) {
			return max;
		} else if (value <= -max) {
			return -max;
		} else {
			return value;
		}
	}

	public static double square(double value) {
		double output;
		output = (value == 0.0 ? 0.0 : Math.pow(value, 3) / Math.abs(value));
		return (Math.abs(output) < 0.05 ? 0.0 : output);
	}

	public static double polynomial(double value, double a, double b, double c, double deadzone) {
		double output;

		output = a * Math.pow(value, 5) + b * Math.pow(value, 3) + c * value;

		if (Math.abs(output) < deadzone) {
			output = 0.0;
		}

		return output;
	}

	public static double normalizeAngle(double angle) {
		angle = angle % 360.0;
		if (angle > 180.0) {
			angle -= 360.0;
		} else if (angle < -180.0) {
			angle += 360.0;
		}

		return angle;
	}
}
