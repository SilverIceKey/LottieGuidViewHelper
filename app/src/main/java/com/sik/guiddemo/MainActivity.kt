package com.sik.guiddemo

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.MotionEvent
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.blankj.utilcode.util.PermissionUtils
import com.blankj.utilcode.util.PermissionUtils.SimpleCallback
import com.blankj.utilcode.util.ToastUtils
import com.sik.guid.GuidViewHelper
import com.sik.guid.onFinish
import com.sik.guid.onNotClear
import com.sik.guid.setGuidDataList
import com.sik.guid.setParentView
import com.sik.guiddemo.utils.guid.FirstGuidData

class MainActivity : AppCompatActivity() {
    private lateinit var guidViewHelper: GuidViewHelper
    private lateinit var mainContent: ConstraintLayout
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainContent = findViewById(R.id.main_content)
        findViewById<TextView>(R.id.start).setOnClickListener {
            startAnimator()
        }
        if (!PermissionUtils.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            PermissionUtils.permission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .callback(object : SimpleCallback {
                    override fun onGranted() {
                        startAnimator()
                    }

                    override fun onDenied() {

                    }

                }).request()
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {
                    startAnimator()
                } else {
                    startActivity(Intent().apply {
                        action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                    })
                }
            } else {
                startAnimator()
            }
        }
    }

    private fun startAnimator() {
        guidViewHelper = GuidViewHelper(this)
            .setParentView(mainContent)
            .onFinish {
                ToastUtils.showShort("引导完成")
            }
            .onNotClear {
                ToastUtils.showShort("请根据动画完成引导")
            }
            .setGuidDataList(FirstGuidData())
        guidViewHelper.start()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        guidViewHelper.onTouchEvent(event)
        return super.onTouchEvent(event)
    }
}