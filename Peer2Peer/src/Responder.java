import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class Responder implements Runnable{
	DatagramPacket packet=null;//Data
	Peer sender;
	int msgType;
	public ArrayList<PeerObjct>incomeList=new ArrayList<PeerObjct>();
	public PeerObjct incomePeer;
	public String incomeText;
	Nachricht n;
	
	public Responder(Peer sender,DatagramPacket packet) {
		super();
		this.packet = packet;
		this.sender=sender;
		this.msgType=unpackingMsgType(packet);
	}
//-----------------------------------------------------------------------------
	public int unpackingMsgType(DatagramPacket packet){
		int res=0;
	try {
		ByteArrayInputStream in = new ByteArrayInputStream(packet.getData());
		ObjectInputStream is = new ObjectInputStream(in);

		Nachricht n1= (Nachricht) is.readObject();
		incomeList=n1.getPeerListe();  // Übertragung der Liste
		incomePeer=n1.getPeerListe().get(0);
		incomeText=n1.getText();
		res=n1.getMsgType();
		n= (Nachricht)n1;
	} catch (ClassNotFoundException e) {
		e.printStackTrace();
	} catch (IOException ex) {
		ex.printStackTrace();
	}
	return res;
}
	public void DeleteFirstElem() {
		incomeList.remove(0);
	}
//------------------------------------------------------------------------------------
	@Override
	public void run() {

		switch (msgType) {
			case 1:
			try {
				sender.AddPeerAndSendList(incomePeer);
			} catch (IOException e2) {
				e2.printStackTrace();
			}
				break;
			case 2:
				sender.UpdateMyUserID(incomePeer);
				DeleteFirstElem();
				sender.UpdateAktiveBekList(incomeList);
				break;
			case 3:
			try {
				sender.PeerIsLeaving(incomePeer);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
				break;
			case 4:
				//Ein neuer Peer ist ins Netzwerk gekommen 
				sender.NewPeerEvaluation(incomePeer);
				break;
			case 5:
			try {
				sender.ChattShow(n);
				sender.PeersSearchByID(incomePeer.getId(),incomePeer);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
				break;
			case 6:
				//Neuer Peer hat mich als Bekannt
				sender.AddAktivePeer(incomePeer);
				break;
			case 7:
				//Jemand Sucht jemanden --> Antwort wird berechnet
				sender.ReplaySearchOutTheBox(n);
				break;
			case 8:
				//Jemand Auf der DeepSuche hat reagiert
				sender.DeepSeachEvaluation(n);
				break;
			case 9:
				//Dies Peer ist gegangen
				sender.DeleteLeavingPeer(incomePeer);
				break;
			case 10:
				//Dieser Peer hat niemanden zum komunizieren
				Nachricht n=new Nachricht(6,"");
				n.getPeerListe().add(sender.getUser());
				sender.SendNachricht(n, incomePeer.getIpAddress(),incomePeer.getPort());
				break;
			case 11:
				//Ping erhalten vong die anderen her
				for(int i = 0; i < sender.AktiveBekannten.size(); i++) {
					if(sender.AktiveBekannten.get(i).getIpAddress().equals(incomePeer.getIpAddress())) {
						incomePeer.setTimestamp(System.currentTimeMillis());
						System.out.println("Penis");
					}else {
						System.out.println("Nicht in der Liste oder Fehler");
					}
				}
				
				break;
			default:break;
		}
	}
}