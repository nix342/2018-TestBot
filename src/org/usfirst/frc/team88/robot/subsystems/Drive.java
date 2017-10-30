package org.usfirst.frc.team88.robot.subsystems;

import org.usfirst.frc.team88.robot.RobotMap;
import org.usfirst.frc.team88.robot.commands.DriveTank;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.command.Subsystem;

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
	
	public Drive(){
		leftTalon1 = new CANTalon(RobotMap.lefttalons[0]);
		leftTalon2 = new CANTalon(RobotMap.lefttalons[1]);
		leftTalon3 = new CANTalon(RobotMap.lefttalons[2]);
		leftTalon4 = new CANTalon(RobotMap.lefttalons[3]);
		rightTalon1 = new CANTalon(RobotMap.righttalons[0]);
		rightTalon2 = new CANTalon(RobotMap.righttalons[1]);
		rightTalon3 = new CANTalon(RobotMap.righttalons[2]);
		rightTalon4 = new CANTalon(RobotMap.righttalons[3]);
		
		leftTalon1.changeControlMode(TalonControlMode.PercentVbus);
		leftTalon2.changeControlMode(TalonControlMode.Follower);
		leftTalon2.set(RobotMap.lefttalon1);
		leftTalon3.changeControlMode(TalonControlMode.Follower);
		leftTalon3.set(RobotMap.lefttalon1);
		leftTalon4.changeControlMode(TalonControlMode.Follower);
		leftTalon4.set(RobotMap.lefttalon1);
		
		rightTalon1.changeControlMode(TalonControlMode.PercentVbus);
		rightTalon2.changeControlMode(TalonControlMode.Follower);
		rightTalon2.set(RobotMap.righttalon1);
		rightTalon3.changeControlMode(TalonControlMode.Follower);
		rightTalon3.set(RobotMap.righttalon1);
		rightTalon4.changeControlMode(TalonControlMode.Follower);
		rightTalon4.set(RobotMap.righttalon1);
		
		shifter = new DoubleSolenoid(RobotMap.shifterLow,RobotMap.shifterHigh);
		shifter.set(Value.kForward);
		
		
	}
	
	
	public void wheelspeed (double left, double right){
		leftTalon1.set(left);
		rightTalon1.set(right);
	}
	
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        setDefaultCommand(new DriveTank());
    }
   
    public void shift() {
    	if (shifter.get() == (Value.kForward)){
    		shifter.set(Value.kReverse);	
    	}
    	else {
    		shifter.set(Value.kForward);
    	}
    }
}

