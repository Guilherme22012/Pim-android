package com.cyberdyne.pim_android.network; // Pacote do seu projeto

import android.content.Context;
import androidx.annotation.NonNull; // Importação para @NonNull
import com.cyberdyne.pim_android.utils.SessionManager; // Importa o SessionManager
import java.io.IOException;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {

    private final SessionManager sessionManager;

    public AuthInterceptor(Context context) {
        // Inicializa o SessionManager ("cofre")
        this.sessionManager = new SessionManager(context.getApplicationContext());
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request originalRequest = chain.request();

        // Pega o token do "cofre"
        String token = sessionManager.getToken();

        Request.Builder builder = originalRequest.newBuilder();

        // Adiciona cabeçalhos padrão (boa prática)
        builder.header("Accept", "application/json");

        // Se o token existir, adiciona-o ao cabeçalho (Header) da requisição
        if (token != null && !token.isEmpty()) {
            builder.addHeader("Authorization", "Bearer " + token);
        }

        Request newRequest = builder.build();
        return chain.proceed(newRequest);
    }
}