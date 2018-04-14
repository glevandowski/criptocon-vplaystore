package levandowski.primao.criptocon;

import android.annotation.SuppressLint;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import java.util.List;
import levandowski.primao.criptocon.adapter.CoinAdapter;
import levandowski.primao.criptocon.deserializer.CoinDeserializer;
import levandowski.primao.criptocon.fragments.FirstFragment;
import levandowski.primao.criptocon.fragments.FourFragment;
import levandowski.primao.criptocon.fragments.SecondFragment;
import levandowski.primao.criptocon.fragments.ThirdFragment;
import levandowski.primao.criptocon.model.Coin;
import levandowski.primao.criptocon.service.APIRetrofitService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import static android.widget.Toast.LENGTH_LONG;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "comando:" ;
    public static CoinAdapter adapter;
    public static ArrayList<Coin> coinArrayList;
    public static ArrayAdapter<String> adapterSpinner;
    public static ArrayList<String> coinSpinner;
    public static SwipeRefreshLayout swipeRefreshLayout;
    public static Spinner spinnerCoin;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, new FirstFragment()).commit();

        Log.d(TAG, "Setando Layout");
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        Log.d(TAG, "Puxou as referencias");
        Log.d(TAG, "Abrindo ActionBar");
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //executando metodo responsavel pelos arrays e download do json
          executeCoin();
    }//oncreate

    @Override
    public void onBackPressed(){
        Log.d(TAG,"Pressionando");
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG,"Criou Menu");
        getMenuInflater().inflate(R.menu.main, menu);
        //inflando o xml da action setting
        // getMenuInflater().inflate(R.menu.refresh_activity, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.d(TAG,"Selecionando Item");
        int id = item.getItemId();
        /*switch(item.getItemId()) {
            case R.id.action_refresh:
                Toast.makeText(getApplicationContext(),"Atualizando Valores",LENGTH_LONG).show();
                refresh();
                return(true);
        } faz a relacao do clique da action settings*/
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        Log.d(TAG,"ultimas opcoes");
        int id = item.getItemId();
        FragmentManager fragmentManager = getFragmentManager();
        if (id == R.id.nav_cotacao) {
            fragmentManager.beginTransaction().replace(R.id.content_frame, new FirstFragment())
                    .commit();
            Vibrar();
        } else if (id == R.id.nav_carteira) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame
                            , new SecondFragment())
                    .commit();
            Vibrar();
        }else if (id == R.id.nav_conversor) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame
                            , new ThirdFragment())
                    .commit();
            Vibrar();

        } else if (id == R.id.nav_sugestoes) {
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame
                            , new FourFragment())
                    .commit();
            Vibrar();
        } else if (id == R.id.nav_compartilhar) {
            share();
            Vibrar();
        } else if (id == R.id.nav_sobre) {
            Vibrar();
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void Vibrar() {
        Vibrator rr = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        long milliseconds = 50;
        rr.vibrate(milliseconds);
    }


    void share(){
        Intent intent = new Intent( Intent.ACTION_SEND );
        intent.setType( "text/plain" );
        intent.putExtra( Intent.EXTRA_TEXT, "*Curta a Página e acompanhe nosso Trabalho* CriptoCon - https://www.facebook.com/CriptoCon39/" );
        startActivity( intent );
    }


    //baixando json
    public void getCoin(){
        final Gson g = new GsonBuilder().registerTypeAdapter(Coin.class, new CoinDeserializer()).create();

        Retrofit retro = new Retrofit.Builder().baseUrl(APIRetrofitService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(g)).build();

        final APIRetrofitService service = retro.create(APIRetrofitService.class);

        final Call<List<Coin>> callCoin = service.getCoin(0);

        callCoin.enqueue(new Callback<List<Coin>>() {
            @SuppressLint("NewApi")
            @Override
            public void onResponse(Call<List<Coin>> call, Response<List<Coin>> response) {
                showProgess();
                if(!response.isSuccessful()){
                    Toast.makeText(
                            getBaseContext(),
                            "Moeda Não registrada",
                            Toast.LENGTH_SHORT).show();
                } else {
                    hideProgress();
                    List<Coin> coinAux = response.body();
                    for (int i = 0; i < coinAux.size(); i++) {
                        Coin c = coinAux.get(i);
                        //Setando no ArrayList
                        coinArrayList.add(c);
                    }
                }
                /* Setando os coins no ArrayList global da aplicação */
                coinArrayList = coinArrayList;
                adapter.setCoin(coinArrayList);
                adapter.notifyDataSetChanged();

                if(coinSpinner.isEmpty()||!coinArrayList.isEmpty()){
                    for(int i = 0; i < coinArrayList.size() ; i++){
                        coinSpinner.add(coinArrayList.get(i).getName());
                    }
                } else{
                    Toast.makeText(getBaseContext(),"Conexão Lenta", LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<List<Coin>> call, Throwable t) {
            }
        });
    }
    //metodo principal para executar no oncreate e no refresh do firstfragment
    public void executeCoin(){
        //showProgess();
        coinSpinner = new ArrayList<String>();
        coinArrayList = new  ArrayList<Coin>();
        coinSpinner.add("Todas as Moedas");
        getCoin();
    }

    //METODOS QUE IRÃO PARA OS UTILS EM BREVE
    //.......................................
    //metodos responsáveis pela progress bar
    public void showProgess(){
        ProgressBar progress = findViewById(R.id.progressReciclerView);
        progress.setVisibility(View.VISIBLE);
    }
    public void hideProgress(){
        ProgressBar progress = findViewById(R.id.progressReciclerView);
        progress.setVisibility(View.GONE);
    }
    //comando responsavel por atualizar os cards
    public void refresh(View v){
        swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipelayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.setRefreshing(true);
                final ArrayList<Coin> coinRefresh = new ArrayList<>();
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.setRefreshing(false);
                        adapterSpinner.clear();
                        adapterSpinner = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item,coinSpinner);
                        spinnerCoin.setAdapter(adapterSpinner);
                        adapterSpinner.notifyDataSetChanged();
                    }},5000);
                executeCoin();
            }
        });
    }
}