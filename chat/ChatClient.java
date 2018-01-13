import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {

	public static void main(String[] args) {

		Client client = new Client("192.168.1.120", 10004);
		client.start();
		
	}

}

class Client extends Thread {
	
	boolean runFlag = true;
	String ip;
	int port;
	
	public Client(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}
	
	public void run() {
		try {
			// 1. 서버에 접속
			Socket socket = new Socket(ip, port);
			// 2. 내 입력을 받을 스캐너 생성
			Scanner scanner = new Scanner(System.in);
			// 3. 스트림
			OutputStream os = socket.getOutputStream();
			
			while(runFlag) {
				String msg = scanner.nextLine();
				if("exit".equals(msg)) {
					runFlag = false;
				} else {
					msg = msg + "\r\n";
					os.write(msg.getBytes());
					os.flush();
				}
				
			}
		} catch(Exception e) {
			
		}
		
	}

}
