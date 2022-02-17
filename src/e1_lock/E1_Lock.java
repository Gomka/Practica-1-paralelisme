package e1_lock;

import java.util.concurrent.locks.*;
import java.util.concurrent.atomic.AtomicInteger;

public class E1_Lock {
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

	private static final int CAN_PING = 1;
	private static final int CAN_PONG = 2;
	private static final int CAN_BANG = 3;
	private static final int WRITING = 4;

	private int state = CAN_PING;

	/* COMPLETE */

	Lock accessGrantor = new ReentrantLock(true);

	public void letMePing() {
		/* COMPLETE */

		accessGrantor.lock();
		while (state != CAN_PING) {
			accessGrantor.unlock();
			Thread.yield();
			accessGrantor.lock();
		}
	}

	public void letMePong() {
		/* COMPLETE */

		accessGrantor.lock();
		while (state != CAN_PONG) {
			accessGrantor.unlock();
			Thread.yield();
			accessGrantor.lock();
		}
	}

	public void letMeBang() {
		/* COMPLETE */

		accessGrantor.lock();
		while (state != CAN_BANG) {
			accessGrantor.unlock();
			Thread.yield();
			accessGrantor.lock();
		}
	}

	public void pingDone() {
		/* COMPLETE */
		state = CAN_PONG;
		accessGrantor.unlock();
	}

	public void pongDone() {
		/* COMPLETE */
		state = CAN_BANG;
		accessGrantor.unlock();
	}

	public void bangDone() {
		/* COMPLETE */
		state = CAN_PING;
		accessGrantor.unlock();
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
