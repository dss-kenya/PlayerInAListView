package com.example.androiddemotwo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;

public class MainActivity extends ActionBarActivity {
	private ListView mLstView;
	private MyAdapter mAdpater;
	private List<FileData> mFileList;
	private boolean isRecording;
	private String outputFile;
	private MediaRecorder myRecorder;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mLstView = (ListView)findViewById(android.R.id.list);
		mFileList = StaticDataManager.getFileList();
		
		mFileList = getFileList();
		mAdpater = new MyAdapter(getApplicationContext(), 
				R.layout.individual_item, mFileList);
		mLstView.setAdapter(mAdpater);
		
		mLstView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		mLstView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				mAdpater.setSelectedIndex(position);
				mAdpater.notifyDataSetChanged();
			}
		});
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnStart:
			if(!isRecording) {
				// start

				File output = new File(Environment.getExternalStorageDirectory(),"temp_folder");
				output = new File(output, "VoiceNotes");
				
				output.mkdirs();
				
				outputFile = Environment.getExternalStorageDirectory().
						getAbsolutePath() + "/temp_folder/VoiceNotes/" + System.currentTimeMillis() + "."
						+ "3gpp";

				myRecorder = new MediaRecorder();
				myRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
				myRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
				myRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
				myRecorder.setOutputFile(outputFile);

				try {
					myRecorder.prepare();
				} catch (IllegalStateException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				myRecorder.start();
				isRecording = true;
			}
			break;

		case R.id.btnStop:
			if(isRecording) {
				// stop
				isRecording = false;
				myRecorder.stop();
				myRecorder.reset();
				myRecorder.release();
				myRecorder = null;
			}

			mFileList = getFileList();
			mAdpater = new MyAdapter(getApplicationContext(), 
					R.layout.individual_item, mFileList);
			mLstView.setAdapter(mAdpater);

			break;

		default:
			break;
		}
	}

	private List<FileData> getFileList() {
		List<FileData> fileDataList = new ArrayList<>();
		File sdcard = new File(Environment.getExternalStorageDirectory(),"temp_folder");
		File actual = new File(sdcard,"VoiceNotes");

		if(actual.listFiles() != null) {
			for(File f:actual.listFiles()) {
				if(f.isFile()) {
					if(f.getAbsolutePath().endsWith("3gpp")) {
						FileData fileData = new FileData();
						fileData.setFileName(f.getAbsolutePath());
						fileData.setFileId(f.getName());
						fileDataList.add(fileData);
					}
				}
			}
		}
		return fileDataList;
	}
}
