package com.example.ggq.gaoguoqing20180828;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QQLoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "QQLoginActivity";
    private static final int CAMERA = 1;
    private static final int PICTURE = 2;
    @BindView(R.id.iv_icon)
    ImageView ivIcon;
    @BindView(R.id.tv_userName)
    TextView tvUserName;
    @BindView(R.id.tv_nick)
    TextView tvNick;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.btn_goods)
    Button btnGoods;
    @BindView(R.id.ll_linear)
    LinearLayout llLinear;
    private UMShareAPI umShareAPI;
    // 声明平移动画
    private TranslateAnimation animation;
    private PopupWindow popupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qqlogin);
        ButterKnife.bind(this);
        umShareAPI = UMShareAPI.get(this);
        if (Build.VERSION.SDK_INT >= 23) {
            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE, Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP, Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS, Manifest.permission.WRITE_APN_SETTINGS};
            ActivityCompat.requestPermissions(this, mPermissionList, 123);
        }
        btnGoods.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(QQLoginActivity.this, GoodsActivity.class));
            }
        });
    }

    @Override

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA && resultCode == RESULT_OK && data != null) {
            // 拍照
            Bundle bundle = data.getExtras();
            // 获取相机返回的数据，并转换为图片格式
            Bitmap bitmap = (Bitmap) bundle.get("data");
            // bitmap圆形裁剪
            //bitmap = BitmapUtils.zoom(bitmap, DensityUtil.dp2px(this, 62), DensityUtil.dp2px(this, 62));
            //Bitmap circleBitmap = BitmapUtils.circleBitmap(bitmap);
            Bitmap circleBitmap = getCircleBitmap(bitmap);
            //TODO 将图片上传到服务器的
            ivIcon.setImageBitmap(circleBitmap);
            // 将图片保存在本地
            try {
                saveImage(circleBitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }else if (requestCode == PICTURE && resultCode == RESULT_OK && data != null) {
            //图库
            Uri selectedImage = data.getData();
            //这里返回的uri情况就有点多了
            //**:在4.4.2之前返回的uri是:content://media/external/images/media/3951或者file://....在4.4.2返回的是content://com.android.providers.media.documents/document/image:3951或者
            //总结：uri的组成，eg:content://com.example.project:200/folder/subfolder/etc
            //content:--->"scheme"
            //com.example.project:200-->"host":"port"--->"authority"[主机地址+端口(省略) =authority]
            //folder/subfolder/etc-->"path" 路径部分
            //android各个不同的系统版本,对于获取外部存储上的资源，返回的Uri对象都可能各不一样,所以要保证无论是哪个系统版本都能正确获取到图片资源的话
            //就需要针对各种情况进行一个处理了
            String pathResult = getPath(selectedImage);

            Bitmap decodeFile = BitmapFactory.decodeFile(pathResult);
            //Bitmap zoomBitmap = BitmapUtils.zoom(decodeFile, DensityUtil.dp2px(this, 62), DensityUtil.dp2px(this, 62));
            // bitmap圆形裁剪p
            //Bitmap circleImage = BitmapUtils.circleBitmap(zoomBitmap);
            // 真实项目当中，是需要上传到服务器的..这步我们就不做了。
            Bitmap circleImage = getCircleBitmap(decodeFile);
            ivIcon.setImageBitmap(circleImage);
            try {
                // 保存图片到本地
                saveImage(circleImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    /**

     * 生成透明背景的圆形图片,！注意要生成透明背景的圆形，图片一定要png类型的，不能是jpg类型

     *

     * @param bitmap

     * @return

     */

    public Bitmap getCircleBitmap(Bitmap bitmap) {

        if (bitmap == null) {

            return null;

        }

        try {

            Bitmap circleBitmap = Bitmap.createBitmap(bitmap.getWidth(),

                    bitmap.getHeight(), Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(circleBitmap);

            final Paint paint = new Paint();

            final Rect rect = new Rect(0, 0, bitmap.getWidth(),

                    bitmap.getHeight());

            final RectF rectF = new RectF(new Rect(0, 0, bitmap.getWidth(),

                    bitmap.getHeight()));

            float roundPx = 0.0f;

            // 以较短的边为标准

            if (bitmap.getWidth() > bitmap.getHeight()) {

                roundPx = bitmap.getHeight() / 2.0f;

            } else {

                roundPx = bitmap.getWidth() / 2.0f;

            }

            paint.setAntiAlias(true);

            canvas.drawARGB(0, 0, 0, 0);

            paint.setColor(Color.WHITE);

            canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

            final Rect src = new Rect(0, 0, bitmap.getWidth(),

                    bitmap.getHeight());

            canvas.drawBitmap(bitmap, src, rect, paint);

            return circleBitmap;

        } catch (Exception e) {

            return bitmap;

        }

    }

    @OnClick({R.id.tv_login, R.id.iv_icon})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_login:
                login();
                break;
            case R.id.iv_icon:
                showPopupWindow();
                break;
        }
    }

    private void showPopupWindow() {
        View view = LayoutInflater.from(this).inflate(R.layout.popup_layout, null, false);
        TextView tv_album = view.findViewById(R.id.tv_album);
        TextView tv_take_photo = view.findViewById(R.id.tv_take_photo);
        TextView tv_cancle = view.findViewById(R.id.tv_cancle);
        popupWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                lighton();
            }
        });
        // 设置背景图片， 必须设置，不然动画没作用
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setFocusable(true);

        // 设置点击popupwindow外屏幕其它地方消失
        popupWindow.setOutsideTouchable(true);

        // 平移动画相对于手机屏幕的底部开始，X轴不变，Y轴从1变0
        animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0, Animation.RELATIVE_TO_PARENT, 0,
                Animation.RELATIVE_TO_PARENT, 1, Animation.RELATIVE_TO_PARENT, 0);
        animation.setInterpolator(new AccelerateInterpolator());
        animation.setDuration(200);

        tv_take_photo.setOnClickListener(this);
        tv_album.setOnClickListener(this);
        tv_cancle.setOnClickListener(this);

        // 设置popupWindow的显示位置，此处是在手机屏幕底部且水平居中的位置
        popupWindow.showAtLocation(QQLoginActivity.this.findViewById(R.id.iv_icon), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        view.startAnimation(animation);
        lightoff();
    }
    /**
     * 设置手机屏幕亮度变暗
     */
    private void lightoff() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.3f;
        getWindow().setAttributes(lp);
    }
    /**
     * 设置手机屏幕亮度显示正常
     */
    private void lighton() {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 1f;
        getWindow().setAttributes(lp);
    }

    private void login() {
        tvLogin.setVisibility(View.GONE);
        UMAuthListener authListener = new UMAuthListener() {

            /**

             * @desc 授权开始的回调

             * @param platform 平台名称

             */

            @Override

            public void onStart(SHARE_MEDIA platform) {

            }

            /**

             * @desc 授权成功的回调

             * @param platform 平台名称

             * @param action 行为序号，开发者用不上

             * @param data 用户资料返回

             */

            @Override

            public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {

                Toast.makeText(QQLoginActivity.this, "成功了", Toast.LENGTH_LONG).show();

                Toast.makeText(QQLoginActivity.this, "授权成功后的回调数据，用户信息：" + data, Toast.LENGTH_LONG).show();
                Log.d(TAG, "onComplete: " + data);

                String uid = data.get("uid");
                final String name = data.get("name");
                final String gender = data.get("gender");
                final String iconurl = data.get("iconurl");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Picasso.get().load(iconurl).into(ivIcon);
                        tvUserName.setText(gender);
                        tvNick.setText(name);
                    }
                });
            }

            /**

             * @desc 授权失败的回调

             * @param platform 平台名称

             * @param action 行为序号，开发者用不上

             * @param t 错误原因

             */

            @Override

            public void onError(SHARE_MEDIA platform, int action, Throwable t) {

                Toast.makeText(QQLoginActivity.this, "失败：" + t.getMessage(), Toast.LENGTH_LONG).show();

            }

            /**

             * @desc 授权取消的回调

             * @param platform 平台名称

             * @param action 行为序号，开发者用不上

             */

            @Override

            public void onCancel(SHARE_MEDIA platform, int action) {

                Toast.makeText(QQLoginActivity.this, "取消了", Toast.LENGTH_LONG).show();

            }

        };
        umShareAPI.getPlatformInfo(this, SHARE_MEDIA.QQ, authListener);
        llLinear.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_take_photo:
                // 打开系统拍照程
                Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(camera, CAMERA);
                popupWindow.dismiss();
                lighton();
                break;
            case R.id.tv_album:
                // 打开系统图库选择图片
                Intent picture = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(picture, PICTURE);
                popupWindow.dismiss();
                lighton();
                break;
            case R.id.tv_cancle:
                // 在点击之后设置popupwindow的销毁
                if (popupWindow.isShowing()) {
                    popupWindow.dismiss();
                    lighton();
                }
                break;
        }
    }
    // 将修改后的图片保存在本地存储中：storage/sdcard/Android/data/应用包名/files/xxx.png
    private void saveImage(Bitmap bitmap) throws FileNotFoundException {

        String path = this.getCacheDir() + "/tx.png";
        Log.e("TAG", "path = " + path);
        try {
            FileOutputStream fos = new FileOutputStream(path);
            // bitmap压缩(压缩格式、质量、压缩文件保存的位置)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

//        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//
//            File externalFilesDir = this.getExternalFilesDir(null);
//            File file = new File(externalFilesDir, "icon.png");
//            // 将Bitmap持久化
//            circleBitmap.compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(file));
//        }
    }
    // 根据系统相册选择的文件获取路径
    @SuppressLint("NewApi")
    private String getPath(Uri uri) {
        int sdkVersion = Build.VERSION.SDK_INT;
        // 高于4.4.2的版本
        if (sdkVersion >= 19) {
            Log.e("TAG", "uri auth: " + uri.getAuthority());
            if (isExternalStorageDocument(uri)) {
                String docId = DocumentsContract.getDocumentId(uri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));
                return getDataColumn(this, contentUri, null, null);
            } else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(this, contentUri, selection, selectionArgs);
            } else if (isMedia(uri)) {
                String[] proj = {MediaStore.Images.Media.DATA};
                Cursor actualimagecursor = this.managedQuery(uri, proj, null, null, null);
                int actual_image_column_index = actualimagecursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                actualimagecursor.moveToFirst();
                return actualimagecursor.getString(actual_image_column_index);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(this, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }
    private String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }
    private boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    public static boolean isMedia(Uri uri) {
        return "media".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}
