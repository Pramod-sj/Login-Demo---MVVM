package com.pramod.login_mvvm.ui.login;

public class LoginFormState {
    private final String emailError;
    private final String passwordError;
    private final boolean isDataValid;

    private LoginFormState(String emailError, String passwordError, boolean isDataValid) {
        this.emailError = emailError;
        this.passwordError = passwordError;
        this.isDataValid = isDataValid;
    }

    public static LoginFormState init(String emailError, String passwordError) {
        return new LoginFormState(emailError, passwordError, false);
    }

    public static LoginFormState init(boolean isDataValid) {
        return new LoginFormState(null, null, isDataValid);
    }

    public String getEmailError() {
        return emailError;
    }

    public String getPasswordError() {
        return passwordError;
    }

    public boolean isDataValid() {
        return isDataValid;
    }
}
