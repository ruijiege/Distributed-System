package edu.buffalo.cse.cse486586.groupmessenger;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;

public class TestCase2Listener implements OnClickListener {
	private String portStr;	    
	private String msgInit;
	public static boolean test2flag=false;

	
	public TestCase2Listener(String _t2PS) {
		portStr=_t2PS;
	}

	private String initTestValues() {
		String msg;
    	if (portStr.equals("5554")){
    		msg="avd0:"+GroupMessengerActivity.id;
    		GroupMessengerActivity.id=GroupMessengerActivity.id+1;
		}
		else if(portStr.equals("5556")){
			msg="avd1:"+GroupMessengerActivity.id;
			GroupMessengerActivity.id=GroupMessengerActivity.id+1;
		}
		else{
			msg="avd2:"+GroupMessengerActivity.id;
			GroupMessengerActivity.id=GroupMessengerActivity.id+1;
		}
        return msg;
	}

	
	@Override
	public void onClick(View v) {
		msgInit = initTestValues();
		test2flag=true;
		new ClientTask().execute("tests2");
		new ClientTask().execute(msgInit);
	}

	public class ClientTask extends AsyncTask<String, Void, Void>{		
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
	            msgs[0]=msgs[0]+"."+msgs[0];
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
            	 e.printStackTrace();
  	        }
			return null;
	    }
	}
}
