package edu.cnm.deepdive.nasaapod.view;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.squareup.picasso.Picasso;
import edu.cnm.deepdive.nasaapod.R;
import edu.cnm.deepdive.nasaapod.model.entity.Apod;
import edu.cnm.deepdive.nasaapod.model.entity.Apod.MediaType;
import edu.cnm.deepdive.nasaapod.model.pojo.ApodWithStats;
import java.util.List;

public class ApodAdapter extends ArrayAdapter<ApodWithStats> {

  private final OnClickListener listener;
  public ApodAdapter(@NonNull Context context, @NonNull List<ApodWithStats> objects,
      OnClickListener listener) {
    super(context, R.layout.item_apod, objects);
    this.listener = listener; // TODO Deal with a null listener.
  }

  @NonNull
  @Override
  public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    View view = convertView;
    if (view == null) {
      view = LayoutInflater.from(getContext()).inflate(R.layout.item_apod, parent, false);
    }

    ImageView thumbnail = view.findViewById(R.id.thumbnail);
    TextView title = view.findViewById(R.id.title);
    TextView date = view.findViewById(R.id.date);
    TextView access = view.findViewById(R.id.access);
    ApodWithStats apod = getItem(position);
    title.setText(apod.getApod().getTitle());
    date.setText(DateFormat.getMediumDateFormat(getContext()).format(apod.getApod().getDate()));
    String countQuantity = getContext().getResources()
        .getQuantityString(R.plurals.access_count, apod.getAccessCount());
    access.setText(getContext().getString(R.string.access_format,
        apod.getAccessCount(), // Argument one
        DateFormat.getMediumDateFormat(getContext()).format(apod.getLastAccess()), // Argument two
        countQuantity)); // Argument three
    if (apod.getApod().getMediaType() == MediaType.IMAGE) {
      Picasso.get().load(apod.getApod().getUrl()).into(thumbnail);
    } else {
      thumbnail.setImageResource(R.drawable.ic_slow_motion_video_black_24dp);
    }
    thumbnail.setContentDescription(apod.getApod().getTitle());
    view.setOnClickListener((v) -> listener.onClick(v, apod.getApod(), position));
    return view;
  }

  @FunctionalInterface
  public interface OnClickListener {

    void onClick(View view, Apod apod, int positition);
  }
}
