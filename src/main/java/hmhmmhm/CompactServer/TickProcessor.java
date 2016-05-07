package hmhmmhm.CompactServer;

public abstract class TickProcessor {
	private long nextTick;
	private boolean isRunning = true;

	private int tickCounter;

	private float[] tickAverage = { 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20, 20 };
	private float[] useAverage = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };

	private float maxTick = 20;
	private float maxUse = 0;

	public abstract void process(int tickCounter, long tickTime);

	public abstract void overloadProcess(int tickCounter, long tickTime);

	public void run() {
		this.nextTick = System.currentTimeMillis();
		while (this.isRunning) {
			try {
				this.tick();
			} catch (RuntimeException e) {
				System.out.println(e);
			}

			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				System.out.println(e);
			}
		}
	}

	public boolean tick() {
		long tickTime = System.currentTimeMillis();
		long tickTimeNano = System.nanoTime();
		if ((tickTime - this.nextTick) < -25) {
			return false;
		}

		++this.tickCounter;

		this.process(this.tickCounter, tickTime);

		if (this.tickCounter % 100 == 0)
			if (this.getTicksPerSecondAverage() < 12)
				this.overloadProcess(this.tickCounter, tickTime);

		if ((this.tickCounter & 0b1111) == 0)
			this.titleTick();

		long nowNano = System.nanoTime();

		float tick = (float) Math.min(20, 1000000000 / Math.max(1000000, ((double) nowNano - tickTimeNano)));
		float use = (float) Math.min(1, ((double) (nowNano - tickTimeNano)) / 50000000);

		if (this.maxTick > tick)
			this.maxTick = tick;

		if (this.maxUse < use)
			this.maxUse = use;

		System.arraycopy(this.tickAverage, 1, this.tickAverage, 0, this.tickAverage.length - 1);
		this.tickAverage[this.tickAverage.length - 1] = tick;

		System.arraycopy(this.useAverage, 1, this.useAverage, 0, this.useAverage.length - 1);
		this.useAverage[this.useAverage.length - 1] = use;

		if ((this.nextTick - tickTime) < -1000) {
			this.nextTick = tickTime;
		} else {
			this.nextTick += 50;
		}
		return true;
	}

	public void titleTick() {
		Runtime runtime = Runtime.getRuntime();
		double used = round((double) (runtime.totalMemory() - runtime.freeMemory()) / 1024 / 1024, 2);
		double max = round(((double) runtime.maxMemory()) / 1024 / 1024, 2);
		String usage = Math.round(used / max * 100) + "%";
		String title = (char) 0x1b + "]0;" + CompactServer.serverName + " " + " | Memory " + usage;
		title += " | TPS " + getTicksPerSecond() + " | Load " + getTickUsage() + "%" + (char) 0x07;
		System.out.print(title);
	}

	public float getTicksPerSecond() {
		return ((float) Math.round(this.maxTick * 100)) / 100;
	}

	public float getTicksPerSecondAverage() {
		float sum = 0;
		int count = this.tickAverage.length;
		for (float aTickAverage : this.tickAverage)
			sum += aTickAverage;
		return (float) round(sum / count, 2);
	}

	public float getTickUsage() {
		return (float) round(this.maxUse * 100, 2);
	}

	public float getTickUsageAverage() {
		float sum = 0;
		int count = this.useAverage.length;
		for (float aUseAverage : this.useAverage)
			sum += aUseAverage;
		return ((float) round(sum / count * 100, 2)) / 100;
	}

	public static double round(double d, int precision) {
		return ((double) Math.round(d * Math.pow(10, precision))) / Math.pow(10, precision);
	}

	public void shutdown() {
		isRunning = false;
	}
}