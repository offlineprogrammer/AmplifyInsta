package com.offlineprogrammer.amplifyinsta;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.auth.AuthProvider;
import com.amplifyframework.core.Amplify;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    ImageButton login_with_amazon;
    TextView login_textView;
    ProgressBar log_in_progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        login_with_amazon = findViewById(R.id.login_with_amazon);
        login_textView = findViewById(R.id.login_textView);
        log_in_progress = findViewById(R.id.log_in_progress);

        login_with_amazon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                log_in_progress.setVisibility(View.VISIBLE);
                login_with_amazon.setVisibility(View.GONE);
                login();
            }
        });


    }

    private void login() {
        Amplify.Auth.signInWithSocialWebUI(
                AuthProvider.amazon(),
                this,
                result -> {
                    Log.i(TAG, "AuthQuickstart RESULT " + result.toString());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            login_textView.setText("Login is successfull");
                            login_with_amazon.setVisibility(View.GONE);
                            log_in_progress.setVisibility(View.GONE);
                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                            finish();
                        }
                    });
                },
                error -> {
                    Log.e(TAG, "AuthQuickstart ERROR " + error.toString());
                }
        );

    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent.getData() != null && "amplifyinsta".equals(intent.getData().getScheme())) {
            Amplify.Auth.handleWebUISignInResponse(intent);
        }
    }

}

