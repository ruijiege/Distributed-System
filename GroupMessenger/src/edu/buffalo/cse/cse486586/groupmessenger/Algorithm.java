package edu.buffalo.cse.cse486586.groupmessenger;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.os.AsyncTask;
import android.widget.TextView;

public class Algorithm extends AsyncTask<ServerSocket, String, Void> {
    private static final String KEY_COLUMN = "key";
    private static final String VALUE_COLUMN = "value";
	private String portStr;	    
    private Uri uri;
    private ContentResolver mContentResolver;

    
	private LinkedList <String> saveContent= new LinkedList<String>();
	private LinkedList <String> saveId= new LinkedList<String>();
	private LinkedList <String> saveSequence= new LinkedList<String>();
	private LinkedList <String> saveSId= new LinkedList<String>();
	
	private TextView tv;
	
	public Algorithm(String _algPort, ContentResolver _cr,TextView _ttv) {
        portStr=_algPort;
        mContentResolver=_cr;
        tv=_ttv;
	}
	public void GroupMessengerAlg(String msg){
        uri = buildUri("content", "edu.buffalo.cse.cse486586.groupmessenger.provider");
        String testflg=(String)msg.substring(0,6);
        if(testflg.equals("tests2")){
        	TestCase2Listener.test2flag=true;
        	return;
        }
        if (portStr.equals("5554")){
            String msgTemp[]=(String[])msg.split("\\.");

			String msgId=msgTemp[0];
			String msgContent=msgTemp[1];
			      		            
			ContentValues deliveryMessage = new ContentValues();
			deliveryMessage.put(KEY_COLUMN, GroupMessengerActivity.sequenceNumber+"");
			deliveryMessage.put(VALUE_COLUMN, msgContent);
			mContentResolver.insert(uri, deliveryMessage);		                	
            publishProgress(msgContent); //publish to the activity	
    		GroupMessengerActivity.sequenceNumber=GroupMessengerActivity.sequenceNumber+1;
            new myClientTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, "@"+GroupMessengerActivity.sequenceNumber+"."+msgId);
          
            if(TestCase2Listener.test2flag){
            	TestCase2Listener.test2flag=false;
            	System.out.println(msgId+".......");
            	if(msgId.equals("avd0:"+(GroupMessengerActivity.id-1))){
                    new ClientTask2().execute("avd0:"+(GroupMessengerActivity.id));	
                    GroupMessengerActivity.id=GroupMessengerActivity.id+1;
                    new ClientTask2().execute("avd0:"+(GroupMessengerActivity.id));	
                    GroupMessengerActivity.id=GroupMessengerActivity.id+1;
            	}
            	else{
            		new ClientTask2().execute("avd0:"+(GroupMessengerActivity.id));
            		GroupMessengerActivity.id=GroupMessengerActivity.id+1;
                    new ClientTask2().execute("avd0:"+(GroupMessengerActivity.id));	
                    GroupMessengerActivity.id=GroupMessengerActivity.id+1;
            	}
            }
        }		   
     	else{		 
            String msgTemp[]=(String[])msg.split("\\.");

			String msgId=msgTemp[0];
			String msgContent=msgTemp[1];
            
     		String flg=(String)msg.substring(0,1);
            int len2=msgTemp[0].length();
            
     		String msgSenquence=(String)msgTemp[0].substring(1,len2);
            String msgSId=msgTemp[1];

            char[] dvec=(char[])msgTemp[0].substring(1,len2).toCharArray();
            int[] dVector=new int[len2-1];
            for(int i=0;i<len2-1;i++){
            	dVector[i]=(int)dvec[i]-48;
            }	             
	        
            
            if(flg.equals("@")){           	
             	if(saveId.contains(msgSId)){
             		ContentValues deliveryMessage = new ContentValues();
                	deliveryMessage.put(KEY_COLUMN, msgSenquence);
                	deliveryMessage.put(VALUE_COLUMN,saveContent.get(saveId.indexOf(msgSId)));
                	mContentResolver.insert(uri, deliveryMessage);
                	publishProgress(saveContent.get(saveId.indexOf(msgSId)));
                	saveContent.remove(saveContent.get(saveId.indexOf(msgSId)));
                	saveId.remove(msgSId);
                	
                	if(portStr.equals("5556")){
                		if(TestCase2Listener.test2flag){
                        	TestCase2Listener.test2flag=false;
                        	if(msgId.equals("avd1:"+(GroupMessengerActivity.id-1))){
                                new ClientTask2().execute("avd1:"+(GroupMessengerActivity.id));	
                                GroupMessengerActivity.id=GroupMessengerActivity.id+1;
                                new ClientTask2().execute("avd1:"+(GroupMessengerActivity.id));
                                GroupMessengerActivity.id=GroupMessengerActivity.id+1;
                        	}
                        	else{
                        		new ClientTask2().execute("avd1:"+(GroupMessengerActivity.id));	
                        		GroupMessengerActivity.id=GroupMessengerActivity.id+1;
                                new ClientTask2().execute("avd1:"+(GroupMessengerActivity.id));	
                                GroupMessengerActivity.id=GroupMessengerActivity.id+1;
                        	}
                        }
                	}
                	else{
                		if(TestCase2Listener.test2flag){
                        	TestCase2Listener.test2flag=false;
                        	if(msgId.equals("avd2:"+(GroupMessengerActivity.id-1))){
                                new ClientTask2().execute("avd2:"+(GroupMessengerActivity.id));	
                                GroupMessengerActivity.id=GroupMessengerActivity.id+1;
                                new ClientTask2().execute("avd2:"+(GroupMessengerActivity.id));
                                GroupMessengerActivity.id=GroupMessengerActivity.id+1;
                        	}
                        	else{
                        		new ClientTask2().execute("avd2:"+(GroupMessengerActivity.id));	
                        		GroupMessengerActivity.id=GroupMessengerActivity.id+1;
                                new ClientTask2().execute("avd2:"+(GroupMessengerActivity.id));	
                                GroupMessengerActivity.id=GroupMessengerActivity.id+1;
                        	}
                        }
                	}
             	}
             	else{
             	saveSequence.addFirst(msgSenquence);
             	saveSId.addFirst(msgSId);
             	}
            }
            else{
            	if(saveSId.contains(msgId)){
             		ContentValues deliveryMessage = new ContentValues();
                	deliveryMessage.put(KEY_COLUMN, saveSequence.get(saveSId.indexOf(msgId)));
                	deliveryMessage.put(VALUE_COLUMN,msgContent);
                	mContentResolver.insert(uri, deliveryMessage);
                	publishProgress(msgContent);
                	saveSequence.remove(saveSequence.get(saveSId.indexOf(msgId)));
                	saveSId.remove(msgSId);
                	if(portStr.equals("5556")){
                		if(TestCase2Listener.test2flag){
                        	TestCase2Listener.test2flag=false;
                        	if(msgId.equals("avd1:"+(GroupMessengerActivity.id-1))){
                                new ClientTask2().execute("avd1:"+(GroupMessengerActivity.id));	
                                GroupMessengerActivity.id=GroupMessengerActivity.id+1;
                                new ClientTask2().execute("avd1:"+(GroupMessengerActivity.id));
                                GroupMessengerActivity.id=GroupMessengerActivity.id+1;
                        	}
                        	else{
                        		new ClientTask2().execute("avd1:"+(GroupMessengerActivity.id));
                        		GroupMessengerActivity.id=GroupMessengerActivity.id+1;
                                new ClientTask2().execute("avd1:"+(GroupMessengerActivity.id));	
                                GroupMessengerActivity.id=GroupMessengerActivity.id+1;
                        	}
                        }
                	}
                	else{
                		if(TestCase2Listener.test2flag){
                        	TestCase2Listener.test2flag=false;
                        	if(msgId.equals("avd2:"+(GroupMessengerActivity.id-1))){
                                new ClientTask2().execute("avd2:"+(GroupMessengerActivity.id));	
                                GroupMessengerActivity.id=GroupMessengerActivity.id+1;
                                new ClientTask2().execute("avd2:"+(GroupMessengerActivity.id));
                                GroupMessengerActivity.id=GroupMessengerActivity.id+1;
                        	}
                        	else{
                        		new ClientTask2().execute("avd2:"+(GroupMessengerActivity.id));
                        		GroupMessengerActivity.id=GroupMessengerActivity.id+1;
                                new ClientTask2().execute("avd2:"+(GroupMessengerActivity.id));	
                                GroupMessengerActivity.id=GroupMessengerActivity.id+1;
                        	}
                        }
                	}
                    
             	}
            	saveContent.addFirst(msgContent);
             	saveId.addFirst(msgId);
            }
     	}
        
	}
	            
	 protected void onProgressUpdate(String...strings){
	    	tv.append("\n"+strings[0]);
	        return;
	    }
	
	public class myClientTask extends AsyncTask<String, Void, Void>{
	    @Override
	    protected Void doInBackground(String...msgs){
	    	Socket socket1;
	    	Socket socket2;
	    	try{ 
				if (portStr.equals("5554")){				
					socket1=new Socket("10.0.2.2",11112);
					socket2=new Socket("10.0.2.2",11116);
				}
				else if(portStr.equals("5556")){
					socket1=new Socket("10.0.2.2",11108);
					socket2=new Socket("10.0.2.2",11116);
				}
				else{
					socket1=new Socket("10.0.2.2",11108);
					socket2=new Socket("10.0.2.2",11112);
				}
             PrintWriter out1 = new PrintWriter(socket1.getOutputStream());
             PrintWriter out2 = new PrintWriter(socket2.getOutputStream());
             out1.println(msgs[0]);
             out2.println(msgs[0]);
             out1.flush();
             out2.flush();
             socket1.close();
             socket2.close();
             }catch (IOException e) {	            
 	        }
			return null;
	    }
	}
	
	public class ClientTask2 extends AsyncTask<String, Void, Void>{		
	    @Override
	    protected Void doInBackground(String...msgs){
	    	Socket socket1;
	    	Socket socket2;
	    	Socket socket3;

	    	try{ 		
	            msgs[0]=msgs[0]+"."+msgs[0];
				socket1=new Socket("10.0.2.2",11108);
				socket2=new Socket("10.0.2.2",11112);
				socket3=new Socket("10.0.2.2",11116);	

	            PrintWriter out1 = new PrintWriter(socket1.getOutputStream());
	            PrintWriter out2 = new PrintWriter(socket2.getOutputStream());
	            PrintWriter out3 = new PrintWriter(socket3.getOutputStream());

                out1.println(msgs[0]);                
                out1.flush();
	            socket1.close();
	            
                out2.println(msgs[0]);                 
                out2.flush();
	            socket2.close();
	            
                out3.println(msgs[0]);                
                out3.flush();                                          
	            socket3.close();
            }catch (IOException e) {
            	 e.printStackTrace();
  	        }
			return null;
	    }
	}
	
    private Uri buildUri(String scheme, String authority) {
        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.authority(authority);
        uriBuilder.scheme(scheme);
        return uriBuilder.build();
    }
	@Override
	protected Void doInBackground(ServerSocket... params) {
		// TODO Auto-generated method stub
		return null;
	}
}
