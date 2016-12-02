package br.com.gabrielacolares.localizarmeusclientes;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.oceanbrasil.libocean.Ocean;
import com.oceanbrasil.libocean.control.glide.GlideRequest;

import java.util.ArrayList;

/**
 * Created by gabrielacolares on 17/11/16.
 */

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder>{

    private final Context context;
    private ArrayList<Cliente> lista;
    private AdapterListener listener;
    private LinearLayoutManager layoutManager;
    private Cliente cliente;

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
        cliente = lista.get(position);

        holder
                .setNome(cliente.getNome())
                .setFotoCliente(cliente.getUrlFoto());
        int firstVisible = layoutManager.findFirstCompletelyVisibleItemPosition();
        cliente = lista.get(position);
        animate(holder, firstVisible,position);
    }

    public void animate(RecyclerView.ViewHolder viewHolder, int firstVisible, int position) {
        final Animation animAntecipeOvershoot = AnimationUtils.loadAnimation(context, R.anim.slide_rigth_from_left);
        int range  = calcRange(firstVisible,position);
        animAntecipeOvershoot.setDuration(range);
        viewHolder.itemView.setAnimation(animAntecipeOvershoot);
    }


    public int calcRange(int firstVisible, int position) {
        int delay = 200;
        int range = (position - firstVisible) * delay;
        return  (range<0) ?range *-1 : range;
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txtNome;
        private ImageView imgCliente;

        public ViewHolder(View itemView) {
            super(itemView);
            LinearLayout linearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayoutItem);
            txtNome= (TextView) itemView.findViewById(R.id.item_nome);
            imgCliente = (ImageView) itemView.findViewById(R.id.item_img);
            linearLayout.setOnClickListener(this);
        }

        public ViewHolder setNome(String nome) {
            if (nome== null) return this;
            txtNome.setText(nome);
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

        @Override
        public void onClick(View view) {
            Log.d("click", "onClick");
            if (listener != null){
                Log.d("click", "listener");
                listener.onItemClick(view,getAdapterPosition());
            }
        }
    }

    public void setListener(AdapterListener listener) {
        this.listener = listener;
    }

    public void setLayoutManager(LinearLayoutManager layout) {
        this.layoutManager = layout;
    }

    interface AdapterListener{
        void onItemClick(View view, int posicao);
    }


}
