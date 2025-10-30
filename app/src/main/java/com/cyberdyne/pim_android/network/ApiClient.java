package com.cyberdyne.pim_android.network; // Verifique se o nome do pacote está correto

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

// Importações necessárias para lidar com o erro "SSLHandshakeException"
// que pode ocorrer ao tentar conectar-se a "https://localhost"
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import okhttp3.OkHttpClient;

public class ApiClient {

    // --- PONTO CRÍTICO ---
    // Esta é a URL base do API Pim-Web.
    // O IP 10.0.2.2 é um endereço especial que o Emulador Android
    // usa para se referir ao 'localhost' do computador (PC/Notebook).
    // Usar porta HTTPS (7086) do backend C#.
    public static final String BASE_URL = "https://10.0.2.2:7086/api/";
    private static Retrofit retrofit = null;
    private static ApiService apiService = null;
    private static Retrofit getClient() {
        if (retrofit == null) {

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)

                    .client(getUnsafeOkHttpClient())

                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static ApiService getApiService() {
        if (apiService == null) {
            apiService = getClient().create(ApiService.class);
        }
        return apiService;
    }
    private static OkHttpClient getUnsafeOkHttpClient() {
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

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);

            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });

            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}