
package org.usfirst.frc.team88.robot;

import edu.wpi.first.wpilibj.CameraServer;
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
	public static Intake intake;
	public static Lights lights;
	
	private SendableChooser<Command> chooser = new SendableChooser<>();
	private Command autonomousCommand;

	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */
	@Override
	public void robotInit() {
		
		CameraServer.getInstance().startAutomaticCapture();
		drive = new Drive();
		lift = new Lift();
		intake = new Intake();
		oi = new OI();
		lights = new Lights();
		
		// Autonomous modes
		chooser.addDefault("Cross the Line", new AutoDriveDistance(100));
		chooser.addObject("Center Switch", new AutoCenterToSwitch());
		SmartDashboard.putData("Auto mode", chooser);

		// Buttons to test commands
		SmartDashboard.putData("Auto Distance", new AutoDriveDistance(100));
		SmartDashboard.putData("Rotate to 90", new DriveRotateToAngleDouble(90));

		SmartDashboard.putData("Zero Yaw", new ZeroYaw());
		
		SmartDashboard.putData("AutoCenterToSwitch", new AutoCenterToSwitch());
		SmartDashboard.putData("Scale Further", new AutoFarScale());
		SmartDashboard.putData("Scale Or Switch Right", new RightSideScaleOrSwitchChoose());
		SmartDashboard.putData("Scale or Switch Left", new LeftSideScaleOrSwitchChoose());
		SmartDashboard.putData("Lights on", new lightsOn());
		SmartDashboard.putData("Lights off", new lightsOff());

		
		//SmartDashboard.putData("Auto Pathfinder", new AutoPathfinder());
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
		
		updateDashboard();
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
		
		updateDashboard();
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
		
		updateDashboard();
	}

	/**
	 * This function is called periodically during test mode
	 */
	@Override
	public void testPeriodic() {

	}
	
	private void updateDashboard() {
		drive.updateDashboard();
		intake.updateDashboard();

		autonomousCommand = chooser.getSelected();
		SmartDashboard.putString("Auto Command",chooser.getName());
	}
}
