package cn.edu.buct.se.cs1808.components;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;

import cn.edu.buct.se.cs1808.R;
import cn.edu.buct.se.cs1808.utils.LoadImage;
import cn.edu.buct.se.cs1808.utils.RoundView;

public class VideoListItem extends ConstraintLayout {
    private View view;
    private ImageView videoImage;
    private TextView videoTitle;
    private TextView videoUser;
    private TextView videoTime;
    private TextView videoUploadTime;
    private TextView statusText;


    public VideoListItem(Context context) {
        super(context);
        init(context);
    }

    public VideoListItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.VideoListItem);
        if (typedArray != null) {
            String title = typedArray.getString(R.styleable.VideoListItem_video_title);
            String user = typedArray.getString(R.styleable.VideoListItem_video_user);
            String time = typedArray.getString(R.styleable.VideoListItem_video_time);
            String uploadTime = typedArray.getString(R.styleable.VideoListItem_video_uploadTime);
            int resourceId = typedArray.getResourceId(R.styleable.VideoListItem_video_image, R.mipmap.ic_launcher);
            setAttr(title, user, time, uploadTime, resourceId);
        }
    }

    private void init(Context context) {
        view = LayoutInflater.from(context).inflate(R.layout.layout_video_list_item, this);

        videoImage = (ImageView) view.findViewById(R.id.videoItemImage);
        videoTitle = (TextView) view.findViewById(R.id.videoItemTitle);
        videoUser = (TextView) view.findViewById(R.id.videoItemUser);
        videoTime = (TextView) view.findViewById(R.id.videoItemTime);
        videoUploadTime = (TextView) view.findViewById(R.id.videoItemUploadTime);
        statusText = (TextView) view.findViewById(R.id.videoStatus);

        int radius = 12;
        RoundView.setRadiusWithDp(radius, videoImage);
        RoundView.setRadiusWithDp(radius, videoUser);
        RoundView.setRadiusWithDp(radius, videoTime);
        RoundView.setRadiusWithDp(radius, videoUploadTime);
    }

    public void setAttr(String title, String user, String time, String uploadTime, String imageSrc) {
        if (imageSrc != null) {
            LoadImage loader = new LoadImage(videoImage);
            loader.setBitmap(imageSrc);
        }
        if (title != null)
            videoTitle.setText(title);
        if (user != null)
            videoUser.setText(user);
        if (time != null)
            videoTime.setText(time);
        if (uploadTime != null)
            videoUploadTime.setText(uploadTime);
    }
    public void setTime(String time) {
        if (time != null)
            videoTime.setText(time);
    }

    public void setAttr(String title, String user, String time, String uploadTime, int resourceId) {
        videoImage.setImageResource(resourceId);
        setAttr(title, user, time, uploadTime, null);
    }
    public void setAttr(String title, String user, String time, String uploadTime, String imageSrc, boolean status) {
        setAttr(title, user, time, uploadTime, imageSrc);
        if (status) {
            statusText.setText("已审核通过");
        }
        else {
            statusText.setText("未审核");
        }
    }

    public void setAttr(String title, String user, String time, String uploadTime, String imageSrc, String museumName) {
        setAttr(title, user, time, uploadTime, imageSrc);
        statusText.setText(museumName);
    }

}
