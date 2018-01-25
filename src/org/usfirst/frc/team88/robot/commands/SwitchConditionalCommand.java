package org.usfirst.frc.team88.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.ConditionalCommand;

/**
 *
 */
public class SwitchConditionalCommand extends ConditionalCommand {

    public SwitchConditionalCommand(Command leftSwitchCommand, Command rightSwitchCommand) {
        super(leftSwitchCommand, rightSwitchCommand);
    }

    protected boolean condition() {
    	String gameData = DriverStation.getInstance().getGameSpecificMessage();
    	
    	return gameData.charAt(0) == 'L';
    }

}
