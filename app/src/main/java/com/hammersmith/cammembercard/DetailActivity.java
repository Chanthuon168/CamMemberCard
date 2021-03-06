package com.hammersmith.cammembercard;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
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
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.hammersmith.cammembercard.adapter.AdapterDiscount;
import com.hammersmith.cammembercard.fragment.FragmentAlbum;
import com.hammersmith.cammembercard.fragment.FragmentCondition;
import com.hammersmith.cammembercard.fragment.FragmentMemberCard;
import com.hammersmith.cammembercard.fragment.FragmentOutlet;
import com.hammersmith.cammembercard.fragment.FragmentReview;
import com.hammersmith.cammembercard.model.CollectionCard;
import com.hammersmith.cammembercard.model.Discount;
import com.hammersmith.cammembercard.model.MemberCard;
import com.hammersmith.cammembercard.model.User;
import com.joanzapata.iconify.widget.IconTextView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.hammersmith.cammembercard.R.id.image_card;
import static com.hammersmith.cammembercard.R.id.memId;
import static com.hammersmith.cammembercard.R.id.merName;

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
    private String strExpDate, strName, strImgCard, strLogo, strStatus;
    private TextView expDate, name;
    private ImageView image;
    private int id, mdId;
    private List<Discount> discounts = new ArrayList<>();
    private AlertDialog dialog;
    private CollectionCard card;
    private User user;
    private String strDiscount;
    private Discount discount;
    private String strRating;
    private MemberCard mMember;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_detail);
        expDate = (TextView) findViewById(R.id.expDate);
        name = (TextView) findViewById(R.id.name);
        image = (ImageView) findViewById(R.id.image);
        user = PrefUtils.getCurrentUser(getApplicationContext());

        Intent intent = getIntent();
        mMember = (MemberCard) intent.getSerializableExtra("member");
        if (mMember != null) {
            id = mMember.getId();
            mdId = mMember.getMerId();
            strExpDate = mMember.getExpDate();
            strName = mMember.getName();
            strImgCard = mMember.getImgCard();
            strLogo = mMember.getImgMerchandise();
            strStatus = mMember.getStatus();
            strRating = mMember.getRating();
            expDate.setText("EXP. " + strExpDate);
            name.setText(strName);
            Uri uri = Uri.parse(ApiClient.BASE_URL + strImgCard);
            context = image.getContext();
            Picasso.with(context).load(uri).into(image);
        }

        fab = (FloatingActionButton) findViewById(R.id.fab);
        if (strStatus.equals("checked")) {
            fab.setImageResource(R.drawable.new_gift);
//            dialogDiscount();
        } else {
            fab.setImageResource(R.drawable.new_card);
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (strStatus.equals("checked")) {
                    fab.setImageResource(R.drawable.new_gift);
                    dialogDiscount();
                } else {
                    card = new CollectionCard(user.getSocialLink(), id);
                    ApiInterface serviceAddCard = ApiClient.getClient().create(ApiInterface.class);
                    Call<CollectionCard> callCreate = serviceAddCard.addCard(card);
                    callCreate.enqueue(new Callback<CollectionCard>() {
                        @Override
                        public void onResponse(Call<CollectionCard> call, Response<CollectionCard> response) {
                            card = response.body();
                            if (card.getStatus().equals("checked")) {
                                fab.setImageResource(R.drawable.new_gift);
                                dialogDiscount();
                            }
                        }

                        @Override
                        public void onFailure(Call<CollectionCard> call, Throwable t) {

                        }
                    });
                }
            }
        });
        findViewById(R.id.arrow_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_righ);
            }
        });
        viewPager = (ViewPager) findViewById(R.id.pager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
        changeTabsFont(tabLayout, "fonts/century_gothic.ttf");

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
        int limit = (adapter.getCount() > 1 ? adapter.getCount() - 1 : 1);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(limit);
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
        dialog = new AlertDialog.Builder(this).create();
        dialog.setView(viewDialog);
        viewDialog.findViewById(R.id.close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        ImageView image = (ImageView) viewDialog.findViewById(R.id.image_card);
        TextView name = (TextView) viewDialog.findViewById(R.id.name);
        RatingBar ratingBar = (RatingBar) viewDialog.findViewById(R.id.ratingBar);
        ratingBar.setRating(Float.parseFloat(strRating));
        Uri uri = Uri.parse(ApiClient.BASE_URL + strLogo);
        context = image.getContext();
        Picasso.with(context).load(uri).into(image);
        name.setText(strName);
        final RecyclerView recyclerView = (RecyclerView) viewDialog.findViewById(R.id.recyclerView);
        GridLayoutManager layoutManager = new GridLayoutManager(DetailActivity.this, 3);
        recyclerView.setLayoutManager(layoutManager);
        ApiInterface serviceDiscount = ApiClient.getClient().create(ApiInterface.class);
        Call<List<Discount>> callDiscount = serviceDiscount.getDiscount(mdId, user.getSocialLink());
        callDiscount.enqueue(new Callback<List<Discount>>() {
            @Override
            public void onResponse(Call<List<Discount>> call, Response<List<Discount>> response) {
                discounts = response.body();
                viewDialog.findViewById(R.id.lProgress).setVisibility(View.GONE);
                AdapterDiscount adapterDiscount = new AdapterDiscount(DetailActivity.this, discounts);
                recyclerView.setAdapter(adapterDiscount);
                adapterDiscount.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<List<Discount>> call, Throwable t) {

            }
        });
        dialog.show();
    }

    public int getMdId() {
        return mdId;
    }

    public String getNameMerchandise() {
        return strName;
    }

    public int getMyData() {
        return id;
    }

    public void closeDialog() {
        dialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_righ);
    }
    public void changeTabsFont(TabLayout tabLayout, String fontPath) {
        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof AppCompatTextView) {
                    Typeface type = Typeface.createFromAsset(getAssets(), fontPath);
                    TextView viewChild = (TextView) tabViewChild;
                    viewChild.setTypeface(type);
                    viewChild.setAllCaps(false);
                }
            }
        }
    }
}
