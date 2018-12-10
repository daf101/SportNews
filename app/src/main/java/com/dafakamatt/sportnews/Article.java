package com.dafakamatt.sportnews;

public class Article {

    // Custom object create for Articles in the ListView:

    private String mArticleName;
    private String[] mAuthorNames;
    private String mSectionName;
    private String mPublicationDate;
    private String mUrl;

    // Setting up required parameter sets to create the object
    public Article(String articleName, String[] authorNames, String sectionName, String publicationDate, String url) {
        mArticleName = articleName;
        mAuthorNames = authorNames;
        mSectionName = sectionName;
        mPublicationDate = publicationDate;
        mUrl = url;
    }

    // Return Article details, when it's called by the Article Adaptor or other classes:
    public String getArticlename() {
        return mArticleName;
    }

    public String[] getAuthorNames() {
        return mAuthorNames;
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

