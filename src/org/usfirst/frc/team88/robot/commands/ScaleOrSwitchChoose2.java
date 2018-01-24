package org.usfirst.frc.team88.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.ConditionalCommand;
import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 *
 */
public class ScaleOrSwitchChoose2 extends ConditionalCommand {

	private String gameData;
	
    public ScaleOrSwitchChoose2() {
        super(new RightSideScale(), new RightSideL());
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    
    protected boolean condition() {
    	gameData = DriverStation.getInstance().getGameSpecificMessage();
    	if(gameData.charAt(1) == 'R'){
    		return true;
    	}
    	
    	return false;
    }

}
