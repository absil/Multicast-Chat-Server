import java.net.*;
import java.io.*;
import java.util.*;

class ChatClient {
	public static void main(String[] args){
	try{
	 	InetAddress group = InetAddress.getByName("239.0.202.1");
		MulticastSocket ms = new MulticastSocket(40202);
		ms.joinGroup(group);				
		byte[] buf = new byte[1500];
		DatagramPacket dp = new DatagramPacket(buf, buf.length);			//datagram packet used to recieve - passed to socket
		DatagramPacket send;
	
		MulticastThread thread = new MulticastThread(ms, dp);
		thread.start();

		byte[] bytes;

		//reads in the keyboard input
		while(true){
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			String in = reader.readLine();
			bytes = in.getBytes();			
			send = new DatagramPacket(bytes, bytes.length, group, 40202);
			ms.send(send);
		}

	}
	catch(Exception e){
		e.printStackTrace();
	}

	}	
}

class MulticastThread extends Thread{
	private MulticastSocket ms;
	private DatagramPacket dp;
	byte[] rec;	
	String data = "";

	public void run(){
		try{
			while(true){				
				ms.receive(dp);
				int length = dp.getLength();					
				rec = dp.getData();				
				data = new String(rec);
				System.out.println(dp.getAddress() + ": " + data.substring(0, length));
			}
		}
		catch(Exception e){
			System.err.println(e);
		}
	}

	public MulticastThread(MulticastSocket ms, DatagramPacket dp){
		this.ms = ms;
		this.dp = dp;
	}
}
