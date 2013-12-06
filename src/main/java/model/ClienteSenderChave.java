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

/**
 * Classe responsável por enviar via UDP uma chave publica a um cliente e a associar esta e uma privada ao servidor
 * 
 *
 */
public class ClienteSenderChave {
	
	private Jogador jogador;
	private Server server;
	private ArrayList<PrivateKey> chavesPrivadas;
	private ArrayList<PublicKey> chavesPublicas;
	
	/**
	 * 
	 * @param j jogador a receber a chave publica
	 * @param s servidor
	 */
	public ClienteSenderChave(Jogador j, Server s) {
		this.jogador = j;
		this.server = s;
		inicializarChaves();
	}
	
	/**
	 * Método que inicializa o par de chaves publica/privada
	 */
	public void inicializarChaves()
	{
		chavesPrivadas = new ArrayList<PrivateKey>();
		chavesPublicas = new ArrayList<PublicKey>();
		
		for(int i=0; i<3; i++){
			ChaveSeguranca chave = new ChaveSeguranca();
			chavesPrivadas.add(chave.getPriv());
			chavesPublicas.add(chave.getPub());
		}
		
		
	}
	
	/**
	 * Método que envia via UDP 
	 * @param porta a porta do server UDP
	 * @param cont inteiro associado a uma lista contendo os jogadores
	 */
	public void enviaChavePublica(int porta, int cont)
	{
		// args give message contents and destination hostname
				DatagramSocket aSocket = null;
				try {
					aSocket = new DatagramSocket();    
					
					byte [] m = Serializer.serialize(chavesPublicas.get(cont));
					InetAddress aHost = InetAddress.getByName(Parameter.HOST_ADDRESS);					                                                
					DatagramPacket request =
					 	new DatagramPacket(m,  m.length, aHost, porta);
					aSocket.send(request);		
					
					for(Jogador jogador : server.getJogadores())
					{
						if(porta == jogador.getPorta())
						{
							server.addChavePublica(jogador.getId(), chavesPublicas.get(cont), chavesPrivadas.get(cont));
						}
					}

				}catch (SocketException e){System.out.println("Socket: " + e.getMessage());
				}catch (IOException e){System.out.println("IO: " + e.getMessage());
				}finally {if(aSocket != null) aSocket.close();}
	}

}
