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

import group.pals.android.lib.ui.filechooser.R;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Adapter for context menu.
 * 
 * @author Hai Bison
 * @since v4.3 beta
 * 
 */
public class MenuItemAdapter extends BaseAdapter {

    private final Context mContext;
    private final Integer[] mItems;
    private final int mPadding;
    private final int mItemPaddingLeft;

    /**
     * Creates new instance.<br>
     * 
     * @param context
     *            {@link Context}
     * @param itemIds
     *            array of resource IDs of titles to be used.
     */
    public MenuItemAdapter(Context context, Integer[] itemIds) {
        mContext = context;
        mItems = itemIds;

        mPadding = mContext.getResources().getDimensionPixelSize(R.dimen.afc_5dp);
        mItemPaddingLeft = mContext.getResources()
                .getDimensionPixelSize(R.dimen.afc_context_menu_item_padding_left);
    }// MenuItemAdapter()

    @Override
    public int getCount() {
        return mItems.length;
    }

    @Override
    public Object getItem(int position) {
        return mItems[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.afc_context_menu_tiem, null);
        }

        ((TextView) convertView).setText(mItems[position]);
        convertView.setPadding(mItemPaddingLeft, mPadding, mPadding, mPadding);

        return convertView;
    }
}