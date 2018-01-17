package org.usfirst.frc.team88.robot;

import org.usfirst.frc.team88.robot.commands.*;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
	private static final double STICK_DEADZONE = 0.1;
	private static final double STICK_MAX = 0.97;
	private static final double POLY_A = 0.35;
	private static final double POLY_B = 0.5;
	private static final double POLY_C = 0.15;

	public XboxController driver = new XboxController(0);
	public XboxController operator = new XboxController(1);

	public OI() {
		//// TRIGGERING COMMANDS WITH BUTTONS
		// Once you have a button, it's trivial to bind it to a button in one of
		// three ways:

		// button.whenPressed(new ExampleCommand());
		//    Start the command when the button is pressed and let it run the command
		//    until it is finished as determined by it's isFinished method.

		// button.whileHeld(new ExampleCommand());
		//    Run the command while the button is being held down and interrupt it once
		//    the button is released.

		// button.whenReleased(new ExampleCommand());
		//    Start the command when the button is released and let it run the command
		//    until it is finished as determined by it's isFinished method.

		driver.buttonLeftBumper.whenPressed(new DriveZeroEncoders());
		
		driver.buttonA.whileHeld(new LiftUp());
		driver.buttonB.whileHeld(new LiftDown());
	}

	// Utility functions

	public double applyDeadZone(double value) {
		if (Math.abs(value) < STICK_DEADZONE) {
			return 0.0;
		} else if (value > 0) {
			value = (value - STICK_DEADZONE) / (1 - STICK_DEADZONE);
		} else {
			value = (value + STICK_DEADZONE) / (1 - STICK_DEADZONE);
		}

		return value;
	}

	public double applyMaxValue(double value){
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

	public double applySquare(double value) {
		double output;
		output = (value == 0.0 ? 0.0 : Math.pow(value, 3)/Math.abs(value));
		return (Math.abs(output) < 0.05 ? 0.0 : output);
	}
	
	public double applyPoly(double value) {
		double output;
		
		output = POLY_A * Math.pow(value, 5) + POLY_B * Math.pow(value, 3) + POLY_C * value;
		
		if (Math.abs(output) < STICK_DEADZONE) {
			output = 0.0;
		}
		
		return output;
	}
}
