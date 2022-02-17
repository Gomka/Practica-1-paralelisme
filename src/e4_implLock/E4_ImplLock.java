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
		
		try {Thread.sleep(10000);} catch (InterruptedException ie) {}
		
		for (int i=0; i<HOW_MANY; i++) {
			thePings[i].stop();
			thePongs[i].stop();
			theBangs[i].stop();
		}
	}

}

class Synchronizer {
	/* declare your primitive-typed variables here. Only primitive-typed */
	
	/* COMPLETE */
	
	public void letMePing(int id) {
		/* COMPLETE */
	}
	
	public  void letMePong(int id) {
		/* COMPLETE */
	}
	
	public void letMeBang(int id ) {
		/* COMPLETE */
	}

	public void pingDone() {
		/* COMPLETE */
	}

	public void pongDone() {
		/* COMPLETE */
	}
	
	public void bangDone() {
		/* COMPLETE */
	}
	
}

/* Classes Ping, Pong and Bang are complete. 
 * DO NOT MODIFY THEM */

class Ping extends Thread {
	private int id;
	private Synchronizer synchronizer;
	
	public Ping (int id, Synchronizer synchronizer) {
		this.synchronizer = synchronizer;
		this.id = id;}
	
	public void run () {
		while (true) {
			synchronizer.letMePing(id);
			System.out.print("PING("+id+") ");
			synchronizer.pingDone();
		}
	}
}

class Pong extends Thread {
	private int id;
	private Synchronizer synchronizer;
	
	public Pong (int id, Synchronizer synchronizer) {
		this.synchronizer = synchronizer;
		this.id = id;}
	
	public void run () {
		while (true) {
			synchronizer.letMePong(id);
			System.out.print("pong("+id+") ");
			synchronizer.pongDone();
		}
	}
}

class Bang extends Thread {
	private int id;
	private Synchronizer synchronizer;
	
	public Bang (int id, Synchronizer synchronizer) {
		this.synchronizer = synchronizer;
		this.id = id;}
	
	public void run () {
		while (true) {
			synchronizer.letMeBang(id);
			System.out.println("BANG!("+id+")");
			synchronizer.bangDone();
			try {Thread.sleep(25);} catch (InterruptedException ie) {}
		}
	}
}
