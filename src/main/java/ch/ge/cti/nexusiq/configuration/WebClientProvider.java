/*
 * Copyright (C) <Date> Republique et canton de Geneve
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ch.ge.cti.nexusiq.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

import static org.springframework.web.reactive.function.client.ExchangeFilterFunctions.basicAuthentication;

/**
 * An object responsible for creating a {@link WebClient}.
 * Allows, in the case of JUnit tests, to inject the URL (nexusServiceUrl) of a mock server.
 */
@Component
@Slf4j
public class WebClientProvider {

    private final WebClient webClient;

    public WebClientProvider(

            @Value("${app.nexus-iq.url}")
            String url,

            @Value("${app.nexus-iq.username}")
            String username,

            @Value("${app.nexus-iq.password}")
            String password
    )
            throws IOException, KeyStoreException, CertificateException, NoSuchAlgorithmException
    {
        log.info("URL NexusIQ = {}", url);

        var httpClient = HttpClient.create();
        int maxInMemorySize = 5 * 1024 * 1024;  // 5 MB

        webClient = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl(url)
                .filter(basicAuthentication(username, password))
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(maxInMemorySize))
                .build();
    }

    public WebClient getWebClient() {
        return webClient;
    }

}
