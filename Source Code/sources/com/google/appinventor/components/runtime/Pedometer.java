package com.google.appinventor.components.runtime;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.DesignerProperty;
import com.google.appinventor.components.annotations.PropertyCategory;
import com.google.appinventor.components.annotations.SimpleEvent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.SimpleProperty;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.common.ComponentCategory;
import java.lang.reflect.Array;

@SimpleObject
@DesignerComponent(category = ComponentCategory.INTERNAL, description = "Component that can count steps.", iconName = "images/pedometer.png", nonVisible = true, version = 1)
@UsesPermissions(permissionNames = "android.permission.ACCESS_FINE_LOCATION")
public class Pedometer extends AndroidNonvisibleComponent implements Component, LocationListener, SensorEventListener, Deleteable {
    private static final int DIMENSIONS = 3;
    private static final int INTERVAL_VARIATION = 250;
    private static final int MIN_SATELLITES = 4;
    private static final int NUM_INTERVALS = 2;
    private static final float PEAK_VALLEY_RANGE = 4.0f;
    private static final String PREFS_NAME = "PedometerPrefs";
    private static final float STRIDE_LENGTH = 0.73f;
    private static final String TAG = "Pedometer";
    private static final int WIN_SIZE = 20;
    private boolean calibrateSteps = true;
    private final Context context;
    private Location currentLocation;
    private float distWhenGPSLost = 0.0f;
    private long elapsedTimestamp = 0;
    private boolean firstGpsReading = true;
    private boolean foundNonStep = true;
    private boolean[] foundValley = new boolean[3];
    private boolean gpsAvailable = false;
    private float gpsDistance = 0.0f;
    private long gpsStepTime = 0;
    private int intervalPos = 0;
    private int lastNumSteps = 0;
    private float[] lastValley = new float[3];
    private float[][] lastValues = ((float[][]) Array.newInstance(Float.TYPE, new int[]{20, 3}));
    private final LocationManager locationManager;
    private Location locationWhenGPSLost;
    private int numStepsRaw = 0;
    private int numStepsWithFilter = 0;
    private int[] peak = new int[3];
    private boolean pedometerPaused = true;
    private float[] prevDiff = new float[3];
    private Location prevLocation;
    private long prevStopClockTime = 0;
    private final SensorManager sensorManager;
    private boolean startPeaking = false;
    private long startTime = 0;
    private boolean statusMoving = false;
    private long[] stepInterval = new long[2];
    private long stepTimestamp = 0;
    private int stopDetectionTimeout = 2000;
    private float strideLength = STRIDE_LENGTH;
    private float totalDistance = 0.0f;
    private boolean useGps = true;
    private int[] valley = new int[3];
    private int winPos = 0;

    public Pedometer(ComponentContainer container) {
        super(container.$form());
        this.context = container.$context();
        this.winPos = 0;
        this.startPeaking = false;
        this.numStepsWithFilter = 0;
        this.numStepsRaw = 0;
        this.firstGpsReading = true;
        this.gpsDistance = 0.0f;
        for (int i = 0; i < 3; i++) {
            this.foundValley[i] = true;
            this.lastValley[i] = 0.0f;
        }
        this.sensorManager = (SensorManager) this.context.getSystemService("sensor");
        this.locationManager = (LocationManager) this.context.getSystemService("location");
        this.locationManager.requestLocationUpdates("gps", 0, 0.0f, this);
        SharedPreferences settings = this.context.getSharedPreferences(PREFS_NAME, 0);
        this.strideLength = settings.getFloat("Pedometer.stridelength", STRIDE_LENGTH);
        this.totalDistance = settings.getFloat("Pedometer.distance", 0.0f);
        this.numStepsRaw = settings.getInt("Pedometer.prevStepCount", 0);
        this.prevStopClockTime = settings.getLong("Pedometer.clockTime", 0);
        this.numStepsWithFilter = this.numStepsRaw;
        this.startTime = System.currentTimeMillis();
        Log.d(TAG, "Pedometer Created");
    }

    @SimpleFunction
    public void Start() {
        if (this.pedometerPaused) {
            this.pedometerPaused = false;
            this.sensorManager.registerListener(this, (Sensor) this.sensorManager.getSensorList(1).get(0), 0);
            this.startTime = System.currentTimeMillis();
        }
    }

    @SimpleFunction
    public void Stop() {
        Pause();
        this.locationManager.removeUpdates(this);
        this.useGps = false;
        this.calibrateSteps = false;
        setGpsAvailable(false);
    }

    @SimpleFunction
    public void Reset() {
        this.numStepsWithFilter = 0;
        this.numStepsRaw = 0;
        this.totalDistance = 0.0f;
        this.calibrateSteps = false;
        this.prevStopClockTime = 0;
        this.startTime = System.currentTimeMillis();
    }

    @SimpleFunction
    public void Resume() {
        Start();
    }

    @SimpleFunction
    public void Pause() {
        if (!this.pedometerPaused) {
            this.pedometerPaused = true;
            this.statusMoving = false;
            this.sensorManager.unregisterListener(this);
            Log.d(TAG, "Unregistered listener on pause");
            this.prevStopClockTime += System.currentTimeMillis() - this.startTime;
        }
    }

    @SimpleFunction(description = "Saves the pedometer state to the phone")
    public void Save() {
        Editor editor = this.context.getSharedPreferences(PREFS_NAME, 0).edit();
        editor.putFloat("Pedometer.stridelength", this.strideLength);
        editor.putFloat("Pedometer.distance", this.totalDistance);
        editor.putInt("Pedometer.prevStepCount", this.numStepsRaw);
        if (this.pedometerPaused) {
            editor.putLong("Pedometer.clockTime", this.prevStopClockTime);
        } else {
            editor.putLong("Pedometer.clockTime", this.prevStopClockTime + (System.currentTimeMillis() - this.startTime));
        }
        editor.putLong("Pedometer.closeTime", System.currentTimeMillis());
        editor.commit();
        Log.d(TAG, "Pedometer state saved.");
    }

    @SimpleEvent(description = "This event is run when a raw step is detected")
    public void SimpleStep(int simpleSteps, float distance) {
        EventDispatcher.dispatchEvent(this, "SimpleStep", Integer.valueOf(simpleSteps), Float.valueOf(distance));
    }

    @SimpleEvent(description = "This event is run when a walking step is detected")
    public void WalkStep(int walkSteps, float distance) {
        EventDispatcher.dispatchEvent(this, "WalkStep", Integer.valueOf(walkSteps), Float.valueOf(distance));
    }

    @SimpleEvent
    public void StartedMoving() {
        EventDispatcher.dispatchEvent(this, "StartedMoving", new Object[0]);
    }

    @SimpleEvent
    public void StoppedMoving() {
        EventDispatcher.dispatchEvent(this, "StoppedMoving", new Object[0]);
    }

    @SimpleEvent
    public void CalibrationFailed() {
        EventDispatcher.dispatchEvent(this, "CalibrationFailed", new Object[0]);
    }

    @SimpleEvent
    public void GPSAvailable() {
        EventDispatcher.dispatchEvent(this, "GPSAvailable", new Object[0]);
    }

    @SimpleEvent
    public void GPSLost() {
        EventDispatcher.dispatchEvent(this, "GPSLost", new Object[0]);
    }

    @DesignerProperty(defaultValue = "true", editorType = "boolean")
    @SimpleProperty(category = PropertyCategory.BEHAVIOR)
    public void CalibrateStrideLength(boolean cal) {
        if (this.gpsAvailable || !cal) {
            if (cal) {
                this.useGps = true;
            }
            this.calibrateSteps = cal;
            return;
        }
        CalibrationFailed();
    }

    @SimpleProperty
    public boolean CalibrateStrideLength() {
        return this.calibrateSteps;
    }

    @DesignerProperty(defaultValue = "0.73", editorType = "non_negative_float")
    @SimpleProperty(category = PropertyCategory.BEHAVIOR)
    public void StrideLength(float length) {
        CalibrateStrideLength(false);
        this.strideLength = length;
    }

    @SimpleProperty
    public float StrideLength() {
        return this.strideLength;
    }

    @DesignerProperty(defaultValue = "2000", editorType = "non_negative_integer")
    @SimpleProperty(category = PropertyCategory.BEHAVIOR)
    public void StopDetectionTimeout(int timeout) {
        this.stopDetectionTimeout = timeout;
    }

    @SimpleProperty
    public int StopDetectionTimeout() {
        return this.stopDetectionTimeout;
    }

    @DesignerProperty(defaultValue = "true", editorType = "boolean")
    @SimpleProperty(category = PropertyCategory.BEHAVIOR)
    public void UseGPS(boolean gps) {
        if (!gps && this.useGps) {
            this.locationManager.removeUpdates(this);
        } else if (gps && !this.useGps) {
            this.locationManager.requestLocationUpdates("gps", 0, 0.0f, this);
        }
        this.useGps = gps;
    }

    @SimpleProperty
    public boolean UseGPS() {
        return this.useGps;
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR)
    public float Distance() {
        return this.totalDistance;
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR)
    public boolean Moving() {
        return this.statusMoving;
    }

    @SimpleProperty(category = PropertyCategory.BEHAVIOR)
    public long ElapsedTime() {
        if (this.pedometerPaused) {
            return this.prevStopClockTime;
        }
        return this.prevStopClockTime + (System.currentTimeMillis() - this.startTime);
    }

    private boolean areStepsEquallySpaced() {
        float avg = 0.0f;
        int num = 0;
        for (long interval : this.stepInterval) {
            if (interval > 0) {
                num++;
                avg += (float) interval;
            }
        }
        avg /= (float) num;
        for (long interval2 : this.stepInterval) {
            if (Math.abs(((float) interval2) - avg) > 250.0f) {
                return false;
            }
        }
        return true;
    }

    private void getPeak() {
        int mid = (this.winPos + 10) % 20;
        int k = 0;
        while (k < 3) {
            this.peak[k] = mid;
            int i = 0;
            while (i < 20) {
                if (i != mid && this.lastValues[i][k] >= this.lastValues[mid][k]) {
                    this.peak[k] = -1;
                    break;
                }
                i++;
            }
            k++;
        }
    }

    private void getValley() {
        int mid = (this.winPos + 10) % 20;
        int k = 0;
        while (k < 3) {
            this.valley[k] = mid;
            int i = 0;
            while (i < 20) {
                if (i != mid && this.lastValues[i][k] <= this.lastValues[mid][k]) {
                    this.valley[k] = -1;
                    break;
                }
                i++;
            }
            k++;
        }
    }

    private void setGpsAvailable(boolean available) {
        if (!this.gpsAvailable && available) {
            this.gpsAvailable = true;
            GPSAvailable();
        } else if (this.gpsAvailable && !available) {
            this.gpsAvailable = false;
            GPSLost();
        }
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.d(TAG, "Accelerometer accuracy changed.");
    }

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == 1) {
            float[] values = event.values;
            if (this.startPeaking) {
                getPeak();
                getValley();
            }
            int argmax = this.prevDiff[0] > this.prevDiff[1] ? 0 : 1;
            if (this.prevDiff[2] > this.prevDiff[argmax]) {
                argmax = 2;
            }
            int k = 0;
            while (k < 3) {
                if (this.startPeaking && this.peak[k] >= 0) {
                    if (!this.foundValley[k] || this.lastValues[this.peak[k]][k] - this.lastValley[k] <= PEAK_VALLEY_RANGE) {
                        this.prevDiff[k] = 0.0f;
                    } else {
                        if (argmax == k) {
                            long timestamp = System.currentTimeMillis();
                            this.stepInterval[this.intervalPos] = timestamp - this.stepTimestamp;
                            this.intervalPos = (this.intervalPos + 1) % 2;
                            this.stepTimestamp = timestamp;
                            if (areStepsEquallySpaced()) {
                                if (this.foundNonStep) {
                                    this.numStepsWithFilter += 2;
                                    if (!this.gpsAvailable) {
                                        this.totalDistance += this.strideLength * 2.0f;
                                    }
                                    this.foundNonStep = false;
                                }
                                this.numStepsWithFilter++;
                                WalkStep(this.numStepsWithFilter, this.totalDistance);
                                if (!this.gpsAvailable) {
                                    this.totalDistance += this.strideLength;
                                }
                            } else {
                                this.foundNonStep = true;
                            }
                            this.numStepsRaw++;
                            SimpleStep(this.numStepsRaw, this.totalDistance);
                            if (!this.statusMoving) {
                                this.statusMoving = true;
                                StartedMoving();
                            }
                        }
                        this.foundValley[k] = false;
                        this.prevDiff[k] = this.lastValues[this.peak[k]][k] - this.lastValley[k];
                    }
                }
                if (this.startPeaking && this.valley[k] >= 0) {
                    this.foundValley[k] = true;
                    this.lastValley[k] = this.lastValues[this.valley[k]][k];
                }
                this.lastValues[this.winPos][k] = values[k];
                k++;
            }
            this.elapsedTimestamp = System.currentTimeMillis();
            if (this.elapsedTimestamp - this.stepTimestamp > ((long) this.stopDetectionTimeout)) {
                if (this.statusMoving) {
                    this.statusMoving = false;
                    StoppedMoving();
                }
                this.stepTimestamp = this.elapsedTimestamp;
            }
            int prev = this.winPos + -1 < 0 ? 19 : this.winPos - 1;
            for (k = 0; k < 3; k++) {
                if (this.lastValues[prev][k] == this.lastValues[this.winPos][k]) {
                    float[] fArr = this.lastValues[this.winPos];
                    fArr[k] = (float) (((double) fArr[k]) + 0.001d);
                }
            }
            if (this.winPos == 19 && !this.startPeaking) {
                this.startPeaking = true;
            }
            this.winPos = (this.winPos + 1) % 20;
        }
    }

    public void onLocationChanged(Location loc) {
        if (this.statusMoving && !this.pedometerPaused && this.useGps) {
            float distDelta = 0.0f;
            this.currentLocation = loc;
            if (this.currentLocation.getAccuracy() > 10.0f) {
                setGpsAvailable(false);
                return;
            }
            setGpsAvailable(true);
            if (this.prevLocation != null) {
                distDelta = this.currentLocation.distanceTo(this.prevLocation);
                if (((double) distDelta) > 0.1d && distDelta < 10.0f) {
                    this.totalDistance += distDelta;
                    this.prevLocation = this.currentLocation;
                }
            } else {
                if (this.locationWhenGPSLost != null) {
                    this.totalDistance = this.distWhenGPSLost + (((this.totalDistance - this.distWhenGPSLost) + this.currentLocation.distanceTo(this.locationWhenGPSLost)) / 2.0f);
                }
                this.gpsStepTime = System.currentTimeMillis();
                this.prevLocation = this.currentLocation;
            }
            if (!this.calibrateSteps) {
                this.firstGpsReading = true;
                this.gpsDistance = 0.0f;
            } else if (this.firstGpsReading) {
                this.firstGpsReading = false;
                this.lastNumSteps = this.numStepsRaw;
            } else {
                this.gpsDistance += distDelta;
                this.strideLength = this.gpsDistance / ((float) (this.numStepsRaw - this.lastNumSteps));
            }
        }
    }

    public void onProviderDisabled(String provider) {
        this.distWhenGPSLost = this.totalDistance;
        this.locationWhenGPSLost = this.currentLocation;
        this.firstGpsReading = true;
        this.prevLocation = null;
        setGpsAvailable(false);
    }

    public void onProviderEnabled(String provider) {
        setGpsAvailable(true);
    }

    public void onStatusChanged(String provider, int status, Bundle data) {
    }

    public void onDelete() {
        this.sensorManager.unregisterListener(this);
        this.locationManager.removeUpdates(this);
    }
}
