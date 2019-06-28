package com.google.appinventor.components.runtime;

import android.app.Activity;
import android.os.Handler;
import android.util.Log;
import com.firebase.client.AuthData;
import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.Firebase.AuthResultHandler;
import com.firebase.client.Firebase.AuthStateListener;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.UsesLibraries;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.errors.YailRuntimeError;
import com.google.appinventor.components.runtime.util.JsonUtil;
import com.google.appinventor.components.runtime.util.SdkLevel;
import java.util.concurrent.atomic.AtomicReference;
import org.json.JSONException;

@DesignerComponent(category = ComponentCategory.EXPERIMENTAL, description = "Non-visible component that communicates with Firebase to store and retrieve information.", designerHelpDescription = "Non-visible component that communicates with a Firebase to store and retrieve information.", iconName = "images/firebaseDB.png", nonVisible = true, version = 1)
@UsesLibraries(libraries = "firebase.jar")
@SimpleObject
@UsesPermissions(permissionNames = "android.permission.INTERNET")
public class FirebaseDB extends AndroidNonvisibleComponent implements Component {
    private static final String LOG_TAG = "Firebase";
    private final Activity activity;
    private Handler androidUIHandler = new Handler();
    private AuthStateListener authListener;
    private ChildEventListener childListener;
    private String defaultURL = null;
    private String developerBucket;
    private String firebaseToken;
    private String firebaseURL = null;
    private Firebase myFirebase;
    private String projectBucket;
    private boolean useDefault = true;

    /* renamed from: com.google.appinventor.components.runtime.FirebaseDB$1 */
    class C00251 implements ChildEventListener {
        C00251() {
        }

        public void onChildAdded(final DataSnapshot snapshot, String previousChildKey) {
            FirebaseDB.this.androidUIHandler.post(new Runnable() {
                public void run() {
                    FirebaseDB.this.DataChanged(snapshot.getKey(), snapshot.getValue());
                }
            });
        }

        public void onCancelled(final FirebaseError error) {
            FirebaseDB.this.androidUIHandler.post(new Runnable() {
                public void run() {
                    FirebaseDB.this.FirebaseError(error.getMessage());
                }
            });
        }

        public void onChildChanged(final DataSnapshot snapshot, String previousChildKey) {
            FirebaseDB.this.androidUIHandler.post(new Runnable() {
                public void run() {
                    FirebaseDB.this.DataChanged(snapshot.getKey(), snapshot.getValue());
                }
            });
        }

        public void onChildMoved(DataSnapshot snapshot, String previousChildKey) {
        }

        public void onChildRemoved(final DataSnapshot snapshot) {
            FirebaseDB.this.androidUIHandler.post(new Runnable() {
                public void run() {
                    FirebaseDB.this.DataChanged(snapshot.getKey(), null);
                }
            });
        }
    }

    /* renamed from: com.google.appinventor.components.runtime.FirebaseDB$2 */
    class C00272 implements AuthStateListener {

        /* renamed from: com.google.appinventor.components.runtime.FirebaseDB$2$1 */
        class C00261 implements AuthResultHandler {
            C00261() {
            }

            public void onAuthenticated(AuthData authData) {
                Log.i(FirebaseDB.LOG_TAG, "Auth Successful.");
            }

            public void onAuthenticationError(FirebaseError error) {
                Log.e(FirebaseDB.LOG_TAG, "Auth Failed: " + error.getMessage());
            }
        }

        C00272() {
        }

        public void onAuthStateChanged(AuthData data) {
            Log.i(FirebaseDB.LOG_TAG, "onAuthStateChanged: data = " + data);
            if (data == null) {
                FirebaseDB.this.myFirebase.authWithCustomToken(FirebaseDB.this.firebaseToken, new C00261());
            }
        }
    }

    public FirebaseDB(ComponentContainer container) {
        super(container.$form());
        this.activity = container.$context();
        Firebase.setAndroidContext(this.activity);
        this.developerBucket = "";
        this.projectBucket = "";
        this.firebaseToken = "";
        this.childListener = new C00251();
        this.authListener = new C00272();
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Gets the URL for this FirebaseDB.", userVisible = false)
    public String FirebaseURL() {
        if (this.useDefault) {
            return "DEFAULT";
        }
        return this.firebaseURL;
    }

    @DesignerProperty(defaultValue = "DEFAULT", editorType = "FirbaseURL")
    @SimpleProperty(description = "Sets the URL for this FirebaseDB.")
    public void FirebaseURL(String url) {
        if (!url.equals("DEFAULT")) {
            this.useDefault = false;
            if (!this.firebaseURL.equals(url)) {
                this.firebaseURL = url;
                this.useDefault = false;
                resetListener();
            }
        } else if (this.useDefault) {
            this.firebaseURL = this.defaultURL;
        } else {
            this.useDefault = true;
            if (this.defaultURL == null) {
                Log.d(LOG_TAG, "FirebaseURL called before DefaultURL (should not happen!)");
                return;
            }
            this.firebaseURL = this.defaultURL;
            resetListener();
        }
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, userVisible = false)
    public String DeveloperBucket() {
        return this.developerBucket;
    }

    @DesignerProperty(editorType = "string")
    @SimpleProperty
    public void DeveloperBucket(String bucket) {
        this.developerBucket = bucket;
        resetListener();
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, description = "Gets the ProjectBucket for this FirebaseDB.")
    public String ProjectBucket() {
        return this.projectBucket;
    }

    @DesignerProperty(defaultValue = "", editorType = "string")
    @SimpleProperty(description = "Sets the ProjectBucket for this FirebaseDB.")
    public void ProjectBucket(String bucket) {
        if (!this.projectBucket.equals(bucket)) {
            this.projectBucket = bucket;
            resetListener();
        }
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR, userVisible = false)
    public String FirebaseToken() {
        return this.firebaseToken;
    }

    @DesignerProperty(editorType = "string")
    @SimpleProperty
    public void FirebaseToken(String JWT) {
        this.firebaseToken = JWT;
        resetListener();
    }

    private void resetListener() {
        if (this.myFirebase != null) {
            this.myFirebase.removeEventListener(this.childListener);
            this.myFirebase.removeAuthStateListener(this.authListener);
        }
        this.myFirebase = null;
        connectFirebase();
    }

    @SimpleFunction
    public void StoreValue(String tag, Object valueToStore) {
        if (valueToStore != null) {
            try {
                valueToStore = JsonUtil.getJsonRepresentation(valueToStore);
            } catch (JSONException e) {
                throw new YailRuntimeError("Value failed to convert to JSON.", "JSON Creation Error.");
            }
        }
        this.myFirebase.child(tag).setValue(valueToStore);
    }

    @SimpleFunction
    public void GetValue(final String tag, final Object valueIfTagNotThere) {
        this.myFirebase.child(tag).addListenerForSingleValueEvent(new ValueEventListener() {
            public void onDataChange(DataSnapshot snapshot) {
                final AtomicReference<Object> value = new AtomicReference();
                try {
                    if (snapshot.exists()) {
                        value.set(snapshot.getValue());
                    } else {
                        value.set(JsonUtil.getJsonRepresentation(valueIfTagNotThere));
                    }
                    FirebaseDB.this.androidUIHandler.post(new Runnable() {
                        public void run() {
                            FirebaseDB.this.GotValue(tag, value.get());
                        }
                    });
                } catch (JSONException e) {
                    throw new YailRuntimeError("Value failed to convert to JSON.", "JSON Creation Error.");
                }
            }

            public void onCancelled(final FirebaseError error) {
                FirebaseDB.this.androidUIHandler.post(new Runnable() {
                    public void run() {
                        FirebaseDB.this.FirebaseError(error.getMessage());
                    }
                });
            }
        });
    }

    @SimpleEvent
    public void GotValue(String tag, Object value) {
        if (value != null) {
            try {
                if (value instanceof String) {
                    value = JsonUtil.getObjectFromJson((String) value);
                }
            } catch (JSONException e) {
                throw new YailRuntimeError("Value failed to convert from JSON.", "JSON Retrieval Error.");
            }
        }
        EventDispatcher.dispatchEvent(this, "GotValue", tag, value);
    }

    @SimpleEvent
    public void DataChanged(String tag, Object value) {
        if (value != null) {
            try {
                if (value instanceof String) {
                    value = JsonUtil.getObjectFromJson((String) value);
                }
            } catch (JSONException e) {
                throw new YailRuntimeError("Value failed to convert from JSON.", "JSON Retrieval Error.");
            }
        }
        EventDispatcher.dispatchEvent(this, "DataChanged", tag, value);
    }

    @SimpleEvent
    public void FirebaseError(String message) {
        Log.e(LOG_TAG, message);
        EventDispatcher.dispatchEvent(this, "FirebaseError", message);
    }

    private void connectFirebase() {
        if (SdkLevel.getLevel() < 10) {
            Notifier.oneButtonAlert(this.activity, "The version of Android on this device is too old to use Firebase.", "Android Too Old", "OK");
            return;
        }
        if (this.useDefault) {
            this.myFirebase = new Firebase(this.firebaseURL + "developers/" + this.developerBucket + this.projectBucket);
        } else {
            this.myFirebase = new Firebase(this.firebaseURL + this.projectBucket);
        }
        this.myFirebase.addChildEventListener(this.childListener);
        this.myFirebase.addAuthStateListener(this.authListener);
    }

    @SimpleFunction(description = "If you are having difficulty with the Companion and you are switching between different Firebase accounts, you may need to use this function to clear internal Firebase caches. You can just use the \"Do It\" function on this block in the blocks editor. Note: You should not normally need to use this block as part of an application.")
    public void Unauthenticate() {
        if (this.myFirebase == null) {
            connectFirebase();
        }
        this.myFirebase.unauth();
    }

    @DesignerProperty(editorType = "string")
    @SimpleProperty(category = PropertyCategory.BEHAVIOR, userVisible = false)
    public void DefaultURL(String url) {
        this.defaultURL = url;
        if (this.useDefault) {
            this.firebaseURL = this.defaultURL;
            resetListener();
        }
    }
}
