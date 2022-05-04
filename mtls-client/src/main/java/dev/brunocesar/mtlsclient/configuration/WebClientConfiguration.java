package dev.brunocesar.mtlsclient.configuration;

import io.netty.channel.ChannelOption;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.SslContextBuilder;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ClientHttpConnector;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.util.ResourceUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.security.KeyStore;

@Configuration
public class WebClientConfiguration {

    @Bean
    public WebClient webClient() {
        try {
            SslContext sslContext = SslContextBuilder.forClient()
                    .keyManager(getKeyManagerFactory())
                    .trustManager(getTrustManagerFactory())
                    .build();

            HttpClient httpClient = HttpClient.create()
                    .tcpConfiguration(client ->
                            client.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 30000)
                                    .doOnConnected(conn -> conn
                                            .addHandlerLast(new ReadTimeoutHandler(30))))
                    .secure(t -> t.sslContext(sslContext));

            ClientHttpConnector connector = new ReactorClientHttpConnector(httpClient.wiretap(true));
            return WebClient.builder()
                    .clientConnector(connector)
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private KeyManagerFactory getKeyManagerFactory() throws Exception {
        try (FileInputStream keyStoreFileInputStream = new FileInputStream(ResourceUtils.getFile("classpath:identity.jks"))) {
            KeyStore keyStore = KeyStore.getInstance("jks");
            keyStore.load(keyStoreFileInputStream, "secret".toCharArray());

            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            keyManagerFactory.init(keyStore, "secret".toCharArray());

            return keyManagerFactory;
        }
    }

    private TrustManagerFactory getTrustManagerFactory() throws Exception {
        try (FileInputStream trustStoreFileInputStream = new FileInputStream(ResourceUtils.getFile("classpath:truststore.jks"))) {
            KeyStore keyStore = KeyStore.getInstance("jks");
            keyStore.load(trustStoreFileInputStream, "secret".toCharArray());

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance("SunX509");
            trustManagerFactory.init(keyStore);

            return trustManagerFactory;
        }

    }

}
