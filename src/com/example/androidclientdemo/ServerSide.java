package com.example.androidclientdemo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

import android.app.Activity;
import android.widget.Toast;

public class ServerSide {

	void sendServerData(Activity mActivity, int port) {
		
		boolean moreData = true;
		Socket socket = null;
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			System.out.println("Waiting for connection");
			mActivity.runOnUiThread(new MyUIRunnable("Waiting for connection", mActivity));
			
			if(socket == null || socket.isClosed()){
				socket = serverSocket.accept();	
			}

			mActivity.runOnUiThread(new MyUIRunnable("Connection received from " + socket.getInetAddress().getHostName(), mActivity));
			System.out.println("Connection received from " + socket.getInetAddress().getHostName());
			BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()), 2048);
			PrintStream output = new PrintStream(socket.getOutputStream());
			sendMessage("Connection successful", output, mActivity);
			
			String message;
			while(moreData){
				message = input.readLine();
				mActivity.runOnUiThread(new MyUIRunnable("received by server ->"+message, mActivity));
				System.out.println("received by server ->"+message);
				if(message.trim().equalsIgnoreCase("quit")){
					sendMessage("quit", output, mActivity);
					moreData = false;
				}
			}
			input.close();
			output.close();
			socket.close();
			serverSocket.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void sendMessage(String message, PrintStream output, Activity mActivity) {
		output.println(message);
		output.flush();
		mActivity.runOnUiThread(new MyUIRunnable("sent by server ->" + message, mActivity));
		System.out.println("sent by server ->" + message);
	}
	
	class MyUIRunnable implements Runnable
	{
		private String message;
		private Activity mActivity;
		
		public MyUIRunnable(String message, Activity mActivity) {
			this.message = message;
			this.mActivity = mActivity;
		}
		@Override
		public void run() {
			Toast.makeText(mActivity, message, Toast.LENGTH_LONG).show();
		}
	}
}
