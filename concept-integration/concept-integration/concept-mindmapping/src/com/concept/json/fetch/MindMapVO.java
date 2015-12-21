package com.concept.json.fetch;

import java.util.HashMap;
import java.util.Map;
import javax.annotation.Generated;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import org.apache.commons.lang.builder.ToStringBuilder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "id", "pid", "title", "content", "contentThumbnail", "createdDate", "isPublic", "userCo" })
public class MindMapVO {

	@JsonProperty("id")
	private Integer id;
	@JsonProperty("pid")
	private Integer pid;
	@JsonProperty("title")
	private String title;
	@JsonProperty("content")
	private String content;
	@JsonProperty("contentThumbnail")
	private String contentThumbnail;
	@JsonProperty("createdDate")
	private Long createdDate;
	@JsonProperty("isPublic")
	private Integer isPublic;
	@JsonProperty("userCo")
	private UserVO userCo;
	@JsonIgnore
	private Map<String, Object> additionalProperties = new HashMap<String, Object>();

	/**
	 * 
	 * @return The id
	 */
	@JsonProperty("id")
	public Integer getId() {
		return id;
	}

	/**
	 * 
	 * @param id
	 *            The id
	 */
	@JsonProperty("id")
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * 
	 * @return The pid
	 */
	@JsonProperty("pid")
	public Integer getPid() {
		return pid;
	}

	/**
	 * 
	 * @param pid
	 *            The pid
	 */
	@JsonProperty("pid")
	public void setPid(Integer pid) {
		this.pid = pid;
	}

	/**
	 * 
	 * @return The title
	 */
	@JsonProperty("title")
	public String getTitle() {
		return title;
	}

	/**
	 * 
	 * @param title
	 *            The title
	 */
	@JsonProperty("title")
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 
	 * @return The content
	 */
	@JsonProperty("content")
	public String getContent() {
		return content;
	}

	/**
	 * 
	 * @param content
	 *            The content
	 */
	@JsonProperty("content")
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * 
	 * @return The contentThumbnail
	 */
	@JsonProperty("contentThumbnail")
	public String getContentThumbnail() {
		return contentThumbnail;
	}

	/**
	 * 
	 * @param contentThumbnail
	 *            The contentThumbnail
	 */
	@JsonProperty("contentThumbnail")
	public void setContentThumbnail(String contentThumbnail) {
		this.contentThumbnail = contentThumbnail;
	}

	/**
	 * 
	 * @return The createdDate
	 */
	@JsonProperty("createdDate")
	public Long getCreatedDate() {
		return createdDate;
	}

	/**
	 * 
	 * @param createdDate
	 *            The createdDate
	 */
	@JsonProperty("createdDate")
	public void setCreatedDate(Long createdDate) {
		this.createdDate = createdDate;
	}

	/**
	 * 
	 * @return The isPublic
	 */
	@JsonProperty("isPublic")
	public Integer getIsPublic() {
		return isPublic;
	}

	/**
	 * 
	 * @param isPublic
	 *            The isPublic
	 */
	@JsonProperty("isPublic")
	public void setIsPublic(Integer isPublic) {
		this.isPublic = isPublic;
	}

	/**
	 * 
	 * @return The userCo
	 */
	@JsonProperty("userCo")
	public UserVO getUserCo() {
		return userCo;
	}

	/**
	 * 
	 * @param userCo
	 *            The userCo
	 */
	@JsonProperty("userCo")
	public void setUserCo(UserVO userCo) {
		this.userCo = userCo;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}

	@JsonAnySetter
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}

}