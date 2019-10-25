package com.example.dimensdemo;

import android.content.res.Configuration;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.File;

/**
 * 核心是根据设备相关信息去取不同的res文件夹，如上面的例子，是根据不同sw取不同的资源文件，文件夹命名如下：
 * values-mcc310-en-sw320dp-w720dp-h720dp-large-long-port-car-night-ldpi-notouch-keysexposed-nokeys-navexposed-nonav-v7
 * <p>
 * 属性是按优先级匹配先后排列出来的，各种含义：
 * values-mcc310(sim卡运营商)-en(语言)-sw320dp(屏幕最小宽度)-w720dp(屏幕最佳宽度)-h720dp(屏幕最佳高度)
 * -large(屏幕尺寸)-long(屏幕长短边模式)-port(当前屏幕横竖屏显示模式)-car(dock模式)-night(白天或夜晚)
 * -ldpi(屏幕最佳dpi)-notouch(触摸屏模类型)-keysexposed(键盘类型)-nokey(硬按键类型)-navexposed(方向键是否可用)-nonav(方向键类型)-v7(android版本)
 * <p>
 * 假设我是横屏，屏幕是1920x1080 android系统就会去解析values-1920x1080-port文件
 * <p>
 * <p>
 * https://juejin.im/post/5a01668a518825293b4fb96c
 * 一、对于这些常见values文件优先级依次递减如下：values-sw720 ，values-w720（values-h720，不常用），values-xdpi，values-1080*720
 * 二、关于dpi的读取先后顺序最佳dpi——最接近的dpi即未找到最佳dpi时，选择读取最接近自身dpi的资源文件夹进行读取，假如A手机屏幕为hdpi，资源文件夹下却没有
 * values-hdpi文件夹，那么他读取的优先级可以为xhdpi——mdpi——values——ldpi（系统判定values比values-ldpi更接近于hdpi）
 * 三、关于分辨率（如960x720）的读取先后顺序  最佳分辨率——向下读取的最接近的分辨率资源文件  假如res下现有两个values文件夹，一个为（values-960x720）另一个为
 * （values-900x600），现有一部山寨机屏幕分辨率并不标准，假定它为959x719， 那么当该部手机运行此程序时，所读取到values资源文件夹应为 （values-900x600），
 * 而不是与他更接近的但是比他分辨率要略高一点的（values-960x720），即向下读取与他最为接近的适配文件
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        boolean sdExist = android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment.getExternalStorageState());
        if (sdExist) {
            //sdcard/dimens 文件夹下
            String dir = android.os.Environment.getExternalStorageDirectory().toString();
            dir += "/dimens";
            String dbPath = dir + "/";
            DimensUtils.main(dbPath);
        }
        Configuration config = getResources().getConfiguration();
        int smallestScreenWidth = config.smallestScreenWidthDp;
        //test
        Log.e("tag", "smallestScreenWidth=" + smallestScreenWidth);
        //Log.e("tag", "w=" + config.screenWidthDp + ",height=" + config.screenHeightDp);
        //物理尺寸 values-sw540dp-xxhdpi values-sw540dp-car-mdpi
        //smallestScreenWidth=width/density
        //比如，1920x720 的屏幕 dpi=213,则 sm= 720/(213/160)=720/1.33125=540.8
        // 1280x480
        //命名规范 values-sw540dp-xxhdpi

        //  values- 540.8 l-m dpi

        //结论 系统匹配values文件夹依靠唯一参数 smallestScreenWidthDp，实际dimens里面的值如果是px为单位，可以按照最大物理尺寸相除，比如 1920/1280=1.5，按这个比例在DimensUtils里设置
        //原则上应该是匹配比sw低（比dpi高）的最接近的文件夹，不会匹配高的
        /*
         * 142
         * 对照表
         * xxhdpi   480(3)
         * xhdpi    320(2)
         * hdpi     240(1.5)
         * tvdpi    213(1.33)
         * mdpi     160(1)
         * ldpi     120(0.75)           142
         * */
        final TextView tv = findViewById(R.id.tv);
        tv.post(new Runnable() {
            @Override
            public void run() {
                int width = tv.getWidth();
                int height = tv.getHeight();
                //测试，注意 宽高不会超过最大值
                Log.e("tag", "width=" + width + ",height=" + height);
            }
        });
    }
}
