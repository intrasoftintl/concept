package com.wisemapping.rest.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import com.concept.json.fetch.MindMapVO;

@XmlRootElement(name = "conceptCreate")
@XmlAccessorType(XmlAccessType.PROPERTY)
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE, setterVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY, isGetterVisibility = JsonAutoDetect.Visibility.NONE, getterVisibility = JsonAutoDetect.Visibility.PUBLIC_ONLY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RestConcept {

	private Integer mindMapId;
	private String result;
	private String errorDesc;
	private MindMapVO mindMapVO;

	public Integer getMindMapId() {
		return mindMapId;
	}

	public void setMindMapId(Integer mindMapId) {
		this.mindMapId = mindMapId;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public String getErrorDesc() {
		return errorDesc;
	}

	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}

	public MindMapVO getMindMapVO() {
		return mindMapVO;
	}

	public void setMindMapVO(MindMapVO mindMapVO) {
		this.mindMapVO = mindMapVO;
	}
}
