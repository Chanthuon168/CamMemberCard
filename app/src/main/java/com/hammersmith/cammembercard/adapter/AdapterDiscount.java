package com.hammersmith.cammembercard.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.hammersmith.cammembercard.ApiClient;
import com.hammersmith.cammembercard.DetailActivity;
import com.hammersmith.cammembercard.PrefUtils;
import com.hammersmith.cammembercard.R;
import com.hammersmith.cammembercard.model.Discount;
import com.hammersmith.cammembercard.model.User;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Chan Thuon on 11/9/2016.
 */
public class AdapterDiscount extends RecyclerView.Adapter<AdapterDiscount.MyViewHolder> {
    private Activity activity;
    private List<Discount> discounts;
    private Context context;
    private DetailActivity activityDetail;
    private User user;

    public AdapterDiscount(Activity activity, List<Discount> discounts) {
        this.activity = activity;
        this.discounts = discounts;
        activityDetail = (DetailActivity) activity;
        user = PrefUtils.getCurrentUser(activity);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_discount, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        if (discounts.size() != 0) {
            if (discounts.get(position).getStatus().equals("valid")) {
                holder.layout.setClickable(true);
                holder.number.setText(discounts.get(position).getDiscount());
                holder.number.setTextColor(activity.getResources().getColor(R.color.red));
                holder.symbol.setTextColor(activity.getResources().getColor(R.color.red));
                holder.layout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activityDetail.closeDialog();
                        try {
                            dialogScan(discounts.get(position).getMerId(), ApiClient.BASE_URL + discounts.get(position).getPhoto(), discounts.get(position).getName(), discounts.get(position).getDiscount());
                        } catch (WriterException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
            holder.number.setText(discounts.get(position).getDiscount());
        }
    }

    @Override
    public int getItemCount() {
        return discounts.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView number, symbol;
        LinearLayout layout;

        public MyViewHolder(View itemView) {
            super(itemView);
            number = (TextView) itemView.findViewById(R.id.number);
            symbol = (TextView) itemView.findViewById(R.id.symbol);
            layout = (LinearLayout) itemView.findViewById(R.id.layout);
        }
    }

    private void dialogScan(int merId, String strImage, String strName, String strDiscount) throws WriterException {
        LayoutInflater factory = LayoutInflater.from(activity);
        final View viewDialog = factory.inflate(R.layout.dialog_scan, null);
        final AlertDialog dialog = new AlertDialog.Builder(activity).create();
        dialog.setView(viewDialog);
        viewDialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        JSONObject obj = new JSONObject();
        try {
            obj.put("id", user.getSocialLink());
            obj.put("mer_id", merId);
            obj.put("status", "valid");
            obj.put("discount", strDiscount);
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(activity, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        String data = obj.toString();
        Log.d("data", data);
        ImageView imgCode = (ImageView) viewDialog.findViewById(R.id.imgResult);
        ImageView profile = (ImageView) viewDialog.findViewById(R.id.profile);
        TextView name = (TextView) viewDialog.findViewById(R.id.name);
        TextView discount = (TextView) viewDialog.findViewById(R.id.discount);
        Uri uri = Uri.parse(strImage);
        context = profile.getContext();
        Picasso.with(context).load(uri).into(profile);
        name.setText(strName);
        discount.setText(strDiscount);
        imgCode.setImageBitmap(generateQrCode(data));
        dialog.show();
    }

    public static Bitmap generateQrCode(String myCodeText) throws WriterException {
        Bitmap qrImage = null;
        int size = 180;

        Map<EncodeHintType, Object> hintMap = new EnumMap<>(EncodeHintType.class);
        hintMap.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        hintMap.put(EncodeHintType.MARGIN, 1);
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        try {
            BitMatrix byteMatrix = qrCodeWriter.encode(myCodeText, BarcodeFormat.QR_CODE, size,
                    size, hintMap);
            int height = byteMatrix.getHeight();
            int width = byteMatrix.getWidth();
            qrImage = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    qrImage.setPixel(x, y, byteMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                }
            }
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return qrImage;
    }


}
