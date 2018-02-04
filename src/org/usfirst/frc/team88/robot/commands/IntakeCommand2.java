package org.usfirst.frc.team88.robot.commands;

import org.usfirst.frc.team88.robot.Robot;
import org.usfirst.frc.team88.robot.subsystems.Intake;
import org.usfirst.frc.team88.robot.util.InputShaping;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class IntakeCommand2 extends Command {

    public IntakeCommand2() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.intake);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	Robot.intake.cubeDistance();
    	Robot.intake.intakeTest(InputShaping.applyPoly(Robot.oi.operator.getLeftStickY()), InputShaping.applyPoly(Robot.oi.operator.getRightStickY()));
    	
    	Robot.intake.updateDashboard();
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
    	Robot.intake.intakeWheelSpeed(0.0);
    }
}
