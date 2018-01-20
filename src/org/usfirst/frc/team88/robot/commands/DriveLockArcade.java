package org.usfirst.frc.team88.robot.commands;

import org.usfirst.frc.team88.robot.Robot;
import org.usfirst.frc.team88.robot.util.InputShaping;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class DriveLockArcade extends Command {
	public final static double SENSITIVITY = 0.25;
	private double targetYaw;
	public DriveLockArcade(double t) {
		requires(Robot.drive);
		targetYaw = t;
	}

	// Called just before this Command runs the first time
	protected void initialize() {
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {
		double magnitude;
		double curve;
		
		if(Math.abs(targetYaw) - Math.abs(Robot.drive.getYaw()) < 3){
			curve = 0;
		}
		else{
			curve = (targetYaw - (Robot.drive.getYaw())) * 0.01;
		}
		// below for rocket league style
		// magnitude = InputShaping.applyPoly(Robot.oi.driver.getZ());
		
		magnitude = InputShaping.applyPoly(Robot.oi.driver.getLeftStickY());
		

		Robot.drive.driveCurve(magnitude, curve, SENSITIVITY);

		Robot.drive.updateDashboard();
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