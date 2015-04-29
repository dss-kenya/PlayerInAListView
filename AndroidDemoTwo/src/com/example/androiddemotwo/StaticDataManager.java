package com.example.androiddemotwo;

import java.util.ArrayList;
import java.util.List;

public class StaticDataManager {
	public static List<FileData> getFileList() {
		List<FileData> fileList = new ArrayList<>();
		for(int i=0;i<25;i++) {
			FileData fileData = new FileData();
			fileData.setFileId(String.valueOf(i+1));
			fileData.setFileName("File " + (i+1));
			fileList.add(fileData);
		}
		return fileList;
	}
}
