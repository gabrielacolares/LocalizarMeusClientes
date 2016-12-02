package br.com.gabrielacolares.localizarmeusclientes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.TextView;

import com.oceanbrasil.libocean.Ocean;
import com.oceanbrasil.libocean.control.glide.GlideRequest;

public class DetalheCliente extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setContentView(R.layout.layout_detalhe);
        recuperarCliente();
    }


    private void recuperarCliente() {
        String nome = getIntent().getStringExtra("nome");
        String email = getIntent().getStringExtra("email");
        String telefone = getIntent().getStringExtra("telefone");
        //Date dataNascimento = (Date) getIntent().getDataString("dataNasc");
        String endereco = getIntent().getStringExtra("endereco");

        Cliente cliente = (Cliente) getIntent().getSerializableExtra("cliente");
        TextView tvNome = (TextView) findViewById(R.id.detalhesNome);
        TextView tvEmail = (TextView) findViewById(R.id.detalhesEmail);
        TextView tvEndereco= (TextView) findViewById(R.id.detalhesEndereco);
        ImageView imageView = (ImageView) findViewById(R.id.detalhesImageView);

        tvNome.setText(nome);
        tvEmail.setText(email);
        tvEndereco.setText(endereco);

        Ocean.glide(this)
                .load(cliente.getUrlFoto())
                .build(GlideRequest.BITMAP)
                .resize(200, 200)
                .circle()
                .into(imageView);
    }
}

