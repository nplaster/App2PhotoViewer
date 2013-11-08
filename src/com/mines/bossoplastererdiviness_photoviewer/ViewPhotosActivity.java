package com.mines.bossoplastererdiviness_photoviewer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.dropbox.sync.android.DbxAccountManager;
import com.dropbox.sync.android.DbxException;
import com.dropbox.sync.android.DbxException.Unauthorized;
import com.dropbox.sync.android.DbxAccount;
import com.dropbox.sync.android.DbxFile;
import com.dropbox.sync.android.DbxFileInfo;
import com.dropbox.sync.android.DbxFileSystem;
import com.dropbox.sync.android.DbxPath;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView.OnItemClickListener;

public class ViewPhotosActivity extends Activity {

	GridView gridView;
	private List<DbxFileInfo> filesInfo;
	private DbxFileSystem fileSystem;
	private DbxAccountManager accountManager;
	private ArrayList<Bitmap> pix;



	public ViewPhotosActivity(){
		filesInfo = new ArrayList<DbxFileInfo>();
		pix = new ArrayList<Bitmap>();


	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		accountManager = DbxAccountManager.getInstance(getApplicationContext(), MainActivity.APP_KEY, MainActivity.APP_SECRET);
		//DbxAccountManager accManager = DbxAccountManager.getInstance(getApplicationContext(), MainActivity.APP_KEY, MainActivity.APP_SECRET);
		try {
			fileSystem = DbxFileSystem.forAccount(accountManager.getLinkedAccount());
		} catch (Unauthorized e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			filesInfo = fileSystem.listFolder(DbxPath.ROOT);
		} catch (DbxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		for (DbxFileInfo fileInfo: filesInfo) {
			String filename = fileInfo.path.getName();
			DbxFile file;
			try{

				file = fileSystem.open(fileInfo.path);
				Bitmap image = BitmapFactory.decodeStream(file.getReadStream());
				pix.add(image);
				file.close();
			}catch (DbxException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			System.gc();

		}




		setContentView(R.layout.view_photos);

		gridView = (GridView) findViewById(R.id.gridView1);

		ImageAdapter adapter = new ImageAdapter(this, pix);

		//		ArrayAdapter<Bitmap> adapter = new ArrayAdapter<Bitmap>(this,
		//				android.R.layout.grid_item_image, pix);

		gridView.setAdapter(adapter);

		//		gridView.setOnItemClickListener(new OnItemClickListener() {
		//			public void onItemClick(AdapterView<?> parent, View v,
		//					int position, long id) {
		//				Toast.makeText(getApplicationContext(),
		//						((TextView) v).getText(), Toast.LENGTH_SHORT).show();
		//			}
		//		});

	}

}