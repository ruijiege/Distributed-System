package edu.buffalo.cse.cse486586.simpledynamo;

import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;


public class SimpleDynamoActivity extends Activity {

	private Button Put1Button;
	private Button Put2Button;
	private Button Put3Button;
	private Button LDumpButton;
	private Button GetButton;
	public static String portStr;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_simple_dynamo);
		
        TelephonyManager tel =
                (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        portStr=tel.getLine1Number().substring(tel.getLine1Number().length()-4);
        
        Put1Button=(Button)findViewById(R.id.button1);
        Put2Button=(Button)findViewById(R.id.button2);
        Put3Button=(Button)findViewById(R.id.button3);
        LDumpButton=(Button)findViewById(R.id.button4);
        GetButton=(Button)findViewById(R.id.button5);
    
		TextView tv = (TextView) findViewById(R.id.textView1);
        tv.setMovementMethod(new ScrollingMovementMethod());
        
        Put1Button.setOnClickListener(new Put1(tv, getContentResolver()));
        Put2Button.setOnClickListener(new Put2(tv, getContentResolver()));
        Put3Button.setOnClickListener(new Put3(tv, getContentResolver()));
        LDumpButton.setOnClickListener(new LDump(tv, getContentResolver()));
        GetButton.setOnClickListener(new Get(tv, getContentResolver()));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.simple_dynamo, menu);
		return true;
	}	

}
