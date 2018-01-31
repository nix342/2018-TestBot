package org.usfirst.frc.team88.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.ConditionalCommand;

/**
 *
 */
public class RightSideScaleOrSwitchChoose extends ConditionalCommand {

    public RightSideScaleOrSwitchChoose() {
        super(new RightSideSwitch(), new RightSideScaleOrSwitchChoose2());
    }

    
    protected boolean condition() {
    	String gameData = DriverStation.getInstance().getGameSpecificMessage();
    	if(gameData.charAt(0) == 'R'){
    		return true;
    	}
    	
    	return false;
    }

}
