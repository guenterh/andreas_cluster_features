package org.swissbib.ml.utilities;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContextBuilder;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Optional;
import java.util.Scanner;

public class FetchSRUJava {

    private String elem;

    public FetchSRUJava(String elem) {
        this.elem = elem;
    }

    public Optional<String> fetch() {

        String query = String.format("https://sru.swissbib.ch/sru/search/defaultdb?query=dc.anywhere=%s&operation=searchRetrieve&recordSchema=info:srw/schema/1/marcxml-v1.1-light&maximumRecords=10&x-info-10-get-holdings=true&startRecord=0&recordPacking=XML&availableDBs=defaultdb",this.elem);

        Optional<String> response = Optional.empty();

        try {

            HttpClientBuilder b = HttpClientBuilder.create();

            // setup a Trust Strategy that allows all certificates.
            //

            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                    return true;
                }
            }).build();
            b.setSSLContext(sslContext);

            // don't check Hostnames, either.
            //      -- use SSLConnectionSocketFactory.getDefaultHostnameVerifier(), if you don't want to weaken
            HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;

            // here's the special part:
            //      -- need to create an SSL Socket Factory, to use our weakened "trust strategy";
            //      -- and create a Registry, to register it.
            //
            SSLConnectionSocketFactory sslSocketFactory = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create().register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", sslSocketFactory).build();

            // now, we create connection-manager using our Registry.
            //      -- allows multi-threaded use
            PoolingHttpClientConnectionManager connMgr = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
            b.setConnectionManager(connMgr);

            // finally, build the HttpClient;
            //      -- done!
            HttpClient client = b.build();

            HttpGet get = new HttpGet(query);
            //get.setFollowRedirects(true);

            HttpResponse httpResponse = client.execute(get);
            HttpEntity entity = httpResponse.getEntity();
            InputStream inputStream = entity.getContent();

            Scanner sc = new Scanner(inputStream);
            //Reading line by line from scanner to StringBuffer
            StringBuilder sb = new StringBuilder();
            while(sc.hasNext()){
                sb.append(sc.nextLine());
            }

            response = Optional.of(sb.toString());


        } catch (NoSuchAlgorithmException | IOException | KeyStoreException | KeyManagementException ex) {

            ex.printStackTrace();

        }

        return response;
        //return Optional.empty();


    }

}
