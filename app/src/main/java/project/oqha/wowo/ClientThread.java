package project.oqha.wowo;

        import android.util.Log;

        import java.io.IOException;
        import java.io.InputStream;
        import java.io.OutputStream;
        import java.net.Socket;

/**
 * Created by oqha on 7/14/15.
 * @author bl4th4nk
 */
public class ClientThread extends Thread {
    private static final String LOG_TAG = "OqhaJect";

    private Socket mClientSocket;
    private Socket mServerSocket;
    private boolean mForwardingActive = false;

    public ClientThread(Socket aClientSocket) {
        mClientSocket = aClientSocket;
    }

    /**
     * Establishes connection to the destination server and
     * starts bidirectional forwarding ot data between the
     * client and the server.
     */
    public void run() {
        InputStream clientIn;
        OutputStream clientOut;
        InputStream serverIn;
        OutputStream serverOut;
        try {
            // Connect to the destination server
            mServerSocket = new Socket(ForegroundService.DESTINATION_HOST,ForegroundService.DESTINATION_PORT);

            // Turn on keep-alive for both the sockets
            mServerSocket.setKeepAlive(true);
            mClientSocket.setKeepAlive(true);

            // Obtain client & server input & output streams
            clientIn = mClientSocket.getInputStream();
            clientOut = mClientSocket.getOutputStream();
            serverIn = mServerSocket.getInputStream();
            serverOut = mServerSocket.getOutputStream();
        } catch (IOException ioe) {
            Log.i(LOG_TAG,"Can not connect to" +
                    ForegroundService.DESTINATION_HOST + ":" +
                            ForegroundService.DESTINATION_PORT);
            connectionBroken();
            return;
        }

        // Start forwarding data between server and client
        mForwardingActive = true;
        ForwardThread clientForward = new ForwardThread(this, clientIn, serverOut);
        clientForward.start();
        ForwardThread serverForward = new ForwardThread(this, serverIn, clientOut);
        serverForward.start();

        Log.i(LOG_TAG,"MemForward " +
                mClientSocket.getInetAddress().getHostAddress() +
                ":" + mClientSocket.getPort() + " <> " +
                mServerSocket.getInetAddress().getHostAddress() +
                ":" + mServerSocket.getPort() + " diMulai.");
    }


    public synchronized void connectionBroken() {
        try {
            mServerSocket.close();
        } catch (Exception e) {}
        try {
            mClientSocket.close(); }
        catch (Exception e) {}

        if (mForwardingActive) {
            Log.i(LOG_TAG,"MemForward " +
                    mClientSocket.getInetAddress().getHostAddress()
                    + ":" + mClientSocket.getPort() + " <--> " +
                    mServerSocket.getInetAddress().getHostAddress()
                    + ":" + mServerSocket.getPort() + " berHenti.");
            mForwardingActive = false;
        }
    }
}