package app.petclinic;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AddActivity extends AppCompatActivity {
    final static String[] newMascotas = {};
    ArrayList<String> listaMascotas;
    ArrayList<String> listaMascotasId;
    ArrayList<String> listaEspecialidades;
    ArrayList<String> listaEspecialidadesId;
    Spinner comboMascota;
    Spinner comboEspecialidades;
    Button btnFecha;
    Button btnHora;
    Calendar calendario = Calendar.getInstance();
    String sessionId;
    String usernameT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        comboMascota = findViewById(R.id.comboMascota);
        comboEspecialidades= findViewById(R.id.comboEspecialidades);
        btnFecha=findViewById(R.id.btnFecha);
        btnHora=findViewById(R.id.btnHora);
        sessionId = getIntent().getStringExtra("id");
        usernameT = getIntent().getStringExtra("name");
        getMacostas();


    }

    private void listarMascotas(){
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.spinneritem, R.id.txt, listaMascotas);
        comboMascota.setAdapter(adapter);
        getEspecialidades();
    }

    private void listarEspecialidades(){
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.spinneritem, R.id.txt, listaEspecialidades);
        comboEspecialidades.setAdapter(adapter);
        btnFecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(AddActivity.this, date, calendario
                        .get(Calendar.YEAR), calendario.get(Calendar.MONTH),
                        calendario.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        btnHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = calendario.get(Calendar.HOUR_OF_DAY);
                int minute = calendario.get(Calendar.MINUTE);
                TimePickerDialog hora = new TimePickerDialog(AddActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        btnHora.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);
                hora.show();

            }
        });

    }

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            calendario.set(Calendar.YEAR, year);
            calendario.set(Calendar.MONTH, month);
            calendario.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            actualizarInput();
        }
    };

    private void actualizarInput() {
        String formatoDeFecha = "yyyy/MM/dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(formatoDeFecha, Locale.US);
        btnFecha.setText(sdf.format(calendario.getTime()));
    }

    private void getMacostas(){
        Retrofit retrofit = Connection.getClient();
        DataService dataService = retrofit.create(DataService.class);
        Call<Data> call = dataService.getMascotaByIdOwner(Integer.parseInt(sessionId));
        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                listaMascotas = new ArrayList<>();
                listaMascotasId = new ArrayList<>();
                for(int x=0;x<response.body().getMascotas().size();x++){
                    listaMascotas.add(response.body().getMascotas().get(x).getName());
                    listaMascotasId.add(response.body().getMascotas().get(x).getId());
                }
                listarMascotas();
            }
            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });

    }

    private void getEspecialidades(){
        Retrofit retrofit = Connection.getClient();
        DataService dataService = retrofit.create(DataService.class);
        Call<Data> call = dataService.getEspecialidades();
        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                listaEspecialidades = new ArrayList<>();
                listaEspecialidadesId = new ArrayList<>();
                for(int x=0;x<response.body().getEspecialidades().size();x++){
                    listaEspecialidades.add(response.body().getEspecialidades().get(x).getName());
                    listaEspecialidadesId.add(response.body().getEspecialidades().get(x).getId());
                }
                listarEspecialidades();
            }
            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });

    }


    public void add(View view) {
        Data prueba = new Data();
        prueba.setOwner_id(sessionId);
        prueba.setFecha(btnFecha.getText().toString());
        prueba.setHora(btnHora.getText().toString()+":00");
        prueba.setMascota(buscarPosMacota());
        prueba.setEspecialidad(buscarEspecialidad());
        prueba.setConfirmacion("0");

        Retrofit retrofit = Connection.getClient();
        DataService dataService = retrofit.create(DataService.class);
        Call<Data> call = dataService.addCita(prueba);
        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                if(response.body().getName().equals("agregado")){
                    Toast toast1 = Toast.makeText(getApplicationContext(), "Cita agregada", Toast.LENGTH_SHORT);
                    toast1.show();
                    Intent screen = new Intent(AddActivity.this, PanelActivity.class);
                    screen.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    screen.putExtra("id", sessionId);
                    screen.putExtra("name", usernameT);
                    startActivity(screen);
                }else{
                    Toast toast1 = Toast.makeText(getApplicationContext(), "Error al agregar la cita", Toast.LENGTH_SHORT);
                    toast1.show();
                }
            }

            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                Toast toast1 = Toast.makeText(getApplicationContext(), "Error al agregar la cita", Toast.LENGTH_SHORT);
                toast1.show();
            }
        });
    }

    private String buscarPosMacota(){
        for(int x=0; x<listaMascotas.size();x++){
            if(listaMascotas.get(x).trim().equals(comboMascota.getSelectedItem().toString().trim())){
                return listaMascotasId.get(x).trim();
            }
        }
        return null;
    }

    private String buscarEspecialidad(){
        for(int x=0; x<listaEspecialidades.size();x++){
            if(listaEspecialidades.get(x).trim().equals(comboEspecialidades.getSelectedItem().toString().trim())){
                return listaEspecialidadesId.get(x).trim();
            }
        }
        return null;
    }
}
