/*
*    Copyright [2015] [wisemapping]
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

package com.wisemapping.rest.view;

import com.wisemapping.importer.ImportFormat;
import com.wisemapping.importer.Importer;
import com.wisemapping.importer.ImporterException;
import com.wisemapping.importer.ImporterFactory;
import com.wisemapping.model.Mindmap;
import org.jetbrains.annotations.NotNull;
import org.springframework.web.servlet.view.AbstractView;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;

public class ImportTransformationView extends AbstractView {

    private String contentType;
    private Importer importer;

    public ImportTransformationView(@NotNull final String contentType) throws ImporterException {
        ImporterFactory exporterFactory = ImporterFactory.getInstance();
        importer = exporterFactory.getImporter(ImportFormat.FREEMIND);
        this.contentType = contentType;
    }

    @Override
    protected void renderMergedOutputModel(@NotNull Map<String, Object> viewMap, @NotNull HttpServletRequest request, @NotNull final HttpServletResponse response) throws Exception {
        final String content = (String) viewMap.get("content");
        final String filename = (String) viewMap.get("filename");

        // Convert to map ...
        final InputStream is = new ByteArrayInputStream(content.getBytes("UTF-8"));
        final Mindmap mindMap = importer.importMap("filename", "filename", is);

        // Set file name...:http://stackoverflow.com/questions/5325322/java-servlet-download-filename-special-characters/13359949#13359949
        final String fileName = (filename != null ? filename : "map") + "." + "xwise";
        setContentDisposition(request, response, fileName);

        // Write the conversion content ...
        final ServletOutputStream outputStream = response.getOutputStream();
        outputStream.print(mindMap.getXmlStr());
    }

    private void setContentDisposition(HttpServletRequest request, HttpServletResponse response, String fileName) {
        final String userAgent = request.getHeader("user-agent");
        String disposition = fileName;
        boolean isInternetExplorer = (userAgent.contains("MSIE"));
        try {
            byte[] fileNameBytes = fileName.getBytes((isInternetExplorer) ? ("windows-1250") : ("utf-8"));
            String dispositionFileName = "";
            for (byte b : fileNameBytes) {
                dispositionFileName += (char) (b & 0xff);
            }
            disposition = "attachment; filename=\"" + dispositionFileName + "\"";
        } catch (UnsupportedEncodingException ence) {
            // ... handle exception ...
        }
        response.setHeader("Content-disposition", disposition);
    }

    @Override
    public String getContentType() {
        return contentType;
    }
}
