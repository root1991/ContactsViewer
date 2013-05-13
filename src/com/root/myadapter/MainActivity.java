package com.root.myadapter;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.app.Activity;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.view.Menu;
import android.widget.ListView;

public class MainActivity extends Activity implements LoaderCallbacks<Cursor> {
	private MyGreatBaseAdapter mAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		getLoaderManager().initLoader(0, null, this);
		mAdapter = new MyGreatBaseAdapter(this);

		ListView lv = (ListView) findViewById(R.id.listView1);
		lv.setAdapter(mAdapter);
	}
//.....
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		CursorLoader cursorLoader = new CursorLoader(this,
				ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
		return cursorLoader;
	}

	@Override
	public void onLoaderReset(Loader<Cursor> loader) {
		mAdapter.swapCursor(null);

	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		mAdapter.swapCursor(data);

	}

}
