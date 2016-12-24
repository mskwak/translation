package kr.co.easymanual.controller;

import java.io.IOException;
import java.util.Map;

import kr.co.easymanual.service.SearchGlossaryManager;
import kr.co.easymanual.utils.Return;

import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SearchGlossary {
	private static final Logger logger = LoggerFactory.getLogger(SearchGlossary.class);

	@Autowired
	private SearchGlossaryManager searchGlossaryManager;

	@RequestMapping(value = {"/", "/searchGlossary.do"}, method = {RequestMethod.GET})
	public String searchGlossaryGet() {
		return "tiles:searchGlossary";
	}

//  1. try ~ catch로 잡는 방식. @ControllerAdvice를 타지 않는다.
//	@RequestMapping(value = {"/searchGlossary.do"}, method = {RequestMethod.POST})
//	public ModelAndView searchGlossaryPostAjax(@RequestParam(value = "word") String word, ModelAndView modelAndView) {
//		modelAndView.setViewName("jsonView");
//		try {
//			modelAndView.addObject("searchResults", this.searchGlossaryManager.searchGlossary(word));
//		} catch (SolrServerException | IOException e) {
//			logger.error("", e);
//		}
//		return modelAndView;
//	}

//  2. throws 로 잡는 방식.  @ControllerAdvice를 탄다.
//	@RequestMapping(value = {"/searchGlossary.do"}, method = {RequestMethod.POST})
//	public ModelAndView searchGlossaryPostAjax(@RequestParam(value = "word") String word, ModelAndView modelAndView) throws SolrServerException, IOException {
//		modelAndView.setViewName("jsonView");
//		modelAndView.addObject("searchResults", this.searchGlossaryManager.searchGlossary(word));
//		return modelAndView;
//	}



	@RequestMapping(value = {"/searchGlossary.do"}, method = {RequestMethod.POST})
	@ResponseBody
	public Map<String, Object> searchGlossaryPost(@RequestParam(value = "word") String word) throws SolrServerException, IOException {

		try {
			return Return.success(this.searchGlossaryManager.searchGlossary(word));
		} catch (SolrServerException e) {
			logger.error("", e);
			throw new SolrServerException(word);
		} catch (IOException e) {
			logger.error("", e);
			throw new IOException();
		}
	}
}
