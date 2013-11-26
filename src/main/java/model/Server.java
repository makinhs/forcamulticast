package model;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;

import ctrl.MultiCastPeer;

public class Server {
	
	private ArrayList<Jogador> jogadores = new ArrayList<Jogador>();
	private int cont = 0, contPalavra = -1;
	private Jogador jogadorDaVez;
	private Palavra palavrasController;
	private String palavraDaVez = "";
	private MultiCastPeer mCast;
	private ArrayList<String> palavraTentada = new ArrayList<String>();
	
	public Server(List<Jogador> jogadores, MultiCastPeer mCast) {
		// TODO Auto-generated constructor stub
		this.jogadores.addAll(jogadores);
		this.mCast = mCast;
		jogadorDaVez = getJogadorDaVez();
		palavrasController = new Palavra();
		palavraDaVez = getProximaPalavra();
		
		
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
	
	public void executar()
	{
		DatagramSocket aSocket = null;
		ChaveLetraJogadorController chaveJogador = null;
		try{
	    	aSocket = new DatagramSocket(6789);
					// create socket at agreed port
			byte[] buffer = new byte[1000];
 			while(true){
 				DatagramPacket request = new DatagramPacket(buffer, buffer.length);
  				aSocket.receive(request); 
  				
  				//chaveJogador = (Cha,,)request;
  				controleJogo(chaveJogador);  				
  				
  				
  				
    			//DatagramPacket reply = new DatagramPacket(request.getData(), request.getLength(), 
    			//	request.getAddress(), request.getPort());
    			aSocket.send(controleJogo(chaveJogador));
    		}
		}catch (SocketException e){System.out.println("Socket: " + e.getMessage());
		}catch (IOException e) {System.out.println("IO: " + e.getMessage());
		}finally {if(aSocket != null) aSocket.close();}
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
	
	

}
