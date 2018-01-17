package org.usfirst.frc.team88.robot.util;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

public class XboxController extends Joystick {
	private static final int LEFT_HORIZ_AXIS = 0;
	private static final int LEFT_VERT_AXIS = 1;
	private static final int RIGHT_HORIZ_AXIS = 4;
	private static final int RIGHT_VERT_AXIS = 5;
	private static final int LEFT_Z_AXIS = 3;
	private static final int RIGHT_Z_AXIS = 2;

	public XboxController(int port) {
		super(port);
	}

	public Button buttonA = new JoystickButton(this, 1);
	public Button buttonB = new JoystickButton(this, 2);
	public Button buttonX = new JoystickButton(this, 3);
	public Button buttonY = new JoystickButton(this, 4);
	public Button buttonLeftBumper = new JoystickButton(this, 5);
	public Button buttonRightBumper = new JoystickButton(this, 6);
	public Button buttonBack = new JoystickButton(this, 7);
	public Button buttonStart = new JoystickButton(this, 8);
	public Button buttonLeftStick = new JoystickButton(this, 9);
	public Button buttonRightStick = new JoystickButton(this, 10);

	public double getRightStickY() {
		return -this.getRawAxis(RIGHT_VERT_AXIS);
	}

	public double getRightStickX() {
		return this.getRawAxis(RIGHT_HORIZ_AXIS);
	}

	public double getLeftStickY() {
		return -this.getRawAxis(LEFT_VERT_AXIS);
	}

	public double getLeftStickX() {
		return this.getRawAxis(LEFT_HORIZ_AXIS);
	}

	public double getLeftTrigger() {
		return this.getRawAxis(LEFT_Z_AXIS);
	}

	public double getRightTrigger() {
		return this.getRawAxis(RIGHT_Z_AXIS);
	}

	public double getZ() {
		return this.getRawAxis(LEFT_Z_AXIS) - this.getRawAxis(RIGHT_Z_AXIS);
	}

	public void rumble(double rumble) {
		this.setRumble(RumbleType.kLeftRumble, rumble);
		this.setRumble(RumbleType.kRightRumble, rumble);
	}

	public void rumbleLeft(double rumble) {
		this.setRumble(RumbleType.kLeftRumble, rumble);
	}

	public void rumbleRight(double rumble) {
		this.setRumble(RumbleType.kRightRumble, rumble);
	}
	
}
