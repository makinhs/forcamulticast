package model;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import util.Criptografia;
import util.Parameter;
import ctrl.MultiCastPeer;


/**
 * Classe responsável pelo Servidor UDP do jogo da Forca
 *
 */
public class Server {

    private ArrayList<Jogador> jogadores = new ArrayList<Jogador>();
    private int cont = 0, contPalavra = -1;
    private Jogador jogadorDaVez;
    private Palavra palavrasController;
    private String palavraDaVez = "";
    private String palavraAdivinhada = "";
    private MultiCastPeer mCast;
    private ArrayList<String> palavraTentada = new ArrayList<String>();
    // private ArrayList<PrivateKey> chavesPrivadas = new ArrayList<PrivateKey>();
    // private ArrayList<PublicKey> chavesPublicas = new ArrayList<PublicKey>();

    private TreeMap<Integer, PublicKey> cPublicas = new TreeMap<Integer, PublicKey>(new IdComparator());
    private TreeMap<Integer, PrivateKey> cPrivadas = new TreeMap<Integer, PrivateKey>(new IdComparator());
    
    private HashMap<Integer, Integer> idContNaoJogou = new HashMap<Integer, Integer>();

    private boolean loopGetPrivateKey = true;
    private int contLoopStillAlive = 0;
    private int contJogadorVez = 0;

    /**
     * Construtora que recebe a lista de Jogadores que são os clientes do jogo da Forca e o MultiCast do Servidor
     * @param jogadores 
     * @param mCast
     */
    public Server(List<Jogador> jogadores, MultiCastPeer mCast) {
        this.jogadores.addAll(jogadores);
        this.mCast = mCast;
        palavrasController = new Palavra();
        palavraDaVez = getProximaPalavra();
        this.initPalavraSecreta();
        setDadosForca();
    }

    public boolean isLoopGetPrivateKey() {
        return loopGetPrivateKey;
    }

    public void addContLoop() {
        contLoopStillAlive += 88;
    }

    public void addJogador(Jogador jogador) {
        jogadores.add(jogador);
    }

    public void addJogadores(List<Jogador> jogadores) {
        jogadores.addAll(jogadores);
    }

    
    /**
     * Método que retorna qual o jogador da vez da rodada.
     * @return retorna um Jogador caso exista ou NULL caso não tenha mais jogadores (no caso de terem todos caído...)
     */
    public Jogador getJogadorDaVez() {
        Jogador jogadorDaVez = null;
        try {
        	
        	if(jogadores.size() < cont)
        	{
        		String msg = "Sem jogadores para continuar";
        		mCast.enviarMensagem(msg.getBytes());
        	}
        	else
        	{        	
	            jogadorDaVez = jogadores.get(cont);
	            if(jogadorDaVez == null)
	            {
	            	for(int i=jogadores.size()-1;i>=0; i--)
	            	{
	            		if(!(jogadores.get(i)==null))
	            		{
	            			jogadorDaVez = jogadores.get(i);
	            			break;
	            		}
	            	}
	            }
	            cont++;
	            if (cont == jogadores.size()) {
	                cont = 0;
	            }
        	}
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Estourou array de jogadores: " + e.getLocalizedMessage());
        }
        return jogadorDaVez;
    }


    /**
     * Método que envia uma mensagem de Hello caso tenha dado estourado o tempo determinado para enviar a mensagem
     */
    private void mandarMensagemHello() {
        addContLoop();
        String hello = "hello";
        if (contLoopStillAlive >= Parameter.DELTA_T1_SERVER_MANDAR_HELLO) {
            mCast.enviarMensagem(hello.getBytes());
            contLoopStillAlive = 0;
        }

    }

    
    /**
     * Método que inicializa o jogo
     */
    public void startJogo() {
        DatagramSocket aSocket = null;        
        boolean isJogoLoop = true;
        try {
        	
        	//thread responsável de enviar mensagem de HELLO a cada intervalo de tempo.
            new Thread(new Runnable() {

                @Override
                public void run() {
                    mandarMensagemHello();
                }
            }).start();

            
            //Loop do jogo
            while (isJogoLoop) {
                //TODO: testar mensagem Hello... mudar pra outra thread seria melhor :)
                aSocket = new DatagramSocket(6789);
                // create socket at agreed port
                boolean msgRecebida = false;
                
                
                //Laço para receber a mensagem do jogador da vez.
                while (!msgRecebida) {
                    avisaVezDoJogador();
                    System.out.println("Vez do jogador: " + jogadorDaVez.getNick());

                    if (palavraTentada != null && !palavraTentada.isEmpty()) {
                        System.out.println("Letras já tentadas: ");
                        for (String s : palavraTentada) {
                            System.out.print(s.toUpperCase() + " ");
                        }
                        System.out.println();
                    }

                    byte[] buffer = new byte[128];
                    DatagramPacket request = new DatagramPacket(buffer, buffer.length);                    
                    aSocket.setSoTimeout(15000);
                    aSocket.receive(request);

                    int contadorChave = 0;

                    byte[] response = Criptografia.decriptarComChavePrivada(request.getData(), cPrivadas.get(jogadorDaVez.getId()));

                    //mensagem recebida nao eh do jogador da vez
                    if (response == null) {
                        System.out.println("Não é o jogador da vez!");
                    } else {
                        String resposta = new String(response).trim();

                        //conseguiu ler a chave porém não é do jogador da vez
                        if (response[0] != 0) {

                            System.out.println("Não é o jogador da vez!");
                            contJogadorVez++;

                            //tenta 3 vezes receber o jogador da vez, senão der certo passa para o próximo
                            if (contJogadorVez > 2) {
                                msgRecebida = true;
//                                for (Jogador j : getJogadores()) {
//                                    if (j.getId() == jogadorDaVez.getId()) {
////                                        getJogadores().remove(j);
////                                        break;
//                                    }
//                                }
                            }

                        } else {
                        	
                        	
                        	idContNaoJogou.put(jogadorDaVez.getId(), 0);
                        	
                        	
                            if (resposta.equals("")) {
                                System.out.println("Jogador passou a vez");
                            } else {
                                String[] respos = resposta.split(" ");
                                resposta = respos[0].trim();
                                if (testaLetra(resposta)) {
                                    String fim = "Acertou palavra!";
                                    System.out.println("---------//---------");
                                    System.out.println("Palavra acertada: " + palavraDaVez.toUpperCase());
                                    System.out.println("Jogador campeão: " + jogadorDaVez.getNick());
                                    mCast.enviarMensagem(fim.getBytes());
                                    this.novoRound();
                                } else {
                                    System.out.println("Palavra ainda não descoberta");
                                }
                            }
                            msgRecebida = true;
                            setDadosForca();
                        }
                    }

                }

            }
        } catch (SocketTimeoutException e) {
            //TODO: verificar quando só 1 jogador permanecer e encerrar o jogo. Verificar se quando for 'setDadosForca' se tem 
            //outro jogador para jogar, senão fechar o game...
            contJogadorVez++;
            System.out.println("timeout");
            
                
            	if(idContNaoJogou.containsKey(jogadorDaVez.getId()))
            	{
            		idContNaoJogou.put(jogadorDaVez.getId(), idContNaoJogou.get(jogadorDaVez.getId())+1);
            		
            		if(idContNaoJogou.get(jogadorDaVez.getId()) >=3)
            		{
            			for(Jogador j : jogadores)
            			{
            				if(j.getId() == jogadorDaVez.getId())
            				{
            					jogadores.remove(j);
            					
            				}
            			}
            			
            			jogadores.add(null);
            		}
            	}
            	else
            	{
            		idContNaoJogou.put(jogadorDaVez.getId(), 1);
            	}
            	
            	setDadosForca();
                
//            }

        } catch (SocketException e) {
            System.out.println("Socket: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO: " + e.getMessage());
        } finally {
            if (aSocket != null) {
                aSocket.close();
            }
        }
    }

    private boolean testaLetra(String resposta) {
        if (resposta.length() == 1) {
            if (!palavraTentada.contains(resposta)) {
                palavraTentada.add(resposta);
            }
        } else {
            if(resposta.equalsIgnoreCase(palavraDaVez))
                palavraAdivinhada = palavraDaVez;
        }
        this.atualizaPalavraTentada(resposta);
        System.out.println("---------//---------");
        System.out.println("Palavra adivinhada até o momento: " + palavraAdivinhada);
        System.out.println("---------//---------");
        return isPalavraAcertada();
    }

    private void atualizaPalavraTentada(String s) {
        if (s != null && !s.isEmpty()) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < palavraAdivinhada.length(); i++) {
                if (palavraDaVez.substring(i, i + 1).equalsIgnoreCase(s)) {
                    sb.append(s);
                } else {
                    sb.append(palavraAdivinhada.substring(i, i + 1));
                }
            }
            palavraAdivinhada = sb.toString().toUpperCase();
        }
    }

    private boolean isPalavraAcertada() {
        if (palavraDaVez.equalsIgnoreCase(palavraAdivinhada)) {
            return true;
        } else {
            return false;
        }
    }

    private void avisaVezDoJogador() {
        String msg = "Jogador da vez: " + jogadorDaVez.getNick();
        for (int i = 0; i < 1; i++) {
            mCast.enviarMensagem(msg.getBytes());
        }
    }

    private void setDadosForca() {
        jogadorDaVez = getJogadorDaVez();
        contJogadorVez = 0;
    }

    private void initPalavraSecreta() {
        for (int i = 0; i < palavraDaVez.length(); i++) {
            palavraAdivinhada += "_";
        }
    }


    private String getProximaPalavra() {
        List<String> list = palavrasController.getPalavras();
        Collections.shuffle(list);
        return list.get(0);

    }
    
    private void novoRound() {
        System.out.println("---------//---------");
        System.out.println("Novo round começando...");
        palavraDaVez = getProximaPalavra();
        this.initPalavraSecreta();
        palavraTentada.clear();
    }

    public String getAddress() {
        return Parameter.HOST_ADDRESS;
    }


    public ArrayList<Jogador> getJogadores() {
        return jogadores;
    }

    public void addChavePublica(Integer id, PublicKey publicKey, PrivateKey privateKey) {

        if (!cPublicas.containsKey(id)) {
            cPublicas.put(id, publicKey);
        }

        if (!cPrivadas.containsKey(id)) {
            cPrivadas.put(id, privateKey);
        }

    }

    public TreeMap<Integer, PublicKey> getcPublicas() {
        return cPublicas;
    }

}