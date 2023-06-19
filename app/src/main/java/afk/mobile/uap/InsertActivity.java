package afk.mobile.uap;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InsertActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView tvNama;
    private Button btKeluar, btTambah, btLiat;
    private FirebaseAuth mAuth;
    private EditText etNama, etAlamat, etHP, etTA, etTM;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_data);
        tvNama = findViewById(R.id.tv_Nama);
        etNama = findViewById(R.id.et_Nama_Baru);
        etAlamat = findViewById(R.id.et_Alamat_Baru);
        etHP = findViewById(R.id.et_HP_Baru);
        etTA = findViewById(R.id.et_TA_Baru);
        etTM = findViewById(R.id.et_TM_Baru);

        btKeluar = findViewById(R.id.btKeluar);
        btTambah = findViewById(R.id.btUpdate);
        btLiat = findViewById(R.id.btLiat);
        btKeluar.setOnClickListener(this);
        btTambah.setOnClickListener(this);
        btLiat.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();

        progressBar = findViewById(R.id.progress);
        progressBar.setVisibility(View.GONE);

    }

    @Override
    protected void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            tvNama.setText(currentUser.getEmail());
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btKeluar:
                logOut();
                break;
            case R.id.btUpdate:
                //Mendapatkan UserID dari pengguna yang Terautentikasi
                String getUserID = mAuth.getCurrentUser().getUid();

                //Mendapatkan Instance dari database
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference getReference;

                //Menyimpan data yang diinputkan user ke dalam variable
                String getNama = etNama.getText().toString();
                String getAlamat = etAlamat.getText().toString();
                String getHandphone = etHP.getText().toString();
                String getTanggalAkhir = etTA.getText().toString();
                String getTanggalMulai = etTM.getText().toString();

                getReference = database.getReference();

                //Mengecek apakah ada data yang kosong
                if (isEmpty(getNama) || isEmpty(getAlamat) || isEmpty(getHandphone) || isEmpty(getTanggalAkhir) || isEmpty(getTanggalMulai)) {
                    //Jika ada, maka menampilkan pesan singkat seperti berikut
                    Toast.makeText(InsertActivity.this, "Data tidak boleh ada yang " +
                            "kosong", Toast.LENGTH_SHORT).show();
                }else {
                    /*
                    Jika tidak maka data dapat diproses dan menyimpannya pada database
                    menyimpan data referensi pada database berdasarkan user id dari masing-masing akan
                     */
                    getReference.child("user").child(getUserID).child("notes").push()
                            .setValue(new Data(getNama, getAlamat, getTanggalAkhir, getTanggalMulai, getHandphone))
                            .addOnSuccessListener(this, new OnSuccessListener(){
                                @Override
                                public void onSuccess(Object o){
                                    //peristiwa ini saat user berhasil menyimpan data ke dlm db
                                    etNama.setText("");
                                    etHP.setText("");
                                    etAlamat.setText("");
                                    etTA.setText("");
                                    etTM.setText("");
                                    Toast.makeText(InsertActivity.this, "Data tersimpan",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                break;
            case R.id.btLiat:
                startActivity(new Intent(InsertActivity.this, MyListData.class));
                break;
        }
    }
    //tampilan user interface pada activity stelah user terautentikasi
    private void updateUI(){
        btKeluar.setEnabled(true);
        btTambah.setEnabled(true);
        btLiat.setEnabled(true);
        progressBar.setVisibility(View.GONE);

    }

    //Mengecek apakah ada data kosong
    private boolean isEmpty(String s){ return TextUtils.isEmpty(s);}

    //private void submit data
    public void logOut(){
        mAuth.signOut();
        Intent intent = new Intent(InsertActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        //makesure user cant go back
        startActivity(intent);
    }
}
