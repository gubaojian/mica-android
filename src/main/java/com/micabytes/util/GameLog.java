/*
 * Copyright 2015 MicaByte Systems
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package com.micabytes.util;

import android.util.Log;

import com.crashlytics.android.Crashlytics;

import org.jetbrains.annotations.NonNls;

import io.fabric.sdk.android.Fabric;

/*
 * This GameLogger uses Fabric and Crashlytics to log exceptions and useful diagnostics information.
 */
@SuppressWarnings("UtilityClass")
@NonNls
public final class GameLog {
  @SuppressWarnings("PublicField") public static boolean debug = true;
  private static final String t = "MicaBytes";

  private GameLog() {
    // NOOP
  }

  public static void i(String tag, String s) {
    //if (debug) Log.i(tag, s);
    if (Fabric.isInitialized())
      Crashlytics.log(Log.INFO, t, s);
  }

  public static void v(String tag, String s) {
    if (debug) Log.v(t, s);
  }

  public static void d(String tag, String s) {
    if (debug) Log.d(t, s);
  }

  public static void w(String tag, String s) {
    //if (debug)
    //  Log.w(tag, s);
    if (Fabric.isInitialized())
      Crashlytics.log(Log.WARN, t, s);
  }

  public static void e(String tag, String s) {
    //if (debug)
    //  Log.e(tag, s);
    if (Fabric.isInitialized())
      Crashlytics.log(Log.ERROR, t, s);
  }

  public static void logException(Exception e) {
    //if (debug) {
    //noinspection CallToPrintStackTrace
    //  e.printStackTrace();
    //}
    if (Fabric.isInitialized()) {
      Crashlytics.logException(e);
    }
  }

}
