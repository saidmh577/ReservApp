package com.app.reservapp;

public class Voyage {
    public int id;
    public String depart;
    public String arrive;
    public String time;
    public String prix;

    public Voyage(int id, String depart, String arrive, String time, String prix) {
        this.id = id;
        this.depart = depart;
        this.arrive = arrive;
        this.time = time;
        this.prix = prix;
    }

    @Override
    public String toString() {
        return "Voyage{" +
                "id=" + id +
                ", depart='" + depart + '\'' +
                ", arrive='" + arrive + '\'' +
                ", time='" + time + '\'' +
                ", prix='" + prix + '\'' +
                '}';
    }
}
