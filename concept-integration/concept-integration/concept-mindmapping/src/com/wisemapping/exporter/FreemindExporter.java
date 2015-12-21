/*
*    Copyright [2011] [wisemapping]
*
*   Licensed under WiseMapping Public License, Version 1.0 (the "License").
*   It is basically the Apache License, Version 2.0 (the "License") plus the
*   "powered by wisemapping" text requirement on every single page;
*   you may not use this file except in compliance with the License.
*   You may obtain a copy of the license at
*
*       http://www.wisemapping.org/license
*
*   Unless required by applicable law or agreed to in writing, software
*   distributed under the License is distributed on an "AS IS" BASIS,
*   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
*   See the License for the specific language governing permissions and
*   limitations under the License.
*/

package com.wisemapping.exporter;


import com.wisemapping.importer.VersionNumber;
import com.wisemapping.importer.freemind.FreemindConstant;
import com.wisemapping.importer.freemind.FreemindIconConverter;
import com.wisemapping.jaxb.freemind.Font;
import com.wisemapping.jaxb.wisemap.Note;
import com.wisemapping.model.Mindmap;
import com.wisemapping.model.ShapeStyle;
import com.wisemapping.util.JAXBUtils;
import com.wisemapping.jaxb.freemind.*;
import com.wisemapping.jaxb.wisemap.RelationshipType;
import com.wisemapping.jaxb.wisemap.TopicType;
import com.wisemapping.jaxb.wisemap.Icon;
import org.apache.commons.lang.StringEscapeUtils;
import org.jetbrains.annotations.NotNull;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class FreemindExporter
        implements Exporter {


    private com.wisemapping.jaxb.freemind.ObjectFactory objectFactory;
    private Map<String, Node> nodesMap = null;
    private VersionNumber version = FreemindConstant.SUPPORTED_FREEMIND_VERSION;

    public void export(Mindmap map, OutputStream outputStream) throws ExportException {
        export(map.getUnzipXml(), outputStream);
    }

    public void export(byte[] xml, @NotNull OutputStream outputStream) throws ExportException {

        objectFactory = new com.wisemapping.jaxb.freemind.ObjectFactory();
        nodesMap = new HashMap<String, Node>();
        final com.wisemapping.jaxb.wisemap.Map mindmapMap;

        try {
            final ByteArrayInputStream stream = new ByteArrayInputStream(xml);
            mindmapMap = (com.wisemapping.jaxb.wisemap.Map) JAXBUtils.getMapObject(stream, "com.wisemapping.jaxb.wisemap");

            final com.wisemapping.jaxb.freemind.Map freemindMap = objectFactory.createMap();
            freemindMap.setVersion(this.getVersionNumber());

            final List<TopicType> topics = mindmapMap.getTopic();

            // Isolated Topic does not exist in Freemind only take the center topic
            TopicType centerTopic = null;
            if (topics.size() > 1) {
                for (TopicType topic : topics) {
                    if (topic.isCentral() != null && topic.isCentral()) {
                        centerTopic = topic;
                        break;
                    }
                }
            } else {
                centerTopic = topics.get(0);
            }

            final Node main = objectFactory.createNode();
            freemindMap.setNode(main);
            if (centerTopic != null) {
                nodesMap.put(centerTopic.getId(), main);
                setTopicPropertiesToNode(main, centerTopic, true);
                addNodeFromTopic(centerTopic, main);
            }

            final List<RelationshipType> relationships = mindmapMap.getRelationship();
            for (RelationshipType relationship : relationships) {
                // FIXME:invert srcNode and dstNode to correct a bug in the wise mind map representation
                Node srcNode = nodesMap.get(relationship.getDestTopicId());
                Node dstNode = nodesMap.get(relationship.getSrcTopicId());


                // Workaround for nodes without relationship associated ...
                if (srcNode != null && dstNode != null) {

                    Arrowlink arrowlink = objectFactory.createArrowlink();

                    arrowlink.setDESTINATION(dstNode.getID());
                    if (relationship.isEndArrow() != null && relationship.isEndArrow())
                        arrowlink.setENDARROW("Default");

                    if (relationship.isStartArrow() != null && relationship.isStartArrow())
                        arrowlink.setSTARTARROW("Default");

                    List<Object> cloudOrEdge = srcNode.getArrowlinkOrCloudOrEdge();
                    cloudOrEdge.add(arrowlink);
                }
            }

            JAXBUtils.saveMap(freemindMap, outputStream);
        } catch (JAXBException e) {
            throw new ExportException(e);
        } catch (UnsupportedEncodingException e) {
            throw new ExportException(e);
        } catch (SAXException e) {
            throw new ExportException(e);
        } catch (ParserConfigurationException e) {
            throw new ExportException(e);
        } catch (IOException e) {
            throw new ExportException(e);
        }
    }

    private void addNodeFromTopic(@NotNull final TopicType mainTopic, @NotNull final Node destNode) throws IOException, SAXException, ParserConfigurationException {
        final List<TopicType> currentTopic = mainTopic.getTopic();

        Collections.sort(currentTopic, new VerticalPositionComparator());
        for (TopicType topicType : currentTopic) {
            final Node newNode = objectFactory.createNode();
            nodesMap.put(topicType.getId(), newNode);

            setTopicPropertiesToNode(newNode, topicType, false);
            destNode.getArrowlinkOrCloudOrEdge().add(newNode);

            addNodeFromTopic(topicType, newNode);

            final String position = topicType.getPosition();
            if (position != null) {
                String xPos = position.split(",")[0];
                int x = Integer.valueOf(xPos);
                newNode.setPOSITION((x < 0 ? FreemindConstant.POSITION_LEFT : FreemindConstant.POSITION_RIGHT));
            } else {
                newNode.setPOSITION(FreemindConstant.POSITION_LEFT);
            }
        }
    }

    private void setTopicPropertiesToNode(@NotNull com.wisemapping.jaxb.freemind.Node freemindNode, @NotNull com.wisemapping.jaxb.wisemap.TopicType mindmapTopic, boolean isRoot) throws IOException, SAXException, ParserConfigurationException {
        freemindNode.setID("ID_" + mindmapTopic.getId());

        String text = mindmapTopic.getTextAttr();
        if (text == null || text.isEmpty()) {
            text = mindmapTopic.getText();
        }

        // Formated text have a different representation ....
        if (text != null) {
            if (!text.contains("\n")) {
                freemindNode.setTEXT(text);
            } else {
                final Richcontent richcontent = buildRichContent(text, "NODE");
                freemindNode.getArrowlinkOrCloudOrEdge().add(richcontent);
            }
        }

        String wiseShape = mindmapTopic.getShape();
        if (wiseShape != null && !ShapeStyle.LINE.equals(ShapeStyle.fromValue(wiseShape))) {
            freemindNode.setBACKGROUNDCOLOR(this.rgbToHex(mindmapTopic.getBgColor()));
        }
        final String shape = mindmapTopic.getShape();
        if (shape != null && !shape.isEmpty()) {
            if (isRoot && !ShapeStyle.ROUNDED_RECTANGLE.getStyle().endsWith(shape) || !isRoot && !ShapeStyle.LINE.getStyle().endsWith(shape)) {

                String style = shape;
                if (ShapeStyle.ROUNDED_RECTANGLE.getStyle().equals(shape) || ShapeStyle.ELLIPSE.getStyle().equals(shape)) {
                    style = "bubble";
                }
                freemindNode.setSTYLE(style);
            }
        } else if (!isRoot) {
            String style = "fork";
            freemindNode.setSTYLE(style);
        }

        addIconNode(freemindNode, mindmapTopic);
        addLinkNode(freemindNode, mindmapTopic);
        addFontNode(freemindNode, mindmapTopic);
        addEdgeNode(freemindNode, mindmapTopic);
        addNote(freemindNode, mindmapTopic);

        Boolean shrink = mindmapTopic.isShrink();
        if (shrink != null && shrink)
            freemindNode.setFOLDED(String.valueOf(shrink));

    }

    private Richcontent buildRichContent(final String text, final String type) throws ParserConfigurationException, SAXException, IOException {
        final Richcontent richcontent = objectFactory.createRichcontent();
        richcontent.setTYPE(type);

        final StringBuilder htmlContent = new StringBuilder("<html><head></head><body>");
        for (String line : text.split("\n")) {
            line = StringEscapeUtils.escapeXml(line);
            htmlContent.append("<p>").append(line.trim()).append("</p>");
        }
        htmlContent.append("</body></html>");

        DocumentBuilder db = getInstanceBuilder();
        byte[] bytes = htmlContent.toString().getBytes("UTF-8");
        Document document = db.parse(new ByteArrayInputStream(bytes), "UTF-8");
        richcontent.setHtml(document.getDocumentElement());
        return richcontent;
    }

    private DocumentBuilder getInstanceBuilder() throws ParserConfigurationException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        return dbf.newDocumentBuilder();
    }

    private void addNote(com.wisemapping.jaxb.freemind.Node fnode, com.wisemapping.jaxb.wisemap.TopicType mindmapTopic) throws IOException, SAXException, ParserConfigurationException {
        final Note note = mindmapTopic.getNote();
        if (note != null) {
            final String noteStr = note.getValue() != null ? note.getValue() : note.getTextAttr();
            if (noteStr != null) {
                final Richcontent richcontent = buildRichContent(noteStr, "NOTE");
                fnode.getArrowlinkOrCloudOrEdge().add(richcontent);
            }
        }
    }

    private void addLinkNode(com.wisemapping.jaxb.freemind.Node freemindNode, com.wisemapping.jaxb.wisemap.TopicType mindmapTopic) {
        if (mindmapTopic.getLink() != null) {
            final String url = mindmapTopic.getLink().getUrl();
            freemindNode.setLINK(url);
        }
    }

    private void addIconNode(com.wisemapping.jaxb.freemind.Node freemindNode, com.wisemapping.jaxb.wisemap.TopicType mindmapTopic) {
        if (mindmapTopic.getIcon() != null) {
            final List<Icon> iconsList = mindmapTopic.getIcon();
            for (Icon icon : iconsList) {
                final String id = icon.getId();
                final String freemindIconId = FreemindIconConverter.toFreemindId(id);
                if (freemindIconId != null) {

                    com.wisemapping.jaxb.freemind.Icon freemindIcon = new com.wisemapping.jaxb.freemind.Icon();
                    freemindIcon.setBUILTIN(freemindIconId);
                    freemindNode.getArrowlinkOrCloudOrEdge().add(freemindIcon);
                }
            }
        }
    }

    private void addEdgeNode(com.wisemapping.jaxb.freemind.Node freemindNode, com.wisemapping.jaxb.wisemap.TopicType mindmapTopic) {
        if (mindmapTopic.getBrColor() != null) {
            final Edge edgeNode = objectFactory.createEdge();
            edgeNode.setCOLOR(this.rgbToHex(mindmapTopic.getBrColor()));
            freemindNode.getArrowlinkOrCloudOrEdge().add(edgeNode);
        }
    }

    /*
     * MindmapFont format : fontName ; size ; color ; bold; italic;
     * eg: Verdana;10;#ffffff;bold;italic;
     */
    private void addFontNode(@NotNull com.wisemapping.jaxb.freemind.Node freemindNode, com.wisemapping.jaxb.wisemap.TopicType mindmapTopic) {
        final String fontStyle = mindmapTopic.getFontStyle();
        if (fontStyle != null && fontStyle.length() != 0) {
            final Font font = objectFactory.createFont();
            final String[] part = fontStyle.split(";", 6);
            int countParts = part.length;
            boolean fontNodeNeeded = false;

            if (!fontStyle.endsWith(FreemindConstant.EMPTY_FONT_STYLE)) {
                int idx = 0;

                // Font name
                if (idx < countParts && part[idx].length() != 0) {
                    font.setNAME(part[idx]);
                    fontNodeNeeded = true;
                }
                idx++;

                // Font size
                if (idx < countParts && part[idx].length() != 0) {
                    final String size = part[idx];
                    if (size != null && !size.isEmpty()) {
                        int wiseSize = Integer.parseInt(size);
                        Integer freeSize = wiseToFreeFontSize.get(wiseSize);
                        if(freeSize!=null){
                            font.setSIZE(BigInteger.valueOf(freeSize));
                            fontNodeNeeded = true;
                        }
                    }
                }
                idx++;

                // Font Color
                if (idx < countParts && part[idx].length() != 0) {
                    freemindNode.setCOLOR(this.rgbToHex(part[idx]));
                }
                idx++;

                // Font Styles
                if (idx < countParts && part[idx].length() != 0) {
                    font.setBOLD(Boolean.TRUE.toString());
                    fontNodeNeeded = true;
                }
                idx++;

                if (idx < countParts && part[idx].length() != 0) {
                    font.setITALIC(Boolean.TRUE.toString());
                    fontNodeNeeded = true;
                }

                if (fontNodeNeeded) {
                    // font size should be set if freemind node has font properties note
                    if (font.getSIZE() == null) {
                        font.setSIZE(BigInteger.valueOf(wiseToFreeFontSize.get(8)));
                    }
                    freemindNode.getArrowlinkOrCloudOrEdge().add(font);
                }
            }
        }
    }

    // Freemind size goes from 10 to 28
    // WiseMapping:
    //  6 Small
    //  8 Normal
    // 10 Large
    // 15 Huge
    static private Map<Integer, Integer> wiseToFreeFontSize = new HashMap<Integer, Integer>();

    static {
        wiseToFreeFontSize.put(6, 10);
        wiseToFreeFontSize.put(8, 12);
        wiseToFreeFontSize.put(10, 18);
        wiseToFreeFontSize.put(15, 24);
    }

    private String rgbToHex(String color) {
        String result = color;
        if (color != null) {
            boolean isRGB = Pattern.matches("^rgb\\([0-9]{1,3}, [0-9]{1,3}, [0-9]{1,3}\\)$", color);
            if (isRGB) {
                String[] rgb = color.substring(4, color.length() - 1).split(",");
                Integer r = Integer.valueOf(rgb[0].trim());
                Integer g = Integer.valueOf(rgb[1].trim());
                Integer b = Integer.valueOf(rgb[2].trim());
                result = String.format("#%02x%02x%02x", r, g, b);
            }
        }
        return result;
    }

    public VersionNumber getVersion() {
        return version;
    }

    public void setVersion(VersionNumber version) {
        this.version = version;
    }

    public String getVersionNumber() {
        return this.getVersion().getVersion();
    }
}
