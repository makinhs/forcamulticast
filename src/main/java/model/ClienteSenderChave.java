package model;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;

import util.Parameter;
import util.Serializer;

public class ClienteSenderChave {
	
	private Jogador jogador;
	private Server server;
	
	public ClienteSenderChave(Jogador j, Server s) {
		this.jogador = j;
		this.server = s;
		inicializarChaves();
	}
	
	public void inicializarChaves()
	{
		ArrayList<PrivateKey> chavesPrivadas = new ArrayList<PrivateKey>();
		ArrayList<PublicKey> chavesPublicas = new ArrayList<PublicKey>();
		
		for(int i=0; i<3; i++){
			ChaveSeguranca chave = new ChaveSeguranca();
			chavesPrivadas.add(chave.getPriv());
			chavesPublicas.add(chave.getPub());
		}
		
		server.setChavesPublicas(chavesPublicas);
		server.setChavesPrivadas(chavesPrivadas);
		
	}
	
	public void enviaChavePublica(int porta, int cont)
	{
		// args give message contents and destination hostname
				DatagramSocket aSocket = null;
				try {
					aSocket = new DatagramSocket();    
					
					byte [] m = Serializer.serialize(server.getChavesPublicas().get(cont));
					InetAddress aHost = InetAddress.getByName(Parameter.HOST_ADDRESS);					                                                
					DatagramPacket request =
					 	new DatagramPacket(m,  m.length, aHost, porta);
					aSocket.send(request);			                        
					
//					System.out.println("Enviado chave cliente: " + jogador.getNick());
					
//					byte[] buffer = new byte[1000];
//					DatagramPacket reply = new DatagramPacket(buffer, buffer.length);	
//					aSocket.receive(reply);
//					System.out.println("Reply: " + new String(reply.getData()));	
				}catch (SocketException e){System.out.println("Socket: " + e.getMessage());
				}catch (IOException e){System.out.println("IO: " + e.getMessage());
				}finally {if(aSocket != null) aSocket.close();}
	}

}
