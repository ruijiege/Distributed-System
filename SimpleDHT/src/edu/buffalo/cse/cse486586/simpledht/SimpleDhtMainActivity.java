package edu.buffalo.cse.cse486586.simpledht;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;

public class SimpleDhtMainActivity extends Activity {
	private Button LDumpButton;
	private Button GDumpButton;
	public static TextView tv;
	public static String portStr;
	public static String mynode_id;
	public static String predecessor_id;
	public static String successor_id;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_dht_main);
        
        TelephonyManager tel =
                (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        portStr=tel.getLine1Number().substring(tel.getLine1Number().length()-4);
        
        tv = (TextView) findViewById(R.id.textView1);
        LDumpButton=(Button)findViewById(R.id.button1);
        GDumpButton=(Button)findViewById(R.id.button2);
        
        tv.setMovementMethod(new ScrollingMovementMethod());
        LDumpButton.setOnClickListener(new LDump(tv, getContentResolver()));
        GDumpButton.setOnClickListener(new GDump(tv, getContentResolver()));
        findViewById(R.id.button3).setOnClickListener(new OnTestClickListener(tv, getContentResolver()));

//        SimpleDhtProvider sdp=new SimpleDhtProvider();
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_simple_dht_main, menu);
        return true;
    }

}
