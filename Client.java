
package cln;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {

	public static void main(String[] args) throws UnknownHostException, IOException {
		// TODO Auto-generated method stub 154.121.83.50  
		//--192.168.42.2 --192.168.42.129
		Socket soc=null;
		
			soc=new Socket("192.168.42.2",6666);
	
		
	File file= new File("K:/eclipse/OKprj/serverClient//client.txt")	;
		//Long length =file.length();
		byte[] b=new byte[20480];
		
		InputStream in =new FileInputStream(file);
		OutputStream out=soc.getOutputStream();
		
		int i;
		while((i=in.read(b))>0) {
		
		out.write(b,0,i);
		
		}
		
		out.close();
		in.close();
		soc.close();
}}//////
