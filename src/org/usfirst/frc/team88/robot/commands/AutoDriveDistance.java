package org.usfirst.frc.team88.robot.commands;

import org.usfirst.frc.team88.robot.Robot;

import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class AutoDriveDistance extends Command {

	// states
	private static final int PREP = 0;
	private static final int ACCELERATE = 1;
	private static final int CRUISE = 2;
	private static final int DECELERATE = 3;
	private static final int STOP = 4;
	private static final int END = 5;

	private static final double CRUISING_SPEED = 0.4;
	private static final double ACCELERATION = 0.03;

	private int state;
	private double speed;
	private double targetDistance;
	private double accelerateDistance;
	private boolean done;

    public AutoDriveDistance() {
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    	requires(Robot.drive);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	Robot.drive.resetEncoders();

    	state = PREP;
    	done = false;
		speed = 0.0;
		targetDistance = 8000;
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {

    	switch (state){
    	case PREP:
    		state = ACCELERATE;
    		break;
    	case ACCELERATE:
    		speed = speed + ACCELERATION;
    		if (speed > CRUISING_SPEED) {
    			state = CRUISE;
    			accelerateDistance = Robot.drive.getAvgPosition(); 
    		}
    		break;
    	case CRUISE:
    		if (Robot.drive.getAvgPosition() > (targetDistance - accelerateDistance)) {
    			state = DECELERATE;
    		}
    		break;
    	case DECELERATE:
    		speed = speed - ACCELERATION;
    		if (speed < 0) {
    			speed = 0.0;
    			state = STOP;
    		}
    		break;
    	case STOP:
    		speed = 0.0;
    		state = END;
    		break;
    	case END:
    		done = true;
    		break;
    	}
    	SmartDashboard.putNumber("State", state);
    	
    	Robot.drive.wheelspeed(-speed, -speed);
    	Robot.drive.updateDashboard();
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return done;
    }

    // Called once after isFinished returns true
    protected void end() {
    	Robot.drive.wheelspeed(0, 0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	Robot.drive.wheelspeed(0, 0);
    }
}
