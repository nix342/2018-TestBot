package org.usfirst.frc.team88.robot.subsystems;

import org.usfirst.frc.team88.robot.Robot;
import org.usfirst.frc.team88.robot.RobotMap;
import org.usfirst.frc.team88.robot.commands.DriveSplitArcade;
import org.usfirst.frc.team88.robot.commands.DriveTank;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * Robot flies around 
 * forwards, backwards, left and right 
 * watch us as we go
 */
public class Drive extends Subsystem implements PIDOutput {

	private final static boolean CAN_CLOSED_LOOP = true;
	private final static boolean SPLIT_ARCADE = true;
	
	private final static int SLOTIDX = 0;
	private final static int TIMEOUTMS = 0;
	private final static double RAMPRATE = .30;
	private final static double MAX_RAMPRATE = 2.0;
	private final static double MIN_RAMPRATE = .30;
	private final static double MAX_SPEED = 13000;
	private final static double P = 0.03;
	private final static double I = 0.0;
	private final static double D = 0.0;
	private final static double F = 1023/ MAX_SPEED;
	private final static double DFT_SENSITIVITY = 0.15;
	private final static double ROTATE_P = 0.0030;
	private final static double ROTATE_I = 0.0004;
	private final static double ROTATE_D = 0.0;
	private final static double ROTATE_F = 0.0;
	private final static double ROTATE_TOLERANCE = 3.0;
	private final static double ROTATE_MAX = 0.15;
	private final static double ROTATE_MIN = 0.05;	
	
	private AHRS navX;
	private TalonSRX[] leftTalons;
	private TalonSRX[] rightTalons;

	private int count;
	private double heading;
	private boolean stabilize;

	public PIDController rotateController;

	public Drive() {
		// init navX
		navX = new AHRS(SPI.Port.kMXP);

		// init rotateController
		rotateController = new PIDController(ROTATE_P, ROTATE_I, ROTATE_D, ROTATE_F, navX, this);
		rotateController.setInputRange(-180.0f, 180.0f);
		rotateController.setOutputRange(-1.0, 1.0);
		rotateController.setAbsoluteTolerance(ROTATE_TOLERANCE);
		rotateController.setContinuous(true);
		
		// init talons
		leftTalons = new TalonSRX[RobotMap.leftTalons.length];
		rightTalons = new TalonSRX[RobotMap.rightTalons.length];
		for (int i = 0; i < RobotMap.leftTalons.length; i++) {
			leftTalons[i] = new TalonSRX(RobotMap.leftTalons[i]);
		}

		for (int i = 0; i < RobotMap.rightTalons.length; i++) {
			rightTalons[i] = new TalonSRX(RobotMap.rightTalons[i]);
		}
		
		if(CAN_CLOSED_LOOP) {  // closed loop
			for (int i = 0; i < RobotMap.leftTalons.length; i++) {
				if (i == 0) {
					// leftTalons[i].changeControlMode(TalonControlMode.PercentVbus);
					//We prob need to remember our control mode since they nuked the controlMode method. ):
					// leftTalons[i].configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, SLOTIDX, 0);
					leftTalons[i].configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, SLOTIDX, TIMEOUTMS);
					
					leftTalons[i].config_kP(SLOTIDX, P, TIMEOUTMS);
					leftTalons[i].config_kI(SLOTIDX, I, TIMEOUTMS);
					leftTalons[i].config_kD(SLOTIDX, D, TIMEOUTMS);
					leftTalons[i].config_kF(SLOTIDX, F, TIMEOUTMS);
					leftTalons[i].configNominalOutputForward(0.0, TIMEOUTMS);
					leftTalons[i].configNominalOutputReverse(0.0, TIMEOUTMS);
					leftTalons[i].configPeakOutputForward(+0.83, TIMEOUTMS);
					leftTalons[i].configPeakOutputReverse(-0.83, TIMEOUTMS);
					
					leftTalons[i].setSensorPhase(false);
					//leftTalons[i].setInverted(false);
				} else {
					leftTalons[i].set(ControlMode.Follower, RobotMap.leftTalons[0]);
				}
				leftTalons[i].configNeutralDeadband(0.04, TIMEOUTMS);
				leftTalons[i].configClosedloopRamp(RAMPRATE, TIMEOUTMS);
				leftTalons[i].setNeutralMode(NeutralMode.Brake);
			}

			for (int i = 0; i < RobotMap.rightTalons.length; i++) {
				if (i == 0) {
					//rightTalons[i].changeControlMode(TalonControlMode.Speed);
					// rightTalons[i].changeControlMode(TalonControlMode.PercentVbus);
					// rightTalons[i].configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, SLOTIDX,0);
					rightTalons[i].configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, SLOTIDX, TIMEOUTMS);
					
					rightTalons[i].config_kP(SLOTIDX, P, TIMEOUTMS);
					rightTalons[i].config_kI(SLOTIDX, I, TIMEOUTMS);
					rightTalons[i].config_kD(SLOTIDX, D, TIMEOUTMS);
					rightTalons[i].config_kF(SLOTIDX, F, TIMEOUTMS);
					rightTalons[i].configNominalOutputForward(0.0, TIMEOUTMS);
					rightTalons[i].configNominalOutputReverse(0.0, TIMEOUTMS);
					rightTalons[i].configPeakOutputForward(+0.83, TIMEOUTMS);
					rightTalons[i].configPeakOutputReverse(-0.83, TIMEOUTMS);
					
					rightTalons[i].setSensorPhase(false);
					//rightTalons[i].setInverted(false);
				} else {
					rightTalons[i].set(ControlMode.Follower, RobotMap.rightTalons[0]);
				}
				rightTalons[i].configNeutralDeadband(0.04, TIMEOUTMS);
				rightTalons[i].configClosedloopRamp(RAMPRATE, TIMEOUTMS);
				rightTalons[i].setNeutralMode(NeutralMode.Brake);
			}
		}
		else { // open loop
			for (int i = 0; i < RobotMap.leftTalons.length; i++) {
				if (i == 0) {
					leftTalons[i].configNominalOutputForward(0.0, 0);
					leftTalons[i].configNominalOutputReverse(0.0, 0);
					leftTalons[i].configPeakOutputForward(+0.83, 0);
					leftTalons[i].configPeakOutputReverse(-0.83, 0);
					leftTalons[i].configOpenloopRamp(RAMPRATE, 0);
				} else {
					leftTalons[i].set(ControlMode.Follower, RobotMap.leftTalons[0]);
				}
				leftTalons[i].setSensorPhase(true);
				leftTalons[i].setNeutralMode(NeutralMode.Brake);
			}
			for (int i = 0; i < RobotMap.rightTalons.length; i++) {
				if (i == 0) {
					rightTalons[i].configNominalOutputForward(0.0, 0);
					rightTalons[i].configNominalOutputReverse(0.0, 0);
					rightTalons[i].configPeakOutputForward(+0.83, 0);
					rightTalons[i].configPeakOutputReverse(-0.83, 0);
					rightTalons[i].configOpenloopRamp(RAMPRATE, 0);
				} else {
					rightTalons[i].set(ControlMode.Follower, RobotMap.rightTalons[0]);
				}
				rightTalons[i].setSensorPhase(false);
				rightTalons[i].setNeutralMode(NeutralMode.Brake);
			}
		}

		resetEncoders();
		navX.zeroYaw();
		count = 0;
		stabilize = false;
	}

	public void wheelSpeed(double left, double right) {
		
		double ramprate = MAX_RAMPRATE * Robot.lift.getHeight() + MIN_RAMPRATE;
		
		leftTalons[0].configClosedloopRamp(ramprate,TIMEOUTMS);
		rightTalons[0].configClosedloopRamp(ramprate, TIMEOUTMS);
		
		if(CAN_CLOSED_LOOP){
			SmartDashboard.putNumber("Left 1w:", -left * MAX_SPEED);
			SmartDashboard.putNumber("Right WheelSpeed:", right * MAX_SPEED);
			
			leftTalons[0].set(ControlMode.Velocity, -left * MAX_SPEED);
			rightTalons[0].set(ControlMode.Velocity, right * MAX_SPEED);
		}
		else{
			SmartDashboard.putNumber("Left WheelSpeed:", left);
			SmartDashboard.putNumber("Right WheelSpeed:", right);
			
			leftTalons[0].set(ControlMode.PercentOutput, -left);
			rightTalons[0].set(ControlMode.PercentOutput, right);
		}
	}

	/**
	 * The below was based on (ie, copied from) very similar code in the WPILib
	 * RobotDrive class on 1/18/2017
	 * 
	 * Drive the motors at "outputMagnitude" and "curve". Both outputMagnitude
	 * and curve are -1.0 to +1.0 values, where 0.0 represents stopped and not
	 * turning. {@literal curve < 0 will turn left
	 * and curve > 0} will turn right.
	 *
	 * <p>
	 * The algorithm for steering provides a constant turn radius for any normal
	 * speed range, both forward and backward. Increasing sensitivity causes
	 * sharper turns for fixed values of curve.
	 *
	 * <p>
	 * This function will most likely be used in an autonomous routine.
	 *
	 * @param outputMagnitude
	 *            The speed setting for the outside wheel in a turn, forward or
	 *            backwards, +1 to -1.
	 * @param curve
	 *            The rate of turn, constant for different forward speeds. Set
	 *            {@literal
	 *                        curve < 0 for left turn or curve > 0 for right turn.}
	 *            Set curve = e^(-r/w) to get a turn radius r for wheelbase w of
	 *            your robot. Conversely, turn radius r = -ln(curve)*w for a
	 *            given value of curve and wheelbase w.
	 */
	public void driveCurve(double outputMagnitude, double curve) {
		driveCurve(outputMagnitude, curve, DFT_SENSITIVITY);

	}

	public void driveCurve(double outputMagnitude, double curve, double sensitivity) {
		double leftOutput;
		double rightOutput;

		SmartDashboard.putNumber("Curve", curve);
		SmartDashboard.putNumber("Magnitude", outputMagnitude);
		SmartDashboard.putNumber("Count", count);
		SmartDashboard.putBoolean("Stabilize", stabilize);
		
		if (outputMagnitude == 0) {
			stabilize = false;
			count = 0;
		} else if (stabilize && curve == 0) {
			curve = (heading - getYaw()) * 0.008;
		} else if (stabilize) {
			stabilize = false;
			count = 0;
		}
		
		if (outputMagnitude != 0) {
			curve = curve * Math.signum(outputMagnitude);
		}
		
		if (Math.abs(outputMagnitude) < 0.10) {
			leftOutput = curve * 0.5;
			rightOutput = -curve * 0.5;
		} else if (curve < 0) {
			double value = Math.log(-curve);
			double ratio = (value - sensitivity) / (value + sensitivity);
			if (ratio == 0) {
				ratio = .0000000001;
			}
			leftOutput = outputMagnitude / ratio;
			rightOutput = outputMagnitude;
		} else if (curve > 0) {
			double value = Math.log(curve);
			double ratio = (value - sensitivity) / (value + sensitivity);
			if (ratio == 0) {
				ratio = .0000000001;
			}
			leftOutput = outputMagnitude;
			rightOutput = outputMagnitude / ratio;
		} else {
			if (count++ > 4) {
				stabilize = true;
				heading = getYaw();
			}
			
			leftOutput = outputMagnitude;
			rightOutput = outputMagnitude;
		}

		wheelSpeed(leftOutput, rightOutput);
	}

	public double getYaw() {
		return navX.getYaw();
	}
	
	public void resetEncoders() {
		leftTalons[0].getSensorCollection().setQuadraturePosition(0, TIMEOUTMS);
		rightTalons[0].getSensorCollection().setQuadraturePosition(0, TIMEOUTMS);
	}

	public void enableRampRate() {
		leftTalons[0].configClosedloopRamp(RAMPRATE, TIMEOUTMS);
		rightTalons[0].configClosedloopRamp(RAMPRATE, TIMEOUTMS);
	}	
	
	public void disableRampRate() {
		leftTalons[0].configClosedloopRamp(0, TIMEOUTMS);
		rightTalons[0].configClosedloopRamp(0, TIMEOUTMS);
	}

	public int getLeftEncPosition() {
		return leftTalons[0].getSelectedSensorPosition(SLOTIDX);
	}

	public int getRightEncPosition() {
		return rightTalons[0].getSelectedSensorPosition(SLOTIDX);
	}

	public double getAvgPosition() {
		return (-leftTalons[0].getSelectedSensorPosition(SLOTIDX) + rightTalons[0].getSelectedSensorPosition(SLOTIDX)) / 2.0;
	}

	public double getAvgSpeed() {
		double speed = (-leftTalons[0].getSelectedSensorVelocity(SLOTIDX) + rightTalons[0].getSelectedSensorVelocity(SLOTIDX)) / 2;

		return speed;
	}

	public void updateDashboard() {
		//waiting to be fixed
		SmartDashboard.putNumber("Left Position: ", leftTalons[0].getSelectedSensorPosition(SLOTIDX));
		SmartDashboard.putNumber("Left Velocity: ", leftTalons[0].getSelectedSensorVelocity(SLOTIDX));
		SmartDashboard.putNumber("Left Error: ", leftTalons[0].getClosedLoopError(SLOTIDX));

		SmartDashboard.putNumber("Right Position: ", rightTalons[0].getSelectedSensorPosition(SLOTIDX));
		SmartDashboard.putNumber("Right Velocity: ", rightTalons[0].getSelectedSensorVelocity(SLOTIDX));
		SmartDashboard.putNumber("Right Error: ", rightTalons[0].getClosedLoopError(SLOTIDX));

		SmartDashboard.putNumber("AvgPosition", getAvgPosition());
		SmartDashboard.putNumber("Yaw", navX.getYaw());
		
		SmartDashboard.putNumber("Lift Height", Robot.lift.getHeight());

		for (int i = 0; i < RobotMap.leftTalons.length; i++) {
			SmartDashboard.putNumber("LeftCurrent" + i, leftTalons[i].getOutputCurrent());
			SmartDashboard.putNumber("LeftVoltage" + i, leftTalons[i].getMotorOutputVoltage());
		}
		for (int i = 0; i < RobotMap.rightTalons.length; i++) {
			SmartDashboard.putNumber("RightCurrent" + i, rightTalons[i].getOutputCurrent());
			SmartDashboard.putNumber("RightVoltage" + i, rightTalons[i].getMotorOutputVoltage());
		}
	}

	@Override
	public void pidWrite(double output) {
		if (output > ROTATE_MAX) {
			output = ROTATE_MAX;
		} else if (output > ROTATE_MIN) {
			// no change
		} else if (output > 0) {
			output = ROTATE_MIN;
		} else if (output == 0) {
			output = 0;
		} else if (output > (0 - ROTATE_MIN)) {
			output = 0 - ROTATE_MIN;
		} else if (output < (0 - ROTATE_MAX)) {
			output = 0 - ROTATE_MAX;
		}

		wheelSpeed(output, -output);

		updateDashboard();
	}

	public void initDefaultCommand() {
		if (SPLIT_ARCADE) {
			setDefaultCommand(new DriveSplitArcade());
		} else {
			setDefaultCommand(new DriveTank());
		}
	}
}
