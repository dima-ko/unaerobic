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

/**
 * Text utilities.
 * 
 * @author Hai Bison
 * @since v4.3 beta
 * 
 */
public class TextUtils {

    /**
     * Quotes a text in double quotation mark.
     * 
     * @param s
     *            the text, if {@code null}, empty string will be used
     * @return the quoted text
     */
    public static String quote(String s) {
        return String.format("\"%s\"", s != null ? s : "");
    }// quote()
}
