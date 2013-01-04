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

package group.pals.android.lib.ui.filechooser.utils.ui;

/**
 * The listener for any task you want to assign to.
 * 
 * @author Hai Bison
 * @since v1.8
 */
public interface TaskListener {

    /**
     * Will be called after the task finished.
     * 
     * @param ok
     *            {@code true} if everything is ok, {@code false} otherwise.
     * @param any
     *            the user data, can be {@code null}.
     */
    public void onFinish(boolean ok, Object any);
}
