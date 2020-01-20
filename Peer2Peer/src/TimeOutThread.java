import java.util.ArrayList;

public class TimeOutThread extends Thread {

	Peer peer;

	public TimeOutThread(Peer peer) {
		this.peer = peer;
	}

	public void run() {
		while (true) {
			System.out.println("timeouter Tick");
			for (int i = 1; i < peer.AktiveBekannten.size(); i++) {
				System.out.println("timeouter sucht");
				System.out.println(peer.AktiveBekannten.get(i).getTimestamp());
				long time = System.currentTimeMillis();
				if ((peer.AktiveBekannten.get(i).getTimestamp() + 20000) < time) {
					System.out.println("Peer " + peer.AktiveBekannten.get(i).getPort() + " hatte einen Timeout und wurde abgemeldet.");
					peer.SayBye(peer.AktiveBekannten.get(i).getIpAddress(), peer.AktiveBekannten.get(i).getPort());
				}
			}
			try {
				Thread.sleep(20000);// 666
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}