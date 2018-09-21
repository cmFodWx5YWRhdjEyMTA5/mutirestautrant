package com.hcpt.multirestaurants.fragment;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxStatus;
import com.androidquery.callback.BitmapAjaxCallback;
import com.hcpt.multirestaurants.R;
import com.hcpt.multirestaurants.util.ImageUtil;

@SuppressLint("NewApi")
public final class BannerFragment extends Fragment implements OnClickListener {

    private View view;
    private ImageView imgBanner;
    private ProgressBar progress;
    public String urlImage;

    public static BannerFragment InStances(String urlImage) {
        BannerFragment fragment = new BannerFragment();
        fragment.urlImage = urlImage;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        view = inflater.inflate(R.layout.fragment_layout_banner, null);
        AQuery aq = new AQuery(getActivity());
        imgBanner = (ImageView) view.findViewById(R.id.imgBanner);
        ImageUtil.calViewRatio(getActivity(), imgBanner, 9, 9, 0);
        progress = (ProgressBar) view.findViewById(R.id.progess);
        aq.id(imgBanner)
                .progress(progress)
                .image(urlImage, true, true, 0,
                        R.drawable.no_image_available_horizontal,
                        new BitmapAjaxCallback() {
                            @SuppressWarnings("deprecation")
                            @Override
                            public void callback(String url, ImageView iv,
                                                 Bitmap bm, AjaxStatus status) {

                                Drawable d = new BitmapDrawable(getResources(),
                                        bm);
                                imgBanner.setBackgroundDrawable(d);
                            }
                        });
        return view;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
    }
}
