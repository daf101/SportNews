package com.dafakamatt.sportnews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class ArticleAdaptor extends ArrayAdapter<Article> {
    public ArticleAdaptor(Context context, ArrayList<Article> articles) {
        super(context,0,articles);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflating the article_list_item.xml file:
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.article_list_item,parent,false);

        }

        // Storing the current article we're working with into a variable, which we'll use later:
        Article currentArticle = getItem(position);

        // Loading TextViews into variables so we can manipulate them in the lower lines of
        // code within the article adaptor:
        TextView dateTextView = listItemView.findViewById(R.id.date_text_view);
        TextView articleNameTextView = listItemView.findViewById(R.id.article_title_text_view);
        TextView sectionTextView = listItemView.findViewById(R.id.section_text_view);

        // Setting appropriate text into each view:
        dateTextView.setText(currentArticle.getPublicationDate());
        articleNameTextView.setText(currentArticle.getArticlename());
        sectionTextView.setText(currentArticle.getSectionName());

        // Returning the listItemView:
        return listItemView;
    }


}
