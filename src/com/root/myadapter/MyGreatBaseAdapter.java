package com.root.myadapter;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.DataSetObserver;
import android.os.Handler;
import android.provider.ContactsContract.Contacts;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class MyGreatBaseAdapter extends BaseAdapter {
	private Context context;
	private Cursor mCursor;
	private DataSetObserver mDataSetObserver;
	private ChangeObserver mChangeObserver;
	private int mRowIDColumn;
	private boolean mDataValid;
	private boolean mAutoRequery;

	public MyGreatBaseAdapter(Context context) {
		this.context = context;
	}

	@Override
	public int getCount() {
		if (mDataValid && mCursor != null) {
			return mCursor.getCount();
		} else {
			return 0;
		}
	}

	@Override
	public Object getItem(int position) {
		if (mDataValid && mCursor != null) {
			mCursor.moveToPosition(position);
			String name = mCursor.getString(mCursor
					.getColumnIndex(Contacts.DISPLAY_NAME));

			return name;
		} else {
			return null;
		}

	}

	@Override
	public long getItemId(int position) {
		if (mDataValid && mCursor != null) {
			if (mCursor.moveToPosition(position)) {
				return mCursor.getLong(mRowIDColumn);
			} else {
				return 0;
			}
		} else {
			return 0;
		}

	}

	static class ViewHolder {
		protected TextView textView;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.item, null);
			holder = new ViewHolder();
			holder.textView = (TextView) convertView
					.findViewById(R.id.textView1);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		String item = (String) getItem(position);

		holder.textView.setText(item);

		return convertView;

	}

	public Cursor swapCursor(Cursor newCursor) {
		if (newCursor == mCursor) {
			return null;
		}
		Cursor oldCursor = mCursor;
		if (oldCursor != null) {
			if (mChangeObserver != null)
				oldCursor.unregisterContentObserver(mChangeObserver);
			if (mDataSetObserver != null)
				oldCursor.unregisterDataSetObserver(mDataSetObserver);
		}
		mCursor = newCursor;
		if (newCursor != null) {
			if (mChangeObserver != null)
				newCursor.registerContentObserver(mChangeObserver);
			if (mDataSetObserver != null)
				newCursor.registerDataSetObserver(mDataSetObserver);
			mRowIDColumn = newCursor.getColumnIndexOrThrow("_id");
			mDataValid = true;
			// notify the observers about the new cursor
			notifyDataSetChanged();
		} else {
			mRowIDColumn = -1;
			mDataValid = false;
			// notify the observers about the lack of a data set
			notifyDataSetInvalidated();
		}
		return oldCursor;

	}

	protected void onContentChanged() {
		if (mAutoRequery && mCursor != null && !mCursor.isClosed()) {
			if (false)
			mDataValid = mCursor.requery();
		}
	}

	public class ChangeObserver extends ContentObserver {

		public ChangeObserver() {
			super(new Handler());
		}

		@Override
		public boolean deliverSelfNotifications() {
			return true;
		}

		@Override
		public void onChange(boolean selfChange) {
			onContentChanged();
		}

	}
}