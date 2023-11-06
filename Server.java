import java.net.ServerSocket;
import java.net.Socket;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.*;

public class Server {
    private static int timeLimitMilliseconds = 3000;
    private static int x;
    private static int x1;
    private  static int x2;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(2020)) {

            Scanner in = new Scanner(System.in);
            System.out.print("Input a number: ");
            x = in.nextInt();
            Socket clientSocket1 = serverSocket.accept();
            Socket clientSocket2 = serverSocket.accept();
            CompletableFuture<Void> client1Work = CompletableFuture.runAsync(() -> {
                handleClient1(clientSocket1);
            });
            CompletableFuture<Void> client2Work = CompletableFuture.runAsync(() -> {
                handleClient2(clientSocket2);
            });
            try {
                CompletableFuture<Void> allOf = CompletableFuture.allOf(client1Work, client2Work);
                allOf.get();
                System.out.println("XOR: " + XOR());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void handleClient1(Socket clientSocket) {
        try (
                DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
        ) {
            ExecutorService executor = Executors.newSingleThreadExecutor();

            Callable<Void> Run = () -> {
                long startTime = System.currentTimeMillis();

                out.writeInt(x);
                x1 = in.readInt();
                System.out.println("Received f(x): " + x1);
                long executionTime = System.currentTimeMillis() - startTime;
                System.out.println("Execution time f(x): " + executionTime + " milliseconds");

                return null;
            };

            Future<Void> future = executor.submit(Run);

            try {
                future.get(timeLimitMilliseconds, TimeUnit.MILLISECONDS);
            } catch (TimeoutException e) {
                future.cancel(true);
                System.out.println("Time limit exceeded: Time Error");
            } finally {
                executor.shutdown();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void handleClient2(Socket clientSocket) {
        try (
                DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                DataOutputStream out = new DataOutputStream(clientSocket.getOutputStream());
        ) {
            ExecutorService executor = Executors.newSingleThreadExecutor();

            Callable<Void> Run = () -> {
                long startTime = System.currentTimeMillis();

                out.writeInt(x);
                x2 = in.readInt();
                System.out.println("Received g(x): " + x2);
                long executionTime = System.currentTimeMillis() - startTime;
                System.out.println("Execution time g(x): " + executionTime + " milliseconds");

                return null;
            };

            Future<Void> future = executor.submit(Run);

            try {
                future.get(timeLimitMilliseconds, TimeUnit.MILLISECONDS);
            } catch (TimeoutException e) {
                future.cancel(true);
                System.out.println("Time limit exceeded: Time Error");
            } finally {
                executor.shutdown();
            }

            out.writeBoolean(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private static int XOR(){
        return x1 ^ x2;
    }
}