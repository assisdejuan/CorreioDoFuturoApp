package com.example.capsuladofuturo;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.capsuladofuturo.databinding.ActivityCapsulaSeladaBinding;

// Segunda tela: mostra a cápsula montada com os dados recebidos pela Intent.
public class CapsulaSeladaActivity extends AppCompatActivity {

    public static final String CHAVE_AUTOR = "capsula.autor";
    public static final String CHAVE_TITULO = "capsula.titulo";
    public static final String CHAVE_ANO = "capsula.ano";
    public static final String CHAVE_MENSAGEM = "capsula.mensagem";
    public static final String CHAVE_IMPORTANCIA = "capsula.importancia";

    private static final float PERCENTUAL_POR_ESTRELA = 20f;

    private ActivityCapsulaSeladaBinding tela;

    private String autor;
    private String titulo;
    private int ano;
    private String mensagem;
    private int importancia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tela = ActivityCapsulaSeladaBinding.inflate(getLayoutInflater());
        setContentView(tela.getRoot());

        Intent recebida = getIntent();
        autor = recebida.getStringExtra(CHAVE_AUTOR);
        titulo = recebida.getStringExtra(CHAVE_TITULO);
        ano = recebida.getIntExtra(CHAVE_ANO, 0);
        mensagem = recebida.getStringExtra(CHAVE_MENSAGEM);
        importancia = recebida.getIntExtra(CHAVE_IMPORTANCIA, 0);

        preencherCarta();

        // finish() fecha esta tela e revela a anterior, com os campos ainda preenchidos
        tela.botaoEditar.setOnClickListener(botao -> finish());
        tela.botaoCompartilhar.setOnClickListener(botao -> compartilhar());
    }

    private void preencherCarta() {
        tela.textoTituloCapsula.setText(titulo);
        tela.textoResumo.setText(getString(R.string.resumo_capsula, autor, ano));
        tela.textoRemetente.setText(getString(R.string.remetente, autor));
        tela.textoAnoEntrega.setText(String.valueOf(ano));
        tela.textoMensagem.setText(mensagem);
        tela.textoImportancia.setText(getString(R.string.importancia_capsula, importancia));
        tela.avaliacaoImportancia.setRating(importancia / PERCENTUAL_POR_ESTRELA);
    }

    // Intent implícita: o sistema oferece os apps capazes de receber texto.
    private void compartilhar() {
        String conteudo = getString(R.string.texto_compartilhado, ano, titulo, mensagem);

        Intent envio = new Intent(Intent.ACTION_SEND);
        envio.setType("text/plain");
        envio.putExtra(Intent.EXTRA_SUBJECT, titulo);
        envio.putExtra(Intent.EXTRA_TEXT, conteudo);

        startActivity(Intent.createChooser(envio, getString(R.string.titulo_escolha_app)));
    }
}
