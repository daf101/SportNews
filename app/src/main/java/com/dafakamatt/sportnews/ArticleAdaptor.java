package com.dafakamatt.sportnews;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ArticleAdaptor extends ArrayAdapter<Article> {
    public ArticleAdaptor(Context context, ArrayList<Article> articles) {
        super(context, 0, articles);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Inflating the article_list_item.xml file:
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.article_list_item, parent, false);

        }

        // Storing the current article we're working with into a variable, which we'll use later:
        Article currentArticle = getItem(position);

        // Loading TextViews into variables so we can manipulate them in the lower lines of
        // code within the article adaptor:
        TextView dateTextView = listItemView.findViewById(R.id.date_text_view);
        TextView articleNameTextView = listItemView.findViewById(R.id.article_title_text_view);
        TextView sectionTextView = listItemView.findViewById(R.id.section_text_view);

        // Modifying date from filename format to DD/MM/YYYY so it looks prettier to the end user:
        String currentArticlePubDate = currentArticle.getPublicationDate();
        String shortDateBackwards = currentArticlePubDate.substring(0, 10);
        String shortDate = formatDate(shortDateBackwards);

        // Setting appropriate text into each view:
        dateTextView.setText(shortDate);
        articleNameTextView.setText(currentArticle.getArticlename());
        sectionTextView.setText(currentArticle.getSectionName());

        // Returning the listItemView:
        return listItemView;
    }

    // I needed some help here converting date from "yyyy-MM-dd" to dd/MM/yyyy
    // This stackoverflow post helped me out:
    // https://stackoverflow.com/questions/17324060/converting-yyyy-mm-dd-into-dd-mm-yyyy
    private String formatDate(String strDate) {
        DateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat outFormat = new SimpleDateFormat("dd/MM/yyyy");
        Date date = null;
        try {
            date = inFormat.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return outFormat.format(date);
    }
}
