package kr.co.easymanual;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.ImageInputStream;

// 내가 원하는 데이터(생성일, 위도, 경도)를 뽑아낼 수 있는 라이브러리는 아닌 것으로 판단이 된다.
// 내가 원하는 데이터(생성일, 위도, 경도)를 뽑아내려면 https://www.drewnoakes.com/code/exif 에 있는 라이브러리를 사용해야 한다.
@Deprecated
public class ProcessImageMain {
	public static void main(String[] args) {
		File file = new File("/2011-09-22 22.13.05.jpg");
		//File file = new File("/그룹웨어 에러.png");

		if(file.exists()) {
			System.out.println("exist");
			System.out.println(file.getName());
		} else {
			System.out.println("no exist");
			System.exit(0);
		}

		try {
			ImageInputStream imageInputStream = ImageIO.createImageInputStream(file);
			Iterator<ImageReader> iterator = ImageIO.getImageReaders(imageInputStream);

			while(iterator.hasNext()) {
				ImageReader imageReader = iterator.next();
				imageReader.setInput(imageInputStream, true);
				IIOMetadata iioMetadata = imageReader.getImageMetadata(0);

				String[] names = iioMetadata.getMetadataFormatNames();
				//String[] names = iioMetadata.getExtraMetadataFormatNames();

				for(int i = 0, length = names.length; i < length; i++) {
					System.out.println(i + ": " + names[i]);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
