package com.example.android.movies;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;


public class TrailReviewAdapters extends BaseAdapter {

    private Context mContext;
    List<String> Trialskeys;
    List<String> ReviewsAuthor;
    List<String> ReviewsContent;

    LinearLayout ListTrails;
    LinearLayout ListReviews;
    public TrailReviewAdapters(Context c,List<String> keys,List<String> author,List<String> content) {
        mContext = c;
        //Trialskeys.clear();
        Trialskeys=keys;
        ReviewsAuthor=author;
        ReviewsContent=content;
    }
    @Override
    public int getCount() {
      return 1;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.trial_review_item, parent, false);
            ListTrails= (LinearLayout) convertView.findViewById(R.id.ListTrials);
            ListReviews= (LinearLayout) convertView.findViewById(R.id.ListReviews);
        }
        for (int i = 0; i <Trialskeys.size() ; i++) {
        LinearLayout inner=new LinearLayout(mContext);
            inner.setOrientation(LinearLayout.HORIZONTAL);
            TextView trailNumber=new TextView(mContext);
            trailNumber.setText("Trial " + (i + 1));
            ImageView youtubeImage =new ImageView(mContext);
            youtubeImage.setImageResource(R.drawable.red_heart);
            inner.addView(trailNumber);
            inner.addView(youtubeImage);
           final int  counter =i;
            inner.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent =new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" + Trialskeys.get(counter)));
                    mContext.startActivity(intent);
                }
            });
            ListTrails.addView(inner);
        }

        for (int i = 0; i <ReviewsAuthor.size() ; i++) {
            TextView authorText=new TextView(mContext);
            authorText.setText(ReviewsAuthor.get(i));
            TextView ReviewContentText=new TextView(mContext);
            ReviewContentText.setText(ReviewsContent.get(i));
            ListReviews.addView(authorText);
            ListReviews.addView(ReviewContentText);

        }


        return convertView;
    }

}
