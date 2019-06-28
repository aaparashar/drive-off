package gnu.kawa.servlet;

import com.google.common.net.HttpHeaders;
import gnu.expr.ModuleBody;
import gnu.expr.ModuleInfo;
import gnu.expr.ModuleMethod;
import gnu.kawa.functions.Format;
import gnu.kawa.xml.MakeResponseHeader;
import gnu.lists.Consumer;
import gnu.mapping.CallContext;
import gnu.mapping.InPort;
import gnu.mapping.SimpleSymbol;
import gnu.mapping.Values;
import gnu.mapping.WrongType;
import gnu.text.URIPath;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.List;
import java.util.Map;

/* compiled from: HTTP.scm */
public class HTTP extends ModuleBody {
    public static final HTTP $instance = new HTTP();
    static final SimpleSymbol Lit0 = ((SimpleSymbol) new SimpleSymbol(HttpHeaders.CONTENT_TYPE).readResolve());
    static final SimpleSymbol Lit1 = ((SimpleSymbol) new SimpleSymbol("Status").readResolve());
    static final SimpleSymbol Lit10 = ((SimpleSymbol) new SimpleSymbol("request-local-port").readResolve());
    static final SimpleSymbol Lit11 = ((SimpleSymbol) new SimpleSymbol("request-local-host").readResolve());
    static final SimpleSymbol Lit12 = ((SimpleSymbol) new SimpleSymbol("request-remote-socket-address").readResolve());
    static final SimpleSymbol Lit13 = ((SimpleSymbol) new SimpleSymbol("request-remote-IP-address").readResolve());
    static final SimpleSymbol Lit14 = ((SimpleSymbol) new SimpleSymbol("request-remote-port").readResolve());
    static final SimpleSymbol Lit15 = ((SimpleSymbol) new SimpleSymbol("request-remote-host").readResolve());
    static final SimpleSymbol Lit16 = ((SimpleSymbol) new SimpleSymbol("request-header").readResolve());
    static final SimpleSymbol Lit17 = ((SimpleSymbol) new SimpleSymbol("request-header-map").readResolve());
    static final SimpleSymbol Lit18 = ((SimpleSymbol) new SimpleSymbol("request-URI").readResolve());
    static final SimpleSymbol Lit19 = ((SimpleSymbol) new SimpleSymbol("request-context-path").readResolve());
    static final SimpleSymbol Lit2 = ((SimpleSymbol) new SimpleSymbol("response-header").readResolve());
    static final SimpleSymbol Lit20 = ((SimpleSymbol) new SimpleSymbol("request-script-path").readResolve());
    static final SimpleSymbol Lit21 = ((SimpleSymbol) new SimpleSymbol("request-local-path").readResolve());
    static final SimpleSymbol Lit22 = ((SimpleSymbol) new SimpleSymbol("request-path").readResolve());
    static final SimpleSymbol Lit23 = ((SimpleSymbol) new SimpleSymbol("request-uri").readResolve());
    static final SimpleSymbol Lit24 = ((SimpleSymbol) new SimpleSymbol("request-url").readResolve());
    static final SimpleSymbol Lit25 = ((SimpleSymbol) new SimpleSymbol("request-path-translated").readResolve());
    static final SimpleSymbol Lit26 = ((SimpleSymbol) new SimpleSymbol("request-query-string").readResolve());
    static final SimpleSymbol Lit27 = ((SimpleSymbol) new SimpleSymbol("request-parameter").readResolve());
    static final SimpleSymbol Lit28 = ((SimpleSymbol) new SimpleSymbol("request-parameters").readResolve());
    static final SimpleSymbol Lit29 = ((SimpleSymbol) new SimpleSymbol("request-parameter-map").readResolve());
    static final SimpleSymbol Lit3 = ((SimpleSymbol) new SimpleSymbol("response-content-type").readResolve());
    static final SimpleSymbol Lit30 = ((SimpleSymbol) new SimpleSymbol("request-body-string").readResolve());
    static final SimpleSymbol Lit31 = ((SimpleSymbol) new SimpleSymbol("request-input-stream").readResolve());
    static final SimpleSymbol Lit32 = ((SimpleSymbol) new SimpleSymbol("request-input-port").readResolve());
    static final SimpleSymbol Lit4 = ((SimpleSymbol) new SimpleSymbol("response-status").readResolve());
    static final SimpleSymbol Lit5 = ((SimpleSymbol) new SimpleSymbol("error-response").readResolve());
    static final SimpleSymbol Lit6 = ((SimpleSymbol) new SimpleSymbol("request-method").readResolve());
    static final SimpleSymbol Lit7 = ((SimpleSymbol) new SimpleSymbol("request-scheme").readResolve());
    static final SimpleSymbol Lit8 = ((SimpleSymbol) new SimpleSymbol("request-local-socket-address").readResolve());
    static final SimpleSymbol Lit9 = ((SimpleSymbol) new SimpleSymbol("request-local-IP-address").readResolve());
    public static final ModuleMethod error$Mnresponse;
    public static final ModuleMethod request$MnURI;
    public static final ModuleMethod request$Mnbody$Mnstring;
    public static final ModuleMethod request$Mncontext$Mnpath;
    public static final ModuleMethod request$Mnheader;
    public static final ModuleMethod request$Mnheader$Mnmap;
    public static final ModuleMethod request$Mninput$Mnport;
    public static final ModuleMethod request$Mninput$Mnstream;
    public static final ModuleMethod request$Mnlocal$MnIP$Mnaddress;
    public static final ModuleMethod request$Mnlocal$Mnhost;
    public static final ModuleMethod request$Mnlocal$Mnpath;
    public static final ModuleMethod request$Mnlocal$Mnport;
    public static final ModuleMethod request$Mnlocal$Mnsocket$Mnaddress;
    public static final ModuleMethod request$Mnmethod;
    public static final ModuleMethod request$Mnparameter;
    public static final ModuleMethod request$Mnparameter$Mnmap;
    public static final ModuleMethod request$Mnparameters;
    public static final ModuleMethod request$Mnpath;
    public static final ModuleMethod request$Mnpath$Mntranslated;
    public static final ModuleMethod request$Mnquery$Mnstring;
    public static final ModuleMethod request$Mnremote$MnIP$Mnaddress;
    public static final ModuleMethod request$Mnremote$Mnhost;
    public static final ModuleMethod request$Mnremote$Mnport;
    public static final ModuleMethod request$Mnremote$Mnsocket$Mnaddress;
    public static final ModuleMethod request$Mnscheme;
    public static final ModuleMethod request$Mnscript$Mnpath;
    public static final ModuleMethod request$Mnuri;
    public static final ModuleMethod request$Mnurl;
    public static final ModuleMethod response$Mncontent$Mntype;
    public static final ModuleMethod response$Mnheader;
    public static final ModuleMethod response$Mnstatus;

    static {
        ModuleBody moduleBody = $instance;
        response$Mnheader = new ModuleMethod(moduleBody, 1, Lit2, 8194);
        response$Mncontent$Mntype = new ModuleMethod(moduleBody, 2, Lit3, 4097);
        response$Mnstatus = new ModuleMethod(moduleBody, 3, Lit4, 8193);
        error$Mnresponse = new ModuleMethod(moduleBody, 5, Lit5, 8193);
        request$Mnmethod = new ModuleMethod(moduleBody, 7, Lit6, 0);
        request$Mnscheme = new ModuleMethod(moduleBody, 8, Lit7, 0);
        request$Mnlocal$Mnsocket$Mnaddress = new ModuleMethod(moduleBody, 9, Lit8, 0);
        request$Mnlocal$MnIP$Mnaddress = new ModuleMethod(moduleBody, 10, Lit9, 0);
        request$Mnlocal$Mnport = new ModuleMethod(moduleBody, 11, Lit10, 0);
        request$Mnlocal$Mnhost = new ModuleMethod(moduleBody, 12, Lit11, 0);
        request$Mnremote$Mnsocket$Mnaddress = new ModuleMethod(moduleBody, 13, Lit12, 0);
        request$Mnremote$MnIP$Mnaddress = new ModuleMethod(moduleBody, 14, Lit13, 0);
        request$Mnremote$Mnport = new ModuleMethod(moduleBody, 15, Lit14, 0);
        request$Mnremote$Mnhost = new ModuleMethod(moduleBody, 16, Lit15, 0);
        request$Mnheader = new ModuleMethod(moduleBody, 17, Lit16, 4097);
        request$Mnheader$Mnmap = new ModuleMethod(moduleBody, 18, Lit17, 0);
        request$MnURI = new ModuleMethod(moduleBody, 19, Lit18, 0);
        request$Mncontext$Mnpath = new ModuleMethod(moduleBody, 20, Lit19, 0);
        request$Mnscript$Mnpath = new ModuleMethod(moduleBody, 21, Lit20, 0);
        request$Mnlocal$Mnpath = new ModuleMethod(moduleBody, 22, Lit21, 0);
        request$Mnpath = new ModuleMethod(moduleBody, 23, Lit22, 0);
        request$Mnuri = new ModuleMethod(moduleBody, 24, Lit23, 0);
        request$Mnurl = new ModuleMethod(moduleBody, 25, Lit24, 0);
        request$Mnpath$Mntranslated = new ModuleMethod(moduleBody, 26, Lit25, 0);
        request$Mnquery$Mnstring = new ModuleMethod(moduleBody, 27, Lit26, 0);
        request$Mnparameter = new ModuleMethod(moduleBody, 28, Lit27, 8193);
        request$Mnparameters = new ModuleMethod(moduleBody, 30, Lit28, 4097);
        request$Mnparameter$Mnmap = new ModuleMethod(moduleBody, 31, Lit29, 0);
        request$Mnbody$Mnstring = new ModuleMethod(moduleBody, 32, Lit30, 0);
        request$Mninput$Mnstream = new ModuleMethod(moduleBody, 33, Lit31, 0);
        request$Mninput$Mnport = new ModuleMethod(moduleBody, 34, Lit32, 0);
        $instance.run();
    }

    public HTTP() {
        ModuleInfo.register(this);
    }

    public static Object errorResponse(int i) {
        return errorResponse(i, "Error");
    }

    public static String requestParameter(String str) {
        return requestParameter(str, null);
    }

    public static Object responseStatus(int i) {
        return responseStatus(i, null);
    }

    public final void run(CallContext $ctx) {
        Consumer $result = $ctx.consumer;
    }

    public static Object responseHeader(Object key, Object value) {
        return MakeResponseHeader.makeResponseHeader.apply2(key, value);
    }

    public int match2(ModuleMethod moduleMethod, Object obj, Object obj2, CallContext callContext) {
        switch (moduleMethod.selector) {
            case 1:
                callContext.value1 = obj;
                callContext.value2 = obj2;
                callContext.proc = moduleMethod;
                callContext.pc = 2;
                return 0;
            case 3:
                callContext.value1 = obj;
                callContext.value2 = obj2;
                callContext.proc = moduleMethod;
                callContext.pc = 2;
                return 0;
            case 5:
                callContext.value1 = obj;
                callContext.value2 = obj2;
                callContext.proc = moduleMethod;
                callContext.pc = 2;
                return 0;
            case 28:
                callContext.value1 = obj;
                callContext.value2 = obj2;
                callContext.proc = moduleMethod;
                callContext.pc = 2;
                return 0;
            default:
                return super.match2(moduleMethod, obj, obj2, callContext);
        }
    }

    public static Object responseContentType(Object type) {
        return responseHeader(Lit0, type);
    }

    public int match1(ModuleMethod moduleMethod, Object obj, CallContext callContext) {
        switch (moduleMethod.selector) {
            case 2:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 3:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 5:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 17:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 28:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            case 30:
                callContext.value1 = obj;
                callContext.proc = moduleMethod;
                callContext.pc = 1;
                return 0;
            default:
                return super.match1(moduleMethod, obj, callContext);
        }
    }

    public static Object responseStatus(int code, String message) {
        SimpleSymbol simpleSymbol = Lit1;
        Object[] objArr = new Object[3];
        objArr[0] = message == null ? "~d " : "~d ~a";
        objArr[1] = Integer.valueOf(code);
        objArr[2] = message;
        return responseHeader(simpleSymbol, Format.formatToString(0, objArr));
    }

    public static Object errorResponse(int code, String message) {
        return responseHeader(Lit1, Format.formatToString(0, "~d ~a", Integer.valueOf(code), message));
    }

    public static String requestMethod() {
        return HttpRequestContext.getInstance("request-method").getRequestMethod();
    }

    public int match0(ModuleMethod moduleMethod, CallContext callContext) {
        switch (moduleMethod.selector) {
            case 7:
                callContext.proc = moduleMethod;
                callContext.pc = 0;
                return 0;
            case 8:
                callContext.proc = moduleMethod;
                callContext.pc = 0;
                return 0;
            case 9:
                callContext.proc = moduleMethod;
                callContext.pc = 0;
                return 0;
            case 10:
                callContext.proc = moduleMethod;
                callContext.pc = 0;
                return 0;
            case 11:
                callContext.proc = moduleMethod;
                callContext.pc = 0;
                return 0;
            case 12:
                callContext.proc = moduleMethod;
                callContext.pc = 0;
                return 0;
            case 13:
                callContext.proc = moduleMethod;
                callContext.pc = 0;
                return 0;
            case 14:
                callContext.proc = moduleMethod;
                callContext.pc = 0;
                return 0;
            case 15:
                callContext.proc = moduleMethod;
                callContext.pc = 0;
                return 0;
            case 16:
                callContext.proc = moduleMethod;
                callContext.pc = 0;
                return 0;
            case 18:
                callContext.proc = moduleMethod;
                callContext.pc = 0;
                return 0;
            case 19:
                callContext.proc = moduleMethod;
                callContext.pc = 0;
                return 0;
            case 20:
                callContext.proc = moduleMethod;
                callContext.pc = 0;
                return 0;
            case 21:
                callContext.proc = moduleMethod;
                callContext.pc = 0;
                return 0;
            case 22:
                callContext.proc = moduleMethod;
                callContext.pc = 0;
                return 0;
            case 23:
                callContext.proc = moduleMethod;
                callContext.pc = 0;
                return 0;
            case 24:
                callContext.proc = moduleMethod;
                callContext.pc = 0;
                return 0;
            case 25:
                callContext.proc = moduleMethod;
                callContext.pc = 0;
                return 0;
            case 26:
                callContext.proc = moduleMethod;
                callContext.pc = 0;
                return 0;
            case 27:
                callContext.proc = moduleMethod;
                callContext.pc = 0;
                return 0;
            case 31:
                callContext.proc = moduleMethod;
                callContext.pc = 0;
                return 0;
            case 32:
                callContext.proc = moduleMethod;
                callContext.pc = 0;
                return 0;
            case 33:
                callContext.proc = moduleMethod;
                callContext.pc = 0;
                return 0;
            case 34:
                callContext.proc = moduleMethod;
                callContext.pc = 0;
                return 0;
            default:
                return super.match0(moduleMethod, callContext);
        }
    }

    public static String requestScheme() {
        return HttpRequestContext.getInstance("request-scheme").getRequestScheme();
    }

    public static InetSocketAddress requestLocalSocketAddress() {
        return HttpRequestContext.getInstance("request-local-socket-address").getLocalSocketAddress();
    }

    public static String requestLocal$MnIPAddress() {
        return HttpRequestContext.getInstance("request-local-IP-address").getLocalIPAddress();
    }

    public static int requestLocalPort() {
        return HttpRequestContext.getInstance("request-local-port").getLocalPort();
    }

    public static InetAddress requestLocalHost() {
        return HttpRequestContext.getInstance("request-local-host").getLocalHost();
    }

    public static InetSocketAddress requestRemoteSocketAddress() {
        return HttpRequestContext.getInstance("request-remote-socket-address").getRemoteSocketAddress();
    }

    public static String requestRemote$MnIPAddress() {
        return HttpRequestContext.getInstance("request-remote-IP-address").getRemoteIPAddress();
    }

    public static int requestRemotePort() {
        return HttpRequestContext.getInstance("request-remote-port").getRemotePort();
    }

    public static InetAddress requestRemoteHost() {
        return HttpRequestContext.getInstance("request-remote-host").getRemoteHost();
    }

    public static String requestHeader(Object name) {
        return HttpRequestContext.getInstance("request-header").getRequestHeader(name == null ? null : name.toString());
    }

    public static Map requestHeaderMap() {
        return HttpRequestContext.getInstance("request-header-map").getRequestHeaders();
    }

    public static URIPath request$MnURI() {
        return URIPath.makeURI(HttpRequestContext.getInstance("request-URI").getRequestURI());
    }

    public static URIPath requestContextPath() {
        return URIPath.makeURI(HttpRequestContext.getInstance("request-context-path").getContextPath());
    }

    public static URIPath requestScriptPath() {
        return URIPath.makeURI(HttpRequestContext.getInstance("request-script-path").getScriptPath());
    }

    public static URIPath requestLocalPath() {
        return URIPath.makeURI(HttpRequestContext.getInstance("request-local-path").getLocalPath());
    }

    public static String requestPath() {
        URIPath makeURI = URIPath.makeURI(HttpRequestContext.getInstance("request-path").getRequestPath());
        return makeURI == null ? null : makeURI.toString();
    }

    public static String requestUri() {
        return requestPath();
    }

    public static StringBuffer requestUrl() {
        return HttpRequestContext.getInstance("request-path").getRequestURLBuffer();
    }

    public static String requestPathTranslated() {
        return HttpRequestContext.getInstance("request-path-translated").getPathTranslated();
    }

    public static Object requestQueryString() {
        String query = HttpRequestContext.getInstance("request-query-string").getQueryString();
        return query == null ? Boolean.FALSE : query;
    }

    public static String requestParameter(String name, Object default_) {
        String value = HttpRequestContext.getInstance("request-parameter").getRequestParameter(name);
        if (value == null) {
            return default_ == null ? null : default_.toString();
        } else {
            return value;
        }
    }

    public Object apply2(ModuleMethod moduleMethod, Object obj, Object obj2) {
        String str = null;
        switch (moduleMethod.selector) {
            case 1:
                return responseHeader(obj, obj2);
            case 3:
                try {
                    return responseStatus(((Number) obj).intValue(), obj2 == null ? null : obj2.toString());
                } catch (ClassCastException e) {
                    throw new WrongType(e, "response-status", 1, obj);
                }
            case 5:
                try {
                    int intValue = ((Number) obj).intValue();
                    if (obj2 != null) {
                        str = obj2.toString();
                    }
                    return errorResponse(intValue, str);
                } catch (ClassCastException e2) {
                    throw new WrongType(e2, "error-response", 1, obj);
                }
            case 28:
                if (obj != null) {
                    str = obj.toString();
                }
                return requestParameter(str, obj2);
            default:
                return super.apply2(moduleMethod, obj, obj2);
        }
    }

    public static Object requestParameters(String name) {
        Object obj = HttpRequestContext.getInstance("request-parameters").getRequestParameters().get(name);
        try {
            return Values.make((List) obj);
        } catch (ClassCastException e) {
            throw new WrongType(e, "plist", -2, obj);
        }
    }

    public Object apply1(ModuleMethod moduleMethod, Object obj) {
        String str = null;
        switch (moduleMethod.selector) {
            case 2:
                return responseContentType(obj);
            case 3:
                try {
                    return responseStatus(((Number) obj).intValue());
                } catch (ClassCastException e) {
                    throw new WrongType(e, "response-status", 1, obj);
                }
            case 5:
                try {
                    return errorResponse(((Number) obj).intValue());
                } catch (ClassCastException e2) {
                    throw new WrongType(e2, "error-response", 1, obj);
                }
            case 17:
                return requestHeader(obj);
            case 28:
                if (obj != null) {
                    str = obj.toString();
                }
                return requestParameter(str);
            case 30:
                if (obj != null) {
                    str = obj.toString();
                }
                return requestParameters(str);
            default:
                return super.apply1(moduleMethod, obj);
        }
    }

    public static Map requestParameterMap() {
        return HttpRequestContext.getInstance("request-parameter-map").getRequestParameters();
    }

    public static CharSequence requestBodyString() {
        return HttpRequestContext.getInstance("request-body-string").getRequestBodyChars();
    }

    public static InputStream requestInputStream() {
        return HttpRequestContext.getInstance("request-input-stream").getRequestStream();
    }

    public static InPort requestInputPort() {
        return HttpRequestContext.getInstance("request-input-port").getRequestPort();
    }

    public Object apply0(ModuleMethod moduleMethod) {
        switch (moduleMethod.selector) {
            case 7:
                return requestMethod();
            case 8:
                return requestScheme();
            case 9:
                return requestLocalSocketAddress();
            case 10:
                return requestLocal$MnIPAddress();
            case 11:
                return Integer.valueOf(requestLocalPort());
            case 12:
                return requestLocalHost();
            case 13:
                return requestRemoteSocketAddress();
            case 14:
                return requestRemote$MnIPAddress();
            case 15:
                return Integer.valueOf(requestRemotePort());
            case 16:
                return requestRemoteHost();
            case 18:
                return requestHeaderMap();
            case 19:
                return request$MnURI();
            case 20:
                return requestContextPath();
            case 21:
                return requestScriptPath();
            case 22:
                return requestLocalPath();
            case 23:
                return requestPath();
            case 24:
                return requestUri();
            case 25:
                return requestUrl();
            case 26:
                return requestPathTranslated();
            case 27:
                return requestQueryString();
            case 31:
                return requestParameterMap();
            case 32:
                return requestBodyString();
            case 33:
                return requestInputStream();
            case 34:
                return requestInputPort();
            default:
                return super.apply0(moduleMethod);
        }
    }
}
