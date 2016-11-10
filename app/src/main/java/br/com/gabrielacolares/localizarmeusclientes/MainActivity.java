package br.com.gabrielacolares.localizarmeusclientes;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    final Fragment fragmentMap = new MapFragment();
    final Fragment fragmentCadastro = new CadastroFragment();
    final Fragment fragmentPesquisar = new PesquisarFragment();
    final Fragment fragmentLista = new ListaFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addFragment(fragmentMap);

        setBottomNavigation();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

     /*   bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.item_menu_home:
                                textHome.setVisibility(View.VISIBLE);
                                textPesquisar.setVisibility(View.GONE);
                                textAdd.setVisibility(View.GONE);
                                break;
                            case R.id.item_menu_pesquisar:
                                textHome.setVisibility(View.GONE);
                                textPesquisar.setVisibility(View.VISIBLE);
                                textAdd.setVisibility(View.GONE);
                                break;
                            case R.id.item_menu_add:
                                textHome.setVisibility(View.GONE);
                                textPesquisar.setVisibility(View.GONE);
                                textAdd.setVisibility(View.VISIBLE);
                                break;
                        }
                        return false;
                    }});
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = new Fragment();
                switch (item.getItemId()) {
                    case R.id.item_menu_home:
                        fragment = new MapFragment();
                        break;
                    case  R.id.item_menu_pesquisar:
                        fragment = new PesquisarFragment();
                        break;
                    case R.id.item_menu_add:
                        fragment = new ClienteFragment();
                        break;

                }


                return false;
            }
        });
*/


        private void setBottomNavigation() {

            BottomNavigationView bottomNavigationView = (BottomNavigationView)
                    findViewById(R.id.bottom_navigation);

            bottomNavigationView.setOnNavigationItemSelectedListener(
                    new BottomNavigationView.OnNavigationItemSelectedListener() {
                        @Override
                        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                            switch (item.getItemId()){
                                case R.id.action_home:
                                    replaceFragment(fragmentMap);
                                    break;

                                case R.id.action_pesquisar:
                                    replaceFragment(fragmentPesquisar);
                                    break;

                                case R.id.action_add_cliente:
                                    replaceFragment(fragmentCadastro);
                                    break;
                            }
                            return false;
                        }
                    });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_lista) {
            replaceFragment(fragmentPesquisar);
        }

        return super.onOptionsItemSelected(item);
    }



    private void addFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        transaction.add(R.id.fragment, fragment);
        transaction.addToBackStack(null);

        transaction.commit();
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction  transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, fragment);
        //transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void addCliente(View view){
        Intent intent = new Intent(this, CadastroCliente.class);
        startActivity(intent);
    }
    }

