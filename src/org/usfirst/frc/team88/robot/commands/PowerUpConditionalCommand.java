package org.usfirst.frc.team88.robot.commands;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.command.ConditionalCommand;

public class PowerUpConditionalCommand extends ConditionalCommand {

	public PowerUpConditionalCommand(Command LLCommand, Command LRCommand, Command RLCommand, Command RRCommand) {
		super(new ScaleConditionalCommand(LLCommand, LRCommand), new ScaleConditionalCommand(RLCommand, RRCommand));
	}

	protected boolean condition() {
		String gameData = DriverStation.getInstance().getGameSpecificMessage();

		return gameData.charAt(0) == 'L';
	}

	private static class ScaleConditionalCommand extends ConditionalCommand {

		public ScaleConditionalCommand(Command leftSwitchCommand, Command rightSwitchCommand) {
			super(leftSwitchCommand, rightSwitchCommand);
		}

		protected boolean condition() {
			String gameData = DriverStation.getInstance().getGameSpecificMessage();

			return gameData.charAt(1) == 'L';
		}

	}
}
