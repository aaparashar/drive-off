package com.google.api.client.http;

import com.google.api.client.util.ArrayValueMap;
import com.google.api.client.util.Base64;
import com.google.api.client.util.ClassInfo;
import com.google.api.client.util.Data;
import com.google.api.client.util.FieldInfo;
import com.google.api.client.util.GenericData;
import com.google.api.client.util.GenericData.Flags;
import com.google.api.client.util.Key;
import com.google.api.client.util.StringUtils;
import com.google.api.client.util.Types;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HttpHeaders extends GenericData {
    @Key("Accept")
    private String accept;
    @Key("Accept-Encoding")
    private String acceptEncoding = "gzip";
    @Key("WWW-Authenticate")
    private String authenticate;
    @Key("Authorization")
    private String authorization;
    @Key("Cache-Control")
    private String cacheControl;
    @Key("Content-Encoding")
    private String contentEncoding;
    @Key("Content-Length")
    private Long contentLength;
    @Key("Content-MD5")
    private String contentMD5;
    @Key("Content-Range")
    private String contentRange;
    @Key("Content-Type")
    private String contentType;
    @Key("Cookie")
    private String cookie;
    @Key("Date")
    private String date;
    @Key("ETag")
    private String etag;
    @Key("Expires")
    private String expires;
    @Key("If-Match")
    private String ifMatch;
    @Key("If-Modified-Since")
    private String ifModifiedSince;
    @Key("If-None-Match")
    private String ifNoneMatch;
    @Key("If-Unmodified-Since")
    private String ifUnmodifiedSince;
    @Key("Last-Modified")
    private String lastModified;
    @Key("Location")
    private String location;
    @Key("MIME-Version")
    private String mimeVersion;
    @Key("Range")
    private String range;
    @Key("Retry-After")
    private String retryAfter;
    @Key("User-Agent")
    private String userAgent;

    private static final class ParseHeaderState {
        final ArrayValueMap arrayValueMap;
        final ClassInfo classInfo;
        final List<Type> context;
        final StringBuilder logger;

        public ParseHeaderState(HttpHeaders headers, StringBuilder logger) {
            this.context = Arrays.asList(new Type[]{headers.getClass()});
            this.classInfo = ClassInfo.of(clazz, true);
            this.logger = logger;
            this.arrayValueMap = new ArrayValueMap(headers);
        }

        void finish() {
            this.arrayValueMap.setValues();
        }
    }

    private static class HeaderParsingFakeLevelHttpRequest extends LowLevelHttpRequest {
        private final ParseHeaderState state;
        private final HttpHeaders target;

        HeaderParsingFakeLevelHttpRequest(HttpHeaders target, ParseHeaderState state) {
            this.target = target;
            this.state = state;
        }

        public void addHeader(String name, String value) {
            this.target.parseHeader(name, value, this.state);
        }

        public void setContent(HttpContent content) throws IOException {
            throw new UnsupportedOperationException();
        }

        public LowLevelHttpResponse execute() throws IOException {
            throw new UnsupportedOperationException();
        }
    }

    public HttpHeaders() {
        super(EnumSet.of(Flags.IGNORE_CASE));
    }

    public HttpHeaders clone() {
        return (HttpHeaders) super.clone();
    }

    public final String getAccept() {
        return this.accept;
    }

    public final void setAccept(String accept) {
        this.accept = accept;
    }

    public final String getAcceptEncoding() {
        return this.acceptEncoding;
    }

    public final void setAcceptEncoding(String acceptEncoding) {
        this.acceptEncoding = acceptEncoding;
    }

    public final String getAuthorization() {
        return this.authorization;
    }

    public final void setAuthorization(String authorization) {
        this.authorization = authorization;
    }

    public final String getCacheControl() {
        return this.cacheControl;
    }

    public final void setCacheControl(String cacheControl) {
        this.cacheControl = cacheControl;
    }

    public final String getContentEncoding() {
        return this.contentEncoding;
    }

    public final void setContentEncoding(String contentEncoding) {
        this.contentEncoding = contentEncoding;
    }

    public final Long getContentLength() {
        return this.contentLength;
    }

    public final void setContentLength(Long contentLength) {
        this.contentLength = contentLength;
    }

    public final String getContentMD5() {
        return this.contentMD5;
    }

    public final void setContentMD5(String contentMD5) {
        this.contentMD5 = contentMD5;
    }

    public final String getContentRange() {
        return this.contentRange;
    }

    public final void setContentRange(String contentRange) {
        this.contentRange = contentRange;
    }

    public final String getContentType() {
        return this.contentType;
    }

    public final void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public final String getCookie() {
        return this.cookie;
    }

    public final void setCookie(String cookie) {
        this.cookie = cookie;
    }

    public final String getDate() {
        return this.date;
    }

    public final void setDate(String date) {
        this.date = date;
    }

    public final String getETag() {
        return this.etag;
    }

    public final void setETag(String etag) {
        this.etag = etag;
    }

    public final String getExpires() {
        return this.expires;
    }

    public final void setExpires(String expires) {
        this.expires = expires;
    }

    public final String getIfModifiedSince() {
        return this.ifModifiedSince;
    }

    public final void setIfModifiedSince(String ifModifiedSince) {
        this.ifModifiedSince = ifModifiedSince;
    }

    public final String getIfMatch() {
        return this.ifMatch;
    }

    public final void setIfMatch(String ifMatch) {
        this.ifMatch = ifMatch;
    }

    public final String getIfNoneMatch() {
        return this.ifNoneMatch;
    }

    public final void setIfNoneMatch(String ifNoneMatch) {
        this.ifNoneMatch = ifNoneMatch;
    }

    public final String getIfUnmodifiedSince() {
        return this.ifUnmodifiedSince;
    }

    public final void setIfUnmodifiedSince(String ifUnmodifiedSince) {
        this.ifUnmodifiedSince = ifUnmodifiedSince;
    }

    public final String getLastModified() {
        return this.lastModified;
    }

    public final void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public final String getLocation() {
        return this.location;
    }

    public final void setLocation(String location) {
        this.location = location;
    }

    public final String getMimeVersion() {
        return this.mimeVersion;
    }

    public final void setMimeVersion(String mimeVersion) {
        this.mimeVersion = mimeVersion;
    }

    public final String getRange() {
        return this.range;
    }

    public final void setRange(String range) {
        this.range = range;
    }

    public final String getRetryAfter() {
        return this.retryAfter;
    }

    public final void setRetryAfter(String retryAfter) {
        this.retryAfter = retryAfter;
    }

    public final String getUserAgent() {
        return this.userAgent;
    }

    public final void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public final String getAuthenticate() {
        return this.authenticate;
    }

    public final void setAuthenticate(String authenticate) {
        this.authenticate = authenticate;
    }

    public final void setBasicAuthentication(String username, String password) {
        this.authorization = "Basic " + Base64.encodeBase64String(StringUtils.getBytesUtf8(((String) Preconditions.checkNotNull(username)) + ":" + ((String) Preconditions.checkNotNull(password))));
    }

    private static void addHeader(Logger logger, StringBuilder logbuf, LowLevelHttpRequest lowLevelHttpRequest, String name, Object value, Writer writer) throws IOException {
        if (value != null && !Data.isNull(value)) {
            String stringValue = value instanceof Enum ? FieldInfo.of((Enum) value).getName() : value.toString();
            if (logbuf != null) {
                logbuf.append(name).append(": ");
                if (!com.google.common.net.HttpHeaders.AUTHORIZATION.equalsIgnoreCase(name) || logger.isLoggable(Level.ALL)) {
                    logbuf.append(stringValue);
                } else {
                    logbuf.append("<Not Logged>");
                }
                logbuf.append(StringUtils.LINE_SEPARATOR);
            }
            if (lowLevelHttpRequest != null) {
                lowLevelHttpRequest.addHeader(name, stringValue);
            }
            if (writer != null) {
                writer.write(name);
                writer.write(": ");
                writer.write(stringValue);
                writer.write("\r\n");
            }
        }
    }

    public static void serializeHeaders(HttpHeaders headers, StringBuilder logbuf, Logger logger, LowLevelHttpRequest lowLevelHttpRequest) throws IOException {
        serializeHeaders(headers, logbuf, logger, lowLevelHttpRequest, null);
    }

    public static void serializeHeadersForMultipartRequests(HttpHeaders headers, StringBuilder logbuf, Logger logger, Writer writer) throws IOException {
        serializeHeaders(headers, logbuf, logger, null, writer);
    }

    private static void serializeHeaders(HttpHeaders headers, StringBuilder logbuf, Logger logger, LowLevelHttpRequest lowLevelHttpRequest, Writer writer) throws IOException {
        HashSet<String> headerNames = new HashSet();
        for (Entry<String, Object> headerEntry : headers.entrySet()) {
            String name = (String) headerEntry.getKey();
            Preconditions.checkArgument(headerNames.add(name), "multiple headers of the same name (headers are case insensitive): %s", name);
            Object value = headerEntry.getValue();
            if (value != null) {
                String displayName = name;
                FieldInfo fieldInfo = headers.getClassInfo().getFieldInfo(name);
                if (fieldInfo != null) {
                    displayName = fieldInfo.getName();
                }
                Class<? extends Object> valueClass = value.getClass();
                if ((value instanceof Iterable) || valueClass.isArray()) {
                    for (Object repeatedValue : Types.iterableOf(value)) {
                        addHeader(logger, logbuf, lowLevelHttpRequest, displayName, repeatedValue, writer);
                    }
                } else {
                    addHeader(logger, logbuf, lowLevelHttpRequest, displayName, value, writer);
                }
            }
        }
        if (writer != null) {
            writer.flush();
        }
    }

    public final void fromHttpResponse(LowLevelHttpResponse response, StringBuilder logger) {
        ParseHeaderState state = new ParseHeaderState(this, logger);
        int headerCount = response.getHeaderCount();
        for (int i = 0; i < headerCount; i++) {
            parseHeader(response.getHeaderName(i), response.getHeaderValue(i), state);
        }
        state.finish();
    }

    public final void fromHttpHeaders(HttpHeaders headers) {
        try {
            ParseHeaderState state = new ParseHeaderState(this, null);
            serializeHeaders(headers, null, null, new HeaderParsingFakeLevelHttpRequest(this, state));
            state.finish();
        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
    }

    void parseHeader(String headerName, String headerValue, ParseHeaderState state) {
        List<Type> context = state.context;
        ClassInfo classInfo = state.classInfo;
        ArrayValueMap arrayValueMap = state.arrayValueMap;
        StringBuilder logger = state.logger;
        if (logger != null) {
            logger.append(headerName + ": " + headerValue).append(StringUtils.LINE_SEPARATOR);
        }
        FieldInfo fieldInfo = classInfo.getFieldInfo(headerName);
        if (fieldInfo != null) {
            Type type = Data.resolveWildcardTypeOrTypeVariable(context, fieldInfo.getGenericType());
            if (Types.isArray(type)) {
                Class rawArrayComponentType = Types.getRawArrayComponentType(context, Types.getArrayComponentType(type));
                arrayValueMap.put(fieldInfo.getField(), rawArrayComponentType, parseValue(rawArrayComponentType, context, headerValue));
                return;
            } else if (Types.isAssignableToOrFrom(Types.getRawArrayComponentType(context, type), Iterable.class)) {
                Collection<Object> collection = (Collection) fieldInfo.getValue(this);
                if (collection == null) {
                    collection = Data.newCollectionInstance(type);
                    fieldInfo.setValue(this, collection);
                }
                collection.add(parseValue(type == Object.class ? null : Types.getIterableParameter(type), context, headerValue));
                return;
            } else {
                fieldInfo.setValue(this, parseValue(type, context, headerValue));
                return;
            }
        }
        ArrayList<String> listValue = (ArrayList) get(headerName);
        if (listValue == null) {
            listValue = new ArrayList();
            set(headerName, listValue);
        }
        listValue.add(headerValue);
    }

    private static Object parseValue(Type valueType, List<Type> context, String value) {
        return Data.parsePrimitiveValue(Data.resolveWildcardTypeOrTypeVariable(context, valueType), value);
    }
}
