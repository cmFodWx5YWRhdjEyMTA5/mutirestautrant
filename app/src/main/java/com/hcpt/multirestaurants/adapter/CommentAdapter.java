package com.hcpt.multirestaurants.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.bumptech.glide.Glide;
import com.hcpt.multirestaurants.R;
import com.hcpt.multirestaurants.object.Comment;
import com.hcpt.multirestaurants.object.Menu;
import com.hcpt.multirestaurants.util.DateTimeUtility;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class CommentAdapter extends BaseAdapter {

    private ArrayList<Comment> arrComment;
    private LayoutInflater inflater;
    public Menu food;
    public Menu mfood;
    private Context context;
    private AQuery aq;
    public boolean commentOnly = false;
    private boolean checkHaveNoComment = false;

    public CommentAdapter(Context ctx, ArrayList<Comment> arrComment, boolean commentOnly) {
        this.arrComment = arrComment;
        this.commentOnly = commentOnly;
        if (arrComment.size() == 0) {
            checkHaveNoComment = true;
            Comment temp = new Comment();
            this.arrComment.add(temp);
        }
        this.context = ctx;
        inflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        aq = new AQuery(ctx);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        try {
            return arrComment.size();
        } catch (NullPointerException ex) {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return arrComment.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder = null;
        if (convertView == null) {
            holder = new Holder();
            convertView = inflater.inflate(R.layout.item_comment, null);
            holder.llComment = (LinearLayout) convertView
                    .findViewById(R.id.ll_comment);
            holder.llComment.setOnClickListener(null);
            holder.lblComment = (TextView) convertView
                    .findViewById(R.id.lbl_comment);
            holder.lblTime = (TextView) convertView.findViewById(R.id.lbl_time);
            holder.lblUser = (TextView) convertView.findViewById(R.id.lbl_user);
            holder.rtb = (RatingBar) convertView.findViewById(R.id.rtb_user);

            holder.lblFoodName = (TextView) convertView.findViewById(R.id.lblFoodName);
            holder.imgFood = (ImageView) convertView.findViewById(R.id.imgFood);
            holder.mRtb = (RatingBar) convertView.findViewById(R.id.rtbRating);
            holder.lblPrice = (TextView) convertView.findViewById(R.id.lblPrice);
            holder.lblDescription = (TextView) convertView.findViewById(R.id.lblDescription);
            holder.llContent = (LinearLayout) convertView.findViewById(R.id.llContent);
            holder.progess = (ProgressBar) convertView.findViewById(R.id.progess);
            holder.ll_comment_content = (LinearLayout) convertView.findViewById(R.id.ll_comment_content);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        if (position == 0 && !commentOnly) {
            holder.llContent.setVisibility(View.VISIBLE);
            holder.lblFoodName.setText(food.getName());
            holder.lblDescription.setText(Html.fromHtml(food.getDescription()));
            double price = mfood.getPrice()
                    - (mfood.getPrice() * mfood.getPercentDiscount() / 100);
            holder.lblPrice.setText(context.getString(R.string.currency) + String.format("%.1f", price));
            holder.mRtb.setRating((food.getRateValue() / 2));
            final Holder temp = holder;
            aq.id(temp.imgFood)
                    .progress(temp.progess)
                    .image(mfood.getImage(), true, true, 0,
                            R.drawable.no_image_available_horizontal,
                            new BitmapAjaxCallback() {
                                @SuppressWarnings("deprecation")
                                @Override
                                public void callback(String url, ImageView iv,
                                                     Bitmap bm, AjaxStatus status) {

                                    if (bm != null) {
                                        Drawable d = new BitmapDrawable(
                                                context.getResources(), bm);
                                        temp.imgFood.setBackgroundDrawable(d);
                                    } else {
                                        temp.imgFood.setBackgroundResource(R.drawable.no_image_available_horizontal);
                                    }
                                }
                            });

            if (checkHaveNoComment) {
                holder.ll_comment_content.setVisibility(View.GONE);
            } else {
                holder.ll_comment_content.setVisibility(View.VISIBLE);
            }
        } else {
            holder.llContent.setVisibility(View.GONE);
        }
        if (checkHaveNoComment) {

        } else {
            Comment cmt = arrComment.get(position);
            if (cmt != null) {
                holder.lblUser.setText(cmt.getUserID());
                holder.lblComment.setText(cmt.getContent());

                // Set custom time
                String time = "";
                Date curDate = Calendar.getInstance().getTime();
                Date specDate = DateTimeUtility.getDateValueFromString(
                        "yyyy-MM-dd HH:mm:ss", cmt.getCreatedDate());

                long minuteDiff = DateTimeUtility.getDateDiff(curDate, specDate,
                        TimeUnit.MINUTES);
                long hourDiff = DateTimeUtility.getDateDiff(curDate, specDate,
                        TimeUnit.HOURS);
                long dateDiff = DateTimeUtility.getDateDiff(curDate, specDate,
                        TimeUnit.DAYS);

                if (minuteDiff <= 1) {
                    time = parent.getContext().getResources()
                            .getString(R.string.just_now);
                } else if (minuteDiff > 1 && minuteDiff <= 59) {
                    time = minuteDiff
                            + " "
                            + parent.getContext().getResources()
                            .getString(R.string.minutes_ago);
                } else if (minuteDiff > 59) {
                    // Reset minute var
                    minuteDiff = 0;

                    if (hourDiff == 1) {
                        time = hourDiff
                                + " "
                                + parent.getContext().getResources()
                                .getString(R.string.hour_ago);
                    } else if (hourDiff < 24) {
                        time = hourDiff
                                + " "
                                + parent.getContext().getResources()
                                .getString(R.string.hours_ago);
                    } else {
                        // Reset hour var
                        hourDiff = 0;

                        if (dateDiff == 0) {
                            time = parent.getContext().getResources()
                                    .getString(R.string.today);
                        } else if (dateDiff == 1) {
                            time = parent.getContext().getResources()
                                    .getString(R.string.yesterday);
                        } else if (dateDiff == 2) {
                            time = parent.getContext().getResources()
                                    .getString(R.string.two_days_ago);
                        } else if (dateDiff == 3) {
                            time = parent.getContext().getResources()
                                    .getString(R.string.three_days_ago);
                        } else {
                            // Reset date var
                            dateDiff = 0;

                            time = DateTimeUtility.convertStringToDate(
                                    cmt.getCreatedDate(), "yyyy-MM-dd HH:mm:ss",
                                    "EEE, dd MMM yyyy");
                        }
                    }
                }

                holder.lblTime.setText(time);

                holder.rtb
                        .setRating((cmt.getRateValue() / 2));
            }
        }
        return convertView;
    }

    class Holder {
        private LinearLayout llComment;
        private TextView lblUser, lblComment, lblTime;
        private RatingBar rtb;

        LinearLayout llContent, ll_comment_content;

        private TextView lblFoodName, lblDescription;
        private ImageView imgFood;
        private RatingBar mRtb;
        private TextView lblPrice;
        private ProgressBar progess;
    }
}