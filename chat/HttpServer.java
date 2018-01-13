import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.activation.MimetypesFileTypeMap;


public class HttpServer {

	public static void main(String[] args) {
		WebServer server = new WebServer(8092);
		server.start();
	}
}

class WebServer extends Thread {
	
	boolean runFlag = true;
	ServerSocket server;
	String FILE_DIR = "C:\\temp\\";
	String FILE_NAME = "text.txt";
	
	public WebServer(int port) {
		try {
			server = new ServerSocket(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void run() {
		while(runFlag) {
			try {
				// 1. Ŭ���̾�Ʈ ���� ���
				Socket client = server.accept();
				// 2. ��û�� ���� ó���� ���ο� thread���� ���ش�
				new Thread() {
					public void run() {
						try {
							// 3. ��Ʈ�� ����
							InputStreamReader isr = new InputStreamReader(client.getInputStream());
							BufferedReader br = new BufferedReader(isr);
							// 4. ������������ ��û�� �ּҷ� �ٴ����� ��ɾ ���ƿ��� ���� ������ ó��
							String line = br.readLine();
							// 5. ��û�� ��ɾ��� ù �ٸ� �Ľ��ؼ� ������ ����
							// Method/�������� ������ �ּ�/�������� �ּ�
							System.out.println("line = "+line);
							String[] cmd = line.split(" ");
							System.out.println("cmd = "+cmd[1]);
							if("/hello".equals(cmd[1])) {
								String msg = "<h1>Hello~~~~</h1>";
								writeOnServer(client, msg);
							} else if("/text.txt".equals(cmd[1])) {
								String content = readFile(FILE_NAME);
								writeOnServer(client, content);
							} else if("/image.jpeg".equals(cmd[1])){
								System.out.println("/image.jpeg");
//								Path�� ó���� ���� �ִ�
//								Path path = Paths.get(FILE_DIR+FILE_NAME);
//								byte[] content = Files.readAllBytes(path);
//								if(Files.exists(path)) {
//									OutputStream os = client.getOutputStream();
//									String mimeType = Files.probeContentType(path);
//									
//								}
//								
								File file = new File(FILE_DIR+cmd[1]);
								if(file.exists()) {
									OutputStream os = client.getOutputStream();
									String mimeType = new MimetypesFileTypeMap().getContentType(file);
									if("plain/text".equals(mimeType)) {
										os.write("Content-Type: text/html \r\n".getBytes());
									} else {
										os.write(("Content-Type: "+mimeType+" \r\n").getBytes());
									}
									String content = readFile("food");
									os.write(("Content-Length: "+content.getBytes().length+"\r\n").getBytes());
									// ����� �ٵ� �����ϴ� �ٵ� ������ ����(�� ��)
									os.write("\r\n".getBytes());
									// ���� ���޵Ǵ� ������
									os.write(content.getBytes());
									os.flush();
								}
							}
							client.close();
						} catch(Exception e) {
							e.printStackTrace();
						}
					}
				}.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void writeOnServer(Socket client, String content) {
		try {
			OutputStream os = client.getOutputStream();
			// ȭ�鿡�� ������ �ʴ� ��Ÿ��
			os.write("HTTP/1.0 200 OK \r\n".getBytes());
			os.write("Content-Type: text/html \r\n".getBytes());
			os.write(("Content-Length: "+content.getBytes().length+"\r\n").getBytes());
			// ����� �ٵ� �����ϴ� �ٵ� ������ ����(�� ��)
			os.write("\r\n".getBytes());
			// ���� ���޵Ǵ� ������
			os.write(content.getBytes());
			os.flush();
		} catch(Exception e) {
			
		}
	}
	
	public String readFile(String filename) {
		String line = "";
		File file = new File(FILE_DIR+filename);
		if(file.exists()) {
			try {
				FileInputStream is = new FileInputStream(file);
				InputStreamReader reader = new InputStreamReader(is);
				BufferedReader br = new BufferedReader(reader);
				line = br.readLine();
				is.close();
				reader.close();
				br.close();
			} catch(Exception e) {
				
			}
		}
		return line;
	}
}
