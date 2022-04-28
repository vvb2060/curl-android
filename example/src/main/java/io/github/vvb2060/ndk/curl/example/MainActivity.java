package io.github.vvb2060.ndk.curl.example;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends Activity {
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private String apkPath;
    private ScrollView scrollView;
    private TextView textView;

    @SuppressWarnings("SameParameterValue")
    private int dp2px(float dp) {
        float density = getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }

    private View buildView() {
        var rootView = new LinearLayout(this);
        rootView.setOrientation(LinearLayout.VERTICAL);
        rootView.setFitsSystemWindows(true);

        scrollView = new ScrollView(this);
        var scrollParams = new LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT, 1);
        rootView.addView(scrollView, scrollParams);

        textView = new TextView(this);
        var textParams = new ScrollView.LayoutParams(WRAP_CONTENT, WRAP_CONTENT);
        var dp8 = dp2px(8);
        textView.setPadding(dp8, dp8, dp8, dp8);
        textView.setTypeface(Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL));
        textView.setTextIsSelectable(true);
        scrollView.addView(textView, textParams);

        return rootView;
    }

    private void append(String string) {
        scrollView.post(() -> {
            textView.append(string);
            textView.append("\n");
            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
        });
    }

    private void startProcess(String... command) throws Exception {
        Process process = new ProcessBuilder(command).redirectErrorStream(true).start();
        InputStreamReader reader = new InputStreamReader(process.getInputStream());
        try (BufferedReader br = new BufferedReader(reader)) {
            String line = br.readLine();
            while (line != null) {
                append(line);
                line = br.readLine();
            }
        }
        append("[ exit " + process.waitFor() + " ]");
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void test32Bit() {
        if (Build.SUPPORTED_32_BIT_ABIS.length == 0) return;
        var path = apkPath + "!/lib/" + Build.SUPPORTED_32_BIT_ABIS[0] + "/libvvb2060.so";
        try {
            append("[ exec " + path + " ]");
            startProcess("linker", path);
        } catch (Exception e) {
            append(Log.getStackTraceString(e));
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void test64Bit() {
        if (Build.SUPPORTED_64_BIT_ABIS.length == 0) return;
        var path = apkPath + "!/lib/" + Build.SUPPORTED_64_BIT_ABIS[0] + "/libvvb2060.so";
        try {
            append("[ exec " + path + " ]");
            startProcess("linker64", path);
        } catch (Exception e) {
            append(Log.getStackTraceString(e));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(buildView());
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) return;
        apkPath = getApplicationInfo().sourceDir;
        executor.submit(this::test32Bit);
        append("\n");
        executor.submit(this::test64Bit);
    }
}
