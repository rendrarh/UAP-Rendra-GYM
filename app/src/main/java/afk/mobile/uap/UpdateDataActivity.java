package afk.mobile.uap;

import static android.widget.Toast.LENGTH_SHORT;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateDataActivity  extends AppCompatActivity {
    private EditText Nama_Baru, TA_Baru, TM_Baru, Alamat_Baru,HP_Baru;
    private Button btUpdate;
    private DatabaseReference database;
    private FirebaseAuth auth;
    private String cekNama, cekTA, cekTM, cekAlamat, cekHP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_data);
        Nama_Baru = findViewById(R.id.et_Nama_Baru);
        Alamat_Baru = findViewById(R.id.et_Alamat_Baru);
        HP_Baru = findViewById(R.id.et_HP_Baru);
        TM_Baru = findViewById(R.id.et_TM_Baru);
        TA_Baru = findViewById(R.id.et_TA_Baru);
        btUpdate = findViewById(R.id.btUpdate);

        //mendapatkan instance autentikasi dan referensi dr db
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
        getData();
        btUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mendapatkan data notes yang akan dicek
                cekNama = Nama_Baru.getText().toString();
                cekAlamat = Alamat_Baru.getText().toString();
                cekHP = HP_Baru.getText().toString();
                cekTM = TM_Baru.getText().toString();
                cekTA = TA_Baru.getText().toString();


                //mengecek agar tidak ada data yang kosong saat proses update
                if (isEmpty(cekNama) || isEmpty(cekAlamat) || isEmpty(cekHP) || isEmpty(cekTM) || isEmpty(cekTA)) {
                    Toast.makeText(UpdateDataActivity.this, "Data tidak boleh ada yang " +
                            "kosong", LENGTH_SHORT).show();
                } else {
                    //menjalankan proses update data
                    Data setNote = new Data();
                    setNote.setNama(Nama_Baru.getText().toString());
                    setNote.setAlamat(Alamat_Baru.getText().toString());
                    setNote.setHandphone(HP_Baru.getText().toString());
                    setNote.setTanggalMulai(TM_Baru.getText().toString());
                    setNote.setTanggalAkhir(TA_Baru.getText().toString());
                    updateNote(setNote);
                }
            }
        });
    }

    //mengecek apakah ada data yang kosong
    private boolean isEmpty(String s) {
        return TextUtils.isEmpty(s);
    }

    //menampilkan data yang akan diupdate
    private void getData() {
        final String getNama = getIntent().getExtras().getString("dataNama");
        final String getAlamat = getIntent().getExtras().getString("dataAlamat");
        final String getHandphone = getIntent().getExtras().getString("dataHandphone");
        final String getTanggalMulai = getIntent().getExtras().getString("dataTanggalMulai");
        final String getTanggalAkhir = getIntent().getExtras().getString("dataTanggalAkhir");

        Nama_Baru.setText(getNama);
        Alamat_Baru.setText(getAlamat);
        HP_Baru.setText(getHandphone);
        TM_Baru.setText(getTanggalMulai);
        TA_Baru.setText(getTanggalAkhir);

    }

    //proses update data yang sudah ditentukan
    private void updateNote(Data data) {
        String userID = auth.getUid();
        String getKey = getIntent().getExtras().getString("getPrimaryKey");
        database.child("user")
                .child(userID)
                .child("notes")
                .child(getKey)
                .setValue(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Nama_Baru.setText("");
                        Alamat_Baru.setText("");
                        HP_Baru.setText("");
                        TM_Baru.setText("");
                        TA_Baru.setText("");
                        Toast.makeText(UpdateDataActivity.this, "Data berhasil diubah",
                                LENGTH_SHORT).show();
                        finish();
                    }
                });
    }
}