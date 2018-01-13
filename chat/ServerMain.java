import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerMain {

	// ���������� ���� ���� �������α׷����� request ��û
	public static void main(String[] args) {
		try {
			// 1. ���� ����
			ServerSocket server = new ServerSocket(10004);
			// 2. ��û ���
			Socket client = server.accept(); // ��ĳ���� nextó�� ��û�� ���������� �� �ٿ��� �����.
											 // ��û�� ������ Ŭ���̾�Ʈ�� �ּ������� ���� ������ ����
			// 3. ���ӵ� client�� stream�� �����Ѵ�
			InputStreamReader isr = new InputStreamReader(client.getInputStream());
			BufferedReader br = new BufferedReader(isr); 
			String temp = "";
			StringBuilder sb = new StringBuilder();
			while((temp = br.readLine()) != null) {
				sb.append(temp).append("\n");
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
