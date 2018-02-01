package org.usfirst.frc.team88.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.ConditionalCommand;

/**
 *
 */
public class LeftSideScaleOrSwitchChoose2 extends ConditionalCommand {

    public LeftSideScaleOrSwitchChoose2() {
        super(new LeftSideScale(), new LeftSideL());
    }

    
    protected boolean condition() {
    	String gameData = DriverStation.getInstance().getGameSpecificMessage();
    	if(gameData.charAt(1) == 'L'){
    		return true;
    	}
    	
    	return false;
    }

}
