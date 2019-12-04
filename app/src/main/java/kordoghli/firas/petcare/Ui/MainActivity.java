package kordoghli.firas.petcare.Ui;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import kordoghli.firas.petcare.R;
import kordoghli.firas.petcare.Ui.Account.AccountFragment;
import kordoghli.firas.petcare.Ui.Adoptions.AdoptionsFragment;
import kordoghli.firas.petcare.Ui.Diary.DiaryFragment;
import kordoghli.firas.petcare.Ui.Home.HomeFragment;
import kordoghli.firas.petcare.Ui.MyPet.MyPetsFragment;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                    Fragment selectedFragment = null;
                    switch (menuItem.getItemId()) {
                        case R.id.home:
                            selectedFragment = new HomeFragment();
                            break;

                        case R.id.account:
                            selectedFragment = new AccountFragment();
                            break;

                        case R.id.adoptions:
                            selectedFragment = new AdoptionsFragment();
                            break;

                        case R.id.myPets:
                            selectedFragment = new MyPetsFragment();
                            break;

                        case R.id.diary:
                            selectedFragment = new DiaryFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                    return true;
                }
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
    }
}
