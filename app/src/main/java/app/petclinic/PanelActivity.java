package app.petclinic;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Logger;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PanelActivity extends AppCompatActivity {

    ImageButton imgBtnUser;
    TextView userName;
    Data obj;
    ArrayList<String> listaView;
    ArrayList<Data> resultados;
    Dialog dialog;
    String id;
    Data especialidad;
    Data mascota;
    ArrayList<Data> especialidades = new ArrayList<>();
    ArrayList<Data> mascotas = new ArrayList<>();
    Integer id_masco;
    Integer ind;


    //falta lista del mas nuevo al mas viejo, corregir el paso de id para cargar las vistas

    ListView simpleList;
    ArrayAdapter<String> arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel);
        init();
    }

    public void logOut(View view) {
        PopupMenu popup = new PopupMenu(PanelActivity.this, imgBtnUser);
        popup.getMenuInflater().inflate(R.menu.menu, popup.getMenu());
        popup.show();
    }

    public void outSession(MenuItem item) {
        Intent screen = new Intent(PanelActivity.this, MainActivity.class);
        screen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(screen);
    }

    public void init(){
        imgBtnUser = findViewById(R.id.imgBtnUser);
        simpleList = findViewById(R.id.listView);
        userName = findViewById(R.id.userName);
        Intent screen = getIntent();
        String name = screen.getStringExtra("name");
        id = screen.getStringExtra("id");
        userName.setText(name);
        getEspecialidades();
    }

    public void listar(){
        arrayAdapter= new ArrayAdapter<String>(PanelActivity.this, R.layout.activity_listview, R.id.textView, listaView);
        simpleList.setAdapter(arrayAdapter);
        reEscribir();
        //agregaprueba();
    }

    public void ver(View view) {
        View item = (View) view.getParent();
        int pos = simpleList.getPositionForView(item);
        dialog = new Dialog(PanelActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.modalver);
        dialog.setCanceledOnTouchOutside(true);
        dialog.show();
        TextView fecha = dialog.findViewById(R.id.txtFecha);
        TextView hora = dialog.findViewById(R.id.txtHora);
        TextView mascota = dialog.findViewById(R.id.txtMascota);
        TextView espe = dialog.findViewById(R.id.txtEspecialidad);
        fecha.setText(resultados.get(pos).getFecha().toString());
        hora.setText(resultados.get(pos).getHora());
        mascota.setText(resultados.get(pos).getMascota());
        espe.setText(resultados.get(pos).getEspecialidad());
    }

    public void dismiss(View view) {
        dialog.dismiss();
    }

    public void eliminar(View view) {
        View item = (View) view.getParent();
        final int pos = simpleList.getPositionForView(item);
        Retrofit retrofit = Connection.getClient();
        DataService dataService = retrofit.create(DataService.class);
        final Integer cita_id = Integer.parseInt(resultados.get(pos).getId());
        Call<Data> call = dataService.delete(cita_id);
        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                if(response.body().getDeleted().equals(String.valueOf(cita_id))){
                    Toast toast1 = Toast.makeText(getApplicationContext(), "Eliminado correctamente", Toast.LENGTH_SHORT);
                    toast1.show();
                    resultados.remove(pos);
                    listaView.remove(pos);
                    arrayAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                Toast toast1 = Toast.makeText(getApplicationContext(), "Error al eliminar", Toast.LENGTH_SHORT);
                toast1.show();
            }
        });
    }

    public void listarInicio(){
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(userName.getWindowToken(), 0);

        Retrofit retrofit = Connection.getClient();
        DataService dataService = retrofit.create(DataService.class);
        Call<Data> call = dataService.getCitas("4");
        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                resultados = new ArrayList<>();
                listaView = new ArrayList<>();
                for(int x=0; x<response.body().getCitas().size(); x++){
                    obj = new Data();
                    obj.setId(response.body().getCitas().get(x).getId());
                    obj.setFecha(response.body().getCitas().get(x).getFecha());
                    obj.setHora(response.body().getCitas().get(x).getHora());
                    obj.setMascota(response.body().getCitas().get(x).getMascota());
                    obj.setEspecialidad(response.body().getCitas().get(x).getEspecialidad());
                    resultados.add(obj);
                    listaView.add("Fecha: " + response.body().getCitas().get(x).getFecha()+ "\nHora: " + response.body().getCitas().get(x).getHora() + "\nMascota: " + response.body().getCitas().get(x).getMascota());
                }
                listar();

            }
            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                Toast toast1 = Toast.makeText(getApplicationContext(), "Error al listar", Toast.LENGTH_SHORT);
                toast1.show();
            }
        });
    }

    public void getEspecialidades(){
        Retrofit retrofit = Connection.getClient();
        DataService dataService = retrofit.create(DataService.class);
        Call<Data> call = dataService.getEspecialidades();
        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                for(int x=0; x<response.body().getEspecialidades().size(); x++){
                    especialidad = new Data();
                    especialidad.setId(response.body().getEspecialidades().get(x).getId());
                    especialidad.setName(response.body().getEspecialidades().get(x).getName());
                    especialidades.add(especialidad);
                }
                listarInicio();
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {

            }
        });
    }

    public void getMascotas(int id_masc){
        Retrofit retrofit = Connection.getClient();
        DataService dataService = retrofit.create(DataService.class);
        Call<Data> call = dataService.getMascotaById(id_masc);
        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                    mascota = new Data();
                    mascota.setName(response.body().getMascotas().get(0).getName());
                    mascotas.add(mascota);
                    reWriteMas();
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {

            }
        });
    }

    public void reEscribir(){
        ind = 0;
        for(int x=0; x<resultados.size(); x++){
            id_masco = Integer.parseInt(resultados.get(x).getMascota());
            getMascotas(id_masco);

            for(int y = 0; y<especialidades.size(); y++){
                if(resultados.get(x).getEspecialidad().equals(especialidades.get(y).getId())){
                    resultados.get(x).setEspecialidad(especialidades.get(y).getName());
                    break;
                }
            }
        }
    }

    public void reWriteMas(){
        resultados.get(ind).setMascota(mascotas.get(ind).getName());
        ind++;
    }

    public void agregaprueba(){
        Data prueba = new Data();
        prueba.setOwner_id("4");
        prueba.setFecha("2019-01-01");
        prueba.setHora("11:00:00");
        prueba.setMascota("13");
        prueba.setEspecialidad("1");
        prueba.setConfirmacion("0");

        Retrofit retrofit = Connection.getClient();
        DataService dataService = retrofit.create(DataService.class);
        Call<Data> call = dataService.addCita(prueba);
        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                System.out.println(response.message());
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {

            }
        });

    }


}


