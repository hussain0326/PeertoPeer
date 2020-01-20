import java.io.Serializable;
import java.util.ArrayList;

public class Nachricht implements Serializable {

    private static final long serialVersionUID = 1L;
    private int msgType;
    private String text;
    private ArrayList<PeerObjct> peerListe;
    //neue
    private String suchBericht="";
    private ArrayList<Integer> sucessorListe;
    
    public Nachricht(int msgType, String text) {
        this.msgType = msgType;
        this.text = text;
        this.peerListe = new ArrayList<PeerObjct>();
        this.sucessorListe = new ArrayList<Integer>();
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ArrayList<PeerObjct> getPeerListe() {
        return peerListe;
    }

    public void setPeerListe(ArrayList<PeerObjct> peerListe) {
        this.peerListe = peerListe;
    }

    public String getSuchBericht() {
		return suchBericht;
	}
    //neue
	public void setSuchBericht(String suchBericht) {
		this.suchBericht = suchBericht;
	}
	
	public ArrayList<Integer> getSucessorListe() {
		return sucessorListe;
	}

	public void setSucessorListe(ArrayList<Integer> sucessorListe) {
		this.sucessorListe = sucessorListe;
	}

	@Override
    public String toString() {
        return "Nachricht{" +
                "msgType=" + msgType +
                ", text='" + text + '\'' +
                ", peerListe=" + peerListe +
                '}'+"\n";
    }
}