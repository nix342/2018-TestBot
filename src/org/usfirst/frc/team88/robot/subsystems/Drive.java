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
 * Robot flies around forwards, backwards, left and right watch us as we go
 */
public class Drive extends Subsystem {

	// Put methods for controlling this subsystem
	// here. Call these from Commands.
	private CANTalon leftTalon1, leftTalon2, leftTalon3, leftTalon4;
	private CANTalon rightTalon1, rightTalon2, rightTalon3, rightTalon4;
	private DoubleSolenoid shifter;
	private final static int PROFILE = 0;
	// private final static double P = 1.5;
	private final static double P = 0.8;
	private final static double I = 0.0;
	// private final static double D = 20.0;
	private final static double D = 0.0;
	private final static double F = 1.7;
	private final static int IZONE = 0;
	public final static double DFT_SENSITIVITY = 0.15;
	private final static double RAMPRATE = 80;
	private final static double MAX_SPEED = 700;

	private CANTalon[] leftTalons;
	private CANTalon[] rightTalons;
	private CANTalon.TalonControlMode controlMode;

	private double maxSpeed;
	private double targetMaxSpeed;

	public Drive() {
		System.out.println("Before loop 1");
		leftTalons = new CANTalon[RobotMap.leftTalons.length];
		rightTalons = new CANTalon[RobotMap.rightTalons.length];

		for (int i = 0; i < RobotMap.leftTalons.length; i++) {
			leftTalons[i] = new CANTalon(RobotMap.leftTalons[i]);
		}
		for (int i = 0; i < RobotMap.rightTalons.length; i++) {
			rightTalons[i] = new CANTalon(RobotMap.rightTalons[i]);
		}

		for (int i = 0; i < RobotMap.leftTalons.length; i++) {
			if (i == 0) {
				leftTalons[i].changeControlMode(TalonControlMode.Speed);
				// leftTalons[i].changeControlMode(TalonControlMode.PercentVbus);
				leftTalons[i].setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
				leftTalons[i].configEncoderCodesPerRev(512);
				leftTalons[i].setPID(P, I, D, F, IZONE, RAMPRATE, PROFILE);
				leftTalons[i].reverseSensor(true);
				leftTalons[i].reverseOutput(true);
				leftTalons[i].configNominalOutputVoltage(+0.0f, -0.0f);
				leftTalons[i].configPeakOutputVoltage(+10.0f, -10.0f);
			} else {
				leftTalons[i].changeControlMode(TalonControlMode.Follower);
				leftTalons[i].set(RobotMap.leftTalons[0]);

			}
			leftTalons[i].enableBrakeMode(true);
		}
		System.out.println("After loop 1");
		System.out.println(RobotMap.rightTalons.length);

		for (int i = 0; i < RobotMap.rightTalons.length; i++) {
			if (i == 0) {
				rightTalons[i].changeControlMode(TalonControlMode.Speed);
				// rightTalons[i].changeControlMode(TalonControlMode.PercentVbus);
				rightTalons[i].setFeedbackDevice(CANTalon.FeedbackDevice.QuadEncoder);
				rightTalons[i].configEncoderCodesPerRev(512);
				rightTalons[i].setPID(P, I, D, F, IZONE, RAMPRATE, PROFILE);
				rightTalons[i].reverseSensor(true);
				rightTalons[i].reverseOutput(false);
				rightTalons[i].configNominalOutputVoltage(+0.0f, -0.0f);
				rightTalons[i].configPeakOutputVoltage(+10.0f, -10.0f);
			} else {
				rightTalons[i].changeControlMode(TalonControlMode.Follower);
				rightTalons[i].set(RobotMap.rightTalons[0]);

			}
			rightTalons[i].enableBrakeMode(true);
		}

		shifter = new DoubleSolenoid(RobotMap.shifterLow, RobotMap.shifterHigh);
		shifter.set(Value.kReverse);

		resetEncoders();
	}

	public void wheelSpeed(double left, double right) {
		leftTalons[0].set(-left * MAX_SPEED);
		rightTalons[0].set(-right * MAX_SPEED);
		// leftTalons[0].set(left);
		// rightTalons[0].set(-right);

	}

	public void resetEncoders() {
		leftTalons[0].setPosition(0);
		rightTalons[0].setPosition(0);
	}

	public boolean isLowGear() {
		if (shifter.get() == Value.kReverse) {
			return true;
		} else
			return false;
	}

	public int getLeftEncPosition() {
		return leftTalons[0].getEncPosition();
	}

	public int getRightEncPosition() {
		return rightTalons[0].getEncPosition();
	}

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		setDefaultCommand(new DriveTank());
	}

	public double getAvgPosition() {
		return (leftTalons[0].getPosition() + rightTalons[0].getPosition()) / 2.0;
	}

	public double getAvgSpeed() {
		double speed = (leftTalons[0].getSpeed() + rightTalons[0].getSpeed()) / 2;

		return speed;
	}

	public void shift() {

		// SECRET COMMENT - KYLE H
		if (shifter.get() == (Value.kForward)) {
			shifter.set(Value.kReverse);
		} else {
			shifter.set(Value.kForward);
		}
	}

	public void updateDashboard() {
		SmartDashboard.putNumber("LeftError: ", leftTalons[0].getError());
		SmartDashboard.putNumber("LeftSetpoint: ", leftTalons[0].getSetpoint());
		SmartDashboard.putNumber("LeftEncPosition: ", leftTalons[0].getEncPosition());
		SmartDashboard.putNumber("LeftPosition: ", leftTalons[0].getPosition());
		SmartDashboard.putNumber("LeftSpeed: ", leftTalons[0].getSpeed());
		SmartDashboard.putNumber("LeftEncVel:", leftTalons[0].getEncVelocity());
		SmartDashboard.putNumber("RightError: ", rightTalons[0].getError());
		SmartDashboard.putNumber("RightSetpoint: ", rightTalons[0].getSetpoint());
		SmartDashboard.putNumber("RightEncPosition: ", rightTalons[0].getEncPosition());
		SmartDashboard.putNumber("RightPosition: ", rightTalons[0].getPosition());
		SmartDashboard.putNumber("RightSpeed: ", rightTalons[0].getSpeed());
		SmartDashboard.putNumber("RightEncVel:", rightTalons[0].getEncVelocity());
		SmartDashboard.putBoolean("LowGear:", isLowGear());
		SmartDashboard.putNumber("AvgPosition", getAvgPosition());
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
