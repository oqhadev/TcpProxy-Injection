package project.oqha.wowo;


        import android.util.Log;

        import java.io.IOException;
        import java.io.InputStream;
        import java.io.OutputStream;
        import java.net.InetAddress;

/**
 * Created by oqha on 7/14/15.
 * @author bl4th4nk
 */
public class ForwardThread extends Thread {
    private static final String LOG_TAG = "OqhaJect";

    private static final int BUFFER_SIZE = 8192;

    InputStream mInputStream;
    OutputStream mOutputStream;
    ClientThread mParent;


    public ForwardThread(ClientThread aParent, InputStream
            aInputStream, OutputStream aOutputStream) {
        mParent = aParent;
        mInputStream = aInputStream;
        mOutputStream = aOutputStream;
    }


    public void run() {
        byte[] buffer = new byte[BUFFER_SIZE];
        int pos;
        String ResponseOK = "HTTP/1.0 200 Connection established\r\n\r\n";
        try {
            while (true) {
                int bytesRead = mInputStream.read(buffer);
                if (bytesRead == -1)
                    break;

                String data = new String (buffer, 0, bytesRead);
                if(data.indexOf("CONNECT") >= 0){
                    String[] split = data.split("\r\n");
                    String method = split[0];
                    String[] CmdArray = method.split(" ");

                    String RawUrl = CmdArray[1];
                    String[] Args = RawUrl.split(":");
                    String host = Args[0];
                    int port = Integer.parseInt(Args[1]);

                    InetAddress address = InetAddress.getByName(host);
                    //            String ip = address.getHostAddress();
                    //              String urlHost = ip + ":" + port;
                    String Protocol = CmdArray[2];

                    String payLoad =  "CONNECT "+host+":443 "+Protocol+"\r\n"+
                            "Host: 202.152.162.124\r\n\r\n"+
                            "CONNECT HTTP://202.152.162.124/login/auth/ HTTP/1.1\r\n"+
                            "Host: 202.152.162.124/login/auth\r\n\r\n";

                    Log.i(LOG_TAG, "mengInject Request");


                    mOutputStream.write(payLoad.getBytes());
                    mOutputStream.flush();
                }else{
                    String[] split = data.split("\r\n");
                    if(split[0].startsWith("HTTP")){
                        data = split[0].substring(8).trim();
                        pos = data.indexOf("200");
                        if(pos < 0){

                            mOutputStream.write(ResponseOK.getBytes());
                            mOutputStream.flush();
                        }else{
                            mOutputStream.write(buffer, 0, bytesRead);
                            mOutputStream.flush();
                            System.out.println("Respon 200 OK");

                        }
                    }else{
                        mOutputStream.write(buffer, 0, bytesRead);
                        mOutputStream.flush();

                    }
                }
            }
        } catch (IOException e) {

        }


        mParent.connectionBroken();
    }
}