import java.io.OutputStream;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {

	public static void main(String[] args) {
		// Ư�� ip�� port�� ���� ������ �����ؼ� �޽����� �����ϴ� ���α׷�
		Client client = new Client("192.168.1.120", 10004);
		client.start();
	}
}

class Client extends Thread{
	public boolean runFlag = true;
	String ip;
	int port;
	public Client(String ip,int port){
		this.ip=ip;
		this.port=port;
	}
	
	public void run(){
		try{
			// 1. ������ ����
			Socket socket = new Socket(ip, port);
			OutputStream os = socket.getOutputStream();
			// 2. �� �Է��� ���� ��ĳ�� ����
			Scanner scanner = new Scanner(System.in);
			while(runFlag){
				String msg = scanner.nextLine();
				// 3. ���� �Է��� ���� exit�̸� 
				if("exit".equals(msg))
					runFlag=false;
				msg = msg+"\r\n";
				os.write(msg.getBytes());
				os.flush();
			}
			os.close();
			socket.close();
		}catch(Exception e){}
	}
}