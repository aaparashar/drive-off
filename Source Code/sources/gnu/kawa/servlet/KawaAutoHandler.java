package gnu.kawa.servlet;

import com.google.appinventor.components.runtime.util.NanoHTTPD;
import gnu.expr.Compilation;
import gnu.expr.Language;
import gnu.expr.ModuleBody;
import gnu.expr.ModuleContext;
import gnu.expr.ModuleExp;
import gnu.expr.ModuleInfo;
import gnu.expr.ModuleManager;
import gnu.mapping.CallContext;
import gnu.mapping.Environment;
import gnu.mapping.InPort;
import gnu.text.Path;
import gnu.text.SourceMessages;
import gnu.text.SyntaxException;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Hashtable;

public class KawaAutoHandler {
    static final String MODULE_MAP_ATTRIBUTE = "gnu.kawa.module-map";

    public static void run(HttpRequestContext hctx, CallContext ctx) throws Throwable {
        Object mod = getModule(hctx, ctx, hctx.getRequestParameter("qexo-save-class") != null);
        if (mod instanceof ModuleBody) {
            ((ModuleBody) mod).run(ctx);
        }
    }

    public static Object getModule(HttpRequestContext hctx, CallContext ctx, boolean saveClass) throws Exception {
        String path = hctx.getRequestPath().substring(hctx.getContextPath().length() - 1);
        Hashtable mmap = (Hashtable) hctx.getAttribute(MODULE_MAP_ATTRIBUTE);
        if (mmap == null) {
            mmap = new Hashtable();
            hctx.setAttribute(MODULE_MAP_ATTRIBUTE, mmap);
        }
        ModuleContext mcontext = (ModuleContext) hctx.getAttribute("gnu.kawa.module-context");
        if (mcontext == null) {
            mcontext = ModuleContext.getContext();
        }
        mcontext.addFlags(ModuleContext.IN_HTTP_SERVER);
        if (hctx.getClass().getName().endsWith("KawaServlet$Context")) {
            mcontext.addFlags(ModuleContext.IN_SERVLET);
        }
        ModuleInfo minfo = (ModuleInfo) mmap.get(path);
        long now = System.currentTimeMillis();
        ModuleManager mmanager = mcontext.getManager();
        if (minfo != null && now - minfo.lastCheckedTime < mmanager.lastModifiedCacheTime) {
            return mcontext.findInstance(minfo);
        }
        URL url;
        int plen = path.length();
        if (plen == 0 || path.charAt(plen - 1) == '/') {
            url = null;
        } else {
            url = hctx.getResourceURL(path);
        }
        String upath = path;
        if (url == null) {
            int sl;
            String xpath = path;
            do {
                sl = xpath.lastIndexOf(47);
                if (sl < 0) {
                    break;
                }
                xpath = xpath.substring(0, sl);
                upath = xpath + "/+default+";
                url = hctx.getResourceURL(upath);
            } while (url == null);
            hctx.setScriptAndLocalPath(path.substring(1, sl + 1), path.substring(sl + 1));
        } else {
            hctx.setScriptAndLocalPath(path, "");
        }
        byte[] bmsg;
        if (url == null) {
            bmsg = ("The requested URL " + path + " was not found on this server." + " res/:" + hctx.getResourceURL("/") + "\r\n").getBytes();
            hctx.sendResponseHeaders(404, null, (long) bmsg.length);
            try {
                hctx.getResponseStream().write(bmsg);
                return null;
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
        String urlString = url.toExternalForm();
        if (minfo == null || !urlString.equals(minfo.getSourceAbsPathname())) {
            minfo = mmanager.findWithURL(url);
        }
        if (minfo.checkCurrent(mmanager, now)) {
            return mcontext.findInstance(minfo);
        }
        Compilation comp;
        mmap.put(path, minfo);
        Path absPath = minfo.getSourceAbsPath();
        InputStream resourceStream = absPath.openInputStream();
        if (!(resourceStream instanceof BufferedInputStream)) {
            resourceStream = new BufferedInputStream(resourceStream);
        }
        Language language = Language.getInstanceFromFilenameExtension(path);
        if (language != null) {
            hctx.log("Compile " + path + " - a " + language.getName() + " source file (based on extension)");
        } else {
            language = Language.detect(resourceStream);
            if (language != null) {
                hctx.log("Compile " + path + " - a " + language.getName() + " source file (detected from content)");
            } else if (path != upath) {
                bmsg = ("The requested URL " + path + " was not found on this server." + " upath=" + upath + ".\r\n").getBytes();
                hctx.sendResponseHeaders(404, null, (long) bmsg.length);
                try {
                    hctx.getResponseStream().write(bmsg);
                    return null;
                } catch (IOException ex2) {
                    throw new RuntimeException(ex2);
                }
            } else {
                hctx.sendResponseHeaders(200, null, absPath.getContentLength());
                OutputStream out = hctx.getResponseStream();
                byte[] buffer = new byte[4096];
                while (true) {
                    int n = resourceStream.read(buffer);
                    if (n < 0) {
                        resourceStream.close();
                        out.close();
                        return null;
                    }
                    out.write(buffer, 0, n);
                }
            }
        }
        InPort inPort = new InPort(resourceStream, absPath);
        Language.setCurrentLanguage(language);
        SourceMessages messages = new SourceMessages();
        try {
            comp = language.parse(inPort, messages, 9, minfo);
        } catch (SyntaxException ex3) {
            if (ex3.getMessages() != messages) {
                throw ex3;
            }
            comp = null;
        }
        Class cl = null;
        if (!messages.seenErrors()) {
            ModuleExp mexp = comp.getModule();
            cl = (Class) ModuleExp.evalModule1(Environment.getCurrent(), comp, url, null);
        }
        if (messages.seenErrors()) {
            String msg = "script syntax error:\n" + messages.toString(20);
            ((ServletPrinter) ctx.consumer).addHeader("Content-type", NanoHTTPD.MIME_PLAINTEXT);
            hctx.sendResponseHeaders(500, "Syntax errors", -1);
            ctx.consumer.write(msg);
            minfo.cleanupAfterCompilation();
            return null;
        }
        minfo.setModuleClass(cl);
        return mcontext.findInstance(minfo);
    }
}
