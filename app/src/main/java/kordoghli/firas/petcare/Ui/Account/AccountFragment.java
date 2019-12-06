package kordoghli.firas.petcare.Ui.Account;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import kordoghli.firas.petcare.R;
import kordoghli.firas.petcare.Utile.SessionManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class AccountFragment extends Fragment {
    private SessionManager sessionManager;
    private Button bntLogOut;

    public AccountFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_account, container, false);
        bntLogOut = view.findViewById(R.id.logOutBtn);
        // User Session Manager
        sessionManager = new SessionManager(getContext());
        bntLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionManager.logoutUser();
            }
        });

        return view;
    }

}
