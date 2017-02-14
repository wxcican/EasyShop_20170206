package com.fuicuiedu.xc.easyshop_20170206.main.me.personInfo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.fuicuiedu.xc.easyshop_20170206.R;
import com.fuicuiedu.xc.easyshop_20170206.model.ItemShow;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 个人信息页面列表的适配器
 */

public class PersonAdapter extends BaseAdapter{

    private List<ItemShow> list = new ArrayList<>();

    public PersonAdapter(List<ItemShow> list){
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.item_person_info,parent,false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.tv_item_name.setText(list.get(position).getItem_title());
        viewHolder.tv_person.setText(list.get(position).getItem_content());
        return convertView;
    }

    class ViewHolder{
        @BindView(R.id.tv_item_name)
        TextView tv_item_name;
        @BindView(R.id.tv_person)
        TextView tv_person;

        public ViewHolder(View view){
            ButterKnife.bind(this,view);
        }
    }
}
