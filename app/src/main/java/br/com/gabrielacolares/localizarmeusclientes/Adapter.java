package br.com.gabrielacolares.localizarmeusclientes;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.oceanbrasil.libocean.Ocean;
import com.oceanbrasil.libocean.control.glide.GlideRequest;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by aluno on 26/10/2016.
 */

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private final Context context;

    private ArrayList<Cliente> lista;
    private AdapterListener listener;

    public Adapter(Context context, ArrayList<Cliente> lista) {
        this.lista = lista;
        this.context = context;
    }

    public AdapterListener getListener() {
        return listener;
    }
    public void setListener(AdapterListener listener) {
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_lista_clientes, null);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Cliente cliente = lista.get(position);
        holder
                .setNome(cliente.getNome())
                .setEmail(cliente.getEmail())
                .setTelefone(cliente.getTelefone())
                .setRua(cliente.getRua())
                .setNumero(cliente.getNumero())
                .setBairro(cliente.getBairro())
                .setCidade(cliente.getCidade());
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView txtNome;
        private TextView txtEmail;
        private TextView txtTelefone;
        private TextView txtRua;
        private TextView txtNumero;
        private ImageView imgCliente;
        private TextView txtBairro;
        private TextView txtCidade;


        public ViewHolder(View itemView) {
            super(itemView);
            imgCliente = (ImageView) itemView.findViewById(R.id.imgCliente);
            txtNome = (TextView) itemView.findViewById(R.id.item_nome);
            txtEmail = (TextView) itemView.findViewById(R.id.email);
            txtTelefone = (TextView) itemView.findViewById(R.id.telefone);
            txtRua = (TextView) itemView.findViewById(R.id.rua);
            txtBairro = (TextView) itemView.findViewById(R.id.bairro);
            txtCidade = (TextView) itemView.findViewById(R.id.cidade);

            itemView.setOnClickListener(this);
        }

        public ViewHolder setNome(String nome){
            if(txtNome == null) return this;
            txtNome.setText(nome);
            return this;
        }

        public ViewHolder setEmail(String email){
            if(txtEmail == null) return this;
            txtEmail.setText(email);
            return this;
        }

        public ViewHolder setTelefone(String telefone) {
            if(telefone == null) return  this;
            txtTelefone.setText((telefone));
            return  this;
        }

       public ViewHolder setRua(String rua) {
           if(rua == null) return this;
           txtRua.setText(rua);
           return this;
       }
        public ViewHolder setNumero(String numero) {
            if(numero == null) return this;
            txtNumero.setText(numero);
            return  this;
        }
        public ViewHolder setBairro(String bairro) {
            if(bairro == null) return this;
                txtRua.setText(bairro);
                return  this;
        }

        public ViewHolder setCidade(String cidade) {
            if(cidade == null) return this;
            txtBairro.setText(cidade);
            return this;
        }

        public ViewHolder setCapa(String image){
            if(imgCliente == null) return this;

            Ocean.glide(context)
                    .load(image)
                    .build(GlideRequest.BITMAP).
                    resize(400, 400)
                    .circle()
                    .into(imgCliente);
            return this;
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            if(listener != null){
                listener.onItemClick(view, position);
            }
        }
    }
    interface AdapterListener{
        void onItemClick(View view, int posicao);
    }
}
