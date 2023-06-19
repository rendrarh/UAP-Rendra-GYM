package afk.mobile.uap;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private ArrayList<Data> listNote;
    private Context context;

    //membuat interface
    public interface dataListener{
        void onDeleteData(Data data, int position);
    }
    //deklarasi objek dari interface
    dataListener listener;

    //membuat konstruktor untuk menerima input dari db
    public RecyclerViewAdapter(ArrayList<Data> listNote, Context context){
        this.listNote = listNote;
        this.context = context;
        listener = (MyListData) context;
    }

    //viewholder digunakan untuk menyimpan referensi dari view2
    class ViewHolder extends RecyclerView.ViewHolder{
        private TextView Nama, Handphone, TanggalAwal, TanggalAkhir, Alamat;
        private CardView ListItem;

        ViewHolder(View itemView) {
            super(itemView);
            //menginisialisasi view2 yang terpasang pada layout recyler view kita
            Nama = itemView.findViewById(R.id.tvListNama);
            Handphone = itemView.findViewById(R.id.tvListHP);
            TanggalAkhir = itemView.findViewById(R.id.tvListTA);
            TanggalAwal = itemView.findViewById(R.id.tvListTM);
            Alamat = itemView.findViewById(R.id.tvListAlamat);

            ListItem = itemView.findViewById(R.id.cv_item_data);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        //membuat view untuk menyiapkan dan memasang layout yang akan digunakan pada recylerview
        View V = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_anggota, parent, false);
        return new ViewHolder(V);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") int position){
        //mengambil nilai/value yang terdapat pda recylerview berdasarkan posisi tertentu
        final String Nama = listNote.get(position).getNama();
        final String HP = listNote.get(position).getHandphone();
        final String TM = listNote.get(position).getTanggalMulai();
        final String TA = listNote.get(position).getTanggalAkhir();
        final String Alamat = listNote.get(position).getAlamat();

        //memasukkan nilai/value kedalam view
        holder.Nama.setText(Nama);
        holder.Handphone.setText(HP);
        holder.Alamat.setText(Alamat);
        holder.TanggalAwal.setText(TM);
        holder.TanggalAkhir.setText(TA);


        //menampilkan menu update dan delete saat user melakukan long klik pada salah satu item
        holder.ListItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                final String[] action = {"Update", "Delete"};
                AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
                alert.setItems(action, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (i){
                            case 0:
                                /*
                                Berpindah activity pada halaman layout updateData dan mengambil data pada listNotes,
                                berdasarkan posisinya untuk dikirim pada activity updateData
                                 */
                                Bundle bundle = new Bundle();
                                bundle.putString("dataNama", listNote.get(position).getNama());
                                bundle.putString("dataHandphone", listNote.get(position).getHandphone());
                                bundle.putString("dataAlamat", listNote.get(position).getAlamat());
                                bundle.putString("dataTanggalMulai", listNote.get(position).getTanggalMulai());
                                bundle.putString("dataTanggalAkhir", listNote.get(position).getTanggalAkhir());
                                bundle.putString("getPrimaryKey", listNote.get(position).getKey());
                                Intent intent = new Intent(view.getContext(), UpdateDataActivity.class);

                                intent.putExtras(bundle);
                                context.startActivity(intent);
                                break;
                            case 1:
                                //menggunakan interface untuk mengirim data notes yang akan dihapus
                                listener.onDeleteData(listNote.get(position), position);
                                break;
                        }
                    }
                });
                alert.create();
                alert.show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount(){
        //menghitung ukuran/jumlah data yang akan ditampilkan pada recylerview
        return listNote.size();
    }
}