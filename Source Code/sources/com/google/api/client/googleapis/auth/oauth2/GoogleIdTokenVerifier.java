package com.google.api.client.googleapis.auth.oauth2;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.JsonParser;
import com.google.api.client.json.JsonToken;
import com.google.api.client.util.Clock;
import com.google.api.client.util.StringUtils;
import com.google.common.base.Preconditions;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GoogleIdTokenVerifier {
    private static final Pattern MAX_AGE_PATTERN = Pattern.compile("\\s*max-age\\s*=\\s*(\\d+)\\s*");
    private Set<String> clientIds;
    private final Clock clock;
    private long expirationTimeMilliseconds;
    private final JsonFactory jsonFactory;
    private final Lock lock;
    private List<PublicKey> publicKeys;
    private final HttpTransport transport;

    public static class Builder {
        private Set<String> clientIds = new HashSet();
        private final JsonFactory jsonFactory;
        private final HttpTransport transport;

        public Builder(HttpTransport transport, JsonFactory jsonFactory) {
            this.transport = transport;
            this.jsonFactory = jsonFactory;
        }

        public GoogleIdTokenVerifier build() {
            return new GoogleIdTokenVerifier(this.clientIds, this.transport, this.jsonFactory);
        }

        public final HttpTransport getTransport() {
            return this.transport;
        }

        public final JsonFactory getJsonFactory() {
            return this.jsonFactory;
        }

        public final Set<String> getClientIds() {
            return this.clientIds;
        }

        public Builder setClientIds(Iterable<String> clientIds) {
            this.clientIds.clear();
            for (String clientId : clientIds) {
                this.clientIds.add(clientId);
            }
            return this;
        }

        public Builder setClientIds(String... clientIds) {
            this.clientIds.clear();
            Collections.addAll(this.clientIds, clientIds);
            return this;
        }
    }

    @Deprecated
    public GoogleIdTokenVerifier(HttpTransport transport, JsonFactory jsonFactory, String clientId) {
        this(clientId == null ? Collections.emptySet() : Collections.singleton(clientId), transport, jsonFactory);
    }

    public GoogleIdTokenVerifier(HttpTransport transport, JsonFactory jsonFactory) {
        this(null, transport, jsonFactory);
    }

    protected GoogleIdTokenVerifier(Set<String> clientIds, HttpTransport transport, JsonFactory jsonFactory) {
        this(clientIds, transport, jsonFactory, Clock.SYSTEM);
    }

    protected GoogleIdTokenVerifier(Set<String> clientIds, HttpTransport transport, JsonFactory jsonFactory, Clock clock) {
        this.lock = new ReentrantLock();
        this.clientIds = clientIds == null ? Collections.emptySet() : Collections.unmodifiableSet(clientIds);
        this.transport = (HttpTransport) Preconditions.checkNotNull(transport);
        this.jsonFactory = (JsonFactory) Preconditions.checkNotNull(jsonFactory);
        this.clock = (Clock) Preconditions.checkNotNull(clock);
    }

    public final JsonFactory getJsonFactory() {
        return this.jsonFactory;
    }

    @Deprecated
    public final String getClientId() {
        return (String) this.clientIds.iterator().next();
    }

    public final Set<String> getClientIds() {
        return this.clientIds;
    }

    public final List<PublicKey> getPublicKeys() {
        return this.publicKeys;
    }

    public final long getExpirationTimeMilliseconds() {
        return this.expirationTimeMilliseconds;
    }

    public boolean verify(GoogleIdToken idToken) throws GeneralSecurityException, IOException {
        return verify(this.clientIds, idToken);
    }

    public GoogleIdToken verify(String idTokenString) throws GeneralSecurityException, IOException {
        GoogleIdToken idToken = GoogleIdToken.parse(this.jsonFactory, idTokenString);
        return verify(idToken) ? idToken : null;
    }

    public boolean verify(GoogleIdToken idToken, String clientId) throws GeneralSecurityException, IOException {
        return verify(clientId == null ? Collections.emptySet() : Collections.singleton(clientId), idToken);
    }

    public boolean verify(Set<String> clientIds, GoogleIdToken idToken) throws GeneralSecurityException, IOException {
        Payload payload = idToken.getPayload();
        if (!payload.isValidTime(300) || !"accounts.google.com".equals(payload.getIssuer())) {
            return false;
        }
        if ((!clientIds.isEmpty() && (!clientIds.contains(payload.getAudience()) || !clientIds.contains(payload.getIssuee()))) || !idToken.getHeader().getAlgorithm().equals("RS256")) {
            return false;
        }
        this.lock.lock();
        try {
            if (this.publicKeys == null || this.clock.currentTimeMillis() + 300000 > this.expirationTimeMilliseconds) {
                loadPublicCerts();
            }
            Signature signer = Signature.getInstance("SHA256withRSA");
            for (PublicKey publicKey : this.publicKeys) {
                signer.initVerify(publicKey);
                signer.update(idToken.getSignedContentBytes());
                if (signer.verify(idToken.getSignatureBytes())) {
                    return true;
                }
            }
            this.lock.unlock();
            return false;
        } finally {
            this.lock.unlock();
        }
    }

    public GoogleIdTokenVerifier loadPublicCerts() throws GeneralSecurityException, IOException {
        JsonParser parser;
        this.lock.lock();
        try {
            this.publicKeys = new ArrayList();
            CertificateFactory factory = CertificateFactory.getInstance("X509");
            HttpResponse certsResponse = this.transport.createRequestFactory().buildGetRequest(new GenericUrl("https://www.googleapis.com/oauth2/v1/certs")).execute();
            for (String arg : certsResponse.getHeaders().getCacheControl().split(",")) {
                Matcher m = MAX_AGE_PATTERN.matcher(arg);
                if (m.matches()) {
                    this.expirationTimeMilliseconds = this.clock.currentTimeMillis() + (Long.valueOf(m.group(1)).longValue() * 1000);
                    break;
                }
            }
            parser = this.jsonFactory.createJsonParser(certsResponse.getContent());
            JsonToken currentToken = parser.getCurrentToken();
            if (currentToken == null) {
                currentToken = parser.nextToken();
            }
            Preconditions.checkArgument(currentToken == JsonToken.START_OBJECT);
            while (parser.nextToken() != JsonToken.END_OBJECT) {
                parser.nextToken();
                this.publicKeys.add(((X509Certificate) factory.generateCertificate(new ByteArrayInputStream(StringUtils.getBytesUtf8(parser.getText())))).getPublicKey());
            }
            this.publicKeys = Collections.unmodifiableList(this.publicKeys);
            parser.close();
            this.lock.unlock();
            return this;
        } catch (Throwable th) {
            this.lock.unlock();
        }
    }
}
