package org.usfirst.frc.team88.robot;

import org.usfirst.frc.team88.robot.commands.*;
import org.usfirst.frc.team88.robot.util.XboxController;

/**
 * This class is the glue that binds the controls on the physical operator
 * interface to the commands and command groups that allow control of the robot.
 */
public class OI {
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
}
