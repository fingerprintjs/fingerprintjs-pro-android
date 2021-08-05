package com.fingerprintjs.android.fpjs_pro_demo


import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.fingerprintjs.android.fpjs_pro.FPJSProFactory


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val tv = findViewById<TextView>(R.id.android_result)
        val aspClient = FPJSProFactory(
            applicationContext
        ).getInstance(
            "https://api.fpjs.io",
            "kDIPlabQCHvWcgMHSyei"
        )

        aspClient.getVisitorId {
            tv.text = it
        }
    }
}