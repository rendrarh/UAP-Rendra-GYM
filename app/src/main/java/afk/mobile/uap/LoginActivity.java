package afk.mobile.uap;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    EditText email, password;
    TextView btLogintoRgs;
    Button login;
    ImageView backBT;
    FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.txEmail);
        password = findViewById(R.id.txPass);
        login = findViewById(R.id.btLogin);
        backBT = findViewById(R.id.btBack);
        btLogintoRgs = findViewById(R.id.btReg);

        fAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailTXT = email.getText().toString().trim();
                String passwordTXT = password.getText().toString().trim();

                if(TextUtils.isEmpty(emailTXT)){
                    email.setError("Masukan Email");
                    return;
                }

                if(TextUtils.isEmpty(passwordTXT)){
                    password.setError("Masukan password");
                    return;
                }

                if (password.length() < 6) {
                    password.setError("Pasword harus lebih dari 6 karakter");
                    return;
                }


                fAuth.signInWithEmailAndPassword(emailTXT, passwordTXT).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "login sukses", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), InsertActivity.class));
                        } else {
                            Toast.makeText(LoginActivity.this, "Email atau password anda salah", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
            }
        });

        backBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });

        btLogintoRgs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
            }
        });


    }
}
