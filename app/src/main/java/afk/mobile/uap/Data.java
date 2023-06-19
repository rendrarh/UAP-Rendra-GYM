package afk.mobile.uap;

public class Data {
    private String nama;
    private String tanggalmulai;
    private String tanggalakhir;
    private String handphone;
    private String alamat;
    private String key;
    public Data(){
    }
    public Data(String nama, String alamat, String tanggalakhir, String tanggalmulai, String handphone){
        this.nama=nama;
        this.tanggalakhir=tanggalakhir;
        this.tanggalmulai=tanggalmulai;
        this.alamat=alamat;
        this.handphone=handphone;
    }
    public String getNama(){ return nama; }
    public void setNama(String nama) {this.nama = nama; }

    public String getAlamat(){ return alamat; }
    public void setAlamat(String alamat) {this.alamat = alamat; }
    public String getTanggalAkhir(){ return tanggalakhir; }
    public void setTanggalAkhir(String tanggalakhir) {this.tanggalakhir = tanggalakhir; }

    public String getHandphone(){ return handphone; }
    public void setHandphone(String handphone) {this.handphone = handphone; }

    public String getTanggalMulai(){ return tanggalmulai; }
    public void setTanggalMulai(String tanggalmulai) {this.tanggalmulai = tanggalmulai; }



    public String getKey(){ return key; }
    public void setKey(String key) {this.key = key; }
}
