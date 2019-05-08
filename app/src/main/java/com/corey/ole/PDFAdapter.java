import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class PdfAdapter extends ArrayAdapter<Pdf> {
    Pdf doc;
    Activity pdfstuff;
    int layoutResourceId;
    ArrayList<Pdf> new = new ArrayList<Pdf>();

    public PdfAdapter(Activity pdfstuff, int layoutResourceId, ArrayList<Pdf> new) {
        super(pdfstuff, layoutResourceId, new);
        this.pdfstuff = pdfstuff;
        this.layoutResourceId = layoutResourceId;
        this.new = new;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        PdfHolder holder = null;

        if(row == null) {
            LayoutInflater inflater = LayoutInflater.from(pdfstuff);
            row = inflater.inflate(layoutResourceId,parent,false);

            holder = new PdfHolder();

            holder.textViewName = (TextView) row.findViewById(R.id.textViewName);
            holder.textViewUrl = (TextView) row.findViewById(R.id.textViewUrl);

            row.setTag(holder);
        } else {
            holder = (PdfHolder) row.getTag();
        }

        doc = new.get(position);
        holder.textViewName.setText(doc.getName());
        holder.textViewUrl.setText(doc.getUrl());
        return row;
    }


    class PdfHolder {
        TextView textViewName,textViewUrl;
    }

}