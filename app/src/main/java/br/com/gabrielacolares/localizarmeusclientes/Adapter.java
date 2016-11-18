package br.com.gabrielacolares.localizarmeusclientes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by gabrielacolares on 17/11/16.
 */

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private final Context context;
    private ArrayList<Cliente> lista;


    public Adapter (Context context, ArrayList<Cliente> lista) {
        this.lista = lista;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_clientes, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Cliente cliente = lista.get(position);
        holder
                .setNome(cliente.getNome());

    }

    @Override
    public int getItemCount() {

        return lista.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtNome;

        public ViewHolder(View itemView) {
            super(itemView);
            txtNome= (TextView) itemView.findViewById(R.id.item_nome);

        }

        public ViewHolder setNome(String nome) {
            if (nome== null) return this;
            txtNome.setText(nome.toUpperCase());
            return this;
        }

    }

}
