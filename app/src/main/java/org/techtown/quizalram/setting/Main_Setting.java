package org.techtown.quizalram.setting;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import androidx.core.graphics.BitmapCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import org.techtown.quizalram.R;
import org.techtown.quizalram.db.AlarmDatabase;
import org.techtown.quizalram.main.MainActivity;
import org.techtown.quizalram.recycle.Alarm_List;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.zip.Inflater;

import static android.app.Activity.RESULT_OK;

public class Main_Setting extends Fragment {

    TextView menu1, menu2, menu3, menu4, first1, first2;
    TextView top_text_left, top_text_right, top_text_center;

    CheckBox check0, check1, check2;

    FrameLayout set_layout1;
    LinearLayout set_main_layout, first1_menu, first2_menu;

    View setting_top_layout, setting_base_layout;
    TextView[] top_views;
    TextView[] base_views;

    GridView list_color;
    String[] string_color;
    Setting_Adapter grid_adapter;

    ImageView top_image, base_image;
    TextView top_text, base_text;
    Button button;

    LinearLayout.LayoutParams params1, params2, params3, params4, params5;
    FrameLayout.LayoutParams params0_1, params0_2, params1_1, params1_2, params3_1;

    String base_background_color, top_background_color, base_text_color, top_text_color;

    int set_select_num = 0;

    Uri imageUri;

    Bitmap real_bitmap;
    String image_path;

    private static final int PICK_FROM_CAMERA = 0;
    private static final int PICK_FROM_ALBUM = 1;
    private static final int CROP_FROM_CAMERA = 2;

    private InterstitialAd mInterstitialAd;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.activity_setting, container, false);

        //광고
        mInterstitialAd = new InterstitialAd(getContext());

        mInterstitialAd.setAdUnitId("ca-app-pub-8631957304793435/8128839717");
//        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");  //테스트
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        image_path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/TempBg" + "/main_bg.jpg";

        set_main_layout = rootView.findViewById(R.id.set_main_layout);
        set_layout1 = rootView.findViewById(R.id.set_layout1);

        setting_top_layout = rootView.findViewById(R.id.setting_top_layout);
        setting_base_layout = rootView.findViewById(R.id.setting_base_layout);

        top_text = rootView.findViewById(R.id.top_textview);
        base_text = rootView.findViewById(R.id.base_textview);

        top_text_left = rootView.findViewById(R.id.top_text_left);
        top_text_right = rootView.findViewById(R.id.top_text_right);
        top_text_center = rootView.findViewById(R.id.top_text_center);

        first1 = rootView.findViewById(R.id.setting_first_menu_1);
        first2 = rootView.findViewById(R.id.setting_first_menu_2);
        first1_menu = rootView.findViewById(R.id.setting_menu1);
        first2_menu = rootView.findViewById(R.id.setting_menu2);

        menu1 = rootView.findViewById(R.id.setting_menu_1);
        menu2 = rootView.findViewById(R.id.setting_menu_2);
        menu3 = rootView.findViewById(R.id.setting_menu_3);
        menu4 = rootView.findViewById(R.id.setting_menu_4);

        check0 = rootView.findViewById(R.id.back_check0);
        check1 = rootView.findViewById(R.id.back_check1);
        check2 = rootView.findViewById(R.id.back_check2);

        list_color = rootView.findViewById(R.id.list_color);
        grid_adapter = new Setting_Adapter();
        string_color = new String[]{"#FF0000", "#DC143C", "#B22222", "#800000", "#8B0000", "#A52A2A", "#A0522D", "#8B4513", "#CD5C5C",
                "#BC8F8F", "#F08080", "#FA8072", "#E9967A", "#FF7F50", "#FF6347", "#F4A460", "#FFA07A", "#CD853F", "#D2691E", "#FF4500",
                "#FFA500", "#FF8C00", "#D2B48C", "#FFDAB9", "#FFE4C4", "#FFE4B5", "#FFDEAD", "#F5DEB3", "#DEB887", "#B8860B", "#DAA520",
                "#FFD700", "#FFFF00", "#FAFAD2", "#EEE8AA", "#F0E68C", "#BDB76B", "#7CFC00", "#ADFF2F", "#7FFF00", "#00FF00", "#32CD32",
                "#9ACD32", "#808000", "#6B8E23", "#556B2F", "#228B22", "#006400", "#008000", "#2E8B57", "#3CB371", "#8FBC8F", "#90EE90",
                "#98FB98", "#00FF7F", "#00FA9A", "#008080", "#008B8B", "#20B2AA", "#66CDAA", "#5F9EA0", "#4682B4", "#7FFFD4", "#B0E0E6",
                "#AFEEEE", "#ADD8E6", "#B0C4DE", "#87CEEB", "#87CEFA", "#48D1CC", "#40E0D0", "#00CED1", "#00FFFF", "#00FFFF", "#00BFFF",
                "#1E90FF", "#6495ED", "#4169E1", "#0000FF", "#0000CD", "#000080", "#00008B", "#191970", "#483D8B", "#6A5ACD", "#7B68EE",
                "#9370DB", "#9932CC", "#9400D3", "#8A2BE2", "#BA55D3", "#DDA0DD", "#E6E6FA", "#D8BFD8", "#DA70D6", "#EE82EE", "#4B0082",
                "#8B008B", "#800080", "#C71585", "#FF1493", "#FF00FF", "#FF00FF", "#FF69B4", "#DB7093", "#FFB6C1", "#FFC0CB", "#FFE4E1",
                "#FFEBCD", "#FFFFE0", "#FFF8DC", "#FAEBD7", "#FFEFD5", "#FFFACD", "#F5F5DC", "#FAF0E6", "#FDF5E6", "#E0FFFF", "#F0F8FF",
                "#F5F5F5", "#FFF0F5", "#FFFAF0", "#F5FFFA", "#F8F8FF", "#F0FFF0", "#FFF5EE", "#FFFFF0", "#F0FFFF", "#FFFAFA", "#FFFFFF",
                "#DCDCDC", "#D3D3D3", "#C0C0C0", "#A9A9A9", "#778899", "#708090", "#808080", "#696969", "#2F4F4F", "#000000"};

        for(int i = 0; i < string_color.length; i++){
            grid_adapter.addItem(new Set_List(string_color[i]));
        }

        list_color.setAdapter(grid_adapter);

        top_image = rootView.findViewById(R.id.top_imageview);
        base_image = rootView.findViewById(R.id.base_imageview);
//        button = rootView.findViewById(R.id.album_select);

        Log.d("1818", MainActivity.main_top_layout.getWidth()/3 + ", " + MainActivity.main_top_layout.getHeight()/3);
        params0_1 = new FrameLayout.LayoutParams(MainActivity.main_base_layout.getWidth()/3, MainActivity.main_base_layout.getHeight()/3);
        params0_2 = new FrameLayout.LayoutParams(MainActivity.main_top_layout.getWidth()/3, MainActivity.main_top_layout.getHeight()/3);
        params1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params1_1 = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        params1_2 = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params3 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 0);
        params3_1 = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 0);
        params4 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 200);
        params5 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0);

        top_views = new TextView[]{top_text_left, top_text_center, top_text_right};
        base_views = new TextView[]{menu1, menu2, menu3, menu4, first1, first2};
        setLayoutBack();


        top_text_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_layout1.setVisibility(View.INVISIBLE);
                set_main_layout.setVisibility(View.VISIBLE);

                setLayoutBack();
            }
        });

        top_text_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_layout1.setVisibility(View.INVISIBLE);
                set_main_layout.setVisibility(View.VISIBLE);
                if (set_select_num == 1){
                    Log.d("1818", "백그라운드컬러" + base_background_color);
                    if (base_background_color == null){
                        modityBase_back2(real_bitmap);
                    } else {
                        modityBase_back(base_background_color);
                    }
                } else if (set_select_num == 2) {
                    modityTop_back(top_background_color);
                } else if (set_select_num == 3) {
                    modityBase_text(base_text_color);
                } else if (set_select_num == 4) {
                    modityTop_text(top_text_color);
                }

                setLayoutBack();
                check0.setChecked(false);
                check1.setChecked(false);
                check2.setChecked(false);

                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }

            }
        });

        first1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (first1_menu.getHeight() != 0){
                    first1_menu.setLayoutParams(params5);
                } else {
                    first1_menu.setLayoutParams(params2);
                }
            }
        });

        first2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (first2_menu.getHeight() != 0){
                    first2_menu.setLayoutParams(params5);
                } else {
                    first2_menu.setLayoutParams(params2);
                }
            }
        });

        check0.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    check1.setChecked(false);
                    check2.setChecked(false);

                    Drawable drawable = getResources().getDrawable(R.drawable.shadow_back);

                    if (set_select_num == 1){
                        drawable.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC);
                        base_background_color = "#FFFFFF";
                        base_image.setImageDrawable(drawable);
                    } else if (set_select_num == 2){
                        drawable.setColorFilter(Color.parseColor("#F15F5F"), PorterDuff.Mode.SRC);
                        top_background_color = "#F15F5F";
                        top_image.setImageDrawable(drawable);
                    } else if (set_select_num == 3){
                        drawable.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC);
                        base_text_color = "#000000";
                        base_text.setTextColor(Color.parseColor(base_text_color));
                    } else if (set_select_num == 4){
                        drawable.setColorFilter(Color.parseColor("#FFFFFF"), PorterDuff.Mode.SRC);
                        top_text_color = "#000000";
                        top_text.setTextColor(Color.parseColor(top_text_color));
                    }
                }
            }
        });


        check1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    check0.setChecked(false);
                    check2.setChecked(false);

//                    if (set_select_num == 1){
//                        base_background_color = "#FFFFFF";
//                    } else if (set_select_num == 2){
//                        top_background_color = "#F15F5F";
//                    } else if (set_select_num == 3){
//                        base_text_color = "#000000";
//                    } else if (set_select_num == 4){
//                        top_text_color = "#000000";
//                    }

                    list_color.setLayoutParams(params4);
                } else {
                    if (!check2.isChecked()){
                        check0.setChecked(true);
                    }
                    list_color.setLayoutParams(params3);
                }
            }
        });

        check2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    check0.setChecked(false);
                    check1.setChecked(false);

                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
                    startActivityForResult(intent, PICK_FROM_ALBUM);

                } else {
                    if (!check1.isChecked()){
                        check0.setChecked(true);
                    }
                }
            }
        });

        menu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_select_num = 1;

                menu_click();

                check2.setLayoutParams(params1);

            }
        });

        menu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_select_num = 2;

                menu_click();

                check2.setLayoutParams(params3);

            }
        });

        menu3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_select_num = 3;

                menu_click();

                check2.setLayoutParams(params3);
            }
        });

        menu4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                set_select_num = 4;

                menu_click();

                check2.setLayoutParams(params3);
            }
        });

        list_color.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Drawable drawable = getResources().getDrawable(R.drawable.shadow_back);
                if (set_select_num == 1){
                    real_bitmap = null;
                    base_background_color = grid_adapter.getItem(position).getStringText();
                    drawable.setColorFilter(Color.parseColor(base_background_color), PorterDuff.Mode.SRC);
                    base_image.setImageDrawable(drawable);
                } else if (set_select_num == 2){
                    top_background_color = grid_adapter.getItem(position).getStringText();
                    drawable.setColorFilter(Color.parseColor(top_background_color), PorterDuff.Mode.SRC);
                    top_image.setImageDrawable(drawable);
                } else if (set_select_num == 3){
                    base_text_color = grid_adapter.getItem(position).getStringText();
                    base_text.setTextColor(Color.parseColor(base_text_color));
                } else if (set_select_num == 4){
                    top_text_color = grid_adapter.getItem(position).getStringText();
                    top_text.setTextColor(Color.parseColor(top_text_color));
                }


            }
        });

        return rootView;
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//
////        Bitmap image_bitmap  = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());   // 이미지 데이터를 비트맵으로 받아온다.
////                    real_bitmap = image_bitmap;
////                    base_image.setImageBitmap(image_bitmap);  //배치해놓은 ImageView에 set
//
//        if(resultCode != RESULT_OK) {return;}
//
//        switch(requestCode){
//            case PICK_FROM_ALBUM:
//
//                imageUri = data.getData();
//
//                Intent intent = new Intent("com.android.camera.action.CROP");
//                intent.setDataAndType(imageUri, "image/*");
//
//                intent.putExtra("outputX", 300); // CROP한 이미지의 x축 크기
//                intent.putExtra("outputY", 400); // CROP한 이미지의 y축 크기
//                intent.putExtra("aspectX", 3); // CROP 박스의 X축 비율
//                intent.putExtra("aspectY", 4); // CROP 박스의 Y축 비율
//                intent.putExtra("scale", true);
//                intent.putExtra("return-data", true); //번들로 값을 받으려면 이게 필요한데 용량 제한이 있음
//
//                startActivityForResult(intent, CROP_FROM_CAMERA);
//                Log.d("1818", "호출 00");
//
//                break;
//
//            case CROP_FROM_CAMERA:
//
//                // CROP 된 이후의 이미지를 넘겨 받음
//                final Bundle extras = data.getExtras();
//
//                if(extras != null) {
//                    Bitmap photo = extras.getParcelable("data"); // CROP된 BITMAP
//                    real_bitmap = photo;
//                    base_image.setImageBitmap(photo);
//                }
//
//                // 임시 파일 삭제
//
//                File f = new File(imageUri.getPath());
//                Log.d("1818", "Uri 존재?" + imageUri);
//                if(f.exists()) {
//                    f.delete();
//                    Log.d("1818", "호출 3");
//                }
//                Log.d("1818", "호출 4");
//
//                break;
//        }
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case PICK_FROM_ALBUM:
                //기존 갤러리에서 선택한 이미지 파일
                Uri getImagePath = data.getData();

                Log.d("1818", "호출1 : " + getImagePath);
                File oriFile = getImageFile(getImagePath);

                //이미지 편집을 위해 선택한 이미지를 저장할 파일
                File copyFile = createImageFile();
                imageUri = FileProvider.getUriForFile(getContext(), getActivity().getPackageName(), copyFile); //Uri.fromUri()로 가져오면 file://로 되서 보안상 에러뜸
                Log.d("1818", "호출2 : " + imageUri);

                //이미지 복사(이미지 편집시 원본 이미지가 변형되는것을 방지하기 위함)
                createTempFile(copyFile, oriFile);
                Log.d("1818", "호출3 : " + createTempFile(copyFile, oriFile));

                //이미지 편집 호출
                cropImage();

                break;
//
//            case REQUEST_TAKE_PHOTO:
//                String photoPath = imageUri.getPath();
//                //이미지를 불러올때 고용량의 경우 OutOfMemory가 발생할 수 있으므로
//                //이미지 크기를 줄여서 호출함
//                BitmapFactory.Options options = new BitmapFactory.Options();
//                options.inSampleSize = 4;
//                Bitmap imageBitmap = BitmapFactory.decodeFile(photoPath, options);
//
//                try{
//                    // 기본 카메라 모듈을 이용해 촬영할 경우 가끔씩 이미지가
//                    // 회전되어 출력되는 경우가 존재하여
//                    // 이미지를 상황에 맞게 회전시킨다
//                    ExifInterface exif = new ExifInterface(photoPath);
//                    int exifOrientation = exif.getAttributeInt(
//                            ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
//                    int exifDegree = exifOrientationToDegrees(exifOrientation);
//
//                    //회전된 이미지를 다시 회전시켜 정상 출력
//                    imageBitmap = rotate(imageBitmap, exifDegree);
//
//                    //회전시킨 이미지를 저장
//                    saveExifFile(imageBitmap, photoPath);
//
//                    //비트맵 메모리 반환
//                    imageBitmap.recycle();
//                }catch (IOException e){
//                    e.getStackTrace();
//                }
//
//                //이미지 편집 호출
//                cropImage();
//
//                break;

            case CROP_FROM_CAMERA:

                Log.d("1818", "호출5 : 열림");

                //편집된 이미지의 경로 취득
//                Uri imageUri2 = data.getData();
//                String cropImagePath = imageUri2.getPath();
////                String cropImagePath = full_path.substring(5, full_path.length());
//                Log.d("1818", "호출4 : " + cropImagePath);
//
//                String photo_path = cropImagePath.substring(8, cropImagePath.length());

                //이미지 정보 취득
                Bitmap photo = BitmapFactory.decodeFile(image_path);
                base_image.setImageBitmap(photo);
                real_bitmap = photo;
                base_background_color = null;

                break;

        }
    }

//    private Uri createImageFile(){
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//        String imageFileName = "JPEG_" + timeStamp + ".jpg";
//        //저장 위치는 Android/data/앱패키지/picture/
//        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
////        File storageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath());
//
//
////        Uri uri = Uri.fromFile(new File(storageDir, imageFileName));
//        Uri uri = FileProvider.getUriForFile(getContext(), "org.techtown.quizalram.fileprovider", new File(storageDir, imageFileName));
//        return uri;
//    }

    private File createImageFile(){
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + ".jpg";

        //storageDir은 저장 폴더
        File storageDir = new File(Environment.getExternalStorageDirectory() + "/Pictures");
        File imageFile = null;

        if (!storageDir.exists()){ //storageDir이 없으면
            storageDir.mkdirs(); //폴더 생성하기
        }

        imageFile = new File(storageDir, imageFileName); //imageFile은 storageDir폴더에 imageFileName 이름의 파일 생성하기

        image_path = imageFile.getAbsolutePath();
        Log.d("1818", "크리에이트" + image_path);
        return imageFile;
    }

    /**
     * 선택된 uri의 사진 Path를 가져온다.
     * uri 가 null 경우 마지막에 저장된 사진을 가져온다.
     * @param uri
     * @return
     */
    public File getImageFile(Uri uri){
        String[] projection = { MediaStore.Images.Media.DATA };

        if(uri == null){
            uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }

        Cursor mCursor = getActivity().getContentResolver().query(uri, projection, null, null,
                MediaStore.Images.Media.DATE_MODIFIED + " desc");

        if(mCursor == null || mCursor.getCount() < 1){
            return null;
        }

        int idxColumn = mCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        mCursor.moveToFirst();

        String path = mCursor.getString(idxColumn);

        if(mCursor != null){
            mCursor.close();
            mCursor = null;
        }

        return new File(path);
    }

    /**
     * 파일 복사
     * @param oriFile : 복사할 File
     * @param copyFile : 복사될 File
     * @return
     */
    public boolean createTempFile(File copyFile, File oriFile) {
        boolean result;
        try {
            InputStream inputStream = new FileInputStream(oriFile);
            OutputStream outputStream = new FileOutputStream(copyFile);

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, bytesRead);
            }
            inputStream.close();
            outputStream.close();
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

//    public boolean copyFile(File srcFile, File destFile) {
//        boolean result;
//        try {
//            InputStream in = new FileInputStream(srcFile);
//            try {
//                result = copyToFile(in, destFile);
//            } finally {
//                in.close();
//            }
//        } catch (IOException e) {
//            Log.d("1818" , " 복사오류");
//            e.printStackTrace();
//            result = false;
//        }
//        return result;
//    }
//
//    /**
//     * Copy data from a source stream to destFile.
//     * Return true if succeed, return false if failed.
//     */
//    private boolean copyToFile(InputStream inputStream, File destFile) {
//        try {
//            OutputStream out = new FileOutputStream(destFile);
//            try {
//                byte[] buffer = new byte[4096];
//                int bytesRead;
//                while ((bytesRead = inputStream.read(buffer)) >= 0) {
//                    out.write(buffer, 0, bytesRead);
//                }
//            } finally {
//                out.close();
//            }
//            return true;
//        } catch (IOException e) {
//            return false;
//        }
//    }

    /**
     * 이미지 편집
     */
    private void cropImage(){
        Intent cropPictureIntent = new Intent("com.android.camera.action.CROP");
        cropPictureIntent.setDataAndType(imageUri, "image/*");

        cropPictureIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION); //이거 꼭 추가해줘야함

        // Crop한 이미지를 저장할 Path
        cropPictureIntent.putExtra("output", imageUri);

        //이미지 편집 크기 제한
        //crop box X and Y rate
        cropPictureIntent.putExtra("aspectX", 3);
        cropPictureIntent.putExtra("aspectY", 4);
        cropPictureIntent.putExtra("scale", true);

        startActivityForResult(cropPictureIntent, CROP_FROM_CAMERA);
        Log.d("1818", "호출4 : 보냄");

    }



    public byte[] getByteToBitmap(Bitmap bitmap){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] data = stream.toByteArray();

        return data;
    }

    public void setLayoutColor(){
        if (!MainActivity.main_base_background.equals("")){
            Drawable drawable = getResources().getDrawable(R.drawable.shadow_back);
            drawable.setColorFilter(Color.parseColor(MainActivity.main_base_background), PorterDuff.Mode.SRC);
            base_background_color = MainActivity.main_base_background;
            base_image.setImageDrawable(drawable);
        } else {
            base_image.setImageBitmap(getBitmapToByte(MainActivity.main_base_bitmap));
        }
        if (MainActivity.main_top_background != null){
            Drawable drawable = getResources().getDrawable(R.drawable.shadow_back);
            drawable.setColorFilter(Color.parseColor(MainActivity.main_top_background), PorterDuff.Mode.SRC);
            top_background_color = MainActivity.main_top_background;
            top_image.setImageDrawable(drawable);
        }

        if (MainActivity.main_base_textcolor != null){
            base_text_color = MainActivity.main_base_textcolor;
            base_text.setTextColor(Color.parseColor(base_text_color));
        }

        if (MainActivity.main_top_textcolor != null){
            top_text_color = MainActivity.main_top_textcolor;
            top_text.setTextColor(Color.parseColor(top_text_color));
        }
    }

    public void menu_click(){
        top_text_left.setLayoutParams(params1);
        top_text_right.setLayoutParams(params1);

        set_main_layout.setVisibility(View.INVISIBLE);
        set_layout1.setVisibility(View.VISIBLE);

        base_image.setLayoutParams(params0_1);
        top_image.setLayoutParams(params0_2);

        if (set_select_num == 1 | set_select_num == 2){
            top_text.setLayoutParams(params3_1);
            base_text.setLayoutParams(params3_1);
        } else if (set_select_num == 3 | set_select_num == 4){
            top_text.setLayoutParams(params1_1);
            base_text.setLayoutParams(params1_1);
            top_text.setGravity(Gravity.CENTER);
            base_text.setGravity(Gravity.CENTER);
        }

        switch (set_select_num){
            case 1 :
                top_text_center.setText(R.string.string47);
                break;
            case 2 :
                top_text_center.setText(R.string.string48);
                break;
            case 3 :
                top_text_center.setText(R.string.string49);
                break;
            case 4 :
                top_text_center.setText(R.string.string50);
                break;
        }

        setLayoutColor();

    }

    public void setLayoutBack(){

        top_text_left.setLayoutParams(params3);
        top_text_right.setLayoutParams(params3);

        top_text_center.setText(R.string.string39);

        if (!MainActivity.main_base_background.equals("")){
            if (MainActivity.main_base_background.length() < 8){
                setting_base_layout.setBackgroundColor(Color.parseColor(MainActivity.main_base_background));
                Log.d("1818", "설정 왜 안돼 ㅅㅂ2");
            }
        } else {
            Log.d("1818", "설정 왜 안돼 ㅅㅂ");
            if (MainActivity.main_base_bitmap != null){

                BitmapDrawable drawable = new BitmapDrawable(getResources(), getBitmapToByte(MainActivity.main_base_bitmap));
                setting_base_layout.setBackgroundDrawable(drawable);

                Log.d("1818", "설정 왜 안돼 ㅅㅂ1");
            }
        }

        if (!MainActivity.main_top_background.equals("")){
            setting_top_layout.setBackgroundColor(Color.parseColor(MainActivity.main_top_background));
        }

        if (!MainActivity.main_base_textcolor.equals("")){
            for (int i = 0; i < base_views.length; i++){
                TextView view = base_views[i];
                view.setTextColor(Color.parseColor(MainActivity.main_base_textcolor));
            }
        }

        if (!MainActivity.main_top_textcolor.equals("")){
            for (int i = 0; i < top_views.length; i++){
                TextView view = top_views[i];
                view.setTextColor(Color.parseColor(MainActivity.main_top_textcolor));
            }
        }
    }

    public static int loadBackgroundData(){
        AlarmDatabase.println("loadBackgroundData called.");

        String sql = "select _id, TOP_BACKGROUND, BASE_BACKGROUND, TOP_TEXTCOLOR, BASE_TEXTCOLOR, BASE_BACKGROUND_BITMAP from " + AlarmDatabase.TABLE_BACK + " order by _id asc"; //내림차순 desc

        int recordCount = -1;
        AlarmDatabase database = AlarmDatabase.getInstance(MainActivity.context);


        if (database != null){
            Cursor outCursor = database.rawQuery(sql);

            recordCount = outCursor.getCount();
            AlarmDatabase.println("record count : " + recordCount + "\n");

            for (int i = 0; i < recordCount; i++){
                outCursor.moveToNext();

                int _id = outCursor.getInt(0);
                String top_back = outCursor.getString(1);
                String base_back = outCursor.getString(2);
                String top_text = outCursor.getString(3);
                String base_text = outCursor.getString(4);
                byte[] base_bitmap = outCursor.getBlob(5);

                MainActivity.main_top_background = top_back;
                MainActivity.main_base_background = base_back;
                MainActivity.main_top_textcolor = top_text;
                MainActivity.main_base_textcolor = base_text;
                MainActivity.main_base_bitmap = base_bitmap;

                Log.d("1818", top_back + ", " + base_back + ", " + top_text + ", " + base_text + ", "
                        + base_bitmap + ", " + base_bitmap.length + ", " + base_bitmap[0]);

                if (base_bitmap[0] == 0){
                    Log.d("1818", "null임");
                }
            }
            outCursor.close();
        }
        return recordCount;
    }

    public static void firstSetBackground(){

        String sql = "select _id, TOP_BACKGROUND, BASE_BACKGROUND, TOP_TEXTCOLOR, BASE_TEXTCOLOR, BASE_BACKGROUND_BITMAP from " + AlarmDatabase.TABLE_BACK + " order by _id desc";

        AlarmDatabase database = AlarmDatabase.getInstance(MainActivity.context);
        Cursor outCursor = database.rawQuery(sql);

        if (outCursor.getCount() == 0){
                String sql2 = "insert into " + AlarmDatabase.TABLE_BACK +
                        "(TOP_BACKGROUND, BASE_BACKGROUND, TOP_TEXTCOLOR, BASE_TEXTCOLOR, BASE_BACKGROUND_BITMAP) values("
                        + "'" + "#8D8D8D" + "', " + "'" + "#FFFFFF" + "', " + "'" + "#000000" + "', " + "'" + "#000000" + "', " + "'" + "')";

            AlarmDatabase.println("saveQuestion : " + sql2);
            database.execSQL(sql2);

            Log.d("1818", sql2);
        }

        loadBackgroundData();

    }

    public static void modityTop_back(String top_back){
        String sql = "update " + AlarmDatabase.TABLE_BACK + " set "
                + " TOP_BACKGROUND = '" + top_back + "'";

        AlarmDatabase database = AlarmDatabase.getInstance(MainActivity.context);
        database.rawQuery(sql);

        loadBackgroundData();

    }

    public static void modityBase_back(String base_back){
        String sql = "update " + AlarmDatabase.TABLE_BACK + " set "
                + " BASE_BACKGROUND = '" + base_back + "'"
                + " ,BASE_BACKGROUND_BITMAP = '" + "'";

        AlarmDatabase database = AlarmDatabase.getInstance(MainActivity.context);
        database.rawQuery(sql);

        loadBackgroundData();
    }

    public void modityBase_back2(Bitmap bitmap){

        byte[] data = getByteToBitmap(bitmap);

        Log.d("1818", "비트맵 배열" + data);
//        String sql = "update " + AlarmDatabase.TABLE_BACK + " set "
//                + " BASE_BACKGROUND = '" + "'"
//                + " BASE_BACKGROUND_BITMAP = '" + "?" + "'";
//
//        AlarmDatabase database = AlarmDatabase.getInstance(MainActivity.context);
//
//        database.rawQuery(sql);
//
//        loadBackgroundData();

        AlarmDatabase.addEntry(data);
        loadBackgroundData();

    }

    public static void modityTop_text(String top_text){
        String sql = "update " + AlarmDatabase.TABLE_BACK + " set "
                + " TOP_TEXTCOLOR = '" + top_text + "'";

        AlarmDatabase database = AlarmDatabase.getInstance(MainActivity.context);
        database.rawQuery(sql);

        loadBackgroundData();

    }

    public static void modityBase_text(String base_text){
        String sql = "update " + AlarmDatabase.TABLE_BACK + " set "
                + " BASE_TEXTCOLOR = '" + base_text + "'";

        AlarmDatabase database = AlarmDatabase.getInstance(MainActivity.context);
        database.rawQuery(sql);

        loadBackgroundData();
    }

    public static Bitmap getBitmapToByte(byte[] bytes){
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        return bitmap;
    }
}
