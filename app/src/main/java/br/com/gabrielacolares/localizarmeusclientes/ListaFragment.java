package br.com.gabrielacolares.localizarmeusclientes;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

/**
 * Created by gabrielacolares on 10/11/16.
 */

public class ListaFragment extends Fragment {
    View myView;
    ArrayList<Cliente> clientes= new ArrayList<>();
    final Fragment fragmentCadastro = new CadastroFragment();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_map,container,false);
        return inflater.inflate(R.layout.layout_lista,container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recuperarFirebase();
        myView = view;
    }
    private void recuperarFirebase(){
        clientes= new ArrayList<>();
        FirebaseDatabase.getInstance().getReference().child("clientes").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Cliente cliente = snapshot.getValue(Cliente.class);
                    Log.d("DEBUG"," >>> "+cliente.getNome());
                    clientes.add(cliente);
                }
                criarAdapter(myView,clientes);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void criarAdapter(View view, ArrayList<Cliente> lista) {
        Adapter adapter = new Adapter(view.getContext(), lista);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.lista_clientes_recycler);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }
}