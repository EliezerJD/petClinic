package app.petclinic;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class AddActivity extends AppCompatActivity {
    final static String[] newMascotas = {};
    ArrayList<String> listaMascotas;
    Spinner comboMascota;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        comboMascota = findViewById(R.id.comboMascota);
        Retrofit retrofit = Connection.getClient();
        DataService dataService = retrofit.create(DataService.class);
        Call<Data> call = dataService.getMascotaByIdOwner(4);
        call.enqueue(new Callback<Data>() {
            @Override
            public void onResponse(Call<Data> call, Response<Data> response) {
                listaMascotas = new ArrayList<>();
                for(int x=0;x<response.body().getMascotas().size();x++){
                    listaMascotas.add(response.body().getMascotas().get(x).getName());
                }
                listar();
            }
            @Override
            public void onFailure(Call<Data> call, Throwable t) {
                System.out.println(t.getMessage());
            }
        });


    }

    private void listar(){
        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.spinneritem, R.id.txt, listaMascotas);
        comboMascota.setAdapter(adapter);
    }



}
