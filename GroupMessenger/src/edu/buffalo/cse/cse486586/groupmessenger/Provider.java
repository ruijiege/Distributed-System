package edu.buffalo.cse.cse486586.groupmessenger;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class Provider extends ContentProvider {
	private DBOpenHelper dbOpenHelper;

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
		SQLiteDatabase db= dbOpenHelper.getWritableDatabase();

		long rowid=db.insert("message", "key", values);
		//Uri insertUri=Uri.parse("content://cse.example.providers.personprovider/person"+rowid);
		Uri insertUri=ContentUris.withAppendedId(uri, rowid);
		this.getContext().getContentResolver().notifyChange(uri, null);
		return insertUri;		
	}


	@Override
	public boolean onCreate() {
		dbOpenHelper =new DBOpenHelper(this.getContext());
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		dbOpenHelper.onCreate(db);
		return true;
	}
	
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		SQLiteDatabase db = dbOpenHelper.getReadableDatabase();
		String sel="message.key = '"+selection+"'";
		//String[] pro=new String[]{"key","value"};
		return db.query("message", projection, sel, selectionArgs, null, null, sortOrder);

	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		SQLiteDatabase db = dbOpenHelper.getWritableDatabase();
		int num = 0;
		num = db.update("message", values, selection, selectionArgs);
						
		getContext().getContentResolver().notifyChange(uri, null);
		return num;
	}

}
