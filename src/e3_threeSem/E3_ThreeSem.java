package e3_threeSem;

import java.util.concurrent.Semaphore;

public class E3_ThreeSem {
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

	// Declare your three semaphores and other simple-typed variables here

	private volatile boolean firstPong = true;

	/* COMPLETE */

	private Semaphore pingSemaphore = new Semaphore(1);
	private Semaphore pongSemaphore = new Semaphore(0);
	private Semaphore bangSemaphore = new Semaphore(0);

	public void letMePing() {
		/* COMPLETE */
		while (!pingSemaphore.tryAcquire()) {
			Thread.yield();
		}
	}

	public void letMePong() {
		/* COMPLETE */
		while (!pongSemaphore.tryAcquire()) {
			Thread.yield();
		}
	}

	public void letMeBang() {
		/* COMPLETE */
		while (!bangSemaphore.tryAcquire()) {
			Thread.yield();
		}
	}

	public void pingDone() {
		/* COMPLETE */
		pongSemaphore.release();
	}

	public void pongDone() {
		/* COMPLETE */
		if (firstPong) {
			pongSemaphore.release();
			firstPong = false;
		} else {
			bangSemaphore.release();
			firstPong = true;
		}
	}

	public void bangDone() {
		/* COMPLETE */
		pingSemaphore.release();
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
