package com.example.kuljarhanip;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class dataEksplan {
    private String namaEksplan;
    private String jenisTanaman;
    private String namaAdmin;
    private String keterangan;
    private String namaPelanggan;
    private String noTelpPelanggan;
    private String Status;
    private String tanggalTerima;
    private String tanggalTanam;
    private String tempatPenyimpanan;
    private String mediaPenyimpanan;
    private String idEksplanUntukAnakan;
    private String linkQR;
    //private String anakan;

    public dataEksplan(){

    }

    public dataEksplan(String namaEksplan, String jenisTanaman,String tanggalTanam,String mediaPenyimpanan, String namaAdmin, String keterangan, String idEksplanUntukAnakan, String namaPelanggan, String noTelpPelanggan, String status, String tanggalTerima, String tempatPenyimpanan, String linkQR){

        this.setNamaEksplan(namaEksplan);
        this.setJenisTanaman(jenisTanaman);
        this.setNamaAdmin(namaAdmin);
        this.setKeterangan(keterangan);
        this.setNamaPelanggan(namaPelanggan);
        this.setNoTelpPelanggan(noTelpPelanggan);
        this.setStatus(status);
        this.setTanggalTerima(tanggalTerima);
        this.setTempatPenyimpanan(tempatPenyimpanan);
        this.setLinkQR(linkQR);
        this.setIdEksplanUntukAnakan(idEksplanUntukAnakan);
        this.setTanggalTanam(tanggalTanam);
        this.setMediaPenyimpanan(mediaPenyimpanan);
    }

    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("namaEksplan", getNamaEksplan());
        result.put("jenisTanaman", getJenisTanaman());
        result.put("keterangan", getKeterangan());
        result.put("namaPelanggan", getNamaPelanggan());
        result.put("Status", getStatus());
        result.put("tanggalTerima", getTanggalTerima());
        result.put("tempatPenyimpanan", getTempatPenyimpanan());
        //result.put("anakan", getAnakan());
        return result;
    }

    public String getNamaEksplan() {
        return namaEksplan;
    }

    public void setNamaEksplan(String namaEksplan) {
        this.namaEksplan = namaEksplan;
    }

    public String getJenisTanaman() {
        return jenisTanaman;
    }

    public void setJenisTanaman(String jenisTanaman) {
        this.jenisTanaman = jenisTanaman;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public String getNamaPelanggan() {
        return namaPelanggan;
    }

    public void setNamaPelanggan(String namaPelanggan) {
        this.namaPelanggan = namaPelanggan;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        this.Status = status;
    }

    public String getTanggalTerima() {
        return tanggalTerima;
    }

    public void setTanggalTerima(String tanggalTerima) {
        this.tanggalTerima = tanggalTerima;
    }

    public String getTempatPenyimpanan() {
        return tempatPenyimpanan;
    }

    public void setTempatPenyimpanan(String tempatPenyimpanan) {
        this.tempatPenyimpanan = tempatPenyimpanan;
    }

    public String getNoTelpPelanggan() {
        return noTelpPelanggan;
    }

    public void setNoTelpPelanggan(String noTelpPelanggan) {
        this.noTelpPelanggan = noTelpPelanggan;
    }

    public String getLinkQR() {
        return linkQR;
    }

    public void setLinkQR(String linkQR) {
        this.linkQR = linkQR;
    }

    public String getNamaAdmin() {
        return namaAdmin;
    }

    public void setNamaAdmin(String namaAdmin) {
        this.namaAdmin = namaAdmin;
    }

    public String getIdEksplanUntukAnakan() {
        return idEksplanUntukAnakan;
    }

    public void setIdEksplanUntukAnakan(String idEksplanUntukAnakan) {
        this.idEksplanUntukAnakan = idEksplanUntukAnakan;
    }

    public String getTanggalTanam() {
        return tanggalTanam;
    }

    public void setTanggalTanam(String tanggalTanam) {
        this.tanggalTanam = tanggalTanam;
    }

    public String getMediaPenyimpanan() {
        return mediaPenyimpanan;
    }

    public void setMediaPenyimpanan(String mediaPenyimpanan) {
        this.mediaPenyimpanan = mediaPenyimpanan;
    }

    /*public String getAnakan() {
        return anakan;
    }

    public void setAnakan(String anakan) {
        this.anakan = anakan;
    }*/
}