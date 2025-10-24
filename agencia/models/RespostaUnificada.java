package agencia.models;

import java.util.Map;

public record RespostaUnificada<T>(
        boolean sucesso,
        String mensagem,
        T dados,
        Map<String, Object> errosDetalhados) {

    public static <T> RespostaUnificada<T> sucesso(String mensagem, T dados) {
        return new RespostaUnificada<>(true, mensagem, dados, null);
    }

    public static <T> RespostaUnificada<T> falha(String mensagem, Map<String, Object> errosDetalhados) {
        return new RespostaUnificada<>(false, mensagem, null, errosDetalhados);
    }
}
