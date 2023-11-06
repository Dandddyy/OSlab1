import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

public class Client2 {
    public static void main(String[] args) {
        try (Socket clientSocket = new Socket("localhost", 2020);
             DataInputStream in = new DataInputStream(clientSocket.getInputStream());
             DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());) {

            int x = in.readInt();
            x = g(x);
            out.writeInt(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static int g(int x) throws InterruptedException {
        //Thread.sleep(3000);

        int y = x + (3 * x);
        return y;
    }
}
