import java.io.IOException;
import java.net.DatagramPacket;

public class BriefkastenThread extends Thread {
	private Peer peer;
	public BriefkastenThread(Peer _peer) {
		this.peer=_peer;
		start();
	}
	@Override
	public void run() {

		while(true)
		{
			byte[] receiveData = new byte[1024];
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			try {
				peer.getEigenUdpSocket().receive(receivePacket);
			} catch (IOException e) {
				e.printStackTrace();
			}
			int clientport = receivePacket.getPort();
			//System.out.println("Anfrage von Peer:"+clientport+"  erhalten.");

			new Thread( new Responder(peer,receivePacket)).start();
		}
	}
}