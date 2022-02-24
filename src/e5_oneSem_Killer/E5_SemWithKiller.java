package e5_oneSem_Killer;

import java.util.Scanner;
import java.util.concurrent.Semaphore;

public class E5_SemWithKiller {
	// LAUNCHER
	public static void main(String[] args) {
		final int HOW_MANY = 10;

		Ping[] thePings = new Ping[HOW_MANY];
		Pong[] thePongs = new Pong[HOW_MANY];
		Bang[] theBangs = new Bang[HOW_MANY];

		Synchronizer sync = new Synchronizer(HOW_MANY);

		SerialBangKiller dexter = new SerialBangKiller(sync, theBangs);

		Scanner sc = new Scanner(System.in);
		System.out.println("\nPress return to start the killing spree");
		System.out.println("Then press return again to put an end to this nightmare ");
		sc.nextLine();

		for (int i = 0; i < HOW_MANY; i++) {
			thePings[i] = new Ping(i, sync);
			thePongs[i] = new Pong(i, sync);
			theBangs[i] = new Bang(i, sync);
			thePings[i].start();
			thePongs[i].start();
			theBangs[i].start();
		}

		dexter.start();

		sc.nextLine();

		System.exit(0);
	}
}

class Synchronizer {

	private final int numPongs; // number of pong instances

	/* declare your primitive-typed variables and constants here */

	private volatile int REMAINING_PING = 1;
	private volatile int REMAINING_PONG = 0;
	private volatile int REMAINING_BANG = 0;
	private volatile boolean wantsToKill = false;
	private volatile boolean canKill = false;

	private int secondPongId = -1;

	/* COMPLETE */

	Semaphore mutex = new Semaphore(1);

	// Constructor
	public Synchronizer(int numPongs) {
		this.numPongs = numPongs;
	}

	public void letMePing() {
		try {
			mutex.acquire();
		} catch (InterruptedException e) {
		}
		while (REMAINING_PING <= 0) {
			mutex.release();
			Thread.yield();
			try {
				mutex.acquire();
			} catch (InterruptedException e) {
			}
		}
	}

	public void letMePong(int id) {
		try {
			mutex.acquire();
		} catch (InterruptedException e) {
		}
		while (REMAINING_PONG <= 0 || (id != secondPongId && secondPongId != -1)) {
			mutex.release();
			Thread.yield();
			try {
				mutex.acquire();
			} catch (InterruptedException e) {
			}
		}
	}

	public void letMeBang() {
		try {
			mutex.acquire();
		} catch (InterruptedException e) {
		}
		while (REMAINING_BANG <= 0) {
			mutex.release();
			Thread.yield();
			try {
				mutex.acquire();
			} catch (InterruptedException e) {
			}
		}
	}

	public void pingDone() {
		REMAINING_PING = 0;
		REMAINING_PONG = 2;
		secondPongId = -1;
		mutex.release();
	}

	public void pongDone(int id) {
		/* COMPLETE */
		secondPongId = (id + 1) % numPongs;
		REMAINING_PONG--;
		if (REMAINING_PONG <= 0) {
			if (wantsToKill && id == 0)
				canKill = true;
			else
				REMAINING_BANG = 1;
		}

		mutex.release();
	}

	public void bangDone() {
		/* COMPLETE */
		REMAINING_BANG = 0;
		REMAINING_PING = 1;
		mutex.release();
	}

	public void letMeKill() {
		wantsToKill = true;

		try {
			mutex.acquire();
		} catch (InterruptedException e) {
		}

		while (!canKill) {
			mutex.release();
			Thread.yield();
			try {
				mutex.acquire();
			} catch (InterruptedException e) {
			}
		}
	}

	public void killingDone() {
		wantsToKill = false;
		canKill = false;
		bangDone();
	}

}

class SerialBangKiller extends Thread {

	private Synchronizer synchronizer;
	private int nextBangToKill = 0;
	private Bang[] theBangs;

	public SerialBangKiller(Synchronizer synchronizer, Bang[] theBangs) {
		this.synchronizer = synchronizer;
		this.theBangs = theBangs;
	}

	public void run() {
		while (true) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException ie) {
			}

			synchronizer.letMeKill();
			Assasinate();
			synchronizer.killingDone();

		}
	}

	public void Assasinate() {
		System.out.println();
		System.out.println();

		if (nextBangToKill < theBangs.length - 1) {
			theBangs[nextBangToKill].syncStop();
			nextBangToKill++;
			System.out.print("\tBYE-BYE BANG ");
		} else {
			System.out.print("\tBANGs ALREADY AT BAY ");
		}

		for (int i = 9; i >= 0; i--) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
			}
			System.out.print(i + " ");
		}
		System.out.println();
		System.out.println();

	}
}

/*
 * COMPLETE and modify class Bang to make its
 * instances stoppable
 */

class Bang extends Thread {
	private int id;
	private Synchronizer synchronizer;

	public Bang(int id, Synchronizer synchronizer) {
		this.synchronizer = synchronizer;
		this.id = id;
	}

	public void syncStop() {
		/* COMPLETE */
		this.stop();
	}

	public void run() {
		/* COMPLETE */
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

/*
 * Classes Ping and Pong are complete.
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
			synchronizer.letMePong(id);
			System.out.print("pong(" + id + ") ");
			synchronizer.pongDone(id);
		}
	}
}
