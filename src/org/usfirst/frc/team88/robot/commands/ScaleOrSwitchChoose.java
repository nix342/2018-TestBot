package org.usfirst.frc.team88.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.ConditionalCommand;

/**
 *
 */
public class ScaleOrSwitchChoose extends ConditionalCommand {

    public ScaleOrSwitchChoose() {
        super(new RightSideSwitch(), new ScaleOrSwitchChoose2());
    }

    
    protected boolean condition() {
    	String gameData = DriverStation.getInstance().getGameSpecificMessage();
    	if(gameData.charAt(0) == 'R'){
    		return true;
    	}
    	
    	return false;
    }

}
