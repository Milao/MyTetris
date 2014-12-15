package zo.milaso.mytetris;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.*;
import android.widget.Button;

public class MainActivity extends Activity {

	Button bnt_start,bnt_rank,bnt_help,bnt_set;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		bnt_start = (Button)findViewById(R.id.start);
		bnt_rank = (Button) findViewById(R.id.rank);
		bnt_help = (Button)findViewById(R.id.help);
		bnt_set = (Button)findViewById(R.id.setting);
	}
	public void setting_btn(View view){
		Intent intent = new Intent(MainActivity.this,setting.class);
		startActivity(intent);
	}
	
	public void rank_btn(View view){
		Intent intent = new Intent(MainActivity.this,rank.class);
		startActivity(intent);
	}
	public void help_btn(View view){
		Intent intent = new Intent(MainActivity.this,help.class);
		startActivity(intent);
	}
	public void start_btn(View view){
		Intent intent = new Intent(MainActivity.this,Game.class);
		startActivity(intent);
	}
	
}
