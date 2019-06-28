package com.google.api.client.http;

import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public final class MultipartRelatedContent extends AbstractHttpContent {
    private static final byte[] CONTENT_TRANSFER_ENCODING = "Content-Transfer-Encoding: binary".getBytes();
    private static final byte[] CONTENT_TYPE = "Content-Type: ".getBytes();
    private static final byte[] CR_LF = "\r\n".getBytes();
    private static final byte[] TWO_DASHES = "--".getBytes();
    private final Collection<HttpContent> parts;

    public MultipartRelatedContent(HttpContent firstPart, HttpContent... otherParts) {
        super(new HttpMediaType("multipart/related").setParameter("boundary", "END_OF_PART"));
        List<HttpContent> parts = new ArrayList(otherParts.length + 1);
        parts.add(firstPart);
        parts.addAll(Arrays.asList(otherParts));
        this.parts = parts;
    }

    public void forRequest(HttpRequest request) {
        request.setContent(this);
        request.getHeaders().setMimeVersion("1.0");
    }

    public void writeTo(OutputStream out) throws IOException {
        byte[] boundaryBytes = getBoundary().getBytes();
        out.write(TWO_DASHES);
        out.write(boundaryBytes);
        for (HttpContent part : this.parts) {
            String contentType = part.getType();
            if (contentType != null) {
                byte[] typeBytes = contentType.getBytes();
                out.write(CR_LF);
                out.write(CONTENT_TYPE);
                out.write(typeBytes);
            }
            out.write(CR_LF);
            if (!isTextBasedContentType(contentType)) {
                out.write(CONTENT_TRANSFER_ENCODING);
                out.write(CR_LF);
            }
            out.write(CR_LF);
            part.writeTo(out);
            out.write(CR_LF);
            out.write(TWO_DASHES);
            out.write(boundaryBytes);
        }
        out.write(TWO_DASHES);
        out.flush();
    }

    public long computeLength() throws IOException {
        byte[] boundaryBytes = getBoundary().getBytes();
        long result = (long) ((TWO_DASHES.length * 2) + boundaryBytes.length);
        for (HttpContent part : this.parts) {
            long length = part.getLength();
            if (length < 0) {
                return -1;
            }
            String contentType = part.getType();
            if (contentType != null) {
                result += (long) ((CR_LF.length + CONTENT_TYPE.length) + contentType.getBytes().length);
            }
            if (!isTextBasedContentType(contentType)) {
                result += (long) (CONTENT_TRANSFER_ENCODING.length + CR_LF.length);
            }
            result += ((((long) (CR_LF.length * 3)) + length) + ((long) TWO_DASHES.length)) + ((long) boundaryBytes.length);
        }
        return result;
    }

    public boolean retrySupported() {
        for (HttpContent onePart : this.parts) {
            if (!onePart.retrySupported()) {
                return false;
            }
        }
        return true;
    }

    public MultipartRelatedContent setMediaType(HttpMediaType mediaType) {
        super.setMediaType(mediaType);
        return this;
    }

    public String getBoundary() {
        return getMediaType().getParameter("boundary");
    }

    public MultipartRelatedContent setBoundary(String boundary) {
        getMediaType().setParameter("boundary", (String) Preconditions.checkNotNull(boundary));
        return this;
    }

    public Collection<HttpContent> getParts() {
        return Collections.unmodifiableCollection(this.parts);
    }

    private static boolean isTextBasedContentType(String contentType) {
        if (contentType == null) {
            return false;
        }
        HttpMediaType hmt = new HttpMediaType(contentType);
        if (hmt.getType().equals(PropertyTypeConstants.PROPERTY_TYPE_TEXT) || hmt.getType().equals("application")) {
            return true;
        }
        return false;
    }
}
