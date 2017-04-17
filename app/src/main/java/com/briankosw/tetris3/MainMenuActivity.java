package com.briankosw.tetris3;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * MainMenuActivity is the page after the user is logged-in/registered. It contains a list of
 * buttons leading to different pages such as Tetris game, leaderboard, chatting, and account details
 */
public class MainMenuActivity extends AppCompatActivity{
    private Button playButton;
    private Button leaderboardButton;
    private Button chattingButton;
    private Button accountButton;

    /**
     * Overridden onCreate method that sets up the views on this Activity and buttons.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        playButton = (Button)findViewById(R.id.playGameButton);
        leaderboardButton = (Button)findViewById(R.id.leaderboardButton);
        chattingButton = (Button)findViewById(R.id.chattingRoomButton);
        accountButton = (Button)findViewById(R.id.accountButton);

        setUpButtons();
    }

    /**
     * setUpButtons method that sets up the buttons on this Activity by attaching onClickListeners
     */
    public void setUpButtons() {
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenuActivity.this, TetrisGameEngine.class));
            }
        });
        leaderboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenuActivity.this, LeaderboardActivity.class));
            }
        });
        chattingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenuActivity.this, ChattingActivity.class));
            }
        });
        accountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainMenuActivity.this, AccountActivity.class));
            }
        });
    }
}
