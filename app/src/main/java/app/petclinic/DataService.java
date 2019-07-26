package app.petclinic;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface DataService {

    @POST("owners/login")
    Call<Data> login(@Body Data data);

    @GET("citas/{ownerID}/owners")
    Call<Data> getCitas(@Path("ownerID") String ownerID);

    @GET("owner/{ownerID}")
    Call<Data> getOwnerId(@Path("ownerID") String ownerID);

    @DELETE("citas/{cita}")
    Call<Data> delete(@Path("cita") int cita_id);

    @GET("especialidades")
    Call<Data> getEspecialidades();

    @GET("mascotas/{owner_id}")
    Call<Data> getMascotaByIdOwner(@Path("owner_id") int owner_id);

    @GET("mascotas/byid/{id}")
    Call<Data> getMascotaById(@Path("id") int id);

    @POST("citas/new")
    Call<Data> addCita(@Body Data data);

    @GET("types")
    Call<Data> setType();







}
