package com.example.stoplichtenevaluatieapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.stoplichtenevaluatieapp.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class LoginFragment extends Fragment {
    private static final String TAG = "MainActivity";

    private FirebaseAuth mAuth;
    private Boolean firstTryDone = false;

    RelativeLayout loadingPanel;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        mAuth = FirebaseAuth.getInstance();

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_login, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final EditText username = (EditText)view.findViewById(R.id.username);
        final EditText password = (EditText)view.findViewById(R.id.password);
        loadingPanel = view.findViewById(R.id.loadingPanel_login);
        loadingPanel.setVisibility(View.GONE);

        if (!User.newUser) {
            TextView explainer = view.findViewById(R.id.pw_explainer);
            explainer.setVisibility(View.GONE);
            view.findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadingPanel.setVisibility(View.VISIBLE);
                    login(username.getText().toString(), password.getText().toString());
                }
            });
        } else {
            TextView title = view.findViewById(R.id.login_title);
            title.setText(R.string.register);
            Button loginButton = view.findViewById(R.id.login);
            loginButton.setText(R.string.register);
            loginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loadingPanel.setVisibility(View.VISIBLE);
                    register(username.getText().toString(), password.getText().toString());
                }
            });
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    public void  updateUI(FirebaseUser account){
        if(account != null){
            Toast.makeText(this.getActivity(),R.string.successful_login, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this.getActivity(), HomeScreen.class);
            startActivity(intent);
        }else if (firstTryDone) {
            Toast.makeText(this.getActivity(),R.string.failed_login,Toast.LENGTH_LONG).show();
        }
    }

    public void updateUIOnRegister(FirebaseUser account) {
        if(account != null){
            User.newUser = false;
            Toast.makeText(this.getActivity(),R.string.successful_register, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this.getActivity(), HomeScreen.class);
            startActivity(intent);
        }else if (firstTryDone) {
            Toast.makeText(this.getActivity(),R.string.failed_register,Toast.LENGTH_LONG).show();
        }
    }


    public void login(String username, String password) {
        this.firstTryDone = true;
        if (username.length() > 0 && password.length() > 0) {
            mAuth.signInWithEmailAndPassword(username, password)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            loadingPanel.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                updateUI(null);
                            }
                        }
                    });
            return;
        }
        updateUI(null);
    }

    public void register(String username, String password) {
        this.firstTryDone = true;if (username.length() > 0 && password.length() > 0) {
            mAuth.createUserWithEmailAndPassword(username, password)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            loadingPanel.setVisibility(View.GONE);
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmailAndPassword:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUIOnRegister(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmailAndPassword:failure", task.getException());
                                updateUIOnRegister(null);
                            }
                        }
                    });
            return;
        }
        updateUIOnRegister(null);
    }

}
