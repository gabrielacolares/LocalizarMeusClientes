package br.com.gabrielacolares.localizarmeusclientes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.oceanbrasil.libocean.Ocean;
import com.oceanbrasil.libocean.control.glide.GlideRequest;

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
                .setNome(cliente.getNome())
                .setFotoCliente(cliente.getUrlFoto())
                .setRua(cliente.getRua())
                .setBairro(cliente.getBairro())
                .setNumero(cliente.getNumero());
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtNome;
        private TextView txtRua;
        private TextView txtBairro;
        private  TextView txtNumero;
        private ImageView imgCliente;

        public ViewHolder(View itemView) {
            super(itemView);
            txtNome= (TextView) itemView.findViewById(R.id.item_nome);
            txtRua = (TextView) itemView.findViewById(R.id.item_rua);
            txtBairro = (TextView) itemView.findViewById(R.id.item_bairro);
            txtNumero = (TextView) itemView.findViewById(R.id.item_numero);
            imgCliente = (ImageView) itemView.findViewById(R.id.item_img);
        }

        public ViewHolder setNome(String nome) {
            if (nome== null) return this;
            txtNome.setText(nome);
            return this;
        }

        public ViewHolder setRua(String rua) {
            if (rua== null) return this;
            txtRua.setText(rua);
            return this;
        }

        public ViewHolder setBairro(String bairro) {
            if (bairro== null) return this;
            txtBairro.setText(bairro);
            return this;
        }

        public ViewHolder setNumero(String numero) {
            if (numero== null) return this;
            txtNumero.setText(numero);
            return this;
        }

        public ViewHolder setFotoCliente(String image){
            if(image== null) return this;
            Ocean.glide(context)
                    .load(image)
                    .build(GlideRequest.BITMAP)
                    .resize(200, 200)
                    .circle()
                    .into(imgCliente);
            return this;
        }
    }
}
