package com.atos.concept.persistence.services;

import java.io.*;
import java.util.*;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.atos.concept.persistence.Slides;
import com.atos.concept.persistence.Stories;
import com.atos.concept.persistence.StoriesSlides;
import com.atos.concept.persistence.dao.StoriesDao;
import com.atos.concept.utilities.ConceptConstants;





public class StoriesServices {

	private static StoriesDao storiesDao;
	private static final Logger logger = LogManager.getLogger(StoriesServices.class);
	private final String USER_AGENT = "Mozilla/5.0";

	public StoriesServices() {
		storiesDao = new StoriesDao();
	}

	public void persist(Stories story) {
		storiesDao.openCurrentSessionwithTransaction();
		storiesDao.persist(story);
		//REST to insert/update Stroyboard
		Integer id = story.getId();
		Integer pid = story.getProjectId();
		Integer uid = new Integer(story.getUserId()); // initial sql type is varchar
		String title = story.getStoryName();
		String content = "";
		String contentThumbnail ="";
		for( StoriesSlides ss : story.getStoriesSlideses()){
			if(ss.getSlideOrder() == 0){
				contentThumbnail = ss.getSlides().getSlideText();
			}
			content += ss.getSlides().getSlideText();
		}
		try {
			HttpResponse<JsonNode> jsonResponse = Unirest.post("http://concept.euprojects.net/conceptRest/api/storyboard/replicate/")
					.queryString("id", id)
					.field("uid", uid)
					.field("pid", pid)
					.field("title",title)
					.field("content",content)
					.field("content_thumbnail",contentThumbnail)
					.asJson();
			logger.info("RESPONSE---> "+jsonResponse.getBody().toString());
		} catch (UnirestException e) {
			e.printStackTrace();
		}

		callElasticSearch(story);
		storiesDao.closeCurrentSessionwithTransaction();
	}

	private void callElasticSearch(Stories story) {
		/** Calling to ElasticSearch. */
//		try {
//			Set<StoriesSlides> storiesSlideses = story.getStoriesSlideses();
//			for (StoriesSlides storiesSlides : storiesSlideses) {
//				HttpClient client = new DefaultHttpClient();
//				HttpPost post = new HttpPost(ConceptConstants.URL_ELASTICSEARCH);
//				post.setHeader("User-Agent", USER_AGENT);
//				List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
//				urlParameters.add(new BasicNameValuePair(ConceptConstants.ELASTICSEARCH_ID, story.getUuid()));
//				urlParameters.add(new BasicNameValuePair(ConceptConstants.ELASTICSEARCH_STORY_NAME, story.getStoryName()));
//				urlParameters.add(new BasicNameValuePair(ConceptConstants.ELASTICSEARCH_SLIDE_NAME, storiesSlides.getSlides().getSlideName()));
//				urlParameters.add(new BasicNameValuePair(ConceptConstants.ELASTICSEARCH_SLIDE_TEXT, storiesSlides.getSlides().getSlideText()));
//				post.setEntity(new UrlEncodedFormEntity(urlParameters));
//				HttpResponse response = client.execute(post);
//				System.out.println("\nSending 'POST' request to URL : " + post.getURI());
//				System.out.println("Post parameters : " + post.getEntity());
//				System.out.println("Response Code : " + response.getStatusLine().getStatusCode());
//				BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
//				StringBuffer result = new StringBuffer();
//				String line = "";
//				while ((line = rd.readLine()) != null) {
//					result.append(line);
//				}
//				logger.debug(result);
//			}
//		} catch (UnsupportedEncodingException e) {
//			e.printStackTrace();
//		} catch (ClientProtocolException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

	public Stories findById(Integer id) {
		storiesDao.openCurrentSession();
		Stories stories = storiesDao.findById(id);
		storiesDao.closeCurrentSession();
		return stories;
	}

	public List<Stories> findBySlide(Stories stories) {
		storiesDao.openCurrentSession();
		List<Stories> slides = storiesDao.findByEntity(stories);
		storiesDao.closeCurrentSession();
		return slides;
	}

	public void delete(Integer id) {
		storiesDao.openCurrentSessionwithTransaction();
		Stories story = storiesDao.findById(id);
		storiesDao.delete(story);
		storiesDao.closeCurrentSessionwithTransaction();
	}

	public void update(Stories story) {
		storiesDao.openCurrentSessionwithTransaction();
		storiesDao.update(story);
		callElasticSearch(story);
		storiesDao.closeCurrentSessionwithTransaction();
	}

	public List<Slides> findSlides(Integer id) {
		List<Slides> slidesList = new ArrayList<Slides>();
		Stories findById = this.findById(id);
		Set<StoriesSlides> storiesSlideses = findById.getStoriesSlideses();
		List<StoriesSlides> list = new ArrayList<StoriesSlides>(storiesSlideses);
		Collections.sort(list, new CompareStoriesSlides());
		for (StoriesSlides storySlide : list) {
			slidesList.add(storySlide.getSlides());
		}
		return slidesList;
	}

	class CompareStoriesSlides implements Comparator<StoriesSlides> {

		@Override
		public int compare(StoriesSlides o1, StoriesSlides o2) {
			return o1.getSlideOrder() - o2.getSlideOrder();
		}

	}

}
