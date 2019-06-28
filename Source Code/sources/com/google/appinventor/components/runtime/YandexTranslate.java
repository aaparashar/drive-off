package com.google.appinventor.components.runtime;

import android.app.Activity;
import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.common.PropertyTypeConstants;
import com.google.appinventor.components.runtime.util.AsynchUtil;
import com.google.appinventor.components.runtime.util.ErrorMessages;
import com.google.common.base.Ascii;
import com.google.common.primitives.SignedBytes;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import org.json.JSONException;
import org.json.JSONObject;

@SimpleObject
@DesignerComponent(category = ComponentCategory.MEDIA, description = "Use this component to translate words and sentences between different languages. This component needs Internet access, as it will request translations to the Yandex.Translate service. Specify the source and target language in the form source-target using two letter language codes. So\"en-es\" will translate from English to Spanish while \"es-ru\" will translate from Spanish to Russian. If you leave out the source language, the service will attempt to detect the source language. So providing just \"es\" will attempt to detect the source language and translate it to Spanish.<p /> This component is powered by the Yandex translation service.  See http://api.yandex.com/translate/ for more information, including the list of available languages and the meanings of the language codes and status codes. <p />Note: Translation happens asynchronously in the background. When the translation is complete, the \"GotTranslation\" event is triggered.", iconName = "images/yandex.png", nonVisible = true, version = 1)
@UsesPermissions(permissionNames = "android.permission.INTERNET")
public final class YandexTranslate extends AndroidNonvisibleComponent {
    public static final String YANDEX_TRANSLATE_SERVICE_URL = "https://translate.yandex.net/api/v1.5/tr.json/translate?key=";
    private final Activity activity;
    private final byte[] key1 = new byte[]{(byte) -127, (byte) -88, (byte) 79, (byte) 80, (byte) 65, (byte) 112, (byte) -80, (byte) 87, (byte) -62, (byte) 126, (byte) -125, (byte) -25, (byte) -31, (byte) 55, (byte) 107, (byte) -42, (byte) -63, (byte) -62, (byte) 33, (byte) -122, (byte) 1, (byte) 89, (byte) -33, Ascii.ETB, (byte) -19, Ascii.DC2, (byte) -81, (byte) 37, (byte) -67, (byte) 114, (byte) 92, (byte) -60, (byte) -76, (byte) -50, (byte) -59, (byte) -49, (byte) -114, (byte) -64, (byte) -96, (byte) -75, (byte) 117, (byte) -116, (byte) 53, (byte) -8, (byte) 44, (byte) 111, (byte) 120, (byte) 48, (byte) 41, Ascii.RS, (byte) 85, (byte) -116, (byte) -31, (byte) 17, (byte) 87, (byte) -89, (byte) -49, (byte) -51, (byte) 47, (byte) 92, (byte) 121, (byte) -58, (byte) -80, (byte) -25, (byte) 86, (byte) 123, (byte) -36, (byte) -9, (byte) 101, (byte) -112, (byte) -22, (byte) -28, (byte) -29, (byte) -14, (byte) -125, (byte) 46, (byte) -103, (byte) -36, (byte) 125, (byte) 114, (byte) 35, (byte) -31, (byte) 1, (byte) 123};
    private final byte[] key2 = new byte[]{(byte) -11, (byte) -38, (byte) 33, (byte) 35, (byte) 45, (byte) 94, (byte) -127, (byte) 121, (byte) -13, (byte) 80, (byte) -79, (byte) -41, (byte) -48, (byte) 3, (byte) 91, (byte) -29, (byte) -15, (byte) -9, (byte) 117, (byte) -74, (byte) 49, (byte) 105, (byte) -26, (byte) 34, (byte) -35, (byte) 72, (byte) -127, SignedBytes.MAX_POWER_OF_TWO, (byte) -116, (byte) 69, (byte) 111, (byte) -12, (byte) -48, (byte) -81, (byte) -11, (byte) -83, (byte) -69, (byte) -12, (byte) -108, (byte) -42, (byte) 65, (byte) -72, (byte) 86, (byte) -42, Ascii.ESC, (byte) 12, Ascii.SUB, (byte) 2, Ascii.FS, (byte) 122, (byte) 51, (byte) -24, (byte) -45, (byte) 36, (byte) 54, (byte) -106, (byte) -87, (byte) -3, Ascii.ESC, (byte) 62, (byte) 65, (byte) -16, (byte) -126, (byte) -42, (byte) 99, (byte) 77, (byte) -70, (byte) -49, (byte) 83, (byte) -12, (byte) -114, (byte) -35, (byte) -44, (byte) -109, (byte) -77, Ascii.FS, (byte) -84, (byte) -66, (byte) 72, Ascii.SYN, Ascii.DC2, (byte) -126, (byte) 50, (byte) 78};
    private final String yandexKey;

    public YandexTranslate(ComponentContainer container) {
        super(container.$form());
        this.form.setYandexTranslateTagline();
        this.yandexKey = gk();
        this.activity = container.$context();
    }

    @SimpleFunction(description = "By providing a target language to translate to (for instance, 'es' for Spanish, 'en' for English, or 'ru' for Russian), and a word or sentence to translate, this method will request a translation to the Yandex.Translate service.\nOnce the text is translated by the external service, the event GotTranslation will be executed.\nNote: Yandex.Translate will attempt to detect the source language. You can also specify prepending it to the language translation. I.e., es-ru will specify Spanish to Russian translation.")
    public void RequestTranslation(final String languageToTranslateTo, final String textToTranslate) {
        if (this.yandexKey.equals("")) {
            this.form.dispatchErrorOccurredEvent(this, "RequestTranslation", ErrorMessages.ERROR_TRANSLATE_NO_KEY_FOUND, new Object[0]);
        } else {
            AsynchUtil.runAsynchronously(new Runnable() {
                public void run() {
                    try {
                        YandexTranslate.this.performRequest(languageToTranslateTo, textToTranslate);
                    } catch (IOException e) {
                        YandexTranslate.this.form.dispatchErrorOccurredEvent(YandexTranslate.this, "RequestTranslation", ErrorMessages.ERROR_TRANSLATE_SERVICE_NOT_AVAILABLE, new Object[0]);
                    } catch (JSONException e2) {
                        YandexTranslate.this.form.dispatchErrorOccurredEvent(YandexTranslate.this, "RequestTranslation", ErrorMessages.ERROR_TRANSLATE_JSON_RESPONSE, new Object[0]);
                    }
                }
            });
        }
    }

    private void performRequest(String languageToTranslateTo, String textToTranslate) throws IOException, JSONException {
        HttpURLConnection connection = (HttpURLConnection) new URL(YANDEX_TRANSLATE_SERVICE_URL + this.yandexKey + "&lang=" + languageToTranslateTo + "&text=" + URLEncoder.encode(textToTranslate, "UTF-8")).openConnection();
        if (connection != null) {
            try {
                JSONObject jsonResponse = new JSONObject(getResponseContent(connection));
                final String responseCode = jsonResponse.getString("code");
                final String translation = (String) jsonResponse.getJSONArray(PropertyTypeConstants.PROPERTY_TYPE_TEXT).get(0);
                this.activity.runOnUiThread(new Runnable() {
                    public void run() {
                        YandexTranslate.this.GotTranslation(responseCode, translation);
                    }
                });
            } finally {
                connection.disconnect();
            }
        }
    }

    private static String getResponseContent(HttpURLConnection connection) throws IOException {
        String encoding = connection.getContentEncoding();
        if (encoding == null) {
            encoding = "UTF-8";
        }
        InputStreamReader reader = new InputStreamReader(connection.getInputStream(), encoding);
        try {
            int contentLength = connection.getContentLength();
            StringBuilder sb = contentLength != -1 ? new StringBuilder(contentLength) : new StringBuilder();
            char[] buf = new char[1024];
            while (true) {
                int read = reader.read(buf);
                if (read == -1) {
                    break;
                }
                sb.append(buf, 0, read);
            }
            String stringBuilder = sb.toString();
            return stringBuilder;
        } finally {
            reader.close();
        }
    }

    @SimpleEvent(description = "Event triggered when the Yandex.Translate service returns the translated text. This event also provides a response code for error handling. If the responseCode is not 200, then something went wrong with the call, and the translation will not be available.")
    public void GotTranslation(String responseCode, String translation) {
        EventDispatcher.dispatchEvent(this, "GotTranslation", responseCode, translation);
    }

    private String gk() {
        byte[] retval = new byte[this.key1.length];
        for (int i = 0; i < this.key1.length; i++) {
            retval[i] = (byte) (this.key1[i] ^ this.key2[i]);
        }
        return new String(retval);
    }
}
