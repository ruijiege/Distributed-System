package edu.buffalo.cse.cse486586.groupmessenger;

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
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import edu.buffalo.cse.cse486586.groupmessenger.Algorithm;

public class GroupMessengerActivity extends Activity {

	private Button Send;
	private Button TestCase1Button;
	private Button TestCase2Button;
	private TextView tv;
	private EditText SendMessenge;
	public  static String portStr;
	public static boolean testCase1Flag=false;

	public Algorithm myGroupMessenger;
	private String msgs;
	public static int id=0;
	public static int sequenceNumber=0;

	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_messenger);


        TelephonyManager tel =
                (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);

        portStr = tel.getLine1Number().substring(tel.getLine1Number().length()-4);

        Send=(Button)findViewById(R.id.button4);
        tv=(TextView)findViewById(R.id.textView1);
        TestCase1Button=(Button)findViewById(R.id.button2);
        TestCase2Button=(Button)findViewById(R.id.button3);
        SendMessenge=(EditText)findViewById(R.id.editText1);
        myGroupMessenger = new Algorithm(portStr, getContentResolver(), tv);

		Send.setOnClickListener(new StartSocketListener());	
		TestCase1Button.setOnClickListener(new TestCase1Listener(portStr));
		TestCase2Button.setOnClickListener(new TestCase2Listener(portStr));

        tv.setMovementMethod(new ScrollingMovementMethod());
        findViewById(R.id.button1).setOnClickListener(new OnPTestClickListener(tv, getContentResolver()));
        
    	try {
            ServerSocket serverSocket = new ServerSocket(10000);
            new ServerTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, serverSocket);
    		}catch(IOException e){
    			e.printStackTrace();
    		}
    }
    
	private class ServerTask extends AsyncTask<ServerSocket, String, Void>{
	    @Override
	    protected Void doInBackground(ServerSocket...sockets){
	        String msg = null;
	        ServerSocket serverSocket = sockets[0];
	        Socket socket;
	        try{
	            while(true){	            	
	                socket = serverSocket.accept(); 
	                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		            msg = in.readLine();
		            myGroupMessenger.GroupMessengerAlg(msg);
	                socket.close();  
	            }
	        }
	        catch (IOException e) {	            
	        }
	        return null;
	    }
	}
	
	
	class StartSocketListener implements OnClickListener{
		@Override
		public void onClick(View v){	
			String msg;
     	   	if (portStr.equals("5554")){    
                 msgs="avd0:";
                 msg=msgs+id+"."+msgs+SendMessenge.getText().toString();
     	   	}
     		else if(portStr.equals("5556")){    
                 msgs="avd1:";
                 msg=msgs+id+"."+msgs+SendMessenge.getText().toString();
     		}
     		else{    	
                 msgs="avd2:";
                 msg=msgs+id+"."+msgs+SendMessenge.getText().toString();
     		}    	   	
     	   	id=id+1;
			SendMessenge.setText("");
			new ClientTask().execute(msg);
		}
	}
	
	public static class ClientTask extends AsyncTask<String, Void, Void>{
	    @Override
	    protected Void doInBackground(String...msgs){
	    	Socket socket1;
	    	Socket socket2;
	    	Socket socket3;

	    	try{ 
	    		socket1=new Socket("10.0.2.2",11108);
				socket2=new Socket("10.0.2.2",11112);
				socket3=new Socket("10.0.2.2",11116);	
	            PrintWriter out1 = new PrintWriter(socket1.getOutputStream());
	            PrintWriter out2 = new PrintWriter(socket2.getOutputStream());
	            PrintWriter out3 = new PrintWriter(socket3.getOutputStream());

                out1.println(msgs[0]);                
                out2.println(msgs[0]);                 
                out3.println(msgs[0]);                
                out1.flush();
                out2.flush();
                out3.flush();                                          
	            socket1.close();
	            socket2.close();
	            socket3.close();
             }catch (IOException e) {	            
 	        }
			return null;
	    }
	}
	

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_group_messenger, menu);
        return true;
    }
}
