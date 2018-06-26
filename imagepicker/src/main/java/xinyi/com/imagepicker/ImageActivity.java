package xinyi.com.imagepicker;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import xinyi.com.selectmore.R;

import static xinyi.com.imagepicker.PhotoAdapter.IMAGE_CHECK;

/**
 * 查看大图
 */
public class ImageActivity extends AppCompatActivity {
    private List<ImageModel> dataSource = new ArrayList<>();
    private ImageView ivBack;
    private ImageView ivDelete;
    private TextView tvTips;
    private ViewPager mViewPager;
    private ArrayList<View> images = new ArrayList<>();
    private CheckImageAdapter mCheckImageAdapter;
    private int p = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_image);
        ivBack = findViewById(R.id.ivBack);
        ivDelete = findViewById(R.id.ivDelete);
        tvTips = findViewById(R.id.tvTips);
        mViewPager = findViewById(R.id.mViewPager);
        dataSource.clear();
        dataSource.addAll(JSONObject.parseObject(getIntent().getStringExtra("select_more_data"), new TypeReference<List<ImageModel>>() {
        }));
        dataSource.remove(0);
        for (int i = 0; i < dataSource.size(); i++) {
            ImageModel imageModel = dataSource.get(i);
            View v = View.inflate(this, R.layout.item_check_image, null);
            uk.co.senab.photoview.PhotoView photoView = v.findViewById(R.id.photoview);
            RequestOptions options = new RequestOptions()
                    .centerCrop();
            Glide.with(this)
                    .load(imageModel.getPath())
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .apply(options)
                    .into(photoView);
            images.add(v);
        }
        mCheckImageAdapter = new CheckImageAdapter();
        mViewPager.setAdapter(mCheckImageAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                ImageActivity.this.p = position + 1;
                tvTips.setText("" + p + "/" + (images.size()));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (images.size() >= 1) {
                    dataSource.remove(p - 1);
                    images.remove(p - 1);
                    mCheckImageAdapter.notifyDataSetChanged();
                    tvTips.setText("" + p + "/" + (images.size()));
                    if (images.size() == 0) {
                        tvTips.setText("0/0");
                    }
                }
            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResult();
                finish();
            }
        });
        tvTips.setText("" + p + "/" + (images.size()));
    }


    public class CheckImageAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup view, int position, Object object) {
            view.removeView((View) object);
        }

        // 当要显示的图片可以进行缓存的时候，会调用这个方法进行显示图片的初始化，我们将要显示的ImageView加入到ViewGroup中，然后作为返回值返回即可
        @Override
        public Object instantiateItem(ViewGroup view, int position) {
            view.addView(images.get(position));
            return images.get(position);
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }
    }

    @Override
    public void onBackPressed() {
        setResult();
        super.onBackPressed();
    }

    public void setResult() {
        Intent intent = new Intent();
        intent.putExtra("select_more_data", JSONObject.toJSONString(dataSource)); //将计算的值回传回去
        setResult(IMAGE_CHECK, intent);
    }
}
