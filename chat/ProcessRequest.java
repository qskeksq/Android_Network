import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ProcessRequest extends Thread{
	
	Socket client;
	
	public ProcessRequest(Socket socket) {
		client = socket;
	}
	
	public void run() {
		try {
			// 1. 서버 생성
			ServerSocket server = new ServerSocket(10004);
			// 2. 요청 대기
			Socket client = server.accept(); // 스캐너의 next처럼 요청이 있을때까지 이 줄에서 대기함.
											 // 요청이 들어오면 클라이언트의 주소정보를 담은 소켓이 생성
			// 3. 접속된 client와 stream을 생성한다
			InputStreamReader isr = new InputStreamReader(client.getInputStream());
			BufferedReader br = new BufferedReader(isr); 
			String temp = "";
			StringBuilder sb = new StringBuilder();
			while(!"exit".equals((br.readLine()))) {
				sb.append(temp).append("\n");
				System.out.println(sb.toString());
			}
			System.out.println(sb.toString());
			isr.close();
			br.close();
			client.close();
			server.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

}
