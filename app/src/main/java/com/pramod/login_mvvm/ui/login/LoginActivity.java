package com.pramod.login_mvvm.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.pramod.login_mvvm.R;
import com.pramod.login_mvvm.databinding.ActivityLoginBinding;
import com.pramod.login_mvvm.ui.main.MainActivity;


public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;
    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        binding.setLifecycleOwner(this);
        binding.setLoginViewModel(loginViewModel);
        setUpLoginObservers();
    }

    private void setUpLoginObservers() {
        loginViewModel.observeNavigateToMainActivity().observe(this, stringEvent -> {
            if (stringEvent.getContentIfNotHandled() != null) {
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        });
        loginViewModel.observeLogingErrorMessage().observe(this, stringEvent -> {
            if (stringEvent.getContentIfNotHandled() != null) {
                Toast.makeText(this, stringEvent.peekContent(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
