package com.petsay.component.view;

import com.petsay.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * 表情
 * @author G
 *
 */
public class FaceGrid extends LinearLayout{

	private GridView gridFace;
	private String[] faces;
	private int[] faceRes;
	private Context mContext;
	public FaceGrid(Context context) {
		super(context);
		inflate(context, R.layout.face_grid, this);
		initView();
	}
	
	public FaceGrid(Context context, AttributeSet attrs) {
		super(context, attrs);
		inflate(context, R.layout.face_grid, this);
		initView();
	}
	
	private void initView(){
		gridFace=(GridView) findViewById(R.id.grid_face);
		gridFace.setAdapter(new FaceAdapter());
	}
	
	private class FaceAdapter extends BaseAdapter{

		
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return faces.length;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return faces[position];
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Holder holder;
			if (null==convertView) {
				holder=new Holder();
				convertView=LayoutInflater.from(mContext).inflate(R.layout.face_grid_item, null);
			    holder.imgFace=(ImageView) convertView.findViewById(R.id.img_face);
				convertView.setTag(holder);
			}else {
				holder=(Holder) convertView.getTag();
			}
			holder.imgFace.setImageResource(faceRes[position]);
			return convertView;
		}
		
		class Holder{
			private ImageView imgFace;
		}
	}
}
