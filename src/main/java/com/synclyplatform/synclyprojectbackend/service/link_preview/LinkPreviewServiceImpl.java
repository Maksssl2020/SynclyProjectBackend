package com.synclyplatform.synclyprojectbackend.service.link_preview;

import com.synclyplatform.synclyprojectbackend.dto.link_preview.LinkPreviewResponseDTO;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class LinkPreviewServiceImpl  implements LinkPreviewService {

    @Override
    public LinkPreviewResponseDTO fetchMetadata(String url) throws IOException {
        Document document = Jsoup.connect(url).userAgent("Mozilla/5.0").get();

        String title = getMetaTag(document, "og:title");
        if (title == null) title = document.title();

        String description = getMetaTag(document, "og:description");
        if (description == null) description = getMetaTag(document, "description");

        String image = getMetaTag(document, "og:image");

        return new LinkPreviewResponseDTO(title, description, url, image);
    }

    private String getMetaTag(Document document, String attribute)  {
        Element tag = document.selectFirst(String.format("meta[property=%s], meta[name=%s]", attribute, attribute));
        if (tag != null) {
            return tag.attr("content");
        }
        return null;
    }
}
