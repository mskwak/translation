package kr.co.easymanual;

import java.io.File;
import java.io.IOException;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;

// https://drewnoakes.com/code/exif
public class GetImageMetaData {
	public static void main(String[] args) {
		File file = new File("/20161112_161054.jpg");

		try {
			Metadata metadata = ImageMetadataReader.readMetadata(file);

			for (Directory directory : metadata.getDirectories()) {

			    for (Tag tag : directory.getTags()) {
			        // System.out.format("[%s] - %s = %s", directory.getName(), tag.getTagName(), tag.getDescription());
			    	System.out.println(directory.getName() + ",    " + tag.getTagName() + ",    " + tag.getDescription());


			    }

			    if (directory.hasErrors()) {
			        for (String error : directory.getErrors()) {
			            System.err.format("ERROR: %s", error);
			        }
			    }
			}
		} catch (ImageProcessingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
