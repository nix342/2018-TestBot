package org.usfirst.frc.team88.robot.commands;

import org.usfirst.frc.team88.robot.Robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class AutoSwitchFirstTurnDouble extends Command {
	double targetAngle;
	private boolean firstPassComplete;
	private int count;
	String gameData;
	
    public AutoSwitchFirstTurnDouble() {
    	requires(Robot.drive);
		
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	gameData = DriverStation.getInstance().getGameSpecificMessage();
		if(gameData.charAt(0) == 'L')
		{
			targetAngle=-36;
		} else if(gameData.charAt(0)=='R'){
			targetAngle=30;
		}
		else{
			//do smart
		}
    	firstPassComplete = false;
    	Robot.drive.rotateController.reset();
    	Robot.drive.rotateController.setSetpoint(targetAngle);
    	Robot.drive.rotateController.enable();    	
    	
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
		SmartDashboard.putNumber("Rot Error", Robot.drive.rotateController.getError());
		SmartDashboard.putNumber("Rot On Target", Robot.drive.rotateController.onTarget()?1:0);
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
		if (Robot.drive.rotateController.onTarget() && !firstPassComplete) {
			firstPassComplete = true;
			Robot.drive.rotateController.disable();
			Robot.drive.wheelSpeed(0, 0);
			count = 0;
	    	return false;
		} else if (count > 11) {
			return Robot.drive.rotateController.onTarget();
		} else if (firstPassComplete && count++ > 10) {
			Robot.drive.rotateController.reset();
	    	Robot.drive.rotateController.setSetpoint(targetAngle);
	    	Robot.drive.rotateController.enable();
	    	return false;
		} else {
			return false;
		}
	}

    // Called once after isFinished returns true
    protected void end() {
		Robot.drive.rotateController.disable();
		Robot.drive.wheelSpeed(0, 0);
   }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
		Robot.drive.rotateController.disable();    	
		Robot.drive.wheelSpeed(0, 0);
   }
   
}
