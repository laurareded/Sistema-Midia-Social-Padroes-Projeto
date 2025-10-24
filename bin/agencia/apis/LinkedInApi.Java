package agencia.adapters;

import agencia.apis.LinkedInApi; 
import agencia.core.IGerenciadorMidiaSocial;
import agencia.models.*;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class LinkedInAdapter implements IGerenciadorMidiaSocial {

    private final LinkedInApi linkedInApi; 
    private final String PLATAFORMA_NOME = "LinkedIn";
    private static final ReentrantLock lock = new ReentrantLock();

    public LinkedInAdapter(LinkedInApi linkedInApi) {
        this.linkedInApi = linkedInApi;
    }

    @Override
    public RespostaUnificada<Publicacao> publicarConteudo(Conteudo conteudo) {
        lock.lock();
        try {
            Map<String, Object> respostaApi = linkedInApi.createArticle(conteudo);

            Integer resultCode = (Integer) respostaApi.get("result_code");

            if (resultCode != null && resultCode.equals(201)) {
                Publicacao publicacao = new Publicacao(
                    (String) respostaApi.get("urn_id"),
                    PLATAFORMA_NOME,
                    (String) respostaApi.get("post_url"),
                    new Date().toString()
                );
                return RespostaUnificada.sucesso("Artigo do LinkedIn criado com sucesso.", publicacao);
            } else {
                String mensagemApi = (String) respostaApi.get("message");
                String mensagemErro = String.format("Erro na publicação do LinkedIn. Código: %d", (resultCode != null ? resultCode : 0));
                return RespostaUnificada.falha(mensagemErro, respostaApi);
            }

        } catch (Exception e) {
            return RespostaUnificada.falha("Erro fatal no Adapter do LinkedIn: " + e.getMessage(), 
                                          Map.of("trace", e.toString()));
        } finally {
            lock.unlock();
        }
    }

    @Override
    public RespostaUnificada<Estatisticas> obterEstatisticas(String idPublicacao) {
        Estatisticas stats = new Estatisticas(20, 5, 50);
        return RespostaUnificada.sucesso("Estatísticas obtidas do LinkedIn.", stats);
    }
}