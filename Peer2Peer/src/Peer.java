import java.io.*;
import java.net.*;
import java.security.SecureRandom;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

import java.awt.Color;
import java.awt.Font;
import javax.swing.JTextField;

import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ActionEvent;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.table.DefaultTableModel;//TT

public class Peer {
	
	long DongJohnson = System.currentTimeMillis();
	private PeerObjct user;
	private DatagramSocket eigenUdpSocket;
	public ArrayList<PeerObjct>alleTeilnehmer=new ArrayList<PeerObjct>();// only for Leeder
	public ArrayList<PeerObjct>AktiveBekannten=new ArrayList<PeerObjct>();
	public int[] IdList=new int[4];//initial IDs create from Chord
	private static SimpleDateFormat sdf = new SimpleDateFormat("MM-dd-yyyy || HH:mm:ss");//fuer Fehlermeldung
	private static Scanner scanner=new Scanner(System. in);
	private PeerObjct chattPartner; 
	// +++++++++++JFrame objects
		private JFrame frmPeer;
		private JTextField textField;
		private JTextArea textArea;
		private ImageIcon ficon;
		private JButton[] btn = new JButton[4];
		 private JTable table;//TT
		 //suche GUI
		    private JTextField textField_1;
		    private JButton btnSuche;
		    private JScrollPane scrollPane_2;
		    private JTextArea textArea_1;
		    private JLabel lblStatus;
		    private JLabel lblIdtext;
		    private JLabel lblID;
		    
	public Peer(PeerObjct _user){
		this.user=_user;
		frmPeer = new JFrame();
	}	
	public void Texting() {
			if (!textField.getText().equals("")) {
		        if(textField.getText().toUpperCase().equals("END")) {
		        	try {
						SayBye(InetAddress.getByName("localhost"), 1444);
					} catch (UnknownHostException e1) {
						e1.printStackTrace();
					}
		        }else {
		        	//ChattFunktion --> mit Send-Button
		        	Timestamp timestamp= new Timestamp(System.currentTimeMillis());sdf.format(timestamp);
		        	if(btn[0].getText()=="") {
		        		textArea_1.append("ERROR!("+timestamp+")-> Da ist niemand zu chatten"+"\n");
		        	}else {
			        		//btn grosse ueberpruefen
		        		
		        		try {	        		
			        		if(btn[1].getText()=="") {
			        			//PeerObjct peerO=PeersSearchByPCname(btn[0].getText());
			        			textArea.append("@"+user.getPcname()+": "+textField.getText() + "\n");
								ChattSend(textField.getText(), chattPartner.getPort(),chattPartner.getIpAddress());//hier ip gebraucht
			        		}else if(chattPartner.getPort()==0) {
			        			textArea_1.append("ERROR!("+timestamp+")-> Du musst einen Button auswaehlen!"+"\n");			        			
			        		}else {
			        			textArea.append("@"+user.getPcname()+": "+textField.getText() + "\n");
			        			//PeerObjct peerO=PeersSearchByPCname(btn[0].getText());
			        			ChattSend(textField.getText(), chattPartner.getPort(),chattPartner.getIpAddress());//hier ip gebraucht
			        		}
			        		} catch (UnknownHostException e1) {
								e1.printStackTrace();
							}	
		        		}	
		        }   			
			textField.setText("");
			textField.setForeground(Color.GRAY);
			textField.setText("Schreib eine Nachricht");
		}
	}
	//-------------------------JFrame initialize------------------------------------
			public void initialize() {
				frmPeer = new JFrame("Peer");
				frmPeer.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frmPeer.setResizable(false);
				frmPeer.setTitle("P2P Chat");
				frmPeer.setBounds(100, 100, 618,  301);//suche GUI
				frmPeer.getContentPane().setLayout(null);
				ficon = new ImageIcon("img/ricon.png");
				frmPeer.setIconImage(ficon.getImage());
				frmPeer.addWindowListener(new WindowAdapter() {
					@Override
					public void windowClosing(WindowEvent e) {
						try {SayBye(InetAddress.getByName("localhost"), 1444);
						} catch (UnknownHostException e1) {
							e1.printStackTrace();
						}
					}
				});

				JButton btnNewButton = new JButton(">>");
				btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 8));
				btnNewButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (textField.getText().equals("Schreib eine Nachricht")) {
							textField.setText("");
							textField.setForeground(Color.BLACK);
							}
						Texting();
					}
				});
				btnNewButton.setBounds(387, 227, 46, 26);//suche GUI 
				frmPeer.getContentPane().add(btnNewButton);

				textField = new JTextField();
				//gg
				textField.setForeground(Color.GRAY);
				textField.setText("Schreib eine Nachricht");
				textField.addFocusListener(new FocusAdapter() {
					@Override
					public void focusGained(FocusEvent e) {
						if (textField.getText().equals("Schreib eine Nachricht")) {
							textField.setText("");
							textField.setForeground(Color.BLACK);
				        }
					}
					@Override
					public void focusLost(FocusEvent e) {
						if (textField.getText().isEmpty()) {
							textField.setForeground(Color.GRAY);
							textField.setText("Schreib eine Nachricht");
				        }
					}
				});				
				textField.addKeyListener(new KeyAdapter() {
					@Override
					public void keyPressed(KeyEvent e) {
						
						if (e.getKeyCode() == KeyEvent.VK_ENTER && !textField.getText().equals("")) {
							if (textField.getText().equals("Schreib eine Nachricht")) {
								textField.setText("");
								textField.setForeground(Color.BLACK);
					        }
						Texting();	
						}
					}
				});
				
				textField.setBounds(114, 229, 263, 20);//suche GUI 
				frmPeer.getContentPane().add(textField);
				textField.setColumns(10);

				JScrollPane scrollPane = new JScrollPane(); 
				scrollPane.setBounds(114, 11, 310, 112);//suche GUI
				frmPeer.getContentPane().add(scrollPane);

				textArea = new JTextArea();
				scrollPane.setViewportView(textArea);
				textArea.setLineWrap(true);
				textArea.setEditable(false);

				for (int i = 0; i < 4; i++) {
					btn[i] = new JButton("");
					btn[i].setBounds(10, 73 + (i * 33), 89, 23);
					frmPeer.getContentPane().add(btn[i]);
					 btn[i].setVisible(false);
					//btn[i].setEnabled(false);
					 btn[i].addActionListener(new ActionListener() {
						    @Override
						    public void actionPerformed(ActionEvent e) {
						        JButton button = (JButton) e.getSource();
						        //btn Farbe ändern
						        for (int i = 0; i < 4; i++) 
						        	 btn[i].setForeground(Color.black);
						        button.setForeground(Color.red);
						      //hier Object suchen und Port merken
						       chattPartner=PeersSearchByPCname(button.getText());
						    }
						});
				}
				JLabel NametxtLabel = new JLabel("Name : ");
				NametxtLabel.setFont(new Font("Tahoma", Font.BOLD, 11));
				NametxtLabel.setBounds(10, 15, 46, 14);
				frmPeer.getContentPane().add(NametxtLabel);

				JLabel Roletxtlabel = new JLabel("Role : ");
				Roletxtlabel.setFont(new Font("Tahoma", Font.BOLD, 11));
				Roletxtlabel.setBounds(10, 35, 46, 14);
				frmPeer.getContentPane().add(Roletxtlabel);

				JLabel lblName = new JLabel("");
				lblName.setBounds(50, 15, 61, 14);
				frmPeer.getContentPane().add(lblName);
				lblName.setText(this.user.getPcname());

				JLabel lblRole = new JLabel("");
				lblRole.setBounds(50, 35, 61, 14);
				frmPeer.getContentPane().add(lblRole);
				lblRole.setText(this.user.getRolle());
				//id
				lblIdtext = new JLabel("ID : ");
				lblIdtext.setBounds(10, 55, 26, 14);
				frmPeer.getContentPane().add(lblIdtext);
				
				lblID = new JLabel();
				lblID.setBounds(53, 55, 46, 14);
				lblID.setText(this.user.getId()+"");
				frmPeer.getContentPane().add(lblID);
				//id
				//TT
				JScrollPane scrollPane_1 = new JScrollPane();
				scrollPane_1.setBounds(472, 11, 116, 171);//suche GUI
				frmPeer.getContentPane().add(scrollPane_1);
				table = new JTable();
				table.setModel(new DefaultTableModel(new Object[][] {},	new String[] {"PC ID", "PC Name"}) {
					Class[] columnTypes = new Class[] {
						Integer.class, String.class
					};
					public Class getColumnClass(int columnIndex) {
						return columnTypes[columnIndex];
					}
				});
				scrollPane_1.setViewportView(table);
				//suche GUI
				btnSuche = new JButton("Suche");
				btnSuche.setBounds(479, 228, 89, 23);
				frmPeer.getContentPane().add(btnSuche);
				//SuchFunktion : Jackson 
				btnSuche.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						SearchPeerForChatt();
					}
				});			
				textField_1 = new JTextField();
				textField_1.setBounds(482, 193, 86, 20);
				frmPeer.getContentPane().add(textField_1);
				textField_1.setColumns(10);
				
				scrollPane_2 = new JScrollPane();
				scrollPane_2.setBounds(114, 156, 310, 62);
				frmPeer.getContentPane().add(scrollPane_2);
				
				textArea_1 = new JTextArea();
				scrollPane_2.setViewportView(textArea_1);
				
				lblStatus = new JLabel("Status : ");
				lblStatus.setFont(new Font("Times New Roman", Font.BOLD, 12));
				lblStatus.setBounds(124, 134, 46, 14);
				frmPeer.getContentPane().add(lblStatus);
				//suche GUI
				frmPeer.setVisible(true);
			}
			
			
	// search by port 
		public void PeersSearchByPort(int port) {
			PeerObjct elem=null;
			for(int i = 0; i< alleTeilnehmer.size();i++)
				
				if(alleTeilnehmer.get(i).getPort() == port){
					elem= alleTeilnehmer.get(i);
				}
			updatePeersBtn(elem);
		}
		
		
		// search by ID 
		//von jackson
		public void PeersSearchByID(int _id,PeerObjct _peer) { // Input a= gesuchteID und b=icome Peer 
			PeerObjct elem=null;
			for(int i = 0; i< AktiveBekannten.size();i++) {
				if(AktiveBekannten.get(i).getId() == _id){
					elem= AktiveBekannten.get(i);
					}
				}
				if(elem==null && (AktiveBekannten.size()<4)) {
					updatePeersBtn(_peer);
					AddAktivePeer(_peer);
				}else {updatePeersBtn(elem);}
		}
		
		// search by PC name 
		public PeerObjct PeersSearchByPCname(String pcname) {
			PeerObjct elem=null;
			for(int i = 0; i< AktiveBekannten.size();i++) {
				if(AktiveBekannten.get(i).getPcname() == pcname){
					elem= AktiveBekannten.get(i);
				}
			}
			return (elem);
		}
		public void updatePeersBtn(PeerObjct elem) {
			//check if the peer is already  in the list
			boolean IsInTheList= false;
			for (int i = 0; i < btn.length; i++) { 
				if( btn[i].getText().equals(elem.getPcname())) {
					IsInTheList= true;
				}
			}			
			//check if it is the same member
			boolean theSamePeer = false;
			if(this.user.getPcname().equals(elem.getPcname())) {
				theSamePeer =true;
				System.out.println("the same Port");
			}		
			//check if btn list if full
			boolean btnArrayIsNotFull = false;
			for (int i = 0; i < btn.length; i++) { 
				if( btn[i].getText().equals("")) {
					btnArrayIsNotFull= true;
					break;
				}
			}				
		  for (int i = 0; i < btn.length; i++) { 
			  if( btn[i].getText().equals("") && btnArrayIsNotFull && !IsInTheList && !theSamePeer) { 
				  btn[i].setText(elem.getPcname()); 
				  btn[i].setEnabled(true);
				  btn[i].setVisible(true);
				  break;
			  	}		  
			 }
		  //**************
		  String[] btnCopy= new String[btn.length];
		  for (int i = 0; i < btnCopy.length; i++) {
			  btnCopy[i]=btn[i].getText();
		  }
		  if(!btnArrayIsNotFull && !IsInTheList && !theSamePeer) {			   
		      for(int i = 0; i < btn.length - 1; i++) {
		    	  btn[i+1].setText(btnCopy[i]);
		      }
		      btn[0].setText(elem.getPcname());
		  }			
		}//this comes back when u delete the comment
	
		//--------------------Getters and Setter----------------------------------------
		public PeerObjct getUser() {
			return user;
		}
		public void setUser(PeerObjct user) {
			this.user = user;
		}
		//-----------------------------------------------
		public DatagramSocket getEigenUdpSocket() {
			return eigenUdpSocket;
		}
		public void setEigenUdpSocket(DatagramSocket eigenUdpSocket) {
			this.eigenUdpSocket = eigenUdpSocket;
		}
		public void ConfiUdpSocket(){
			try {
				this.setEigenUdpSocket(new DatagramSocket(user.getPort()));
			} catch (SocketException e) {
				e.printStackTrace();
			}
		}
		public void ConfigID() {
			user.setId(100,this);
		}
		public void UpdateMyUserID(PeerObjct _peer) {
			if(user.getId()==0) {
				if(_peer.getPcname().equals(user.getPcname())) {
					this.setUser(_peer);
				}
			}
		}
		//------------TT
		public void ConfigListTable() {
			DefaultTableModel model= (DefaultTableModel)  table.getModel();
			if (model.getRowCount() > 0) {
                 for (int i = model.getRowCount() - 1; i > -1; i--) {
                     model.removeRow(i);
                 }
             }
			for(int i = 0; i< AktiveBekannten.size();i++ ) {
				if(user.getId()!=AktiveBekannten.get(i).getId()) { 
					model.addRow(new Object[] { AktiveBekannten.get(i).getId(),AktiveBekannten.get(i).getPcname() });
				}
			}
		}
		//------------------------Check Permission-----------------
				public boolean CheckPermission(){
					boolean permission=false;
					if(this.user.getRolle().toUpperCase().equals("LEEDER")) {
						permission=true;
					}
					return permission;
				}
		//-----------------------------Only Function for Leeders-----------------------------
				public void AddToList(PeerObjct peer) {//return true,wenn peer Leeder ist und die Funktion benutzen darf!
					if(CheckPermission()) {
						alleTeilnehmer.add(peer);
					}
				}
				public void AddPeerAndSendList(PeerObjct incomePeer) throws IOException {
					if(CheckPermission()) {
						int id=GenerateUniqueId();
						if(id!=0){//check:Ob das Netzwerk ist voll
								if(alleTeilnehmer.size()<4) {
										if(!CheckID(104)){incomePeer.setId(104, this);}
										else if(!CheckID(108)){incomePeer.setId(108, this);}
										else{incomePeer.setId(112, this);}	
								}else {incomePeer.setId(id, this);}
										alleTeilnehmer.add(incomePeer);
										ConfigListTable();
										Create4Musthave(incomePeer.getId());
										UpdateYourUserID(incomePeer);
										SendPeerToAllPeers(incomePeer,4);
						}else {System.out.println("Netzwerk ist voll");}		
					}		
				}
				public void SendNachricht(Nachricht n,InetAddress _ipAddress,int _port){
					try {
						ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
						ObjectOutputStream os = new ObjectOutputStream(outputStream);
						os.writeObject(n);
						byte[] sendData = outputStream.toByteArray();
						DatagramPacket sendPacket = new DatagramPacket(sendData,sendData.length,_ipAddress,_port);
						getEigenUdpSocket().send(sendPacket);
						System.out.println("Nachricht an: "+_port+ " gesendet.");
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				public void SendPeerToAllPeers(PeerObjct _peer,int msgTyp){
						if(CheckPermission()) {
							Nachricht n1=new Nachricht(msgTyp,"");
							n1.getPeerListe().add(_peer);
							for (int i = 0; i < alleTeilnehmer.size(); i++) {
								if (alleTeilnehmer.get(i).getPort() != _peer.getPort()) {// an alle außer den Neuen Peer 
								SendNachricht(n1,alleTeilnehmer.get(i).getIpAddress(),alleTeilnehmer.get(i).getPort());
								}
							}
							//ConfigListTable();	
					    }//last if
			  }
			
				public void PeerIsLeaving(PeerObjct peer) throws IOException {
					if(CheckPermission()) {
						for (int i=0;i<alleTeilnehmer.size();i++) {
							if (alleTeilnehmer.get(i).getPort() == peer.getPort()) {
								alleTeilnehmer.remove(i);
							}
						}
						System.out.println("Peer:"+peer.getId()+" hat das Netzwerk verlassen.");
						SendPeerToAllPeers(peer,9);// msgTyp 9 ist new
					}
				}
				public void UpdateYourUserID(PeerObjct _peer) {
					if(CheckPermission()) {
						Nachricht n=new Nachricht(2,"");
						Nachricht n1=new Nachricht(6,"Peer mit ID:"+_peer.getId()+" koennte dich kennen.");
						n1.getPeerListe().add(_peer);
						n.getPeerListe().add(_peer);
						PeerObjct bekannte=null;
						for(int x=0;x<IdList.length;x++) {  
							bekannte=SearchOnePeer(alleTeilnehmer,IdList[x]);
							if(bekannte!=null) {
								n.getPeerListe().add(bekannte);
								SendNachricht(n1,bekannte.getIpAddress(),bekannte.getPort());
							 }
						}
						//Check ob Liste der Bekannte leer ist,Falls ja sende him den Leeder als Bekannte
						if(n.getPeerListe().size()<2) {n.getPeerListe().add(alleTeilnehmer.get(0));}
						SendNachricht(n,_peer.getIpAddress(),_peer.getPort());
					}
				}
				public PeerObjct SearchOnePeer(ArrayList<PeerObjct>inUseList,int _id) {
					PeerObjct _peer=null;
					for(int i=0;i<inUseList.size();i++) {
						if(inUseList.get(i).getId()==_id) {
							_peer=inUseList.get(i);
						}	
					}return _peer;
				}
	//----------------------------------------For Chord --------------------------------------------
				public int GenerateUniqueId() {				
						int res=0;
							if(alleTeilnehmer.size()<16){
								for(int i=0;i<1;i++) {
									int id=randomID();
										if(CheckID(id+100)) {
											i=i-1;
										}else res=(id+100);
									}
								}return res;
					}
				public int randomID() {
					Random rd=new Random();
					return 1+rd.nextInt(15);
				}
				public boolean CheckID(int id) {// return true wenn ID existiert
					boolean res=false;
					for(int i=0;i<alleTeilnehmer.size();i++) {
						if(alleTeilnehmer.get(i).getId()==id) res=true;
					}return res;
				}
				public void Create4Musthave(int _id) {
					for(int i=0;i<IdList.length;i++) {
						int n=((int)(_id+Math.pow(2,i)));
							if(n>115) {n=((n-100)%16)+100;}
							IdList[i]=(n);
					}
				}
				public void Create4MHRevers(int _id){
					for(int i=0;i<IdList.length;i++) {
						int n=((int)((_id-100)-Math.pow(2,i)));
							if(n<0) {n=n+16;}
							IdList[i]=(n+100);
					}
				}
				public void SearchPeerForChatt() {// Die Suche! Erstmal Local 
					if (!textField_1.getText().equals("")) {
						int gesID=Integer.parseInt(textField_1.getText());
						Create4Musthave(user.getId());// meine mögliche Bekannte berechnen und in IdList speichen
						int musthave=CheckMustHave(gesID);
						if(gesID==user.getId() ) {
							Timestamp timestamp= new Timestamp(System.currentTimeMillis());sdf.format(timestamp);
							textArea_1.append("ERROR!("+timestamp+")-> Die ID:"+user.getId()+" wurde dir vergeben."+"\n");
						}else if((SearchOnePeer(AktiveBekannten, gesID)==null)&& AktiveBekannten.size()==1) {
							SearchOutTheBox(AktiveBekannten.get(0),gesID);
						}else if ((SearchOnePeer(AktiveBekannten, gesID)==null)&& musthave>0){
							textArea_1.append("SUCHERGEBNIS: Die ID: "+gesID+" existiert nicht.\n");
							System.out.println("SUCHERGEBNIS: Die ID: "+gesID+" existiert nicht.\n");
						}else if(SearchOnePeer(AktiveBekannten, gesID)==null){
							ArrayList<Integer>tempList=new ArrayList<Integer>();tempList.add(user.getId());// IŽm the first Sucessor
							SearchOutTheBox(SearchOnePeer(AktiveBekannten, GetIdOfSucessor(gesID,tempList)), gesID);
						}else {PeersSearchByID(gesID,null);}//hier wurde der Peer gefunden und Button wirde erstellt								
					}
				}
//-------------------------------------------------------Werkzeuge-Funktionen----------------------------------------				
				public int GetIdOfSucessor(int gesID,ArrayList<Integer> sucessorList) {
					int id=0;
					for(int i=0;i<AktiveBekannten.size();i++) {
						int preId=Math.max(id,AktiveBekannten.get(i).getId());
						if(preId<gesID && (CheckSucessor(preId,sucessorList)==0)) {id=preId;}
					}
					if(id==0) {id=SearchMinID();}//einfach mit dem kleinste ID anfangen
					return id;
				}
				public int SearchMinID() {//sucht den min ID
					int preId=AktiveBekannten.get(0).getId();
					for(int i=0;i<AktiveBekannten.size();i++) {
							preId=Math.min(preId,AktiveBekannten.get(i).getId());
					}
					return preId;
				}
				public int CheckMustHave(int gesID) {
					int exists=0;
					for(int i=0;i<IdList.length;i++) {
						if(gesID==IdList[i]) {
						exists+=1;
						}
					}return exists;
				}
				public int CheckSucessor(int preId,ArrayList<Integer> sucessorList) {
					int res=0;
					for(int i=0;i<sucessorList.size();i++) {
						if(sucessorList.get(i)==preId) res+=1;;
					}return res;
				}
				public void SearchOutTheBox(PeerObjct sucessor,int gesID) {
					Nachricht n=new Nachricht(7,""+gesID);
					n.getPeerListe().add(user);// Der Nachfrager mit Idex 0...
					n.getSucessorListe().add(user.getId());
					n.setSuchBericht(n.getSuchBericht()+"StartSuche:(id:"+user.getId()+")[sucht:"+gesID+"]-->(id:"+sucessor.getId()+")\n");
					SendNachricht(n, sucessor.getIpAddress(), sucessor.getPort());
				}
				public void ReplaySearchOutTheBox(Nachricht gesElem) {// Antwort auf die MsgType 7
					int gesID=Integer.parseInt(gesElem.getText());
					gesElem.setMsgType(8);
					PeerObjct nachfrager=gesElem.getPeerListe().get(0);
					PeerObjct gesuchtePeer=null;
					gesuchtePeer=SearchOnePeer(AktiveBekannten, gesID);
					Create4Musthave(user.getId());
					int musthave=CheckMustHave(gesID);//has 0 or number>0
					if((gesuchtePeer==null)&& musthave>0) {
						gesElem.setSuchBericht(gesElem.getSuchBericht()+"(id:"+user.getId()+")[not exists]-->(id:"+nachfrager.getId()+") EndeSuche.\n");
						System.out.println("Gesuchter Peer existiert nicht!");
					}else if(gesuchtePeer==null) {
						gesElem.getPeerListe().add(SearchOnePeer(AktiveBekannten, GetIdOfSucessor(gesID,gesElem.getSucessorListe())));//Einen Sucessor wurde hinzugefuegt
						gesElem.setSuchBericht(gesElem.getSuchBericht()+"(id:"+user.getId()+")[sucessor]-->(id:"+nachfrager.getId()+")\n");
						gesElem.getSucessorListe().add(user.getId());//sich als einer der Sucessors dieser Suche eingetragen
						System.out.println("Aktueller Sucessor:"+gesElem.getSucessorListe());
					}else {
						gesElem.getPeerListe().add(SearchOnePeer(AktiveBekannten, gesID));//Der gesuchte Peer wurde hinzugefuegt
						gesElem.setSuchBericht(gesElem.getSuchBericht()+"(id:"+user.getId()+")[treffer]-->(id:"+nachfrager.getId()+")EndeSuche.\n");
					}
					SendNachricht(gesElem, nachfrager.getIpAddress(), nachfrager.getPort());
				}
				public void DeepSeachEvaluation(Nachricht resElem){// If 8 -> Antwort der SucheOutTheBox
					int gesID=Integer.parseInt(resElem.getText());
					if(resElem.getPeerListe().size()<2||resElem.getPeerListe().get(1)==null) {// if no Peer has been sendet
						System.out.println("Aus der DeepSuche wurde niemanden gefunden.");
						System.out.println(resElem.getSuchBericht());
					}else if(resElem.getPeerListe().get(1).getId()==gesID){//gesuchte Element?
							if(AktiveBekannten.size()==4) {//Liste der Bekannte ist Voll
								AktiveBekannten.remove(0);
								System.out.println("Aus der DeepSuche wurde einen Peer gefunden \n Einen Peer wurde gelöscht.");
							}else {
								System.out.println("Aus der DeepSuche wurde einen Peer gefunden.");
							}
							AktiveBekannten.add(resElem.getPeerListe().get(1));
							PeersSearchByID(gesID,resElem.getPeerListe().get(1));
							ConfigListTable();
							System.out.println(resElem.getSuchBericht());
					}else{
						SearchOutTheBox(resElem.getPeerListe().get(1),gesID);
						System.out.println("Aus der DeepSuche wurde einen Sucessor gefunden.");
					}					
				}
				//-------------------Functions for any Peer to another Peer  -----------------------------------------------------------
				public void AskeForID(InetAddress _ipAddress,int _port) {//return true if is nacessarie to aske..
					if(!(CheckPermission())){
						Nachricht n=new Nachricht(1,"");
						n.getPeerListe().add(user);
						SendNachricht(n,_ipAddress,_port);
					}else {
						System.out.println("Glueckwusche! Du bist der Leeder und kannst die Liste verwalten...");
					}
				}
				public void AddAktivePeer(PeerObjct _peer) {
					 PeerObjct bekannt=SearchOnePeer(AktiveBekannten,_peer.getId());
					 if(bekannt==null && (AktiveBekannten.size()<4)) {
						 AktiveBekannten.add(_peer);
						 ConfigListTable();
					 }
				}
				public void DeleteLeavingPeer(PeerObjct _peer){
					for(int i=0;i<AktiveBekannten.size();i++) {
						if(AktiveBekannten.get(i).getId()==_peer.getId()) {
							AktiveBekannten.remove(i);
						 ConfigListTable();
						 }
					}
					if(AktiveBekannten.size()==0) {
						Nachricht n1 = new Nachricht(10, "");
						n1.getPeerListe().add(user);
						try {
							SendNachricht(n1,InetAddress.getByName("192.168.75.42"), 1422);
						} catch (UnknownHostException e) {
							e.printStackTrace();
						}
					}
				}
				public void NewPeerEvaluation(PeerObjct _peer) {
					Create4Musthave(user.getId());
					if(AktiveBekannten.size()<4) {
						AddAktivePeer(_peer);
					}else if(CheckMustHave(_peer.getId())>0){DeleteOneNotMusthave();}
					AddAktivePeer(_peer);					
				}
				public void DeleteOneNotMusthave() {
					boolean found=false;
					for(int i=0;i<IdList.length;i++) {
						for(int j=0;j<AktiveBekannten.size();j++) {
							if(!(AktiveBekannten.get(j).getId()==IdList[i])) {
								AktiveBekannten.remove(j);found=true;return;
							}
						}if(found)return;
					}
				}
				public void UpdateAktiveBekList(ArrayList<PeerObjct>incomeList) {
					AktiveBekannten=incomeList;
				}
				
				public void ChattShow(Nachricht n) throws UnknownHostException {
					textArea.append("@"+n.getPeerListe().get(0).getPcname()+": "+n.getText() + "\n");
				}
		//----------------------- Functions for Local using -------------------------------------------------------------
				public void ShowList() {
					for(PeerObjct p:alleTeilnehmer) {
						System.out.println(p);
					}
				}			
				public boolean SayBye(InetAddress _ipAddress,int _port) {
					if(!CheckPermission()) {
						Nachricht n1 = new Nachricht(3, "");
						n1.getPeerListe().add(user);
						SendNachricht(n1,_ipAddress,_port);
						return true;
					}System.out.print("Achtung!! Du bist der Leeder...");
					return false;
				}
				public void ShowMusthaveIDs() {
					Create4Musthave(user.getId());
					for(int i=0;i<IdList.length;i++) {
						System.out.println(IdList[i]);
					}
				}
				public void ChattSend(String text,int port,InetAddress ip) throws UnknownHostException {
					Nachricht n=new Nachricht(5,text);
					n.getPeerListe().add(user);
					SendNachricht(n,ip, port);
					System.out.println("@"+user.getPcname()+": "+text);
				}
 //-------------------------------Main Methode --------------------------------------------------------------				
				public static void main(String[]args) throws UnknownHostException {
					PeerObjct user=new PeerObjct("Hossa",1424,InetAddress.getByName("192.168.178.94"),"leeder",true);
					Peer peer=new Peer(user);
					peer.ConfiUdpSocket();
					peer.ConfigID();
					peer.AddToList(user);// sich in die Liste hinzufuegen--> Klappt nur, wenn der user Leeder ist
					new BriefkastenThread(peer);//Ab hier gibt es MessageVerkeher..
					peer.AskeForID(InetAddress.getByName("192.168.178.94"), 1422);// wird gebraucht, wenn der User kein Leeder ist
				
					peer.initialize();
					peer.ConfigListTable();//TT
					//++++++++++++++++console code
					boolean sessionEnd=false;
					try {
						HeartbeatThread TonyStark = new HeartbeatThread(peer);	
						TimeOutThread QasemSoleimani = new TimeOutThread(peer);
						QasemSoleimani.run();
						TonyStark.run();
					} catch (SocketException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					while(!sessionEnd) {	
						String s= scanner.nextLine();
							if(!(s.isEmpty())){
						        if(s.toUpperCase().equals("END")) {
						        	peer.SayBye(InetAddress.getByName("192.168.178.94"), 1422);
						        	sessionEnd=true;
						        }else if(s.equals("1")) {
						        	peer.ShowList();
						        }else if(s.equals("6")) {
						        	peer.ShowMusthaveIDs();
						        }
							}    
						}
					//++++++++++++++++console code
				}//main
}//class