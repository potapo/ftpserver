package ftpserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class ftpclient extends Thread {
	Socket ftp;
	Socket pasvftp;

	public ftpclient() {

	}

	public ftpclient(Socket ftp) {
		// TODO Auto-generated constructor stub
		super();
		this.ftp = ftp;
	}

	@Override
	public void run() {
		try {
			OutputStream ftpOut = ftp.getOutputStream();
			InputStream ftpIn = ftp.getInputStream();
			ftpOut.write("220 LZB FTP server v0.01 for winsock ready \r\n".getBytes());
			ftpOut.flush();
			byte[] bb = new byte[1024];
			String username = new String();
			while (ftpIn.read(bb) != -1) {
				username = username + new String(bb);
				if (username.indexOf("\r\n") != -1)
					break;
			}
			System.out.println(username);
			ftpOut.write("331 User name okay.please send e-mail address as password.\r\n".getBytes());
			ftpOut.flush();
			String password = new String();
			bb = new byte[1024];
			while (ftpIn.read(bb) != -1) {
				password = password + new String(bb);
				if (username.indexOf("\r\n") != -1)
					break;
			}
			// CONSOLE.LOG
			System.out.println(password.substring(0, password.length() - 2));
			ftpOut.write("230 User logged in,proceed.\r\n".getBytes());
			ftpOut.flush();
			String command = new String();
			BufferedReader read = new BufferedReader(new InputStreamReader(ftpIn));
			while (true) {
				while (true) {
					command = read.readLine();
					System.out.println(command);
					if (command != null) break;
				}
				String para = command.toLowerCase();
				switch (para) {
				case "quit":
					ftpOut.write("221 Goodbye!\r\n".getBytes());
					ftpOut.flush();
					break;
				case "pwd":
					ftpOut.write("257 \" \\ \"is current directory.\r\n".getBytes());
					ftpOut.flush();
					break;
				case "pasv":
					ftpOut.write("227 Entering Passive mode (192,168,1,107,117,13)".getBytes());
					ftpOut.flush();
					new Thread(this.new p()).start();

					System.out.println("pasv mode starting...");
					break;
				case "list -l":
					ftpOut.write("150 Openiing ASCII mode data connection for /bin/ls.\r\n".getBytes());
					ftpOut.flush();
					list();
					ftpOut.write("226 TRANSFER COMPLETE.\r\n".getBytes());
					break;
				case "syst":
					ftpOut.write("215 UNIX TYPE L8.\r\n".getBytes());
					ftpOut.flush();
				case "type i":
					ftpOut.write("200 Type set to I.\r\n".getBytes());
					ftpOut.flush();
				case "size /":
					ftpOut.write("500 no such file or dorectory.\r\n".getBytes());
					ftpOut.flush();
				case "cwd":
					ftpOut.write("250 change to dir \r\n".getBytes());
					ftpOut.flush();
				default:
					System.out.println("none");
					ftpOut.write("500 no such file or dorectory.\r\n".getBytes());
					ftpOut.flush();
					break;
				}

			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void list() {
		// TODO Auto-generated method stub
		try {
			Process p = Runtime.getRuntime().exec("/bin/ls -l /Users/liangzhibang");
			InputStream stdout = p.getInputStream();
			BufferedReader reader = new BufferedReader(new InputStreamReader(stdout));
			String line;
			StringBuffer buf = new StringBuffer();
			while ((line = reader.readLine()) != null)
				buf.append(line);
			OutputStream o = pasvftp.getOutputStream();
			o.write(new String(buf.toString() + "\r\n").getBytes());
			o.flush();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	class p implements Runnable {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				ServerSocket pasv = new ServerSocket(29965);
				System.out.println(ftp.toString());
					pasvftp = pasv.accept();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

}
