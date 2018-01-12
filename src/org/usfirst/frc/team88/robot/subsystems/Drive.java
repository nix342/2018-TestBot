package org.usfirst.frc.team88.robot.subsystems;

import org.usfirst.frc.team88.robot.RobotMap;
import org.usfirst.frc.team88.robot.commands.DriveTank;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.RemoteFeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

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

	private final static int PROFILE = 0;
	// private final static double P = 1.5;
	private final static int SLOTIDX = 0;
	private final static int TIMEOUTMS = 0;
	private final static double P = 0.8;
	private final static double I = 0.0;
	// private final static double D = 20.0;
	private final static double D = 0.0;
	private final static double F = 1.7;
	private final static int IZONE = 0;
	private final static double RAMPRATE = 80;
	private final static double MAX_SPEED = 700;
	private final static boolean CAN_SHIFT = false;
	private final static boolean CAN_CLOSED_LOOP = false;

	private TalonSRX[] leftTalons;
	private TalonSRX[] rightTalons;
	private DoubleSolenoid shifter;

	public Drive() {
		leftTalons = new TalonSRX[RobotMap.leftTalons.length];
		rightTalons = new TalonSRX[RobotMap.rightTalons.length];

		for (int i = 0; i < RobotMap.leftTalons.length; i++) {
			leftTalons[i] = new TalonSRX(RobotMap.leftTalons[i]);
		}

		for (int i = 0; i < RobotMap.rightTalons.length; i++) {
			rightTalons[i] = new TalonSRX(RobotMap.rightTalons[i]);
		}
		if(CAN_CLOSED_LOOP){
			for (int i = 0; i < RobotMap.leftTalons.length; i++) {
				if (i == 0) {
					// leftTalons[i].changeControlMode(TalonControlMode.PercentVbus);
					//We prob need to remember our control mode since they nuked the controlMode method. ):
					leftTalons[i].configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, SLOTIDX, 0);
					
					leftTalons[i].config_kP(SLOTIDX, P, TIMEOUTMS);
					leftTalons[i].config_kI(SLOTIDX, I, TIMEOUTMS);
					leftTalons[i].config_kD(SLOTIDX, D, TIMEOUTMS);
					leftTalons[i].config_kF(SLOTIDX, F, TIMEOUTMS);
					
					leftTalons[i].setSensorPhase(true);
					leftTalons[i].setInverted(false);
					leftTalons[i].configNominalOutputForward(0.0, 0);
					leftTalons[i].configNominalOutputReverse(0.0, 0);
					leftTalons[i].configPeakOutputForward(+10.0, 0);
					leftTalons[i].configPeakOutputReverse(-10.0, 0);
				} else {
					leftTalons[i].set(ControlMode.Follower, RobotMap.leftTalons[0]);
				}
				leftTalons[i].setNeutralMode(NeutralMode.Brake);
			}


			for (int i = 0; i < RobotMap.rightTalons.length; i++) {
				if (i == 0) {
					//rightTalons[i].changeControlMode(TalonControlMode.Speed);
					// rightTalons[i].changeControlMode(TalonControlMode.PercentVbus);
					rightTalons[i].configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, SLOTIDX,0);
					
					rightTalons[i].config_kP(SLOTIDX, P, TIMEOUTMS);
					rightTalons[i].config_kI(SLOTIDX, I, TIMEOUTMS);
					rightTalons[i].config_kD(SLOTIDX, D, TIMEOUTMS);
					rightTalons[i].config_kF(SLOTIDX, F, TIMEOUTMS);
					
					rightTalons[i].setSensorPhase(true);
					rightTalons[i].setInverted(false);
					rightTalons[i].configNominalOutputForward(0.0, 0);
					rightTalons[i].configNominalOutputReverse(0.0, 0);
					rightTalons[i].configPeakOutputForward(+10.0, 0);
					rightTalons[i].configPeakOutputReverse(-10.0, 0);
				} else {
					rightTalons[i].set(ControlMode.Follower, RobotMap.rightTalons[0]);
				}
				rightTalons[i].setNeutralMode(NeutralMode.Brake);
			}
			resetEncoders();
		}
		else{
			for (int i = 0; i < RobotMap.leftTalons.length; i++) {
				if (i == 0) {
					leftTalons[i].configNominalOutputForward(0.0, 0);
					leftTalons[i].configNominalOutputReverse(0.0, 0);
					leftTalons[i].configPeakOutputForward(+10.0, 0);
					leftTalons[i].configPeakOutputReverse(-10.0, 0);
					leftTalons[i].configOpenloopRamp(RAMPRATE, 0);
				} else {
					leftTalons[i].set(ControlMode.Follower, RobotMap.leftTalons[0]);
				}
				leftTalons[i].setNeutralMode(NeutralMode.Brake);
			}
			for (int i = 0; i < RobotMap.rightTalons.length; i++) {
				if (i == 0) {
					rightTalons[i].configNominalOutputForward(0.0, 0);
					rightTalons[i].configNominalOutputReverse(0.0, 0);
					rightTalons[i].configPeakOutputForward(+10.0, 0);
					rightTalons[i].configPeakOutputReverse(-10.0, 0);
					rightTalons[i].configOpenloopRamp(RAMPRATE, 0);
				} else {
					rightTalons[i].set(ControlMode.Follower, RobotMap.rightTalons[0]);
				}
				rightTalons[i].setNeutralMode(NeutralMode.Brake);
			}
		}
		if(CAN_SHIFT){
			shifter = new DoubleSolenoid(RobotMap.shifterLow, RobotMap.shifterHigh);
			shifter.set(Value.kReverse);
		}

	}

	public void wheelSpeed(double left, double right) {
		if(CAN_CLOSED_LOOP){
			leftTalons[0].set(ControlMode.Velocity, -left * MAX_SPEED);
			rightTalons[0].set(ControlMode.Velocity, -right * MAX_SPEED);
		}
		else{
			leftTalons[0].set(ControlMode.Velocity, -left);
			rightTalons[0].set(ControlMode.Velocity, right);
		}
	}

	public void resetEncoders() {
		leftTalons[0].getSensorCollection().setQuadraturePosition(0, TIMEOUTMS);
		rightTalons[0].getSensorCollection().setQuadraturePosition(0, TIMEOUTMS);
	}

	public boolean isLowGear() {
		if(CAN_SHIFT){
			if (shifter.get() == Value.kReverse) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

	public int getLeftEncPosition() {
		return leftTalons[0].getSelectedSensorPosition(SLOTIDX);
	}

	public int getRightEncPosition() {
		return rightTalons[0].getSelectedSensorPosition(SLOTIDX);
	}

	public double getAvgPosition() {
		return (leftTalons[0].getSelectedSensorPosition(SLOTIDX) + rightTalons[0].getSelectedSensorPosition(SLOTIDX)) / 2.0;
	}

	public double getAvgSpeed() {
		double speed = (leftTalons[0].getSelectedSensorVelocity(SLOTIDX) + rightTalons[0].getSelectedSensorVelocity(SLOTIDX)) / 2;

		return speed;
	}

	public void shift() {
		if(CAN_SHIFT){
			if (shifter.get() == (Value.kForward)) {
				shifter.set(Value.kReverse);
			} else {
				shifter.set(Value.kForward);
			}
		}
	}

	public void updateDashboard() {
		//waiting to be fixed
		//SmartDashboard.putNumber("LeftError: ", leftTalons[0].getError());
		//SmartDashboard.putNumber("LeftSetpoint: ", leftTalons[0].getSetpoint());
		SmartDashboard.putNumber("LeftEncPosition: ", leftTalons[0].getSelectedSensorPosition(SLOTIDX));
		SmartDashboard.putNumber("LeftPosition: ", leftTalons[0].getSelectedSensorPosition(SLOTIDX));
		SmartDashboard.putNumber("LeftSpeed: ", leftTalons[0].getSelectedSensorVelocity(SLOTIDX));
		SmartDashboard.putNumber("LeftEncVel:", leftTalons[0].getSelectedSensorVelocity(SLOTIDX));
		//SmartDashboard.putNumber("RightError: ", rightTalons[0].getError());
		//SmartDashboard.putNumber("RightSetpoint: ", rightTalons[0].getSetpoint());
		SmartDashboard.putNumber("RightEncPosition: ", rightTalons[0].getSelectedSensorPosition(SLOTIDX));
		SmartDashboard.putNumber("RightPosition: ", rightTalons[0].getSelectedSensorPosition(SLOTIDX));
		SmartDashboard.putNumber("RightSpeed: ", rightTalons[0].getSelectedSensorVelocity(SLOTIDX));
		SmartDashboard.putNumber("RightEncVel:", rightTalons[0].getSelectedSensorVelocity(SLOTIDX));
		SmartDashboard.putBoolean("LowGear:", isLowGear());
		SmartDashboard.putNumber("AvgPosition", getAvgPosition());

		for (int i = 0; i < RobotMap.leftTalons.length; i++) {
			SmartDashboard.putNumber("LeftCurrent" + i, leftTalons[i].getOutputCurrent());
			SmartDashboard.putNumber("LeftVoltage" + i, leftTalons[i].getMotorOutputVoltage());
		}
		for (int i = 0; i < RobotMap.rightTalons.length; i++) {
			SmartDashboard.putNumber("RightCurrent" + i, rightTalons[i].getOutputCurrent());
			SmartDashboard.putNumber("RightVoltage" + i, rightTalons[i].getMotorOutputVoltage());
		}
	}

	public void initDefaultCommand() {
		// Set the default command for a subsystem here.
		setDefaultCommand(new DriveTank());
	}
}
