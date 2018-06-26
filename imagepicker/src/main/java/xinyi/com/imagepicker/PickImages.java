package xinyi.com.imagepicker;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.view.CropImageView;

import java.util.ArrayList;
import java.util.List;

import xinyi.com.selectmore.R;

public class PickImages extends LinearLayout {

    private View rootView;
    private int numColumns;//recycleView 列数
    private int maxImages;//最大选取的图片
    private int defaluteImage;//加载错误.占位符图片

    private RecyclerView mRecycleView;
    private List<ImageModel> dataList = new ArrayList<>();
    private PhotoAdapter mPhotoAdapter;

    public PickImages(Context context) {
        super(context);
    }

    public PickImages(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.PickMoreStyle, 0, 0);
        numColumns = a.getInt(R.styleable.PickMoreStyle_numColumns, 3);
        maxImages = a.getInt(R.styleable.PickMoreStyle_maxImages, 9);
        defaluteImage =a.getResourceId(R.styleable.PickMoreStyle_itemDefaluteImage,R.mipmap.ic_launcher);
        a.recycle();
        init(context);
    }

    public PickImages(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    public void init(Context context) {
        rootView = View.inflate(context, R.layout.pick_more, this);
        mRecycleView = rootView.findViewById(R.id.mRecycleView);
        mRecycleView.setLayoutManager(new GridLayoutManager(context, numColumns));
        dataList.add(new ImageModel());//初始化元素
        mPhotoAdapter = new PhotoAdapter(dataList, defaluteImage, maxImages);
        mRecycleView.setAdapter(mPhotoAdapter);

        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new GlideImageLoader());   //设置图片加载器
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setCrop(false);        //允许裁剪（单选才有效）
        imagePicker.setSaveRectangle(true); //是否按矩形区域保存
        imagePicker.setSelectLimit(numColumns);    //选中数量限制
        imagePicker.setStyle(CropImageView.Style.RECTANGLE);  //裁剪框的形状
        imagePicker.setFocusWidth(800);   //裁剪框的宽度。单位像素（圆形自动取宽高最小值）
        imagePicker.setFocusHeight(800);  //裁剪框的高度。单位像素（圆形自动取宽高最小值）
        imagePicker.setOutPutX(1000);//保存文件的宽度。单位像素
        imagePicker.setOutPutY(1000);//保存文件的高度。单位像素
    }

    //拍完照片之后的回调
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == PhotoAdapter.IMAGE_PICKER) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                for (ImageItem mImageItem : images
                        ) {
                    ImageModel model = new ImageModel();
                    model.setPath(mImageItem.path);
                    dataList.add(model);
                }
                mPhotoAdapter.notifyDataSetChanged();

            } else {
                Toast.makeText(mRecycleView.getContext(), "没有数据", Toast.LENGTH_SHORT).show();
            }
        }
        if (resultCode == PhotoAdapter.IMAGE_CHECK) {
            List<ImageModel> d = JSONObject.parseObject(data.getStringExtra("select_more_data"), new TypeReference<List<ImageModel>>() {
            });
            dataList.clear();
            dataList.add(new ImageModel());
            dataList.addAll(d);
            mPhotoAdapter.notifyDataSetChanged();
        }
    }

    public List<ImageModel> getDataList() {
        return dataList;
    }
    public PickImages setDataList(List<ImageModel> dataList) {
        this.dataList = dataList;
        return this;
    }
}
