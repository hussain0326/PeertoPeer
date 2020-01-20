public class KnotenListeStatisch {
	String knotenName;
	String ipAdresse;
	int port;

	public  String getKnotenName() {
		return knotenName;
	}
	public  void setknotenName(String name) {
		knotenName = name;
	}
	public  String getIpAdresse() {
		return ipAdresse;
	}
	public void setIpAdresse(String ip) {
		ipAdresse = ip;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int i) {
		port = i;
	}
}
