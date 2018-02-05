package org.usfirst.frc.team88.robot.commands;

import org.usfirst.frc.team88.robot.Robot;
import org.usfirst.frc.team88.robot.util.InputShaping;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveTank2 extends Command {
	public final static double SENSITIVITY = 0.25;

	public DriveTank2() {
		requires(Robot.drive);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		double left, right, magnitude, curve, targetHeading, error;

		left = InputShaping.applyPoly(Robot.oi.driver.getLeftStickY());
		right = InputShaping.applyPoly(Robot.oi.driver.getRightStickY());
		
		magnitude = (left + right) / 2;

		curve = (left - right) / 2;
		
		if (Math.abs(curve) < 0.10) {
			curve = 0;
		}
		

		if ((curve == 0) && (magnitude != 0)) {
			if (Robot.oi.driver.isButtonAPressed()) {
				targetHeading = 180;
			} else if (Robot.oi.driver.isButtonBPressed()) {
				targetHeading = 90;
			} else if (Robot.oi.driver.isButtonXPressed()) {
				targetHeading = -90;
			} else if (Robot.oi.driver.isButtonYPressed()) {
				targetHeading = 0;
			} else {
				targetHeading = 999;
			}

      if (targetHeading != 999) {
				error = targetHeading - Robot.drive.getYaw();

				if (error > 180) {
					error = error - 360;
				} else if (error < -180) {
					error = error + 360;
				}

				curve = error * 0.013;

				if (curve > 1) {
					curve = 1;
				} else if (curve < -1) {
					curve = -1;
				}
			}
		}

		Robot.drive.driveCurve(magnitude, curve, SENSITIVITY);
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return false;
	}

	// Called once after isFinished returns true
	protected void end() {
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		Robot.drive.wheelSpeed(0, 0);
	}
}
