package com.charade.mathcharade;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Nat on 19/4/2016.
 */
public class TopicAdapter extends ArrayAdapter<Topic> {

    public TopicAdapter(Context context, ArrayList<Topic> topics) {
        super(context, 0, topics);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Topic topic = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_topic, parent, false);
        }
        // Lookup view for data population
        Button topicName = (Button) convertView.findViewById(R.id.topic_name);
        // Populate the data into the template view using the data object
        topicName.setText(topic.getTopicName());

        // Return the completed view to render on screen
        return convertView;
    }
}
