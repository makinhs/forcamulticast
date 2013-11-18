package ctrl;

import java.io.IOException;
import java.io.Serializable;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

public class MultiCastPeer implements Runnable, Serializable {

    private static final long serialVersionUID = 1L;
    private final String HOST = "229.0.0.50";
    private final int PORT = 5050;
    private MulticastSocket socket;

    public MultiCastPeer() {
        try {
            InetAddress group = InetAddress.getByName(HOST);
            socket = new MulticastSocket(PORT);
        } catch (UnknownHostException e) {
            System.out.println("Erro no host: " + e.getLocalizedMessage());
        } catch (IOException e) {
            System.out.println("Erro I/O: " + e.getLocalizedMessage());
        }
    }

    @Override
    public void run() {
        while (!socket.isClosed()) {

        }
    }
}
