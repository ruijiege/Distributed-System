package edu.buffalo.cse.cse486586.simpledynamo;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class Put2 implements OnClickListener {
	
	private static final int TEST_CNT = 20;
	private static final String KEY_FIELD = "key";
	private static final String VALUE_FIELD = "value";
	private static final String VERSION_FIELD = "version";

	private final TextView mTextView;
	private final ContentResolver mContentResolver;
	private final Uri mUri;
	private ContentValues[] mContentValues;

	public Put2(TextView _tv, ContentResolver _cr) {
		mTextView = _tv;
		mContentResolver = _cr;
		mUri = buildUri("content", "edu.buffalo.cse.cse486586.simpledynamo.provider");
		mContentValues = initTestValues();
	}

	private Uri buildUri(String scheme, String authority) {
		Uri.Builder uriBuilder = new Uri.Builder();
		uriBuilder.authority(authority);
		uriBuilder.scheme(scheme);
		return uriBuilder.build();
	}

	private ContentValues[] initTestValues() {
		ContentValues[] cv = new ContentValues[TEST_CNT];
		for (int i = 0; i < TEST_CNT; i++) {
			cv[i] = new ContentValues();
			cv[i].put(KEY_FIELD, Integer.toString(i));
			cv[i].put(VALUE_FIELD, "Put2" + Integer.toString(i));
			cv[i].put(VERSION_FIELD, Integer.toString(SimpleDynamoProvider.versionNum));
		}

		return cv;
	}

	@Override
	public void onClick(View v) {
		SimpleDynamoProvider.versionNum=SimpleDynamoProvider.versionNum+1;
		System.out.println("SimpleDynamoProvider.versionNum="+SimpleDynamoProvider.versionNum);
		ContentValues[] cv = new ContentValues[TEST_CNT];
		for (int i = 0; i < TEST_CNT; i++) {
			cv[i] = new ContentValues();
			cv[i].put(KEY_FIELD, Integer.toString(i));
			cv[i].put(VALUE_FIELD, "Put2" + Integer.toString(i));
			cv[i].put(VERSION_FIELD, Integer.toString(SimpleDynamoProvider.versionNum));
		}
		mContentValues=cv;
		new Task().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
	}
	
	private class Task extends AsyncTask<Void, String, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			publishProgress("Put2 Inserting...\n");
			if (testInsert()) {
				publishProgress("Put2 Insert success\n");
			} else {
				publishProgress("Put2 Insert fail\n");
				return null;
			}			
			return null;
		}
		
		protected void onProgressUpdate(String...strings) {
			mTextView.append(strings[0]);
			return;
		}

		private boolean testInsert() {
			try {
				for (int i = 0; i < TEST_CNT; i++) {
					mContentResolver.insert(mUri, mContentValues[i]);
					Thread.sleep(1000);
				}
			} catch (Exception e) {
				System.out.println(e.toString());
				return false;
			}

			return true;
		}
	}

}
