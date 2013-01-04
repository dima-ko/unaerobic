/*
 *   Copyright 2012 Hai Bison
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this mFile except in compliance with the License.
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

package group.pals.android.lib.ui.filechooser;

import group.pals.android.lib.ui.filechooser.io.IFile;

/**
 * This class is used to hold data ({@link IFile}) in {@link IFileAdapter}
 * 
 * @author Hai Bison
 * 
 */
public class IFileDataModel {

    private IFile mFile;
    private boolean mSelected;
    private boolean mTobeDeleted;

    /**
     * Creates new {@link IFileDataModel} with a {@link IFile}
     * 
     * @param file
     *            {@link IFile}
     */
    public IFileDataModel(IFile file) {
        this.mFile = file;
    }

    /**
     * Gets the file which this container holds.
     * 
     * @return {@link IFile}
     */
    public IFile getFile() {
        return mFile;
    }

    /**
     * Gets the status of this item (listed in {@link android.widget.ListView})
     * 
     * @return {@code true} if the item is mSelected, {@code false} otherwise
     */
    public boolean isSelected() {
        return mSelected;
    }

    /**
     * Sets the status of this item (listed in {@link android.widget.ListView})
     * 
     * @param mSelected
     */
    public void setSelected(boolean selected) {
        if (mSelected != selected)
            mSelected = selected;
    }

    /**
     * Checks if this item is marked as to be deleted.
     * 
     * @return {@code true} if this item is marked as to be deleted.
     */
    public boolean isTobeDeleted() {
        return mTobeDeleted;
    }

    /**
     * Marks this item as to be deleted.
     * 
     * @param tobeDeleted
     *            {@code true} or {@code false}
     */
    public void setTobeDeleted(boolean tobeDeleted) {
        if (mTobeDeleted != tobeDeleted)
            mTobeDeleted = tobeDeleted;
    }
}
