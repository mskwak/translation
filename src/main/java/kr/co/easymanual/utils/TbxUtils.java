package kr.co.easymanual.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class TbxUtils {
	private static final Logger logger = LoggerFactory.getLogger(TbxUtils.class);

	private static final Map<String, String> map = new ConcurrentHashMap<String, String>();

	@Deprecated
	private static final String delimiter = "|";

	// TODO 외부에서 설정 가능하도록 해야 한다.
	static {
		map.put("aa-dj", "Djibouti");
		map.put("aa-er", "Eritrea");
		map.put("aa-et", "Ethiopia");
		map.put("af-za", "South Africa");
		map.put("am-et", "Ethiopia");
		map.put("an-es", "Spain");
		map.put("ar-ae", "United Arab Emirates");
		map.put("ar-bh", "Bahrain");
		map.put("ar-dz", "Algeria");
		map.put("ar-eg", "Egypt");
		map.put("ar-in", "India");
		map.put("ar-iq", "Iraq");
		map.put("ar-jo", "Jordan");
		map.put("ar-kw", "Kuwait");
		map.put("ar-lb", "Lebanon");
		map.put("ar-ly", "Libya");
		map.put("ar-ma", "Morocco");
		map.put("ar-om", "Oman");
		map.put("ar-qa", "Qatar");
		map.put("ar-sa", "Saudi Arabia");
		map.put("ar-sd", "Sudan");
		map.put("ar-sy", "Syrian Arab Republic");
		map.put("ar-tn", "Tunisia");
		map.put("ar-ye", "Yemen");
		map.put("as-in", "India");
		map.put("ast-es", "Spain");
		map.put("az-az", "Azerbaijan");
		map.put("be-by", "Belarus");
		map.put("ber-dz", "Algeria");
		map.put("ber-ma", "Morocco");
		map.put("bg-bg", "Bulgaria");
		map.put("bn-bd", "Bangladesh");
		map.put("bn-in", "India");
		map.put("bo-cn", "China");
		map.put("bo-in", "India");
		map.put("br-fr", "France");
		map.put("bs-ba", "Bosnia and Herzegovina");
		map.put("byn-er", "Eritrea");
		map.put("ca-ad", "Andorra");
		map.put("ca-es", "Spain");
		map.put("ca-fr", "France");
		map.put("ca-it", "Italy");
		map.put("crh-ua", "Ukraine");
		map.put("cs-cz", "Czech Republic");
		map.put("csb-pl", "Poland");
		map.put("cv-ru", "Russian Federation");
		map.put("cy-gb", "United Kingdom(Great Britain)");
		map.put("da-dk", "Denmark");
		map.put("de-at", "Austria");
		map.put("de-be", "Belgium");
		map.put("de-ch", "Switzerland");
		map.put("de-de", "Germany");
		map.put("de-lu", "Luxembourg");
		map.put("dv-mv", "Maldives");
		map.put("dz-bt", "Bhutan");
		map.put("el-cy", "Cyprus");
		map.put("el-gr", "Greece");
		map.put("en-ag", "Antigua and Barbuda");
		map.put("en-au", "Australia");
		map.put("en-bw", "Botswana");
		map.put("en-ca", "Canada");
		map.put("en-dk", "Denmark");
		map.put("en-gb", "United Kingdom(Great Britain)");
		map.put("en-hk", "Hong Kong");
		map.put("en-ie", "Ireland");
		map.put("en-in", "India");
		map.put("en-ng", "Nigeria");
		map.put("en-nz", "New Zealand");
		map.put("en-ph", "Philippines");
		map.put("en-sg", "Singapore");
		map.put("en-us", "United States of America(USA)");
		map.put("en-za", "South Africa");
		map.put("en-zw", "Zimbabwe");
		map.put("es-ar", "Argentina");
		map.put("es-bo", "Bolivia");
		map.put("es-cl", "Chile");
		map.put("es-co", "Colombia");
		map.put("es-cr", "Costa Rica");
		map.put("es-do", "Dominican Republic");
		map.put("es-ec", "Ecuador");
		map.put("es-es", "Spain");
		map.put("es-gt", "Guatemala");
		map.put("es-hn", "Honduras");
		map.put("es-mx", "Mexico");
		map.put("es-ni", "Nicaragua");
		map.put("es-pa", "Panama");
		map.put("es-pe", "Peru");
		map.put("es-pr", "Puerto Rico");
		map.put("es-py", "Paraguay");
		map.put("es-sv", "El Salvador");
		map.put("es-us", "United States of America(USA)");
		map.put("es-uy", "Uruguay");
		map.put("es-ve", "Venezuela");
		map.put("et-ee", "Estonia");
		map.put("eu-es", "Spain");
		map.put("fa-ir", "Iran, Islamic Republic of");
		map.put("fi-fi", "Finland");
		map.put("fil-ph", "Philippines");
		map.put("fo-fo", "Faroe Islands");
		map.put("fr-be", "Belgium");
		map.put("fr-ca", "Canada");
		map.put("fr-ch", "Switzerland");
		map.put("fr-fr", "France");
		map.put("fr-lu", "Luxembourg");
		map.put("fur-it", "Italy");
		map.put("fy-de", "Germany");
		map.put("fy-nl", "Netherlands");
		map.put("ga-ie", "Ireland");
		map.put("gd-gb", "United Kingdom(Great Britain)");
		map.put("gez-er", "Eritrea");
		map.put("gez-et", "Ethiopia");
		map.put("gl-es", "Spain");
		map.put("gu-in", "India");
		map.put("gv-gb", "United Kingdom(Great Britain)");
		map.put("ha-ng", "Nigeria");
		map.put("he-il", "Israel");
		map.put("hi-in", "India");
		map.put("hne-in", "India");
		map.put("hr-hr", "Croatia");
		map.put("hsb-de", "Germany");
		map.put("ht-ht", "Haiti");
		map.put("hu-hu", "Hungary");
		map.put("hy-am", "Armenia");
		map.put("id-id", "Indonesia");
		map.put("ig-ng", "Nigeria");
		map.put("ik-ca", "Canada");
		map.put("is-is", "Iceland");
		map.put("it-ch", "Switzerland");
		map.put("it-it", "Italy");
		map.put("iu-ca", "Canada");
		map.put("iw-il", "Israel");
		map.put("ja-jp", "Japan");
		map.put("ka-ge", "Georgia");
		map.put("kk-kz", "Kazakhstan");
		map.put("kl-gl", "Greenland");
		map.put("km-kh", "Cambodia");
		map.put("kn-in", "India");
		map.put("ko-kr", "South Korea");
		map.put("kok-in", "India");
		map.put("ks-in", "India");
		map.put("ku-tr", "Turkey");
		map.put("kw-gb", "United Kingdom(Great Britain)");
		map.put("ky-kg", "Kyrgyzstan");
		map.put("lg-ug", "Uganda");
		map.put("li-be", "Belgium");
		map.put("li-nl", "Netherlands");
		map.put("lo-la", "Lao People\"s Democratic Republic");
		map.put("lt-lt", "Lithuania");
		map.put("lv-lv", "Latvia");
		map.put("mai-in", "India");
		map.put("mg-mg", "Madagascar");
		map.put("mi-nz", "New Zealand");
		map.put("mk-mk", "Macedonia, the Former Yugoslav Republic of");
		map.put("ml-in", "India");
		map.put("mn-mn", "Mongolia");
		map.put("mr-in", "India");
		map.put("ms-my", "Malaysia");
		map.put("mt-mt", "Malta");
		map.put("my-mm", "Myanmar");
		map.put("nb-no", "Norway");
		map.put("nds-de", "Germany");
		map.put("nds-nl", "Netherlands");
		map.put("ne-np", "Nepal");
		map.put("nl-aw", "Aruba");
		map.put("nl-be", "Belgium");
		map.put("nl-nl", "Netherlands");
		map.put("nn-no", "Norway");
		map.put("no-no", "Norway");
		map.put("nr-za", "South Africa");
		map.put("nso-za", "South Africa");
		map.put("oc-fr", "France");
		map.put("om-et", "Ethiopia");
		map.put("om-ke", "Kenya");
		map.put("or-in", "India");
		map.put("pa-in", "India");
		map.put("pa-pk", "Pakistan");
		map.put("pl-pl", "Poland");
		map.put("ps-af", "Afghanistan");
		map.put("pt-br", "Brazil");
		map.put("pt-pt", "Portugal");
		map.put("ro-ro", "Romania");
		map.put("ru-ru", "Russian Federation");
		map.put("ru-ua", "Ukraine");
		map.put("rw-rw", "Rwanda");
		map.put("sa-in", "India");
		map.put("sc-it", "Italy");
		map.put("sd-in", "India");
		map.put("se-no", "Norway");
		map.put("shs-ca", "Canada");
		map.put("si-lk", "Sri Lanka");
		map.put("sid-et", "Ethiopia");
		map.put("sk-sk", "Slovakia");
		map.put("sl-si", "Slovenia");
		map.put("so-dj", "Djibouti");
		map.put("so-et", "Ethiopia");
		map.put("so-ke", "Kenya");
		map.put("so-so", "Somalia");
		map.put("sq-al", "Albania");
		map.put("sq-mk", "Macedonia, the Former Yugoslav Republic of");
		map.put("sr-me", "Montenegro");
		map.put("sr-rs", "Serbia");
		map.put("ss-za", "South Africa");
		map.put("st-za", "South Africa");
		map.put("sv-fi", "Finland");
		map.put("sv-se", "Sweden");
		map.put("ta-in", "India");
		map.put("te-in", "India");
		map.put("tg-tj", "Tajikistan");
		map.put("th-th", "Thailand");
		map.put("ti-er", "Eritrea");
		map.put("ti-et", "Ethiopia");
		map.put("tig-er", "Eritrea");
		map.put("tk-tm", "Turkmenistan");
		map.put("tl-ph", "Philippines");
		map.put("tn-za", "South Africa");
		map.put("tr-cy", "Cyprus");
		map.put("tr-tr", "Turkey");
		map.put("ts-za", "South Africa");
		map.put("tt-ru", "Russian Federation");
		map.put("ug-cn", "China");
		map.put("uk-ua", "Ukraine");
		map.put("ur-pk", "Pakistan");
		map.put("uz-uz", "Uzbekistan");
		map.put("ve-za", "South Africa");
		map.put("vi-vn", "Viet Nam");
		map.put("wa-be", "Belgium");
		map.put("wo-sn", "Senegal");
		map.put("xh-za", "South Africa");
		map.put("yi-us", "United States of America(USA)");
		map.put("yo-ng", "Nigeria");
		map.put("zh-cn", "China");
		map.put("zh-hk", "Hong Kong");
		map.put("zh-sg", "Singapore");
		map.put("zh-tw", "Taiwan, Province of China");
		map.put("zu-za", "South Africa");


		map.put("aa_dj", "Djibouti");
		map.put("aa_er", "Eritrea");
		map.put("aa_et", "Ethiopia");
		map.put("af_za", "South Africa");
		map.put("am_et", "Ethiopia");
		map.put("an_es", "Spain");
		map.put("ar_ae", "United Arab Emirates");
		map.put("ar_bh", "Bahrain");
		map.put("ar_dz", "Algeria");
		map.put("ar_eg", "Egypt");
		map.put("ar_in", "India");
		map.put("ar_iq", "Iraq");
		map.put("ar_jo", "Jordan");
		map.put("ar_kw", "Kuwait");
		map.put("ar_lb", "Lebanon");
		map.put("ar_ly", "Libya");
		map.put("ar_ma", "Morocco");
		map.put("ar_om", "Oman");
		map.put("ar_qa", "Qatar");
		map.put("ar_sa", "Saudi Arabia");
		map.put("ar_sd", "Sudan");
		map.put("ar_sy", "Syrian Arab Republic");
		map.put("ar_tn", "Tunisia");
		map.put("ar_ye", "Yemen");
		map.put("as_in", "India");
		map.put("ast_es", "Spain");
		map.put("az_az", "Azerbaijan");
		map.put("be_by", "Belarus");
		map.put("ber_dz", "Algeria");
		map.put("ber_ma", "Morocco");
		map.put("bg_bg", "Bulgaria");
		map.put("bn_bd", "Bangladesh");
		map.put("bn_in", "India");
		map.put("bo_cn", "China");
		map.put("bo_in", "India");
		map.put("br_fr", "France");
		map.put("bs_ba", "Bosnia and Herzegovina");
		map.put("byn_er", "Eritrea");
		map.put("ca_ad", "Andorra");
		map.put("ca_es", "Spain");
		map.put("ca_fr", "France");
		map.put("ca_it", "Italy");
		map.put("crh_ua", "Ukraine");
		map.put("cs_cz", "Czech Republic");
		map.put("csb_pl", "Poland");
		map.put("cv_ru", "Russian Federation");
		map.put("cy_gb", "United Kingdom(Great Britain)");
		map.put("da_dk", "Denmark");
		map.put("de_at", "Austria");
		map.put("de_be", "Belgium");
		map.put("de_ch", "Switzerland");
		map.put("de_de", "Germany");
		map.put("de_lu", "Luxembourg");
		map.put("dv_mv", "Maldives");
		map.put("dz_bt", "Bhutan");
		map.put("el_cy", "Cyprus");
		map.put("el_gr", "Greece");
		map.put("en_ag", "Antigua and Barbuda");
		map.put("en_au", "Australia");
		map.put("en_bw", "Botswana");
		map.put("en_ca", "Canada");
		map.put("en_dk", "Denmark");
		map.put("en_gb", "United Kingdom(Great Britain)");
		map.put("en_hk", "Hong Kong");
		map.put("en_ie", "Ireland");
		map.put("en_in", "India");
		map.put("en_ng", "Nigeria");
		map.put("en_nz", "New Zealand");
		map.put("en_ph", "Philippines");
		map.put("en_sg", "Singapore");
		map.put("en_us", "United States of America(USA)");
		map.put("en_za", "South Africa");
		map.put("en_zw", "Zimbabwe");
		map.put("es_ar", "Argentina");
		map.put("es_bo", "Bolivia");
		map.put("es_cl", "Chile");
		map.put("es_co", "Colombia");
		map.put("es_cr", "Costa Rica");
		map.put("es_do", "Dominican Republic");
		map.put("es_ec", "Ecuador");
		map.put("es_es", "Spain");
		map.put("es_gt", "Guatemala");
		map.put("es_hn", "Honduras");
		map.put("es_mx", "Mexico");
		map.put("es_ni", "Nicaragua");
		map.put("es_pa", "Panama");
		map.put("es_pe", "Peru");
		map.put("es_pr", "Puerto Rico");
		map.put("es_py", "Paraguay");
		map.put("es_sv", "El Salvador");
		map.put("es_us", "United States of America(USA)");
		map.put("es_uy", "Uruguay");
		map.put("es_ve", "Venezuela");
		map.put("et_ee", "Estonia");
		map.put("eu_es", "Spain");
		map.put("fa_ir", "Iran, Islamic Republic of");
		map.put("fi_fi", "Finland");
		map.put("fil_ph", "Philippines");
		map.put("fo_fo", "Faroe Islands");
		map.put("fr_be", "Belgium");
		map.put("fr_ca", "Canada");
		map.put("fr_ch", "Switzerland");
		map.put("fr_fr", "France");
		map.put("fr_lu", "Luxembourg");
		map.put("fur_it", "Italy");
		map.put("fy_de", "Germany");
		map.put("fy_nl", "Netherlands");
		map.put("ga_ie", "Ireland");
		map.put("gd_gb", "United Kingdom(Great Britain)");
		map.put("gez_er", "Eritrea");
		map.put("gez_et", "Ethiopia");
		map.put("gl_es", "Spain");
		map.put("gu_in", "India");
		map.put("gv_gb", "United Kingdom(Great Britain)");
		map.put("ha_ng", "Nigeria");
		map.put("he_il", "Israel");
		map.put("hi_in", "India");
		map.put("hne_in", "India");
		map.put("hr_hr", "Croatia");
		map.put("hsb_de", "Germany");
		map.put("ht_ht", "Haiti");
		map.put("hu_hu", "Hungary");
		map.put("hy_am", "Armenia");
		map.put("id_id", "Indonesia");
		map.put("ig_ng", "Nigeria");
		map.put("ik_ca", "Canada");
		map.put("is_is", "Iceland");
		map.put("it_ch", "Switzerland");
		map.put("it_it", "Italy");
		map.put("iu_ca", "Canada");
		map.put("iw_il", "Israel");
		map.put("ja_jp", "Japan");
		map.put("ka_ge", "Georgia");
		map.put("kk_kz", "Kazakhstan");
		map.put("kl_gl", "Greenland");
		map.put("km_kh", "Cambodia");
		map.put("kn_in", "India");
		map.put("ko_kr", "South Korea");
		map.put("kok_in", "India");
		map.put("ks_in", "India");
		map.put("ku_tr", "Turkey");
		map.put("kw_gb", "United Kingdom(Great Britain)");
		map.put("ky_kg", "Kyrgyzstan");
		map.put("lg_ug", "Uganda");
		map.put("li_be", "Belgium");
		map.put("li_nl", "Netherlands");
		map.put("lo_la", "Lao People\"s Democratic Republic");
		map.put("lt_lt", "Lithuania");
		map.put("lv_lv", "Latvia");
		map.put("mai_in", "India");
		map.put("mg_mg", "Madagascar");
		map.put("mi_nz", "New Zealand");
		map.put("mk_mk", "Macedonia, the Former Yugoslav Republic of");
		map.put("ml_in", "India");
		map.put("mn_mn", "Mongolia");
		map.put("mr_in", "India");
		map.put("ms_my", "Malaysia");
		map.put("mt_mt", "Malta");
		map.put("my_mm", "Myanmar");
		map.put("nb_no", "Norway");
		map.put("nds_de", "Germany");
		map.put("nds_nl", "Netherlands");
		map.put("ne_np", "Nepal");
		map.put("nl_aw", "Aruba");
		map.put("nl_be", "Belgium");
		map.put("nl_nl", "Netherlands");
		map.put("nn_no", "Norway");
		map.put("no_no", "Norway");
		map.put("nr_za", "South Africa");
		map.put("nso_za", "South Africa");
		map.put("oc_fr", "France");
		map.put("om_et", "Ethiopia");
		map.put("om_ke", "Kenya");
		map.put("or_in", "India");
		map.put("pa_in", "India");
		map.put("pa_pk", "Pakistan");
		map.put("pl_pl", "Poland");
		map.put("ps_af", "Afghanistan");
		map.put("pt_br", "Brazil");
		map.put("pt_pt", "Portugal");
		map.put("ro_ro", "Romania");
		map.put("ru_ru", "Russian Federation");
		map.put("ru_ua", "Ukraine");
		map.put("rw_rw", "Rwanda");
		map.put("sa_in", "India");
		map.put("sc_it", "Italy");
		map.put("sd_in", "India");
		map.put("se_no", "Norway");
		map.put("shs_ca", "Canada");
		map.put("si_lk", "Sri Lanka");
		map.put("sid_et", "Ethiopia");
		map.put("sk_sk", "Slovakia");
		map.put("sl_si", "Slovenia");
		map.put("so_dj", "Djibouti");
		map.put("so_et", "Ethiopia");
		map.put("so_ke", "Kenya");
		map.put("so_so", "Somalia");
		map.put("sq_al", "Albania");
		map.put("sq_mk", "Macedonia, the Former Yugoslav Republic of");
		map.put("sr_me", "Montenegro");
		map.put("sr_rs", "Serbia");
		map.put("ss_za", "South Africa");
		map.put("st_za", "South Africa");
		map.put("sv_fi", "Finland");
		map.put("sv_se", "Sweden");
		map.put("ta_in", "India");
		map.put("te_in", "India");
		map.put("tg_tj", "Tajikistan");
		map.put("th_th", "Thailand");
		map.put("ti_er", "Eritrea");
		map.put("ti_et", "Ethiopia");
		map.put("tig_er", "Eritrea");
		map.put("tk_tm", "Turkmenistan");
		map.put("tl_ph", "Philippines");
		map.put("tn_za", "South Africa");
		map.put("tr_cy", "Cyprus");
		map.put("tr_tr", "Turkey");
		map.put("ts_za", "South Africa");
		map.put("tt_ru", "Russian Federation");
		map.put("ug_cn", "China");
		map.put("uk_ua", "Ukraine");
		map.put("ur_pk", "Pakistan");
		map.put("uz_uz", "Uzbekistan");
		map.put("ve_za", "South Africa");
		map.put("vi_vn", "Viet Nam");
		map.put("wa_be", "Belgium");
		map.put("wo_sn", "Senegal");
		map.put("xh_za", "South Africa");
		map.put("yi_us", "United States of America(USA)");
		map.put("yo_ng", "Nigeria");
		map.put("zh_cn", "China");
		map.put("zh_hk", "Hong Kong");
		map.put("zh_sg", "Singapore");
		map.put("zh_tw", "Taiwan, Province of China");
		map.put("zu_za", "South Africa");
	}

	// TODO 중복제거
	public static String getCharsetFromMartif(String path) {
		InputStream inputStream = null;

		try {
			inputStream = new FileInputStream(path);
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream);
			XPath xpath = XPathFactory.newInstance().newXPath();

			String expression = "//martif";
			Node node = (Node) xpath.compile(expression).evaluate(document, XPathConstants.NODE);

			if(node == null) {
				return null;
			}

			String type = node.getAttributes().getNamedItem("type").getTextContent();

			if(!"TBX".equals(type)) {
				return null;
			}

			String charset = node.getAttributes().getNamedItem("xml:lang").getTextContent();
			return charset;
		} catch (FileNotFoundException e) {
			logger.error("", e);
		} catch (SAXException e) {
			logger.error("", e);
		} catch (IOException e) {
			logger.error("", e);
		} catch (ParserConfigurationException e) {
			logger.error("", e);
		} catch (XPathExpressionException e) {
			logger.error("", e);
		}

		return null;
	}

	// TODO 중복 제거
	public static List<String> getAllLangSet(String path) {
		InputStream inputStream = null;
		Set<String> set = new HashSet<String>();

		try {
			inputStream = new FileInputStream(path);
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(inputStream);
			XPath xpath = XPathFactory.newInstance().newXPath();
			String expression = "//termEntry";
			NodeList termEntryNodeList = (NodeList) xpath.compile(expression).evaluate(document, XPathConstants.NODESET);
			int termEntryCount = termEntryNodeList.getLength();

			for(int i = 0; i < termEntryCount; i++ ) {
				String termEntryId = termEntryNodeList.item(i).getAttributes().getNamedItem("id").getTextContent();
	        	NodeList langSetNodeList = termEntryNodeList.item(i).getChildNodes();
	        	Set<String> charsetSet = processLangSetNodeList(xpath, document, termEntryId, langSetNodeList);
	        	set.addAll(charsetSet);
			}
		} catch (FileNotFoundException e) {
			logger.error("file not exist.", e);
		} catch (SAXException | IOException | ParserConfigurationException e) {
			logger.error("", e);
		} catch (XPathExpressionException e) {
			logger.error("", e);
		} finally {
			IOUtils.closeQuietly(inputStream);
		}

		return setToList(set);
	}

	public static String getCountryName(String charset) {
		return StringUtils.defaultString(map.get(charset.toLowerCase()), "Unknown");
	}

	private static Set<String> processLangSetNodeList(XPath xpath, Document document, String termEntryId, NodeList langSetNodeList) throws XPathExpressionException {
		int langSetCount = langSetNodeList.getLength();
		Set<String> set = new HashSet<String>();

		for(int i = 0; i < langSetCount; i++) {
			String charset = langSetNodeList.item(i).getAttributes().getNamedItem("xml:lang").getNodeValue();

			if(!StringUtils.isEmpty(charset)) {
				set.add(charset);
			}
		}

		return set;
	}

	private static List<String> setToList(Set<String> set) {
		List<String> list = new ArrayList<String>();
		list.addAll(set);
		return list;
	}

	@Deprecated
	private static String setToString(Set<String> set) {
		StringBuilder stringBuilder = new StringBuilder();
		Iterator<String> iterator = set.iterator();

		while(iterator.hasNext()) {
			stringBuilder.append(iterator.next());
			stringBuilder.append(delimiter);
		}

		return StringUtils.removeEnd(stringBuilder.toString(), delimiter);
	}
}
