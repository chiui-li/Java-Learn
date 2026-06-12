package com.example.springdemo.services;

import com.example.springdemo.entity.PostEntity;
import com.example.springdemo.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class SitemapService {

  @Autowired
  private UserService userService;

  @Autowired
  private PostService postService;

  private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ISO_LOCAL_DATE;

  /**
   * Generate the full sitemap.xml content.
   *
   * @param baseUrl Site base URL (e.g. https://example.com)
   * @return XML string conforming to the sitemap protocol
   */
  public String generateSitemap(String baseUrl) {
    StringBuilder xml = new StringBuilder();
    xml.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
    xml.append("<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">\n");

    // 1) Home page
    appendUrl(xml, baseUrl + "/", null, "daily", "1.0");

    // 2) Per-user blog pages + published posts
    List<User> users = userService.selectAllUsers();

    for (User user : users) {
      String userName = user.getUsername();
      String blogUrl = baseUrl + "/blog/" + userName;

      // User blog index
      appendUrl(xml, blogUrl, user.getUpdatedAt(), "daily", "0.8");

      // Published posts for this user
      List<PostEntity> posts = postService.listPublishedPosts(user.getId(), null);
      for (PostEntity post : posts) {
        String titleSlug = toUrlSegment(post.getTitle());
        String postUrl = blogUrl + "/post/" + post.getId() + "/" + titleSlug + ".html";
        appendUrl(xml, postUrl, post.getUpdatedAt(), "weekly", "0.6");
      }
    }

    xml.append("</urlset>");
    return xml.toString();
  }

  /**
   * Append a single <url> entry with proper XML escaping.
   */
  private void appendUrl(StringBuilder xml, String loc, Object lastmod,
      String changefreq, String priority) {
    xml.append("  <url>\n");
    xml.append("    <loc>").append(escapeXml(loc)).append("</loc>\n");
    if (lastmod != null) {
      String dateStr;
      if (lastmod instanceof java.time.LocalDateTime dt) {
        dateStr = dt.format(DATE_FMT);
      } else if (lastmod instanceof java.time.LocalDate d) {
        dateStr = d.toString();
      } else {
        dateStr = lastmod.toString();
      }
      xml.append("    <lastmod>").append(dateStr).append("</lastmod>\n");
    }
    xml.append("    <changefreq>").append(changefreq).append("</changefreq>\n");
    xml.append("    <priority>").append(priority).append("</priority>\n");
    xml.append("  </url>\n");
  }

  /**
   * Escape a string for safe inclusion in XML.
   */
  private String escapeXml(String s) {
    if (s == null) return "";
    return s.replace("&", "&amp;")
            .replace("<", "&lt;")
            .replace(">", "&gt;")
            .replace("\"", "&quot;")
            .replace("'", "&apos;");
  }

  /**
   * Convert a post title into a URL-safe path segment.
   * Spaces become hyphens; special characters are URL-encoded;
   * result is trimmed to 200 characters.
   */
  private String toUrlSegment(String title) {
    if (title == null || title.isBlank()) {
      return "post";
    }
    String slug = title.trim().replaceAll("\\s+", "-");
    String encoded = URLEncoder.encode(slug, StandardCharsets.UTF_8)
        .replace("+", "%20");
    if (encoded.length() > 200) {
      encoded = encoded.substring(0, 200);
    }
    return encoded;
  }
}