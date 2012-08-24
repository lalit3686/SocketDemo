package com.example.androidclientdemo;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

	private EditText et_ip_address, et_port_number;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		initComponents();
	}
	
	private void initComponents() {
		et_ip_address = (EditText) findViewById(R.id.et_ip_address);
		et_port_number = (EditText) findViewById(R.id.et_port_number);
	}

	public void myOnClick(final View view) {
		int id = view.getId();
		switch (id) {
		case R.id.btn_server:

			new Thread(new Runnable() {

				@Override
				public void run() {
					ServerSide serverSide = new ServerSide();
					if(!TextUtils.isEmpty(et_port_number.getText().toString()))
					serverSide.sendServerData(MainActivity.this, Integer.parseInt(et_port_number.getText().toString().trim()));
					else{
						MainActivity.this.runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								Toast.makeText(view.getContext(), "Port Number can't be blank for Server.", Toast.LENGTH_LONG).show();
							}
						});
					}
				}
			}).start();
			break;
		case R.id.btn_client:
			new Thread(new Runnable() {

				@Override
				public void run() {
					ClientSide client = new ClientSide();
					if(!TextUtils.isEmpty(et_port_number.getText().toString()) && !TextUtils.isEmpty(et_ip_address.getText().toString()))
					client.sendClientData(MainActivity.this, et_ip_address.getText().toString().trim(), Integer.parseInt(et_port_number.getText().toString().trim()));
					else{
						MainActivity.this.runOnUiThread(new Runnable() {
							
							@Override
							public void run() {
								Toast.makeText(view.getContext(), "IP Address OR Port Number can't be blank for Client.", Toast.LENGTH_LONG).show();
							}
						});
					}
				}
			}).start();
			break;
		}
	}
}
