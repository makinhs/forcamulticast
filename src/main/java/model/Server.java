package model;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import util.Parameter;
import util.Serializer;
import ctrl.MultiCastPeer;

public class Server {
	
	private ArrayList<Jogador> jogadores = new ArrayList<Jogador>();
	private int cont = 0, contPalavra = -1;
	private Jogador jogadorDaVez;
	private Palavra palavrasController;
	private String palavraDaVez = "";
	private MultiCastPeer mCast;
	private ArrayList<String> palavraTentada = new ArrayList<String>();
	private ArrayList<PrivateKey> chavesPrivadas = new ArrayList<PrivateKey>();
	private boolean loopGetPrivateKey = true;
	private boolean loopMainGame = false;
	private int contLoopStillAlive = 0;
	
	



	public Server(List<Jogador> jogadores, MultiCastPeer mCast) {
		// TODO Auto-generated constructor stub
		this.jogadores.addAll(jogadores);
		this.mCast = mCast;
		palavrasController = new Palavra();
	}
	
	public boolean isLoopGetPrivateKey() {
		return loopGetPrivateKey;
	}
	
	
	public void addContLoop()
	{
		contLoopStillAlive+=88;
	}
	
	
	public boolean isLoopMainGame() {
		return loopMainGame;
	}

	public void addJogador(Jogador jogador)
	{
		jogadores.add(jogador);
	}
	
	public void addJogadores(List<Jogador> jogadores)
	{
		jogadores.addAll(jogadores);
	}
	
	public Jogador getJogadorDaVez()
	{
		Jogador jogadorDaVez = jogadores.get(cont);
		cont++;
		if(cont == jogadores.size())
			cont = 0;
		
		return jogadorDaVez;
	}
	
	
	public void getChavesPrivadas()
	{
		DatagramSocket aSocket = null;
		ChaveLetraJogadorController chaveJogador = null;
		
		try{
			
			setDadosForca();
			
	    	
 			while(loopGetPrivateKey){
 				mandarMensagemHello();
 				System.out.println("recebendo chaves privadas");
 				aSocket = new DatagramSocket(6789);
				// create socket at agreed port
 				byte[] buffer = new byte[1000];
 				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
  				aSocket.receive(request); 
  				  				  				
  				Object o;
				try {
					o = Serializer.deserialize(request.getData());
					if (o instanceof PrivateKey) {
	  					PrivateKey privateKey = ((PrivateKey)o);
	  					if(!chavesPrivadas.contains(privateKey))
	  					{
	  						chavesPrivadas.add(privateKey);
	  					}  					               
	  				}
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
  				
				if(chavesPrivadas.size() == 3)
				{
					loopGetPrivateKey = false;
					loopMainGame = true;
					mCast.enviarAvisoPrivadasFim(Parameter.CHAVES_PRIVADAS_RECEBIDAS.getBytes());
				}
  				
  				System.out.println("Lista de chaves privadas: " + chavesPrivadas.size());
  				  				
  						

    		}
		}catch (SocketException e){;
		}catch (IOException e) {;
		}finally {if(aSocket != null) aSocket.close();}
	}


	
	
	private void mandarMensagemHello() {
		// TODO Auto-generated method stub
		addContLoop();
		String hello = "hello";
		if(contLoopStillAlive >= Parameter.DELTA_T1_SERVER_MANDAR_HELLO)
		{
			mCast.enviarMensagem(hello.getBytes());
			contLoopStillAlive = 0;
		}
		
	}

	private void adicionaChavePrivada(Object o) {
		
		
		
	}



	public void startJogo()
	{
		DatagramSocket aSocket = null;
		ChaveLetraJogadorController chaveJogador = null;
		boolean isJogoLoop = true;
		try{
			
			setDadosForca();
			
	    	
 			while(isJogoLoop){
 				mandarMensagemHello();
 				System.out.println("server inicializado");
 				aSocket = new DatagramSocket(6789);
				// create socket at agreed port
 				byte[] buffer = new byte[1000];
 				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
  				aSocket.receive(request); 
  				
//  				System.out.println(new String(request.getData()));
  				  				
  				
  						
  				
  				//chaveJogador = (Cha,,)request;
//  				controleJogo(chaveJogador);  				
  				
  				
  				
    			//DatagramPacket reply = new DatagramPacket(request.getData(), request.getLength(), 
    			//	request.getAddress(), request.getPort());
//    			aSocket.send(controleJogo(chaveJogador));
    		}
		}catch (SocketException e){System.out.println("Socket: " + e.getMessage());
		}catch (IOException e) {System.out.println("IO: " + e.getMessage());
		}finally {if(aSocket != null) aSocket.close();}
	}

	private void setDadosForca() {
		jogadorDaVez = getJogadorDaVez();		
//		palavraDaVez = getProximaPalavra();
		
	}



	private DatagramPacket controleJogo(ChaveLetraJogadorController chaveJogador) {
		// TODO Auto-generated method stub
//		DatagramPacket replyPacket = null;
//		int idJogador = chaveJogador.getId();
//		if(jogadorDaVez.getId() == idJogador)
//		{
//
//			//verifica se o usuario tentou a palavra inteira
//			if(chaveJogador.getLetra().length()>1)
//			{
//				if(chaveJogador.getLetra().equals(palavraDaVez))
//				{
//					//acertou, fim
//					
//				}
//				else
//				{
//					
//					//fim, perdeu partida
//				}
//				
//				limparDados();
//			}
//			if(chaveJogador.getLetra().length()==1)
//			{
//				if(palavraDaVez.contains(chaveJogador.getLetra()))
//				{
//					
//				}
//			}
//			
//			//escolhe o proximo jogador (mesmo que tenha passado a palavra em branco)
//			jogadorDaVez = getJogadorDaVez();
//		}
		
//		return replyPacket;
		return null;
		
	}



	private void limparDados() {
		// TODO Auto-generated method stub
		palavraDaVez = getProximaPalavra();
		palavraTentada = new ArrayList<String>();						
	}



	private String getProximaPalavra() {
		contPalavra++;
		return palavrasController.getPalavras().get(contPalavra);
		
	}



	public String getAddress() {
		// TODO Auto-generated method stub
		return Parameter.HOST_ADDRESS;
	}
	
	

}
