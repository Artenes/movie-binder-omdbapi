package moviebinder.io.github.artenes.moviebinder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by Artenes on 28/12/2015.
 */
public class AdapterMovieListing extends ArrayAdapter<Movie> {

    private final String TAG = AdapterMovieListing.class.getSimpleName();

    public AdapterMovieListing(Context context, int resource, List<Movie> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_movie, null);

        Movie movie = getItem(position);

        if (movie != null) {

            ImageView imgThumbnail = (ImageView) convertView.findViewById(R.id.imgThumbnail);
            TextView txvTitle      = (TextView) convertView.findViewById(R.id.txvTitle);
            TextView txvYear       = (TextView) convertView.findViewById(R.id.txvrRleaseYear);

            Glide.with(getContext())
                    .load(movie.poster)
                    .placeholder(R.mipmap.img_loading)
                    .error(R.mipmap.img_not_available)
                    .into(imgThumbnail);

            txvTitle.setText(movie.title);
            txvYear.setText(movie.year);

        }

        return convertView;

    }
}