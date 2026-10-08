package com.example.capsuladofuturo;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;

import com.example.capsuladofuturo.databinding.ActivityCriarCapsulaBinding;
import com.google.android.material.textfield.TextInputLayout;

// Primeira tela: o usuário escreve a carta e escolhe quando ela será aberta.
public class CriarCapsulaActivity extends AppCompatActivity {

    private static final int ANO_LIMITE = 2026;          // o ano de abertura precisa ser maior que este
    private static final int MINIMO_CARACTERES = 20;

    private ActivityCriarCapsulaBinding tela;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tela = ActivityCriarCapsulaBinding.inflate(getLayoutInflater());
        setContentView(tela.getRoot());

        prepararBarraDeImportancia();

        apagarErroAoDigitar(tela.campoAutor, tela.layoutCampoAutor);
        apagarErroAoDigitar(tela.campoTitulo, tela.layoutCampoTitulo);
        apagarErroAoDigitar(tela.campoAno, tela.layoutCampoAno);
        apagarErroAoDigitar(tela.campoMensagem, tela.layoutCampoMensagem);

        tela.botaoSelar.setOnClickListener(botao -> {
            if (camposEstaoCorretos()) {
                enviarParaTelaSelada();
            }
        });
    }

    private void prepararBarraDeImportancia() {
        mostrarImportancia(tela.barraImportancia.getProgress());

        tela.barraImportancia.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar barra, int valor, boolean peloUsuario) {
                mostrarImportancia(valor);
            }

            @Override
            public void onStartTrackingTouch(SeekBar barra) { }

            @Override
            public void onStopTrackingTouch(SeekBar barra) { }
        });
    }

    private void mostrarImportancia(int valor) {
        tela.textoValorImportancia.setText(getString(R.string.valor_importancia, valor));
    }

    /*
     * Confere os campos de cima para baixo. No primeiro problema encontrado,
     * mostra o erro naquele campo, coloca o foco nele e interrompe a verificação.
     */
    private boolean camposEstaoCorretos() {
        if (lerTexto(tela.campoAutor).isEmpty()) {
            return marcarErro(tela.layoutCampoAutor, tela.campoAutor, getString(R.string.erro_autor));
        }

        if (lerTexto(tela.campoTitulo).isEmpty()) {
            return marcarErro(tela.layoutCampoTitulo, tela.campoTitulo, getString(R.string.erro_titulo));
        }

        String anoDigitado = lerTexto(tela.campoAno);
        if (anoDigitado.isEmpty()) {
            return marcarErro(tela.layoutCampoAno, tela.campoAno, getString(R.string.erro_ano_vazio));
        }
        Integer ano = converterAno(anoDigitado);
        if (ano == null) {
            return marcarErro(tela.layoutCampoAno, tela.campoAno, getString(R.string.erro_ano_invalido));
        }
        if (ano <= ANO_LIMITE) {
            return marcarErro(tela.layoutCampoAno, tela.campoAno,
                    getString(R.string.erro_ano_passado, ANO_LIMITE));
        }

        String mensagem = lerTexto(tela.campoMensagem);
        if (mensagem.isEmpty()) {
            return marcarErro(tela.layoutCampoMensagem, tela.campoMensagem,
                    getString(R.string.erro_mensagem_vazia));
        }
        if (mensagem.length() < MINIMO_CARACTERES) {
            int faltam = MINIMO_CARACTERES - mensagem.length();
            return marcarErro(tela.layoutCampoMensagem, tela.campoMensagem,
                    getString(R.string.erro_mensagem_curta, MINIMO_CARACTERES, faltam));
        }

        return true;
    }

    private boolean marcarErro(TextInputLayout caixa, EditText campo, String mensagem) {
        caixa.setError(mensagem);
        campo.requestFocus();
        return false;
    }

    private Integer converterAno(String texto) {
        try {
            return Integer.parseInt(texto);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    // Intent explícita: aponta diretamente para a classe da segunda tela.
    private void enviarParaTelaSelada() {
        Intent abrirCapsula = new Intent(CriarCapsulaActivity.this, CapsulaSeladaActivity.class);
        abrirCapsula.putExtra(CapsulaSeladaActivity.CHAVE_AUTOR, lerTexto(tela.campoAutor));
        abrirCapsula.putExtra(CapsulaSeladaActivity.CHAVE_TITULO, lerTexto(tela.campoTitulo));
        abrirCapsula.putExtra(CapsulaSeladaActivity.CHAVE_ANO, Integer.parseInt(lerTexto(tela.campoAno)));
        abrirCapsula.putExtra(CapsulaSeladaActivity.CHAVE_MENSAGEM, lerTexto(tela.campoMensagem));
        abrirCapsula.putExtra(CapsulaSeladaActivity.CHAVE_IMPORTANCIA, tela.barraImportancia.getProgress());
        startActivity(abrirCapsula);
    }

    private void apagarErroAoDigitar(EditText campo, TextInputLayout caixa) {
        campo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int inicio, int qtd, int depois) { }

            @Override
            public void onTextChanged(CharSequence s, int inicio, int antes, int qtd) {
                caixa.setError(null);
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
    }

    private String lerTexto(EditText campo) {
        return campo.getText() == null ? "" : campo.getText().toString().trim();
    }
}
