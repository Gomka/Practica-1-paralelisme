package e2_ts;

import java.util.concurrent.atomic.AtomicBoolean;

public class E2_TS {
	// LAUNCHER
	public static void main(String[] args) {
		final int HOW_MANY = 10;

		Ping[] thePings = new Ping[HOW_MANY];
		Pong[] thePongs = new Pong[HOW_MANY];
		Bang[] theBangs = new Bang[HOW_MANY];

		Synchronizer sync = new Synchronizer();

		for (int i = 0; i < HOW_MANY; i++) {
			thePings[i] = new Ping(i, sync);
			thePongs[i] = new Pong(i, sync);
			theBangs[i] = new Bang(i, sync);
			thePings[i].start();
			thePongs[i].start();
			theBangs[i].start();
		}

		try {
			Thread.sleep(10000);
		} catch (InterruptedException ie) {
		}

		for (int i = 0; i < HOW_MANY; i++) {
			thePings[i].stop();
			thePongs[i].stop();
			theBangs[i].stop();
		}
	}

}

class Synchronizer {
	/* declare your primitive-typed variables and constants here */

	private volatile int REMAINING_PING = 1;
	private volatile int REMAINING_PONG = 0;
	private volatile int REMAINING_BANG = 0;

	AtomicBoolean accessGrantor = new AtomicBoolean(true);

	public void letMePing() {		
		while (true) {
			if (accessGrantor.compareAndSet(true, false)) {
				if (REMAINING_PING <=0) {
					accessGrantor.set(true);
				} else {
					return;
				}
			}
			Thread.yield();
		}
	}

	public void letMePong() {
		while (true) {
			if (accessGrantor.compareAndSet(true, false)) {
				if (REMAINING_PONG <=0) {
					accessGrantor.set(true);
				} else {
					return;
				}
			}
			Thread.yield();
		}
	}

	public void letMeBang() {
		while (true) {
			if (accessGrantor.compareAndSet(true, false)) {
				if (REMAINING_BANG <=0) {
					accessGrantor.set(true);
				} else {
					return;
				}
			}
			Thread.yield();
		}
	}

	public void pingDone() {
		REMAINING_PING = 0;
		REMAINING_PONG = 2;
		accessGrantor.set(true);
	}

	public void pongDone() {
		REMAINING_PONG--;
		if (REMAINING_PONG <= 0)
			REMAINING_BANG = 1;
		accessGrantor.set(true);
	}

	public void bangDone() {
		REMAINING_BANG = 0;
		REMAINING_PING = 1;
		accessGrantor.set(true);
	}

}

/*
 * Classes Ping, Pong and Bang are complete.
 * DO NOT MODIFY THEM
 */

class Ping extends Thread {
	private int id;
	private Synchronizer synchronizer;

	public Ping(int id, Synchronizer synchronizer) {
		this.synchronizer = synchronizer;
		this.id = id;
	}

	public void run() {
		while (true) {
			synchronizer.letMePing();
			System.out.print("PING(" + id + ") ");
			synchronizer.pingDone();
		}
	}
}

class Pong extends Thread {
	private int id;
	private Synchronizer synchronizer;

	public Pong(int id, Synchronizer synchronizer) {
		this.synchronizer = synchronizer;
		this.id = id;
	}

	public void run() {
		while (true) {
			synchronizer.letMePong();
			System.out.print("pong(" + id + ") ");
			synchronizer.pongDone();
		}
	}
}

class Bang extends Thread {
	private int id;
	private Synchronizer synchronizer;

	public Bang(int id, Synchronizer synchronizer) {
		this.synchronizer = synchronizer;
		this.id = id;
	}

	public void run() {
		while (true) {
			synchronizer.letMeBang();
			System.out.println("BANG!(" + id + ")");
			synchronizer.bangDone();
			try {
				Thread.sleep(25);
			} catch (InterruptedException ie) {
			}
		}
	}
}
