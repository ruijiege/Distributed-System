package edu.buffalo.cse.cse486586.simplemessenger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

/**
 * This is a simple messenger.
 * Two phones can send messages to each other after installing this app.
 * @author ruijiege
 */
public class MainActivity extends Activity {
	private Button Send;
	private TextView ContentMessenge;
	private EditText SendMessenge;
	private String portStr;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

        TelephonyManager tel =
                (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        portStr = tel.getLine1Number().substring(tel.getLine1Number().length()-4);

        Send=(Button)findViewById(R.id.SendButton);
        ContentMessenge=(TextView)findViewById(R.id.ContentMessenge);
        SendMessenge=(EditText)findViewById(R.id.SendMessenge);
		Send.setOnClickListener(new StartSocketListener());	
		
	try {
		//set up socked, bind port 10000
        ServerSocket serverSocket = new ServerSocket(10000);
        //start the server, blocked here, waiting new messages
        new ServerTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, serverSocket);
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	
	private class ServerTask extends AsyncTask<ServerSocket, String, Void>{
		//private String strings;
	    @Override
	    protected Void doInBackground(ServerSocket...sockets){
	        String msg = null;
	        ServerSocket serverSocket = sockets[0];
	        Socket socket;
	        try{
	            while(true){
	                socket = serverSocket.accept(); //wait for the client for request;
	                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	                msg = in.readLine();
	                publishProgress(msg); //publish to the activity
	                socket.close();  
	            }
	        } catch (IOException e) {
	            
	        }
	        return null;
	    }
	    
	    //Update UI
	    protected void onProgressUpdate(String...strings){
	        //communicate with main thread;
	    	ContentMessenge.append("\n"+"you:"+strings[0]);
	        return;
	    }
	}
	
	/**
	 * Button listener.
	 * After click the 'Send' button, send out the message.
	 * @author ruijiege
	 *
	 */
	class StartSocketListener implements OnClickListener{
		@Override
		public void onClick(View v){
			String msg=SendMessenge.getText().toString();
			ContentMessenge.setText(ContentMessenge.getText().toString()+"\n"+"me:"+msg);
			SendMessenge.setText("");
			new ClientTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, msg);
		}
	}
	
	/**
	 * Use AsyncTask to send string type messages.
	 * @param String is the message we want to sent.
	 * @author ruijiege
	 */
	public class ClientTask extends AsyncTask<String, Void, Void>{
	    @Override
	    protected Void doInBackground(String...msgs){
	    	Socket socket;
	    	try{ 
				if (portStr.equals("5554")){					
					socket=new Socket("10.0.2.2",11112);
				}
				else{
					socket=new Socket("10.0.2.2",11108);
				}
             PrintWriter out = new PrintWriter(socket.getOutputStream());
             out.println(msgs[0]);
             out.flush();
             socket.close();
             }catch (IOException e) {	            
 	        }
			return null;
	    }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

}





