package ctrl;

import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.Jogador;
import util.Serializer;

public class MultiCastPeer extends Thread implements Serializable {

    private static final long serialVersionUID = 1L;
    private final String HOST = "229.0.0.50";
    private final int PORT = 5050;
    private final int TIMEOUT = 20000;
    private String usuario;
    private MulticastSocket socket;
    private InetAddress group;
    private Jogador jogador;

    public MultiCastPeer(Jogador jogador) {
        try {
            this.jogador = jogador;
            group = InetAddress.getByName(HOST);
            socket = new MulticastSocket(PORT);
            socket.setSoTimeout(TIMEOUT);
            socket.joinGroup(group);
            this.start();
        } catch (UnknownHostException e) {
            System.out.println("Erro no host: " + e.getLocalizedMessage());
        } catch (IOException e) {
            System.out.println("Erro I/O: " + e.getLocalizedMessage());
        }
    }

    @Override
    public void run() {  
        while (!socket.isClosed()) {
            if (this.jogador.getListaJogadores().size() < 4) {
                this.adicionarJogadores();
            }
        }
    }

    /**
     * Envia uma mensagem para o grupo multicast
     *
     * @param String msg
     */
    public void enviarMensagem(byte[] msg) {
        DatagramPacket msgOut = new DatagramPacket(msg, msg.length, group, PORT);
        try {
            socket.send(msgOut);
        } catch (IOException e) {
            System.out.println("Erro I/O: " + e.getLocalizedMessage());
        }
    }

    /**
     * Método responsável por limpar o buffer de dados
     *
     * @param byte [] buffer
     */
    private void cleanBuffer(byte[] buffer) {
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = 0;
        }
    }

    private void adicionarJogadores() {
        byte[] buffer = new byte[1024];
        DatagramPacket msgIn = new DatagramPacket(buffer, buffer.length, group, PORT);
        try {
            this.enviarMensagem(jogador.sendInfo());
            socket.receive(msgIn);
            Object o = Serializer.deserialize(msgIn.getData());
            if (o instanceof Jogador) {
                System.out.println(((Jogador) o).getNick());
                jogador.addJogador((Jogador) o);
            }
            sleep(500);
            // enviarMensagem("Recebido por " + usuario);
        } catch (IOException e) {
            System.out.println("Erro I/O: " + e.getLocalizedMessage());
        } catch (ClassNotFoundException ex) {
            System.out.println("Erro Serializacao: " + ex.getLocalizedMessage());
        } catch (InterruptedException ex) {
            System.out.println("Erro sleep: " + ex.getLocalizedMessage());
        } finally {
            cleanBuffer(buffer);
        }
    }
}