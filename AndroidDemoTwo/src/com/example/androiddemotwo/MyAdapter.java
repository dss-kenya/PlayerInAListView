package com.example.androiddemotwo;

import java.util.List;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class MyAdapter extends ArrayAdapter<FileData>{
	private Context mContext;
	private int RESOURCE;
	private LayoutInflater mInflater;
	private List<FileData> mFileList;
	private ViewHolder vh;
	private int selectedIndex = -1;
	private MediaPlayer mPlayer;
	private Handler mHandler;
	private SeekBar sb;  
	
	public MyAdapter(Context context, int resource, List<FileData> objects) {
		super(context, resource, objects);
		mContext = context;
		RESOURCE = resource;
		mFileList = objects;
	}
	
	public void setSelectedIndex(int index){
	    selectedIndex = index;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		vh = null;
		
		if(view == null) {
			mInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = mInflater.inflate(RESOURCE, parent, false);
			vh = new ViewHolder();
			vh.txtId = (TextView)view.findViewById(R.id.txtId);
			vh.txtFileName = (TextView)view.findViewById(R.id.txtFileName);
			vh.relTop = (RelativeLayout)view.findViewById(R.id.relTop);
			vh.relDetails = (RelativeLayout)view.findViewById(R.id.relDetails);
			vh.imgPlay = (ImageView)view.findViewById(R.id.imgPlay);
			vh.sb = (SeekBar)view.findViewById(R.id.seekBar);
			view.setTag(vh);
		}else {
			vh = (ViewHolder)view.getTag();
		}
		
		FileData fileData = mFileList.get(position);
		vh.txtId.setText(fileData.getFileId());
		vh.txtFileName.setText(fileData.getFileName());
		
		vh.sb.setProgress(0);
		
		if(selectedIndex != position) {
			vh.relDetails.setVisibility(View.GONE);
		}else {
			vh.relDetails.setVisibility(View.VISIBLE);
		}
		
		vh.imgPlay.setTag(String.valueOf(position));
		vh.imgPlay.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				FileData f = mFileList.get(selectedIndex);
				View view = (View)v.getParent();
				sb  = (SeekBar)view.findViewById(R.id.seekBar);
				
				ImageView img = (ImageView)v;
				
				Runnable r = new Runnable() {
					@Override
					public void run() {
						if(mPlayer != null && mPlayer.isPlaying()) {
							sb.setProgress(mPlayer.getCurrentPosition());
							mHandler.postDelayed(this, 1);
						}
					}
				};
				
				if(Integer.parseInt((String)img.getTag()) == selectedIndex) {
					if(mPlayer != null && mPlayer.isPlaying()) {
						mPlayer.stop();
						mPlayer.release();
						mPlayer = null;
						mHandler.removeCallbacks(r);
					}else {
						mPlayer = MediaPlayer.create(mContext, Uri.parse(f.getFileName()));
						mPlayer.start();
						sb.setMax(mPlayer.getDuration());
						sb.setProgress(mPlayer.getCurrentPosition());
						mHandler = new Handler(Looper.getMainLooper());
						mHandler.postDelayed(r, 0);
					}
				}
 			}
		});
		
		return view;
	}
	
	static class ViewHolder {
		TextView txtId;
		TextView txtFileName;
		RelativeLayout relTop;
		RelativeLayout relDetails;
		ImageView imgPlay;
		SeekBar sb;
	}
}
