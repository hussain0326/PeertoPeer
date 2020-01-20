import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Scanner;

public class HeartbeatThread extends Thread implements Runnable {

	Peer peer;

	public HeartbeatThread(Peer peer) throws Exception {
		this.peer = peer;
	}

	//int portAddress = 1337;

	public void run() {
	
		System.out.println("Starte Heartbeat");
		while(true) {
			
			for(int i = 0; i < peer.AktiveBekannten.size(); i++) {
				Nachricht n = new Nachricht(5, "Ping erhalten");
				System.out.println("Heartbeat senden an:" + peer.AktiveBekannten.get(i).getPcname());
				peer.SendNachricht(n, peer.AktiveBekannten.get(i).getIpAddress(), peer.AktiveBekannten.get(i).getPort());
			}			
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//________________//
  	        System.out.println("Heartbeat gesendet");
		}
	}}