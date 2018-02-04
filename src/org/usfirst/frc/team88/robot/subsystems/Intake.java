package org.usfirst.frc.team88.robot.subsystems;

import org.usfirst.frc.team88.robot.Robot;
import org.usfirst.frc.team88.robot.RobotMap;
import org.usfirst.frc.team88.robot.SharpIR;
import org.usfirst.frc.team88.robot.commands.IntakeCommand;
import org.usfirst.frc.team88.robot.commands.IntakeCommand2;

import com.ctre.phoenix.motorcontrol.ControlMode;

import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Kyle Hackett
 * 
 * Intake Haiku v2.
 * 
 * The green wheels spin fast
 * The cube is now ours to use
 * And now we will win
 *
 */
public class Intake extends Subsystem {
	final double MAXSPEED = 1;
	final double LOWERSPEED = 0.8;
	private Talon rightSide; 
	private Talon leftSide; 
	private SharpIR leftSensor, rightSensor;
	private boolean haveCube = false;
	// Put methods for controlling this subsystem
	// here. Call these from Commands.

	public Intake () {

		rightSide = new Talon(RobotMap.rightSide);
		leftSide = new Talon(RobotMap.leftSide);
		leftSensor = new SharpIR(RobotMap.leftIR);
		rightSensor = new SharpIR(RobotMap.rightIR);

	}

	public void intakeWheelSpeed (double speed) {

		if (Robot.oi.driver.getZ() > 0){
			rightSide.set(speed * LOWERSPEED);
			leftSide.set(speed * MAXSPEED);
		}
		else  { 
			rightSide.set(speed * MAXSPEED);
			leftSide.set(speed * MAXSPEED);
		}

	}
	
	public void intakeTest(double left, double right){
		leftSide.set(left);
		rightSide.set(right);
	}

	public double cubeDistance(){
		double distance = leftSensor.getDistance();
		
		//TODO Change the distances to allow for different positions of cube as well as the actual distance
		if((distance < 5)&&(distance < 5)){
			haveCube = true;
		}
		else {
			haveCube = false;
		}
		SmartDashboard.putBoolean("Intake/Has Cube", haveCube);
		return distance;
	}

   public void updateDashboard() {
	   SmartDashboard.putNumber("Intake/Left Distance", leftSensor.getDistance());
	   SmartDashboard.putNumber("Intake/Right Distance", rightSensor.getDistance());
	   SmartDashboard.putNumber("Intake/Left IR Voltage", leftSensor.getAverageVoltage());
	   SmartDashboard.putNumber("Intake/Right IR Voltage", rightSensor.getAverageVoltage());
   }
	
	
	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		//setDefaultCommand(new MySpecialCommand());
		setDefaultCommand(new IntakeCommand2());
	}



}

