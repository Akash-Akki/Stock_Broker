package project.wpl.service;

import com.sun.net.ssl.KeyManagerFactory;
import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContextBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.tomcat.util.net.SSLUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
//
//@Configuration
//public class SslConfiguration {
//
//
//
////
////    @Bean
////    RestTemplate restTemplate() throws Exception {
//////
////        KeyStore trustStore = KeyStore.getInstance("jks");
////
////        X509Certificate serverCert = null;
////        CertificateFactory certificateFactory = null;
////        FileInputStream fileInputStream =new FileInputStream("wplProject.p12");
////        certificateFactory = CertificateFactory.getInstance("X.509");
////        serverCert = (X509Certificate)(certificateFactory.generateCertificate(fileInputStream));
////        trustStore.setCertificateEntry("simulatorCert",serverCert);
////        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
////        trustManagerFactory.init(trustStore);
////
////        SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(trustStore).build();
////
////        SSLConnectionSocketFactory socketFactory =
////                new SSLConnectionSocketFactory(sslContext);
////        HttpClient httpClient = HttpClients.custom()
////                .setSSLSocketFactory(socketFactory).build();
////        HttpComponentsClientHttpRequestFactory factory =
////                new HttpComponentsClientHttpRequestFactory(httpClient);
////        return new RestTemplate(factory);
//    }
//}
