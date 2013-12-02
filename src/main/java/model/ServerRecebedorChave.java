package model;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.security.PrivateKey;
import java.security.PublicKey;

import util.Serializer;

public class ServerRecebedorChave {
	
	private int porta = 0;
	private Jogador jogador;
	
	public ServerRecebedorChave(int porta, Jogador j) {
		// TODO Auto-generated constructor stub
		this.porta = porta;
		this.jogador = j;
	}
	
	public void run()
	{
		DatagramSocket aSocket = null;
		try{
	    	aSocket = new DatagramSocket(porta);
	    	boolean laco = true;
					// create socket at agreed port
			byte[] buffer = new byte[1000];
 			while(laco){
 				System.out.println("Server RODANDO nick: " + jogador.getNick());
 				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
  				aSocket.receive(request);     
  				
  				 Object o = null;
                 try {
                     o = Serializer.deserialize(request.getData());
                     if (o instanceof PublicKey) {
                    	 PublicKey publicKey = ((PublicKey) o);
                    	
                    	 jogador.setChavePublica(publicKey);
                    	 System.out.println("Chave recebida nick: " + jogador.getNick());
                    	 laco = false;
                        
                     }
                 } catch (ClassNotFoundException e) {
                     // TODO Auto-generated catch block
                     e.printStackTrace();
                 }
  			
    		}
		}catch (SocketException e){System.out.println("Socket: " + e.getMessage());
		}catch (IOException e) {System.out.println("IO: " + e.getMessage());
		}finally {if(aSocket != null) aSocket.close();}
	}
	
	
}