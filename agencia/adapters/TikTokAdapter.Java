package agencia.adapters;

import agencia.apis.TikTokApi;
import agencia.core.IGerenciadorMidiaSocial;
import agencia.models.*;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class TikTokAdapter implements IGerenciadorMidiaSocial {

    private final TikTokApi tiktokApi; 
    private final String PLATAFORMA_NOME = "TikTok";
    private static final ReentrantLock lock = new ReentrantLock();

    public TikTokAdapter(TikTokApi tiktokApi) {
        this.tiktokApi = tiktokApi;
    }

    @Override
    public RespostaUnificada<Publicacao> publicarConteudo(Conteudo conteudo) {
        lock.lock();
        try {
            Map<String, Object> respostaApi = tiktokApi.uploadVideo(conteudo);

            Boolean isPublished = (Boolean) respostaApi.get("is_published");

            if (Boolean.TRUE.equals(isPublished)) {

                Publicacao publicacao = new Publicacao(
                    (String) respostaApi.get("item_id"),
                    PLATAFORMA_NOME,
                    "tiktok.com/v/" + respostaApi.get("item_id"),
                    new Date().toString()
                );
                return RespostaUnificada.sucesso("Vídeo do TikTok publicado.", publicacao);
            } else {
                return RespostaUnificada.falha("Erro desconhecido ao publicar no TikTok.", respostaApi);
            }

        } catch (Exception e) {
            return RespostaUnificada.falha("Erro ao tentar fazer upload no TikTok: " + e.getMessage(), 
                                          Map.of("tipo", "Validacao_Video", "trace", e.toString()));
        } finally {
            lock.unlock();
        }
    }

    @Override
    public RespostaUnificada<Estatisticas> obterEstatisticas(String idPublicacao) {
        Estatisticas stats = new Estatisticas(8000, 1500, 300);
        return RespostaUnificada.sucesso("Estatísticas obtidas do TikTok.", stats);
    }
}