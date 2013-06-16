///**
// * 
// */
//package test.org.korsakow.rule;
//
//import java.io.DataInputStream;
//import java.io.IOException;
//import java.net.ServerSocket;
//import java.net.Socket;
//
//public class XMLSocket
//{
//	private static class Client extends Thread
//	{
//		private Socket socket;
//		private DataInputStream inputStream;
//		private volatile String lastMessage;
//		public Client(Socket socket) throws IOException
//		{
//			super("Client");
//			setDaemon(true);
//			this.socket = socket;
//			inputStream = new DataInputStream(socket.getInputStream());
//		}
//		public void close()
//		{
//			synchronized (this) {
//				if (this.socket == null)
//					return;
//			}
//			try { inputStream.close(); } catch (IOException e) {}
//			try { socket.shutdownInput(); } catch (IOException e) {}
//			try { socket.shutdownOutput(); } catch (IOException e) {}
//			try { socket.close(); } catch (IOException e) {}
//			
//			synchronized (this) {
//				this.inputStream = null;
//				this.socket = null;
//				
//				synchronized (this) {
//					this.notifyAll();
//				}
//			}
//		}
//		public String getString()
//		{
//			return lastMessage;
//		}
//		public String read() throws IOException
//		{
//			System.out.println("Client.read.begin");
//			int r = 0;
//			StringBuilder sb = new StringBuilder();
//			while ((r = inputStream.read()) != -1)
//			{
//				if (r == 0) // end of message
//					break;
//				sb.append((char)r);
//			}
//			System.out.println("Client.read.end");
//			return sb.toString();
//		}
//		public void waitFor() throws InterruptedException
//		{
//			synchronized (this) {
//				if (this.socket != null)
//					this.wait();
//			}
//		}
//		public void run()
//		{
//			try {
//				String str = read();
//				lastMessage = str;
//			} catch (IOException e) {
//				e.printStackTrace();
//			} finally {
//				close();
//			}
//			
//		}
//	}
//	private class Accept extends Thread
//	{
//		private ServerSocket serverSocket;
//		private volatile XMLSocket.Client client;
//		public Accept(int port) throws IOException
//		{
//			super("accept");
//			setDaemon(true);
//			serverSocket = new ServerSocket(port);
//			System.out.println("Accept.listening: " + port);
//		}
//		public XMLSocket.Client getClient()
//		{
//			return client;
//		}
//		public void close()
//		{
//			try { serverSocket.close(); } catch (IOException e) {}
//		}
//		public void waitFor() throws InterruptedException
//		{
//			synchronized (this) {
//				this.wait();
//			}
//		}
//		public void run()
//		{
//			if (true)
//			{
//				try {
//					System.out.println("Accept.waiting");
//					Socket socket = serverSocket.accept();
//					client = new Client(socket);
//					System.out.println("Accept.new client");
//					client.start();
//				} catch (IOException e) {
//					// 
//				} finally {
//					if (client != null)
//						client.close();
//					synchronized (this) {
//						this.notifyAll();
//					}
//				}
//			}
//		}			
//	}
//	private Accept accept;
//	public XMLSocket(int port) throws IOException
//	{
//		accept = new Accept(port);
//		accept.start();
//	}
//	public void waitFor() throws InterruptedException
//	{
//		System.out.println("Waiting for accept");
//		accept.waitFor();
//		System.out.println("Waiting for client");
//		XMLSocket.Client client = accept.getClient();
//		if (client != null)
//			client.waitFor();
//		System.out.println("Waiting done");
//	}
//	public String getString()
//	{
//		XMLSocket.Client client = accept.getClient();
//		if (client == null)
//			return null;
//		return client.getString();
//	}
//}