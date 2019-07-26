package app.petclinic;

import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;

public class Data {

    @SerializedName("email")
    @Expose
    private String email;

    @SerializedName("password")
    @Expose
    private String password;

    @SerializedName("response")
    @Expose
    private String response;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("citas")
    @Expose
    ArrayList<Data> citas = new ArrayList<>();

    @SerializedName("especialidades")
    @Expose
    ArrayList<Data> especialidades = new ArrayList<>();

    @SerializedName("mascotas")
    @Expose
    ArrayList<Data> mascotas = new ArrayList<>();

    @SerializedName("fecha")
    @Expose
    private String fecha;

    @SerializedName("hora")
    @Expose
    private String hora;

    @SerializedName("mascota")
    @Expose
    private String mascota;

    @SerializedName("especialidad")
    @Expose
    private String especialidad;

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("deleted")
    @Expose
    private String deleted;

    @SerializedName("owner")
    @Expose
    private String owner;

    @SerializedName("userId")
    @Expose
    private String userId;

    @SerializedName("owner_id")
    @Expose
    private String owner_id;

    @SerializedName("type_id")
    @Expose
    private String type_id;

    @SerializedName("confirmacion")
    @Expose
    private String confirmacion;


    public Data() {
        this.email = email;
        this.password = password;
        this.response = response;
        this.name = name;
        this.fecha = fecha;
        this.hora = hora;

        this.especialidad = especialidad;

    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public ArrayList<Data> getCitas() {
        return citas;
    }

    public void setCitas(ArrayList<Data> citas) {
        this.citas = citas;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getMascota() {
        return mascota;
    }

    public void setMascota(String mascota) {
        this.mascota = mascota;
    }

    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }


    public String getDeleted() {
        return deleted;
    }

    public void setDeleted(String deleted) {
        this.deleted = deleted;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public ArrayList<Data> getEspecialidades() {
        return especialidades;
    }

    public void setEspecialidades(ArrayList<Data> especialidades) {
        this.especialidades = especialidades;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Data> getMascotas() {
        return mascotas;
    }

    public void setMascotas(ArrayList<Data> mascotas) {
        this.mascotas = mascotas;
    }

    public String getOwner_id() {
        return owner_id;
    }

    public void setOwner_id(String owner_id) {
        this.owner_id = owner_id;
    }

    public String getConfirmacion() {
        return confirmacion;
    }

    public void setConfirmacion(String confirmacion) {
        this.confirmacion = confirmacion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }
}
