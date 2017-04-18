package com.briankosw.tetris3;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.HashSet;
import java.util.regex.Pattern;

/**
 * LoginActivity is the first Activity the user is presented with. Using Firebase authentication,
 * the user is able to register and/or login to use this application.
 */
public class LoginActivity extends AppCompatActivity {
    private static final int translateValue = -400;
    private static final int translateDuration = 1000;
    private static final int crossFadeDuration = 1100;
    private static final float crossFadeInitValue = 0f;
    private static final float crossFadeFinalValue = 1f;
    private static final int requiredPasswordLength = 6;
    private static final int initScore = 0;
    private ImageView logoImage;
    private EditText nameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginRegisterButton;
    private Button registerButton;
    private ProgressBar progressBar;
    private FirebaseAuth authentication;

    /**
     * Overridden onCreate method that sets up all the Views on this Activity, the buttons, and
     * the authentication system.
     *
     * @param savedInstanceState savedInstanceState bundle
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        authentication = FirebaseAuth.getInstance();
        logoImage = (ImageView)findViewById(R.id.logoButton);
        nameEditText = (EditText)findViewById(R.id.nameEditText);
        emailEditText = (EditText)findViewById(R.id.emailEditText);
        passwordEditText = (EditText)findViewById(R.id.passwordEditText);
        loginRegisterButton = (Button)findViewById(R.id.loginRegisterButton);
        registerButton = (Button)findViewById(R.id.registerButton);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);

        setUpButtons();
        FirebaseAuth.getInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if (firebaseUser == null) {
                    nameEditText.setText("");
                    emailEditText.setText("");
                    passwordEditText.setText("");
                }
            }
        });
    }

    /**
     * setUpButtons method that sets up the buttons on this Activity by attaching onClickListeners
     */
    public void setUpButtons() {
        logoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TranslateAnimation transAnimation = new TranslateAnimation(0, 0, 0, translateValue);
                transAnimation.setDuration(translateDuration);
                transAnimation.setFillAfter(true);
                logoImage.startAnimation(transAnimation);
                crossFade();
                logoImage.setEnabled(false);
            }
        });
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (loginRegisterButton.getText().toString().equals(getString(R.string.login))) {
                    loginRegisterButton.setText(getString(R.string.register));
                    registerButton.setText(getString(R.string.loginButton));
                    nameEditText.setVisibility(View.VISIBLE);
                } else {
                    loginRegisterButton.setText(getString(R.string.login));
                    registerButton.setText(getString(R.string.registerButton));
                    nameEditText.setVisibility(View.GONE);
                }
            }
        });
        loginRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString().trim();
                if (!checkInputs(name, email, password)) {
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);
                 if (loginRegisterButton.getText().equals(getString(R.string.register))) {
                    createUser(name, email, password);
                } else {
                    signInUser(email, password);
                }

            }
        });
    }

    /**
     * Overridden onResume method called by the Activity
     */
    @Override
    public void onResume(){
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }

    /**
     * checkInputs method that checks the validity of the email and password and warns the user
     * if the inputted email/password is not valid
     *
     * @param email string of inputted email
     * @param password string of inputted password
     */
    public boolean checkInputs(String name, String email, String password) {
        if (TextUtils.isEmpty(name) && registerButton.getText().toString()
                                                               .equals(getString(R.string.login))) {
            Toast.makeText(getApplicationContext(), getString(R.string.empty_name),
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if (!isNameValid(name) && registerButton.getText().toString()
                                                          .equals(getString(R.string.login))) {
            Toast.makeText(getApplicationContext(), getString(R.string.incorrect_name),
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplicationContext(), getString(R.string.empty_email),
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (TextUtils.isEmpty(password)) {
            Toast.makeText(getApplicationContext(), getString(R.string.empty_password),
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (!isEmailValid(email)) {
            Toast.makeText(getApplicationContext(), getString(R.string.incorrect_email),
                    Toast.LENGTH_SHORT).show();
            return false;
        } else if (!isPasswordValid(password)) {
            Toast.makeText(getApplicationContext(), getString(R.string.incorrect_password),
                    Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    /**
     * createUser method that creates a user with inputted email/password combination in Firebase
     * and send user to MainMenuActivity page
     *
     * @param email string of inputted email
     * @param password string of inputted password
     */
    public void createUser(final String name, final String email, String password) {
        authentication.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        startActivity(new Intent(LoginActivity.this, MainMenuActivity.class));
                        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("users");
                        String userId = databaseRef.push().getKey();
                        User user = new User(email, name, new HashMap<>(), new HashSet<>(), initScore);
                        databaseRef.child(userId).setValue(user);
                    } else {
                        if (task.getException().toString()
                                                    .contains(getString(R.string.taken_email))) {
                            Toast.makeText(LoginActivity.this, getString(R.string.taken_email)
                                    + ". Try another email", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            });
    }

    /**
     * singInUser method that signs in the user with inputted email/password combination. If
     * authentication is successful, method sends the user to the MainMenuActivity page. If
     * unsuccessful, warns the user that authentication failed.
     *
     * @param email string of inputted email
     * @param password string of inputted password
     */
    public void signInUser(String email, String password) {
        authentication.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    progressBar.setVisibility(View.GONE);
                    if (task.isSuccessful()) {
                        startActivity(new Intent(LoginActivity.this, GestureDetectorExample.class));
                    } else {
                        Toast.makeText(LoginActivity.this, getString(R.string.failed_authentication),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    /**
     * isEmailValid method that uses regex pattern to validate email form input
     *
     * @param email string of email input by user
     * @return true/false depending on validity of user input
     */
    private boolean isEmailValid(String email) {
        final String emailRegex = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x07\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x07\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";
        Pattern emailPattern = Pattern.compile(emailRegex);
        return emailPattern.matcher(email).matches();
    }

    /**
     * isNameValid method that checks if both first and last name were inputted
     *
     * @param name string of name input by user
     * @return true/false depending on validity of user input
     */
    private boolean isNameValid(String name) {
        String[] nameArray = name.split("");
        return nameArray.length == 2;
    }

    /**
     * isPasswordValid method that checks for password length longer than 6
     *
     * @param password string of password input by user
     * @return true/false depending on length of user input
     */
    private boolean isPasswordValid(String password) {
        return password.length() > requiredPasswordLength;
    }

    /**
     * crossFade method used to cross-fade the EditTexts and Buttons in the Activity
     */
    private void crossFade() {
        LinearLayout linearLayout = (LinearLayout)findViewById(R.id.loginLinearLayout);
        linearLayout.setVisibility(View.VISIBLE);
        linearLayout.setAlpha(crossFadeInitValue);
        linearLayout.animate()
                .alpha(crossFadeFinalValue)
                .setDuration(crossFadeDuration)
                .setListener(null);
    }
}
