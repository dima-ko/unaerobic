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

public class Converter {

    /**
     * Converts {@code size} (in bytes) to string. This tip is from:
     * http://stackoverflow.com/a/5599842/942821
     * 
     * @param size
     *            the size in bytes.
     * @return e.g.:<br>
     *         - 128 B<br>
     *         - 1.5 KB<br>
     *         - 10 MB<br>
     *         - ...
     */
    public static String sizeToStr(double size) {
        if (size <= 0)
            return "0 B";
        final String[] _units = new String[] { "B", "KB", "MB", "GB", "TB" };
        final Short _blockSize = 1024;

        int digitGroups = (int) (Math.log10(size) / Math.log10(_blockSize));
        if (digitGroups >= _units.length)
            digitGroups = _units.length - 1;
        size = size / Math.pow(_blockSize, digitGroups);

        return String.format(String.format("%s %%s", digitGroups == 0 ? "%,.0f" : "%,.2f"), size, _units[digitGroups]);
    }// sizeToStr()
}
