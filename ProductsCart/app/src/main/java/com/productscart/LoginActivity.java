package com.productscart;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {
    EditText et_email,et_password;
    Button btn_login;
    TextView tv_register;
    ProgressDialog loadingBar;
    private String parentDbName = "Registration";
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setTitle("Login");

        loadingBar = new ProgressDialog(LoginActivity.this);
        db = FirebaseFirestore.getInstance();

        et_email=(EditText)findViewById(R.id.et_email);
        et_password=(EditText)findViewById(R.id.et_password);

        tv_register=(TextView)findViewById(R.id.tv_register);
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }
        });

        btn_login=(Button)findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginUser();
            }
        });

    }
    private void LoginUser() {
        String username = et_email.getText().toString().trim();
        String password = et_password.getText().toString().trim();

        if (TextUtils.isEmpty(username))
        {
            Toast.makeText(this, "Please write your Username...", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please write your password...", Toast.LENGTH_SHORT).show();
        }
        else {
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please wait, while we are checking the credentials.");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAccessToAccount(username, password);

        }
    }
    private void AllowAccessToAccount(final String username, final String password) {

        db.collection(parentDbName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        loadingBar.dismiss();
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Users usersData = document.toObject(Users.class);
                                if(usersData.getEmail().equals(username)){
                                    if(usersData.getPassword().equals(password)){
                                        Toast.makeText(LoginActivity.this, "logged in Successfully...", Toast.LENGTH_SHORT).show();
                                        loadingBar.dismiss();
                                        Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }

                            }
                        }

                        else {
                            Toast.makeText(LoginActivity.this, "Credentials are incorrect please try again ", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }
}