package org.usfirst.frc.team88.robot.commands;

import org.usfirst.frc.team88.robot.Robot;


import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 *
 */
public class DriveZeroEncoders extends InstantCommand {

    public DriveZeroEncoders() {
    	requires(Robot.drive);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.drive.resetEncoders();
    }

   
}
