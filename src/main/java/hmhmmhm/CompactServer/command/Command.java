package hmhmmhm.CompactServer.command;

public abstract class Command {
	String commandName;
	String commandUsage;
	String commandDescription;

	public abstract boolean onCommand(String commandName, String[] args);

	public void load(String commandName, String commandUsage, String commandDescription) {
		this.commandName = commandName;
		this.commandUsage = commandUsage;
		this.commandDescription = commandDescription;
	}
}
