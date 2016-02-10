package ftpserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ftpserver {
	public ftpserver()
	{
		start(config.port,config.pwd);
	}
	public ftpserver(int port, String pwd) {
		// TODO Auto-generated constructor stub
		start(port,pwd);
	}

	private void start(int port, String pwd) {
		// TODO Auto-generated method stub
		try {
			ServerSocket ftpSocket=new ServerSocket(port);
			Socket s = ftpSocket.accept();
			ftpclient client=new ftpclient(s);
			client.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
