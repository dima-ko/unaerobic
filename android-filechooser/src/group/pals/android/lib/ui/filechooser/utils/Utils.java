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

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * Utilities.
 */
public class Utils {

    /**
     * Checks if the app has <b>all</b> {@code permissions} granted.
     * 
     * @param context
     *            {@link Context}
     * @param permissions
     *            list of permission names.
     * @return {@code true} if the app has all {@code permissions} asked.
     */
    public static boolean hasPermissions(Context context, String... permissions) {
        for (String p : permissions)
            if (context.checkCallingOrSelfPermission(p) == PackageManager.PERMISSION_DENIED)
                return false;
        return true;
    }// hasPermissions()
}
