package e4_implLock;

public class E4_ImplLock {
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
			Thread.sleep(1000);
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
	/* declare your primitive-typed variables here. Only primitive-typed */
	private int REMAINING_PING = 1;
	private int REMAINING_PONG = 0;
	private int REMAINING_BANG = 0;
	private boolean MUST_PRINT = false;
	private int pingId = -1;
	private int pongId = -1;

	public void letMePing(int id) {
		while (true) {
			synchronized (this) {
				if (!MUST_PRINT && REMAINING_PING > 0) {
					MUST_PRINT = true;
					pingId = id;
					return;
				}
			}
			Thread.yield();
		}
	}

	public void letMePong(int id) {
		while (true) {
			synchronized (this) {
				if (!MUST_PRINT && REMAINING_PONG > 0) {
					
					if (REMAINING_PONG == 1 && id != pingId) {
						pongId = id;
						MUST_PRINT = true;
						return;
					}
					
					if (REMAINING_PONG == 2 && id == pingId) {
						MUST_PRINT = true;
						return;
					}

				}
			}
			Thread.yield();
		}
	}

	public void letMeBang(int id) {
		while (true) {
			synchronized (this) {
				if (!MUST_PRINT && REMAINING_BANG > 0 && id == pongId) {
					MUST_PRINT = true;
					return;
				}
			}
			Thread.yield();
		}
	}

	public void pingDone() {
		synchronized (this) {
			REMAINING_PING = 0;
			REMAINING_PONG = 2;
			MUST_PRINT = false;
		}
	}

	public void pongDone() {
		synchronized (this) {
			REMAINING_PONG--;
			if (REMAINING_PONG <= 0)
				REMAINING_BANG = 1;
			MUST_PRINT = false;
		}
	}

	public void bangDone() {
		synchronized (this) {
			REMAINING_BANG = 0;
			REMAINING_PING = 1;
			MUST_PRINT = false;
		}
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
			synchronizer.letMePing(id);
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
			synchronizer.letMeBang(id);
			System.out.println("BANG!(" + id + ")");
			synchronizer.bangDone();
			try {
				Thread.sleep(25);
			} catch (InterruptedException ie) {
			}
		}
	}
}
