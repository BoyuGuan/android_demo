package Graduation.example;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;

public class PicListAdapterAfterClassification extends BaseAdapter {

    private Context mcontext;
    private LayoutInflater mLayoutInfalter;
    private String[] myClassNameInference;

    PicListAdapterAfterClassification(Context context, String[] classNameInference){
        this.mcontext = context;
        mLayoutInfalter = LayoutInflater.from(context);
        myClassNameInference = classNameInference;
    }

    @Override
    public int getCount() {
        return 50;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    static class ViewHolder{
        public ImageView tvImage;
        public TextView tvTitle, tvClassOriginal, tvClassClassification;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        PicListAdapterBeforeClassification.ViewHolder holder = null;
        if (view == null ){
            view = mLayoutInfalter.inflate(R.layout.list_view_item,null);
            holder = new PicListAdapterBeforeClassification.ViewHolder();
            holder.tvImage = view.findViewById(R.id.tv_image);
            holder.tvTitle = view.findViewById(R.id.tv_title);
            holder.tvClassOriginal = view.findViewById(R.id.tv_class_original);
            holder.tvClassClassification = view.findViewById(R.id.tv_class_classification);
            view.setTag(holder);
        }else {
            holder = (PicListAdapterBeforeClassification.ViewHolder) view.getTag();
        }
        String className = cifarClass.CIFAR_CLASS[i/5];
        //给空间赋值
        holder.tvTitle.setText("第" + String.valueOf(i+1) + "张照片");
        holder.tvClassOriginal.setText("类别："+className);
        holder.tvClassClassification.setText("识别类别：" + myClassNameInference[i]);
        Bitmap bitmap = null;
        try {
            // creating bitmap from packaged into app android asset 'image.jpg',
            // app/src/main/assets/image3.jpg
            bitmap = BitmapFactory.decodeStream(mcontext.getAssets().open("image"+String.valueOf(i) +".jpg"));

        } catch (IOException e) {
            Log.e("PytorchHelloWorld", "Error reading assets", e);
        }
        holder.tvImage.setImageBitmap(bitmap);
        return view;
    }
}
