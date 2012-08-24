package com.example.androidclientdemo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

import android.app.Activity;
import android.widget.Toast;

public class ClientSide {

	void sendClientData(Activity mActivity, String ip_adress, int port) {
		
		boolean moreData = true;
		Socket socket = null;
		try {
			if(socket == null || socket.isClosed()){
				socket = new Socket(ip_adress, port);	
			}
			
			mActivity.runOnUiThread(new MyUIRunnable("Connected to "+socket.getInetAddress().getHostName()+" in port "+socket.getPort(), mActivity));
			System.out.println("Connected to "+socket.getInetAddress().getHostName()+" in port "+socket.getPort());
			BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()), 2048);
			PrintStream output = new PrintStream(socket.getOutputStream());

			String message;
			while(moreData){
				message = input.readLine();
				mActivity.runOnUiThread(new MyUIRunnable("received by client ->" + message, mActivity));
				System.out.println("received by client ->" + message);
				sendMessage("Hi my server", output, mActivity);
				message = "quit";
				sendMessage(message, output, mActivity);
				if(message.equalsIgnoreCase("quit")){
					moreData = false;
				}
			}
			// This is final message sent by server.
			message = input.readLine();
			if(message != null){
				mActivity.runOnUiThread(new MyUIRunnable("received by client ->" + message, mActivity));
				System.out.println("received by client ->" + message);
			}
			input.close();
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	private void sendMessage(String message, PrintStream output, Activity mActivity) {
		output.println(message);
		output.flush();
		mActivity.runOnUiThread(new MyUIRunnable("sent by client ->" + message, mActivity));
		System.out.println("sent by client ->" + message);
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
