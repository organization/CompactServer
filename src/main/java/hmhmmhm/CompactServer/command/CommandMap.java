package hmhmmhm.CompactServer.command;

import java.util.LinkedHashMap;

public class CommandMap {
	private LinkedHashMap<String, Command> commandMap = new LinkedHashMap<String, Command>();

	public CommandMap() {
		this.loadDefaultCommands();
	}

	public void registerCommand(Command command) {
		commandMap.put(command.commandName, command);
	}

	public boolean dispatchCommand(String commandName, String[] args) {
		if (!commandMap.containsKey(commandName))
			return false;
		return commandMap.get(commandName).onCommand(commandName, args);
	}

	public void loadDefaultCommands() {
		this.registerCommand(new DefaultShutdownCommand());
	}
}
