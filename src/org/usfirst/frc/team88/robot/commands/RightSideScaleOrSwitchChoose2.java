package org.usfirst.frc.team88.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.ConditionalCommand;

/**
 *
 */
public class RightSideScaleOrSwitchChoose2 extends ConditionalCommand {

    public RightSideScaleOrSwitchChoose2() {
        super(new RightSideScale(), new RightSideL());
    }

    
    protected boolean condition() {
    	String gameData = DriverStation.getInstance().getGameSpecificMessage();
    	if(gameData.charAt(1) == 'R'){
    		return true;
    	}
    	
    	return false;
    }

}
