package progark.a15;


import progark.a15.viewController.GameView;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class GameActivity extends Activity {
	private int lastScore=0;
	private Dialog dialog;
	private boolean dialogShown=false;

	private GameView gV;
	private Bundle lastExtras;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		gV = (GameView) this.findViewById(R.id.gameView);
		lastExtras = getIntent().getExtras();
		gV.setGameSettings(lastExtras);
		Handler gameOverHandler = new Handler() {
			public void handleMessage(Message msg) {
				lastScore = msg.getData().getInt("score");
				GameActivity.this.runOnUiThread(new Thread() {
					public void run() {
						GameActivity.this.onGameOver();
					}
				});
			}
		};
		gV.setGameOverHandler(gameOverHandler);
	}

	@Override
	public void onBackPressed() {
		//Monitor key presses

		Log.d("BACK BUTTON PUSH","Pausing game and showing dialog");
		//Pause game
		gV.pause();
		//Create pause dialog.
		//TODO: Change setCancelable(false) to true and make it work!
		final AlertDialog.Builder builder = new AlertDialog.Builder(GameActivity.this);
		builder.setMessage("Game paused")
		.setCancelable(false)
		.setNegativeButton("Restart", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				Intent intent = new Intent(GameActivity.this, GameActivity.class);
				//insert last known user settings
				intent.putExtras(lastExtras);
				startActivity(intent);
			}
		})
		.setNeutralButton("Quit", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				//Start main menu activity
				Intent intent = new Intent(GameActivity.this, MainMenuActivity.class);
				startActivity(intent);
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

	public void onGameOver() {
		if(!dialogShown) {
			Log.d("GAME OVER!","Showing dialog");
			dialog = new Dialog(this);
			dialog.setCancelable(false);
			dialog.setContentView(R.layout.game_over_dialog);
			dialog.setTitle(R.string.gameover_title);

			TextView text = (TextView) dialog.findViewById(R.id.gameover_user_score);
			text.setText(lastScore+"");

			//Quit button. OnClick returns to main menu.
			Button quitBtn = (Button) dialog.findViewById(R.id.gameover_btn_quit);
			quitBtn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					dialog.dismiss();
					Intent intent = new Intent(GameActivity.this, MainMenuActivity.class);
					startActivity(intent);
				}});
			Button submitBtn = (Button) dialog.findViewById(R.id.gameover_btn_submit);
			submitBtn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					EditText nameInput = (EditText) dialog.findViewById(R.id.gameover_name_input);
					String name = nameInput.getText().toString();
					//No input!
					if(name.length()==0) {
						Toast.makeText(GameActivity.this, R.string.gameover_noname_error, Toast.LENGTH_SHORT).show();
					}
					else {
						//Name input. Write score to persistent storage and return to main menu.
						HighScores hs = new HighScores(GameActivity.this);
						hs.writeScore(name, lastScore);
						dialog.dismiss();
						Intent intent = new Intent(GameActivity.this, MainMenuActivity.class);
						startActivity(intent);
					}
				}});
			dialog.show();
		}
		dialogShown=true;
	}
}
