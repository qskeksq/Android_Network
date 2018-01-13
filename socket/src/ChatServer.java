import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ChatServer {
	public static void main(String[] args) {
		Server server = new Server(10004);
		server.start();
	}
}

class Server extends Thread{
	ServerSocket server;
	public boolean runFlag = true;
	// 0. �������� ����
	public Server(int port){
		try{
			server = new ServerSocket(port);
		}catch(Exception e){}
	}
	public void run(){
		System.out.println("server is running...");
		while(runFlag){
			try{
				// 1. Ŭ���̾�Ʈ�� ��û�� ���
				Socket client = server.accept(); // �Ʒ��� �ڵ�� ���ӿ�û�� �ޱ� �������� ������� �ʴ´�
				new ClientProcess(client).start();
			}catch(Exception e){}
		}
	}
}
// Ŭ���̾�Ʈ ��û�� ���� thread�� ó���ϴ� Ŭ����
class ClientProcess extends Thread{
	Socket client;
	public ClientProcess(Socket client){
		this.client = client;
	}
	public void run(){
		try{
			// 1. client�� stream�� ����
			InputStreamReader isr = new InputStreamReader(client.getInputStream());
			BufferedReader br = new BufferedReader(isr);
			String msg = "";
			// 2. exit�� �ƴҶ����� ���پ� �о ������ ��� 
			while((msg=br.readLine()) != null){
				if("exit".equals(msg)) break;
				System.out.println(client.getInetAddress()+":"+msg);
			}
			// ����ݱ�
			br.close();
			isr.close();
			client.close();
		}catch(Exception e){
			
		}
	}
}

