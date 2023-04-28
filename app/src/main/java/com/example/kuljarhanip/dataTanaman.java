package com.example.kuljarhanip;

public class dataTanaman {
    private String namaTanaman;
    private String namaLatin;
    private String habitat;
    private String sebaran;
    private String deskripsi;
    private String sumber;

    public dataTanaman(){

    }

    public dataTanaman(String namaTanaman, String namaLatin,String habitat,String sebaran, String deskripsi, String sumber){
        this.setNamaTanaman(namaTanaman);
        this.setDeskripsi(deskripsi);
        this.setNamaLatin(namaLatin);
        this.setHabitat(habitat);
        this.setSebaran(sebaran);
        this.setSumber(sumber);

    }


    public String getNamaTanaman() {
        return namaTanaman;
    }

    public void setNamaTanaman(String namaTanaman) {
        this.namaTanaman = namaTanaman;
    }

    public String getNamaLatin() {
        return namaLatin;
    }

    public void setNamaLatin(String namaLatin) {
        this.namaLatin = namaLatin;
    }

    public String getHabitat() {
        return habitat;
    }

    public void setHabitat(String habitat) {
        this.habitat = habitat;
    }

    public String getSebaran() {
        return sebaran;
    }

    public void setSebaran(String sebaran) {
        this.sebaran = sebaran;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getSumber() {
        return sumber;
    }

    public void setSumber(String sumber) {
        this.sumber = sumber;
    }
}
