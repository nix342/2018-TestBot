package org.usfirst.frc.team88.robot.subsystems;

import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.command.Subsystem;

/**
 *
 */
public class Lights extends Subsystem {

    // Put methods for controlling this subsystem
    // here. Call these from Commands.
	private SerialPort serialPort;
	public Lights(){
		serialPort = new SerialPort(9600, SerialPort.Port.kUSB);
	}
    public void initDefaultCommand() {
        // Set the default command for a subsystem here.
        //setDefaultCommand(new MySpecialCommand());
    }
    public void sendOn(){
    	serialPort.writeString("on");
    }
    public void sendOff(){
    	serialPort.writeString("off");
    }
    public void sendString(String message){
    	serialPort.writeString(message);
    }
}

