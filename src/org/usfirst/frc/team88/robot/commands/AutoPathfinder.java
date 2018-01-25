package org.usfirst.frc.team88.robot.commands;

import org.usfirst.frc.team88.robot.Robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import jaci.pathfinder.*;
import jaci.pathfinder.followers.EncoderFollower;
import jaci.pathfinder.modifiers.TankModifier;

/**
 *
 */
public class AutoPathfinder extends Command {
	private static final double TIME_DELTA = 0.020; // 20ms between points
	private static final double MAX_VELOCITY = 1.7;
	private static final double MAX_ACCELERATION = 2.0;
	private static final double MAX_JERK = 60.0;

	// states
	private static final int PREP_SENSORS = 0;
	private static final int CONFIG_PATHFINDER = 10;
	private static final int MOVE = 20;
	private static final int STOP = 30;
	private static final int END = 40;

	// All dimensions below are in inches
	private static final double ROBOT_WHEELBASE_WIDTH = 26.0;
	private static final double ROBOT_WHEEL_DIAMETER = 4.0;
	private static final double ROBOT_WIDTH = 39.0;
	private static final double ROBOT_LENGTH = 34.0;
	private static final int ROBOT_ENCODER_COUNTS_PER_REV = 4096;

	private static final double FIELD_WIDTH = 324.0;
	private static final double FIELD_EXCHANGE_OFFSET = 12.0;
	private static final double FIELD_ALLIANCE_WALL_TO_SWITCH = 140.0;
	private static final double FIELD_SIDE_WALL_TO_SWITCH = 85.25;

	private TankModifier trajectoryLeft, trajectoryRight;
	private EncoderFollower left, right;
	private int state;

	public AutoPathfinder() {
		requires(Robot.drive);

		// Calculate the trajectory for potential paths
		double destinationX = FIELD_ALLIANCE_WALL_TO_SWITCH - ROBOT_LENGTH;
		double destinationYRight = (FIELD_WIDTH / 2) + FIELD_EXCHANGE_OFFSET - FIELD_SIDE_WALL_TO_SWITCH - ROBOT_WIDTH;
		double destinationYLeft = -(FIELD_WIDTH / 2) + FIELD_EXCHANGE_OFFSET + FIELD_SIDE_WALL_TO_SWITCH;

		Trajectory.Config config = new Trajectory.Config(Trajectory.FitMethod.HERMITE_CUBIC,
				Trajectory.Config.SAMPLES_HIGH, TIME_DELTA, MAX_VELOCITY, MAX_ACCELERATION, MAX_JERK);
		Waypoint[] pointsRight = new Waypoint[] { new Waypoint(0, 0, 0),
				new Waypoint(destinationX, destinationYRight, 0) };
		Waypoint[] pointsLeft = new Waypoint[] { new Waypoint(0, 0, 0),
				new Waypoint(destinationX, destinationYLeft, 0) };

		trajectoryRight = new TankModifier(Pathfinder.generate(pointsRight, config)).modify(ROBOT_WHEELBASE_WIDTH);
		trajectoryLeft = new TankModifier(Pathfinder.generate(pointsLeft, config)).modify(ROBOT_WHEELBASE_WIDTH);
	}

	// Called just before this Command runs the first time
	protected void initialize() {
		// generate followers now that we know where to go
		String gameData = DriverStation.getInstance().getGameSpecificMessage();

		if (gameData.charAt(0) == 'L') {
			left = new EncoderFollower(trajectoryLeft.getLeftTrajectory());
			right = new EncoderFollower(trajectoryLeft.getRightTrajectory());
		} else if (gameData.charAt(0) == 'R') {
			left = new EncoderFollower(trajectoryRight.getLeftTrajectory());
			right = new EncoderFollower(trajectoryRight.getRightTrajectory());
		}

		state = PREP_SENSORS;
	}

	// Called repeatedly when this Command is scheduled to run
	protected void execute() {

		switch (state) {
		case PREP_SENSORS:
			Robot.drive.resetEncoders();
			Robot.drive.zeroYaw();

			if (Math.abs(Robot.drive.getAvgPosition()) < 100 && Math.abs(Robot.drive.getYaw()) < 1.0) {
				state = CONFIG_PATHFINDER;
			}

			break;
		case CONFIG_PATHFINDER:
			left.configureEncoder(Robot.drive.getLeftEncPosition(), ROBOT_ENCODER_COUNTS_PER_REV, ROBOT_WHEEL_DIAMETER);
			right.configureEncoder(Robot.drive.getRightEncPosition(), ROBOT_ENCODER_COUNTS_PER_REV,
					ROBOT_WHEEL_DIAMETER);

			// The first argument is the proportional gain. Usually this will be quite high
			// The second argument is the integral gain. This is unused for motion profiling
			// The third argument is the derivative gain. Tweak this if you are unhappy with
			// the tracking of the trajectory
			// The fourth argument is the velocity ratio. This is 1 over the maximum
			// velocity you provided in the
			// trajectory configuration (it translates m/s to a -1 to 1 scale that your
			// motors can read)
			// The fifth argument is your acceleration gain. Tweak this if you want to get
			// to a higher or lower speed quicker
			left.configurePIDVA(1.0, 0.0, 0.0, 1 / MAX_VELOCITY, 0);
			right.configurePIDVA(1.0, 0.0, 0.0, 1 / MAX_VELOCITY, 0);

			state = MOVE;
			break;
		case MOVE:
			double outputLeft = left.calculate(Robot.drive.getLeftEncPosition());
			double outputRight = right.calculate(Robot.drive.getRightEncPosition());

			// we still need heading correction
			// note, we invert the yaw below to match the pathfinder coordinate system
			double gyro_heading = -Robot.drive.getYaw();
			double desired_heading = Pathfinder.r2d(left.getHeading());
			double angleDifference = Pathfinder.boundHalfDegrees(desired_heading - gyro_heading);
			
			double turn = 0.8 * (-1.0/80.0) * angleDifference;

			Robot.drive.wheelSpeed(outputLeft + turn, outputRight - turn);

			if (outputLeft == 0 && outputRight == 0) {
				state = STOP;
			}
			break;
		case STOP:
			Robot.drive.wheelSpeed(0, 0);
			state = END;
			break;
		}
	}

	// Make this return true when this Command no longer needs to run execute()
	protected boolean isFinished() {
		return state == END;
	}

	// Called once after isFinished returns true
	protected void end() {
		Robot.drive.wheelSpeed(0, 0);
	}

	// Called when another command which requires one or more of the same
	// subsystems is scheduled to run
	protected void interrupted() {
		Robot.drive.wheelSpeed(0, 0);
	}
}
