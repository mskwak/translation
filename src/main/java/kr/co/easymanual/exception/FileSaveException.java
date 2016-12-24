package kr.co.easymanual.exception;

import java.io.IOException;

public class FileSaveException extends IOException {

	private static final long serialVersionUID = 6891943890966808095L;

    public FileSaveException() {
        super();
    }

    public FileSaveException(String s) {
        super(s);
    }
}
