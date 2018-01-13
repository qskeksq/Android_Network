import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server extends Thread {
	
	public boolean runFlag = true;
	ServerSocket server;
	
	public Server(int port) {
		try {
			server = new ServerSocket(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run() {
		System.out.println("server is running...");
		while(runFlag) {
			// 1. Ŭ���̾�Ʈ ��û�� ���
			Socket client;
			try {
				client = server.accept();
				// 2. ���ÿ� ���� ��û�� ���� �� �̰����� ó�����ָ� �ٸ� ��û�� ���� �� �����Ƿ� ��Ʈ���� ó���� �ٸ� ������� ���ش�
				new ProcessRequest(client);
			} catch (IOException e) {
				e.printStackTrace();
			}	
			
		}
	}

}
