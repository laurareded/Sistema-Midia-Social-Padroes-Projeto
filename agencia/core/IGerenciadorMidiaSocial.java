package agencia.core;

import agencia.models.Conteudo;
import agencia.models.Publicacao;
import agencia.models.Estatisticas;
import agencia.models.RespostaUnificada;

public interface IGerenciadorMidiaSocial {

    RespostaUnificada<Publicacao> publicarConteudo(Conteudo conteudo);

    RespostaUnificada<Estatisticas> obterEstatisticas(String idPublicacao);
}
