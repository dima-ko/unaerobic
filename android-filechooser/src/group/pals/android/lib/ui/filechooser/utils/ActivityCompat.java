/*
 *   Copyright 2012 Hai Bison
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package group.pals.android.lib.ui.filechooser.utils;

import android.app.Activity;
import android.os.Build;

/**
 * Helper for accessing features in {@link Activity} introduced in newer API
 * levels in a backwards compatible fashion.<br>
 * <br>
 * <b>Note:</b> You must check API level first with
 * {@link Build.VERSION#SDK_INT} and {@link Build.VERSION_CODES}.
 * 
 * @author Hai Bison
 * @since v4.3 beta
 * 
 */
public class ActivityCompat {

    /**
     * @see {@link Activity#invalidateOptionsMenu()}
     * @param a
     *            {@link Activity}
     */
    public static void invalidateOptionsMenu(Activity a) {
        a.invalidateOptionsMenu();
    }
}
