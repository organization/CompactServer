package hmhmmhm.CompactServer;

import hmhmmhm.CompactServer.command.CommandMap;
import hmhmmhm.CompactServer.command.CommandReader;

public class CompactServer extends TickProcessor {
	private static CompactServer instance = null;
	private CommandReader console;
	private CommandMap commandMap;

	public static final String serverName = "CompactServer";

	public void init() {
		if (instance == null)
			instance = this;

		this.console = new CommandReader();
		this.console.start();

		this.commandMap = new CommandMap();
	}

	@Override
	public void process(int tickCounter, long tickTime) {

	}

	@Override
	public void overloadProcess(int tickCounter, long tickTime) {

	}

	public void dispatchCommand(String line) {
		if (line.charAt(0) == '/')
			line = line.split("\\/")[1];

		String[] command = line.split(" ");
		if (command.length > 0) {
			String commandName = command[0];
			String[] args = new String[command.length - 1];

			if (command.length > 1)
				for (int i = 1; i >= (command.length - 1); i++)
					args[i] = command[i];

			if (!this.commandMap.dispatchCommand(commandName, args))
				MainLogger.error("NOT FOUND COMMAND USE /HELP COMMAND");
		}
	}

	public void shutdown() {
		this.console.shutdown();
		super.shutdown();
	}

	public static CompactServer getInstance() {
		return instance;
	}

	// TODO UDP HANDSHAKE
	// TODO UDP TARNSMIT

	// TODO POOL
	// TODO ASYNCTASK

	// TODO UDP PING ALIVE CHECK
	// TODO UDP FAULT TORL
}
