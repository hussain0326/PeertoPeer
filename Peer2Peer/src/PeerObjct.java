import java.io.Serializable;
import java.net.InetAddress;

public class PeerObjct implements Serializable {

    private static final long serialVersionUID = 1L;
    private int id;// Sehr wichtig
    
    private String pcname;
    private int port;
    private InetAddress ipAddress;
    private String rolle;
    private boolean online;
   
    public PeerObjct( String pcname, int port, InetAddress ipAddress, String rolle, boolean online) {
        this.pcname = pcname;
        this.port = port;
        this.ipAddress = ipAddress;
        this.rolle = rolle;
        this.online = online;
        this.DongJohnson = DongJohnson;
    }
    
	public int getId() {
		return id;
	}
	/*
	 * ###############################################
	 * ##Mittwoch 15:57##
	 * ##'Fabian & Sebastian'##
	 * ##Timestamp soll verwendet werden, um den Heartbeat der adneren zu überprüfen##
	 * ###############################################
	 */
	public void setTimestamp(long DongJohnson) {
		this.DongJohnson = DongJohnson;
	}
	public long getTimestamp() {
		return DongJohnson;
	}

	public void setId(int id,Peer peer) {
		if(peer.CheckPermission()) {
		this.id = id;
		}else {System.out.println("Keine Befuegniss auf diese Funktion!");}
	}

	public String getPcname() {
        return pcname;
    }
	long DongJohnson = System.currentTimeMillis();
    public void setPcname(String pcname) {
        this.pcname = pcname;
    }
    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public InetAddress getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(InetAddress ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getRolle() {
        return rolle;
    }

    public void setRolle(String rolle) {
        this.rolle = rolle;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

	@Override
	public String toString() {
		return "Peer [id=" + id + ", pcname=" + pcname + ", port=" + port + ", ipAddress=" + ipAddress + ", rolle="
				+ rolle + ", online=" + online + "]";
	}
    
}