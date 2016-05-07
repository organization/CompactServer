package hmhmmhm.CompactServer.command;

import hmhmmhm.CompactServer.CompactServer;
import hmhmmhm.CompactServer.MainLogger;

public class DefaultShutdownCommand extends Command {
	public DefaultShutdownCommand() {
		super.load("stop", "/stop", "Shutdown server");
	}

	@Override
	public boolean onCommand(String commandName, String[] args) {
		MainLogger.info("SERVER STOP...");
		CompactServer.getInstance().shutdown();
		return true;
	}
}