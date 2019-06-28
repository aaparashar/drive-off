package com.google.api.client.http.apache;

import com.google.api.client.http.HttpTransport;
import org.apache.http.HttpVersion;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRouteBean;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;

public final class ApacheHttpTransport extends HttpTransport {
    private final HttpClient httpClient;

    public ApacheHttpTransport() {
        this(newDefaultHttpClient());
    }

    public ApacheHttpTransport(HttpClient httpClient) {
        this.httpClient = httpClient;
        HttpParams params = httpClient.getParams();
        HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
        params.setBooleanParameter("http.protocol.handle-redirects", false);
    }

    public static DefaultHttpClient newDefaultHttpClient() {
        HttpParams params = new BasicHttpParams();
        HttpConnectionParams.setStaleCheckingEnabled(params, false);
        HttpConnectionParams.setSocketBufferSize(params, 8192);
        ConnManagerParams.setMaxTotalConnections(params, 200);
        ConnManagerParams.setMaxConnectionsPerRoute(params, new ConnPerRouteBean(20));
        SchemeRegistry registry = new SchemeRegistry();
        registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        registry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
        DefaultHttpClient defaultHttpClient = new DefaultHttpClient(new ThreadSafeClientConnManager(params, registry), params);
        defaultHttpClient.setHttpRequestRetryHandler(new DefaultHttpRequestRetryHandler(0, false));
        return defaultHttpClient;
    }

    public boolean supportsHead() {
        return true;
    }

    public boolean supportsPatch() {
        return true;
    }

    public ApacheHttpRequest buildDeleteRequest(String url) {
        return new ApacheHttpRequest(this.httpClient, new HttpDelete(url));
    }

    public ApacheHttpRequest buildGetRequest(String url) {
        return new ApacheHttpRequest(this.httpClient, new HttpGet(url));
    }

    public ApacheHttpRequest buildHeadRequest(String url) {
        return new ApacheHttpRequest(this.httpClient, new HttpHead(url));
    }

    public ApacheHttpRequest buildPatchRequest(String url) {
        return new ApacheHttpRequest(this.httpClient, new HttpPatch(url));
    }

    public ApacheHttpRequest buildPostRequest(String url) {
        return new ApacheHttpRequest(this.httpClient, new HttpPost(url));
    }

    public ApacheHttpRequest buildPutRequest(String url) {
        return new ApacheHttpRequest(this.httpClient, new HttpPut(url));
    }

    public void shutdown() {
        this.httpClient.getConnectionManager().shutdown();
    }

    public HttpClient getHttpClient() {
        return this.httpClient;
    }
}
