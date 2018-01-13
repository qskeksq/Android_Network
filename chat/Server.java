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
			// 1. 클라이언트 요청을 대기
			Socket client;
			try {
				client = server.accept();
				// 2. 동시에 여러 요청이 들어올 떄 이곳에서 처리해주면 다른 요청을 받을 수 없으므로 스트리밍 처리는 다른 스레드로 빼준다
				new ProcessRequest(client);
			} catch (IOException e) {
				e.printStackTrace();
			}	
			
		}
	}

}
