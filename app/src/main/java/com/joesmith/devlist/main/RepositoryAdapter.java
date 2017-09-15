package com.joesmith.devlist.main;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.joesmith.devlist.data.Developer;
import com.joesmith.devlist.R;

import java.util.ArrayList;


public class RepositoryAdapter extends ArrayAdapter<Developer> {

    public RepositoryAdapter(Activity context, ArrayList<Developer> repos){
        super(context, 0, repos);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_repositories, parent, false);
        }

        Developer repository = getItem(position);

        TextView reposName = (TextView)convertView.findViewById(R.id.repos_name);
        reposName.setText(repository.getReposName());

        TextView reposDescription = (TextView)convertView.findViewById(R.id.repos_description);
        reposDescription.setText(repository.getDescription());

        TextView starCount = (TextView)convertView.findViewById(R.id.star_count);
        starCount.setText(String.valueOf(repository.getStarCount()));

        return convertView;
    }
}
