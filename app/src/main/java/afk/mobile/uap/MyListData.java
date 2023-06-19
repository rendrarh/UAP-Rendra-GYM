package afk.mobile.uap;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;

public class MyListData extends AppCompatActivity implements RecyclerViewAdapter.dataListener {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private DatabaseReference reference;
    private ArrayList<Data> dataNote;

    private FirebaseAuth auth;

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);
        recyclerView = findViewById(R.id.recycler_data);
        auth = FirebaseAuth.getInstance();
        MyRecyclerView();
        GetData();
    }
    //berisi baris kode untuk mengambil data dari database dan menampilkan ke dalam adapter
    private void GetData(){
        //mendapatkan referensi dr db
        reference= FirebaseDatabase.getInstance().getReference();
        reference.child("user").child(auth.getUid()).child("notes")
                .addValueEventListener(new ValueEventListener(){
                    @Override
                    public void onDataChange(DataSnapshot dataSnapShot){
                        //inisialisasi Arraylist
                        dataNote = new ArrayList<>();

                        for (DataSnapshot snapshot : dataSnapShot.getChildren()){
                            //Mapping data pada DataSnapshot ke dalam object notes
                            Data data = snapshot.getValue(Data.class);

                            //mengambil primary key, digunakan untuk proses update dan delete
                            data.setKey(snapshot.getKey());
                            dataNote.add(data);
                        }
                        //Inisialisasi adapter dan data notes dalam bentuk array
                        adapter = new RecyclerViewAdapter(dataNote, MyListData.this);

                        //memasang adapter pada recyclerview
                        recyclerView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError){
                    /*
                    kode ini akan dijalankan ketika ada error dan pengambilan data error tersebut
                    lalu mencetak eror nya ke dalam LogCat
                     */
                        Toast.makeText(getApplicationContext(), "Data gagal dimuat", Toast.LENGTH_LONG)
                                .show();
                        Log.e("MyListActivity", databaseError.getDetails()+" "+databaseError.getMessage());
                    }
                });
    }

    //metode yang berisi kumpulan baris kode untuk mengatur recyclerview
    private void MyRecyclerView(){
        //menggunakan layout manager, dan membuat list secara vertical
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        //membuat underline pda setiap item didalam list
        DividerItemDecoration itemDecoration = new DividerItemDecoration(getApplicationContext(),
                DividerItemDecoration.VERTICAL);
        itemDecoration.setDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.line));
        recyclerView.addItemDecoration(itemDecoration);
    }

    @Override
    public void onDeleteData(Data data, int position){
        /*
        kode ini akan dipanggil ketika method onDeleteData
        dipanggil dr adapter pada recylerview melalui interface
        kemudian akan menghapus data berdasarkan primary key dari data tersebut
        jika berhasil maka akan memunculkan toast
         */
        String userID = auth.getUid();
        if (reference != null){
            reference.child("user")
                    .child(userID)
                    .child("notes")
                    .child(data.getKey())
                    .removeValue()
                    .addOnSuccessListener(new OnSuccessListener<Void>(){
                        @Override
                        public void onSuccess(Void aVoid){
                            Toast.makeText(MyListData.this, "Data berhasil dihapus",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}