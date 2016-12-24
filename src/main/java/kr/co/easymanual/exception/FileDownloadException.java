package kr.co.easymanual.exception;

import java.io.IOException;

public class FileDownloadException extends IOException {

	private static final long serialVersionUID = 6891943890966808095L;

    public FileDownloadException() {
        super();
    }

    public FileDownloadException(String s) {
        super(s);
    }
}
