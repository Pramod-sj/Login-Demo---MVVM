package com.pramod.login_mvvm.ui.login;

import android.app.Application;
import android.os.Handler;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.pramod.login_mvvm.Event;
import com.pramod.login_mvvm.User;


public class LoginViewModel extends AndroidViewModel {
    private MediatorLiveData<User> formInputLiveData = new MediatorLiveData<>();
    public MutableLiveData<String> mobileLiveData = new MutableLiveData<>();
    public MutableLiveData<String> passwordLiveDta = new MutableLiveData<>();
    private MutableLiveData<LoginFormState> formStateLiveData = new MutableLiveData<>();

    private MutableLiveData<Event<String>> navigateToMainActivity = new MutableLiveData<>();
    private MutableLiveData<Event<Boolean>> logingProgress = new MutableLiveData<>();
    private MutableLiveData<Event<String>> logingErrorMessage = new MutableLiveData<>();

    public LoginViewModel(@NonNull Application application) {
        super(application);
        init();
    }

    private void init() {
        formStateLiveData.setValue(LoginFormState.init(false));
        formInputLiveData.addSource(mobileLiveData, mobile -> {
            User user = formInputLiveData.getValue();
            if (user == null) {
                user = new User();
            }
            user.setMobile(mobile);
            formInputLiveData.setValue(user);
        });
        formInputLiveData.addSource(passwordLiveDta, password -> {
            User user = formInputLiveData.getValue();
            if (user == null) {
                user = new User();
            }
            user.setPassword(password);
            formInputLiveData.setValue(user);
        });
        formStateLiveData = (MutableLiveData<LoginFormState>) Transformations.map(formInputLiveData, user -> {
            if (TextUtils.isEmpty(user.getMobile())) {
                return LoginFormState.init("Mobile number is empty", null);
            }
            if (TextUtils.isEmpty(user.getPassword())) {
                return LoginFormState.init(null, "password is empty");
            }
            return LoginFormState.init(true);
        });
    }

    public void login() {
        User postUserData = formInputLiveData.getValue();
        logingProgress.setValue(Event.init(true));
        new Handler().postDelayed(() -> {
            logingProgress.setValue(Event.init(false));
            if (postUserData != null && postUserData.getMobile().equals("9999999999") && postUserData.getPassword().equals("9999999999")) {
                navigateToMainActivity.setValue(Event.init("Successfully logged in!"));
            } else {
                logingErrorMessage.setValue(Event.init("Failed to login!"));
            }
        }, 3000);
    }

    public LiveData<LoginFormState> getFormStateLiveData() {
        return formStateLiveData;
    }

    public LiveData<Event<String>> observeNavigateToMainActivity() {
        return navigateToMainActivity;
    }

    public LiveData<Event<Boolean>> observeLogingProgress() {
        return logingProgress;
    }

    public LiveData<Event<String>> observeLogingErrorMessage() {
        return logingErrorMessage;
    }
}
