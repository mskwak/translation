package kr.co.easymanual.controller;

import java.io.IOException;
import java.util.Map;

import kr.co.easymanual.service.FileDownloadService;
import kr.co.easymanual.service.SearchTerminologyService;
import kr.co.easymanual.utils.Return;

import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SearchTerminology {
	private static final Logger logger = LoggerFactory.getLogger(SearchTerminology.class);

	@Autowired
	private SearchTerminologyService searchTerminologyService;

	@Autowired
	private FileDownloadService fileDownloadService;

	@RequestMapping(value = {"/search.do"}, method = {RequestMethod.GET})
	public String searchTerminologyGet(Model model) {
		model.addAttribute("charsetMap", this.fileDownloadService.getCharsetMap());
		return "search";
	}

	@RequestMapping(value = {"/search.do"}, method = {RequestMethod.POST})
	@ResponseBody
	public Map<String, Object> searchTerminologyPost(
			@RequestParam(value = "word") String word,
			@RequestParam(value = "charset") String charset,
			@RequestParam(value = "handler") String handler) throws SolrServerException, IOException {

		try {
			return Return.success(this.searchTerminologyService.searchTerminology(word, charset, handler));
		} catch (SolrServerException e) {
			logger.error("", e);
			throw new SolrServerException(word);
		} catch (IOException e) {
			logger.error("", e);
			throw new IOException();
		}
	}

//  1. try ~ catch로 잡는 방식. @ControllerAdvice를 타지 않는다.
//	@RequestMapping(value = {"/searchTerminology.do"}, method = {RequestMethod.POST})
//	public ModelAndView searchTerminologyPostAjax(@RequestParam(value = "word") String word, ModelAndView modelAndView) {
//		modelAndView.setViewName("jsonView");
//		try {
//			modelAndView.addObject("searchResults", this.searchTerminologyManager.searchTerminology(word));
//		} catch (SolrServerException | IOException e) {
//			logger.error("", e);
//		}
//		return modelAndView;
//	}

//  2. throws 로 잡는 방식.  @ControllerAdvice를 탄다.
//	@RequestMapping(value = {"/searchTerminology.do"}, method = {RequestMethod.POST})
//	public ModelAndView searchTerminologyPostAjax(@RequestParam(value = "word") String word, ModelAndView modelAndView) throws SolrServerException, IOException {
//		modelAndView.setViewName("jsonView");
//		modelAndView.addObject("searchResults", this.searchTerminologyManager.searchTerminology(word));
//		return modelAndView;
//	}
}
