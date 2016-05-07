package hmhmmhm.CompactServer;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import hmhmmhm.CompactServer.command.CommandReader;

public class MainLogger {
	protected static void send(String message) {
		send(message, -1);
	}

	protected static void send(String message, int level) {
		Date now = new Date();
		CommandReader.getInstance().stashLine();
		System.out.println(TextFormat.toANSI(TextFormat.RED + new SimpleDateFormat("HH:mm:ss").format(now)
				+ TextFormat.RESET + " " + message + TextFormat.RESET));
		CommandReader.getInstance().unstashLine();
	}

	public static void emergency(String message) {
		send(TextFormat.RED + "[EMERGENCY] " + message);
	}

	public static void alert(String message) {
		send(TextFormat.RED + "[ALERT] " + message);
	}

	public static void critical(String message) {
		send(TextFormat.RED + "[CRITICAL] " + message);
	}

	public static void error(String message) {
		send(TextFormat.DARK_RED + "[ERROR] " + message);
	}

	public static void warning(String message) {
		send(TextFormat.YELLOW + "[WARNING] " + message);
	}

	public static void notice(String message) {
		send(TextFormat.AQUA + "[NOTICE] " + message);
	}

	public static void info(String message) {
		send(TextFormat.WHITE + "[INFO] " + message);
	}

	public static void debug(String message) {
		send(TextFormat.GRAY + "[DEBUG] " + message);
	}

	public static void logException(Exception e) {
		alert(getExceptionMessage(e));
	}

	public static String getExceptionMessage(Throwable e) {
		StringWriter stringWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(stringWriter);
		e.printStackTrace(printWriter);
		return stringWriter.toString();
	}
}