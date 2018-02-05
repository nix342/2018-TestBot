package org.usfirst.frc.team88.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.ConditionalCommand;

/**
 *
 */
public class LeftSideScaleOrSwitchChoose extends ConditionalCommand {

    public LeftSideScaleOrSwitchChoose() {
        super(new LeftSideSwitch(), new LeftSideScaleOrSwitchChoose2());
    }

    
    protected boolean condition() {
    	String gameData = DriverStation.getInstance().getGameSpecificMessage();
    	if(gameData.charAt(0) == 'L'){
    		return true;
    	}
    	
    	return false;
    }

}
