package levandowski.primao.criptocon.fragments;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import java.util.ArrayList;
import levandowski.primao.criptocon.MainActivity;
import levandowski.primao.criptocon.R;
import levandowski.primao.criptocon.adapter.CoinAdapter;
import levandowski.primao.criptocon.model.Coin;
import static levandowski.primao.criptocon.MainActivity.*;
import static levandowski.primao.criptocon.MainActivity.adapterSpinner;
import static levandowski.primao.criptocon.MainActivity.coinArrayList;
import static levandowski.primao.criptocon.MainActivity.coinSpinner;
import static levandowski.primao.criptocon.MainActivity.spinnerCoin;


@SuppressLint("ValidFragment")
public class FirstFragment extends Fragment {
    private RecyclerView recyclerViewCoin;
    private SwipeRefreshLayout swipeRefreshLayout;

    public FirstFragment(){}
    public interface AoSelecionarItemEventos {public void aoSelecionarItem(String item);}
    View v;
    @SuppressLint("ResourceType")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.first_fragment, container, false);//iniciando fragment
        //ArrayList
        adapter = new CoinAdapter(getActivity(), new ArrayList<Coin>());
        adapter.notifyDataSetChanged();
        //fecha ArrayList

        //referencias
        recyclerViewCoin = v.findViewById(R.id.first_recyclerViewCoin);
        spinnerCoin = v.findViewById(R.id.first_spinner_coin);
        SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) v.findViewById(R.id.swipelayout);
        adapter.notifyDataSetChanged();
        //fecha referencia

        //ReciclerView
        recyclerViewCoin.setAdapter(adapter);
        recyclerViewCoin.setHasFixedSize(true);
        recyclerViewCoin.setLayoutManager(new LinearLayoutManager(getActivity()));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerViewCoin.setLayoutManager(linearLayoutManager);
        //fecha ReciclerView

        //responsável pelo layout do spinner
        adapterSpinner = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item,coinSpinner);
        spinnerCoin.getBackground().setColorFilter(Color.parseColor("#455a64"), PorterDuff.Mode.SRC_ATOP);
        //aqui encerra os comandos responsáveis pelo layout

        //cria o alert dialog de pesquisa do spinner
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        //insere o adapter no spinner, o adapter esta recebendo o array coinspinner
        spinnerCoin.setAdapter(adapterSpinner);
        adapterSpinner.notifyDataSetChanged();
        //notifica e faz a relação de cliques no spinner
        spinnerCoin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,  int i, long l) {
                // Toast.makeText(getActivity(), " " +i, Toast.LENGTH_SHORT).show();
                if (!coinArrayList.isEmpty()) {
                    ArrayList<Coin> coinss = new ArrayList<>();
                    if( spinnerCoin.getItemAtPosition(i).toString().equals("Todas as Moedas")){
                        adapter.setCoin(coinArrayList);
                        adapter.notifyDataSetChanged();
                    }else{
                        coinss.add(coinArrayList.get(i-1));
                        adapter.setCoin(coinss);
                        adapter.notifyDataSetChanged();
                        adapterSpinner.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                adapter.setCoin(coinArrayList);
                adapter.notifyDataSetChanged();
            }
        });
        //trazendo o refresh para o view do firstfragment
        MainActivity o = (MainActivity) getActivity();
        o.refresh(v);
        return  v;
    }//oncreate
}
