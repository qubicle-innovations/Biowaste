package uems.biowaste.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import uems.biowaste.R;
import uems.biowaste.vo.GWasteItemVo;


public class GwasteListAdapter extends BaseAdapter {


    private ArrayList<GWasteItemVo> pList;
    private Context context;
    private LayoutInflater inflater;
    private int color;

    public GwasteListAdapter(Context context, ArrayList<GWasteItemVo> pList, int color) {
        this.pList = pList;
        this.context = context;
        this.color =color;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    public void updateList(ArrayList<GWasteItemVo> pList) {
        this.pList = pList;
        notifyDataSetChanged();
        notifyDataSetInvalidated();
    }


    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return pList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    public GWasteItemVo getProduct(int position) {
        // TODO Auto-generated method stub
        return pList.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder v;
        if (convertView != null && convertView.getTag() != null) {
            v = (ViewHolder) convertView.getTag();
        } else {

            convertView = inflater.inflate(R.layout.gwaste_item, null);
            TextView tvTitle, tvDate, tvUser;
            ImageView imCal, imArrow, imUser;
            v = new ViewHolder();
            v.tvMonth = (TextView) convertView.findViewById(R.id.tvMonth);
            v.tvDate = (TextView) convertView.findViewById(R.id.tvDate);
            v.tvUser = (TextView) convertView.findViewById(R.id.tvUser);
            v.tvKg = (TextView) convertView.findViewById(R.id.tvKg);
            v.borderView =  convertView.findViewById(R.id.borderView);
            convertView.setTag(v);
        }
        if (position % 2 == 0) {
            v.borderView.setBackgroundColor(color);
        } else {
            v.borderView.setBackgroundColor(ContextCompat.getColor(context, R.color.backgroundGray));

        }
        v.tvMonth.setText(pList.get(position).getMonth());
        v.tvDate.setText(pList.get(position).getDate());
        v.tvUser.setText(pList.get(position).getCreatedBy());
        v.tvKg.setText(pList.get(position).getTotalWeight() +" Kg");
        return convertView;
    }


    public class ViewHolder {
        TextView tvMonth, tvDate, tvUser, tvKg;
        View borderView;
     }

}
