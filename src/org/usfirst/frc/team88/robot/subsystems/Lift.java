package org.usfirst.frc.team88.robot.subsystems;

import edu.wpi.first.wpilibj.command.Subsystem;

/**
 * It's not really there
 * we are only pretending
 * it goes up and down
 */
public class Lift extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.

	private double height;
	
	public Lift() {
		height = 0;
	}
	
	public void up() {
		height += 0.04;
		
		if (height > 1) {
			height = 1.0;
		}
	}
	
	public void down() {
		height -= 0.04;
		
		if (height < 0) {
			height = 0.0;
		}
	}
	
	public double getHeight() {
		return height;
	}
	
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
}

