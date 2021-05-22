package cn.edu.buct.se.cs1808.fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.DialogFragment;

import com.baidu.mapapi.utils.DistanceUtil;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.edu.buct.se.cs1808.DetailsExhibitionActivity;
import cn.edu.buct.se.cs1808.R;
import cn.edu.buct.se.cs1808.VideoIntroduceActivity;
import cn.edu.buct.se.cs1808.VideoPlayActivity;
import cn.edu.buct.se.cs1808.api.ApiPath;
import cn.edu.buct.se.cs1808.api.ApiTool;
import cn.edu.buct.se.cs1808.components.ExhibitionCard;
import cn.edu.buct.se.cs1808.components.TextWithIcon;
import cn.edu.buct.se.cs1808.components.VideoListItem;
import cn.edu.buct.se.cs1808.utils.DensityUtil;
import cn.edu.buct.se.cs1808.utils.LoadImage;
import cn.edu.buct.se.cs1808.utils.Museum;
import cn.edu.buct.se.cs1808.utils.RoundView;
import cn.edu.buct.se.cs1808.utils.VideoUtil;

public class MapMuseumCard extends DialogFragment {
    private View view;
    private ImageView museumImage;
    private TextView museumName;
    private TextView museumPos;
    private TextView museumIntroduce;
    private LinearLayout museumVideoArea;
    private LinearLayout museumExhibitionArea;
    private TextView museumDistance;
    private TextView moreVideoButton;
    private TextView moreExhibitionButton;
    private ScrollView bottomCardScroll;

    private OnClickListener listener;
    private int museumId;
    private Museum museum;
    private String distance;
    public MapMuseumCard(Museum museum, String distance) {
        this.museum = museum;
        this.distance = distance;
    }
    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater, @Nullable @org.jetbrains.annotations.Nullable ViewGroup container, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_map_museum_card, container, false);
    }

    @Override
    public void onViewCreated(@NonNull @NotNull View view, @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.view = view;
        museumImage = (ImageView) view.findViewById(R.id.museumImage);
        RoundView.setRadiusWithDp(12, museumImage);
        museumImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(ClickAction.MUSEUM_IMAGE_CLICK);
                }
            }
        });
        bottomCardScroll = (ScrollView) view.findViewById(R.id.bottomCardScroll);
        museumName = (TextView) view.findViewById(R.id.museumName);
        museumPos = (TextView) view.findViewById(R.id.museumPos);
        museumIntroduce = (TextView) view.findViewById(R.id.museumIntroduce);
        museumVideoArea = (LinearLayout) view.findViewById(R.id.videoArea);
        moreVideoTitle = (ConstraintLayout) view.findViewById(R.id.moreVideoTitle);
        museumExhibitionArea = (LinearLayout) view.findViewById(R.id.exhibitionArea);
        moreExhibitionTitle = (ConstraintLayout) view.findViewById(R.id.moreExhibitionTitle);
        moreExhibitionButton = (TextView) view.findViewById(R.id.gotoMoreExhibition);
        moreExhibitionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(ClickAction.MORE_EXHIBITION_CLICK);
                }
            }
        });
        moreVideoButton = (TextView) view.findViewById(R.id.gotoMoreVideo);
        moreVideoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(ClickAction.MORE_VIDEO_CLICK);
                }
            }
        });
        museumDistance = (TextView) view.findViewById(R.id.museumDistance);
        setMuseumInfo(museum, distance);

        TextWithIcon walk = (TextWithIcon) view.findViewById(R.id.getWalkRoute);
        RoundView.setRadiusWithDp(12, walk);
        walk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(ClickAction.GET_WALK_ROUTER_CLICK);
                }
            }
        });
        TextWithIcon drive = (TextWithIcon) view.findViewById(R.id.getDriveRoute);
        RoundView.setRadiusWithDp(12, drive);
        drive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(ClickAction.GET_DRIVE_ROUTER_CLICK);
                }
            }
        });
    }

    private ConstraintLayout moreVideoTitle;
    /**
     * 控制 相关视频 列表是否显示
     * @param ifShow 是否显示
     */
    private void hiddenVideoList(boolean ifShow) {
        if (ifShow) {
            moreVideoTitle.setVisibility(View.VISIBLE);
            museumVideoArea.setVisibility(View.VISIBLE);
        }
        else {
            moreVideoTitle.setVisibility(View.GONE);
            museumVideoArea.setVisibility(View.GONE);
        }
    }

    private ConstraintLayout moreExhibitionTitle;

    /**
     * 控制 相关展览 列表是否显示
     * @param ifShow 是否显示
     */
    private void hiddenExhibitionList(boolean ifShow) {
        if (ifShow) {
            moreExhibitionTitle.setVisibility(View.VISIBLE);
            museumExhibitionArea.setVisibility(View.VISIBLE);
        }
        else {
            moreExhibitionTitle.setVisibility(View.GONE);
            museumExhibitionArea.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public @NotNull Dialog onCreateDialog(Bundle savedInstanceState) {
        // 使用不带Theme的构造器, 获得的dialog边框距离屏幕仍有几毫米的缝隙。
        Dialog dialog = new Dialog(getActivity(), R.style.FullScreenDialogStyle);

        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); // 设置Content前设定
        dialog.setContentView(R.layout.layout_map_museum_card);
        dialog.setCanceledOnTouchOutside(true); // 外部点击取消

        // 设置宽度为屏宽, 靠近屏幕底部。
        Window window = dialog.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.gravity = Gravity.BOTTOM; // 紧贴底部
        lp.width = WindowManager.LayoutParams.MATCH_PARENT; // 宽度持平
        lp.height = DensityUtil.dip2px(getContext(), getContext().getResources().getConfiguration().screenHeightDp * 0.618f);
        lp.dimAmount = 0.5f;
        window.setAttributes(lp);

        return dialog;
    }

    /**
     * 初始化卡片上的博物馆的信息
     * @param museum 博物馆对象
     * @param distance 当前定位的地址与博物馆位置之间的距离，单位KM
     */
    public void setMuseumInfo(Museum museum, String distance) {
        this.museum = museum;
        this.museumId = museum.getId();
        this.distance = distance;
        museumName.setText(museum.getName());
        museumPos.setText(museum.getPos());
        museumIntroduce.setText(museum.getIntroduce());
        museumImage.setImageResource(R.mipmap.ic_launcher);
        museumDistance.setText(String.format("%skm", distance));
        bottomCardScroll.fullScroll(ScrollView.FOCUS_UP);
        LoadImage loader = new LoadImage(museumImage);
        loader.setBitmap(museum.getImageSrc());
        Log.i("museId", String.valueOf(museumId));
    }

    /**
     * 加载部分博物馆对应的讲解视频信息
     * @param context 应用上下文
     * @param id 博物馆ID
     * @param num 需要加载的数量
     */
    public void loadMuseumVideo(Context context, int id, int num) {
        JSONObject params = new JSONObject();
        try {
            params.put("pageIndex", 1);
            params.put("pageSize", num);
            params.put("muse_ID", id);
        }
        catch (JSONException e) {
            Toast.makeText(context, "博物馆讲解视频列表加载失败", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiTool.request(context, ApiPath.GET_VIDEO, params, (JSONObject rep) -> {
            String code;
            try {
                code = rep.getString("code");
            }
            catch (JSONException e){
                code = "未知错误";
            }
            if (!"success".equals(code)) {
                Toast.makeText(context, "加载失败: " + code, Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                JSONObject info = rep.getJSONObject("info");
                JSONArray items = info.getJSONArray("items");
                for (int i = 0; i < items.length(); i ++) {
                    JSONObject item = items.getJSONObject(i);
                    int ifShow = item.getInt("video_IfShow");
                    if (ifShow == 0) continue;
                    VideoListItem video = new VideoListItem(context);
                    String title = item.getString("video_Name");
                    String uploadTime = item.getString("video_Time");
                    String userName = item.getString("user_Name");
                    String videoUrl = item.getString("video_Url");
                    String imageUrl = VideoIntroduceActivity.getVideoImage(videoUrl);
                    int videoId = item.getInt("video_ID");
                    // 暂时无法获取视频的时长
                    String time = "loading";
                    video.setAttr(title, userName, time, uploadTime, imageUrl);
                    VideoUtil.setVideoDuration(context, ApiTool.getADDRESS() + videoUrl, (int duration) -> {
                        video.setTime(VideoUtil.durationSecToString(duration));
                    });
                    addVideo(video, videoId);
                }
                hiddenVideoList(items.length() != 0);
            }
            catch (JSONException ignore) {}
        }, (JSONObject error) -> {
            try {
                Toast.makeText(context, "请求失败: " + error.get("info"), Toast.LENGTH_SHORT).show();
            }
            catch (JSONException e) {
                Toast.makeText(context, "请求失败: 未知错误", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 向列表中添加一个Video
     * @param item 列表item，代表一个video
     */
    public void addVideo(VideoListItem item, int videoId) {
        museumVideoArea.addView(item);
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), VideoPlayActivity.class);
                intent.putExtra("video_ID", videoId);
                startActivity(intent);
            }
        });
    }

    /**
     * 向列表中添加一个Exhibition
     * @param item 列表item，代表一个展览
     */
    public void addExhibition(ExhibitionCard item, int museId, String image, String name, String content) {
        museumExhibitionArea.addView(item);
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DetailsExhibitionActivity.class);
                intent.putExtra("muse_ID", museId);
                intent.putExtra("exhib_Pic", image);
                intent.putExtra("exhib_Name", name);
                intent.putExtra("exhib_Content", content);
                startActivity(intent);
            }
        });
    }

    /**
     * 加载该博物馆对应的展览列表
     * @param context 应用上下文
     * @param id 博物馆ID
     * @param num 加载的数量
     */
    public void loadMuseumExhibitions(Context context, int id, int num) {
        JSONObject params = new JSONObject();
        try {
            params.put("pageIndex", 1);
            params.put("pageSize", num);
            params.put("muse_ID", id);
        }
        catch (JSONException e) {
            Toast.makeText(context, "博物馆展览列表加载失败", Toast.LENGTH_SHORT).show();
            return;
        }
        ApiTool.request(context, ApiPath.GET_EXHIBITIONS, params, (JSONObject rep) -> {
            String code;
            try {
                code = rep.getString("code");
            }
            catch (JSONException e){
                code = "未知错误";
            }
            if (!"success".equals(code)) {
                Toast.makeText(context, "加载失败: " + code, Toast.LENGTH_SHORT).show();
                return;
            }
            try {
                JSONObject info = rep.getJSONObject("info");
                JSONArray items = info.getJSONArray("items");
                for (int i = 0; i < items.length(); i ++) {
                    JSONObject item = items.getJSONObject(i);
                    ExhibitionCard exhibitionCard = new ExhibitionCard(context);
                    String name = item.getString("exhib_Name");
                    exhibitionCard.setAttr(R.drawable.bblk_exhibition, name);
                    String imageSrc = item.getString("exhib_Pic");
                    String content = item.getString("exhib_Content");
                    exhibitionCard.setImage(imageSrc);
                    addExhibition(exhibitionCard, museumId, imageSrc, name, content);
                }
                hiddenExhibitionList(items.length() != 0);
            }
            catch (JSONException ignore) {}
        }, (JSONObject error) -> {
            try {
                Toast.makeText(context, "请求失败: " + error.get("info"), Toast.LENGTH_SHORT).show();
            }
            catch (JSONException e) {
                Toast.makeText(context, "请求失败: 未知错误", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public int getMuseumId() {
        return museumId;
    }

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
    }

    public static abstract class OnClickListener {
        public abstract void onClick(ClickAction action);
    }

    public static enum ClickAction {
        MUSEUM_IMAGE_CLICK,
        MORE_VIDEO_CLICK,
        MORE_EXHIBITION_CLICK,
        GET_WALK_ROUTER_CLICK,
        GET_DRIVE_ROUTER_CLICK
    }

}
