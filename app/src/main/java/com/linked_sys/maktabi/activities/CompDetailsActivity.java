package com.linked_sys.maktabi.activities;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.Request;
import com.android.volley.error.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.linked_sys.maktabi.R;
import com.linked_sys.maktabi.adapters.CircleTransform;
import com.linked_sys.maktabi.core.CacheHelper;
import com.linked_sys.maktabi.models.CompReplies;
import com.linked_sys.maktabi.network.ApiCallback;
import com.linked_sys.maktabi.network.ApiEndPoints;
import com.linked_sys.maktabi.network.ApiHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class CompDetailsActivity extends BaseActivity {
    int compID, statusID;
    TextView compTitleTXT, compBodyTXT, compDateTXT;
    LinearLayout btnSendReply, placeholder;
    LinearLayout replyContainer;
    ArrayList<CompReplies> repliesList = new ArrayList<>();
    LayoutInflater inflater;
    String captainReply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        Bundle bundle = getIntent().getExtras();
        compID = bundle.getInt("ID");
        statusID = bundle.getInt("statusID");
        compTitleTXT = (TextView) findViewById(R.id.compTitleTxt);
        compBodyTXT = (TextView) findViewById(R.id.compContent);
        compDateTXT = (TextView) findViewById(R.id.compDateTxt);
        btnSendReply = (LinearLayout) findViewById(R.id.btnSendReply);
        replyContainer = (LinearLayout) findViewById(R.id.compReplysCotainer);
        placeholder = (LinearLayout) findViewById(R.id.no_data_placeholder);
        btnSendReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MaterialDialog.Builder(CompDetailsActivity.this)
                        .title("إضافة رد")
                        .input(null, captainReply, new MaterialDialog.InputCallback() {
                            @Override
                            public void onInput(MaterialDialog dialog, CharSequence input) {
                                try {
                                    String reply = URLEncoder.encode(String.valueOf(input), "utf-8");
                                    //POST REPLY
                                    postReply(reply);
                                    captainReply = String.valueOf(input);
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).show();
            }
        });
        getCompDetails();
    }

    @Override
    protected int getLayoutID() {
        return R.layout.activity_comp_details;
    }

    private void getCompDetails() {
        String url = ApiEndPoints.GET_CAPTAIN_COMPLAINTS_Details
                + "?ComplainID=" + String.valueOf(compID);
        ApiHelper api = new ApiHelper(this, url, Request.Method.GET, new ApiCallback() {
            @Override
            public void onSuccess(Object response) {
                JSONObject res = (JSONObject) response;
                JSONObject compObj = res.optJSONObject("complainDetail");
                compTitleTXT.setText(compObj.optString("Title"));
                compBodyTXT.setText(compObj.optString("Body"));
                SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd");
                Date newDate = null;
                try {
                    newDate = spf.parse(compObj.optString("PostDate"));
                    spf = new SimpleDateFormat("dd/MM/yyyy");
                    String date = spf.format(newDate);
                    compDateTXT.setText(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try {
                    JSONArray repliesArray = res.optJSONArray("ResponsesList");
                    repliesList.clear();
                    if (repliesArray.length() > 0) {
                        for (int i = 0; i < repliesArray.length(); i++) {
                            try {
                                JSONObject jsonObject = repliesArray.getJSONObject(i);
                                CompReplies replies = new CompReplies();
                                replies.setUserImage(jsonObject.optString("Image"));
                                replies.setUserName(jsonObject.optString("UserName"));
                                replies.setDate(jsonObject.optString("PostDate"));
                                replies.setReply(jsonObject.optString("Body"));
                                repliesList.add(replies);
                                View v = getCompReplies(inflater, replies, i);
                                replyContainer.addView(v);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        placeholder.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(VolleyError error) {

            }
        });
        api.executeRequest(true, false);
    }

    private View getCompReplies(LayoutInflater inflater, final CompReplies compReplies, final int currentIndex) {
        View view = inflater.inflate(R.layout.comp_reply_item, null);
        final ImageView profileImage = (ImageView) view.findViewById(R.id.icon_profile);
        TextView replyDate = (TextView) view.findViewById(R.id.timestamp);
        TextView senderName = (TextView) view.findViewById(R.id.from);
        TextView replyBody = (TextView) view.findViewById(R.id.txt_secondary);
        if (compReplies.getUserImage().equals("null")) {
            profileImage.setImageDrawable(getResources().getDrawable(R.drawable.avatar_placeholder));
        } else {
            Glide.with(this).load(ApiEndPoints.BASE_URL + compReplies.getUserImage())
                    .asBitmap()
                    .transform(new CircleTransform(this))
                    .into(new SimpleTarget<Bitmap>(300, 300) {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                            profileImage.setImageBitmap(resource);
                        }

                        @Override
                        public void onLoadFailed(Exception e, Drawable errorDrawable) {
                            super.onLoadFailed(e, errorDrawable);
                            profileImage.setImageDrawable(getResources().getDrawable(R.drawable.avatar_placeholder));
                        }
                    });
        }
        SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd");
        Date newDate = null;
        try {
            newDate = spf.parse(compReplies.getDate());
            spf = new SimpleDateFormat("dd/MM/yyyy");
            String date = spf.format(newDate);
            replyDate.setText(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        senderName.setText(compReplies.getUserName());
        replyBody.setText(compReplies.getReply());
        return view;
    }

    private void postReply(String reply) {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("ComplainID", String.valueOf(compID));
        map.put("Body", reply);
        map.put("UserID", String.valueOf(CacheHelper.getInstance().userData.get(session.KEY_USER_ID)));
        ApiHelper api = new ApiHelper(this, ApiEndPoints.POST_REPLY, Request.Method.POST, map, new ApiCallback() {
            @Override
            public void onSuccess(Object response) {
                Toast.makeText(CompDetailsActivity.this, "تم إرسال الرد", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(VolleyError error) {
                Toast.makeText(CompDetailsActivity.this, "خطــأ في الارسال", Toast.LENGTH_SHORT).show();
            }
        });
        api.executePostRequest(true);
    }
}
