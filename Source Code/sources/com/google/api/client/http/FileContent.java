package com.google.api.client.http;

import com.google.common.base.Preconditions;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public final class FileContent extends AbstractInputStreamContent {
    private final File file;

    public FileContent(String type, File file) {
        super(type);
        this.file = (File) Preconditions.checkNotNull(file);
    }

    public long getLength() {
        return this.file.length();
    }

    public boolean retrySupported() {
        return true;
    }

    public InputStream getInputStream() throws FileNotFoundException {
        return new FileInputStream(this.file);
    }

    public File getFile() {
        return this.file;
    }

    public FileContent setEncoding(String encoding) {
        return (FileContent) super.setEncoding(encoding);
    }

    public FileContent setType(String type) {
        return (FileContent) super.setType(type);
    }

    public FileContent setCloseInputStream(boolean closeInputStream) {
        return (FileContent) super.setCloseInputStream(closeInputStream);
    }
}
