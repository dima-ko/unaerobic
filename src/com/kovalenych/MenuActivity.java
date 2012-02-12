package com.kovalenych;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MenuActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.main_menu);


    }

    View.OnClickListener activListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = null;


            switch (view.getId()) {
                case R.id.menu_art:
                    intent = new Intent(MenuActivity.this, ArticlesActivity.class);
                    break;
                case R.id.menu_videos:
                    intent = new Intent(MenuActivity.this, NostraVideoActivity.class);
                    break;
                case R.id.menu_info:
                    intent = new Intent(MenuActivity.this, ArticlesActivity.class);
                    break;
                case R.id.menu_rank:
                    intent = new Intent(MenuActivity.this, RankingActivity.class);
                    break;
                case R.id.menu_tables:
                    intent = new Intent(MenuActivity.this, TablesActivity.class);
                    break;
                case R.id.menu_heart:
                    Toast.makeText(MenuActivity.this, MenuActivity.this.getString(R.string.under_construction), Toast.LENGTH_SHORT).show();
                    break;

            }
            if (intent != null)
                startActivity(intent); //TODO: troubles
        }
    };
}
