package org.usfirst.frc.team88.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.ConditionalCommand;
import edu.wpi.first.wpilibj.command.InstantCommand;

/**
 *
 */
public class ScaleOrSwitchChoose extends ConditionalCommand {

	private String gameData;
	
    public ScaleOrSwitchChoose() {
        super(new RightSideSwitch(), new ScaleOrSwitchChoose2());
        // Use requires() here to declare subsystem dependencies
        // eg. requires(chassis);
    }

    
    protected boolean condition() {
    	gameData = DriverStation.getInstance().getGameSpecificMessage();
    	if(gameData.charAt(0) == 'R'){
    		return true;
    	}
    	
    	return false;
    }

}
