package com.petsay.component.photo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.petsay.R;
import com.petsay.activity.ExBaseAdapter;
import com.petsay.utile.ImageLoaderHelp;
import com.petsay.vo.petalk.PhotoAlbumItem;

import java.io.File;

/**
 * 相册文件夹列表Adapter
 * @author wangw (404441027@qq.com)
 * @CreateDate 2015/4/1
 * @Description
 */
public class PhotoAlbumAdapter extends ExBaseAdapter<PhotoAlbumItem> {

    public PhotoAlbumAdapter(Context context) {
        super(context);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        return super.getView(position, convertView, parent);
        ViewHolder holder = null;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.photoalbum_item,null);
            holder = new ViewHolder();
            holder.findViews(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        PhotoAlbumItem item = getItem(position);
        holder.tvName.setText(getPathNameToShow(item));//item.folderPath +"("+item.fileCount +")");
        ImageLoaderHelp.displayHeaderImage("file://"+item.thumbnailPath,holder.ivThumbnail);


        return convertView;
    }

    class ViewHolder{
        public ImageView ivThumbnail;
        public TextView tvName;

        public void findViews(View rootView) {
            ivThumbnail = (ImageView)rootView.findViewById( R.id.iv_thumbnail );
            tvName = (TextView)rootView.findViewById( R.id.tv_name );
        }

    }

    /**根据完整路径，获取最后一级路径，并拼上文件数用以显示。*/
    private String getPathNameToShow(PhotoAlbumItem item) {
        int lastSeparator = item.folderPath.lastIndexOf(File.separator);
        return item.folderPath.substring(lastSeparator + 1) + "(" + item.fileCount + ")";
    }

}
