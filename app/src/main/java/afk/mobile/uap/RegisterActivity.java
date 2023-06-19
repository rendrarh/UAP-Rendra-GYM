package afk.mobile.uap;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    Button _bttRegister, _bttLogin;
    EditText _txtName, _txtEmail, _txtPassword;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        _bttLogin = findViewById(R.id.bttLogin);
        _bttRegister = findViewById(R.id.bttRegister);

        _txtName = findViewById(R.id.txtNama);
        _txtEmail = findViewById(R.id.txtEmail);
        _txtPassword = findViewById(R.id.txtPassword);


        mAuth = FirebaseAuth.getInstance();


        _bttRegister.setOnClickListener((v) -> {
            String nama = _txtName.getText().toString().trim();
            String email = _txtEmail.getText().toString().trim();
            String password = _txtPassword.getText().toString().trim();

            if(TextUtils.isEmpty(nama)){
                _txtName.setError("Name is Required!");
                return;
            }

            if(TextUtils.isEmpty(nama)){
                _txtEmail.setError("Email is Required!");
                return;
            }

            if(TextUtils.isEmpty(nama)){
                _txtPassword.setError("Password is Required!");
                return;
            }

            if(password.length() < 6){
                _txtPassword.setError("Password must be >= 6 character");
                return;
            }

            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener((task) -> {
                if(task.isSuccessful()){
                    Toast.makeText(RegisterActivity.this,"the registration process is successful!",Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                }else{
                    Toast.makeText(RegisterActivity.this,"Please repeat the process in a few moments!",Toast.LENGTH_SHORT).show();
                }
            });
        });


        _bttLogin.setOnClickListener((v) -> {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        });
    }
}
