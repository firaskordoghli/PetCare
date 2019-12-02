package kordoghli.firas.petcare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        Toast.makeText(MainActivity.this, "home", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.events:
                        Toast.makeText(MainActivity.this, "events", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.explore:
                        Toast.makeText(MainActivity.this, "explore", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
    }
}
