package progark.a15;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

public class DiffSelectActivity extends Activity {
	private RadioGroup diffRadioGroup;
	private RadioGroup charRadioGroup;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diffselect);
        
        diffRadioGroup = (RadioGroup) findViewById(R.id.diff_radiogroup);
        charRadioGroup = (RadioGroup) findViewById(R.id.char_radiogroup);
        
        Button startButton = (Button) findViewById(R.id.start_button);
        
        startButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				// Check if both difficulty and character are selected
				int difficulty = -1;
				int playerType = -1;
				
				switch (diffRadioGroup.getCheckedRadioButtonId()) {
				case R.id.easy_radio:
					difficulty = 0;
					break;
				case R.id.medium_radio:
					difficulty = 1;
					break;
				case R.id.hard_radio:
					difficulty = 2;
				}
				
				switch (charRadioGroup.getCheckedRadioButtonId()) {
				case R.id.first_char_radio:
					playerType = 0;
					break;
				case R.id.second_char_radio:
					playerType = 1;
					break;
				default:
					playerType = -1;
					break;
				}
				if (difficulty != -1 && playerType != -1) {
					
					Intent intent = new Intent(DiffSelectActivity.this, GameActivity.class);
					
					intent.putExtra("difficulty", difficulty);
					intent.putExtra("playerType", playerType);
					
					DiffSelectActivity.this.startActivity(intent);
					
				} else {
					// Tell the user to choose difficulty and character
					Toast.makeText(getApplicationContext(), "You must choose a difficulty and a character.", Toast.LENGTH_SHORT).show();
				}
			}
		});
        
    }
}