package edu.buffalo.cse.cse486586.simpledynamo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;

public class SimpleDynamoProvider extends ContentProvider {
	public static int versionNum=0;
    public String key;
    public String key_id;
    public String value;
    public String version;
    public String hash_node_5554;
    public String hash_node_5556;
    public String hash_node_5558;
    public String portStr;
    public String sendport;
	public static final String KEY_COLUMN = "key";
    public static final String VALUE_COLUMN = "value";
    public static final String VERSION_COLUMN = "version";
    public Uri mUri;
    public ContentResolver mContentResolver;
    public ContentValues mContentValues;
    public DBOpenHelper dbOpenHelper;
    public String[] columnNames={KEY_COLUMN,VALUE_COLUMN,VERSION_COLUMN};
    public String mynode_id;
    public static boolean voteFlag1=false;
    public static boolean voteFlag2=false;
    public Cursor cursor1;
    public Cursor cursora;
//    public static Cursor GLcursor1;
//    public static Cursor GLcursor2;
    public static MatrixCursor matrixCursor1=null;
    public static MatrixCursor matrixCursor2=null;
    public static MatrixCursor GLcursor=null;
    public String GLselection;
    public String[] Arg={"ruijie","ge"};
    public String GLkey;
    public String GLvalue;
    public String GLversion;
    public static byte[] querywait;
    public static boolean flag=false;
    public static boolean flag1=false;
    public static boolean flag2=false;
    public static boolean cursorFlag=true;
    
	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
    public Uri insert(Uri uri, ContentValues values) {
//		SQLiteDatabase db= dbOpenHelper.getWritableDatabase();		
		key=(String)values.get(KEY_COLUMN);
		value=(String) values.get(VALUE_COLUMN);
		version=(String) values.get(VERSION_COLUMN);
		if(key.compareTo("0")==0){
			updateVersionNum(versionNum);
		}
		try {
			key_id=genHash(key);
			System.out.println(key+"="+key_id);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}	
	
			
		if(((key_id.compareTo(hash_node_5556)>0)&&(key_id.compareTo(hash_node_5554)<=0))){
				String msg="insert"+","+"5554"+","+key+","+value+","+version+","+"5558";
				new ClientTask().execute(msg);
				return null;		
		}
		else if(((key_id.compareTo(hash_node_5554)>0)&&(key_id.compareTo(hash_node_5558)<=0))){
				String msg="insert"+","+"5558"+","+key+","+value+","+version+","+"5556";
				new ClientTask().execute(msg);
				return null;
		}
		else{
				String msg="insert"+","+"5556"+","+key+","+value+","+version+","+"5554";
				new ClientTask().execute(msg);
				return null;
		}
    }

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
	
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		
		if(selection==null){
			cursora=db.query("dynamo", projection, null, null, null, null, sortOrder);
    		return cursora;
		}	
		else if(selectionArgs!=null){
			cursorFlag=false;
    		String sel="dynamo.key = '"+selection+"'";
    		cursora=db.query("dynamo", projection, sel, null, null, null, sortOrder);
    		System.out.println(sel+"....cursor1.getcount"+cursora.getCount());
    		return cursora;
    	}		
		else{
			try {
				key_id=genHash(selection);
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
			if(((key_id.compareTo(hash_node_5556)>0)&&(key_id.compareTo(hash_node_5554)<=0))){
				String msg="query"+","+"5554"+","+selection+","+"5558"+","+portStr;
				new ClientTask().execute(msg);
			}
			else if(((key_id.compareTo(hash_node_5554)>0)&&(key_id.compareTo(hash_node_5558)<=0))){
				String msg="query"+","+"5558"+","+selection+","+"5556"+","+portStr;
				new ClientTask().execute(msg);
			}
			else{
				String msg="query"+","+"5556"+","+selection+","+"5554"+","+portStr;
				new ClientTask().execute(msg);			
			}
//			synchronized (querywait) {
//				try {
//					querywait.wait(100);
//				} catch (InterruptedException e) {
//					e.printStackTrace();
//				}
//			}
//			try {
//				Thread.sleep(100);
//			} catch (InterruptedException e) {
//				e.printStackTrace();
//			}			
//			String columnValues[]={GLkey,GLvalue,GLversion};
//	    	GLcursor.addRow(columnValues);
			cursorFlag=true;
			while(cursorFlag){}
			flag1=false;
			flag2=false;
			return GLcursor;		
		}
	}
	

	@Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		return 0;
    }
	
	@Override
    public boolean onCreate() {
		querywait=new byte[0];
		matrixCursor1=new MatrixCursor(columnNames);
		matrixCursor2=new MatrixCursor(columnNames);
		GLcursor=new MatrixCursor(columnNames);
		
        TelephonyManager tel =
                (TelephonyManager) this.getContext().getSystemService(Context.TELEPHONY_SERVICE);
        portStr=tel.getLine1Number().substring(tel.getLine1Number().length()-4);
        try {
			mynode_id=genHash(portStr);
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		}
    	try {
			hash_node_5554=genHash("5554");
			hash_node_5556=genHash("5556");
	    	hash_node_5558=genHash("5558");
	    	System.out.println("5556="+hash_node_5556);
	    	System.out.println("5554="+hash_node_5554);
	    	System.out.println("5558="+hash_node_5558);
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		}      
		dbOpenHelper =new DBOpenHelper(this.getContext());
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		dbOpenHelper.onCreate(db);
        mUri = buildUri("content", "edu.buffalo.cse.cse486586.simpledynamo.provider");
        mContentResolver=this.getContext().getContentResolver();
		
    	try {
            ServerSocket serverSocket = new ServerSocket(10000);
            new ServerTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, serverSocket);
    		}catch(IOException e){
    			e.printStackTrace();		
    		}
    	recover();
		return true;
    }

	public void recover(){
    	if(mynode_id.compareTo(hash_node_5554)==0){
    		String reco="recover"+","+"5556"+","+"5554";
    		new ClientTask().execute(reco);
    	}
    	else if(mynode_id.compareTo(hash_node_5556)==0){
    		String reco="recover"+","+"5558"+","+"5556";
    		new ClientTask().execute(reco);
    	}
    	else if(mynode_id.compareTo(hash_node_5558)==0){
    		String reco="recover"+","+"5554"+","+"5558";
    		new ClientTask().execute(reco);
    	}
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
    
	private class ServerTask extends AsyncTask<ServerSocket, String, Void>{
	    @Override
	    protected Void doInBackground(ServerSocket...sockets){
	        ServerSocket serverSocket = sockets[0];
	        Socket socket;
	        try{
	            while(true){
	                socket = serverSocket.accept(); //wait for the client for request;
	                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	                String mmssgg = in.readLine();
	                String[] fields = mmssgg.split(",");
	                SQLiteDatabase db= dbOpenHelper.getWritableDatabase();
	                
	                if(fields[0].equals("insert")){
	                	
	                	String okInsert="okInsert";	                	
	                	PrintWriter out = new PrintWriter(socket.getOutputStream());
	    				out.println(okInsert);
	    				out.flush();
	    				
	                	ContentValues cv = new ContentValues();
	  	                cv.put(KEY_COLUMN,fields[2]);
	  	                cv.put(VALUE_COLUMN,fields[3]);
	  	                cv.put(VERSION_COLUMN,fields[4]);
	  	                
	  	                
	  	              if(vote()){	
	  	            	    try {
								Thread.sleep(100);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
		  	                db.insertWithOnConflict("dynamo", "key", cv, SQLiteDatabase.CONFLICT_REPLACE);	  
		  	                System.out.println("insert one done!!~");
		                	//replicate
		                	if(portStr.equals("5554")){
		                		String repmsg1="replicate"+","+"5556"+","+fields[2]+","+fields[3]+","+fields[4];
			    				new ClientTask().execute(repmsg1);
			    				String repmsg2="replicate"+","+"5558"+","+fields[2]+","+fields[3]+","+fields[4];
			    				new ClientTask().execute(repmsg2);
		                	}
		                	else if(portStr.equals("5556")){
		                		String repmsg1="replicate"+","+"5554"+","+fields[2]+","+fields[3]+","+fields[4];
			    				new ClientTask().execute(repmsg1);
			    				String repmsg2="replicate"+","+"5558"+","+fields[2]+","+fields[3]+","+fields[4];
			    				new ClientTask().execute(repmsg2);
		                	}
		                	else if(portStr.equals("5558")){
		                		String repmsg1="replicate"+","+"5554"+","+fields[2]+","+fields[3]+","+fields[4];
			    				new ClientTask().execute(repmsg1);
		                		String repmsg2="replicate"+","+"5556"+","+fields[2]+","+fields[3]+","+fields[4];
			    				new ClientTask().execute(repmsg2);
		                	}
	  	                }
	  	                else{
	  	                	//return error;
	  	                }
	                }
	                else if(fields[0].equals("replicate")){
	                	ContentValues cv = new ContentValues();           		
	            		cv.put(KEY_COLUMN,fields[2]);
	  	                cv.put(VALUE_COLUMN,fields[3]);
	  	                cv.put(VERSION_COLUMN,fields[4]);
	                	db.insertWithOnConflict("dynamo", "key", cv, SQLiteDatabase.CONFLICT_REPLACE);	
	                }
	                else if(fields[0].equals("updateVN")){
	                	SimpleDynamoProvider.versionNum=Integer.parseInt(fields[2]);
	                }
	                else if(fields[0].equals("vote1")){
	                	String repmsg2="OK1"+","+fields[2];//fields[2] means the coordinator
	                	PrintWriter out = new PrintWriter(socket.getOutputStream());
	    				out.println(repmsg2);
	    				out.flush();
	                }
	                else if(fields[0].equals("vote2")){
	                	String repmsg2="OK2"+","+fields[2];//fields[2] means the coordinator
	                	PrintWriter out = new PrintWriter(socket.getOutputStream());
	    				out.println(repmsg2);
	    				out.flush();
	                }
	                else if(fields[0].equals("query")){
	                	
	                	String okInsert="okQuery";	                	
	                	PrintWriter out = new PrintWriter(socket.getOutputStream());
	    				out.println(okInsert);
	    				out.flush();
	    				
	    				String sel="dynamo.key = '"+fields[2]+"'";	    		    		    		    	
    		    		cursor1=db.query("dynamo", null, sel, null, null, null, null);
    		    		System.out.println("cursor1.getCount"+cursor1.getCount());   
    		    		cursor1.moveToFirst();
    		        	int VNIndex1 = cursor1.getColumnIndex(VERSION_COLUMN); //2
    		            String returnVN1 = cursor1.getString(VNIndex1);  
    		            
    		            int keyIndex1 = cursor1.getColumnIndex(KEY_COLUMN);               
    	                int valueIndex1 = cursor1.getColumnIndex(VALUE_COLUMN);  	                
    	                String returnKey1 = cursor1.getString(keyIndex1);
    	                String returnValue1 = cursor1.getString(valueIndex1);
    	               
//                		    	                
//    		    		
//    		    		if(vote()){
//    		    			System.out.println("enter query vote success");
//    		    			GLselection=fields[2];
//		                	if(portStr.equals("5554")){
//		                		String repmsg1="query1"+","+"5556"+","+"5554"+","+GLselection+","+returnVN1+","+fields[4];
//		            			new ClientTask().execute(repmsg1);
//		            			String repmsg2="query2"+","+"5558"+","+"5554"+","+GLselection+","+returnVN1+","+fields[4];
//		            			new ClientTask().execute(repmsg2);
//		                	}
//		                	else if(portStr.equals("5556")){
//		                		String repmsg1="query1"+","+"5554"+","+"5556"+","+GLselection+","+returnVN1+","+fields[4];
//		            			new ClientTask().execute(repmsg1);
//		            			String repmsg2="query2"+","+"5558"+","+"5556"+","+GLselection+","+returnVN1+","+fields[4];
//		            			new ClientTask().execute(repmsg2);
//		                	}
//		                	else if(portStr.equals("5558")){
//		                		String repmsg1="query1"+","+"5556"+","+"5558"+","+GLselection+","+returnVN1+","+fields[4];
//		            			new ClientTask().execute(repmsg1);
//		            			String repmsg2="query2"+","+"5554"+","+"5558"+","+GLselection+","+returnVN1+","+fields[4];
//		            			new ClientTask().execute(repmsg2);
//		                	}
//	  	                }
//	  	                else{
//	  	                	//return error;
//	  	                }
    		    		if(flag1||flag2==false){
    		    			String qback2="back"+","+fields[4]+","+returnKey1+","+returnValue1+","+returnVN1;
                    		new ClientTask().execute(qback2);
    	                }

	                }
	                else if(fields[0].equals("query1")){
//	                	String sel="dynamo.key = '"+fields[3]+"'";	
//	                	System.out.println(sel);
//	                	Cursor GLcursor1=db.query("dynamo", null, sel, null, null, null, null);
	                	Cursor GLcursor1 = mContentResolver.query(mUri, null, fields[3], Arg, null);
	                	System.out.println("getCount"+GLcursor1.getCount());               		
	                	GLcursor1.moveToFirst();
    		        	int VNIndex1 = GLcursor1.getColumnIndex(VERSION_COLUMN); //2
    		            String returnGVN1 = GLcursor1.getString(VNIndex1);  
    		            if(returnGVN1.compareTo(fields[4])>0){
    		            	int keyIndex1 = GLcursor1.getColumnIndex(KEY_COLUMN);               
        	                int valueIndex1 = GLcursor1.getColumnIndex(VALUE_COLUMN);
        	                int versionIndex1 = GLcursor1.getColumnIndex(VERSION_COLUMN);
        	                System.out.println(""+keyIndex1+valueIndex1+versionIndex1);
        	                GLcursor1.moveToFirst();
        	                String returnVN1 = GLcursor1.getString(versionIndex1);
        	                String returnKey1 = GLcursor1.getString(keyIndex1);
        	                String returnValue1 = GLcursor1.getString(valueIndex1);
        	            
                    		String qback1="qflag1"+","+fields[2]+","+returnKey1+","+returnValue1+","+returnVN1+","+fields[5];
                    		new ClientTask().execute(qback1);
    		            }       				    
	                }
                	else if(fields[0].equals("query2")){
//                		String sel="dynamo.key = '"+fields[3]+"'";	
//                		System.out.println(sel);
//	                	GLcursor2=db.query("dynamo", null, sel, null, null, null, null);
                		Cursor GLcursor2 = mContentResolver.query(mUri, null, fields[3], Arg, null);               		
	                	System.out.println("getCount"+GLcursor2.getCount());
	                	GLcursor2.moveToFirst();
    		        	int VNIndex1 = GLcursor2.getColumnIndex(VERSION_COLUMN); //2
    		            String returnGVN1 = GLcursor2.getString(VNIndex1);  
    		            if(returnGVN1.compareTo(fields[4])>0){
    		            	int keyIndex1 = GLcursor2.getColumnIndex(KEY_COLUMN);               
        	                int valueIndex1 = GLcursor2.getColumnIndex(VALUE_COLUMN);
        	                int versionIndex1 = GLcursor2.getColumnIndex(VERSION_COLUMN);
        	                System.out.println(""+keyIndex1+valueIndex1+versionIndex1);
        	                GLcursor2.moveToFirst();
        	                String returnVN1 = GLcursor2.getString(versionIndex1);
        	                String returnKey1 = GLcursor2.getString(keyIndex1);
        	                String returnValue1 = GLcursor2.getString(valueIndex1);
        	                
        	                String qback2="qflag2"+","+fields[2]+","+returnKey1+","+returnValue1+","+returnVN1+","+fields[5];
                    		new ClientTask().execute(qback2);
    		            }
       				 
	                }
                	else if(fields[0].equals("qflag1")){                		
                		String qback2="back1"+","+fields[5]+","+fields[2]+","+fields[3]+","+fields[4];
                		new ClientTask().execute(qback2);                		
	                }
                	else if(fields[0].equals("qflag2")){               		
                		String qback2="back2"+","+fields[5]+","+fields[2]+","+fields[3]+","+fields[4];
                		new ClientTask().execute(qback2);
	                }
                	else if(fields[0].equals("back1")){
                		flag1=true;
                		String columnValues[]={fields[2],fields[3],fields[4]};
                		GLcursor.addRow(columnValues);
                		cursorFlag=false;
	                }
                	else if(fields[0].equals("back2")){
                		flag2=true;
                		System.out.println(fields[2]+"..!!"+fields[3]+"..!!"+fields[4]);
                		String columnValues[]={fields[2],fields[3],fields[4]};
                		GLcursor.addRow(columnValues);
                		cursorFlag=false;
	                }
                	else if(fields[0].equals("back")){
                		System.out.println(fields[2]+"..!!"+fields[3]+"..!!"+fields[4]);
                		String columnValues[]={fields[2],fields[3],fields[4]};
                		GLcursor.addRow(columnValues);
                		cursorFlag=false;
	                }
                	else if(fields[0].equals("recover")){
                		for(int i=0;i<20;i++){
                			Cursor Rcursor1 = mContentResolver.query(mUri, null, Integer.toString(i), Arg, null);
                			Rcursor1.moveToFirst();
            				int keyIndex1 = Rcursor1.getColumnIndex(KEY_COLUMN);               
                            int valueIndex1 = Rcursor1.getColumnIndex(VALUE_COLUMN);
                            int versionIndex1 = Rcursor1.getColumnIndex(VERSION_COLUMN);                
                            String returnKey1 = Rcursor1.getString(keyIndex1);
                            String returnValue1 = Rcursor1.getString(valueIndex1);
                            String returnVN1 = Rcursor1.getString(versionIndex1);
                            
                            String tupleback="tupleback"+","+fields[2]+","+returnKey1+","+returnValue1+","+returnVN1;
                            new ClientTask().execute(tupleback);
            				try {
            					Thread.sleep(200);
            				} catch (InterruptedException e) {
            					e.printStackTrace();
            				}
                		}
	                }
                	else if(fields[0].equals("tupleback")){
                		ContentValues mContentValues = new ContentValues();
            			mContentValues.put(KEY_COLUMN, fields[2]);
            			mContentValues.put(VALUE_COLUMN, fields[3]);
            			mContentValues.put(VERSION_COLUMN, fields[4]);   
            			versionNum=Integer.parseInt(fields[4]);
        				try {
        					Thread.sleep(100);
        				} catch (InterruptedException e) {
        					e.printStackTrace();
        				}
            			mContentResolver.insert(mUri, mContentValues);                	
	                }
	                	    			
	                socket.close();  
	            }
	        } catch (IOException e) {
	            
	        }
	        return null;
	    } 
	}
  
	public class ClientTask extends AsyncTask<String, Void, Void>{
	    @Override
	    protected Void doInBackground(String...msgs){
	    	Socket socket;
	    	try{ 
	    		String[] fields = msgs[0].split(",");
	        	sendport=fields[1];
				socket=new Socket("10.0.2.2",Integer.parseInt(sendport)*2); 	
				//socket.setSoTimeout(100);				
				PrintWriter out = new PrintWriter(socket.getOutputStream());
				out.println(msgs[0]);
				out.flush();
				
				if(fields[0].equals("insert")){
					BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));				
	                String mmssgg = in.readLine();

	                if(mmssgg==null){
	                	String msgg="insert"+","+"5558"+","+key+","+value+","+version+","+"5558";
	    				socket=new Socket("10.0.2.2",Integer.parseInt(fields[5])*2); 				    				
	    				PrintWriter out2 = new PrintWriter(socket.getOutputStream());	    				
	    				out2.println(msgg);
	    				out2.flush();
	                }
				}
				if(fields[0].equals("vote1")){
					BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));				
	                String mmssgg = in.readLine();   
	                System.out.println("mmssgg.."+mmssgg);
	                if(mmssgg!=null){
	                	voteFlag1=true;
	                }
				} 
				if(fields[0].equals("vote2")){
					BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));				
	                String mmssgg = in.readLine();  
	                System.out.println("mmssgg.."+mmssgg);
	                if(mmssgg!=null){
	                	voteFlag2=true;
	                }
				} 
				if(fields[0].equals("query")){
					BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));				
	                String mmssgg = in.readLine();                
	                if(mmssgg==null){
	                	String msgg="query"+","+"5558"+","+fields[2]+","+"5558"+","+portStr;
	    				socket=new Socket("10.0.2.2",Integer.parseInt(fields[3])*2); 				    				
	    				PrintWriter out2 = new PrintWriter(socket.getOutputStream());
	    				out2.println(msgg);
	    				out2.flush();
	                }
				}
				socket.close();
             	}catch (IOException e) {	            
 	        }
			return null;
	    }
	}
	
    @SuppressWarnings("resource")
	private String genHash(String input) throws NoSuchAlgorithmException {
        MessageDigest sha1 = MessageDigest.getInstance("SHA-1");
        byte[] sha1Hash = sha1.digest(input.getBytes());
        Formatter formatter = new Formatter();
        for (byte b : sha1Hash) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
    }
    public Uri buildUri(String scheme, String authority) {
        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.authority(authority);
        uriBuilder.scheme(scheme);
        return uriBuilder.build();
    }
    public void updateVersionNum(int VN){
    	String updateVN5554="updateVN"+","+"5554"+","+Integer.toString(VN);
    	new ClientTask().execute(updateVN5554);
		String updateVN5556="updateVN"+","+"5556"+","+Integer.toString(VN);
		new ClientTask().execute(updateVN5556);
		String updateVN5558="updateVN"+","+"5558"+","+Integer.toString(VN);
		new ClientTask().execute(updateVN5558);
    }
    public boolean vote(){
    	if(mynode_id.compareTo(hash_node_5554)==0){
    		String repmsg1="vote1"+","+"5556"+","+"5554";
			new ClientTask().execute(repmsg1);
			String repmsg2="vote2"+","+"5558"+","+"5554";
			new ClientTask().execute(repmsg2);
    	}
    	else if(mynode_id.compareTo(hash_node_5556)==0){
    		String repmsg1="vote1"+","+"5554"+","+"5556";
			new ClientTask().execute(repmsg1);
			String repmsg2="vote2"+","+"5558"+","+"5556";
			new ClientTask().execute(repmsg2);
    	}
    	else if(mynode_id.compareTo(hash_node_5558)==0){
    		String repmsg1="vote1"+","+"5556"+","+"5558";
			new ClientTask().execute(repmsg1);
			String repmsg2="vote2"+","+"5554"+","+"5558";
			new ClientTask().execute(repmsg2);
    	}
          try {
			Thread.sleep(100);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
    	return voteFlag1||voteFlag2;
    }
}
