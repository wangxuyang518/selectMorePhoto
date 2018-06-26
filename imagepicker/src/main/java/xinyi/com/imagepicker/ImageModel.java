package xinyi.com.imagepicker;

import java.io.Serializable;
import xinyi.com.selectmore.R;
public class ImageModel implements Serializable{

   public String path="";
    public int addImageView=R.mipmap.add_photo;

    public String getPath() {
        return path;
    }

    public ImageModel setPath(String path) {
        this.path = path;
        return this;
    }

    public int getAddImageView() {
        return addImageView;
    }

    public ImageModel setAddImageView(int addImageView) {
        this.addImageView = addImageView;
        return this;
    }
}
