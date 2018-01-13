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
			// 1. ������ ����
			Socket socket = new Socket(ip, port);
			// 2. �� �Է��� ���� ��ĳ�� ����
			Scanner scanner = new Scanner(System.in);
			// 3. ��Ʈ��
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
