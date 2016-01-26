package eu.concept.util.semantic;

import com.google.common.base.Joiner;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.tika.exception.TikaException;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.json.JSONArray;
import org.json.JSONObject;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
public class SemanticAnnotator {

    private static final Logger logger = Logger.getLogger(SemanticAnnotator.class.getName());
    public static final double DEFAULT_RELEVANCY_THRESHOLD = 0.5;
    private static final Parser DocumentParser = new AutoDetectParser();

    public static String extractKeywordsFromImage(byte[] fileContent, double score_threshold) {
        HttpResponse<JsonNode> response;
        List<String> keywords = new ArrayList<>();
        try {
            response = Unirest.post("http://localhost:8081/semantic-enhancer/tags/image").body(fileContent).asJson();
            JSONArray results = response.getBody().getArray();
            //Iterate all keywords
            for (int i = 0; i < results.length(); i++) {
                System.out.println("Keyword: "+results.getJSONObject(i).getString("name"));
                if (DEFAULT_RELEVANCY_THRESHOLD < results.getJSONObject(i).getDouble("relevancy")) {
                    keywords.add(results.getJSONObject(i).getString("name"));
                }
            }
        } catch (UnirestException ex) {
            Logger.getLogger(SemanticAnnotator.class.getName()).log(Level.SEVERE, null, ex);
        }
        logger.log(Level.INFO, "Phrase constructed is: {0}", Joiner.on(",").join(keywords));
        //Construct & Return Keywords Phrase
        return Joiner.on(",").join(keywords);
    }

    public static String extractKeywordsFromFile(byte[] fileContent, double score_threshold) {
       String extractedText = extractTextFromFile(fileContent);
          logger.log(Level.INFO, "Extracted test is: {0}", extractedText);
          return getTagsForText(extractedText, score_threshold);
    }

    private static String extractTextFromFile(byte[] fileContent) {
        ContentHandler handler = new BodyContentHandler();
        try {
            DocumentParser.parse(new ByteArrayInputStream(fileContent), handler, null, null);
        } catch (IOException | SAXException | TikaException ex) {
            Logger.getLogger(SemanticAnnotator.class.getName()).log(Level.SEVERE, null, ex);
        }
        return handler.toString();

    }

    public static String getTagsForText(String content,double score_threshold ) {
        try {
            HttpResponse<JsonNode> jsonResponse = Unirest.post("http://localhost:8081/semantic-enhancer/tags/text").body(content).asJson();
            List<String> tags = new ArrayList<>();
            for (int i = 0; i < jsonResponse.getBody().getArray().length(); i++) {
                JSONObject obj = jsonResponse.getBody().getArray().getJSONObject(i);
                double relevancy = (double) obj.get("relevancy");
                if (relevancy > score_threshold) {
                    tags.add((String) obj.get("name"));
                }
            }
            logger.log(Level.INFO, "Phrase constructed is: {0}", Joiner.on(",").join(tags));
            return Joiner.on(",").join(tags);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String getTagsForImage(String uri) {
        try {
            HttpResponse<JsonNode> jsonResponse = Unirest.get("http://localhost:8081/semantic-enhancer/tags?url=http://concept.euprojects.net/" + uri).asJson();
            List<String> tags = new ArrayList<>();
            for (int i = 0; i < jsonResponse.getBody().getArray().length(); i++) {
                JSONObject obj = jsonResponse.getBody().getArray().getJSONObject(i);
                double relevancy = (double) obj.get("relevancy");
                if (relevancy > 0.3) {
                    tags.add((String) obj.get("name"));
                }
            }
            return Joiner.on(",").join(tags);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

}
