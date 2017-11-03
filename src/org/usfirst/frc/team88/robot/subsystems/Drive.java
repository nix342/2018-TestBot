package org.usfirst.frc.team88.robot.subsystems;

import org.usfirst.frc.team88.robot.RobotMap;
import org.usfirst.frc.team88.robot.commands.DriveTank;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Robot flies around
 * forwards, backwards, left and right
 * watch us as we go
 */
public class Drive extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	private CANTalon leftTalon1, leftTalon2, leftTalon3, leftTalon4;
	private CANTalon rightTalon1, rightTalon2, rightTalon3, rightTalon4;
	private DoubleSolenoid shifter;
	
	private CANTalon[] leftTalons;
	private CANTalon[] rightTalons;
	
	public Drive(){
		leftTalons = new CANTalon[RobotMap.leftTalons.length];
		rightTalons = new CANTalon[RobotMap.rightTalons.length];
		
		
		for(int i = 0; i<RobotMap.leftTalons.length; i++){
			leftTalons[i] = new CANTalon(RobotMap.leftTalons[i]);
		}
		for(int i = 0; i<RobotMap.rightTalons.length; i++){
			rightTalons[i] = new CANTalon(RobotMap.rightTalons[i]);
		}

	
		
		for(int i = 0; i<RobotMap.leftTalons.length; i++){
			if (i==0){
				leftTalons[i].changeControlMode(TalonControlMode.PercentVbus);
			} else {
				leftTalons[i].changeControlMode(TalonControlMode.Follower);
				leftTalons[i].set(RobotMap.leftTalons[0]);
				
			}
			leftTalons[i].enableBrakeMode(true);
		}
		
		
		for(int i = 0; i<RobotMap.rightTalons.length; i++){
			if (i==0){
				rightTalons[i].changeControlMode(TalonControlMode.PercentVbus);
			} else {
				rightTalons[i].changeControlMode(TalonControlMode.Follower);
				rightTalons[i].set(RobotMap.rightTalons[0]);
				
			}
			rightTalons[i].enableBrakeMode(true);
		}
		
		shifter = new DoubleSolenoid(RobotMap.shifterLow,RobotMap.shifterHigh);
		shifter.set(Value.kForward);
		
		
	}
	
	
	public void wheelspeed (double left, double right){
		leftTalons[0].set(left);
		rightTalons[0].set(-right);
	}
	
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new DriveTank());
    }
   
    public void shift() {
	    
	    // SECRET COMMENT - KYLE H
    	if (shifter.get() == (Value.kForward)){
    		shifter.set(Value.kReverse);	
    	}
    	else {
    		shifter.set(Value.kForward);
    	}
    }
	
    public void updateDashboard(){
	for (int i = 0; i < RobotMap.leftTalons.length; i++) {
		SmartDashboard.putNumber("LeftCurrent" + i, leftTalons[i].getOutputCurrent());
		SmartDashboard.putNumber("LeftVoltage" + i, leftTalons[i].getOutputVoltage());
	}
	for (int i = 0; i < RobotMap.rightTalons.length; i++) {
		SmartDashboard.putNumber("RightCurrent" + i, rightTalons[i].getOutputCurrent());
		SmartDashboard.putNumber("RightVoltage" + i, rightTalons[i].getOutputVoltage());
	}
    }
}

