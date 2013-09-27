package edu.buffalo.cse.cse486586.simpledynamo;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class Get implements OnClickListener {
    public  TextView mTextView;
    private final ContentResolver mContentResolver;
    private Uri mUri;
    private static final String KEY_FIELD = "key";
    private static final String VALUE_FIELD = "value";

    
	public Get(TextView mTextView, ContentResolver mContentResolver) {
		this.mTextView = mTextView;
		this.mContentResolver = mContentResolver;
        mUri = buildUri("content", "edu.buffalo.cse.cse486586.simpledynamo.provider");

	}
    private Uri buildUri(String scheme, String authority) {
        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.authority(authority);
        uriBuilder.scheme(scheme);
        return uriBuilder.build();
    }

	@Override
	public void onClick(View v) {
		new Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}
	
	private class Task extends AsyncTask<Void, String, Void> {

		@Override
		protected Void doInBackground(Void... params) {
//			SimpleDhtProvider.querynode_id=SimpleDhtProvider.mynode_id;
			for(int i=0;i<20;i++){
		        Cursor cursor = mContentResolver.query(mUri, null, Integer.toString(i), null, null);
		        System.out.println("cursor.getCount()="+cursor.getCount());
		        cursor.moveToNext();
	            int keyIndex = cursor.getColumnIndex(KEY_FIELD);               
	            int valueIndex = cursor.getColumnIndex(VALUE_FIELD);
	            String returnKey = cursor.getString(keyIndex);
	            String returnValue = cursor.getString(valueIndex);
	            
	            String ldht=returnKey+":"+returnValue;
	            System.out.println("ldht="+ldht);
	            
//	            mTextView.append(ldht+"\n");
	            publishProgress(ldht+"\n");	
	            
	            cursor.close();
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}		
			return null;
		}
		
		protected void onProgressUpdate(String...strings) {
			mTextView.append(strings[0]);
			return;
		}
	}

}
