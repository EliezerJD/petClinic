package app.petclinic;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class PanelActivity extends AppCompatActivity {

    ImageButton imgBtnUser;
    TextView userName;
    Data obj;
    Data especialidad;
    Data tipo;
    Data allpets;
    Dialog dialog;
    String id;
    String sessionId;
    String usernameT;
    ArrayList<String> listaView = null;
    ArrayList<Data> citas = null;
    ArrayList<Data> especialidades = null;
    ArrayList<Data> mascotas  = null;
    ArrayList<Data> types = null;

    //falta lista del mas nuevo al mas viejo


    ListView simpleList;
    ArrayAdapter<String> arrayAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panel);
        init();
    }

    public void init(){
        imgBtnUser = findViewById(R.id.imgBtnUser);
        simpleList = findViewById(R.id.listView);
        userName = findViewById(R.id.userName);
        sessionId = getIntent().getStringExtra("id");
        usernameT = getIntent().getStringExtra("name");
        listaView = new ArrayList<>();
        citas = new ArrayList<>();
        especialidades = new ArrayList<>();
        mascotas = new ArrayList<>();
        types = new ArrayList<>();
        userName.setText(usernameT);

        listarInicio();
    }

    public void listarInicio(){
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(userName.getWindowToken(), 0);
        Retrofit retrofit = Connection.getClient();
        DataService dataService = retrofit.create(DataService.class);
        Call<Data> call = dataService.getCitas(sessionId);
        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                for(int x=0; x<response.body().getCitas().size(); x++){
                    obj = new Data();
                    obj.setId(response.body().getCitas().get(x).getId());
                    obj.setFecha(response.body().getCitas().get(x).getFecha());
                    obj.setHora(response.body().getCitas().get(x).getHora());
                    obj.setMascota(response.body().getCitas().get(x).getMascota());
                    obj.setEspecialidad(response.body().getCitas().get(x).getEspecialidad());
                    citas.add(obj);
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

    public void listar(){
        arrayAdapter= new ArrayAdapter<String>(PanelActivity.this, R.layout.activity_listview, R.id.textView, listaView);
        simpleList.setAdapter(arrayAdapter);
        getEspecialidades();
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
                getMascotas();
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {

            }
        });
    }

    public void getMascotas(){
        Retrofit retrofit = Connection.getClient();
        DataService dataService = retrofit.create(DataService.class);
        Call<Data> call = dataService.getMascotaByIdOwner(Integer.parseInt(sessionId));
        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                for(int x=0; x<response.body().getMascotas().size();x++){
                    allpets = new Data();
                    allpets.setId(response.body().getMascotas().get(x).getId());
                    allpets.setName(response.body().getMascotas().get(x).getName());
                    allpets.setType_id(response.body().getMascotas().get(x).getType_id());
                    mascotas.add(allpets);
                }
                getTypes();
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {

            }
        });
    }

    private void getTypes(){
        Retrofit retrofit = Connection.getClient();
        DataService dataService = retrofit.create(DataService.class);
        Call<Data> call = dataService.setType();
        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                for(int x=0; x<response.body().getMascotas().size(); x++){
                    tipo = new Data();
                    tipo.setId(response.body().getMascotas().get(x).getId());
                    tipo.setName(response.body().getMascotas().get(x).getName());
                    types.add(tipo);
                }
                setAll();
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });
    }

    private void setAll(){
        //especialidad
        for(int x=0; x<citas.size();x++) {
            for(int y=0; y<especialidades.size();y++){
                if(citas.get(x).getEspecialidad().equals(especialidades.get(y).getId())){
                    citas.get(x).setEspecialidad(especialidades.get(y).getName());
                    break;
                }
            }
        }

        //tipo
        int tam=0;
        for(int x=0; x<citas.size();x++) {
            for(int y=0; y<mascotas.size();y++){
                if(citas.get(x).getMascota().equals(mascotas.get(y).getId())){
                    for(int a=y; a<mascotas.size(); a++){
                        for(int b=0; b<types.size(); b++){
                            if(mascotas.get(a).getType_id().equals(types.get(b).getId()) &&tam<=x){
                                citas.get(x).setType_id(types.get(b).getName());
                                tam++;
                                break;
                            }
                        }
                    }
                    break;
                }
            }
        }

        //nombres
        for(int x=0; x<citas.size();x++) {
            for(int y=0; y<mascotas.size();y++){
                if(citas.get(x).getMascota().equals(mascotas.get(y).getId())){
                    citas.get(x).setMascota(mascotas.get(y).getName());
                    break;
                }
            }
        }
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
        TextView txtTipo = dialog.findViewById(R.id.txtTipo);
        fecha.setText(citas.get(pos).getFecha());
        hora.setText(citas.get(pos).getHora());
        mascota.setText(citas.get(pos).getMascota());
        espe.setText(citas.get(pos).getEspecialidad());
        txtTipo.setText(citas.get(pos).getType_id());
    }

    public void eliminar(View view) {
        View item = (View) view.getParent();
        final int pos = simpleList.getPositionForView(item);
        Retrofit retrofit = Connection.getClient();
        DataService dataService = retrofit.create(DataService.class);
        final Integer cita_id = Integer.parseInt(citas.get(pos).getId());
        Call<Data> call = dataService.delete(cita_id);
        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                if(response.body().getDeleted().equals(String.valueOf(cita_id))){
                    Toast toast1 = Toast.makeText(getApplicationContext(), "Eliminado correctamente", Toast.LENGTH_SHORT);
                    toast1.show();
                    citas.remove(pos);
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

    public void agregar(View view) {
        Intent screen = new Intent(PanelActivity.this, AddActivity.class);
        screen.putExtra("id", sessionId);
        screen.putExtra("name", usernameT);
        startActivity(screen);
    }


    public void dismiss(View view) {
        dialog.dismiss();
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
}


