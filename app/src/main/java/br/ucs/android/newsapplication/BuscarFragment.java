package br.ucs.android.newsapplication;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import br.ucs.android.newsapplication.activities.MainActivity;
import br.ucs.android.newsapplication.model.Artigo;
import br.ucs.android.newsapplication.model.NewsResponse;
import br.ucs.android.newsapplication.model.Resposta;
import br.ucs.android.newsapplication.rest.ApiClient;
import br.ucs.android.newsapplication.rest.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BuscarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BuscarFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String TAG = BuscarFragment.class.getSimpleName();
    private final static String API_KEY = "16613c31e3b54b27bf64db1ba67bfe95";

    private EditText mEditTextPesquisa;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public BuscarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BuscarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BuscarFragment newInstance(String param1, String param2) {
        BuscarFragment fragment = new BuscarFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_buscar, container, false);


        mEditTextPesquisa = view.findViewById(R.id.etPesquisa);

        Button novo = (Button) view.findViewById(R.id.button);
        novo.setOnClickListener(view1 -> {
            String termo = mEditTextPesquisa.getText().toString();
            buscar(termo);
        });


        return view;
    }

    public void buscar(String termo)
    {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date data = new Date();
        String dataFormatada = dateFormat.format(data);

        if (API_KEY.isEmpty()) {
            Toast.makeText(getContext(), "É necessário obter a chave da API https://newsapi.org/!", Toast.LENGTH_LONG).show();
            return;
        }

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        //Call<Artigo> call = apiService.getTopHeadLines("BR", "business", API_KEY);

        Call<NewsResponse> call = apiService.getSearchByUser(termo, dataFormatada, API_KEY);

        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                int statusCode = response.code();
                Log.e(TAG, response.code() + " - " + response.message());
                //List<Artigo> movies = response.body().getResults();
                //recyclerView.setAdapter(new NewsAdapter(movies, R.layout.list_item_movie, getApplicationContext()));
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                mostraAlerta("Erro", t.toString());
                // Log error here since request failed
                Log.e(TAG, t.toString());
            }
        });
    }
    private void mostraAlerta(String titulo, String mensagem) {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle(titulo);
        alertDialog.setMessage(mensagem);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok), (dialog, which) -> dialog.dismiss());
        alertDialog.show();
    }
}