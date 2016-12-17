package br.com.gabrielacolares.localizarmeusclientes;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by gabrielacolares on 10/11/16.
 */

public class ListaFragment extends Fragment implements Adapter.AdapterListener {
    View myView;
    ArrayList<Cliente> clientes= new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Clientes");
        return inflater.inflate(R.layout.layout_lista,container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        myView = view;
        recuperarFirebase();

    }

    private void recuperarFirebase(){
        clientes= new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("clientes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Cliente cliente = snapshot.getValue(Cliente.class);
                    clientes.add(cliente);
                }

                criarAdapter(myView,clientes);
                escondeProgressBar(clientes);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void criarAdapter(View view, ArrayList<Cliente> lista) {
        ArrayList<Cliente> listaClientes = new ArrayList<>();
        listaClientes = lista;

        Adapter adapter = new Adapter(getActivity(), lista);
        adapter.setListener(this);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.lista_clientes_recycler);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());

        adapter.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        Log.d("Teste","criarAdapter");

    }

    private void escondeProgressBar(ArrayList<Cliente> lista) {
        ProgressBar progressBar = (ProgressBar) myView.findViewById(R.id.loading);
        LinearLayout linearLayout = (LinearLayout) myView.findViewById(R.id.linear_layout_lista_vazia);
        progressBar.setVisibility(View.VISIBLE);
        if(lista.size() > 0){
            progressBar.setVisibility(View.GONE);
            linearLayout.setVisibility(View.GONE);
        } else {
            progressBar.setVisibility(View.GONE);
            linearLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onItemClick(View view, int posicao) {
        Log.d("teste","click");
        Cliente cliente = new Cliente();
        cliente = clientes.get(posicao);
        Intent i = new Intent(getActivity(), DetalheCliente.class);
        i.putExtra("nome", cliente.getNome());
        i.putExtra("email", cliente.getEmail());
        i.putExtra("datanasc", cliente.getDataNascimento());
        i.putExtra("telefone", cliente.getTelefone());
        i.putExtra("endereco", cliente.getEndereco());

        //ClienteManager.getInstamce().setCliente(cliente);

        i.putExtra("cliente", cliente);

        View imgCliente = view.findViewById(R.id.item_img);

        if(Build.VERSION.SDK_INT>= Build.VERSION_CODES.JELLY_BEAN) {
            ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeBasic().makeSceneTransitionAnimation(getActivity(),imgCliente,"cliente");
            startActivity(i, optionsCompat.toBundle());
        } else {
            startActivity(i);
        }
    }
}