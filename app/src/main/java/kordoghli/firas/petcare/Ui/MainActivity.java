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
import kordoghli.firas.petcare.Ui.Blog.BlogFragment;
import kordoghli.firas.petcare.Ui.Home.HomeFragment;
import kordoghli.firas.petcare.Ui.MyPet.MyPetsFragment;
import kordoghli.firas.petcare.Utile.SessionManager;

public class MainActivity extends AppCompatActivity {
    private SessionManager sessionManager;

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

                        case R.id.blog:
                            selectedFragment = new BlogFragment();
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

        // User Session Manager
        sessionManager = new SessionManager(getApplicationContext());
        // Check user login (this is the important point)
        // If User is not logged in , This will redirect user to LoginActivity
        // and finish current activity from activity stack.
        if (sessionManager.checkLogin())
            finish();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
    }
}
