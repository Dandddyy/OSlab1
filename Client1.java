import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class Client1 {
    public static void main(String[] args) {
        try (Socket clientSocket = new Socket("localhost", 2020);
             DataInputStream in = new DataInputStream(clientSocket.getInputStream());
             DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream())) {

            int x = in.readInt();
            x = f(x);
            out.writeInt(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int f(int x) throws InterruptedException {
        //Thread.sleep(6000);

        int y = ((x * x) + (x * 2)) / 3;
        return y;
    }
}
