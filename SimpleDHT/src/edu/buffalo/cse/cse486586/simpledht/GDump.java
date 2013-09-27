package edu.buffalo.cse.cse486586.simpledht;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class GDump implements OnClickListener {
    private final TextView mTextView;
    private final ContentResolver mContentResolver;
    private Uri mUri;
    private static final String KEY_FIELD = "key";
    private static final String VALUE_FIELD = "value";
    
	public GDump(TextView mTextView, ContentResolver mContentResolver) {
		this.mTextView = mTextView;
		this.mContentResolver = mContentResolver;
        mUri = buildUri("content", "edu.buffalo.cse.cse486586.simpledht.provider");
	}
    private Uri buildUri(String scheme, String authority) {
        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.authority(authority);
        uriBuilder.scheme(scheme);
        return uriBuilder.build();
    }

	@Override
	public void onClick(View v) {
		SimpleDhtProvider.querynode_id=SimpleDhtProvider.mynode_id;
		String[] GLump={"rui","jie","ge"};
        Cursor cursor = mContentResolver.query(mUri, null, null, GLump, null);
        System.out.println("cursor.getCount()="+cursor.getCount());
		while (cursor.moveToNext()){
            int keyIndex = cursor.getColumnIndex(KEY_FIELD);               
            int valueIndex = cursor.getColumnIndex(VALUE_FIELD);
            String returnKey = cursor.getString(keyIndex);
            String returnValue = cursor.getString(valueIndex);
            String ldht=returnKey+":"+returnValue;
            mTextView.append(ldht+"\n");
		}
		cursor.close();
	}

}
