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

import group.pals.android.lib.ui.filechooser.io.IFile;
import group.pals.android.lib.ui.filechooser.services.IFileProvider;

import java.util.Comparator;

/**
 * {@link IFile} comparator.<br>
 * Rules:<br>
 * - directories first;<br>
 * - other properties are based on parameters given in constructor, see
 * {@link #FileComparator(IFileProvider.SortType, IFileProvider.SortOrder)};
 * 
 * @author Hai Bison
 * @since v1.91
 */
public class FileComparator implements Comparator<IFile> {

    private final IFileProvider.SortType mSortType;
    private final IFileProvider.SortOrder mSortOrder;

    /**
     * Creates new {@link FileComparator}
     * 
     * @param sortType
     *            see {@link IFileProvider.SortType}
     * @param sortOrder
     *            see {@link IFileProvider.SortOrder}
     */
    public FileComparator(IFileProvider.SortType sortType, IFileProvider.SortOrder sortOrder) {
        mSortType = sortType;
        mSortOrder = sortOrder;
    }

    @Override
    public int compare(IFile lhs, IFile rhs) {
        if ((lhs.isDirectory() && rhs.isDirectory()) || (lhs.isFile() && rhs.isFile())) {
            // default is to compare by name (case insensitive)
            int res = lhs.getName().compareToIgnoreCase(rhs.getName());

            switch (mSortType) {
            case SortByName:
                break;// SortByName

            case SortBySize:
                if (lhs.length() > rhs.length())
                    res = 1;
                else if (lhs.length() < rhs.length())
                    res = -1;
                break;// SortBySize

            case SortByDate:
                if (lhs.lastModified() > rhs.lastModified())
                    res = 1;
                else if (lhs.lastModified() < rhs.lastModified())
                    res = -1;
                break;// SortByDate
            }

            return mSortOrder == IFileProvider.SortOrder.Ascending ? res : -res;
        }

        return lhs.isDirectory() ? -1 : 1;
    }// compare
}
