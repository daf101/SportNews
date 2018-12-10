package com.dafakamatt.sportnews;

public class Article {

    // Custom object create for Articles in the ListView:

    private String mArticleName;
    private String mSectionName;
    private String mPublicationDate;
    private String mUrl;

    // Setting up required parameter sets to create the object
    public Article(String articleName, String sectionName, String publicationDate, String url) {
        mArticleName = articleName;
        mSectionName = sectionName;
        mPublicationDate = publicationDate;
        mUrl = url;
    }

    // Return Article details, when it's called by the Article Adaptor or other classes:
    public String getArticlename() {
        return mArticleName;
    }

    public String getSectionName() {
        return mSectionName;
    }

    public String getPublicationDate() {
        return mPublicationDate;
    }

    public String getUrl() {
        return mUrl;
    }
}

