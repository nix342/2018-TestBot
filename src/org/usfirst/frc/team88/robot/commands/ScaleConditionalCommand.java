package org.usfirst.frc.team88.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.ConditionalCommand;

/**
 *
 */
public class ScaleConditionalCommand extends ConditionalCommand {

    public ScaleConditionalCommand(Command leftScaleCommand, Command rightScaleCommand) {
        super(leftScaleCommand, rightScaleCommand);
    }

    protected boolean condition() {
    	String gameData = DriverStation.getInstance().getGameSpecificMessage();
    	
    	return gameData.charAt(1) == 'L';
    }

}
