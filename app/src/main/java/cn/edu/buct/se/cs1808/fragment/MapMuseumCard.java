package cn.edu.buct.se.cs1808.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import org.jetbrains.annotations.NotNull;

import cn.edu.buct.se.cs1808.R;
import cn.edu.buct.se.cs1808.components.ExhibitionCard;
import cn.edu.buct.se.cs1808.components.TextWithIcon;
import cn.edu.buct.se.cs1808.components.VideoListItem;
import cn.edu.buct.se.cs1808.utils.DensityUtil;
import cn.edu.buct.se.cs1808.utils.LoadImage;
import cn.edu.buct.se.cs1808.utils.Museum;
import cn.edu.buct.se.cs1808.utils.RoundView;

public class MapMuseumCard extends DialogFragment {
    private View view;
    private ImageView museumImage;
    private TextView museumName;
    private TextView museumPos;
    private TextView museumIntroduce;
    private LinearLayout museumVideoArea;
    private LinearLayout museumExhibitionArea;
    private TextView moreVideoButton;
    private TextView moreExhibitionButton;

    private OnClickListener listener;
    private int museumId;
    private Museum museum;
    public MapMuseumCard(Museum museum) {
        this.museum = museum;
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
        museumName = (TextView) view.findViewById(R.id.museumName);
        museumPos = (TextView) view.findViewById(R.id.museumPos);
        museumIntroduce = (TextView) view.findViewById(R.id.museumIntroduce);
        museumVideoArea = (LinearLayout) view.findViewById(R.id.videoArea);
        museumExhibitionArea = (LinearLayout) view.findViewById(R.id.exhibitionArea);
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
        setMuseumInfo(museum);

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
        walk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(ClickAction.GET_DRIVE_ROUTER_CLICK);
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        loadExhibitions(1);
        loadVideos(1);
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

    public void setMuseumInfo(Museum museum) {
        this.museum = museum;
        this.museumId = museum.getId();
        museumName.setText(museum.getName());
        museumPos.setText(museum.getPos());
        museumIntroduce.setText(museum.getIntroduce());
        museumImage.setImageResource(R.mipmap.ic_launcher);
        LoadImage loader = new LoadImage(museumImage);
        loader.setBitmap(museum.getImageSrc());
    }

    public void addVideo(VideoListItem item) {
        museumVideoArea.addView(item);
    }

    private void loadVideos(int id) {
        Context ctx = getContext();
        for (int i = 0; i < 3; i ++) {
            VideoListItem item = new VideoListItem(ctx);
            item.setAttr("讲解视频" + i, "essay", "13:14", "2021-12-31 22-22", R.mipmap.ic_launcher);
            addVideo(item);
        }
    }

    public void addExhibition(ExhibitionCard item) {
        museumExhibitionArea.addView(item);
    }

    private void loadExhibitions(int id) {
        Context ctx = getContext();
        for (int i = 0; i < 3; i ++) {
            ExhibitionCard item = new ExhibitionCard(ctx);
            item.setAttr(R.mipmap.ic_launcher, "展览" + i);
            addExhibition(item);
        }
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
