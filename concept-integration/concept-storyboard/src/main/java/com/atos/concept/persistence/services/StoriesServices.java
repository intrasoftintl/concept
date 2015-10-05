package com.atos.concept.persistence.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.apache.http.HttpResponse;
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
