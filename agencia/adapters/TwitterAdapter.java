package agencia.adapters;

import agencia.apis.TwitterApi;
import agencia.core.IGerenciadorMidiaSocial;
import agencia.models.Conteudo;
import agencia.models.Estatisticas;
import agencia.models.Publicacao;
import agencia.models.RespostaUnificada;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class TwitterAdapter implements IGerenciadorMidiaSocial {

    private final TwitterApi twitterApi;
    private final String PLATAFORMA_NOME = "Twitter";

    private static final ReentrantLock lock = new ReentrantLock();

    public TwitterAdapter(TwitterApi twitterApi) {
        this.twitterApi = twitterApi;
    }

    @Override
    public RespostaUnificada<Publicacao> publicarConteudo(Conteudo conteudo) {
        lock.lock(); 
        try {
            Map<String, Object> respostaApi = twitterApi.postTweet(conteudo.texto(), conteudo.mediaUrl());

            String status = (String) respostaApi.get("status");

            if ("posted".equals(status)) {
                Publicacao publicacao = new Publicacao(
                    (String) respostaApi.get("tweet_id"),
                    PLATAFORMA_NOME,
                    (String) respostaApi.get("link"),
                    new Date().toString()
                );
                return RespostaUnificada.sucesso("Tweet publicado com sucesso.", publicacao);
            } else {
                String razao = (String) respostaApi.get("reason");
                String mensagemErro = String.format("Erro na publicação do Twitter. Razão: %s", razao);
                return RespostaUnificada.falha(mensagemErro, respostaApi);
            }

        } catch (Exception e) {
            return RespostaUnificada.falha("Erro fatal no Adapter do Twitter: " + e.getMessage(), 
                                          Map.of("trace", e.toString()));
        } finally {
            lock.unlock();
        }
    }

    @Override
    public RespostaUnificada<Estatisticas> obterEstatisticas(String idPublicacao) {
        Estatisticas stats = new Estatisticas(150, 8, 3);
        return RespostaUnificada.sucesso("Estatísticas obtidas do Twitter.", stats);
    }
}
