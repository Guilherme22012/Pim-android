package com.cyberdyne.pim_android.network; // Pacote do seu projeto

// --- IMPORTAÇÕES ADICIONADAS ---
import android.content.Context; // Necessário para o Interceptor
import okhttp3.logging.HttpLoggingInterceptor; // Para vermos os logs da rede
import java.util.concurrent.TimeUnit; // Para definir timeouts
// --- FIM DAS IMPORTAÇÕES ADICIONADAS ---

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import okhttp3.OkHttpClient;

// ... (todas as suas importações "javax.net.ssl" para o UnsafeOkHttp) ...
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class ApiClient {

    // A URL base para o emulador
    public static final String BASE_URL = "https://10.0.2.2:7086/api/";

    // (A URL para o celular físico, comentada por enquanto)
    // public static final String BASE_URL = "http://192.168.1.105:5236/api/";

    private static Retrofit retrofit = null;
    private static ApiService apiService = null; // Vamos mudar como isso funciona

    // --- MÉTODO getClient() ATUALIZADO ---
    // Agora ele recebe o 'Context' (ex: a sua MainActivity)
    public static Retrofit getClient(Context context) {
        if (retrofit == null) {

            // 1. Cria o interceptor de Logging (para vermos o que está sendo enviado/recebido)
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            // 2. Cria o construtor do OkHttpClient
            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    // 3. Adiciona o Interceptor de Token (que precisa do Contexto)
                    .addInterceptor(new AuthInterceptor(context.getApplicationContext()))
                    // 4. Adiciona o Interceptor de Logging
                    .addInterceptor(logging);

            // 5. Adiciona o código "unsafe" para confiar no certificado do emulador
            configureUnsafeHttpClient(clientBuilder);

            // 6. Constrói o cliente OkHttp
            OkHttpClient client = clientBuilder.build();

            // 7. Constrói o Retrofit
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client) // Usa o cliente OkHttp customizado
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    // --- MÉTODO getApiService() ATUALIZADO ---
    // Agora ele também precisa do 'Context'
    public static ApiService getApiService(Context context) {
        // Sempre (re)cria a instância do Retrofit para garantir o contexto correto?
        // Não, vamos reutilizar a instância criada.
        // Mas a primeira chamada DEVE passar o contexto.
        if (apiService == null) {
            apiService = getClient(context).create(ApiService.class);
        }
        return apiService;
    }
    // --- FIM DAS ATUALIZAÇÕES ---

    // Renomeamos o método antigo para ser um auxiliar
    private static void configureUnsafeHttpClient(OkHttpClient.Builder builder) {
        try {
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                        }
                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                        }
                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[]{};
                        }
                    }
            };

            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}