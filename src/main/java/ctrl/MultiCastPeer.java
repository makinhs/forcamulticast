package ctrl;

import java.io.IOException;
import java.io.Serializable;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class MultiCastPeer extends Thread implements Serializable {

    private static final long serialVersionUID = 1L;
    private final String HOST = "229.0.0.50";
    private final int PORT = 5050;
    private final int TIMEOUT = 20000;
    private static int USER = 0;
    private String usuario;
    private MulticastSocket socket;
    private InetAddress group;

    public MultiCastPeer() {
        try {
            group = InetAddress.getByName(HOST);
            socket = new MulticastSocket(PORT);
            socket.setSoTimeout(TIMEOUT);
            socket.joinGroup(group);
            USER++;
            usuario = "Usuario " + USER;
            this.start();
        } catch (UnknownHostException e) {
            System.out.println("Erro no host: " + e.getLocalizedMessage());
        } catch (IOException e) {
            System.out.println("Erro I/O: " + e.getLocalizedMessage());
        }
    }

    @Override
    public void run() {
        byte[] buffer = new byte[1024];
        DatagramPacket msgIn = new DatagramPacket(buffer, buffer.length, group, PORT);
        enviarMensagem("GO");
        while (!socket.isClosed()) {
            try {
                socket.receive(msgIn);
                String res = new String(msgIn.getData()).trim();
                System.out.println(res);
                enviarMensagem("Recebido por " + usuario);
            } catch (IOException e) {
                System.out.println("Erro I/O: " + e.getLocalizedMessage());
            } finally {
                cleanBuffer(buffer);
            }
        }
    }

    /**
     * Envia uma mensagem para o grupo multicast
     * 
     * @param String
     *            msg
     */
    public void enviarMensagem(String msg) {
        DatagramPacket msgOut = new DatagramPacket(msg.getBytes(), msg.getBytes().length, group, PORT);
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
}
