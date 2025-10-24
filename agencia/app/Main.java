package agencia.app;

import agencia.core.IGerenciadorMidiaSocial;
import agencia.factory.SocialMediaFactory;
import agencia.models.Conteudo;
import agencia.models.Estatisticas;
import agencia.models.Publicacao;
import agencia.models.RespostaUnificada;

public class Main {

    public static void main(String[] args) {
        System.out.println("--- 1. Inicialização e Obtenção dos Adapters via Factory ---");
        IGerenciadorMidiaSocial twitterAdapter;
        IGerenciadorMidiaSocial instagramAdapter;
        IGerenciadorMidiaSocial linkedInAdapter;
        IGerenciadorMidiaSocial tiktokAdapter;

        try {
            twitterAdapter = SocialMediaFactory.getAdapter("Twitter");
            instagramAdapter = SocialMediaFactory.getAdapter("Instagram");
            linkedInAdapter = SocialMediaFactory.getAdapter("LinkedIn");
            tiktokAdapter = SocialMediaFactory.getAdapter("TikTok");
            System.out.println("Adapters criados com sucesso para todas as 4 plataformas.");
        } catch (IllegalArgumentException e) {
            System.err.println("Erro ao inicializar: " + e.getMessage());
            return;
        }

        System.out.println("\n--- 2. Publicação Unificada (Twitter) ---");
        Conteudo conteudoTwitter = new Conteudo(
                "Finalizando a implementação do padrão Adapter em Java! #DesignPatterns #AdapterPattern",
                "diagrama_uml.png"
        );
        RespostaUnificada<Publicacao> respTwitter = twitterAdapter.publicarConteudo(conteudoTwitter);
        imprimirResposta(respTwitter);

        System.out.println("\n--- 3. Publicação Unificada (Instagram) ---");
        Conteudo conteudoInstagram = new Conteudo(
                "Nova foto do escritório!",
                "foto_escritorio.jpg"
        );
        RespostaUnificada<Publicacao> respInstagram = instagramAdapter.publicarConteudo(conteudoInstagram);
        imprimirResposta(respInstagram);

        System.out.println("\n--- 4. Publicação Unificada (LinkedIn) ---");
        Conteudo conteudoLinkedIn = new Conteudo(
                "Compartilhando uma nova arquitetura de sistema que utiliza os padrões Adapter e Factory Method para garantir a integração flexível de múltiplas APIs externas. Essencial para microserviços!",
                null
        );
        RespostaUnificada<Publicacao> respLinkedIn = linkedInAdapter.publicarConteudo(conteudoLinkedIn);
        imprimirResposta(respLinkedIn);

        System.out.println("\n--- 5. Publicação Unificada (TikTok) ---");
        Conteudo conteudoTikTok = new Conteudo(
                "Meu primeiro vídeo sobre padrões de projeto!",
                "video_padroes.mp4"
        );
        RespostaUnificada<Publicacao> respTikTok = tiktokAdapter.publicarConteudo(conteudoTikTok);
        imprimirResposta(respTikTok);


        System.out.println("\n--- 6. Teste de Tratamento de Erro Granular ---");
        Conteudo conteudoCurto = new Conteudo("Oi.", null);
        RespostaUnificada<Publicacao> respErroLinkedIn = linkedInAdapter.publicarConteudo(conteudoCurto);
        imprimirResposta(respErroLinkedIn);

        Conteudo conteudoErroVideo = new Conteudo("Vídeo errado.", "foto.jpg");
        RespostaUnificada<Publicacao> respErroTikTok = tiktokAdapter.publicarConteudo(conteudoErroVideo);
        imprimirResposta(respErroTikTok);


        System.out.println("\n--- 7. Obtenção de Estatísticas Unificadas ---");
        RespostaUnificada<Estatisticas> statsTwitter = twitterAdapter.obterEstatisticas("TW1234");
        imprimirResposta(statsTwitter);

        RespostaUnificada<Estatisticas> statsLinkedIn = linkedInAdapter.obterEstatisticas("LI5678");
        imprimirResposta(statsLinkedIn);

        RespostaUnificada<Estatisticas> statsTikTok = tiktokAdapter.obterEstatisticas("TT9012");
        imprimirResposta(statsTikTok);
    }


    private static void imprimirResposta(RespostaUnificada<?> resposta) {
        System.out.printf("  [Status: %s] Mensagem: %s\n",
                resposta.sucesso() ? "SUCESSO" : "FALHA",
                resposta.mensagem());

        if (resposta.sucesso() && resposta.dados() != null) {
            System.out.printf("    Dados Recebidos: %s\n", resposta.dados());
        }

        if (!resposta.sucesso() && resposta.errosDetalhados() != null) {
            System.out.printf("    Detalhes do Erro (Granular): %s\n", resposta.errosDetalhados());
        }
    }
}