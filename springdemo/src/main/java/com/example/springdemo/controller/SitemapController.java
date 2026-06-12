package com.example.springdemo.controller;

import com.example.springdemo.services.SitemapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SitemapController {

  @Autowired
  private SitemapService sitemapService;

  @Value("${app.site-url:http://localhost:8080}")
  private String siteUrl;

  /**
   * Serve the XML sitemap for Google and other search engines.
   * Usage: submit /sitemap.xml to Google Search Console.
   */
  @GetMapping(value = "/sitemap.xml", produces = "application/xml")
  public String sitemap() {
    return sitemapService.generateSitemap(siteUrl);
  }
}