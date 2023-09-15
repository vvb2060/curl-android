package io.github.vvb2060.ndk.curl;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Process;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.zip.ZipFile;

public class MainActivity extends Activity {
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private String apkPath;
    private ScrollView scrollView;
    private TextView textView;
    private EditText editText;
    private Future<?> future;

    @SuppressWarnings("SameParameterValue")
    private int dp2px(float dp) {
        float density = getResources().getDisplayMetrics().density;
        return (int) (dp * density + 0.5f);
    }

    private View buildView() {
        var rootView = new LinearLayout(this);
        rootView.setOrientation(LinearLayout.VERTICAL);
        rootView.setFitsSystemWindows(true);

        editText = new EditText(this);
        var editParams = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
        editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        editText.setHorizontallyScrolling(false);
        editText.setOnEditorActionListener((v, actionId, event) -> {
            if (event == null) return false;
            if (event.getAction() == KeyEvent.ACTION_DOWN &&
                event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                var text = v.getText().toString();
                if (text.isEmpty()) return true;
                textView.setText("");
                future = executor.submit(() -> {
                    var st = new StringTokenizer(text);
                    var cmd = new ArrayList<String>();
                    while (st.hasMoreTokens()) {
                        cmd.add(st.nextToken());
                    }
                    execCurl(cmd);
                });
                return true;
            }
            return false;
        });
        editText.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus && future != null) {
                future.cancel(true);
            }
        });
        rootView.addView(editText, editParams);

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

    private void startProcess(List<String> command) throws Exception {
        var process = new ProcessBuilder(command).redirectErrorStream(true).start();
        var reader = new InputStreamReader(process.getInputStream());
        try (BufferedReader br = new BufferedReader(reader)) {
            String line = br.readLine();
            while (line != null) {
                append(line);
                if (Thread.interrupted()) {
                    process.destroy();
                    append("[ kill ]");
                    break;
                }
                line = br.readLine();
            }
        }
        append("[ exit " + process.waitFor() + " ]");
    }

    @TargetApi(Build.VERSION_CODES.Q)
    private void execCurlLinker(List<String> command) {
        var is64Bit = Process.is64Bit();
        var abi = is64Bit ? Build.SUPPORTED_64_BIT_ABIS[0] : Build.SUPPORTED_32_BIT_ABIS[0];
        var path = apkPath + "!/lib/" + abi + "/libcurl.so";
        try {
            append("[ exec " + path + " ]");
            var cmd = new ArrayList<String>();
            cmd.add(is64Bit ? "linker64" : "linker");
            cmd.add(path);
            cmd.addAll(command);
            startProcess(cmd);
        } catch (Exception e) {
            append(Log.getStackTraceString(e));
        }
    }

    private boolean copyCurl(File curl) {
        if (curl.canExecute()) return true;
        try (var apk = new ZipFile(apkPath)) {
            var so = apk.getEntry("lib/" + Build.SUPPORTED_ABIS[0] + "/libcurl.so");
            assert so != null;
            try (var in = apk.getInputStream(so); var out = new FileOutputStream(curl)) {
                var buffer = new byte[8192];
                for (var n = in.read(buffer); n >= 0; n = in.read(buffer))
                    out.write(buffer, 0, n);
            }
            return curl.setExecutable(true);
        } catch (IOException e) {
            append(Log.getStackTraceString(e));
            return false;
        }
    }

    private void execCurlFile(List<String> command) {
        var curl = new File(getCodeCacheDir(), "curl");
        if (!copyCurl(curl)) return;
        try {
            append("[ exec " + curl + " ]");
            var cmd = new ArrayList<String>();
            cmd.add(curl.toString());
            cmd.addAll(command);
            startProcess(cmd);
        } catch (Exception e) {
            append(Log.getStackTraceString(e));
        }
    }

    private void execCurl(List<String> command) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            execCurlLinker(command);
        } else {
            execCurlFile(command);
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(buildView());
        apkPath = getApplicationInfo().sourceDir;
        editText.setText("--http3-only https://www.cloudflare.com/cdn-cgi/trace");
        editText.dispatchKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.shutdownNow();
    }
}
