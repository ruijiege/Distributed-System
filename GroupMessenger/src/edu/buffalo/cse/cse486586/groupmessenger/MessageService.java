package edu.buffalo.cse.cse486586.groupmessenger;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MessageService {
	private DBOpenHelper dbOpenHelper;
	
	public MessageService(Context context ) {
		this.dbOpenHelper = new DBOpenHelper(context);
	}
	
	public void insert(Message message){
		SQLiteDatabase db=dbOpenHelper.getWritableDatabase();
		db.execSQL("insert into message(key,value) values(?,?)", 
				new Object[]{message.getKey(), message.getValue()});		
	}
	
	public Message query(String key){
		SQLiteDatabase db=dbOpenHelper.getReadableDatabase();
		Cursor cursor=db.rawQuery("select * from message WHERE key=?", new String[]{key});
		if (cursor.moveToFirst()){
			String key1=cursor.getString(cursor.getColumnIndex("key"));
			String value1=cursor.getString(cursor.getColumnIndex("value"));
			return new Message(key1, value1);
		}
		cursor.close();
		return null;		
	}
	
	public void update(Message message){
		SQLiteDatabase db=dbOpenHelper.getWritableDatabase();
		db.execSQL("update message set value=? where key=?", 
				new Object[]{message.getValue(),message.getKey()});
		
	}

}
