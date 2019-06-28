package com.google.appinventor.components.runtime.util;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import com.google.appinventor.components.runtime.ReplForm;
import com.google.appinventor.components.runtime.util.NanoHTTPD.Response;
import com.google.common.net.HttpHeaders;
import gnu.expr.Language;
import gnu.expr.ModuleExp;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Enumeration;
import java.util.Formatter;
import java.util.Properties;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class AppInvHTTPD extends NanoHTTPD {
    private static final String LOG_TAG = "AppInvHTTPD";
    private static final String MIME_JSON = "application/json";
    private static final int YAV_SKEW_BACKWARD = 4;
    private static final int YAV_SKEW_FORWARD = 1;
    private static byte[] hmacKey;
    private static int seq;
    private final Handler androidUIHandler = new Handler();
    private ReplForm form;
    private File rootDir;
    private Language scheme;
    private boolean secure;

    /* renamed from: com.google.appinventor.components.runtime.util.AppInvHTTPD$1 */
    class C01361 implements Runnable {
        C01361() {
        }

        public void run() {
            AppInvHTTPD.this.form.clear();
        }
    }

    public AppInvHTTPD(int port, File wwwroot, boolean secure, ReplForm form) throws IOException {
        super(port, wwwroot);
        this.rootDir = wwwroot;
        this.scheme = Language.getInstance("scheme");
        this.form = form;
        this.secure = secure;
        ModuleExp.mustNeverCompile();
    }

    public Response serve(String uri, String method, Properties header, Properties parms, Properties files, Socket mySocket) {
        Log.d(LOG_TAG, method + " '" + uri + "' ");
        if (this.secure) {
            String hostAddress = mySocket.getInetAddress().getHostAddress();
            if (!hostAddress.equals("127.0.0.1")) {
                Log.d(LOG_TAG, "Debug: hostAddress = " + hostAddress + " while in secure mode, closing connection.");
                Response response = new Response(NanoHTTPD.HTTP_OK, "application/json", "{\"status\" : \"BAD\", \"message\" : \"Security Error: Invalid Source Location " + hostAddress + "\"}");
                response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
                response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "origin, content-type");
                response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "POST,OPTIONS,GET,HEAD,PUT");
                response.addHeader(HttpHeaders.ALLOW, "POST,OPTIONS,GET,HEAD,PUT");
                return response;
            }
        }
        Enumeration e;
        String value;
        if (method.equals("OPTIONS")) {
            e = header.propertyNames();
            while (e.hasMoreElements()) {
                value = (String) e.nextElement();
                Log.d(LOG_TAG, "  HDR: '" + value + "' = '" + header.getProperty(value) + "'");
            }
            response = new Response(NanoHTTPD.HTTP_OK, NanoHTTPD.MIME_PLAINTEXT, "OK");
            response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
            response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "origin, content-type");
            response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "POST,OPTIONS,GET,HEAD,PUT");
            response.addHeader(HttpHeaders.ALLOW, "POST,OPTIONS,GET,HEAD,PUT");
            return response;
        } else if (uri.equals("/_newblocks")) {
            String inSeq = parms.getProperty("seq", "0");
            int iseq = Integer.parseInt(inSeq);
            String blockid = parms.getProperty("blockid");
            String code = parms.getProperty("code");
            inMac = parms.getProperty("mac", "no key provided");
            compMac = "";
            String input_code = code;
            if (hmacKey != null) {
                try {
                    hmacSha1 = Mac.getInstance("HmacSHA1");
                    hmacSha1.init(new SecretKeySpec(hmacKey, "RAW"));
                    tmpMac = hmacSha1.doFinal((code + inSeq + blockid).getBytes());
                    r0 = new StringBuffer(tmpMac.length * 2);
                    r0 = new Formatter(r0);
                    len$ = tmpMac.length;
                    for (i$ = 0; i$ < len$; i$++) {
                        r0.format("%02x", new Object[]{Byte.valueOf(arr$[i$])});
                    }
                    compMac = r0.toString();
                    Log.d(LOG_TAG, "Incoming Mac = " + inMac);
                    Log.d(LOG_TAG, "Computed Mac = " + compMac);
                    Log.d(LOG_TAG, "Incoming seq = " + inSeq);
                    Log.d(LOG_TAG, "Computed seq = " + seq);
                    Log.d(LOG_TAG, "blockid = " + blockid);
                    if (!inMac.equals(compMac)) {
                        Log.e(LOG_TAG, "Hmac does not match");
                        this.form.dispatchErrorOccurredEvent(this.form, LOG_TAG, ErrorMessages.ERROR_REPL_SECURITY_ERROR, "Invalid HMAC");
                        return new Response(NanoHTTPD.HTTP_OK, "application/json", "{\"status\" : \"BAD\", \"message\" : \"Security Error: Invalid MAC\"}");
                    } else if (seq == iseq || seq == iseq + 1) {
                        if (seq == iseq + 1) {
                            Log.e(LOG_TAG, "Seq Fixup Invoked");
                        }
                        seq = iseq + 1;
                        code = "(begin (require <com.google.youngandroid.runtime>) (process-repl-input " + blockid + " (begin " + code + " )))";
                        Log.d(LOG_TAG, "To Eval: " + code);
                        try {
                            if (input_code.equals("#f")) {
                                Log.e(LOG_TAG, "Skipping evaluation of #f");
                            } else {
                                this.scheme.eval(code);
                            }
                            response = new Response(NanoHTTPD.HTTP_OK, "application/json", RetValManager.fetch(false));
                        } catch (Throwable ex) {
                            Log.e(LOG_TAG, "newblocks: Scheme Failure", ex);
                            RetValManager.appendReturnValue(blockid, "BAD", ex.toString());
                            response = new Response(NanoHTTPD.HTTP_OK, "application/json", RetValManager.fetch(false));
                        }
                        res.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
                        res.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "origin, content-type");
                        res.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "POST,OPTIONS,GET,HEAD,PUT");
                        res.addHeader(HttpHeaders.ALLOW, "POST,OPTIONS,GET,HEAD,PUT");
                        return res;
                    } else {
                        Log.e(LOG_TAG, "Seq does not match");
                        this.form.dispatchErrorOccurredEvent(this.form, LOG_TAG, ErrorMessages.ERROR_REPL_SECURITY_ERROR, "Invalid Seq");
                        return new Response(NanoHTTPD.HTTP_OK, "application/json", "{\"status\" : \"BAD\", \"message\" : \"Security Error: Invalid Seq\"}");
                    }
                } catch (Exception e2) {
                    Log.e(LOG_TAG, "Error working with hmac", e2);
                    this.form.dispatchErrorOccurredEvent(this.form, LOG_TAG, ErrorMessages.ERROR_REPL_SECURITY_ERROR, "Exception working on HMAC");
                    return new Response(NanoHTTPD.HTTP_OK, NanoHTTPD.MIME_PLAINTEXT, "NOT");
                }
            }
            Log.e(LOG_TAG, "No HMAC Key");
            this.form.dispatchErrorOccurredEvent(this.form, LOG_TAG, ErrorMessages.ERROR_REPL_SECURITY_ERROR, "No HMAC Key");
            return new Response(NanoHTTPD.HTTP_OK, "application/json", "{\"status\" : \"BAD\", \"message\" : \"Security Error: No HMAC Key\"}");
        } else if (uri.equals("/_values")) {
            response = new Response(NanoHTTPD.HTTP_OK, "application/json", RetValManager.fetch(true));
            response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
            response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "origin, content-type");
            response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "POST,OPTIONS,GET,HEAD,PUT");
            response.addHeader(HttpHeaders.ALLOW, "POST,OPTIONS,GET,HEAD,PUT");
            return response;
        } else if (uri.equals("/_getversion")) {
            try {
                String installer;
                String packageName = this.form.getPackageName();
                PackageInfo pInfo = this.form.getPackageManager().getPackageInfo(packageName, 0);
                if (SdkLevel.getLevel() >= 5) {
                    installer = EclairUtil.getInstallerPackageName("edu.mit.appinventor.aicompanion3", this.form);
                } else {
                    installer = "Not Known";
                }
                String versionName = pInfo.versionName;
                if (installer == null) {
                    installer = "Not Known";
                }
                response = new Response(NanoHTTPD.HTTP_OK, "application/json", "{\"version\" : \"" + versionName + "\", \"fingerprint\" : \"" + Build.FINGERPRINT + "\"," + " \"installer\" : \"" + installer + "\", \"package\" : \"" + packageName + "\" }");
            } catch (NameNotFoundException n) {
                n.printStackTrace();
                response = new Response(NanoHTTPD.HTTP_OK, "application/json", "{\"verison\" : \"Unknown\"");
            }
            res.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
            res.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "origin, content-type");
            res.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "POST,OPTIONS,GET,HEAD,PUT");
            res.addHeader(HttpHeaders.ALLOW, "POST,OPTIONS,GET,HEAD,PUT");
            if (!this.secure) {
                return res;
            }
            seq = 1;
            this.androidUIHandler.post(new C01361());
            return res;
        } else if (uri.equals("/_update") || uri.equals("/_install")) {
            String url = parms.getProperty("url", "");
            inMac = parms.getProperty("mac", "");
            if (url.equals("") || hmacKey == null || inMac.equals("")) {
                response = new Response(NanoHTTPD.HTTP_OK, "application/json", "{\"status\" : \"BAD\", \"message\" : \"Missing Parameters\"}");
                response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
                response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "origin, content-type");
                response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "POST,OPTIONS,GET,HEAD,PUT");
                response.addHeader(HttpHeaders.ALLOW, "POST,OPTIONS,GET,HEAD,PUT");
                return response;
            }
            try {
                SecretKeySpec secretKeySpec = new SecretKeySpec(hmacKey, "RAW");
                hmacSha1 = Mac.getInstance("HmacSHA1");
                hmacSha1.init(secretKeySpec);
                tmpMac = hmacSha1.doFinal(url.getBytes());
                r0 = new StringBuffer(tmpMac.length * 2);
                r0 = new Formatter(r0);
                len$ = tmpMac.length;
                for (i$ = 0; i$ < len$; i$++) {
                    r0.format("%02x", new Object[]{Byte.valueOf(arr$[i$])});
                }
                compMac = r0.toString();
                Log.d(LOG_TAG, "Incoming Mac (update) = " + inMac);
                Log.d(LOG_TAG, "Computed Mac (update) = " + compMac);
                if (inMac.equals(compMac)) {
                    doPackageUpdate(url);
                    response = new Response(NanoHTTPD.HTTP_OK, "application/json", "{\"status\" : \"OK\", \"message\" : \"Update Should Happen\"}");
                    response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
                    response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "origin, content-type");
                    response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "POST,OPTIONS,GET,HEAD,PUT");
                    response.addHeader(HttpHeaders.ALLOW, "POST,OPTIONS,GET,HEAD,PUT");
                    return response;
                }
                Log.e(LOG_TAG, "Hmac does not match");
                this.form.dispatchErrorOccurredEvent(this.form, LOG_TAG, ErrorMessages.ERROR_REPL_SECURITY_ERROR, "Invalid HMAC (update)");
                response = new Response(NanoHTTPD.HTTP_OK, "application/json", "{\"status\" : \"BAD\", \"message\" : \"Security Error: Invalid MAC\"}");
                response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
                response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "origin, content-type");
                response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "POST,OPTIONS,GET,HEAD,PUT");
                response.addHeader(HttpHeaders.ALLOW, "POST,OPTIONS,GET,HEAD,PUT");
                return response;
            } catch (Exception e22) {
                Log.e(LOG_TAG, "Error verifying update", e22);
                this.form.dispatchErrorOccurredEvent(this.form, LOG_TAG, ErrorMessages.ERROR_REPL_SECURITY_ERROR, "Exception working on HMAC for update");
                response = new Response(NanoHTTPD.HTTP_OK, "application/json", "{\"status\" : \"BAD\", \"message\" : \"Security Error: Exception processing MAC\"}");
                response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
                response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "origin, content-type");
                response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "POST,OPTIONS,GET,HEAD,PUT");
                response.addHeader(HttpHeaders.ALLOW, "POST,OPTIONS,GET,HEAD,PUT");
                return response;
            }
        } else if (uri.equals("/_package")) {
            String packageapk = parms.getProperty("package", null);
            if (packageapk == null) {
                return new Response(NanoHTTPD.HTTP_OK, NanoHTTPD.MIME_PLAINTEXT, "NOT OK");
            }
            Log.d(LOG_TAG, this.rootDir + "/" + packageapk);
            Intent intent = new Intent("android.intent.action.VIEW");
            intent.setDataAndType(Uri.fromFile(new File(this.rootDir + "/" + packageapk)), "application/vnd.android.package-archive");
            this.form.startActivity(intent);
            response = new Response(NanoHTTPD.HTTP_OK, NanoHTTPD.MIME_PLAINTEXT, "OK");
            response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
            response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "origin, content-type");
            response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "POST,OPTIONS,GET,HEAD,PUT");
            response.addHeader(HttpHeaders.ALLOW, "POST,OPTIONS,GET,HEAD,PUT");
            return response;
        } else if (method.equals("PUT")) {
            Boolean error = Boolean.valueOf(false);
            String tmpFileName = files.getProperty("content", null);
            if (tmpFileName != null) {
                fileFrom = new File(tmpFileName);
                filename = parms.getProperty("filename", null);
                if (filename != null && (filename.startsWith("..") || filename.endsWith("..") || filename.indexOf("../") >= 0)) {
                    Log.d(LOG_TAG, " Ignoring invalid filename: " + filename);
                    filename = null;
                }
                if (filename != null) {
                    fileTo = new File(this.rootDir + "/" + filename);
                    if (!fileFrom.renameTo(fileTo)) {
                        copyFile(fileFrom, fileTo);
                        fileFrom.delete();
                    }
                } else {
                    fileFrom.delete();
                    Log.e(LOG_TAG, "Received content without a file name!");
                    error = Boolean.valueOf(true);
                }
            } else {
                Log.e(LOG_TAG, "Received PUT without content.");
                error = Boolean.valueOf(true);
            }
            if (error.booleanValue()) {
                response = new Response(NanoHTTPD.HTTP_OK, NanoHTTPD.MIME_PLAINTEXT, "NOTOK");
                response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
                response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "origin, content-type");
                response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "POST,OPTIONS,GET,HEAD,PUT");
                response.addHeader(HttpHeaders.ALLOW, "POST,OPTIONS,GET,HEAD,PUT");
                return response;
            }
            response = new Response(NanoHTTPD.HTTP_OK, NanoHTTPD.MIME_PLAINTEXT, "OK");
            response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
            response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "origin, content-type");
            response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "POST,OPTIONS,GET,HEAD,PUT");
            response.addHeader(HttpHeaders.ALLOW, "POST,OPTIONS,GET,HEAD,PUT");
            return response;
        } else {
            e = header.propertyNames();
            while (e.hasMoreElements()) {
                value = (String) e.nextElement();
                Log.d(LOG_TAG, "  HDR: '" + value + "' = '" + header.getProperty(value) + "'");
            }
            e = parms.propertyNames();
            while (e.hasMoreElements()) {
                value = (String) e.nextElement();
                Log.d(LOG_TAG, "  PRM: '" + value + "' = '" + parms.getProperty(value) + "'");
            }
            e = files.propertyNames();
            if (e.hasMoreElements()) {
                String fieldname = (String) e.nextElement();
                String tempLocation = files.getProperty(fieldname);
                filename = parms.getProperty(fieldname);
                if (filename.startsWith("..") || filename.endsWith("..") || filename.indexOf("../") >= 0) {
                    Log.d(LOG_TAG, " Ignoring invalid filename: " + filename);
                    filename = null;
                }
                fileFrom = new File(tempLocation);
                if (filename == null) {
                    fileFrom.delete();
                } else {
                    fileTo = new File(this.rootDir + "/" + filename);
                    if (!fileFrom.renameTo(fileTo)) {
                        copyFile(fileFrom, fileTo);
                        fileFrom.delete();
                    }
                }
                Log.d(LOG_TAG, " UPLOADED: '" + filename + "' was at '" + tempLocation + "'");
                response = new Response(NanoHTTPD.HTTP_OK, NanoHTTPD.MIME_PLAINTEXT, "OK");
                response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, "*");
                response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS, "origin, content-type");
                response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS, "POST,OPTIONS,GET,HEAD,PUT");
                response.addHeader(HttpHeaders.ALLOW, "POST,OPTIONS,GET,HEAD,PUT");
                return response;
            }
            return serveFile(uri, header, this.rootDir, true);
        }
    }

    private void copyFile(File infile, File outfile) {
        try {
            FileInputStream in = new FileInputStream(infile);
            FileOutputStream out = new FileOutputStream(outfile);
            byte[] buffer = new byte[32768];
            while (true) {
                int len = in.read(buffer);
                if (len > 0) {
                    out.write(buffer, 0, len);
                } else {
                    in.close();
                    out.close();
                    return;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void setHmacKey(String inputKey) {
        hmacKey = inputKey.getBytes();
        seq = 1;
    }

    private void doPackageUpdate(String inurl) {
        PackageInstaller.doPackageInstall(this.form, inurl);
    }

    public void resetSeq() {
        seq = 1;
    }
}
