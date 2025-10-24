package agencia.apis;

import java.util.HashMap;
import java.util.Map;

public class TwitterApi {

    public Map<String, Object> postTweet(String tweetText, String imageUrl) {
        System.out.println("Twitter API: Recebendo requisição para tweet: '" + tweetText.substring(0, Math.min(tweetText.length(), 20)) + "...'");

        if (tweetText.length() > 280) {
            Map<String, Object> erro = new HashMap<>();
            erro.put("status", "error");
            erro.put("reason", "Tamanho_Excedido");
            erro.put("code", 186);
            return erro;
        }

        Map<String, Object> sucesso = new HashMap<>();
        String tweetId = "TW" + tweetText.hashCode();
        sucesso.put("tweet_id", tweetId);
        sucesso.put("status", "posted");
        sucesso.put("link", "twitter.com/post/" + tweetId);
        return sucesso;
    }
}
