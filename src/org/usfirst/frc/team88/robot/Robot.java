
package org.usfirst.frc.team88.robot;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.Scheduler;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team88.robot.commands.*;
import org.usfirst.frc.team88.robot.subsystems.*;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.properties file in the
 * project.
 */
public class Robot extends TimedRobot {
	public static OI oi;
	public static Drive drive;
	public static Lift lift;

	private SendableChooser<Command> chooser = new SendableChooser<>();
	private Command autonomousCommand;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		drive = new Drive();
		lift = new Lift();
		oi = new OI();

		// Autonomous modes
		chooser.addDefault("Cross the Line", new AutoDriveDistance(100));
		// chooser.addObject("My Auto", new MyAutoCommand());
		SmartDashboard.putData("Auto mode", chooser);

		// Buttons to test commands
		SmartDashboard.putData("Auto Distance", new AutoDriveDistance(100));
		SmartDashboard.putData("Rotate to 90", new DriveRotateToAngleDouble(90));
		SmartDashboard.putData("Spin Test", new AutoDriveSpinTest());
		
		
		SmartDashboard.putData("Leg 1", new AutoDriveDistance(10));
		//SmartDashboard.putData("Turn 1", new AutoSwitchFirstTurn());
		SmartDashboard.putData("Leg 2", new AutoDriveDistanceAngleSmart());
		//SmartDashboard.putData("Turn 2", new DriveRotateToAngle(0));
		SmartDashboard.putData("Leg 3", new AutoDriveDistanceAngle(12, 0));
		SmartDashboard.putData("Run Command Group", new AutoSwitchAlpha());
		SmartDashboard.putData("Zero Yaw", new ZeroYaw());

		
	}

	/**
	 * This function is called once each time the robot enters Disabled mode.
	 * You can use it to reset any subsystem information you want to clear when
	 * the robot is disabled.
	 */
	@Override
	public void disabledInit() {

	}

	@Override
	public void disabledPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This autonomous (along with the chooser code above) shows how to select
	 * between different autonomous modes using the dashboard. The sendable
	 * chooser code works with the Java SmartDashboard. If you prefer the
	 * LabVIEW Dashboard, remove all of the chooser code and uncomment the
	 * getString code to get the auto name from the text box below the Gyro
	 *
	 * You can add additional auto modes by adding additional commands to the
	 * chooser code above (like the commented example) or additional comparisons
	 * to the switch structure below with additional strings & commands.
	 */
	@Override
	public void autonomousInit() {
		autonomousCommand = chooser.getSelected();

		// schedule the autonomous command (example)
		if (autonomousCommand != null) {
			autonomousCommand.start();
		}
	}

	/**
	 * This function is called periodically during autonomous
	 */
	@Override
	public void autonomousPeriodic() {
		Scheduler.getInstance().run();
	}

	@Override
	public void teleopInit() {
		// This makes sure that the autonomous stops running when
		// teleop starts running. If you want the autonomous to
		// continue until interrupted by another command, remove
		// this line or comment it out.
		if (autonomousCommand != null)
			autonomousCommand.cancel();
		drive.resetEncoders();
	}

	/**
	 * This function is called periodically during operator control
	 */
	@Override
	public void teleopPeriodic() {
		Scheduler.getInstance().run();
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {

	}
}
