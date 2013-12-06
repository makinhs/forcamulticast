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
import java.util.List;
import java.util.TreeMap;

import util.Criptografia;
import util.Parameter;
import ctrl.MultiCastPeer;

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

    private boolean loopGetPrivateKey = true;
    private int contLoopStillAlive = 0;
    private int contJogadorVez = 0;

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

    public Jogador getJogadorDaVez() {
        Jogador jogadorDaVez = null;
        try {
            jogadorDaVez = jogadores.get(cont);
            cont++;
            if (cont == jogadores.size()) {
                cont = 0;
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Estourou array de jogadores: " + e.getLocalizedMessage());
        }
        return jogadorDaVez;
    }

    // public ArrayList<PrivateKey> getChavesPrivadas() {
    //         return chavesPrivadas;
    // }

    private void mandarMensagemHello() {
        addContLoop();
        String hello = "hello";
        if (contLoopStillAlive >= Parameter.DELTA_T1_SERVER_MANDAR_HELLO) {
            mCast.enviarMensagem(hello.getBytes());
            contLoopStillAlive = 0;
        }

    }

    public void startJogo() {
        DatagramSocket aSocket = null;
        ChaveLetraJogadorController chaveJogador = null;
        boolean isJogoLoop = true;
        try {
            new Thread(new Runnable() {

                @Override
                public void run() {
                    mandarMensagemHello();
                }
            }).start();

            while (isJogoLoop) {
                //TODO: testar mensagem Hello... mudar pra outra thread seria melhor :)
                aSocket = new DatagramSocket(6789);
                // create socket at agreed port
                boolean msgRecebida = false;

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
                    //                    byte[] response = request.getData();

                    //mensagem recebida nao eh do jogador da vez
                    if (response == null) {
                        System.out.println("Não é o jogador da vez!");
                    } else {
                        String resposta = new String(response).trim();

                        //conseguiu ler a chave porém não é do jogador da vez
                        if (response[0] != 0) {

                            System.out.println("Não é o jogador da vez!");
                            contJogadorVez++;

                            if (contJogadorVez > 2) {
                                msgRecebida = true;
                                for (Jogador j : getJogadores()) {
                                    if (j.getId() == jogadorDaVez.getId()) {
                                        getJogadores().remove(j);
                                        break;
                                    }
                                }
                            }

                        } else {
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
            if (contJogadorVez > 2) {
                setDadosForca();
                System.out.println("tirando jogador");
                for (Jogador j : getJogadores()) {
                    if (j.getId() == jogadorDaVez.getId()) {
                        getJogadores().remove(j);
                        break;
                    }
                }
            }

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

    private DatagramPacket controleJogo(ChaveLetraJogadorController chaveJogador) {

        return null;

    }

    private void limparDados() {
        palavraDaVez = getProximaPalavra();
        palavraTentada = new ArrayList<String>();
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

    //        public void setChavesPublicas(ArrayList<PublicKey> chavesPublicas) {
    //                this.chavesPublicas = chavesPublicas;
    //        }

    //        public void setChavesPrivadas(ArrayList<PrivateKey> chavesPrivadas) {
    //                this.chavesPrivadas = chavesPrivadas;
    //        }

    //        public ArrayList<PublicKey> getChavesPublicas() {
    //                return chavesPublicas;
    //        }

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