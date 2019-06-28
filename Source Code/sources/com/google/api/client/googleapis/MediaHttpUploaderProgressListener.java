package com.google.api.client.googleapis;

import java.io.IOException;

@Deprecated
public interface MediaHttpUploaderProgressListener {
    void progressChanged(MediaHttpUploader mediaHttpUploader) throws IOException;
}
