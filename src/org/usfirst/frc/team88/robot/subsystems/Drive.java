package org.usfirst.frc.team88.robot.subsystems;

import org.usfirst.frc.team88.robot.Robot;
import org.usfirst.frc.team88.robot.RobotMap;
import org.usfirst.frc.team88.robot.commands.DriveSplitArcade;
import org.usfirst.frc.team88.robot.commands.DriveTank;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 * <pre>
 * Robot flies around 
 * forwards, backwards, left and right 
 * watch us as we go
 * </pre>
 */
public class Drive extends Subsystem implements PIDOutput {

	private final static boolean CAN_CLOSED_LOOP = true;
	private final static boolean SPLIT_ARCADE = true;

	private final static int SLOTIDX = 0;
	private final static int TIMEOUTMS = 0;
	private final static double RAMPRATE = .4;
	private final static double MAX_RAMPRATE = 2.0;
	private final static double MIN_RAMPRATE = .30;
	private final static double MAX_SPEED = 13000;
	private final static double P = 0.03;
	private final static double I = 0.0;
	private final static double D = 0.0;
	private final static double F = 1023 / MAX_SPEED;
	private final static double DFT_SENSITIVITY = 0.15;
	private final static double ROTATE_P = 0.0030;
	private final static double ROTATE_I = 0.0004;
	private final static double ROTATE_D = 0.0;
	private final static double ROTATE_F = 0.0;
	private final static double ROTATE_TOLERANCE = 3.0;
	private final static double ROTATE_MAX = 0.15;
	private final static double ROTATE_MIN = 0.05;

	private AHRS navX;
	private TalonSRX leftTalon;
	private TalonSRX rightTalon;
	private VictorSPX[] leftVictors;
	private VictorSPX[] rightVictors;

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
		leftTalon = new TalonSRX(RobotMap.leftTalonMaster);
		rightTalon = new TalonSRX(RobotMap.rightTalonMaster);

		// init Victors
		leftVictors = new VictorSPX[RobotMap.leftFollowers.length];
		rightVictors = new VictorSPX[RobotMap.rightFollowers.length];

		for (int i = 0; i < RobotMap.leftFollowers.length; i++) {
			leftVictors[i] = new VictorSPX(RobotMap.leftFollowers[i]);
		}

		for (int i = 0; i < RobotMap.rightFollowers.length; i++) {
			rightVictors[i] = new VictorSPX(RobotMap.rightFollowers[i]);
		}

		leftTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, SLOTIDX, TIMEOUTMS);
		leftTalon.config_kP(SLOTIDX, P, TIMEOUTMS);
		leftTalon.config_kI(SLOTIDX, I, TIMEOUTMS);
		leftTalon.config_kD(SLOTIDX, D, TIMEOUTMS);
		leftTalon.config_kF(SLOTIDX, F, TIMEOUTMS);
		leftTalon.configNominalOutputForward(0.0, TIMEOUTMS);
		leftTalon.configNominalOutputReverse(0.0, TIMEOUTMS);
		leftTalon.configPeakOutputForward(+0.5, TIMEOUTMS);
		leftTalon.configPeakOutputReverse(-0.5, TIMEOUTMS);
		leftTalon.configNeutralDeadband(0.04, TIMEOUTMS);
		leftTalon.configClosedloopRamp(RAMPRATE, TIMEOUTMS);
		leftTalon.setNeutralMode(NeutralMode.Brake);
		leftTalon.setSensorPhase(false);

		for (int i = 0; i < RobotMap.leftFollowers.length; i++) {
			// leftTalons[i].setInverted(false);
			leftVictors[i].setNeutralMode(NeutralMode.Brake);
			//leftVictors[i].set(ControlMode.Follower, RobotMap.leftTalonMaster);
			leftVictors[i].follow(leftTalon);
		}

		rightTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative, SLOTIDX, TIMEOUTMS);
		rightTalon.config_kP(SLOTIDX, P, TIMEOUTMS);
		rightTalon.config_kI(SLOTIDX, I, TIMEOUTMS);
		rightTalon.config_kD(SLOTIDX, D, TIMEOUTMS);
		rightTalon.config_kF(SLOTIDX, F, TIMEOUTMS);
		rightTalon.configNominalOutputForward(0.0, TIMEOUTMS);
		rightTalon.configNominalOutputReverse(0.0, TIMEOUTMS);
		rightTalon.configPeakOutputForward(+0.5, TIMEOUTMS);
		rightTalon.configPeakOutputReverse(-0.5, TIMEOUTMS);
		rightTalon.configNeutralDeadband(0.04, TIMEOUTMS);
		rightTalon.configClosedloopRamp(RAMPRATE, TIMEOUTMS);
		rightTalon.setNeutralMode(NeutralMode.Brake);
		rightTalon.setSensorPhase(false);

		for (int i = 0; i < RobotMap.rightFollowers.length; i++) {
			// rightTalons[i].setInverted(false);
			rightVictors[i].setNeutralMode(NeutralMode.Brake);
			//rightVictors[i].set(ControlMode.Follower, RobotMap.rightTalonMaster);
			rightVictors[i].follow(rightTalon);
		}

		resetEncoders();
		navX.zeroYaw();
		count = 0;
		stabilize = false;
	}

	public void wheelSpeed(double left, double right) {

		// double ramprate = MAX_RAMPRATE * Robot.lift.getHeight() +
		// MIN_RAMPRATE;

		// leftTalon.configClosedloopRamp(ramprate,TIMEOUTMS);
		// rightTalon.configClosedloopRamp(ramprate, TIMEOUTMS);

		if (CAN_CLOSED_LOOP) {
			SmartDashboard.putNumber("Left 1w:", -left * MAX_SPEED);
			SmartDashboard.putNumber("Right WheelSpeed:", right * MAX_SPEED);

			leftTalon.set(ControlMode.Velocity, -left * MAX_SPEED);
			rightTalon.set(ControlMode.Velocity, right * MAX_SPEED);
		} else {
			SmartDashboard.putNumber("Left WheelSpeed:", left);
			SmartDashboard.putNumber("Right WheelSpeed:", right);

			leftTalon.set(ControlMode.PercentOutput, -left);
			rightTalon.set(ControlMode.PercentOutput, right);
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

	public void zeroYaw() {
		navX.zeroYaw();
	}

	public void resetEncoders() {
		leftTalon.getSensorCollection().setQuadraturePosition(0, TIMEOUTMS);
		rightTalon.getSensorCollection().setQuadraturePosition(0, TIMEOUTMS);
	}

	public void enableRampRate() {
		leftTalon.configClosedloopRamp(RAMPRATE, TIMEOUTMS);
		rightTalon.configClosedloopRamp(RAMPRATE, TIMEOUTMS);
	}

	public void disableRampRate() {
		leftTalon.configClosedloopRamp(0, TIMEOUTMS);
		rightTalon.configClosedloopRamp(0, TIMEOUTMS);
	}

	public int getLeftEncPosition() {
		return leftTalon.getSelectedSensorPosition(SLOTIDX);
	}

	public int getRightEncPosition() {
		return rightTalon.getSelectedSensorPosition(SLOTIDX);
	}

	public double getAvgPosition() {
		return (-leftTalon.getSelectedSensorPosition(SLOTIDX) + rightTalon.getSelectedSensorPosition(SLOTIDX)) / 2.0;
	}

	public double getAvgSpeed() {
		double speed = (-leftTalon.getSelectedSensorVelocity(SLOTIDX) + rightTalon.getSelectedSensorVelocity(SLOTIDX))
				/ 2;

		return speed;
	}

	public void updateDashboard() {
		// waiting to be fixed
		SmartDashboard.putNumber("Left Position: ", leftTalon.getSelectedSensorPosition(SLOTIDX));
		SmartDashboard.putNumber("Left Velocity: ", leftTalon.getSelectedSensorVelocity(SLOTIDX));
		SmartDashboard.putNumber("Left Error: ", leftTalon.getClosedLoopError(SLOTIDX));

		SmartDashboard.putNumber("Right Position: ", rightTalon.getSelectedSensorPosition(SLOTIDX));
		SmartDashboard.putNumber("Right Velocity: ", rightTalon.getSelectedSensorVelocity(SLOTIDX));
		SmartDashboard.putNumber("Right Error: ", rightTalon.getClosedLoopError(SLOTIDX));

		SmartDashboard.putNumber("AvgPosition", getAvgPosition());
		SmartDashboard.putNumber("Yaw", navX.getYaw());

		SmartDashboard.putNumber("Lift Height", Robot.lift.getHeight());

		SmartDashboard.putNumber("LeftTalonCurrent", leftTalon.getOutputCurrent());
		SmartDashboard.putNumber("RightTalonCurrent", rightTalon.getOutputCurrent());

		for (int i = 0; i < RobotMap.leftFollowers.length; i++) {
			SmartDashboard.putNumber("LeftCurrent" + i, leftVictors[i].getOutputCurrent());
			SmartDashboard.putNumber("LeftVoltage" + i, leftVictors[i].getMotorOutputVoltage());
		}
		for (int i = 0; i < RobotMap.rightFollowers.length; i++) {
			SmartDashboard.putNumber("RightCurrent" + i, rightVictors[i].getOutputCurrent());
			SmartDashboard.putNumber("RightVoltage" + i, rightVictors[i].getMotorOutputVoltage());
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
