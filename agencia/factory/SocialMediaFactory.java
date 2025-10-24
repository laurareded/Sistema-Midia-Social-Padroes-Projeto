package agencia.factory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import agencia.adapters.*;
import agencia.apis.*;
import agencia.core.IGerenciadorMidiaSocial;

public class SocialMediaFactory {

    private static final Map<String, Supplier<IGerenciadorMidiaSocial>> FACTORIES = new HashMap<>();

    static {

        FACTORIES.put("Twitter", () -> {
            TwitterApi api = new TwitterApi();
            return new TwitterAdapter(api);
        });

        FACTORIES.put("Instagram", () -> {
            InstagramApi api = new InstagramApi();
            return new InstagramAdapter(api);
        });

        FACTORIES.put("LinkedIn", () -> {
            LinkedInApi api = new LinkedInApi(); 
            return new LinkedInAdapter(api);     
        });

        FACTORIES.put("TikTok", () -> {
            TikTokApi api = new TikTokApi();     
            return new TikTokAdapter(api);       
        });
    }


    public static IGerenciadorMidiaSocial getAdapter(String plataforma) {
        Supplier<IGerenciadorMidiaSocial> factory = FACTORIES.get(plataforma);

        if (factory == null) {
            throw new IllegalArgumentException(String.format("Plataforma '%s' não suportada ou configurada na Factory.", plataforma));
        }

        return factory.get();
    }
}