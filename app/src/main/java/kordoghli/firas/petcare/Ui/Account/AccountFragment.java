package kordoghli.firas.petcare.Ui.Account;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import kordoghli.firas.petcare.Data.User;
import kordoghli.firas.petcare.R;
import kordoghli.firas.petcare.Utile.Adapters.SectionPagerAdapter;
import kordoghli.firas.petcare.Utile.SessionManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {
    private SessionManager sessionManager;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ImageView menuBtnIv,profileIv;
    private TextView emaimProfileTv,usernameProfileTv;

    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        viewPager = view.findViewById(R.id.viewPager);
        tabLayout = view.findViewById(R.id.tabLayout);
        menuBtnIv = view.findViewById(R.id.ivMenuBtn);
        profileIv = view.findViewById(R.id.ivProfile);
        emaimProfileTv = view.findViewById(R.id.tvProfileEmail);
        usernameProfileTv = view.findViewById(R.id.tvProfileUsername);

        // User Session Manager
        sessionManager = new SessionManager(getContext());
        Gson gson = new Gson();
        User currentUser = gson.fromJson(sessionManager.getUserDetails(), User.class);

        emaimProfileTv.setText(currentUser.getEmail());
        usernameProfileTv.setText(currentUser.getUsername());

        menuBtnIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popup = new PopupMenu(getActivity(), view);
                popup.getMenuInflater().inflate(R.menu.drawer_view,popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        switch(item.getItemId()){

                            case R.id.nav_third_fragment:
                                Toast.makeText(getActivity(), "test 3", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.nav_about:
                                Toast.makeText(getActivity(), "test 4", Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.nav_log_out:
                                // Clear the User session data
                                // and redirect user to LoginActivity
                                sessionManager.logoutUser();
                                getActivity().finish();
                                break;
                        }
                        return false;
                    }
                });
                popup.show();
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setUpViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void setUpViewPager(ViewPager viewPager) {
        SectionPagerAdapter adapter = new SectionPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new MyAdoptionsFragment(), "my adoptions");
        adapter.addFragment(new MyPetsProfileFragment(), "my pets");

        viewPager.setAdapter(adapter);
    }

}
