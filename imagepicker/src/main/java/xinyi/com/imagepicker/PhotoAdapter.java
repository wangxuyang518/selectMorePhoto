package xinyi.com.imagepicker;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.ui.ImageGridActivity;

import java.util.List;

public class PhotoAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int IMAGE_PICKER=1000;
    public static final int IMAGE_CHECK=1001;
    private List<ImageModel> dataSource;
    private int defaluteImage;
    private int maxImages;
    public PhotoAdapter(List<ImageModel> dataSource,int defaluteImage,int maxImages) {
        this.dataSource = dataSource;
        this.defaluteImage=defaluteImage;
        this.maxImages=maxImages;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //创建不同的 ViewHolder
        View view=View.inflate(parent.getContext(), R.layout.item_image, null);
        return new MoreSelectItemHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder,final int position) {
        final ImageModel t = dataSource.get(dataSource.size()-position-1);
        final MoreSelectItemHolder moreSelectItemHolder= (MoreSelectItemHolder) holder;
        moreSelectItemHolder.tagImageView.setVisibility(View.VISIBLE);
        if (t.getPath()==null||t.getPath().equals("")){
            moreSelectItemHolder.iconImageView.setImageResource(t.getAddImageView());
            moreSelectItemHolder.tagImageView.setVisibility(View.GONE);
        }else {
            RequestOptions options = new RequestOptions()
                    .centerCrop()
                    .placeholder(defaluteImage)
                    .error(defaluteImage);
            Glide.with(moreSelectItemHolder.iconImageView.getContext())
                    .load(t.getPath())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .apply(options)
                    .into(moreSelectItemHolder.iconImageView);
        }
        moreSelectItemHolder.iconImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (t.getPath()==null||t.getPath().equals("")){
                    //添加新的照片
                    ImagePicker.getInstance().setSelectLimit(maxImages-dataSource.size()+1);    //选中数量限制
                    Intent intent = new Intent(  moreSelectItemHolder.iconImageView.getContext(), ImageGridActivity.class);
                    ((Activity)moreSelectItemHolder.iconImageView.getContext()).startActivityForResult(intent, IMAGE_PICKER);
                }else {
                    //查看大图
                    Intent intent = new Intent(  moreSelectItemHolder.iconImageView.getContext(), ImageActivity.class);
                    intent.putExtra("select_more_data", com.alibaba.fastjson.JSONObject.toJSONString(dataSource));
                    ((Activity)moreSelectItemHolder.iconImageView.getContext()).startActivityForResult(intent,IMAGE_CHECK);
                }
            }
        });
        moreSelectItemHolder.tagImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataSource.remove(dataSource.size()-position-1);
                notifyDataSetChanged();
            }
        });

    }

    @Override
    public int getItemCount() {
        if (dataSource.size()<=maxImages){
            return dataSource.size();
        }else {
            return maxImages;
        }
    }



    private class MoreSelectItemHolder extends RecyclerView.ViewHolder {

        public ImageView tagImageView;
        public ImageView iconImageView;

        public MoreSelectItemHolder(View itemView) {
            super(itemView);
            tagImageView = itemView.findViewById(R.id.tagPhoto);
            iconImageView = itemView.findViewById(R.id.ivPhoto);

        }
    }
}
