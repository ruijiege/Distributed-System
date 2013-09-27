package edu.buffalo.cse.cse486586.groupmessenger;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;
//import edu.buffalo.cse.cse486586.groupmessenger.GroupMessengerActivity;


public class TestCase1Listener implements OnClickListener {
	private String portStr;	    
	private String avdNum;
	private String[] msg;
	private String[] t1msg;

    
	public TestCase1Listener(String _t1PS){
		t1msg=new String[5];
		//msg=null;
        portStr=_t1PS;
        }
	
    private String[] initTestValues() {
//    	int temp;
    	if (portStr.equals("5554")){
    		avdNum="avd0:";
		}
		else if(portStr.equals("5556")){
			avdNum="avd1:";
		}
		else{
			avdNum="avd2:";
		}
 //   	temp=GroupMessengerActivity.id;
        for (int i = 0; i < 5; i++) {
            t1msg[i]=avdNum+GroupMessengerActivity.id+"."+avdNum+GroupMessengerActivity.id;
            GroupMessengerActivity.id=GroupMessengerActivity.id+1;
//        	GroupMessengerActivity.sequenceNumber=GroupMessengerActivity.sequenceNumber+1;
        }
        return t1msg;
    }
    
    
    
	@Override
	public void onClick(View v) {
        msg = initTestValues();
		new ClientTask().execute(msg);
	}

	public class ClientTask extends AsyncTask<String, Void, Void>{		
	    @Override
	    protected Void doInBackground(String...msgs){
	    	Socket socket1;
	    	Socket socket2;
	    	Socket socket3;
	    	try{ 		
	    		for (int i = 0; i < 5; i++) {
					socket1=new Socket("10.0.2.2",11108);
					socket2=new Socket("10.0.2.2",11112);
					socket3=new Socket("10.0.2.2",11116);	
		            PrintWriter out1 = new PrintWriter(socket1.getOutputStream());
		            PrintWriter out2 = new PrintWriter(socket2.getOutputStream());
		            PrintWriter out3 = new PrintWriter(socket3.getOutputStream());
	                out1.println(msgs[i]);                
	                out2.println(msgs[i]);                 
	                out3.println(msgs[i]);                
	                out1.flush();
	                out2.flush();
	                out3.flush();                                          
		            socket1.close();
		            socket2.close();
		            socket3.close();
		            Thread.sleep(3000);
	    		}//for

            }catch (IOException e) {
            	 e.printStackTrace();
  	        } catch (InterruptedException e) {
				e.printStackTrace();
			}	    	
			return null;
	    }
	}
}