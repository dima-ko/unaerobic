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

package group.pals.android.lib.ui.filechooser.services;

import group.pals.android.lib.ui.filechooser.io.IFile;
import group.pals.android.lib.ui.filechooser.io.IFileFilter;
import group.pals.android.lib.ui.filechooser.io.localfile.LocalFile;
import group.pals.android.lib.ui.filechooser.utils.FileComparator;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.os.Environment;

/**
 * This is simple local file provider - as its name means.<br>
 * It handles file request on local device.
 * 
 * @author Hai Bison
 * @since v2.1 alpha
 */
public class LocalFileProvider extends FileProviderService {

    /*-------------------------------------------------------------------
     * Service
     */

    @Override
    public void onCreate() {
        // TODO
    }// onCreate()

    /*-------------------------------------------------------------------
     * IFileProvider
     */

    @Override
    public IFile defaultPath() {
        File res = Environment.getExternalStorageDirectory();
        return res == null ? fromPath("/") : new LocalFile(res);
    }// defaultPath()

    @Override
    public IFile fromPath(String pathname) {
        return new LocalFile(pathname);
    }// defaultPath()

    @Override
    public IFile[] listFiles(IFile dir, final boolean[] hasMoreFiles) throws Exception {
        if (!dir.canRead())
            return null;

        List<IFile> files = listAllFiles(dir, hasMoreFiles);
        if (files == null)
            return null;
        return files.toArray(new IFile[files.size()]);
    }// listFiles()

    @Override
    public List<IFile> listAllFiles(IFile dir, final boolean[] hasMoreFiles) throws Exception {
        if (!(dir instanceof File) || !dir.canRead())
            return null;

        if (hasMoreFiles != null && hasMoreFiles.length > 0)
            hasMoreFiles[0] = false;

        final List<IFile> _files = new ArrayList<IFile>();

        try {
            File[] files = ((File) dir).listFiles(new FileFilter() {

                @Override
                public boolean accept(File pathname) {
                    LocalFile file = new LocalFile(pathname);
                    if (!LocalFileProvider.this.accept(file))
                        return false;
                    if (_files.size() >= getMaxFileCount()) {
                        if (hasMoreFiles != null && hasMoreFiles.length > 0)
                            hasMoreFiles[0] = true;
                        return false;
                    }
                    _files.add(file);
                    return false;
                }// accept()
            });// dir.listFiles()

            if (files != null) {
                Collections.sort(_files, new FileComparator(getSortType(), getSortOrder()));
                return _files;
            }

            return null;
        } catch (Throwable t) {
            return null;
        }
    }// listAllFiles()

    @Override
    public List<IFile> listAllFiles(IFile dir) throws Exception {
        if (!(dir instanceof File) || !dir.canRead())
            return null;

        try {
            final List<IFile> _files = new ArrayList<IFile>();

            File[] files = ((File) dir).listFiles(new FileFilter() {

                @Override
                public boolean accept(File pathname) {
                    _files.add(new LocalFile(pathname));
                    return false;
                }
            });

            if (files != null)
                return _files;
            return null;
        } catch (Throwable t) {
            return null;
        }
    }// listAllFiles()

    @Override
    public List<IFile> listAllFiles(IFile dir, final IFileFilter filter) {
        if (!(dir instanceof File))
            return null;

        final List<IFile> _res = new ArrayList<IFile>();
        try {
            File[] files = ((File) dir).listFiles(new FileFilter() {

                @Override
                public boolean accept(File pathname) {
                    LocalFile file = new LocalFile(pathname);
                    if (filter == null || filter.accept(file))
                        _res.add(file);
                    return false;
                }
            });
            if (files != null)
                return _res;
            return null;
        } catch (Throwable t) {
            return null;
        }
    }// listAllFiles()

    @Override
    public boolean accept(IFile pathname) {
        if (!isDisplayHiddenFiles() && pathname.getName().startsWith("."))
            return false;

        switch (getFilterMode()) {
        case FilesOnly:
            if (getRegexFilenameFilter() != null && pathname.isFile())
                return pathname.getName().matches(getRegexFilenameFilter());
            return true;

        case DirectoriesOnly:
            return pathname.isDirectory();

        default:
            if (getRegexFilenameFilter() != null && pathname.isFile())
                return pathname.getName().matches(getRegexFilenameFilter());
            return true;
        }// switch
    }// accept()
}
