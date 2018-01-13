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
				// 1. 클라이언트 연결 대기
				Socket client = server.accept();
				// 2. 요청에 대한 처리는 새로운 thread에서 해준다
				new Thread() {
					public void run() {
						try {
							// 3. 스트림 연결
							InputStreamReader isr = new InputStreamReader(client.getInputStream());
							BufferedReader br = new BufferedReader(isr);
							// 4. 웹브라우저에서 요청한 주소로 줄단위의 명령어가 날아오는 것을 꺼내서 처리
							String line = br.readLine();
							// 5. 요청된 명령어의 첫 줄만 파싱해서 동작을 결정
							// Method/도메인을 제외한 주소/프토토콜 주소
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
//								Path로 처리할 수도 있다
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
									// 헤더와 바디를 구분하는 바디 구분자 전송(줄 띔)
									os.write("\r\n".getBytes());
									// 실제 전달되는 데이터
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
			// 화면에는 보이지 않는 메타정
			os.write("HTTP/1.0 200 OK \r\n".getBytes());
			os.write("Content-Type: text/html \r\n".getBytes());
			os.write(("Content-Length: "+content.getBytes().length+"\r\n").getBytes());
			// 헤더와 바디를 구분하는 바디 구분자 전송(줄 띔)
			os.write("\r\n".getBytes());
			// 실제 전달되는 데이터
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
