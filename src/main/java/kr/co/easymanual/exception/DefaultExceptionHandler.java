package kr.co.easymanual.exception;

import java.io.IOException;
import java.util.Map;

import kr.co.easymanual.utils.Return;

import org.apache.solr.client.solrj.SolrServerException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class DefaultExceptionHandler {
	@ExceptionHandler({IOException.class})
	@ResponseBody
	public Map<String, Object> handleException(IOException ex) {
		return Return.fail();
	}

	@ExceptionHandler({SolrServerException.class})
	@ResponseBody
	public Map<String, Object> handleException(SolrServerException ex) {
		return Return.fail();
	}

	@ExceptionHandler({FileDownloadException.class})
	public String handleException(FileDownloadException ex) {
		return "downloadError";
	}
}
