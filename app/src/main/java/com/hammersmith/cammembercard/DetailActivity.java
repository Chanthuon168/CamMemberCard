package com.hammersmith.cammembercard;

import android.content.Context;
import android.net.Uri;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hammersmith.cammembercard.adapter.AdapterDiscount;
import com.hammersmith.cammembercard.fragment.FragmentAlbum;
import com.hammersmith.cammembercard.fragment.FragmentCondition;
import com.hammersmith.cammembercard.fragment.FragmentMemberCard;
import com.hammersmith.cammembercard.fragment.FragmentOutlet;
import com.hammersmith.cammembercard.fragment.FragmentReview;
import com.hammersmith.cammembercard.model.Discount;
import com.joanzapata.iconify.widget.IconTextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.hammersmith.cammembercard.R.id.image_card;

public class DetailActivity extends AppCompatActivity {
    private Context context;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private FloatingActionButton fab;
    private int[] tabIcons = {
            R.drawable.folder_image,
            R.drawable.file_document,
            R.drawable.home,
            R.drawable.comment_processing
    };
    private String strExpDate, strName, strImgCard, strLogo;
    private TextView expDate, name;
    private ImageView image;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        expDate = (TextView) findViewById(R.id.expDate);
        name = (TextView) findViewById(R.id.name);
        image = (ImageView) findViewById(R.id.image);
        if (getIntent() != null) {
            id = getIntent().getIntExtra("id", 0);
            strExpDate = getIntent().getStringExtra("exp");
            strName = getIntent().getStringExtra("name");
            strImgCard = getIntent().getStringExtra("image_card");
            strLogo = getIntent().getStringExtra("logo");
            expDate.setText("EXP. " + strExpDate);
            name.setText(strName);
            Uri uri = Uri.parse(ApiClient.BASE_URL + strImgCard);
            context = image.getContext();
            Picasso.with(context).load(uri).into(image);
        }
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDiscount();
                fab.setImageDrawable(getResources().getDrawable(R.drawable.img_gift));
            }
        });
        findViewById(R.id.arrow_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        viewPager = (ViewPager) findViewById(R.id.pager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        dialogDiscount();
    }

    private void setupTabIcons() {
        tabLayout.getTabAt(0).setIcon(tabIcons[0]);
        tabLayout.getTabAt(1).setIcon(tabIcons[1]);
        tabLayout.getTabAt(2).setIcon(tabIcons[2]);
        tabLayout.getTabAt(3).setIcon(tabIcons[3]);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new FragmentAlbum(), "Albums");
        adapter.addFragment(new FragmentCondition(), "T & C");
        adapter.addFragment(new FragmentOutlet(), "Outlet");
        adapter.addFragment(new FragmentReview(), "Reviews");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
//            return null;
        }
    }

    private void dialogDiscount() {
        LayoutInflater factory = LayoutInflater.from(this);
        final View viewDialog = factory.inflate(R.layout.dialog_discount, null);
        final AlertDialog dialog = new AlertDialog.Builder(this).create();
        dialog.setView(viewDialog);
        viewDialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        ImageView image = (ImageView) viewDialog.findViewById(R.id.image_card);
        TextView name = (TextView) viewDialog.findViewById(R.id.name);
        Uri uri = Uri.parse(ApiClient.BASE_URL + strLogo);
        context = image.getContext();
        Picasso.with(context).load(uri).into(image);
        name.setText(strName);
        List<Discount> discounts = new ArrayList<>();
        RecyclerView recyclerView = (RecyclerView) viewDialog.findViewById(R.id.recyclerView);
        AdapterDiscount adapterDiscount = new AdapterDiscount(DetailActivity.this, discounts);
        GridLayoutManager layoutManager = new GridLayoutManager(DetailActivity.this, 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapterDiscount);
        dialog.show();
    }

    public int getMyData() {
        return id;
    }
}
