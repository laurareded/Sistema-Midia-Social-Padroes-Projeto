package agencia.apis;

import agencia.models.Conteudo;
import java.util.HashMap;
import java.util.Map;

public class InstagramApi {

    public Map<String, Object> uploadMedia(Conteudo content) throws IllegalArgumentException {
        System.out.println("Instagram API: Tentando upload de mídia: '" + content.mediaUrl() + "'");

        if (content.mediaUrl() == null || content.mediaUrl().isEmpty()) {
            throw new IllegalArgumentException("Instagram requer URL de mídia para upload.");
        }

        Map<String, Object> sucesso = new HashMap<>();
        String mediaId = "IG" + content.mediaUrl().hashCode();
        sucesso.put("media_id", mediaId);
        sucesso.put("success", true);
        sucesso.put("type", "image_post");
        return sucesso;
    }
}