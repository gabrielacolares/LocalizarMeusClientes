package br.com.gabrielacolares.localizarmeusclientes;


import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by gabrielacolares on 10/11/16.
 */

public class CadastroFragment extends Fragment {
    private EditText editNome;
    private EditText editEmail;
    private EditText editDataNascimento;
    private EditText editTelefone;
    private EditText editRg;
    private EditText editCpf;
    private EditText editRua;
    private EditText editNumero;
    private EditText editBairro;
    private EditText editCidade;
    private EditText editStatus;
    private View appView;
    private LayoutInflater myInflater;
    private Cliente cliente;

    public CadastroFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.itemMenu:
                salvar();
                break;
        }
        return true;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myInflater = inflater;
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Adicionar Cliente");
        return inflater.inflate(R.layout.layout_add, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        appView = view;
        editNome = (EditText) view.findViewById(R.id.nome);
        editEmail = (EditText) view.findViewById(R.id.email);
        editDataNascimento = (EditText) view.findViewById(R.id.datanasc);
        editTelefone = (EditText) view.findViewById(R.id.telefone);
        editRua = (EditText) view.findViewById(R.id.rua);
        editNumero = (EditText) view.findViewById(R.id.numero);
        editBairro = (EditText) view.findViewById(R.id.bairro);
        editCidade = (EditText) view.findViewById(R.id.cidade);
    }

    public void setaDados() {
        String nome = editNome.getText().toString() ;
        String email = editEmail.getText().toString();
        Date dataNascimento = new Date(editDataNascimento.getText().toString());
        String telefone = editTelefone.getText().toString();
        String rua = editRua.getText().toString();
        String numero = editNumero.getText().toString();
        String bairro = editBairro.getText().toString();
        String cidade = editBairro.getText().toString();

        this.cliente = new Cliente();
        this.cliente.setNome(nome);
        this.cliente.setEmail(email);
        this.cliente.setDataNascimento(dataNascimento);
        this.cliente.setTelefone(telefone);
        this.cliente.setRua(rua);
        this.cliente.setNumero(numero);
        this.cliente.setBairro(bairro);
        this.cliente.setCidade(cidade);
    }

    public void salvar( ) {
        setaDados();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("clientes");
        databaseReference.push().setValue(this.cliente);
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }
}