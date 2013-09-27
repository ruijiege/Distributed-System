package edu.buffalo.cse.cse486586.simpledht;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Formatter;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;

public class SimpleDhtProvider extends ContentProvider {
	
	private static final String KEY_COLUMN = "key";
    private static final String VALUE_COLUMN = "value";
    public String key;
    public String key_id;
    public String value;
    public DBOpenHelper dbOpenHelper;
	public String portStr;	    
	public static String mynode_id;
	public String predecessor_id;
	public String successor_id;
    public ContentResolver mContentResolver;
    public Uri mUri;
    public static String querynode_id=mynode_id;
    public MatrixCursor matrixCursor;
    public MatrixCursor GmatrixCursor;
    public String[] columnNames={KEY_COLUMN,VALUE_COLUMN};
    public static boolean flag=true, Gflag1=true, Gflag2=true;
    public ArrayList<String> hashlist;
    public ArrayList<String> portlist;
    public ArrayList<String> templist;
    public String sendport;
    public String mmssgg = null;
    public String[] fields;
    

	@Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

	
    @Override
    public String getType(Uri uri) {
        return null;
    }
    
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    @Override
    public Uri insert(Uri uri, ContentValues values) {
		SQLiteDatabase db= dbOpenHelper.getWritableDatabase();
//		predecessor_id=SimpleDhtMainActivity.predecessor_id;
//		mynode_id=SimpleDhtMainActivity.mynode_id;
//		successor_id=SimpleDhtMainActivity.successor_id;
        querynode_id=mynode_id;
				
		key=(String)values.get(KEY_COLUMN);
		value=(String) values.get(VALUE_COLUMN);
		try {
			key_id=genHash(key);
//			System.out.println("hashkey:  "+key_id);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}	
		
		if((predecessor_id.compareTo(mynode_id)>=0)){
			if(((key_id.compareTo(predecessor_id)>0)|(key_id.compareTo(mynode_id)<=0))){
				long rowid=db.insert("dht", "key", values);
				Uri insertUri=ContentUris.withAppendedId(uri, rowid);
				this.getContext().getContentResolver().notifyChange(uri, null);
				return insertUri;
			}
			else{
				String msg="insert"+","+key+","+value;
				new ClientTask().execute(msg);
				return null;
			}
		}
		else{
			if(((key_id.compareTo(predecessor_id)>0)&&(key_id.compareTo(mynode_id)<=0))){
				long rowid=db.insert("dht", "key", values);
				Uri insertUri=ContentUris.withAppendedId(uri, rowid);
				this.getContext().getContentResolver().notifyChange(uri, null);
				return insertUri;
	    	}
			else{
				String msg="insert"+","+key+","+value;
				new ClientTask().execute(msg);
				return null;
			}	
		}
    }
    
    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	@Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {

    	SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
    	String sel=null;
    	matrixCursor=new MatrixCursor(columnNames);;
    	if(selection!=null){
    		sel="dht.key = '"+selection+"'";
    	}
    	Cursor cursor=db.query("dht", projection, sel, null, null, null, sortOrder);
    	
    	if(selectionArgs!=null){
    		GmatrixCursor=new MatrixCursor(columnNames);
    		String GDump="GDump"+","+querynode_id;
			new ClientTask().execute(GDump);
			
			while (cursor.moveToNext()){
	            int keyIndex = cursor.getColumnIndex(KEY_COLUMN);               
	            int valueIndex = cursor.getColumnIndex(VALUE_COLUMN);
	            String returnKey = cursor.getString(keyIndex);
	            String returnValue = cursor.getString(valueIndex);
	            String columnValues[]={returnKey,returnValue};
	    		GmatrixCursor.addRow(columnValues);
			}
			cursor.close();
			if(querynode_id.equals(mynode_id)){
				querynode_id=mynode_id;
				Gflag1=true;
				while(Gflag1){}				
				return GmatrixCursor;
			}
			return null;
    	}
  	
    	if(cursor.getCount()==0||cursor==null){
    		String msg="query"+","+selection+","+querynode_id;
			new ClientTask().execute(msg);
			if(querynode_id.equals(mynode_id)){
				querynode_id=mynode_id;
				flag=true;
				while(flag){}
				return matrixCursor;
			}
			return null;
    	}
    	else {
    		if(querynode_id.equals(mynode_id)){
    			querynode_id=mynode_id;
    			if(hashlist.size()==1){
    				Gflag1=false;
    				return cursor;
    			}
    			else{
            		return cursor;
    			}
        	}
    		else{
    			if(cursor.getCount()>1){
    				String ldht=querynode_id+","+"GDump";
        			for(int j=2;j<fields.length;j++){
        				ldht=ldht+","+fields[j];
        			}
        	        System.out.println("cursor.getCount()="+cursor.getCount());
   				    cursor.moveToFirst();
   				    int keyIndex1 = cursor.getColumnIndex(KEY_COLUMN);               
	                int valueIndex1 = cursor.getColumnIndex(VALUE_COLUMN);
	                String returnKey1 = cursor.getString(keyIndex1);
	                String returnValue1 = cursor.getString(valueIndex1);
	                ldht=ldht+","+returnKey1+","+returnValue1;	
	            
	                
        			while (cursor.moveToNext()){
        		        System.out.println("!!!");

    		       		 int keyIndex = cursor.getColumnIndex(KEY_COLUMN);               
    		             int valueIndex = cursor.getColumnIndex(VALUE_COLUMN);
    		             String returnKey = cursor.getString(keyIndex);
    		             String returnValue = cursor.getString(valueIndex);
    		             ldht=ldht+","+returnKey+","+returnValue;					 
        			}
        			new ClientTask().execute(ldht);
    				cursor.close();
        			return null;
    			}
    			else{
    				 cursor.moveToFirst();
    	       		 int keyIndex = cursor.getColumnIndex(KEY_COLUMN);               
    	             int valueIndex = cursor.getColumnIndex(VALUE_COLUMN);
    	             String returnKey = cursor.getString(keyIndex);
    	             String returnValue = cursor.getString(valueIndex);
    	             String ldht=querynode_id+","+"haha"+","+returnKey+","+returnValue;
    				 new ClientTask().execute(ldht);
    				 cursor.close();
    				 return null;
    			}
    		}
    	}   	
    }
	
    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		int num = 0;
		num = db.update("dht", values, selection, selectionArgs);						
		getContext().getContentResolver().notifyChange(uri, null);
		return num;
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
//************************************************************************************
    
    @Override
    public boolean onCreate() {
    	hashlist=new ArrayList<String>();
    	portlist=new ArrayList<String>();
    	templist=new ArrayList<String>();
		matrixCursor=new MatrixCursor(columnNames);
		GmatrixCursor=new MatrixCursor(columnNames);


        TelephonyManager tel =
                (TelephonyManager) this.getContext().getSystemService(Context.TELEPHONY_SERVICE);
        portStr=tel.getLine1Number().substring(tel.getLine1Number().length()-4);
        try {
			mynode_id=genHash(portStr);
		} catch (NoSuchAlgorithmException e1) {
			e1.printStackTrace();
		}
        querynode_id=mynode_id;
        String[] joinmsg={mynode_id,portStr};
        templist.add("5554");templist.add("5556");
    	templist.add("5558");templist.add("5560");

        
		dbOpenHelper =new DBOpenHelper(this.getContext());
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		dbOpenHelper.onCreate(db);
        mUri = buildUri("content", "edu.buffalo.cse.cse486586.simpledht.provider");
        mContentResolver=this.getContext().getContentResolver();
		
    	try {
            ServerSocket serverSocket = new ServerSocket(10000);
            new ServerTask().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, serverSocket);
    		}catch(IOException e){
    			e.printStackTrace();		
    		}
        join(joinmsg);
//		System.out.println("................");

		return true;
    }
//************************************************************************************
    	
	public void join(String[] joinmsg) {
		String msg="join"+","+joinmsg[0]+","+joinmsg[1];
		new ClientTask().execute(msg);
	}
	
	public void insert_noid(String j_node, String j_port) {
    //always executed by 5554
		hashlist.add(j_node);
		portlist.add(j_port);
		Collections.sort(hashlist);
//		System.out.println("hashlist.size()="+hashlist.size());
//		if(hashlist.size()>=3){
//			for(int i=1;i<hashlist.size()-1;i++){
//				String neighbors="neighbors"+","+hashlist.get(i-1)+","+hashlist.get(i)+","+hashlist.get(i+1);
//				new ClientTask().execute(neighbors);
//			}
//			String neighbors0="neighbors"+","+hashlist.get(hashlist.size()-1)+","+hashlist.get(0)+","+hashlist.get(1);
//			new ClientTask().execute(neighbors0);
//			String neighborsn="neighbors"+","+hashlist.get(hashlist.size()-2)+","+hashlist.get(hashlist.size()-1)+","+hashlist.get(0);
//			new ClientTask().execute(neighborsn);
//		}
		if(hashlist.size()==1){
			String neighbors="neighbors"+","+hashlist.get(0)+","+hashlist.get(0)+","+hashlist.get(0);
			new ClientTask().execute(neighbors);
		}
		else if(hashlist.size()==2){
			String neighbors="neighbors"+","+hashlist.get(1)+","+hashlist.get(0)+","+hashlist.get(1);
			new ClientTask().execute(neighbors);
			String neighbors2="neighbors"+","+hashlist.get(0)+","+hashlist.get(1)+","+hashlist.get(0);
			new ClientTask().execute(neighbors2);
		}
		else{
			String neighbors="neighbors"+","+hashlist.get(2)+","+hashlist.get(0)+","+hashlist.get(1);
			new ClientTask().execute(neighbors);
			String neighbors1="neighbors"+","+hashlist.get(0)+","+hashlist.get(1)+","+hashlist.get(2);
			new ClientTask().execute(neighbors1);
			String neighbors2="neighbors"+","+hashlist.get(1)+","+hashlist.get(2)+","+hashlist.get(0);
			new ClientTask().execute(neighbors2);
		}
	}	

	public String getport(String hvalue, ArrayList<String> plist) {
		String newhashvalue;
		for(int i=0;i<plist.size();i++){
			try {
				newhashvalue=genHash(plist.get(i));
				if(newhashvalue.equals(hvalue)){
//					System.out.println("plist.get(i)="+plist.get(i));
					return plist.get(i);
				}
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	private class ServerTask extends AsyncTask<ServerSocket, String, Void>{
	    @Override
	    protected Void doInBackground(ServerSocket...sockets){
//	        String msg = null;
	        ServerSocket serverSocket = sockets[0];
	        Socket socket;
	        try{
	            while(true){
	                socket = serverSocket.accept(); //wait for the client for request;
	                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	                mmssgg = in.readLine();
	                fields = mmssgg.split(",");
//	                System.out.println("fields[0]_ser "+fields[0]);

	                if(fields[0].equals("insert")){
	                	ContentValues cv = new ContentValues();
	  	                cv.put(KEY_COLUMN,fields[1]);
	  	                cv.put(VALUE_COLUMN,fields[2]);
	  	                mContentResolver.insert(mUri,cv);
	                }
	                else if(fields[0].equals("query")){
	                	querynode_id=fields[2];
	                	mContentResolver.query(mUri, null, fields[1], null, null);
	                }
	                else if(fields[0].equals("GDump")){
	                	querynode_id=fields[1];
	                	mContentResolver.query(mUri, null, null, null, null);
	                }
	                else if(fields[0].equals("join")){
//	                	System.out.println("fields[1],fields[2]"+fields[1]+"......."+fields[2]);
	                	insert_noid(fields[1],fields[2]);
	                }
	                else if(fields[0].equals("neighbors")){
	                	predecessor_id=fields[1];
	    				successor_id=fields[3];
//	    				System.out.println("predecessor_id="+predecessor_id);
//	    				System.out.println("successor_id="+successor_id);
	                }
	                
	                else{
	                	if(fields[0].equals(mynode_id)){
	                		for(int i=2;i<fields.length;i=i+2){
		                		String columnValues[]={fields[i],fields[i+1]};
		                		matrixCursor.addRow(columnValues);
		                		GmatrixCursor.addRow(columnValues);
	                		}
//	                		String columnValues[]={fields[2],fields[3]};
//	                		matrixCursor.addRow(columnValues);
	                		flag=false;
	                		Gflag1=false;
	                	}
	                	else if(fields[1].equals("GDump")){
		                	querynode_id=fields[0];
	                        mContentResolver.query(mUri, null, null, null, null);
	                	}
	                	else{
	     				 new ClientTask().execute(mmssgg);
	                	}
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
//                System.out.println("msgs[0]   "+msgs[0]);

                if(fields[0].equals("join")){
 //                   System.out.println("fields[0]   "+fields[0]);
                	socket=new Socket("10.0.2.2",11108);
                }
                else if(fields[0].equals("neighbors")){
    				sendport=getport(fields[2],portlist);
 //   				System.out.println("sendport="+sendport);
                	socket=new Socket("10.0.2.2",Integer.parseInt(sendport)*2);
                }
                else{
 //               	System.out.println("successor_id=============="+successor_id);
                	sendport=getport(successor_id,templist);
//                	System.out.println("sendport=============="+sendport);

					socket=new Socket("10.0.2.2",Integer.parseInt(sendport)*2);
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

    public Uri buildUri(String scheme, String authority) {
        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.authority(authority);
        uriBuilder.scheme(scheme);
        return uriBuilder.build();
    }

}
