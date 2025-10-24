package agencia.adapters;

import agencia.apis.InstagramApi;
import agencia.core.IGerenciadorMidiaSocial;
import agencia.models.Conteudo;
import agencia.models.Estatisticas;
import agencia.models.Publicacao;
import agencia.models.RespostaUnificada;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class InstagramAdapter implements IGerenciadorMidiaSocial {

    private final InstagramApi instagramApi;
    private final String PLATAFORMA_NOME = "Instagram";
    private static final ReentrantLock lock = new ReentrantLock();

    public InstagramAdapter(InstagramApi instagramApi) {
        this.instagramApi = instagramApi;
    }

    @Override
    public RespostaUnificada<Publicacao> publicarConteudo(Conteudo conteudo) {
        lock.lock();
        try {
            Map<String, Object> respostaApi = instagramApi.uploadMedia(conteudo);

            if (Boolean.TRUE.equals(respostaApi.get("success"))) {
                Publicacao publicacao = new Publicacao(
                    (String) respostaApi.get("media_id"),
                    PLATAFORMA_NOME,
                    "instagram.com/p/" + respostaApi.get("media_id"),
                    new Date().toString()
                );
                return RespostaUnificada.sucesso("Mídia do Instagram publicada.", publicacao);
            } else {
                return RespostaUnificada.falha("Erro desconhecido no upload do Instagram.", respostaApi);
            }

        } catch (IllegalArgumentException e) {
            return RespostaUnificada.falha("Erro de validação do Instagram: " + e.getMessage(), 
                                          Map.of("tipo", "Validacao", "mensagem_api", e.getMessage()));
        } catch (Exception e) {
            return RespostaUnificada.falha("Erro fatal no Adapter do Instagram: " + e.getMessage(), 
                                          Map.of("trace", e.toString()));
        } finally {
            lock.unlock();
        }
    }

    @Override
    public RespostaUnificada<Estatisticas> obterEstatisticas(String idPublicacao) {
        Estatisticas stats = new Estatisticas(580, 25, 6);
        return RespostaUnificada.sucesso("Estatísticas obtidas do Instagram.", stats);
    }
}
