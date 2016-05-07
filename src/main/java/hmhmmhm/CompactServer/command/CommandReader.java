package hmhmmhm.CompactServer.command;

import java.io.IOException;

import hmhmmhm.CompactServer.CompactServer;
import hmhmmhm.CompactServer.MainLogger;
import jline.console.ConsoleReader;
import jline.console.CursorBuffer;

public class CommandReader extends Thread {

	private ConsoleReader reader;

	public static CommandReader instance;

	private CursorBuffer stashed;

	private boolean running = true;

	public static CommandReader getInstance() {
		return instance;
	}

	public CommandReader() {
		if (instance != null) {
			throw new RuntimeException("Command Reader is already exist");
		}
		try {
			this.reader = new ConsoleReader();
			reader.setPrompt("> ");
			instance = this;
		} catch (IOException e) {
			MainLogger.error("Unable to start ConsoleReader");
			MainLogger.logException(e);
		}
		this.setName("Console");
	}

	public String readLine() {
		try {
			reader.resetPromptLine("", "", 0);
			return this.reader.readLine("> ");
		} catch (IOException e) {
			MainLogger.logException(e);
			return "";
		}
	}

	public void run() {
		Long lastLine = System.currentTimeMillis();
		while (this.running) {
			String line = readLine();

			if (line != null && !line.trim().equals("")) {
				try {
					CompactServer.getInstance().dispatchCommand(line);
				} catch (Exception e) {
					MainLogger.logException(e);
				}

			} else if (System.currentTimeMillis() - lastLine <= 1) {
				try {
					sleep(40);
				} catch (InterruptedException e) {
					MainLogger.logException(e);
				}
			}
			lastLine = System.currentTimeMillis();
		}
	}

	public void shutdown() {
		this.running = false;
	}

	public void stashLine() {
		this.stashed = reader.getCursorBuffer().copy();
		try {
			reader.getOutput().write("\u001b[1G\u001b[K");
			reader.flush();
		} catch (IOException e) {
		}
	}

	public void unstashLine() {
		try {
			reader.resetPromptLine("> ", this.stashed.toString(), this.stashed.cursor);
		} catch (IOException e) {
		}
	}

	public void removePromptLine() {
		try {
			reader.resetPromptLine("", "", 0);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
