package kordoghli.firas.petcare.Data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Program {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("id_pet")
    @Expose
    private Integer idPet;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getIdPet() {
        return idPet;
    }

    public void setIdPet(Integer idPet) {
        this.idPet = idPet;
    }

    @Override
    public String toString() {
        return "Program{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", date='" + date + '\'' +
                ", idPet=" + idPet +
                '}';
    }
}
