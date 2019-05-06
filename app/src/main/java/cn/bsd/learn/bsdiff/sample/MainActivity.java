package cn.bsd.learn.bsdiff.sample;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import java.io.File;

import cn.bsd.learn.bsdiff.sample.utils.UriParseUtils;

public class MainActivity extends AppCompatActivity {

    private MainActivity activity;

    // Used to load the 'native-lib' library on application startup.
    //用于在应用中程序启动时，加载本地Lib库
    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;

        // Example of a call to a native method
        TextView tv = findViewById(R.id.version);
        tv.setText(BuildConfig.VERSION_NAME);

        // 运行时权限申请
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.REQUEST_INSTALL_PACKAGES};
            if (checkSelfPermission(perms[0]) == PackageManager.PERMISSION_DENIED) {
                requestPermissions(perms, 200);
            }
        }
    }


    /**
     * 合成新的版本安装包
     *
     * @param oldApk 旧的安装包（目前手机上使用的版本），比如 V1.0
     * @param patch  差分包（服务器接口下载到手机储存）
     * @param output 合成后的新版本安装包输出/存储路径，如 V2.0
     */
    public native void bsPatch(String oldApk, String patch, String output);

    //点击事件
    public void bsdiff(View view) {
        //模拟从服务器接口下载 patch补丁包 到本地手机储存（不模拟网络环境）， 直接复制到SDCard
        new AsyncTask<Void, Void, File>() {

            //后台子线程做耗时操作
            @Override
            protected File doInBackground(Void... voids) {
                //获取手机上正在使用的apk（旧版本）
                String oldApk = getApplicationInfo().sourceDir;
                //补丁包路径
                String patch = new File(Environment.getExternalStorageDirectory(), "patch").getAbsolutePath();
                String output = createNewApk().getAbsolutePath();
                //JNI完成合成工作
                bsPatch(oldApk, patch, output);
                return new File(output);
            }

            //当耗时操作完成类，调用此方法
            @Override
            protected void onPostExecute(File file) {
                super.onPostExecute(file);
                //安装合成后的新版本apk（V2.0）
                UriParseUtils.installApk(activity, file);
            }
        }.execute();
    }

    //创建合成后的新版本apk（占坑）
    private File createNewApk() {
        File newApk = new File(Environment.getExternalStorageDirectory(), "bsdiff.apk");
        try {
            if (!newApk.exists()) {
                //创建文件在指定目录
                newApk.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newApk;
    }
}
